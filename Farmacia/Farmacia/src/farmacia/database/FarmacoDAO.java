package farmacia.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import farmacia.exceptions.DBException;

public class FarmacoDAO {
	private int id;
	private float prezzo;
	private boolean prescrizione;
	private String nome;
	private int scorta;

	private static final Logger logger = Logger.getLogger("FarmacoDAO");

	/**
	 * Costruttore di default.
	 */
	public FarmacoDAO() { }

	/**
	 * Costruttore che crea un nuovo FarmacoDAO e lo popola via DB.
	 * @param id L'id del farmaco.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public FarmacoDAO(int id) throws DBException {
		this.id = id;
		this.caricaDaDB();
	}

	/**
	 * Costruttore che crea un nuovo FarmacoDAO e lo popola interamente.
	 * @param id L'id del farmaco.
	 * @param prezzo Il prezzo del farmaco.
	 * @param prescrizione Se il farmaco richiede una prescrizione per la vendita.
	 * @param nome Il nome del farmaco.
	 * @param scorta Le scorte del farmaco.
	 */
	private FarmacoDAO(int id, float prezzo, boolean prescrizione, String nome, int scorta) {
		this.id = id;
		this.prezzo = prezzo;
		this.prescrizione = prescrizione;
		this.nome = nome;
		this.scorta = scorta;
	}

