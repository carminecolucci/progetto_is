package farmacia.controller;

import farmacia.dto.DTO;
import farmacia.entity.EntityFarmacia;
import farmacia.entity.EntityFarmaco;
import farmacia.entity.EntityOrdine;
import farmacia.exceptions.DBException;
import farmacia.exceptions.ReportException;

import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;

public class ControllerReport {

	private static ControllerReport uniqueInstance;

	private ControllerReport() {}

	public static ControllerReport getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ControllerReport();
		}
		return uniqueInstance;
	}

	public DTO generaReport(Date dataInizio, Date dataFine) throws ReportException {
		DTO report = new DTO();
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		int venditeBanco = 0;
		int venditePrescrizione = 0;
		float incassoBanco = 0;
		float incassoPrescrizione = 0;

		try {
			List<EntityOrdine> ordini = farmacia.visualizzaOrdini();
			Map<String, Integer> farmaciVendutiBanco = new HashMap<>();
			Map<String, Integer> farmaciVendutiPrescrizione = new HashMap<>();

			for (EntityOrdine ordine : ordini) {
				Date dataCreazione = ordine.getDataCreazione();
				if (!(dataCreazione.before(dataInizio) || dataCreazione.after(dataFine))) {
					Map<EntityFarmaco, Integer> quantitaFarmaci = ordine.getQuantitaFarmaci();
					for (Map.Entry<EntityFarmaco, Integer> entry : quantitaFarmaci.entrySet()) {
						EntityFarmaco farmaco = entry.getKey();
						int quantita = entry.getValue();
						if (farmaco.isPrescrizione()){
							if (farmaciVendutiPrescrizione.containsKey(farmaco.getNome())){
								farmaciVendutiPrescrizione.put(farmaco.getNome(), farmaciVendutiPrescrizione.get(farmaco.getNome()) + quantita);
							} else {
								farmaciVendutiPrescrizione.put(farmaco.getNome(), quantita);
							}
							venditePrescrizione += quantita;
							incassoPrescrizione += quantita * farmaco.getPrezzo();
						} else {
							if (farmaciVendutiBanco.containsKey(farmaco.getNome())){
								farmaciVendutiBanco.put(farmaco.getNome(), farmaciVendutiBanco.get(farmaco.getNome()) + quantita);
							} else {
								farmaciVendutiBanco.put(farmaco.getNome(), quantita);
							}
							venditeBanco += quantita;
							incassoBanco += quantita * farmaco.getPrezzo();
						}
					}
				}
			}

			String nomePrescrizione = "-";
			int unitaPrescrizione = 0;
			String nomeBanco = "-";
			int unitaBanco = 0;

			for (Map.Entry<String, Integer> entry : farmaciVendutiPrescrizione.entrySet()) {
				if (entry.getValue() > unitaPrescrizione){
					unitaPrescrizione = entry.getValue();
					nomePrescrizione = entry.getKey();
				}
			}

			for (Map.Entry<String, Integer> entry : farmaciVendutiBanco.entrySet()) {
				if (entry.getValue() > unitaBanco){
					unitaBanco = entry.getValue();
					nomeBanco = entry.getKey();
				}
			}

			report.set("venditeBanco", venditeBanco);
			report.set("venditePrescrizione", venditePrescrizione);
			report.set("incassoBanco", incassoBanco);
			report.set("incassoPrescrizione", incassoPrescrizione);
			report.set("nomeBanco", nomeBanco);
			report.set("unitaBanco", unitaBanco);
			report.set("unitaPrescrizione", unitaPrescrizione);
			report.set("nomePrescrizione", nomePrescrizione);
			return report;

		} catch (DBException e) {
			throw new ReportException(e.getMessage());
		}
	}

}
