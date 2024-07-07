package farmacia.entity;

import farmacia.database.FarmacoDAO;
import farmacia.database.OrdineDAO;
import farmacia.exceptions.DBException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityOrdine {
	private final String id;
	private Date dataCreazione;
	private boolean ritirato;
	private final EntityCliente cliente;
	private final Map<EntityFarmaco, Integer> quantitaFarmaci;

	/**
	 * Costruttore di <code>EntityOrdine</code>
	 * @param cliente Cliente che ha creato l'ordine.
	 */
	public EntityOrdine(EntityCliente cliente) {
		this.id = UUID.randomUUID().toString();
		this.dataCreazione = new Date();
		this.ritirato = false;
		this.cliente = cliente;
		this.quantitaFarmaci = new HashMap<>();
	}

	public EntityOrdine(String id) throws DBException {
		this(new OrdineDAO(id));
	}

	public EntityOrdine(OrdineDAO ordineDAO) throws DBException {
		this.id = ordineDAO.getId();
		this.dataCreazione = ordineDAO.getDataCreazione();
		this.ritirato = ordineDAO.isRitirato();
		this.dataCreazione = ordineDAO.getDataCreazione();
		this.cliente = new EntityCliente(ordineDAO.getCliente());
		quantitaFarmaci = new HashMap<>();
		for (Map.Entry<FarmacoDAO, Integer> entry : ordineDAO.getOrdineFarmaci().entrySet()) {
			quantitaFarmaci.put(new EntityFarmaco(entry.getKey()), entry.getValue());
		}
	}

	/**
	 * Funzione che permette di creare un nuovo Ordine nel DB a partire da
	 * un'istanza di <code>EntityOrdine</code> già popolata.
	 * @throws DBException Errore generico del DB
	 */
	public void salvaInDB() throws DBException {
		OrdineDAO ordineDAO = new OrdineDAO(this.id, this.dataCreazione, this.cliente.getId());
		for (Map.Entry<EntityFarmaco, Integer> entry : quantitaFarmaci.entrySet()) {
			ordineDAO.aggiungiOrdineFarmaco(entry.getKey().getId(), entry.getValue());
		}
		ordineDAO.createOrdine();
	}

	public void aggiorna() throws DBException {
		OrdineDAO ordine = new OrdineDAO(this.id);
		ordine.aggiorna();
		this.ritirato = true;
	}

	public void aggiungiOrdineFarmaco(EntityFarmaco farmaco, int quantita) {
		quantitaFarmaci.put(farmaco, quantita);
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

	public EntityCliente getCliente() {
		return cliente;
	}
}
