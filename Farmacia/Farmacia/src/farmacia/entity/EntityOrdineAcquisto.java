package farmacia.entity;

import farmacia.database.FarmacoDAO;
import farmacia.database.OrdineAcquistoDAO;
import farmacia.exceptions.DBException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe che rappresenta un ordine di fornitura richiesto dalla farmacia.
 */
public class EntityOrdineAcquisto {
	private final String id;
	private Date dataCreazione;
	private boolean ricevuto;
	private final Map<EntityFarmaco, Integer> quantitaFarmaci;

	/**
	 * Quantità da ordinare di default per un farmaco quando viene creato un ordine di acquisto, in seguito a un ordine
	 * che ha provocato la terminazione delle scorte di uno o più farmaci.
	 */
	public static final int QUANTITA_ORDINE_DEFAULT = 50;

	/**
	 * Costruttore di default di <code>EntityOrdineAcquisto</code>. Crea un nuovo ordine d'acquisto, da popolare
	 * attraverso il metodo {@link #aggiungiFarmaco(EntityFarmaco, int)}.
	 */
	public EntityOrdineAcquisto() {
		quantitaFarmaci = new HashMap<>();
		this.id = UUID.randomUUID().toString();
		this.ricevuto = false;
		this.dataCreazione = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
	}

	/**
	 * Costruttore che permette di popolare un <code>EntityOrdineAcquisto</code> a partire da un <code>OrdineAcquistoDAO</code>.
	 * @param ordineAcquistoDAO dao dell'ordine di acquisto.
	 */
	public EntityOrdineAcquisto(OrdineAcquistoDAO ordineAcquistoDAO) {
		this.id = ordineAcquistoDAO.getId();
		this.dataCreazione = ordineAcquistoDAO.getDataCreazione();
		this.ricevuto = ordineAcquistoDAO.isRicevuto();
		quantitaFarmaci = new HashMap<>();
		for (Map.Entry<FarmacoDAO, Integer> entry: ordineAcquistoDAO.getOrdiniAcquistoFarmaci().entrySet()) {
			quantitaFarmaci.put(new EntityFarmaco(entry.getKey()), entry.getValue());
		}
	}

	/**
	 * Funzione che aggiunge un nuovo farmaco all'ordine di acquisto.
	 * @param farmaco farmaco da aggiungere all'ordine.
	 * @param quantita quantità di farmaco da aggiungere.
	 */
	public void aggiungiFarmaco(EntityFarmaco farmaco, int quantita) {
		quantitaFarmaci.put(farmaco, quantita);
	}

	/**
	 * Funzione che aggiorna lo stato dell'ordine da "In consegna" a "Ricevuto".
	 * @throws DBException se non è possibile accedere al DB.
	 */
	public void aggiorna() throws DBException {
		OrdineAcquistoDAO ordine = new OrdineAcquistoDAO(this.id);
		ordine.aggiorna();
		this.ricevuto = true;
	}

	/**
	 * Funzione che permette di creare un nuovo OrdineAcquisto nel DB a partire da
	 * un'istanza di <code>EntityOrdineAcquisto</code> già popolata.
	 * @throws DBException Errore generico del DB
	 */
	public void salvaInDB() throws DBException {
		OrdineAcquistoDAO ordineAcquistoDAO = new OrdineAcquistoDAO(this.id, this.dataCreazione, this.ricevuto);
		for (Map.Entry<EntityFarmaco, Integer> entry: quantitaFarmaci.entrySet()) {
			ordineAcquistoDAO.aggiungiFarmaco(entry.getKey().getId(), entry.getValue());
		}
		ordineAcquistoDAO.createOrdineAcquisto();
	}

	public String getId() {
		return id;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public boolean isRicevuto() {
		return ricevuto;
	}

	public void setRicevuto(boolean ricevuto) {
		this.ricevuto = ricevuto;
	}

	public Map<EntityFarmaco, Integer> getQuantitaFarmaci() {
		return quantitaFarmaci;
	}
}

