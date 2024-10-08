package farmacia.entity;

import farmacia.database.FarmacoDAO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoCreationFailedException;
import farmacia.exceptions.FarmacoNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe che rappresenta il catalogo della farmacia.
 */
public class EntityCatalogo {
	/**
	 * L'unica istanza di <code>EntityCatalogo</code> che implementa il pattern Singleton.
	 */
	private static EntityCatalogo uniqueInstance;
	private final List<EntityFarmaco> farmaci;
	private static final String FARMACO_NON_TROVATO = "Farmaco non trovato";

	/**
	 * Costruttore privato per impedire la creazione di istanze multiple.
	 */
	private EntityCatalogo() {
		farmaci = new ArrayList<>();
		try {
			for (FarmacoDAO farmaco: FarmacoDAO.getFarmaci()) {
				farmaci.add(new EntityFarmaco(farmaco));
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
		if (uniqueInstance == null) {
			uniqueInstance = new EntityCatalogo();
		}
		return uniqueInstance;
	}

	/**
	 * Aggiunge un nuovo farmaco nel catalogo.
	 * @param prezzo prezzo del farmaco.
	 * @param prescrizione se il farmaco richiede una prescrizione.
	 * @param nome nome del farmaco.
	 * @param scorte scorte del farmaco.
	 * @throws FarmacoCreationFailedException se non è possibile accedere al DB
	 */
	public void aggiungiFarmaco(float prezzo, boolean prescrizione, String nome, int scorte) throws FarmacoCreationFailedException {
		EntityFarmaco farmaco = new EntityFarmaco(prezzo, prescrizione, nome, scorte);
		try {
			farmaco.salvaInDB();
			farmaci.add(farmaco);
		} catch (DBException e) {
			throw new FarmacoCreationFailedException(e.getMessage());
		}
	}

	/**
	 * Modifica i dati di un farmaco cercato attraverso il suo id.
	 * @param id id del farmaco da eliminare.
	 * @param prezzo nuovo prezzo del farmaco
	 * @param prescrizione nuova prescrizione del farmaco.
	 * @param nome nuovo nome del farmaco.
	 * @param scorte nuove scorte del farmaco.
	 * @throws FarmacoNotFoundException se il farmaco non esiste.
	 */
	public void modificaFarmaco(int id, float prezzo, boolean prescrizione, String nome, int scorte) throws FarmacoNotFoundException {
		try {
			FarmacoDAO.aggiornaFarmacoDB(id, prezzo, prescrizione, nome, scorte);
			for (EntityFarmaco farmaco: farmaci) {
				if (farmaco.getId() == id) {
					farmaco.setPrezzo(prezzo);
					farmaco.setPrescrizione(prescrizione);
					farmaco.setNome(nome);
					farmaco.setScorte(scorte);
					break;
				}
			}
		} catch (DBException e) {
			throw new FarmacoNotFoundException(e.getMessage());
		}
	}

	/**
	 * Elimina un farmaco a partire dal suo id.
	 * @param id id del farmaco da eliminare.
	 * @throws FarmacoNotFoundException se il farmaco non esiste.
	 * @throws DBException se non è possibile accedere al DB.
	 */
	public void eliminaFarmaco(int id) throws FarmacoNotFoundException, DBException {
		for (EntityFarmaco farmaco: farmaci) {
			if (farmaco.getId() == id) {
				farmaco.eliminaDaDB();
				farmaci.remove(farmaco);
				return;
			}
		}
		throw new FarmacoNotFoundException(FARMACO_NON_TROVATO);
	}

	/**
	 * Cerca un farmaco nella farmacia a partire dal suo id.
	 * @param id id del farmaco.
	 * @return il farmaco cercato.
	 * @throws FarmacoNotFoundException se il farmaco non è stato trovato.
	 */
	public EntityFarmaco cercaFarmacoById(int id) throws FarmacoNotFoundException {
		for (EntityFarmaco farmaco: farmaci) {
			if (farmaco.getId() == id) {
				return farmaco;
			}
		}
		throw new FarmacoNotFoundException(FARMACO_NON_TROVATO);
	}

	/**
	 * Cerca un farmaco nella farmacia a partire dal suo nome.
	 * @param nome nome del farmaco.
	 * @return il farmaco cercato.
	 * @throws FarmacoNotFoundException se il farmaco non è stato trovato.
	 */
	public EntityFarmaco cercaFarmaco(String nome) throws FarmacoNotFoundException {
		for (EntityFarmaco farmaco: farmaci) {
			if (farmaco.getNome().equalsIgnoreCase(nome)) {
				return farmaco;
			}
		}
		throw new FarmacoNotFoundException(FARMACO_NON_TROVATO);
	}

	/**
	 * Funzione che incrementa le scorte in <code>EntityCatalogo</code> e nel DB.
	 * @param idFarmaco id del farmaco di cui aggiornare le scorte.
	 * @param quantita quantità da aggiungere alle scorte.
	 * @throws FarmacoNotFoundException se il farmaco non esiste.
	 * @return numero di scorte rimanenti del farmaco.
	 */
	public int incrementaScorte(int idFarmaco, int quantita) throws FarmacoNotFoundException {
		return aggiornaScorte(idFarmaco, quantita);
	}

	/**
	 * Funzione che decrementa le scorte in <code>EntityCatalogo</code> e nel DB.
	 * @param idFarmaco id del farmaco di cui aggiornare le scorte.
	 * @param quantita quantità da sottrarre alle scorte.
	 * @throws FarmacoNotFoundException se il farmaco non esiste.
	 * @return numero di scorte rimanenti del farmaco.
	 */
	public int decrementaScorte(int idFarmaco, int quantita) throws FarmacoNotFoundException {
		return aggiornaScorte(idFarmaco, -quantita);
	}

	/**
	 * Funzione privata che aggiorna le scorte in <code>EntityCatalogo</code> e nel DB.
	 * @param idFarmaco id del farmaco di cui aggiornare le scorte.
	 * @param quantita quantità da aggiungere (o da sottrarre, se negativa) alle scorte.
	 * @return numero di scorte rimanenti del farmaco.
	 */
	private int aggiornaScorte(int idFarmaco, int quantita) throws FarmacoNotFoundException {
		for (EntityFarmaco farmaco: farmaci) {
			if (farmaco.getId() == idFarmaco) {
				int nuoveScorte = farmaco.getScorte() + quantita;
				try {
					FarmacoDAO.aggiornaScorteDB(farmaco.getId(), nuoveScorte);
				} catch (DBException e) {
					throw new FarmacoNotFoundException(FARMACO_NON_TROVATO);
				}
				farmaco.setScorte(nuoveScorte);
				return nuoveScorte;
			}
		}
		throw new FarmacoNotFoundException(FARMACO_NON_TROVATO);
	}

	/**
	 * Funzione che controlla se ci sono abbastanza scorte nel catalogo.
	 * @param farmacoQuantita serie di coppie (idFarmaco, quantita)
	 * @return true se tutte i farmaci di <code>farmacoQuantita</code> hanno scorte sufficienti, false altrimenti
	 */
	public boolean checkScorte(Map<Integer, Integer> farmacoQuantita) {
		for (Map.Entry<Integer, Integer>  entry: farmacoQuantita.entrySet()) {
			for (EntityFarmaco farmaco: farmaci) {
				if (farmaco.getId() == entry.getKey() && farmaco.getScorte() - entry.getValue() < 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Funzione utilizzata per visualizzare il catalogo
	 * @return lista di <code>EntityFarmaco</code>
	 */
	public List<EntityFarmaco> visualizza() {
		return farmaci;
	}
}
