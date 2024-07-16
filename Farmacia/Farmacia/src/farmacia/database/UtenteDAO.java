package farmacia.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import farmacia.TipoUtente;
import farmacia.exceptions.DBException;

/**
 * Classe <code>DAO</code> degli utenti della farmacia.
 */
public class UtenteDAO {
	private int id;
	private String nome;
	private String cognome;
	private String username;
	private String password;
	private Date dataNascita;
	private String email;
	private TipoUtente tipoUtente;

	private static final Logger logger = Logger.getLogger("UtenteDAO");

	/**
	 * Costruttore che crea un nuovo <code>UtenteDAO</code>. Per assegnare un'id all'utente è necessario salvarlo nel
	 * DB utilizzando {@link #createUtente()}.
	 * @param nome Il nome dell'utente.
	 * @param cognome Il cognome dell'utente.
	 * @param username L'username dell'utente.
	 * @param password La password dell'utente.
	 * @param dataNascita La data di nascita dell'utente.
	 * @param email L'email dell'utente.
	 */
	public UtenteDAO(String nome, String cognome, String username, String password, Date dataNascita, String email) {
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.dataNascita = dataNascita;
		this.email = email;
	}

	/**
	 * Costruttore che crea un nuovo <code>UtenteDAO</code> e popola i suoi attributi cercando attraverso il suo <code>id</code>.
	 * @param id l'<code>id</code> dell'utente utilizzato per la ricerca.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'utente non esiste.
	 */
	public UtenteDAO(int id) throws DBException {
		this.caricaDaDB(id);
	}

	/**
	 * Costruttore che crea un nuovo <code>UtenteDAO</code> e popola i suoi attributi cercando attraverso il suo <code>username</code>.
	 * @param username l'<code>username</code> dell'utente utilizzato per la ricerca.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'utente non esiste.
	 */
	public UtenteDAO(String username) throws DBException {
		int idUtente = cercaInDB(username);
		if (idUtente == -1) {
			throw new DBException(String.format("Utente '%s' non esistente", username));
		}

		this.caricaDaDB(idUtente);
	}

	/**
	 * Funzione che ritorna l'<code>username</code> dell'utente a partire dal suo <code>id</code>.
	 * @param id l'<code>id</code> dell'utente.
	 * @return l'<code>username</code> dell'utente.
	 * @throws DBException se l'utente con quell'<code>id</code> non esiste.
	 */
	public static String getUsername(int id) throws DBException {
		String query = String.format("SELECT * FROM utenti WHERE id = %d;", id);
		logger.info(query);
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (rs.next()) {
				return rs.getString("username");
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException(String.format("Errore nella ricerca dell'utente con id %d.%n%s", id, e.getMessage()));
		}
		return "";
	}

	/**
	 * Funzione che crea un utente nel DB e assegna all'<code>UtenteDAO</code> l'ID scelto dal DB.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'utente è già presente.
	 */
	public void createUtente() throws DBException {
		if (cercaInDB(this.username) != -1) {
			throw new DBException(String.format("Utente '%s' già esistente", this.username));
		}

		if (cercaEmailInDB(this.email)) {
			throw new DBException(String.format("Email '%s' già esistente", email));
		}

		if (salvaInDB(this.nome, this.cognome, this.username, this.password, this.dataNascita, this.email) == 0)
			throw new DBException(String.format("Errore durante la registrazione dell'utente '%s'", this.username));

		this.id = cercaInDB(this.username);
	}

