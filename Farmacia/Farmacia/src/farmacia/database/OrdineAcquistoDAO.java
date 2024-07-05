package farmacia.database;

import farmacia.exceptions.DBException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class OrdineAcquistoDAO {
	private String id;
	private Date dataCreazione;
	private boolean ricevuto;
	private Map<FarmacoDAO, Integer> ordineAcquistoFarmaci;

	private static final Logger logger = Logger.getLogger("OrdineAcquistoDAO");

	/**
	 * Costruttore di default.
	 */
	public OrdineAcquistoDAO() {}

	/**
	 * Costruttore che popola il DAO a partire dall'id dell'ordine di acquisto.
	 * @param id L'id (uuid) dell'ordine di acquisto.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'ordine di acquisto non esiste.
	 */
	public OrdineAcquistoDAO(String id) throws DBException {
		this.id = id;
		this.caricaDaDB();
	}

	/**
	 * Costruttore che crea un nuovo OrdineAcquistoDAO. La lista dei farmaci verrà popolato via <code>aggiungiOrdineAcquistoFarmaco</code>.
	 * @param id L'id (uuid) dell'ordine di acquisto.
	 * @param dataCreazione La data di creazione dell'ordine di acquisto.
	 */
	public OrdineAcquistoDAO(String id, Date dataCreazione) {
		this.id = id;
		this.dataCreazione = dataCreazione;
		this.ricevuto = false;
		this.ordineAcquistoFarmaci = new HashMap<>();
	}

	/**
	 * Funzione che popola l'ordine con il farmaco e la rispettiva quantità.
	 * @param idFarmaco L'id del farmaco.
	 * @param quantita La quantità di farmaco desiderata.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */

	public void aggiungiOrdineAcquistoFarmaco(int idFarmaco, int quantita) throws DBException {
		this.ordineAcquistoFarmaci.put(new FarmacoDAO(idFarmaco), quantita);
	}

	/**
	 * Funzione privata che popola l'OrdineAcquistoDAO consultando il DB.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'ordine di acquisto non esiste.
	 */
	private void caricaDaDB() throws DBException {
		String query = String.format("SELECT * from ordini_acquisto WHERE id = '%s';", this.id);

		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (rs.next()) {
				this.dataCreazione = rs.getDate("dataCreazione");
				this.ricevuto = rs.getBoolean("ricevuto");
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento dell'ordine di acquisto con id '%s'.%n%s",
					this.id, e.getMessage())
			);
			throw new DBException("Errore nel caricamento dell'ordine di acquisto con id '" + this.id + "'.");
		}

		query = String.format("SELECT * from ordini_acquisto_farmaci WHERE id = '%s';", this.id);
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			while (rs.next()) {
				int idFarmaco = rs.getInt("farmaco");
				int quantita = rs.getInt("quantita");
				this.ordineAcquistoFarmaci.put(new FarmacoDAO(idFarmaco), quantita);
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento dell'ordine di acquisto con id " +
					"'%s'.%n%s", this.id, e.getMessage()));
			throw new DBException("Errore nel caricamento delle informazioni dell'ordine di acquisto con id '" + this.id + "'.");
		}
	}

	/**
	 * Funzione privata che salva l'ordine di acquisto nel DB.
	 * @return '-1' se non è stato possibile inserire l'ordine di acquisto, altrimenti ritorna il numero di farmaci nell'ordine di acquisto.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'ordine di acquisto già esiste.
	 */
	private int salvaInDB() throws DBException {
		String query = String.format("INSERT INTO ordini_acquisto (id, dataCreazione, ricevuto) VALUES" +
						" ('%s', '%s', %d);",
				this.id, this.dataCreazione, this.ricevuto ? 1 : 0);

		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'inserimento dell'ordine di acquisto '%s' nel database.%n%s",
					this.id, e.getMessage())
			);
			throw new DBException(String.format("Errore nel salvataggio dell'ordine di acquisto'%s'.", this.id));
		}

		query = "INSERT INTO ordini_acquisto_farmaci (ordine_acquisto, farmaco, quantita) " +
				"VALUES ('%s', %d, %d);";
		for (Map.Entry<FarmacoDAO, Integer> item: this.ordineAcquistoFarmaci.entrySet()) {
			try {
				rs = DBManager.getInstance().executeQuery(String.format(query, this.id, item.getKey().getId(), item.getValue()));
			} catch (ClassNotFoundException | SQLException e) {
				logger.warning("Errore durante l'inserimento dell'ordine di acquisto con id '" + this.id + "'.");
			}
		}
		return rs;
	}

	/**
	 * Funzione che ritorna una lista contenente tutti gli ordini di acquisto registrati presso la farmacia.
	 * @return La lista contenente gli ordini di acquisto registrati presso la farmacia.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se non ci sono ordini di acquisto.
	 */
	public ArrayList<OrdineAcquistoDAO> getOrdiniAcquisto() throws DBException {

		String query = "SELECT id FROM ordini_acquisto;";
		ArrayList<OrdineAcquistoDAO> listaOrdiniAcquistoDAO = new ArrayList<>();
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			while (rs.next()) {
				String idOrdine = rs.getString("id");
				listaOrdiniAcquistoDAO.add(new OrdineAcquistoDAO(idOrdine));
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento degli ordini di acquisto" +
					".%n%s", e.getMessage()));
			throw new DBException("Errore nel caricamento di un ordine di acquisto.");
		}
		return listaOrdiniAcquistoDAO;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public boolean isRicevuto() {
		return ricevuto;
	}

	public void setRicevuto(boolean ricevuto) {
		this.ricevuto = ricevuto;
	}

	public Map<FarmacoDAO, Integer> getOrdineAcquistoFarmaci() {
		return ordineAcquistoFarmaci;
	}

	public void setOrdineAcquistoFarmaci(Map<FarmacoDAO, Integer> ordineAcquistoFarmaci) {
		this.ordineAcquistoFarmaci = ordineAcquistoFarmaci;
	}
}
