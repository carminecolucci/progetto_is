package farmacia.entity;

import farmacia.TipoUtente;
import farmacia.database.UtenteDAO;
import farmacia.exceptions.DBException;

import java.util.Date;

public class EntityUtente {
	private int id;
	private String username;
	private String password;
	private String nome;
	private String cognome;
	private Date dataNascita;
	private TipoUtente tipoUtente;
	private String email;

	/**
	 * Costruttore di default che crea un'istanza vuota.
	 */
	public EntityUtente() { }

	/**
	 * Costruttore che crea un nuovo <code>EntityUtente</code>
	 * @param username Username dell'utente.
	 * @param password Password dell'utente.
	 * @param nome Nome dell'utente.
	 * @param cognome Cognome dell'utente.
	 * @param dataNascita DataNascita dell'utente.
	 * @param tipoUtente TipoUtente dell'utente.
	 * @param email Email dell'utente.
	 */
	public EntityUtente(String username, String password, String nome, String cognome, Date dataNascita, TipoUtente tipoUtente, String email) {
		this.username = username;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.tipoUtente = tipoUtente;
		this.email = email;
	}

	/**
	 * Costruttore che permette di popolare una <code>EntityUtente</code> a partire
	 * da un <code>UtenteDA0</code>. Lo usiamo per la registrazione.
	 * @param utenteDAO istanza di <code>UtenteDA0</code> già popolata
	 */
	public EntityUtente(UtenteDAO utenteDAO) {
		this.id = utenteDAO.getId();
		this.username = utenteDAO.getUsername();
		this.password = utenteDAO.getPassword();
		this.nome = utenteDAO.getNome();
		this.cognome = utenteDAO.getCognome();
		this.dataNascita = utenteDAO.getDataNascita();
		this.tipoUtente = utenteDAO.getTipoUtente();
		this.email = utenteDAO.getEmail();
	}

	/**
	 * Funzione che ritorna l'username dell'utente a partire dal suo id.
	 * @param id id dell'utente.
	 * @return username dell'utente.
	 * @throws DBException se l'utente con quell'<code>id</code> non esiste.
	 */
	public static String getUsername(int id) throws DBException {
		return UtenteDAO.getUsername(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TipoUtente getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(TipoUtente tipoUtente) {
		this.tipoUtente = tipoUtente;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
