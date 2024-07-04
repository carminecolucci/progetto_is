package farmacia.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EntityOrdine {
	private int id;
	private Date dataCreazione;
	private boolean ritirato;
	private Map<EntityFarmaco, Integer> quantitaFarmaci;

	public EntityOrdine() {
		quantitaFarmaci = new HashMap<EntityFarmaco, Integer>();
	}

	public void aggiungiOrdineFarmaco(EntityFarmaco farmaco, int quantita) {
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
