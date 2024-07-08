package farmacia.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import farmacia.exceptions.DBException;

public class FarmacoDAO {
	private int id;
	private float prezzo;
	private boolean prescrizione;
	private String nome;
	private int scorte;

	private static final Logger logger = Logger.getLogger("FarmacoDAO");

	/**
	 * Costruttore privato che crea un nuovo <code>FarmacoDAO</code>
	 */
	private FarmacoDAO() { }

	/**
	 * Costruttore che crea un nuovo <code>FarmacoDAO</code> e lo popola via DB a partire dal suo <code>id</code>.
	 * @param id l'id del farmaco da caricare dal DB.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public FarmacoDAO(int id) throws DBException {
		this.caricaDaDB(id);
	}

	/**
	 * Costruttore che crea un nuovo <code>FarmacoDAO</code> e lo popola via DB a partire dal suo <code>nome</code>.
	 * @param nome il nome del farmaco da caricare dal DB.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public FarmacoDAO(String nome) throws DBException {
		this.caricaDaDB(nome);
	}

	/**
	 * Costruttore che crea un nuovo <code>FarmacoDAO</code>. Per assegnare un'id al farmaco è necessario salvarlo nel
	 * DB utilizzando <code>createFarmaco()</code>
	 * @param prezzo Il prezzo del farmaco.
	 * @param prescrizione Se il farmaco richiede una prescrizione per la vendita.
	 * @param nome Il nome del farmaco.
	 * @param scorte Le scorte del farmaco.
	 */
	public FarmacoDAO(float prezzo, boolean prescrizione, String nome, int scorte) {
		this.prezzo = prezzo;
		this.prescrizione = prescrizione;
		this.nome = nome;
		this.scorte = scorte;
	}

	/**
	 * Funzione che crea un farmaco nel DB e assegna al <code>FarmacoDAO</code> l'ID scelto dal DB. Il metodo presuppone
	 * che l'istanza di <code>FarmacoDAO</code> sulla quale viene richiamato abbia il resto degli attributi già popolati.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco è gia presente.
	 */
	public void createFarmaco() throws DBException {
		if (cercaInDB(this.nome) != -1) {
			throw new DBException(String.format("Farmaco '%s' già esistente", nome));
		}

		salvaInDB(this.prezzo, this.prescrizione, this.nome, this.scorte);
		this.id = cercaInDB(this.nome);
	}

	/**
	 * Funzione che elimina un farmaco nel DB. Il metodo presuppone che l'istanza di <code>FarmacoDAO</code> sulla quale
	 * viene richiamato abbia popolato sia l'attributo <code>nome</code> che l'attributo <code>id</code>.
	 * @throws DBException se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public void deleteFarmaco() throws DBException {
		if (cercaInDB(this.nome) == -1) {
			throw new DBException(String.format("Farmaco '%s' non esistente", this.nome));
		}

		eliminaDaDB();
	}

	/**
	 * Funzione privata che popola il <code>FarmacoDAO</code> consultando il DB a partire dall'id.
	 * @param id l'<code>id</code> del farmaco da caricare.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	private void caricaDaDB(int id) throws DBException {
		String query = String.format("SELECT * FROM farmaci WHERE id = %d;", id);
		logger.info(query);

		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (rs.next()) {
				this.nome = rs.getString("nome");
				this.prezzo = rs.getFloat("prezzo");
				this.prescrizione = rs.getBoolean("prescrizione");
				this.scorte = rs.getInt("scorte");
				this.id = id;
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento dal database del farmaco con id %d.%n%s",
				id, e.getMessage()));
			throw new DBException("Errore nel caricamento di un farmaco. " + e.getMessage());
		}
	}

	/**
	 * Funzione privata che popola il FarmacoDAO consultando il DB a partire dal nome.
	 * @param nome il <code>nome</code> del farmaco da caricare.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	private void caricaDaDB(String nome) throws DBException {
		String query = String.format("SELECT * FROM farmaci WHERE nome = '%s';", nome);
		logger.info(query);

		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (rs.next()) {
				this.id = rs.getInt("id");
				this.nome = nome;
				this.prezzo = rs.getFloat("prezzo");
				this.prescrizione = rs.getBoolean("prescrizione");
				this.scorte = rs.getInt("scorte");
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento dal database del farmaco '%s'.%n%s",
				nome, e.getMessage()));
			throw new DBException("Errore nel caricamento di un farmaco." + e.getMessage());
		}
	}

	/**
	 * Funzione privata che cerca il farmaco nel DB.
	 * @param nome il nome del farmaco.
	 * @return -1 se il farmaco non esiste, o l'id del farmaco cercato.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	private int cercaInDB(String nome) throws DBException {
		String query = String.format("SELECT id FROM farmaci WHERE nome = '%s';", nome);
		logger.info(query);
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (!rs.next())
				return -1;
			return rs.getInt("id");
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore nella ricerca del farmaco '%s'.%n%s",
				nome, e.getMessage()));
			throw new DBException(String.format("Errore nella ricerca del farmaco '%s'", nome));
		}
	}

	/**
	 * Funzione di utilità che è stata aggiunta in fase di testing solo per rendere agevole la cancellazione del farmaco
	 * creato nel test <code>AggiuntiEliminaFarmacoTest</code>.
	 * @param nome Il nome del farmaco.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco da eliminare non è stato trovato.
	 */
	public static void deleteFarmaco(String nome) throws DBException {
		String query = String.format("DELETE FROM farmaci WHERE nome = '%s';", nome);
		logger.info(query);
		try {
			DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'eliminazione dal DB del farmaco '%s'.", nome));
			throw new DBException(String.format("Errore durante l'eliminazione dal DB del farmaco '%s'.", nome));
		}
	}

