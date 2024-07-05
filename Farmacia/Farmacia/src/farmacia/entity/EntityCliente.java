package farmacia.entity;

import farmacia.TipoUtente;
import farmacia.database.UtenteDAO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoNotFoundException;
import farmacia.exceptions.OrderCreationFailedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Map;

public class EntityCliente extends  EntityUtente {
	List<EntityOrdine> storicoOrdini;

	public EntityCliente(String username, String password, String nome, String cognome, Date dataNascita, String email) {
		super(username, password, nome, cognome, dataNascita, TipoUtente.CLIENTE, email);
	}

	public EntityCliente(UtenteDAO utenteDAO) {
		// se chiamo questo costruttore, significa che sono già registrato
		super(utenteDAO);
		storicoOrdini = new ArrayList<>();
		// TODO: popolare lo storico ordini attraverso OrdineDAO
	}

	public EntityCliente(int id) throws DBException {
		this(new UtenteDAO(id));
	}

	public List<EntityOrdine> visualizzaStoricoOrdini() {
		return storicoOrdini;
	}

	/**
	 * Funzione che permette di creare un nuovo Ordine di un cliente già loggato.
	 * @param farmaciQuantita una serie di coppie (idFarmaco, quantita).
	 * @throws OrderCreationFailedException lanciata quando un farmaco non viene trovato o quando le scorte sono insufficienti
	 */
	public void creaOrdine(Map<Integer, Integer> farmaciQuantita) throws OrderCreationFailedException {
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		if (!catalogo.checkScorte(farmaciQuantita)) {
			throw new OrderCreationFailedException("Ordine non creato per mancanza scorte");
		}
		EntityOrdine ordine = new EntityOrdine();
		try {
			for (Integer idFarmaco : farmaciQuantita.keySet()) {
				EntityFarmaco farmaco = catalogo.cercaFarmaco(idFarmaco);
				ordine.aggiungiOrdineFarmaco(farmaco, farmaciQuantita.get(idFarmaco));
				int scortaResidua = catalogo.decrementaScorte(idFarmaco, farmaciQuantita.get(idFarmaco));
				if (scortaResidua == 0) {
					// TODO: fai partire un ordine di acquisto per questo farmaco
				}
			}
			ordine.salvaInDB();
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
		UtenteDAO utenteDAO = new UtenteDAO();
		utenteDAO.createUtente(this.getNome(), this.getCognome(), this.getUsername(), this.getPassword(), this.getDataNascita(), this.getEmail());
	}
}
