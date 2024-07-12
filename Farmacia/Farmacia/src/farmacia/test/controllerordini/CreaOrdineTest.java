package farmacia.test.controllerordini;

import farmacia.controller.ControllerCatalogo;
import farmacia.controller.ControllerOrdini;
import farmacia.controller.ControllerUtenti;
import farmacia.database.FarmacoDAO;
import farmacia.database.UtenteDAO;
import farmacia.dto.DTO;
import farmacia.entity.EntityOrdineAcquisto;
import farmacia.exceptions.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static farmacia.util.Utility.contains;
import static org.junit.Assert.*;

public class CreaOrdineTest {
	private static ControllerOrdini controllerOrdini;
	private static ControllerCatalogo controllerCatalogo;
	private String idOrdineSuccess;
	private String idOrdineAzzeraScorte;

	@BeforeClass
	public static void setUp() throws FarmacoCreationFailedException, DBException, ParseException {

		// inizializzazione dei controller
		controllerOrdini = ControllerOrdini.getInstance();
		ControllerUtenti controllerUtenti = ControllerUtenti.getInstance();
		controllerCatalogo = ControllerCatalogo.getInstance();

		String dateString = "2023-07-06";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = formatter.parse(dateString);
		Date dataNascita = new Date(date.getTime());
		boolean esito = true;
		try {
			controllerUtenti.registraCliente("testUserCreaOrdine", "MiaPassword", "Utente", "Di Prova", dataNascita, "cliente@creaordine.com");
			controllerUtenti.loginUtente("testUserCreaOrdine", "MiaPassword");
		} catch (RegistrationFailedException | LoginFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		// aggiunta di un farmaco e creazione di un ordine di acquisto
		controllerCatalogo.aggiungiFarmaco(50, true, "Farmaco1", 20);
		controllerCatalogo.aggiungiFarmaco(50, false, "Farmaco2", 20);
		controllerCatalogo.aggiungiFarmaco(50, false, "Farmaco3", 20);
		controllerCatalogo.aggiungiFarmaco(50, false, "Farmaco4", 20);
	}

	@Test
	public void testCreaOrdineSuccess() {
		Map<Integer, Integer> ordine = new HashMap<>();
		boolean esito = true;
		try {
			ordine.put((int)controllerCatalogo.cercaFarmaco("Farmaco1").get("id"), 5);
			ordine.put((int)controllerCatalogo.cercaFarmaco("Farmaco2").get("id"), 10);
			idOrdineSuccess = controllerOrdini.creaOrdine(ordine);
		} catch (FarmacoNotFoundException | OrderCreationFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		try {
			for (DTO ordineCreato: controllerOrdini.visualizzaStoricoOrdini()) {
				if (ordineCreato.get("id").equals(idOrdineSuccess)) {
					Map<DTO, Integer> quantitaFarmaci = (Map<DTO, Integer>) ordineCreato.get("quantitaFarmaci");
					if (!contains(quantitaFarmaci, "Farmaco1", 5)) {
						esito = false;
					}
					if (!contains(quantitaFarmaci, "Farmaco2", 10)) {
						esito = false;
					}
					break;
				}
			}
		} catch (DBException e) {
			System.err.println(e.getMessage());
			fail();
		}

		assertTrue(esito);
	}

	@Test
	public void testCreaOrdineAzzeraScorte() {
		Map<Integer, Integer> ordine = new HashMap<>();
		boolean esito = true;
		try {
			ordine.put((int)controllerCatalogo.cercaFarmaco("Farmaco3").get("id"), 20);
			idOrdineAzzeraScorte = controllerOrdini.creaOrdine(ordine);
		} catch (FarmacoNotFoundException | OrderCreationFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		try {
			for (DTO ordineCreato: controllerOrdini.visualizzaStoricoOrdini()) {
				if (ordineCreato.get("id").equals(idOrdineAzzeraScorte)) {
					Map<DTO, Integer> quantitaFarmaci = (Map<DTO, Integer>) ordineCreato.get("quantitaFarmaci");
					if (!contains(quantitaFarmaci, "Farmaco3", 20)) {
						esito = false;
					}
					break;
				}
			}
		} catch (DBException e) {
			System.err.println(e.getMessage());
			fail();
		}
		assertTrue(esito);

		for (DTO ordineAcquisto: controllerOrdini.visualizzaOrdiniAcquistoFarmacia()) {
			if (ordineAcquisto.get("id").equals(idOrdineAzzeraScorte)) {
				Map<DTO, Integer> quantitaFarmaci = (Map<DTO, Integer>) ordineAcquisto.get("quantitaFarmaci");
				if (!contains(quantitaFarmaci, "Farmaco3", EntityOrdineAcquisto.QUANTITA_ORDINE_DEFAULT)) {
					esito = false;
				}
				break;
			}
		}
		assertTrue(esito);
	}

	@Test
	public void testGeneraOrdineVuoto() {
		Map<Integer, Integer> ordine = new HashMap<>();
		boolean esito = true;
		try {
			controllerOrdini.creaOrdine(ordine);
		} catch (OrderCreationFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}

		assertFalse(esito);
	}

	@Test
	public void testGeneraOrdineScorteInsufficienti() {
		Map<Integer, Integer> ordine = new HashMap<>();
		boolean esito = true;
		try {
			ordine.put((int)controllerCatalogo.cercaFarmaco("Farmaco4").get("id"), 100);
			controllerOrdini.creaOrdine(ordine);
		} catch (OrderCreationFailedException | FarmacoNotFoundException e) {
			System.err.println(e.getMessage());
			esito = false;
		}

		assertFalse(esito);
	}

	@AfterClass
	public static void tearDown() throws DBException {
		UtenteDAO utenteDAO = new UtenteDAO("testUserCreaOrdine");
		utenteDAO.deleteUtente();
		FarmacoDAO.deleteFarmaco("Farmaco1");
		FarmacoDAO.deleteFarmaco("Farmaco2");
		FarmacoDAO.deleteFarmaco("Farmaco3");
		FarmacoDAO.deleteFarmaco("Farmaco4");
	}
}