	/**
	 * Funzione che salva un farmaco nel DB.
	 * @param prezzo Il prezzo del farmaco.
	 * @param prescrizione Se il farmaco richiede o meno una prescrizione per la vendita.
	 * @param nome Il nome del farmaco.
	 * @param scorte Le scorte del farmaco.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se non è stato possibile aggiungere il farmaco.
	 */
	private void salvaInDB(float prezzo, boolean prescrizione, String nome, int scorte) throws DBException {
		String query = String.format(Locale.US, "INSERT INTO farmaci (prezzo, prescrizione, nome, scorte) " +
				"VALUES (%.2f, %d, '%s', %d);",
			prezzo, prescrizione ? 1 : 0, nome, scorte
		);

		try {
			DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'inserimento del farmaco '%s' nel database.%n%s",
				nome, e.getMessage()));
			throw new DBException(String.format("Errore nel salvataggio del farmaco '%s'", nome));
		}
	}

	/**
	 * Elimina il farmaco dal DB. Il metodo presuppone che l'istanza di <code>FarmacoDAO</code> sulla quale viene
	 * richiamato abbia abbia popolato l'attributo <code>id</code>.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se non è stato possibile eliminare il farmaco.
	 */
	private void eliminaDaDB() throws DBException {
		String query = String.format("DELETE FROM farmaci WHERE id = %d;", this.id);
		logger.info(query);
		try {
			DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'eliminazione dal DB del farmaco '%s'.", this.nome));
			throw new DBException(String.format("Errore durante l'eliminazione dal DB del farmaco '%s'.", this.nome));
		}
	}

	/**
	 * Funzione che ritorna una lista di FarmacoDAO mappati sui farmaci registrati presso la farmacia.
	 * @return Lista di FarmacoDAO.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se non ci sono farmaci registrati presso la farmacia.
	 */
	public static List<FarmacoDAO> getFarmaci() throws DBException {

		String query = "SELECT * FROM farmaci;";
		List<FarmacoDAO> farmaci = new ArrayList<>();
		logger.info(query);

		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			while (rs.next()) {
				FarmacoDAO farmaco = new FarmacoDAO();
				farmaco.id = rs.getInt("id");
				farmaco.prezzo = rs.getFloat("prezzo");
				farmaco.prescrizione = rs.getBoolean("prescrizione");
				farmaco.nome = rs.getString("nome");
				farmaco.scorte = rs.getInt("scorte");
				farmaci.add(farmaco);
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning("Errore durante il caricamento di farmaci dal database.");
			throw new DBException("Errore nel caricamento dei farmaci.");
		}

		return farmaci;
	}

	/**
	 * Funzione che aggiorna il prezzo di un farmaco.
	 * @param nome Nome del farmaco da modificare.
	 * @param nuovoPrezzo Nuovo prezzo da impostare per il farmaco.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public static void aggiornaPrezzoDB(String nome, float nuovoPrezzo) throws DBException {
		FarmacoDAO farmacoDaModificare = new FarmacoDAO(nome);
		String nomeFarmacoDaModificare = farmacoDaModificare.getNome();
		String query = String.format("UPDATE farmaci SET prezzo = %f WHERE nome = '%s';", nuovoPrezzo, nome);
		logger.info(query);
		try {
			DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning("Errore durante l'aggiornamento del prezzo del farmaco '" + nomeFarmacoDaModificare + "'.");
		}
	}

	/**
	 * Funzione che aggiorna le scorte di un farmaco.
	 * @param idFarmaco id del farmaco da modificare.
	 * @param nuoveScorte Nuove scorte da impostare per il farmaco.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public static void aggiornaScorteDB(int idFarmaco, int nuoveScorte) throws DBException {
		FarmacoDAO farmacoDaModificare = new FarmacoDAO(idFarmaco);
		String nomeFarmacoDaModificare = farmacoDaModificare.getNome();
		String query = String.format("UPDATE farmaci SET scorte = %d WHERE id = %d;", nuoveScorte, idFarmaco);
		logger.info(query);
		try {
			DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning("Errore durante l'aggiornamento delle scorte del farmaco '" + nomeFarmacoDaModificare + "'.");
		}
	}

	/**
	 * Funzione che aggiorna le informazioni di un farmaco. L'<code>id</code> passato viene utilizzato per la ricerca
	 * del farmaco da modificare.
	 * @param idFarmaco Id del farmaco da aggiornare.
	 * @param prezzo Nuovo prezzo da impostare per il farmaco.
	 * @param prescrizione Se il farmaco, diversamente da prima, ha bisogno o meno della prescrizione.
	 * @param nuovoNome Nuovo nome da impostare per il farmaco.
	 * @param scorte Nuove scorte da impostare per il farmaco.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public static void aggiornaFarmacoDB(int idFarmaco, float prezzo, boolean prescrizione, String nuovoNome, int scorte) throws DBException {
		FarmacoDAO farmaco = new FarmacoDAO(idFarmaco);
		String query = String.format(Locale.US, "UPDATE farmaci SET prezzo = %f, prescrizione = %d, " +
							"nome = '%s', scorte = %d WHERE id = %d;",
							prezzo, prescrizione ? 1 : 0, nuovoNome, scorte, idFarmaco);
		logger.info(query);
		try {
			DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'aggiornamento del farmaco '%s'.", farmaco.getNome()));
			throw new DBException("Errore durante l'aggiornamento del farmaco");
		}

	}

	public float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}

	public boolean isPrescrizione() {
		return prescrizione;
	}

	public void setPrescrizione(boolean prescrizione) {
		this.prescrizione = prescrizione;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getScorte() {
		return scorte;
	}

	public void setScorte(int scorte) {
		this.scorte = scorte;
	}

	public int getId() {
		return id;
	}
}