	/**
	 * Funzione che popola un FarmacoDAO e gli assegna l'ID scelto dal DB.
	 * @param prezzo Il prezzo del farmaco.
	 * @param prescrizione Se il farmaco richiede o meno una prescrizione per la vendita.
	 * @param nome Il nome del farmaco.
	 * @param scorta Le scorte del farmaco.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public void createFarmaco(float prezzo, boolean prescrizione, String nome, int scorta) throws DBException {
		if (cercaInDB(nome) != 0) {
			throw new DBException(String.format("Farmaco '%s' già esistente", nome));
		}

		if (salvaInDB(prezzo, prescrizione, nome, scorta) == 0)
			throw new DBException(String.format("Errore durante la registrazione del farmaco '%s'.", nome));

		this.id = cercaInDB(nome);
		this.prezzo = prezzo;
		this.prescrizione = prescrizione;
		this.nome = nome;
		this.scorta = scorta;
	}
	/**
	 * Funzione privata che popola il FarmacoDAO consultando il DB.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	private void caricaDaDB() throws DBException {
		String query = String.format("SELECT * FROM farmaci WHERE id = %d;", this.id);

		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (rs.next()) {
				this.prezzo = rs.getFloat("prezzo");
				this.prescrizione = rs.getBoolean("prescrizione");
				this.nome = rs.getString("nome");
				this.scorta = rs.getInt("scorta");
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento dal database del farmaco con" +
				"id %d.%n%s", this.id, e.getMessage()));
			throw new DBException("Errore nel caricamento di un farmaco.");
		}
	}

	/**
	 * Funzione privata che cerca il farmaco nel DB e ritorna il suo id.
	 * @param nome Il nome del farmaco.
	 * @return '-1' se il farmaco non esiste, altrimenti ritorna l'id.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	private int cercaInDB(String nome) throws DBException {
		String query = String.format("SELECT * FROM farmaci WHERE nome = '%s';", nome);
		logger.info(query);
		int id = -1;
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (!rs.next())
				return id;
			id = rs.getInt("id");
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore nella ricerca del farmaco '%s'.%n%s",
				nome, e.getMessage()));
			throw new DBException(String.format("Errore nella ricerca del farmaco '%s'", nome));
		}
		return id;
	}

	/**
	 * Funzione che salva un farmaco nel DB.
	 * @param prezzo Il prezzo del farmaco.
	 * @param prescrizione Se il farmaco richiede o meno una prescrizione per la vendita.
	 * @param nome Il nome del farmaco.
	 * @param scorta Le scorte del farmaco.
	 * @return '1' se il farmaco è stato inserito, '-1' altrimenti.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se non è stato possibile aggiungere il farmaco.
	 */
	private int salvaInDB(float prezzo, boolean prescrizione, String nome, int scorta) throws DBException {
		String query = String.format("INSERT INTO farmaci (prezzo, prescrizione, nome, scorta)" +
				"VALUES (%f, %d, '%s', %d);",
			prezzo, prescrizione ? 1 : 0, nome, scorta
		);

		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);

		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'inserimento del farmaco " +
					"'%s' nel database.%n%s",
				nome, e.getMessage()));
			throw new DBException(String.format("Errore nel salvataggio del farmaco '%s'", nome));
		}
		return rs;
	}

	/**
	 * Elimina il farmaco dal DB.
	 * @return '1' se il farmaco è stato cancellato, '-1' altrimenti.
	 */
	private int eliminaDaDB() {
		String query = String.format("DELETE FROM farmaci WHERE id = %d;", this.id);
		logger.info(query);
		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning("Errore durante l'eliminazione dal DB del farmaco '%s'." + this.nome);
		}
		return rs;
	}

	/**
	 * Funzione che ritorna una lista di FarmacoDAO mappati sui farmaci registrati presso la farmacia.
	 * @return Lista di FarmacoDAO.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se non ci sono farmaci registrati presso la farmacia.
	 */
	public static ArrayList<FarmacoDAO> getFarmaci() throws DBException {

		String query = "SELECT * FROM farmaci;";
		ArrayList<FarmacoDAO> arrayListFarmacoDAO = new ArrayList<>();
		logger.info(query);

		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			while (rs.next()) {
				int id = rs.getInt("id");
				float prezzo = rs.getFloat("prezzo");
				boolean prescrizione = rs.getBoolean("prescrizione");
				String nome = rs.getString("nome");
				int scorta = rs.getInt("scorta");
				FarmacoDAO queryFarmacoDAO = new FarmacoDAO(id, prezzo, prescrizione, nome, scorta);
				arrayListFarmacoDAO.add(queryFarmacoDAO);
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning("Errore durante il caricamento dal database.");
			throw new DBException("Errore nel caricamento di un farmaco.");
		}

		return arrayListFarmacoDAO;
	}

	/**
	 * Funzione che aggiorna il prezzo di un farmaco.
	 * @param id Id del farmaco da modificare.
	 * @param nuovoPrezzo Nuovo prezzo da impostare per il farmaco.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public static void aggiornaPrezzoDB(int id, float nuovoPrezzo) throws DBException {
		FarmacoDAO farmacoDaModificare = new FarmacoDAO(id);
		String nomeFarmacoDaModificare = farmacoDaModificare.getNome();
		String query = String.format("UPDATE farmaci SET prezzo = %f WHERE id = %d;", nuovoPrezzo, id);
		logger.info(query);
		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning("Errore durante l'aggiornamento del prezzo del farmaco '" + nomeFarmacoDaModificare + "'.");
		}
	}

	/**
	 * Funzione che aggiorna le scorte di un farmaco.
	 * @param id Id del farmaco da modificare.
	 * @param nuoveScorte Nuove scorte da impostare per il farmaco.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public static void aggiornaScorteDB(int id, int nuoveScorte) throws DBException {
		FarmacoDAO farmacoDaModificare = new FarmacoDAO(id);
		String nomeFarmacoDaModificare = farmacoDaModificare.getNome();
		String query = String.format("UPDATE farmaci SET scorta = %d WHERE id = %d;", nuoveScorte, id);
		logger.info(query);
		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning("Errore durante l'aggiornamento delle scorte del farmaco '" + nomeFarmacoDaModificare + "'.");
		}
	}

	/**
	 * Funzione che aggiorna le informazioni di un farmaco.
	 * @param id Id del farmaco.
	 * @param nuovoPrezzo Nuovo prezzo da impostare per il farmaco.
	 * @param nuovaPrescrizione Se il farmaco, diversamente da prima, ha bisogno o meno della prescrizione.
	 * @param nuovoNome Nuovo nome da impostare per il farmaco.
	 * @param nuoveScorte Nuove scorte da impostare per il farmaco.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se il farmaco non esiste.
	 */
	public static void aggiornaFarmacoDB(int id, float nuovoPrezzo, boolean nuovaPrescrizione, String nuovoNome, int nuoveScorte) throws DBException {
		FarmacoDAO farmacoDaModificare = new FarmacoDAO(id);
		String nomeFarmacoDaModificare = farmacoDaModificare.getNome();

		String query = String.format("UPDATE farmaci SET prezzo = %f, prescrizione = %d, " +
							"nome = '%s', scorta = %d WHERE id = %d;",
							nuovoPrezzo, nuovaPrescrizione ? 1 : 0, nuovoNome, nuoveScorte, id);
		logger.info(query);
		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'aggiornamento del farmaco '%s'.", nomeFarmacoDaModificare));
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

	public int getScorta() {
		return scorta;
	}

	public void setScorta(int scorta) {
		this.scorta = scorta;
	}

	public int getId() {
		return id;
	}

}