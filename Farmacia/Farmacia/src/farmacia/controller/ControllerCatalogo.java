package farmacia.controller;

import farmacia.dto.DTO;
import farmacia.entity.EntityCatalogo;
import farmacia.entity.EntityFarmaco;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoCreationFailedException;
import farmacia.exceptions.FarmacoNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ControllerCatalogo {

	/**
	 * L'unica istanza di <code>ControllerCatalogo</code> che implementa il pattern Singleton.
	 */
	private static ControllerCatalogo uniqueInstance;

	/**
	 * Costruttore privato per impedire la creazione di istanze multiple.
	 */
	private ControllerCatalogo() {}

	/**
	 * Funzione statica per richiamare l'unica istanza di <code>ControllerCatalogo</code> o crearne una se non esiste già.
	 * @return l'istanza singleton di <code>ControllerCatalogo</code>.
	 */
	public static ControllerCatalogo getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ControllerCatalogo();
		}
		return uniqueInstance;
	}

	/**
	 * Funzione che aggiunge un nuovo farmaco al catalogo.
	 * @param prezzo prezzo del farmaco.
	 * @param prescrizione se il farmaco richiede una prescrizione.
	 * @param nome nome del farmaco.
	 * @param scorteIniziali scorte del farmaco.
	 * @throws FarmacoCreationFailedException se non è possibile creare il farmaco.
	 */
	public void aggiungiFarmaco(float prezzo, boolean prescrizione, String nome, int scorteIniziali) throws FarmacoCreationFailedException {
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		catalogo.aggiungiFarmaco(prezzo, prescrizione, nome, scorteIniziali);
	}

	/**
	 * Funzione utilizzata per modificare un farmaco a partire dal suo id.
	 * @param id id del farmaco da modificare.
	 * @param prezzo nuovo prezzo del farmaco.
	 * @param prescrizione nuova prescrizione del farmaco.
	 * @param nome nuovo nome del farmaco.
	 * @param scorte nuove scorte del farmaco.
	 * @throws FarmacoNotFoundException se il farmaco non è presente nel catalogo.
	 */
	public void modificaFarmaco(int id, float prezzo, boolean prescrizione, String nome, int scorte) throws FarmacoNotFoundException {
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		catalogo.modificaFarmaco(id, prezzo, prescrizione, nome, scorte);
	}

	/**
	 * Funzione utilizzata per cercare un farmaco nel catalogo a partire dal nome.
	 * @param nome nome del farmaco da cercare.
	 * @return Farmaco DTO
	 * @throws FarmacoNotFoundException se il farmaco non è presente nel catalogo
	 */
	public DTO cercaFarmaco(String nome) throws FarmacoNotFoundException {
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		EntityFarmaco farmaco = catalogo.cercaFarmaco(nome);
		DTO dto = new DTO();
		dto.set("id", farmaco.getId());
		dto.set("prezzo", farmaco.getPrezzo());
		dto.set("prescrizione", farmaco.isPrescrizione());
		dto.set("nome", farmaco.getNome());
		dto.set("scorte", farmaco.getScorte());
		return dto;
	}

	/**
	 * Funzione utilizzata per eliminare un farmaco a partire dal suo id.
	 * @param id id del farmaco da eliminare.
	 * @throws FarmacoNotFoundException se il farmaco non è presente nel catalogo.
	 * @throws DBException se non è possibile accedere al DB.
	 */
	public void eliminaFarmaco(int id) throws FarmacoNotFoundException, DBException {
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		catalogo.eliminaFarmaco(id);
	}

	/**
	 * Funzione utilizzata per visualizzare il catalogo.
	 * @return Lista di Farmaci DTO.
	 */
	public List<DTO> visualizzaCatalogo() {
		List<DTO> farmaciDTO = new ArrayList<>();
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		List<EntityFarmaco> farmaci = catalogo.visualizza();
		for (EntityFarmaco farmaco : farmaci) {
			DTO dto = new DTO();
			dto.set("id", farmaco.getId());
			dto.set("prezzo", farmaco.getPrezzo());
			dto.set("prescrizione", farmaco.isPrescrizione());
			dto.set("nome", farmaco.getNome());
			dto.set("scorte", farmaco.getScorte());
			farmaciDTO.add(dto);
		}
		return farmaciDTO;
	}
}
