package farmacia.controller;

import farmacia.dto.DTO;
import farmacia.entity.*;
import farmacia.exceptions.*;

import java.util.*;

/**
 * Classe che ha la responsabilità di gestire gli ordini degli utenti e gli ordini di fornitura della farmacia.
 */
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
	 *     <li>"quantitaFarmaci": <code>quantitaFarmaci</code> dell'ordine (<code>Map&lt;DTO, Integer&gt;</code>). La chiave
	 *     di questo <code>Map</code> è un'istanza di <code>DTO</code> rappresentante un farmaco.</li>
	 *     <li>"cliente": <code>cliente</code> titolare dell'ordine (<code>String</code>)</li>
	 *     <li>"totale": <code>totale</code> dell'ordine (<code>float</code>)</li>
	 * </ul>
	 * @throws DBException se non è possibile accedere al DB.
	 */
	public List<DTO> visualizzaStoricoOrdini() throws DBException {
		EntityCliente cliente = (EntityCliente) Sessione.getInstance().getUtenteCorrente();
		List<EntityOrdine> ordini = cliente.visualizzaStoricoOrdini();
		return getDtoOrdini(ordini);
	}

	/**
	 * Funzione che permette di creare un ordine di acquisto. Viene richiamata quando un ordine da parte di un cliente
	 * azzera le scorte di un determinato farmaco ({@link #creaOrdine(Map)}) o quando un farmacista decide di generare
	 * un ordine di acquisto.
	 * @param farmaciQuantita mappa che contiene una serie di coppie (<code>idFarmaco</code>, <code>quantità</code>).
	 * @return l'<code>id</code> dell'ordine appena creato.
	 * @throws OrderCreationFailedException quando la creazione dell'ordine fallisce
	 */
	public String creaOrdineAcquistoFarmacia(Map<Integer, Integer> farmaciQuantita) throws OrderCreationFailedException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		return farmacia.creaOrdineAcquisto(farmaciQuantita);
	}

	/**
	 * Funzione che permette di ottenere tutti gli ordini di acquisto memorizzati nella farmacia.
	 * @return una <code>List&lt;DTO&gt;</code>, in cui ogni istanza di <code>DTO</code> contiene un singolo ordine di acquisto.
	 * In particolare il <code>DTO</code> contiene i seguenti campi:
	 * <ul>
	 *     <li>"id": <code>id</code> dell'ordine di acquisto (<code>String</code>)</li>
	 *     <li>"dataCreazione": <code>dataCreazione</code> dell'ordine di acquisto (<code>Date</code>)</li>
	 *     <li>"ricevuto": <code>ricevuto</code> dell'ordine di acquisto (<code>boolean</code>)</li>
	 *     <li>"quantitaFarmaci": <code>quantitaFarmaci</code> dell'ordine di acquisto (<code>Map&lt;DTO, Integer&gt;</code>)</li>
	 * </ul>
	 */
	public List<DTO> visualizzaOrdiniAcquistoFarmacia() {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		List<DTO> dtoOrdiniAcquisto = new ArrayList<>();

		List<EntityOrdineAcquisto> ordiniAcquisto = farmacia.visualizzaOrdiniAcquisto();
		for (EntityOrdineAcquisto ordineAcquisto: ordiniAcquisto) {
			DTO dtoOrdineAcquisto = DTO.getDTOOrdineAcquisto(ordineAcquisto);
			dtoOrdiniAcquisto.add(dtoOrdineAcquisto);
		}

		return dtoOrdiniAcquisto;
	}

	/**
	 * Funzione che permette di ottenere tutti gli ordini di tutti i clienti.
	 * @return una <code>List&lt;DTO&gt;</code>, in cui ogni istanza di <code>DTO</code> contiene un singolo ordine di un cliente.
	 * In particolare il <code>DTO</code> contiene i seguenti campi:
	 * <ul>
	 *     <li>"id": <code>id</code> dell'ordine (<code>String</code>)</li>
	 *     <li>"dataCreazione": <code>dataCreazione</code> dell'ordine (<code>Date</code>)</li>
	 *     <li>"ritirato": <code>ritirato</code> dell'ordine (<code>boolean</code>)</li>
	 *     <li>"quantitaFarmaci": <code>quantitaFarmaci</code> dell'ordine (<code>Map&lt;DTO, Integer&gt;</code>). La chiave
	 *     di questo <code>Map</code> è un'istanza di <code>DTO</code> rappresentante un farmaco.</li>
	 *     <li>"cliente": <code>cliente</code> titolare dell'ordine (<code>String</code>)</li>
	 *     <li>"totale": <code>totale</code> dell'ordine (<code>float</code>)</li>
	 * </ul>
	 * @throws DBException se non è possibile accedere al DB.
	 */
	public List<DTO> visualizzaOrdiniFarmacia() throws DBException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		List<EntityOrdine> ordini = farmacia.visualizzaOrdini();
		return getDtoOrdini(ordini);
	}

	/**
	 * Funzione privata di utilità che converte una <code>List&lt;EntityOrdine&gt;</code> in una <code>List&lt;DTO&gt;</code>.
	 * @param ordini <code>List&lt;EntityOrdine&gt;</code> da convertire
	 @return una <code>List&lt;DTO&gt;</code>, in cui ogni istanza di <code>DTO</code> contiene un singolo ordine di un cliente.
	  * In particolare il <code>DTO</code> contiene i seguenti campi:
	  * <ul>
	  *     <li>"id": <code>id</code> dell'ordine (<code>String</code>)</li>
	  *     <li>"dataCreazione": <code>dataCreazione</code> dell'ordine (<code>Date</code>)</li>
	  *     <li>"ritirato": <code>ritirato</code> dell'ordine (<code>boolean</code>)</li>
	  *     <li>"quantitaFarmaci": <code>quantitaFarmaci</code> dell'ordine (<code>Map&lt;DTO, Integer&gt;</code>). La chiave
	  *     di questo <code>Map</code> è un'istanza di <code>DTO</code> rappresentante un farmaco.</li>
	  *     <li>"cliente": username del <code>cliente</code> titolare dell'ordine (<code>String</code>)</li>
	  *     <li>"totale": <code>totale</code> dell'ordine (<code>float</code>)</li>
	  * </ul>
	 * @throws DBException se non è possibile accedere al DB
	 */
	private List<DTO> getDtoOrdini(List<EntityOrdine> ordini) throws DBException {
		List<DTO> dtoOrdini = new ArrayList<>();
		for (EntityOrdine ordine: ordini) {
			DTO dtoOrdine = DTO.getDTOOrdine(ordine);
			dtoOrdini.add(dtoOrdine);
		}
		return dtoOrdini;
	}

	/**
	 * Funzione che cambia lo stato di un ordine da 'Non ritirato' a 'Ritirato' cercandolo con il suo <code>id</code>.
	 * @param idOrdine viene usato per la ricerca dell'ordine.
	 * @throws OrderNotFoundException quando non esiste un ordine con <code>id</code> uguale a <code>idOrdine</code>.
	 */
	public void aggiornaOrdine(String idOrdine) throws OrderNotFoundException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		farmacia.aggiornaOrdine(idOrdine);
	}

	/**
	 * Funzione che cambia lo stato di un ordine di acquisto da 'Non ritirato' a 'Ritirato' cercandolo con il suo <code>id</code>.
	 * @param idOrdine viene usato per la ricerca dell'ordine di acquisto.
	 * @throws OrderNotFoundException quando non esiste un ordine di acquisto con <code>id</code> uguale a <code>idOrdine</code>.
	 * @throws FarmacoNotFoundException se uno dei farmaci all'interno dell'ordine di acquisto non esiste
	 */
	public void aggiornaOrdineAcquisto(String idOrdine) throws OrderNotFoundException, FarmacoNotFoundException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		farmacia.aggiornaOrdineAcquisto(idOrdine);
	}
}