	/**
	 * Funzione privata che popola l'<code>UtenteDAO</code> consultando il DB a partire dall'<code>id</code>.
	 * @param id l'<code>id</code> dell'utente utilizzato per la ricerca.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se l'utente non esiste.
	 */
	private void caricaDaDB(int id) throws DBException {
		String query = String.format("SELECT * FROM utenti WHERE id = %d;", id);
		logger.info(query);
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (rs.next()) {
				this.nome = rs.getString("nome");
				this.cognome = rs.getString("cognome");
				this.username = rs.getString("username");
				this.email = rs.getString("email");
				this.password = rs.getString("password");
				this.dataNascita = rs.getDate("dataNascita");
				this.tipoUtente = TipoUtente.fromInt(rs.getInt("tipo"));
				this.id = id;
			} else {
				throw new DBException(String.format("Utente '%s' non esistente", id));
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException("Errore nel caricamento di un utente. " + e.getMessage());
		}
	}

	/**
	 * Funzione che cerca l'utente nel DB attraverso il suo <code>username</code>.
	 * @param username l'<code>username</code> dell'utente.
	 * @return -1 se l'utente non esiste, o l'id dell'utente cercato.
	 * @throws DBException Lanciata se non è possibile accedere al DB.
	 */
	public int cercaInDB(String username) throws DBException {
		String query = String.format("SELECT * FROM utenti WHERE username = '%s';", username);
		logger.info(query);
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (!rs.next())
				return -1;
			return rs.getInt("id");
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException(String.format("Errore nella ricerca dell'utente '%s'.%n%s", username, e.getMessage()));
		}
	}

	/**
	 * Funzione privata che cerca l'utente nel DB a partire dalla sua <code>email</code>.
	 * @param email l'<code>email</code> dell'utente utilizzata per la ricerca.
	 * @return true se l'email esiste nel DB.
	 * @throws DBException Lanciata se non è possibile accedere al DB.
	 */
	private boolean cercaEmailInDB(String email) throws DBException {
		String query = String.format("SELECT * FROM utenti WHERE email = '%s';", email);
		logger.info(query);
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			return rs.next();
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException(String.format("Errore nella ricerca dell'email '%s'.%n%s", email, e.getMessage()));
		}
	}

	/**
	 * Funzione che salva un utente nel DB.
	 * @param nome Il nome dell'utente.
	 * @param cognome Il cognome dell'utente.
	 * @param username L'username dell'utente.
	 * @param password La password dell'utente.
	 * @param dataNascita La data di nascita dell'utente.
	 * @param email L'email dell'utente.
	 * @return 1 se l'utente è stato inserito correttamente.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se non è stato possibile aggiungere l'utente.
	 */
	private int salvaInDB(String nome, String cognome, String username, String password, Date dataNascita, String email) throws DBException {
		String query = String.format("INSERT INTO utenti (nome, cognome, username, password, dataNascita, tipo, email) " +
			"VALUES ('%s', '%s', '%s', '%s', '%s', %d, '%s');",
			nome, cognome, username, password, dataNascita, TipoUtente.CLIENTE.ordinal(), email);
		logger.info(query);

		try {
			return DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException(String.format("Errore nel salvataggio dell'utente '%s'.%n%s", username, e.getMessage()));
		}
	}

	/**
	 * Funzione che cancella l'istanza di <code>UtenteDAO</code> dal DB.
	 * @throws DBException se non è possibile accedere al DB.
	 */
	public void deleteUtente() throws DBException {
		eliminaDaDB();
	}

	/**
	 * Elimina l'utente dal DB. Il metodo presuppone che l'istanza di <code>UtenteDAO</code> sulla quale viene richiamato
	 * abbia il campo <code>id</code> popolato.
	 * @throws DBException Lanciata se non è possibile accedere al DB o se non è stato possibile eliminare l'utente.
	 */
	private void eliminaDaDB() throws DBException {
		String query = String.format("DELETE FROM utenti WHERE id = %d;", this.id);
		logger.info(query);
		try {
			DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException(String.format("Errore durante l'eliminazione dal DB dell'utente '%s'.%n%s", this.username, e.getMessage()));
		}
	}

	public int getId() {
		return id;
	}

	public TipoUtente getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(TipoUtente tipoUtente) {
		this.tipoUtente = tipoUtente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
