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

	/**
	 * Funzione che permette di creare un ordine. È necessario che un cliente sia loggato nel sistema.
	 * @param farmaciQuantita mappa che contiene una serie di coppie (<code>idFarmaco</code>, <code>quantità</code>).
	 * @return l'<code>id</code> dell'ordine appena creato.
	 * @throws OrderCreationFailedException quando la creazione dell'ordine fallisce
	 */
	public String creaOrdine(Map<Integer, Integer> farmaciQuantita) throws OrderCreationFailedException {
		EntityCliente cliente = (EntityCliente) Sessione.getInstance().getUtenteCorrente();
		return cliente.creaOrdine(farmaciQuantita);
	}

	/**
	 * Funzione che permette di ottenere lo storico degli ordini di un dato cliente. È necessario che un cliente sia
	 * loggato nel sistema.
	 * @return una <code>List&lt;DTO&gt;</code>, in cui ogni istanza di <code>DTO</code> contiene un singolo ordine di un cliente.
	 * In particolare il <code>DTO</code> contiene i seguenti campi:
	 * <ul>
	 *     <li>"id": <code>id</code> dell'ordine (<code>String</code>)</li>
	 *     <li>"dataCreazione": <code>dataCreazione</code> dell'ordine (<code>Date</code>)</li>
	 *     <li>"ritirato": <code>ritirato</code> dell'ordine (<code>boolean</code>)</li>
	 *     <li>"quantitaFarmaci": <code>quantitaFarmaci</code> dell'ordine (<code>DTO</code>)</li>
	 *     <li>"totale": <code>totale</code> dell'ordine (<code>float</code>)</li>
	 * </ul>
	 * @throws DBException
	 */
	public List<DTO> visualizzaStoricoOrdini() throws DBException {
		EntityCliente cliente = (EntityCliente) Sessione.getInstance().getUtenteCorrente();
		List<EntityOrdine> ordini = cliente.visualizzaStoricoOrdini();
		return getDtoOrdini(ordini);
	}

	/**
	 * Metodo privato che converte un <code>Map&lt;EntityFarmaco, Integer&gt;</code> in un <code>Map&lt;DTO, Integer&gt;</code>.
	 * @param quantitaFarmaci
	 */
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

	private List<DTO> getDtoOrdini(List<EntityOrdine> ordini) throws DBException {
		List<DTO> dtoOrdini = new ArrayList<>();
		for (EntityOrdine ordine : ordini) {
			DTO dtoOrdine = new DTO();
			dtoOrdine.set("id", ordine.getId());
			dtoOrdine.set("dataCreazione", ordine.getDataCreazione());
			dtoOrdine.set("ritirato", ordine.isRitirato());
			dtoOrdine.set("cliente", EntityUtente.getUsername(ordine.getIdCliente()));
			Map<DTO, Integer> dtoQuantitaFarmaci = getDtoQuantitaFarmaci(ordine.getQuantitaFarmaci());
			dtoOrdine.set("quantitaFarmaci", dtoQuantitaFarmaci);
			dtoOrdine.set("totale", ordine.getTotale());
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
