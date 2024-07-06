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
		farmaci = new ArrayList<>();
		try {
			for (FarmacoDAO farmaco : FarmacoDAO.getFarmaci()) {
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

	public void aggiungiFarmaco(float prezzo, boolean prescrizione, String nome, int scorteIniziali) throws FarmacoCreationFailedException {
		EntityFarmaco nuovoFarmaco = new EntityFarmaco(prezzo, prescrizione, nome, scorteIniziali);
		try {
			nuovoFarmaco.salvaInDB();
			farmaci.add(nuovoFarmaco);
		} catch (DBException e) {
			throw new FarmacoCreationFailedException(e.getMessage());
		}
	}

	public void modificaFarmaco(int id, float prezzo, boolean prescrizione, String nome, int scorte) throws FarmacoNotFoundException {
		try {
			FarmacoDAO.aggiornaFarmacoDB(id, prezzo, prescrizione, nome, scorte);
			for (EntityFarmaco farmaco : farmaci) {
				if (farmaco.getId() == id) {
					farmaco.setPrezzo(prezzo);
					farmaco.setPrescrizione(prescrizione);
					farmaco.setNome(nome);
					farmaco.setScorte(scorte);
				}
			}
		} catch (DBException e) {
			throw new FarmacoNotFoundException(e.getMessage());
		}
	}

	public void eliminaFarmaco(int id) throws FarmacoNotFoundException, DBException {
		for (EntityFarmaco farmaco : farmaci) {
			if (farmaco.getId() == id) {
				farmaco.eliminaDaDB();
			}
		}
		throw new FarmacoNotFoundException("Farmaco non trovato");
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
				int differenza = farmaco.getScorte() - quantita;
				farmaco.setScorte(differenza);
				try {
					FarmacoDAO.aggiornaScorteDB(farmaco.getId(), farmaco.getScorte());
				} catch (DBException e) {
					throw new FarmacoNotFoundException("Farmaco non trovato");
				}
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
					if (farmaco.getScorte() - farmacoQuantita.get(id) < 0) {
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
