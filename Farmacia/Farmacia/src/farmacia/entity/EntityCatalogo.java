package farmacia.entity;

import farmacia.database.FarmacoDAO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoCreationFailedException;
import farmacia.exceptions.FarmacoNotFoundException;

import java.util.ArrayList;
import java.util.Map;

public class EntityCatalogo {

	ArrayList<EntityFarmaco> farmaci;

	/**
	 * L'unica istanza di <code>EntityCatalogo</code> che implementa il pattern Singleton.
	 */
	private static EntityCatalogo uniqueInstance;

	/**
	 * Costruttore privato per impedire la creazione di istanze multiple.
	 */
	private EntityCatalogo() {
		FarmacoDAO farmacoDAO = new FarmacoDAO();
		try {
			for (FarmacoDAO farmaco : farmacoDAO.getFarmaci()) {
				try {
					farmaci.add(new EntityFarmaco(farmaco));
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		} catch (DBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Funzione statica per richiamare l'unica istanza di <code>EntityCatalogo</code> o crearne una se non esiste già.
	 * @return l'istanza singleton di <code>EntityCatalogo</code>.
	 */
	public static EntityCatalogo getInstance() {
		if(uniqueInstance == null) {
			uniqueInstance = new EntityCatalogo();
		}
		return uniqueInstance;
	}

	public void aggiungiFarmaco(float prezzo, boolean prescrizione, String nome, int scortaIniziale) throws FarmacoCreationFailedException {
		EntityFarmaco nuovoFarmaco = new EntityFarmaco(prezzo, prescrizione, nome, scortaIniziale);
		try {
			nuovoFarmaco.salvaInDB();
			farmaci.add(nuovoFarmaco);
		} catch (DBException e) {
			throw new FarmacoCreationFailedException(e.getMessage());
		}
	}

	public void modificaFarmaco(int id, float prezzo, boolean prescrizione, String nome, int scorta) throws FarmacoNotFoundException {
		// TODO: necessito di FarmacoDAO.updateFarmaco()
	}

	public EntityFarmaco cercaFarmaco(int id) throws FarmacoNotFoundException {
		for (EntityFarmaco farmaco : farmaci) {
			if (farmaco.getId() == id) {
				return farmaco;
			}
		}
		throw new FarmacoNotFoundException("Farmaco non trovato");
	}

	/**
	 * Funzione che aggiorna le scorte in <code>EntityCatalogo</code> e nel DB.
	 * @param idFarmaco id del farmaco di cui aggiornare le scorte.
	 * @param quantita quantità da sottrarre alle scorte.
	 * @return numero di scorte rimanenti del farmaco.
	 */
	public int decrementaScorte(int idFarmaco, int quantita) throws FarmacoNotFoundException {
		for (EntityFarmaco farmaco : farmaci) {
			if (farmaco.getId() == idFarmaco) {
				int differenza = farmaco.getScorta() - quantita;
				farmaco.setScorta(differenza);
				this.modificaFarmaco(farmaco.getId(), farmaco.getPrezzo(), farmaco.isPrescrizione(), farmaco.getNome(), farmaco.getScorta());
				return differenza;
			}
		}
		throw new FarmacoNotFoundException("Farmaco non trovato");
	}

	/**
	 * @param farmacoQuantita serie di coppie (idFarmaco,quantita)
	 * @return true se tutte i farmaci di <code>farmacoQuantita</code> hanno scorte sufficienti, false altrimenti
	 */
	public boolean checkScorte(Map<Integer, Integer> farmacoQuantita) {
		boolean trovato = false;
		for (Integer id : farmacoQuantita.keySet()) {
			for (EntityFarmaco farmaco : farmaci) {
				if (farmaco.getId() == id) {
					trovato = true;
					if (farmaco.getScorta() - farmacoQuantita.get(id) < 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public ArrayList<EntityFarmaco> visualizza() {
		return farmaci;
	}
}
