package farmacia.entity;

import farmacia.TipoUtente;
import farmacia.database.OrdineDAO;
import farmacia.database.UtenteDAO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.LoginFailedException;
import farmacia.exceptions.RegistrationFailedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntityFarmacia {

	/**
	 * L'unica istanza di <code>EntityFarmacia</code> che implementa il pattern Singleton.
	 */
	private static EntityFarmacia uniqueInstance;

	/**
	 * Costruttore privato per impedire la creazione di istanze multiple.
	 */
	private EntityFarmacia() {}

	/**
	 * Funzione statica per richiamare l'unica istanza di <code>EntityFarmacia</code> o crearne una se non esiste già.
	 * @return l'istanza singleton di <code>EntityFarmacia</code>.
	 */
	public static EntityFarmacia getInstance() {
		if(uniqueInstance == null) {
			uniqueInstance = new EntityFarmacia();
		}
		return uniqueInstance;
	}

	/**
	 * Funzione che permette di registrare un nuovo cliente.
	 * @throws RegistrationFailedException sollevata quando la registrazione di un utente fallisce.
	 */
	public void registraCliente(String username, String password, String nome, String cognome, Date dataNascita, String email) throws RegistrationFailedException {
		EntityCliente nuovoCliente = new EntityCliente(username, password, nome, cognome, dataNascita, email);
		try {
			nuovoCliente.salvaInDB();
		} catch (DBException e) {
			throw new RegistrationFailedException("Registrazione dell'utente " + username + " fallita");
		}
	}

	public void loginUtente(String username, String password) throws LoginFailedException {
		try {
			UtenteDAO utenteDAO = new UtenteDAO(username);
			if (utenteDAO.getPassword().equals(password)) {
				EntityUtente utenteLoggato;
				if (utenteDAO.getTipoUtente() == TipoUtente.CLIENTE) {
					utenteLoggato = new EntityCliente(utenteDAO);
				} else {
					utenteLoggato = new EntityUtente(utenteDAO);
				}
				Sessione.getInstance().setUtenteCorrente(utenteLoggato);
			} else {
				throw new LoginFailedException("Login fallito, password errata");
			}
		} catch (DBException e) {
			throw new LoginFailedException("Login dell'utente " + username + " fallita");
		}

	}

	/**
	 *
	 */
	public List<EntityOrdine> visualizzaOrdini() throws DBException {
		List<EntityOrdine> ordini = new ArrayList<>();
		for (OrdineDAO ordineDAO : OrdineDAO.visualizzaOrdini()) {
			ordini.add(new EntityOrdine(ordineDAO));
		}
		return ordini;
	}

	/**
	 *
	 */
	public void creaOrdineAcquisto() {

	}

	/**
	 *
	 */
	//public List<EntityOrdineAcquisto> visualizzaOrdiniAcquisto() {
		// TODO: necessito di OrdineAcquistoDAO.getOrdiniAcquisto()
	//}

}
