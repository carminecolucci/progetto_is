package farmacia.database;

import farmacia.exceptions.DBException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Classe <code>DAO</code> degli ordini di fornitura della farmacia.
 */
public class OrdineAcquistoDAO {
	private String id;
	private Date dataCreazione;
	private boolean ricevuto;
	private final Map<FarmacoDAO, Integer> ordiniAcquistoFarmaci;

	private static final Logger logger = Logger.getLogger("OrdineAcquistoDAO");

	/**
	 * Costruttore privato di <code>OrdineAcquistoDAO</code>
	 */
	private OrdineAcquistoDAO() {
		ordiniAcquistoFarmaci = new HashMap<>();
	}

	/**
	 * Costruttore che popola il DAO a partire dall'id dell'ordine di acquisto.
	 * @param id L'id (uuid) dell'ordine di acquisto.
	 * @throws DBException Se non è possibile accedere al DB o se l'ordine di acquisto non esiste.
	 */
	public OrdineAcquistoDAO(String id) throws DBException {
		ordiniAcquistoFarmaci = new HashMap<>();
		this.caricaDaDB(id);
	}

	/**
	 * Costruttore che crea un nuovo OrdineAcquistoDAO. La lista dei farmaci verrà
	 * popolata attraverso {@link #aggiungiFarmaco(int, int)}.
	 * @param id L'id (uuid) dell'ordine di acquisto.
	 * @param dataCreazione La data di creazione dell'ordine di acquisto.
	 * @param ricevuto lo stato iniziale dell'ordine di acquisto.
	 */
	public OrdineAcquistoDAO(String id, Date dataCreazione, boolean ricevuto) {
		this.id = id;
		this.dataCreazione = dataCreazione;
		this.ricevuto = ricevuto;
		this.ordiniAcquistoFarmaci = new HashMap<>();
	}

	/**
	 * Funzione che aggiunge all'ordine di acquisto un farmaco con la sua rispettiva quantità.
	 * @param idFarmaco id del farmaco.
	 * @param quantita La quantità di farmaco desiderata.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public void aggiungiFarmaco(int idFarmaco, int quantita) throws DBException {
		this.ordiniAcquistoFarmaci.put(new FarmacoDAO(idFarmaco), quantita);
	}

	/**
	 * Funzione che crea un ordine di acquisto nel DB. Il metodo presuppone che l'istanza di <code>OrdineAcquistoDAO</code>
	 * sulla quale viene richiamato abbia già tutti gli attributi popolati (compreso l'<code>id</code>).
	 * @throws DBException Lanciata se non è possibile accedere al DB.
	 */
	public void createOrdineAcquisto() throws DBException {
		if (salvaInDB() == 0) {
			throw new DBException(String.format("Errore nella creazione della ordine '%s'", id));
		}
	}

	/**
	 * Funzione che cambia lo stato dell'ordine di acquisto da 'Non ricevuto' a 'Ricevuto'. Il metodo presuppone che
	 * l'istanza di <code>OrdineAcquistoDAO</code> sulla quale viene richiamato abbia popolato il campo <code>id</code>.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'ordine di acquisto non esiste.
	 */
	public void aggiorna() throws DBException {
		String query = String.format("UPDATE ordini_acquisto SET ricevuto = 1 WHERE id = '%s';", this.id);
		logger.info(query);
		try {
			DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException(String.format("Errore durante l'aggiornamento dell'ordine d'acquisto '%s'.%n%s", this.id, e.getMessage()));
		}

		this.ricevuto = true;
	}

	/**
	 * Funzione di utilità che è stata aggiunta in fase di testing solo per rendere agevole la cancellazione dell'ordine d'acquisto.
	 * @param id Id dell'ordine da eliminare.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'ordine da eliminare non è stato trovato.
	 */
	public static void deleteOrdineAcquisto(String id) throws DBException {
		String query = String.format("DELETE FROM ordini_acquisto WHERE id = '%s';", id);
		logger.info(query);
		try {
			DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException(String.format("Errore durante l'eliminazione dal DB dell'ordine '%s'.%n%s", id, e.getMessage()));
		}
		// i farmaci presenti nell'ordine sono cancellati automaticamente dalla tabella ordini_acquisto_farmaci
		// poiché abbiamo utilizzato la politica ON DELETE CASCADE.
	}

