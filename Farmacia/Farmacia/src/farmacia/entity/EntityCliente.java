package farmacia.entity;

import farmacia.TipoUtente;
import farmacia.database.OrdineDAO;
import farmacia.database.UtenteDAO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoNotFoundException;
import farmacia.exceptions.OrderCreationFailedException;

import java.util.List;
import java.util.Date;
import java.util.Map;

public class EntityCliente extends EntityUtente {
	List<EntityOrdine> storicoOrdini;

	public EntityCliente(String username, String password, String nome, String cognome, Date dataNascita, String email) {
		super(username, password, nome, cognome, dataNascita, TipoUtente.CLIENTE, email);
	}

	public EntityCliente(UtenteDAO utenteDAO) throws DBException {
		// se chiamo questo costruttore, significa che sono già registrato
		super(utenteDAO);
		for (OrdineDAO ordineDAO : OrdineDAO.getOrdiniByCliente(utenteDAO.getId())) {
			storicoOrdini.add(new EntityOrdine(ordineDAO));
		}
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
				EntityFarmaco farmaco = catalogo.cercaFarmacoById(idFarmaco);
				ordine.aggiungiOrdineFarmaco(farmaco, farmaciQuantita.get(idFarmaco));
				int scorteResidue = catalogo.decrementaScorte(idFarmaco, farmaciQuantita.get(idFarmaco));
				if (scorteResidue == 0) {
					// TODO: OrdineAcquisto fuori dal for, per contenere più coppie
					EntityOrdineAcquisto ordineAcquisto = new EntityOrdineAcquisto();
					ordineAcquisto.aggiungiOrdineAcquistoFarmaco(farmaco, 50); // TODO esportare questa costante
					ordineAcquisto.salvaInDB();
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
		// TODO: Make crud methods take no parameters
		UtenteDAO utenteDAO = new UtenteDAO();
		utenteDAO.createUtente(this.getNome(), this.getCognome(), this.getUsername(), this.getPassword(), this.getDataNascita(), this.getEmail());
	}
}
