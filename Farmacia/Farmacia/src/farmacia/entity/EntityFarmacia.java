package farmacia.entity;


import farmacia.TipoUtente;
import farmacia.database.OrdineAcquistoDAO;
import farmacia.database.OrdineDAO;
import farmacia.database.UtenteDAO;
import farmacia.exceptions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Classe che rappresenta la farmacia.
 */
public class EntityFarmacia {
	/**
	 * L'unica istanza di <code>EntityFarmacia</code> che implementa il pattern Singleton.
	 */
	private static EntityFarmacia uniqueInstance;

	private final List<EntityOrdineAcquisto> ordiniAcquisto;

	/**
	 * Costruttore privato per impedire la creazione di istanze multiple.
	 */
	private EntityFarmacia() {
		ordiniAcquisto = new ArrayList<>();
	}

	/**
	 * Funzione statica per richiamare l'unica istanza di <code>EntityFarmacia</code> o crearne una se non esiste già.
	 * @return l'istanza singleton di <code>EntityFarmacia</code>.
	 */
	public static EntityFarmacia getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new EntityFarmacia();
		}
		return uniqueInstance;
	}

	/**
	 * Funzione che permette di registrare un nuovo cliente.
	 * @param username username del cliente.
	 * @param password password del cliente.
	 * @param nome nome del cliente.
	 * @param cognome cognome del cliente.
	 * @param dataNascita data di nascita del cliente.
	 * @param email email del cliente.
	 * @throws RegistrationFailedException sollevata quando la registrazione di un utente fallisce.
	 */
	public void registraCliente(String username, String password, String nome, String cognome, Date dataNascita, String email) throws RegistrationFailedException {
		EntityCliente nuovoCliente = new EntityCliente(username, password, nome, cognome, dataNascita, email);
		try {
			nuovoCliente.salvaInDB();
		} catch (DBException e) {
			throw new RegistrationFailedException(String.format("Registrazione dell'utente '%s' fallita.%n%s", username, e.getMessage()));
		}
	}

	/**
	 * Funzione che permette a un utente di effettuare il login nella farmacia.
	 * @param username username dell'utente.
	 * @param password password dell'utente.
	 * @throws LoginFailedException se il login fallisce.
	 */
	public void loginUtente(String username, String password) throws LoginFailedException {
		try {
			UtenteDAO utenteDAO = new UtenteDAO(username);
			if (utenteDAO.getPassword().equals(password)) {
				EntityUtente utente;
				if (utenteDAO.getTipoUtente() == TipoUtente.CLIENTE) {
					utente = new EntityCliente(utenteDAO);
				} else {
					if (utenteDAO.getTipoUtente() == TipoUtente.FARMACISTA) {
						for (OrdineAcquistoDAO ordineAcquistoDAO: OrdineAcquistoDAO.visualizzaOrdiniAcquisto()) {
							ordiniAcquisto.add(new EntityOrdineAcquisto(ordineAcquistoDAO));
						}
					}
					utente = new EntityUtente(utenteDAO);
				}
				Sessione.getInstance().setUtenteCorrente(utente);
			} else {
				throw new LoginFailedException("Login fallito, password errata");
			}
		} catch (DBException e) {
			throw new LoginFailedException(String.format("Login dell'utente '%s' fallita.%n%s", username, e.getMessage()));
		}
	}

	/**
	 * Funzione che consente di visualizzare tutti gli ordini effettuati dai clienti della farmacia.
	 * @throws DBException se non è possibile accedere al DB.
	 * @return <code>List&lt;EntityOrdine&gt;</code> lista di ordini.
	 */
	public List<EntityOrdine> visualizzaOrdini() throws DBException {
		List<EntityOrdine> ordini = new ArrayList<>();
		for (OrdineDAO ordineDAO: OrdineDAO.visualizzaOrdini()) {
			ordini.add(new EntityOrdine(ordineDAO));
		}
		return ordini;
	}

	/**
	 * Funzione che permette di creare un nuovo OrdineAcquisto.
	 * @param farmaciQuantita una serie di coppie (idFarmaco, quantita).
	 * @throws OrderCreationFailedException lanciata quando un farmaco non viene trovato o quando le scorte sono insufficienti.
	 * @return id dell'ordine di acquisto.
	 */
	public String creaOrdineAcquisto(Map<Integer, Integer> farmaciQuantita) throws OrderCreationFailedException {
		if (farmaciQuantita.isEmpty())
			throw new OrderCreationFailedException("Ordine vuoto");

		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		EntityOrdineAcquisto ordineAcquisto = new EntityOrdineAcquisto();
		try {
			for (Map.Entry<Integer, Integer> entry: farmaciQuantita.entrySet()) {
				EntityFarmaco farmaco = catalogo.cercaFarmacoById(entry.getKey());
				ordineAcquisto.aggiungiFarmaco(farmaco, entry.getValue());
			}
			ordineAcquisto.salvaInDB();
			ordiniAcquisto.add(ordineAcquisto);
			return ordineAcquisto.getId();
		} catch (FarmacoNotFoundException | DBException e) {
			throw new OrderCreationFailedException(e.getMessage());
		}
	}

	/**
	 * Funzione che restituisce gli ordini di fornitura richiesti dalla farmacia.
	 * @return <code>List&lt;EntityOrdineAcquisto&gt;</code> lista di ordini di acquisto.
	 */
	public List<EntityOrdineAcquisto> visualizzaOrdiniAcquisto() {
		return ordiniAcquisto;
	}

	/**
	 * Funzione che aggiorna lo stato di un ordine da "Non ritirato" a "Ritirato".
	 * @param idOrdine id dell'ordine da aggiornare.
	 * @throws OrderNotFoundException se l'ordine non esiste.
	 */
	public void aggiornaOrdine(String idOrdine) throws OrderNotFoundException {
		try {
			EntityOrdine ordine = new EntityOrdine(idOrdine);
			ordine.aggiorna();
		} catch (DBException e) {
			throw new OrderNotFoundException(e.getMessage());
		}
	}

	/**
	 * Funzione che aggiorna lo stato di un ordine d'acquisto "Non ritirato" a "Ritirato".
	 * @param idOrdine viene usato per la ricerca dell'ordine di acquisto.
	 * @throws OrderNotFoundException quando non esiste un ordine di acquisto con <code>id</code> uguale a <code>idOrdine</code>.
	 * @throws FarmacoNotFoundException se uno dei farmaci all'interno dell'ordine di acquisto non esiste
	 */
	public void aggiornaOrdineAcquisto(String idOrdine) throws OrderNotFoundException, FarmacoNotFoundException {
		boolean trovato = false;
		for (EntityOrdineAcquisto ordineAcquisto: ordiniAcquisto) {
			if (ordineAcquisto.getId().equals(idOrdine)) {
				try {
					trovato = true;
					ordineAcquisto.aggiorna();
					for (Map.Entry<EntityFarmaco, Integer> entry: ordineAcquisto.getQuantitaFarmaci().entrySet()) {
						EntityCatalogo.getInstance().incrementaScorte(entry.getKey().getId(), entry.getValue());
					}
				} catch (DBException e) {
					throw new OrderNotFoundException(e.getMessage());
				}
			}
		}
		if (!trovato) {
			throw new OrderNotFoundException("Ordine di acquisto non trovato");
		}
	}
}
