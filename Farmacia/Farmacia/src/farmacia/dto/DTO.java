package farmacia.dto;

import farmacia.entity.EntityFarmaco;
import farmacia.entity.EntityOrdine;
import farmacia.entity.EntityOrdineAcquisto;
import farmacia.entity.EntityUtente;
import farmacia.exceptions.DBException;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilizzata per lo scambio di informazioni tra il livello controller e il livello boundary.
 */
public class DTO {
	private final Map<String, Object> data;

	/**
	 * Costruttore di default di <code>DTO</code>, crea un'entità vuota.
	 */
	public DTO() {
		data = new HashMap<>();
	}

	/**
	 * Funzione che restituisce il valore associato alla chiave.
	 * @param key chiave da cercare nel <code>DTO</code>.
	 * @return un <code>Object</code>, valore di <code>key</code>.
	 */
	public Object get(String key) {
		return data.get(key);
	}

	/**
	 * Aggiunge la coppia (<code>key</code>, <code>value</code>) al <code>DTO</code>.
	 * @param key chiave da aggiungere.
	 * @param value valore da aggiungere.
	 */
	public void set(String key, Object value) {
		data.put(key, value);
	}

	/**
	 * Funzione di utilità che controlla se la coppia (<code>nomeFarmaco</code>, <code>quantita</code>) è presente in <code>quantitaFarmaci</code>.
	 * @param quantitaFarmaci mappa di coppie (<code>DTO</code>, <code>Integer</code>).
	 * @param nomeFarmaco nome del farmaco da cercare.
	 * @param quantita quantita del farmaco da cercare.
	 * @return true se la coppia (<code>nomeFarmaco</code>, <code>quantita</code>) è presente in <code>quantitaFarmaci</code>.
	 */
	public static boolean contains(Map<DTO, Integer> quantitaFarmaci, String nomeFarmaco, int quantita) {
		for (Map.Entry<DTO, Integer> entry: quantitaFarmaci.entrySet()) {
			if (entry.getKey().get("nome").equals(nomeFarmaco) && entry.getValue() == quantita) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Funzione di utilità che crea un <code>DTO</code> di un farmaco a partire da un <code>EntityFarmaco</code>.
	 * @param farmaco farmaco da trasformare.
	 * @return <code>DTO</code> che rappresenta un farmaco.
	 */
	public static DTO getDTOFarmaco(EntityFarmaco farmaco) {
		DTO dto = new DTO();
		dto.set("id", farmaco.getId());
		dto.set("prezzo", farmaco.getPrezzo());
		dto.set("prescrizione", farmaco.isPrescrizione());
		dto.set("nome", farmaco.getNome());
		dto.set("scorte", farmaco.getScorte());
		dto.set("codice", farmaco.getCodice());
		return dto;
	}

	/**
	 * Funzione di utilità che crea un <code>DTO</code> di un ordine a partire da un <code>EntityOrdine</code>.
	 * @param ordine ordine da trasformare.
	 * @throws DBException se non è possibile accedere al DB per prelevare l'username del cliente che ha creato l'ordine.
	 * @return <code>DTO</code> che rappresenta un ordine.
	 */
	public static DTO getDTOOrdine(EntityOrdine ordine) throws DBException {
		DTO dto = new DTO();
		dto.set("id", ordine.getId());
		dto.set("dataCreazione", ordine.getDataCreazione());
		dto.set("ritirato", ordine.isRitirato());
		dto.set("cliente", EntityUtente.getUsername(ordine.getIdCliente()));
		Map<DTO, Integer> dtoQuantitaFarmaci = getDtoQuantitaFarmaci(ordine.getQuantitaFarmaci());
		dto.set("quantitaFarmaci", dtoQuantitaFarmaci);
		dto.set("totale", ordine.getTotale());
		return dto;
	}

	/**
	 * Funzione di utilità che crea un <code>DTO</code> di un ordine di acquisto a partire da un <code>EntityOrdineAcquisto</code>.
	 * @param ordineAcquisto ordineAcquisto da trasformare.
	 * @return <code>DTO</code> che rappresenta un ordine di acquisto.
	 */
	public static DTO getDTOOrdineAcquisto(EntityOrdineAcquisto ordineAcquisto) {
		DTO dto = new DTO();
		dto.set("id", ordineAcquisto.getId());
		dto.set("dataCreazione", ordineAcquisto.getDataCreazione());
		dto.set("ricevuto", ordineAcquisto.isRicevuto());
		Map<DTO, Integer> dtoQuantitaFarmaci = getDtoQuantitaFarmaci(ordineAcquisto.getQuantitaFarmaci());
		dto.set("quantitaFarmaci", dtoQuantitaFarmaci);
		return dto;
	}

	/**
	 * Metodo privato che converte un <code>Map&lt;EntityFarmaco, Integer&gt;</code> in un <code>Map&lt;DTO, Integer&gt;</code>.
	 * @param quantitaFarmaci mappa che contiene una serie di coppie (<code>Farmaco DTO</code>, <code>quantità</code>).
	 */
	private static Map<DTO, Integer> getDtoQuantitaFarmaci(Map<EntityFarmaco, Integer> quantitaFarmaci) {
		Map<DTO, Integer> dtoQuantitaFarmaci = new HashMap<>();
		for (Map.Entry<EntityFarmaco, Integer> entry: quantitaFarmaci.entrySet()) {
			EntityFarmaco farmaco = entry.getKey();
			DTO dto = getDTOFarmaco(farmaco);
			dtoQuantitaFarmaci.put(dto, entry.getValue());
		}
		return dtoQuantitaFarmaci;
	}
}
