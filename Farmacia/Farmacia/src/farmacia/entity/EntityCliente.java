package farmacia.entity;

import farmacia.TipoUtente;
import farmacia.database.OrdineDAO;
import farmacia.database.UtenteDAO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoNotFoundException;
import farmacia.exceptions.OrderCreationFailedException;

import java.util.*;

public class EntityCliente extends EntityUtente {
	List<EntityOrdine> storicoOrdini;

	/**
	 * Costruttore di <code>EntityCliente</code>
	 * @param username Username dell'utente.
	 * @param password Password dell'utente.
	 * @param nome Nome dell'utente.
	 * @param cognome Cognome dell'utente.
	 * @param dataNascita DataNascita dell'utente.
	 * @param email Email dell'utente.
	 */
	public EntityCliente(String username, String password, String nome, String cognome, Date dataNascita, String email) {
		super(username, password, nome, cognome, dataNascita, TipoUtente.CLIENTE, email);
		storicoOrdini = new ArrayList<>();
	}

	/**
	 * Costruttore che permette di popolare una EntityCliente a partire
	 * da un <code>UtenteDA0</code>. Lo usiamo per la registrazione.
	 * @param utenteDAO istanza di <code>UtenteDA0</code> già popolata
	 */
	public EntityCliente(UtenteDAO utenteDAO) throws DBException {
		super(utenteDAO);
		storicoOrdini = new ArrayList<>();
		for (OrdineDAO ordineDAO : OrdineDAO.getOrdiniByCliente(utenteDAO.getId())) {
			storicoOrdini.add(new EntityOrdine(ordineDAO));
		}
	}

	/**
	 * Costruttore che crea un <code>EntityCliente</code> a partire dal suo id.
	 * @param id id del cliente.
	 * @throws DBException se non è possibile accedere al DB.
	 */
	public EntityCliente(int id) throws DBException {
		this(new UtenteDAO(id));
	}

	/**
	 * Visualizza lo storico degli ordini del cliente.
	 * @return la lista di ordini effettuati dal cliente.
	 */
	public List<EntityOrdine> visualizzaStoricoOrdini() {
		return storicoOrdini;
	}

	/**
	 * Funzione che permette di creare un nuovo Ordine.
	 * @param farmaciQuantita una serie di coppie (idFarmaco, quantita).
	 * @throws OrderCreationFailedException lanciata quando un farmaco non viene trovato o quando le scorte sono insufficienti
	 */
	public String creaOrdine(Map<Integer, Integer> farmaciQuantita) throws OrderCreationFailedException {
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		if (!catalogo.checkScorte(farmaciQuantita)) {
			throw new OrderCreationFailedException("Ordine non creato per mancanza scorte");
		}
		EntityOrdine ordine = new EntityOrdine(this.getId());
		try {
			for (Map.Entry<Integer, Integer> entry : farmaciQuantita.entrySet()) {
				int id = entry.getKey();
				int quantita = entry.getValue();
				EntityFarmaco farmaco = catalogo.cercaFarmacoById(id);
				ordine.aggiungiOrdineFarmaco(farmaco, quantita);
				int scorteResidue = catalogo.decrementaScorte(id, quantita);
				if (scorteResidue == 0) {
					// TODO: OrdineAcquisto fuori dal for, per contenere più coppie
					EntityOrdineAcquisto ordineAcquisto = new EntityOrdineAcquisto();
					ordineAcquisto.aggiungiOrdineAcquistoFarmaco(farmaco, EntityOrdineAcquisto.QUANTITA_ORDINE_DEFAULT);
					ordineAcquisto.salvaInDB();
				}
			}
			ordine.salvaInDB();
			storicoOrdini.add(ordine);
			return ordine.getId();
		} catch (FarmacoNotFoundException e) {
			throw new OrderCreationFailedException("Errore creazione ordine, farmaco non trovato");
		} catch (DBException e) {
			throw new OrderCreationFailedException(e.getMessage());
		}
	}

	/**
	 * Funzione che permette di creare un nuovo Cliente nel DB a partire da
	 * un'istanza di <code>EntityCliente</code> già popolata.
	 * @throws DBException Lanciata quando si cerca di salvare un utente già registrato
	 */
	public void salvaInDB() throws DBException {
		UtenteDAO utenteDAO = new UtenteDAO(this.getNome(), this.getCognome(), this.getUsername(), this.getPassword(), this.getDataNascita(), this.getEmail());
		utenteDAO.createUtente();
	}
}
