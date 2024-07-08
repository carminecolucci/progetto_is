package farmacia.database;

import farmacia.exceptions.DBException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class OrdineDAO {
	private String id;
	private Date dataCreazione;
	private boolean ritirato;
	private Map<FarmacoDAO, Integer> ordineFarmaci;
	private int cliente;
	private float totale;

	private static final Logger logger = Logger.getLogger("OrdineDAO");

	/**
	 * Costruttore di default di <code>OrdineDAO</code>
	 */
	private OrdineDAO() {
		this.ordineFarmaci = new HashMap<>();
	}

	/**
	 * Costruttore che crea un nuovo <code>OrdineDAO</code>. Il campo <code>ordineFarmaci</code> deve essere popolato
	 * attraverso il metodo {@link #aggiungiOrdineFarmaco(int, int)}.
	 * @param id id dell'ordine
	 * @param dataCreazione data di creazione dell'ordine
	 * @param cliente id del cliente che ha generato l'ordine
	 */
	public OrdineDAO(String id, Date dataCreazione, int cliente, float totale) {
		this.id = id;
		this.dataCreazione = dataCreazione;
		this.ritirato = false;
		this.cliente = cliente;
		this.ordineFarmaci = new HashMap<>();
		this.totale = totale;
	}

	/**
	 * Costruttore che crea un nuovo <code>OrdineDAO</code> e lo popola via DB attraverso il suo <code>id</code>.
	 * @param id l'<code>id</code> dell'ordine da caricare dal DB.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'ordine non esiste.
	 */
	public OrdineDAO(String id) throws DBException {
		ordineFarmaci = new HashMap<>();
		this.caricaDaDB(id);
	}

	/**
	 * Funzione che crea un ordine nel DB. Il metodo presuppone che l'istanza di <code>OrdineDAO</code> sulla quale viene
	 * richiamato abbia tutti attributi già popolati.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'ordine è gia presente.
	 */
	public void createOrdine() throws DBException {
		if (salvaInDB() == 0) {
			throw new DBException(String.format("Errore nella creazione della ordine '%s'", id));
		}
	}

	/**
	 * Funzione che cambia lo stato dell'ordine di acquisto da 'Non ritirato' a 'Ritirato'. Il metodo presuppone che
	 * l'istanza di <code>OrdineDAO</code> sulla quale viene richiamato abbia popolato il campo <code>id</code>.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'ordine non esiste.
	 */
	public void aggiorna() throws DBException {
		String query = String.format("UPDATE ordini SET ritirato = 1 WHERE id = '%s';", this.id);
		logger.info(query);
		try {
			DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'aggiornamento dell'ordine '%s'.", this.id));
			throw new DBException("Errore durante l'aggiornamento dell'ordine. " + e.getMessage());
		}

		this.ritirato = true;
	}

	/**
	 * Funzione che ritorna tutti gli ordini effettuati dai clienti della farmacia
	 * @throws DBException se non si possono prelevare gli ordini dal DB.
	 */
	public static List<OrdineDAO> visualizzaOrdini() throws DBException {
		String query = "SELECT * FROM ordini;";
		return OrdineDAO.caricaOrdiniDaDB(query);
	}

	/**
	 * Funzione che ritorna tutti gli ordini effettuati da un determinato cliente.
	 * @param cliente id del cliente utilizzato per la ricerca
	 * @return lista di <code>OrdineDAO</code> effettuati dal cliente specificato.
	 * @throws DBException se non si possono prelevare gli ordini dal DB
	 */
	public static List<OrdineDAO> getOrdiniByCliente(int cliente) throws DBException {
		String query = String.format("SELECT * FROM ordini WHERE cliente = %d;", cliente);
		return OrdineDAO.caricaOrdiniDaDB(query);
	}

	/**
	 * Funzione che aggiunge all'ordine un farmaco con la sua rispettiva quantità.
	 * @param idFarmaco id del farmaco.
	 * @param quantita La quantità di farmaco desiderata.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public void aggiungiOrdineFarmaco(int idFarmaco, int quantita) throws DBException {
		this.ordineFarmaci.put(new FarmacoDAO(idFarmaco), quantita);
	}

	/**
	 * Funzione di utilità per caricare tutti gli ordini dal DB che soddisfano una query
	 * @param query da cercare nel DB
	 * @return <code>List&lt;OrdineDAO&gt;</code> lista di ordini
	 * @throws DBException se non si può caricare gli ordini dal DB
	 */
	private static List<OrdineDAO> caricaOrdiniDaDB(String query) throws DBException {
		logger.info(query);

		List<OrdineDAO> ordini = new ArrayList<>();
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			while (rs.next()) {
				OrdineDAO ordine = new OrdineDAO();
				ordine.id = rs.getString("id");
				ordine.dataCreazione = rs.getDate("dataCreazione");
				ordine.ritirato = rs.getBoolean("ritirato");
				ordine.cliente = rs.getInt("cliente");
				ordine.totale = rs.getFloat("totale");
				ordine.caricaOrdiniFarmaciDaDB();
				ordini.add(ordine);
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento degli ordini dei clienti.%n%s", e.getMessage()));
			throw new DBException("Errore nel caricamento degli ordini dei clienti");
		}

		return ordini;
	}

	/**
	 * Funzione di utilità che carica un ordine dal DB a partire dal suo <code>id</code>.
	 * @param id l'<code>id</code> dell'ordine da caricare.
	 * @throws DBException se non si può caricare un ordine dal DB.
	 */
	private void caricaDaDB(String id) throws DBException {
		String query = String.format("SELECT * FROM ordini WHERE id = '%s';", id);
		logger.info(query);
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (rs.next()) {
				this.dataCreazione = rs.getDate("dataCreazione");
				this.ritirato = rs.getBoolean("ritirato");
				this.cliente = rs.getInt("cliente");
				this.totale = rs.getFloat("totale");
				this.id = id;
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento di un ordine con id '%s'.%n%s",
				id, e.getMessage()));
			throw new DBException("Errore nel caricamento di un ordine");
		}

		this.caricaOrdiniFarmaciDaDB();
	}

	/**
	 * Funzione di utilità per caricare gli ordineFarmaci
	 * @throws DBException se non si possono caricare gli ordineFarmaci
	 */
	private void caricaOrdiniFarmaciDaDB() throws DBException {
		String query = String.format("SELECT * FROM ordini_farmaci WHERE ordine = '%s'", this.id);
		logger.info(query);
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			while (rs.next()) {
				int idFarmaco = rs.getInt("farmaco");
				int quantita = rs.getInt("quantita");
				this.ordineFarmaci.put(new FarmacoDAO(idFarmaco), quantita);
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento di un ordine con id " +
				"'%s'.%n%s", this.id, e.getMessage()));
			throw new DBException("Errore nel caricamento di un ordine");
		}
	}

	/**
	 * Funzione di utilità per inserire l'ordine nel DB. Il metodo presuppone che l'istanza di <code>OrdineDAO</code> sulla
	 * quale viene richiamato abbia tutti gli attributi popolati.
	 * @return il numero di righe inserite
	 * @throws DBException se non si può inserire l'ordine nel DB
	 */
	private int salvaInDB() throws DBException {
		java.sql.Date data = new java.sql.Date(this.dataCreazione.getTime());
		String query = String.format(Locale.US, "INSERT INTO ordini (id, dataCreazione, ritirato, cliente, totale) VALUES ('%s', '%s', %d, %d, %f);",
			this.id, data, this.ritirato ? 1 : 0, this.cliente, this.totale);
		logger.info(query);
		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'inserimento dell'ordine '%s' nel database.%n%s",
				this.id, e.getMessage()));
			throw new DBException(String.format("Errore nel salvataggio dell'ordine '%s'", this.id));
		}

		query = "INSERT INTO ordini_farmaci (ordine, farmaco, quantita) " +
				"VALUES ('%s', %d, %d);";
		logger.info(query);
		for (Map.Entry<FarmacoDAO, Integer> item: ordineFarmaci.entrySet()) {
			try {
				rs = DBManager.getInstance().executeQuery(String.format(query,
					this.id, item.getKey().getId(), item.getValue())
				);
			} catch (ClassNotFoundException | SQLException e) {
				logger.warning("Errore durante l'inserimento dell'ordine");
			}
		}

		return rs;
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

	public boolean isRitirato() {
		return ritirato;
	}

	public void setRitirato(boolean ritirato) {
		this.ritirato = ritirato;
	}

	public Map<FarmacoDAO, Integer> getOrdineFarmaci() {
		return ordineFarmaci;
	}

	public int getCliente() {
		return cliente;
	}

	public void setCliente(int cliente) {
		this.cliente = cliente;
	}

	public float getTotale() {
		return totale;
	}

	public void setTotale(float totale) {
		this.totale = totale;
	}
}