	/**
	 * Funzione privata che popola l'OrdineAcquistoDAO consultando il DB a partire dall'id.
	 * @param id l'<code>id</code> dell'ordine di acquisto da caricare.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'ordine di acquisto non esiste.
	 */
	private void caricaDaDB(String id) throws DBException {
		String query = String.format("SELECT * from ordini_acquisto WHERE id = '%s';", id);
		logger.info(query);
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (rs.next()) {
				this.dataCreazione = rs.getDate("dataCreazione");
				this.ricevuto = rs.getBoolean("ricevuto");
				this.id = id;
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException(String.format("Errore nel caricamento dell'ordine di acquisto con id '%s'.%n%s",
				this.id, e.getMessage()));
		}

		this.caricaOrdiniAcquistoFarmaciDaDB();
	}

	/**
	 * Funzione di utilità per caricare gli ordiniAcquisto
	 * @throws DBException se non si possono caricare gli ordiniAcquisto
	 */
	private void caricaOrdiniAcquistoFarmaciDaDB() throws DBException {
		String query = String.format("SELECT * FROM ordini_acquisto_farmaci WHERE ordineAcquisto = '%s'", this.id);
		logger.info(query);
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			while (rs.next()) {
				int idFarmaco = rs.getInt("farmacoAcquisto");
				int quantita = rs.getInt("quantita");
				this.ordiniAcquistoFarmaci.put(new FarmacoDAO(idFarmaco), quantita);
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException(String.format("Errore nel caricamento delle informazioni dell'ordine di acquisto con id '%s.%n%s",
				this.id, e.getMessage()));
		}
	}

	/**
	 * Funzione privata che salva l'ordine di acquisto nel DB. Il metodo presuppone che l'istanza di <code>OrdineAcquistoDAO</code>
	 * sulla quale viene richiamato abbia popolati gli attributi <code>id</code>, <code>dataCreazione</code> e <code>ricevuto</code>.
	 * @return Il numero di farmaci nell'ordine di acquisto.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'ordine di acquisto già esiste.
	 */
	private int salvaInDB() throws DBException {
		java.sql.Date data = new java.sql.Date(this.dataCreazione.getTime());
		String query = String.format("INSERT INTO ordini_acquisto (id, dataCreazione, ricevuto) VALUES ('%s', '%s', %d);",
			this.id, data, this.ricevuto ? 1 : 0);
		logger.info(query);

		int rs;
		try {
			rs = DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException(String.format("Errore nel salvataggio dell'ordine di acquisto '%s'.%n%s", this.id, e.getMessage()));
		}

		query = "INSERT INTO ordini_acquisto_farmaci (ordineAcquisto, farmacoAcquisto, quantita) " +
				"VALUES ('%s', %d, %d);";
		logger.info(query);
		for (Map.Entry<FarmacoDAO, Integer> item: this.ordiniAcquistoFarmaci.entrySet()) {
			try {
				rs = DBManager.getInstance().executeQuery(String.format(query, this.id, item.getKey().getId(), item.getValue()));
			} catch (ClassNotFoundException | SQLException e) {
				throw new DBException(String.format("Errore nel salvataggio dell'ordine di acquisto '%s'.%n%s", this.id, e.getMessage()));
			}
		}
		return rs;
	}

	/**
	 * Funzione che ritorna una lista contenente tutti gli ordini di acquisto registrati presso la farmacia.
	 * @return La lista di <code>OrdineAcquistoDAO</code> contenente tutti gli ordini di acquisto registrati presso la farmacia.
	 * @throws DBException Lanciata se non è possibile accedere al DB.
	 */
	public static List<OrdineAcquistoDAO> visualizzaOrdiniAcquisto() throws DBException {
		String query = "SELECT * FROM ordini_acquisto;";
		logger.info(query);

		List<OrdineAcquistoDAO> ordiniAcquisto = new ArrayList<>();
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			while (rs.next()) {
				OrdineAcquistoDAO ordine = new OrdineAcquistoDAO();
				ordine.id = rs.getString("id");
				ordine.dataCreazione = rs.getDate("dataCreazione");
				ordine.ricevuto = rs.getBoolean("ricevuto");
				ordine.caricaOrdiniAcquistoFarmaciDaDB();
				ordiniAcquisto.add(ordine);
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException("Errore durante il caricamento degli ordini di acquisto: " + e.getMessage());
		}
		return ordiniAcquisto;
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

	public Map<FarmacoDAO, Integer> getOrdiniAcquistoFarmaci() {
		return ordiniAcquistoFarmaci;
	}
}
