package farmacia.database;

import farmacia.TipoOrdine;
import farmacia.exceptions.DBException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class OrdineDAO {
	private int id; // TODO: Da dove viene?
	private Date dataCreazione;
	private boolean ritirato;
	private TipoOrdine tipoOrdine;
	private Map<FarmacoDAO, Integer> ordineFarmaco;
	private int utente;

	private static final Logger logger = Logger.getLogger("OrdineDAO");

	public OrdineDAO() {}

	public OrdineDAO(Date dataCreazione, TipoOrdine tipoOrdine, int utente) {
		this.dataCreazione = dataCreazione;
		this.tipoOrdine = tipoOrdine;
		this.utente = utente;
		this.ordineFarmaco = new HashMap<>();
	}

	public OrdineDAO(int id) throws DBException {
		this.id = id;
		this.caricaDaDB(id);
	}

	public void aggiungiOrdineFarmaco(int idFarmaco, int quantita) throws DBException {
		ordineFarmaco.put(new FarmacoDAO(idFarmaco), quantita);
	}

	private void caricaDaDB(int id) throws DBException {
		String query = String.format("SELECT * FROM ordini WHERE id = %d;", id);

		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (rs.next()) {
				this.dataCreazione = rs.getDate("dataCreazione");
				this.ritirato = rs.getBoolean("ritirato");
				this.tipoOrdine = TipoOrdine.fromInt(rs.getInt("tipo"));
				this.utente = rs.getInt("utente");
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento di un ordine con id %d.%n%s",
				id, e.getMessage())
			);
			throw new DBException("Errore nel caricamento di un ordine");
		}

		query = String.format("SELECT * FROM ordini_farmaci WHERE id = %d", id);
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			while (rs.next()) {
				int idFarmaco = rs.getInt("farmaco");
				int quantita = rs.getInt("quantita");
				this.ordineFarmaco.put(new FarmacoDAO(idFarmaco), quantita);
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento di un ordine con id " +
				"%d.%n%s", id, e.getMessage()));
			throw new DBException("Errore nel caricamento di un ordine");
		}
	}

	private int salvaInDB() throws DBException {
		String query = String.format("INSERT INTO ordini (dataCreazione, ritirato, tipoOrdine, utente) " +
						"VALUES ('%s', %d, %d, %d);",
						dataCreazione, ritirato ? 1 : 0, tipoOrdine.ordinal(), utente
		);

		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'inserimento dell'ordine '%d' nel database.%n%s",
				this.id, e.getMessage())
			);
			throw new DBException(String.format("Errore nel salvataggio dell'ordine '%d'", this.id));
		}

		query = "INSERT INTO ordini_farmaci (ordine, farmaco, quantita) " +
				"VALUES (%d, %d, %d);";
		for (Map.Entry<FarmacoDAO, Integer> item: ordineFarmaco.entrySet()) {
			try {
				rs = DBManager.getInstance().executeQuery(String.format(query, this.id, item.getKey().getId(), item.getValue()));
			} catch (ClassNotFoundException | SQLException e) {
				logger.warning("Errore durante l'inserimento dell'ordine");
			}
		}

		return rs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public boolean isRitirato() {
		return ritirato;
	}

	public void setRitirato(boolean ritirato) {
		this.ritirato = ritirato;
	}

	public TipoOrdine getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(TipoOrdine tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public Map<FarmacoDAO, Integer> getOrdineFarmaco() {
		return ordineFarmaco;
	}

	public int getUtente() {
		return utente;
	}

	public void setUtente(int utente) {
		this.utente = utente;
	}
}
