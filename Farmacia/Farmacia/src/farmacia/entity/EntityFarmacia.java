package farmacia.entity;

import farmacia.database.UtenteDAO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.LoginFailedException;
import farmacia.exceptions.RegistrationFailedException;

import java.util.ArrayList;
import java.util.Date;

/**
 * Classe del package entity nel modello BCED, essa implementa la sessione utente.
 */
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
		UtenteDAO utenteDAO = new UtenteDAO();
		// TODO
	}

	/**
	 *
	 */
	public ArrayList<EntityOrdine> visualizzaOrdini() {

	}

	/**
	 *
	 */
	public void creaOrdineAcquisto() {

	}

	/**
	 *
	 */
	public void visualizzaOrdiniAcquisto() {

	}

}
