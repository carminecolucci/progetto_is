package farmacia.entity;

import farmacia.database.FarmacoDAO;
import farmacia.database.OrdineDAO;
import farmacia.exceptions.DBException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe che rappresenta un ordine di un cliente.
 */
public class EntityOrdine {
	private final String id;
	private Date dataCreazione;
	private boolean ritirato;
	private final int idCliente;
	private final Map<EntityFarmaco, Integer> quantitaFarmaci;
	private float totale;

	/**
	 * Costruttore di <code>EntityOrdine</code> a partire dall'id del cliente.
	 * @param idCliente id del cliente che ha creato l'ordine.
	 */
	public EntityOrdine(int idCliente) {
		this.id = UUID.randomUUID().toString();
		this.dataCreazione = new Date();
		this.ritirato = false;
		this.idCliente = idCliente;
		this.quantitaFarmaci = new HashMap<>();
		this.totale = 0;
	}

	/**
	 * Costruttore di <code>EntityOrdine</code> a partire dall'id dell'ordine.
	 * @param id id dell'ordine.
	 * @throws DBException se non è possibile accedere al DB.
	 */
	public EntityOrdine(String id) throws DBException {
		this(new OrdineDAO(id));
	}

	/**
	 * Costruttore che permette di popolare un <code>EntityOrdine</code> a partire da un <code>OrdineDAO</code>
	 * @param ordineDAO dao dell'ordine.
	 */
	public EntityOrdine(OrdineDAO ordineDAO) {
		this.id = ordineDAO.getId();
		this.dataCreazione = ordineDAO.getDataCreazione();
		this.ritirato = ordineDAO.isRitirato();
		this.dataCreazione = ordineDAO.getDataCreazione();
		this.idCliente = ordineDAO.getCliente();
		this.totale = ordineDAO.getTotale();
		this.quantitaFarmaci = new HashMap<>();
		for (Map.Entry<FarmacoDAO, Integer> entry: ordineDAO.getOrdineFarmaci().entrySet()) {
			this.quantitaFarmaci.put(new EntityFarmaco(entry.getKey()), entry.getValue());
		}
	}

	/**
	 * Funzione che aggiunge un nuovo farmaco all'ordine.
	 * @param farmaco farmaco da aggiungere all'ordine.
	 * @param quantita quantità di farmaco da aggiungere.
	 */
	public void aggiungiFarmaco(EntityFarmaco farmaco, int quantita) {
		quantitaFarmaci.put(farmaco, quantita);
		this.totale += quantita * farmaco.getPrezzo();
	}

	/**
	 * Funzione che permette di creare un nuovo Ordine nel DB a partire da
	 * un'istanza di <code>EntityOrdine</code> già popolata.
	 * @throws DBException Se non è possibile accedere al DB.
	 */
	public void salvaInDB() throws DBException {
		OrdineDAO ordineDAO = new OrdineDAO(this.id, this.dataCreazione, this.idCliente, this.totale);
		for (Map.Entry<EntityFarmaco, Integer> entry: quantitaFarmaci.entrySet()) {
			ordineDAO.aggiungiFarmaco(entry.getKey().getId(), entry.getValue());
		}
		ordineDAO.createOrdine();
	}

	/**
	 * Funzione che aggiorna lo stato dell'ordine da "Non ritirato" a "Ritirato".
	 * @throws DBException Se non è possibile accedere al DB.
	 */
	public void aggiorna() throws DBException {
		OrdineDAO ordine = new OrdineDAO(this.id);
		ordine.aggiorna();
		this.ritirato = true;
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

	public boolean isRitirato() {
		return ritirato;
	}

	public void setRitirato(boolean ritirato) {
		this.ritirato = ritirato;
	}

	public Map<EntityFarmaco, Integer> getQuantitaFarmaci() {
		return quantitaFarmaci;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public float getTotale() {
		return totale;
	}

	public void setTotale(float totale) {
		this.totale = totale;
	}
}
