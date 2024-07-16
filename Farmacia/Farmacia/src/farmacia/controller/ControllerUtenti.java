package farmacia.controller;

import farmacia.dto.DTO;
import farmacia.entity.EntityFarmacia;
import farmacia.entity.EntityUtente;
import farmacia.entity.Sessione;
import farmacia.exceptions.LoginFailedException;
import farmacia.exceptions.RegistrationFailedException;

import java.util.Date;

/**
 * Classe che ha la responsabilità di gestire gli utenti della farmacia.
 */
public class ControllerUtenti {
	
	/**
	 * L'unica istanza di <code>ControllerUtenti</code> che implementa il pattern Singleton.
	 */
	private static ControllerUtenti uniqueInstance;

	/**
	 * Costruttore privato per impedire la creazione di istanze multiple.
	 */
	private ControllerUtenti() {}

	/**
	 * Funzione statica per richiamare l'unica istanza di <code>ControllerUtenti</code> o crearne una se non esiste già.
	 * @return l'istanza singleton di <code>ControllerUtenti</code>.
	 */
	public static ControllerUtenti getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ControllerUtenti();
		}
		return uniqueInstance;
	}

	/**
	 * Funzione che permette di registrare un nuovo <i>cliente</i> nella piattaforma a partire dai suoi dati.
	 * @param username username dell'utente che vuole registrarsi.
	 * @param password password dell'utente che vuole registrarsi.
	 * @param nome nome dell'utente che vuole registrarsi.
	 * @param cognome cognome dell'utente che vuole registrarsi.
	 * @param dataNascita data di nascita dell'utente che vuole registrarsi.
	 * @param email email dell'utente che vuole registrarsi.
	 * @throws RegistrationFailedException quando <code>username</code> e/o <code>email</code> sono già presenti in
	 * un altro utente registrato nella piattaforma.
	 */
	public void registraCliente(String username, String password, String nome, String cognome, Date dataNascita, String email) throws RegistrationFailedException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		farmacia.registraCliente(username, password, nome, cognome, dataNascita, email);
	}

	/**
	 * Funzione che permette a un utente registrato di loggarsi nella piattaforma. La funzione autentica l'utente e setta
	 * la sessione corrente del programma.
	 * @param username username dell'utente che vuole loggarsi.
	 * @param password password dell'utente che vuole loggarsi.
	 * @throws LoginFailedException quando lo <code>username</code> dell'utente che cerca di loggarsi non esiste nella piattaforma
	 * o la <code>password</code> inserita è sbagliata.
	 */
	public void loginUtente(String username, String password) throws LoginFailedException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		farmacia.loginUtente(username, password);
	}

	/**
	 * Funzione che ritorna l'utente attualmente loggato nel sistema. È necessario che un utente sia loggato nel sistema.
	 * @return un'istanza di <code>DTO</code> contenente i seguenti campi:
	 * <ul>
	 *     <li>"nome": <code>nome</code> dell'utente attualmente loggato nel sistema.</li>
	 *     <li>"cognome": <code>cognome</code> dell'utente attualmente loggato nel sistema.</li>
	 *     <li>"tipoUtente": <code>TipoUtente</code> dell'utente attualmente loggato nel sistema. Può assumere i valori
	 *     "CLIENTE", "FARMACISTA" e "DIRETTORE".</li>
	 * </ul>
	 */
	public DTO getUtenteCorrente() {
		EntityUtente utenteCorrente = Sessione.getInstance().getUtenteCorrente();
		DTO dto = new DTO();
		dto.set("nome", utenteCorrente.getNome());
		dto.set("cognome", utenteCorrente.getCognome());
		dto.set("tipoUtente", utenteCorrente.getTipoUtente());
		return dto;
	}
}
