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

/**
 * Classe che ha la responsabilità di gestire la creazione dei report del direttore della farmacia.
 */
public class ControllerReport {
	/**
	 * L'unica istanza di <code>ControllerReport</code> che implementa il pattern Singleton.
	 */
	private static ControllerReport uniqueInstance;

	/**
	 * Costruttore privato per impedire la creazione di istanze multiple.
	 */
	private ControllerReport() {}

	/**
	 * Funzione statica per richiamare l'unica istanza di <code>ControllerReport</code> o crearne una se non esiste già.
	 * @return l'istanza singleton di <code>ControllerReport</code>.
	 */
	public static ControllerReport getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ControllerReport();
		}
		return uniqueInstance;
	}

	/**
	 * Funzione che genera un report sulle vendite da banco e da prescrizione in un dato intervallo di date.
	 * @param dataInizio data di inizio del report
	 * @param dataFine data di fine del report
	 * @return <code>DTO</code> contenente i seguenti campi:
	 * <ul>
	 *     <li>"venditeBanco": numero totale di farmaci da banco venduti (<code>int</code>)</li>
	 *     <li>"venditePrescrizione": numero totale di farmaci da prescrizione venduti (<code>int</code>)</li>
	 *     <li>"incassoBanco": incasso totale di tutte le vendite da banco (<code>float</code>)</li>
	 *     <li>"incassoPrescrizione": incasso totale di tutte le vendite da prescrizione (<code>float</code>)</li>
	 *     <li>"nomeBanco": nome del farmaco da banco più venduto (<code>String</code>)</li>
	 *     <li>"unitaBanco": numero di unità del farmaco <code>nomeBanco</code> vendute (<code>int</code>)</li>
	 *     <li>"nomePrescrizione": nome del farmaco da prescrizione più venduto (<code>String</code>)</li>
	 *     <li>"unitaPrescrizione": numero di unità del farmaco <code>nomePrescrizione</code> vendute (<code>int</code>)</li>
	 * </ul>
	 * @throws ReportException se non è possibile creare il report.
	 */
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

			for (EntityOrdine ordine: ordini) {
				Date dataCreazione = ordine.getDataCreazione();
				if (!(dataCreazione.before(dataInizio) || dataCreazione.after(dataFine))) {
					Map<EntityFarmaco, Integer> quantitaFarmaci = ordine.getQuantitaFarmaci();
					for (Map.Entry<EntityFarmaco, Integer> entry: quantitaFarmaci.entrySet()) {
						EntityFarmaco farmaco = entry.getKey();
						int quantita = entry.getValue();
						if (farmaco.isPrescrizione()){
							aggiungiVenditeFarmaco(farmaciVendutiPrescrizione, farmaco.getNome(), quantita);
							venditePrescrizione += quantita;
							incassoPrescrizione += quantita * farmaco.getPrezzo();
						} else {
							aggiungiVenditeFarmaco(farmaciVendutiBanco, farmaco.getNome(), quantita);
							venditeBanco += quantita;
							incassoBanco += quantita * farmaco.getPrezzo();
						}
					}
				}
			}

			Map.Entry<String, Integer> maxPrescrizione = maxVendite(farmaciVendutiPrescrizione);
			String nomePrescrizione = maxPrescrizione.getKey();
			int unitaPrescrizione = maxPrescrizione.getValue();

			Map.Entry<String, Integer> maxBanco = maxVendite(farmaciVendutiBanco);
			String nomeBanco = maxBanco.getKey();
			int unitaBanco = maxBanco.getValue();

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

	/**
	 * Funzione privata che aggiunge la coppia <code>nomeFarmaco</code>, <code>quantità</code> alla mappa di farmaci venduti.
	 * @param farmaci mappa che contiene le coppie (<code>nomeFarmaco</code>, <code>quantità</code>) venduti
	 * @param nomeFarmaco nome del farmaco
	 * @param quantita quantità venduta del farmaco
	 */
	private static void aggiungiVenditeFarmaco(Map<String, Integer> farmaci, String nomeFarmaco, int quantita) {
		int nuovaQuantita = quantita;
		if (farmaci.containsKey(nomeFarmaco)){
			nuovaQuantita += farmaci.get(nomeFarmaco);
		}
		farmaci.put(nomeFarmaco, nuovaQuantita);
	}

	/**
	 * Funzone privata che trova la coppia <code>nomeFarmaco</code>, <code>quantità</code> più venduta.
	 * @param farmaci mappa che contiene le coppie (<code>nomeFarmaco</code>, <code>quantità</code>) venduti.
	 * @return coppia <code>nomeFarmaco</code>, <code>quantità</code> più venduta.
	 */
	private static Map.Entry<String, Integer> maxVendite(Map<String, Integer> farmaci) {
		String nome = "";
		int unita = 0;
		for (Map.Entry<String, Integer> entry: farmaci.entrySet()) {
			if (entry.getValue() > unita){
				unita = entry.getValue();
				nome = entry.getKey();
			}
		}
		return Map.entry(nome, unita);
	}
}
