package farmacia.testcontrollerreport;

import farmacia.controller.ControllerCatalogo;
import farmacia.controller.ControllerOrdini;
import farmacia.controller.ControllerReport;
import farmacia.controller.ControllerUtenti;
import farmacia.database.FarmacoDAO;
import farmacia.database.OrdineDAO;
import farmacia.database.UtenteDAO;
import farmacia.dto.DTO;
import farmacia.exceptions.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GeneraReportTest {

	private static ControllerReport controllerReport;
	private static String idOrdine1;
	private static String idOrdine2;
	private static String idOrdine3;

	@BeforeClass
	public static void setUpBeforeClass() throws ParseException {
		controllerReport = ControllerReport.getInstance();
		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		ControllerUtenti controllerUtenti = ControllerUtenti.getInstance();
		ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();

		try {
			controllerUtenti.loginUtente("farmacista", "farmacista");
		} catch (LoginFailedException e) {
			fail("Errore dovuto alle dipendenze: controllerUtenti");
		}

		try {
			controllerCatalogo.aggiungiFarmaco(17.00f, true, "FarmacoReport1", 100);
			controllerCatalogo.aggiungiFarmaco(11.00f, true, "FarmacoReport2", 100);
			controllerCatalogo.aggiungiFarmaco(6.00f, false, "FarmacoReport3", 100);
			controllerCatalogo.aggiungiFarmaco(10.00f, false, "FarmacoReport4", 100);
		} catch (FarmacoCreationFailedException e) {
			fail("Errore dovuto alle dipendenze: controllerCatalogo");
		}

		String dateString = "2023-07-06";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = formatter.parse(dateString);
		java.sql.Date dataNascita = new java.sql.Date(date.getTime());

		try {
			controllerUtenti.registraCliente("utentereport", "utentereport", "Franco", "Bollo", dataNascita, "franco.bollo@libero.it");
		} catch (RegistrationFailedException e) {
			fail("Errore dovuto alle dipendenze: controllerUtenti");
		}

		try {
			controllerUtenti.loginUtente("utentereport", "utentereport");
		} catch (LoginFailedException e) {
			fail("Errore dovuto alle dipendenze: controllerUtenti");
		}

		Map<Integer, Integer> ordine1 = new HashMap<>();
		Map<Integer, Integer> ordine2 = new HashMap<>();
		Map<Integer, Integer> ordine3 = new HashMap<>();

		try {
			ordine1.put((int) controllerCatalogo.cercaFarmaco("FarmacoReport1").get("id"), 2);
			ordine1.put((int) controllerCatalogo.cercaFarmaco("FarmacoReport2").get("id"), 6);
			ordine2.put((int) controllerCatalogo.cercaFarmaco("FarmacoReport3").get("id"), 11);
			ordine3.put((int) controllerCatalogo.cercaFarmaco("FarmacoReport3").get("id"), 3);
			ordine3.put((int) controllerCatalogo.cercaFarmaco("FarmacoReport4").get("id"), 4);
		} catch (FarmacoNotFoundException e) {
			fail("Errore dovuto alle dipendenze: controllerCatalogo");
		}

		try {
			idOrdine1 = controllerOrdini.creaOrdine(ordine1);
			idOrdine2 = controllerOrdini.creaOrdine(ordine2);
			idOrdine3 = controllerOrdini.creaOrdine(ordine3);
		} catch (OrderCreationFailedException e) {
			fail("Errore dovuto alle dipendenze: controllerOrdini");
		}
	}

	@Test
	public void testGeneraReport() throws ParseException {
		// risolto bug della , nel formato di rappresentazione standard di Java
		Date dataOggi = new Date();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dataReportString = formatter.format(dataOggi);

		java.util.Date date = formatter.parse(dataReportString);
		java.sql.Date dataReport = new java.sql.Date(date.getTime());

		float totaleBancoExpected = (14 * 6) + (4 * 10);
		float totalePrescrizioneExpected = (2 * 17) + (6 * 11);
		String nomeBancoExpected = "FarmacoReport3";
		String nomePrescrizioneExpected = "FarmacoReport2";
		int unitaPrescrizioneExpected = 6;
		int unitaBancoExpected = 14;

		try {
			DTO datiReport = controllerReport.generaReport(dataReport, dataReport);
			float incassoBanco = (float)datiReport.get("incassoBanco");
			float incassoPrescrizione = (float)datiReport.get("incassoPrescrizione");
			String nomeBanco = (String)datiReport.get("nomeBanco");
			String nomePrescrizione = (String)datiReport.get("nomePrescrizione");
			int unitaPrescrizione = (int)datiReport.get("unitaPrescrizione");
			int unitaBanco = (int)datiReport.get("unitaBanco");

			assertEquals(incassoBanco, totaleBancoExpected, 0.01);
			assertEquals(incassoPrescrizione, totalePrescrizioneExpected, 0.01);
			assertEquals(nomeBanco, nomeBancoExpected);
			assertEquals(nomePrescrizione, nomePrescrizioneExpected);
			assertEquals(unitaBanco, unitaBancoExpected);
			assertEquals(unitaPrescrizione, unitaPrescrizioneExpected);
		} catch (ReportException e) {
			fail("Errore nella generazione del report: " + e.getMessage());
		}

	}

	@AfterClass
	public static void tearDown() {
		try {
			FarmacoDAO.deleteFarmaco("FarmacoReport1");
			FarmacoDAO.deleteFarmaco("FarmacoReport2");
			FarmacoDAO.deleteFarmaco("FarmacoReport3");
			FarmacoDAO.deleteFarmaco("FarmacoReport4");
			OrdineDAO.deleteOrdine(idOrdine1);
			OrdineDAO.deleteOrdine(idOrdine2);
			OrdineDAO.deleteOrdine(idOrdine3);
			UtenteDAO utente = new UtenteDAO("utentereport");
			utente.deleteUtente();
		} catch (DBException e) {
			fail("Errore nel cleanup:" + e.getMessage());
		}
	}

}
