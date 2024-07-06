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
		if(uniqueInstance == null) {
			uniqueInstance = new ControllerCatalogo();
		}
		return uniqueInstance;
	}

	public void aggiungiFarmaco(float prezzo, boolean prescrizione, String nome, int scorteIniziali) throws FarmacoCreationFailedException {
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		catalogo.aggiungiFarmaco(prezzo, prescrizione, nome, scorteIniziali);
	}

	public void modificaFarmaco(int id, float prezzo, boolean prescrizione, String nome, int scorte) throws FarmacoNotFoundException {
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		catalogo.modificaFarmaco(id, prezzo, prescrizione, nome, scorte);
	}

	public DTO cercaFarmacoByID(int id) throws FarmacoNotFoundException {
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		EntityFarmaco farmaco = catalogo.cercaFarmaco(id);
		DTO dto = new DTO();
		dto.set("id", farmaco.getId());
		dto.set("prezzo", farmaco.getPrezzo());
		dto.set("prescrizione", farmaco.isPrescrizione());
		dto.set("nome", farmaco.getNome());
		dto.set("scorte", farmaco.getScorte());
		return dto;
	}

	public void eliminaFarmaco(int id) throws FarmacoNotFoundException, DBException {
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		catalogo.eliminaFarmaco(id);
	}

	public List<DTO> visualizzaCatalogo() {
		ArrayList<DTO> farmaciDTO = new ArrayList<>();
		EntityCatalogo catalogo = EntityCatalogo.getInstance();
		ArrayList<EntityFarmaco> farmaci = catalogo.visualizza();
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
