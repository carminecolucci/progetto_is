package farmacia.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import farmacia.TipoUtente;
import farmacia.exceptions.DBException;

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

	public UtenteDAO() { }

	public UtenteDAO(int id) throws DBException {
		this.id = id;
		this.caricaDaDB(id);
	}

	public UtenteDAO(String username) throws DBException {
		int id = cercaInDB(username);
		if (id == 0) {
			throw new DBException(String.format("Utente '%s' non esistente", username));
		}

		this.id = id;
		this.caricaDaDB(id);
	}

	public void createUtente(String nome, String cognome, String username, String password, Date dataNascita, String email) throws DBException {
		if (cercaInDB(username) != 0) {
			throw new DBException(String.format("Utente '%s' già esistente", username));
		}

		if (salvaInDB(nome, cognome, username, password, dataNascita, email) == 0)
			throw new DBException(String.format("Errore durante la registrazione dell'utente '%s'", username));

		this.id = cercaInDB(username);
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.dataNascita = dataNascita;
		this.tipoUtente = TipoUtente.CLIENTE;
		this.email = email;
	}

	private void caricaDaDB(int id) throws DBException {
		String query = String.format("SELECT * FROM utenti WHERE id = %d;", id);

		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (rs.next()) {
				this.nome = rs.getString("nome");
				this.cognome = rs.getString("cognome");
				this.username = rs.getString("username");
				this.email = rs.getString("email");
				this.password = rs.getString("password");
				this.dataNascita = rs.getDate("dataNascita");
				this.tipoUtente = TipoUtente.valueOf(rs.getString("tipo"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento dal database di un utente con id %d.%n%s",
				id, e.getMessage())
			);
			throw new DBException("Errore nel caricamento di un utente");
		}
	}

	public int cercaInDB(String username) throws DBException {
		String query = String.format("SELECT * FROM utenti WHERE username = '%s';", username);
		logger.info(query);
		int id = -1;
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (!rs.next())
				return 0;
			id = rs.getInt("id");
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore nella ricerca dell'utente '%s'.%n%s",
				username, e.getMessage()));
			throw new DBException(String.format("Errore nella ricerca dell'utente '%s'", username));
		}
		return id;
	}

	private int salvaInDB(String nome, String cognome, String username, String password, Date dataNascita, String email) throws DBException {
		String query = String.format("INSERT INTO utenti (nome, cognome, username, password, dataNascita, tipoUtente, email)" +
				"VALUES ('%s', '%s', '%s', '%s', '%s', %d, '%s');",
			nome, cognome, username, password, dataNascita, TipoUtente.CLIENTE.ordinal(), email
		);

		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'inserimento dell'utente " +
					"['%s', '%s', '%s', '%s', '%s', '%s'] nel database.%n%s",
				nome, cognome, username, password, dataNascita, email, e.getMessage()));
			throw new DBException(String.format("Errore nel salvataggio dell'utente '%s'", username));
		}
		return rs;
	}

	private int eliminaDaDB() {
		String query = String.format("DELETE FROM utenti WHERE id = %d;", this.id);
		logger.info(query);
		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning("Errore durante l'eliminazione dal DB dell'utente " + this.id);
		}
		return rs;
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

	public static class OrdineDAO {

	}
}
