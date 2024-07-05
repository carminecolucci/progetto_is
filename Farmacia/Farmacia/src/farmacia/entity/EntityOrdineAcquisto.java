package farmacia.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EntityOrdineAcquisto {
	private int id;
	private Date dataCreazione;
	private boolean ritirato;
	private Map<EntityFarmaco, Integer> quantitaFarmaci;

	public EntityOrdineAcquisto() {
		quantitaFarmaci = new HashMap<EntityFarmaco, Integer>();
	}

	public void aggiungiOrdineAcquistoFarmaco(EntityFarmaco farmaco, int quantita) {
		quantitaFarmaci.put(farmaco, quantita);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

