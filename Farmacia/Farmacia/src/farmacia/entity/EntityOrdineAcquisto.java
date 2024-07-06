package farmacia.entity;

import farmacia.database.FarmacoDAO;
import farmacia.database.OrdineAcquistoDAO;
import farmacia.database.OrdineDAO;
import farmacia.exceptions.DBException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityOrdineAcquisto {
	private String id;
	private Date dataCreazione;
	private boolean ritirato;
	private Map<EntityFarmaco, Integer> quantitaFarmaci;

	public EntityOrdineAcquisto() {
		quantitaFarmaci = new HashMap<>();
	}

	public EntityOrdineAcquisto(OrdineAcquistoDAO ordineAcquistoDAO) {
		this.id = ordineAcquistoDAO.getId();
		this.dataCreazione = ordineAcquistoDAO.getDataCreazione();
		this.ritirato = ordineAcquistoDAO.isRicevuto();
		quantitaFarmaci = new HashMap<>();
		for (Map.Entry<FarmacoDAO, Integer> entry : ordineAcquistoDAO.getOrdineAcquistoFarmaci().entrySet()) {
			quantitaFarmaci.put(new EntityFarmaco(entry.getKey()), entry.getValue());
		}
	}

	public void aggiungiOrdineAcquistoFarmaco(EntityFarmaco farmaco, int quantita) {
		quantitaFarmaci.put(farmaco, quantita);
	}

	/**
	 * Funzione che permette di creare un nuovo OrdineAcquisto nel DB a partire da
	 * un'istanza di <code>EntityOrdineAcquisto</code> già popolata.
	 * @throws DBException Errore generico del DB
	 */
	public void salvaInDB() throws DBException {
		this.id = UUID.randomUUID().toString();
		this.ritirato = false;
		this.dataCreazione = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
		OrdineAcquistoDAO ordineAcquistoDAO = new OrdineAcquistoDAO(this.id, this.dataCreazione, this.ritirato);
		for (Map.Entry<EntityFarmaco, Integer> entry : quantitaFarmaci.entrySet()) {
			ordineAcquistoDAO.aggiungiOrdineAcquistoFarmaco(entry.getKey().getId(), entry.getValue());
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

	public boolean isRitirato() {
		return ritirato;
	}

	public void setRitirato(boolean ritirato) {
		this.ritirato = ritirato;
	}
}

