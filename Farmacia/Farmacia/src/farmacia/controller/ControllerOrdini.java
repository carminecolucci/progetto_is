package farmacia.controller;

import farmacia.dto.DTO;
import farmacia.entity.*;
import farmacia.exceptions.DBException;
import farmacia.exceptions.OrderCreationFailedException;
import farmacia.exceptions.OrderNotFoundException;

import java.util.*;

public class ControllerOrdini {
	/**
	 * L'unica istanza di <code>ControllerOrdini</code> che implementa il pattern Singleton.
	 */
	private static ControllerOrdini uniqueInstance;

	/**
	 * Costruttore privato per impedire la creazione di istanze multiple.
	 */
	private ControllerOrdini() {}

	/**
	 * Funzione statica per richiamare l'unica istanza di <code>ControllerOrdini</code> o crearne una se non esiste già.
	 * @return l'istanza singleton di <code>ControllerOrdini</code>.
	 */
	public static ControllerOrdini getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ControllerOrdini();
		}
		return uniqueInstance;
	}

	public void creaOrdine(Map<Integer, Integer> farmaciQuantita) throws OrderCreationFailedException {
		EntityCliente cliente = (EntityCliente) Sessione.getInstance().getUtenteCorrente();
		cliente.creaOrdine(farmaciQuantita);
	}

	public List<DTO> visualizzaStoricoOrdini() {
		EntityCliente cliente = (EntityCliente) Sessione.getInstance().getUtenteCorrente();
		List<EntityOrdine> ordini = cliente.visualizzaStoricoOrdini();
		return getDtoOrdini(ordini);
	}

	private static Map<DTO, Integer> getDtoQuantitaFarmaci(Map<EntityFarmaco, Integer> quantitaFarmaci) {
		Map<DTO, Integer> dtoQuantitaFarmaci = new HashMap<>();
		for (Map.Entry<EntityFarmaco, Integer> entry: quantitaFarmaci.entrySet()) {
			DTO dtoFarmaco = new DTO();
			EntityFarmaco farmaco = entry.getKey();
			dtoFarmaco.set("id", farmaco.getId());
			dtoFarmaco.set("prezzo", farmaco.getPrezzo());
			dtoFarmaco.set("prescrizione", farmaco.isPrescrizione());
			dtoFarmaco.set("nome", farmaco.getNome());
			dtoFarmaco.set("scorte", farmaco.getScorte());
			dtoQuantitaFarmaci.put(dtoFarmaco, entry.getValue());
		}
		return dtoQuantitaFarmaci;
	}

	public void creaOrdineAcquistoFarmacia(Map<Integer, Integer> farmaciQuantita) throws DBException, OrderCreationFailedException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		farmacia.creaOrdineAcquisto(farmaciQuantita);
	}

	public List<DTO> visualizzaOrdiniAcquistoFarmacia() throws DBException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		List<DTO> dtoOrdiniAcquisto = new ArrayList<>();

		List<EntityOrdineAcquisto> ordiniAcquisto = farmacia.visualizzaOrdiniAcquisto();
		for (EntityOrdineAcquisto ordineAcquisto : ordiniAcquisto) {
			DTO dtoOrdineAcquisto = new DTO();
			dtoOrdineAcquisto.set("id", ordineAcquisto.getId());
			dtoOrdineAcquisto.set("dataCreazione", ordineAcquisto.getDataCreazione());
			dtoOrdineAcquisto.set("ricevuto", ordineAcquisto.isRicevuto());
			Map<DTO, Integer> dtoQuantitaFarmaci = getDtoQuantitaFarmaci(ordineAcquisto.getQuantitaFarmaci());
			dtoOrdineAcquisto.set("quantitaFarmaci", dtoQuantitaFarmaci);
			dtoOrdiniAcquisto.add(dtoOrdineAcquisto);
		}

		return dtoOrdiniAcquisto;
	}

	public List<DTO> visualizzaOrdiniFarmacia() throws DBException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		List<EntityOrdine> ordini = farmacia.visualizzaOrdini();
		return getDtoOrdini(ordini);
	}

	private List<DTO> getDtoOrdini(List<EntityOrdine> ordini) {
		List<DTO> dtoOrdini = new ArrayList<>();
		for (EntityOrdine ordine : ordini) {
			DTO dtoOrdine = new DTO();
			dtoOrdine.set("id", ordine.getId());
			dtoOrdine.set("dataCreazione", ordine.getDataCreazione());
			dtoOrdine.set("ritirato", ordine.isRitirato());
			dtoOrdine.set("cliente", ordine.getCliente());
			Map<DTO, Integer> dtoQuantitaFarmaci = getDtoQuantitaFarmaci(ordine.getQuantitaFarmaci());
			dtoOrdine.set("quantitaFarmaci", dtoQuantitaFarmaci);
			dtoOrdini.add(dtoOrdine);
		}
		return dtoOrdini;
	}

	public void aggiornaOrdine(String idOrdine) throws OrderNotFoundException, DBException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		farmacia.aggiornaOrdine(idOrdine);
	}

	public void aggiornaOrdineAcquisto(String idOrdine) throws OrderNotFoundException, DBException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		farmacia.aggiornaOrdineAcquisto(idOrdine);
	}
}
