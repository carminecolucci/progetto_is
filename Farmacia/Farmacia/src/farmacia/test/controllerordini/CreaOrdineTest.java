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
import java.util.logging.Logger;

import static farmacia.dto.DTO.contains;
import static org.junit.Assert.*;

public class CreaOrdineTest {
	private static ControllerOrdini controllerOrdini;
	private static ControllerCatalogo controllerCatalogo;
	private String idOrdineSuccess;
	private String idOrdineAzzeraScorte;
	private static final String TEST_USER_CREA_ORDINE = "testUserCreaOrdine";
	private static final String FARMACO1 = "Farmaco1";
	private static final String FARMACO2 = "Farmaco2";
	private static final String FARMACO3 = "Farmaco3";
	private static final String FARMACO4 = "Farmaco4";
	private static final String QUANTITA_FARMACI = "quantitaFarmaci";
	private static final Logger logger = Logger.getLogger("CreaOrdineTest");

	@BeforeClass
	public static void setUp() throws FarmacoCreationFailedException, ParseException {

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
			controllerUtenti.registraCliente(TEST_USER_CREA_ORDINE, "MiaPassword", "Utente", "Di Prova", dataNascita, "cliente@creaordine.com");
			controllerUtenti.loginUtente(TEST_USER_CREA_ORDINE, "MiaPassword");
		} catch (RegistrationFailedException | LoginFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		// aggiunta di un farmaco e creazione di un ordine di acquisto
		controllerCatalogo.aggiungiFarmaco(50, true, FARMACO1, 20);
		controllerCatalogo.aggiungiFarmaco(50, false, FARMACO2, 20);
		controllerCatalogo.aggiungiFarmaco(50, false, FARMACO3, 20);
		controllerCatalogo.aggiungiFarmaco(50, false, FARMACO4, 20);
	}

	@Test
	public void testCreaOrdineSuccess() {
		Map<Integer, Integer> ordine = new HashMap<>();
		boolean esito = true;
		try {
			ordine.put((int)controllerCatalogo.cercaFarmaco(FARMACO1).get("id"), 5);
			ordine.put((int)controllerCatalogo.cercaFarmaco(FARMACO2).get("id"), 10);
			idOrdineSuccess = controllerOrdini.creaOrdine(ordine);
		} catch (FarmacoNotFoundException | OrderCreationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		try {
			for (DTO ordineCreato: controllerOrdini.visualizzaStoricoOrdini()) {
				if (ordineCreato.get("id").equals(idOrdineSuccess)) {
					Map<DTO, Integer> quantitaFarmaci = (Map<DTO, Integer>) ordineCreato.get(QUANTITA_FARMACI);
					if (!contains(quantitaFarmaci, FARMACO1, 5)) {
						esito = false;
					}
					if (!contains(quantitaFarmaci, FARMACO2, 10)) {
						esito = false;
					}
					break;
				}
			}
		} catch (DBException e) {
			logger.warning(e.getMessage());
			fail();
		}

		assertTrue(esito);
	}

	@Test
	public void testCreaOrdineAzzeraScorte() {
		Map<Integer, Integer> ordine = new HashMap<>();
		boolean esito = true;
		try {
			ordine.put((int)controllerCatalogo.cercaFarmaco(FARMACO3).get("id"), 20);
			idOrdineAzzeraScorte = controllerOrdini.creaOrdine(ordine);
		} catch (FarmacoNotFoundException | OrderCreationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		try {
			for (DTO ordineCreato: controllerOrdini.visualizzaStoricoOrdini()) {
				if (ordineCreato.get("id").equals(idOrdineAzzeraScorte)) {
					Map<DTO, Integer> quantitaFarmaci = (Map<DTO, Integer>) ordineCreato.get(QUANTITA_FARMACI);
					if (!contains(quantitaFarmaci, FARMACO3, 20)) {
						esito = false;
					}
					break;
				}
			}
		} catch (DBException e) {
			logger.warning(e.getMessage());
			fail();
		}
		assertTrue(esito);

		for (DTO ordineAcquisto: controllerOrdini.visualizzaOrdiniAcquistoFarmacia()) {
			if (ordineAcquisto.get("id").equals(idOrdineAzzeraScorte)) {
				Map<DTO, Integer> quantitaFarmaci = (Map<DTO, Integer>) ordineAcquisto.get(QUANTITA_FARMACI);
				if (!contains(quantitaFarmaci, FARMACO3, EntityOrdineAcquisto.QUANTITA_ORDINE_DEFAULT)) {
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
			logger.warning(e.getMessage());
			esito = false;
		}

		assertFalse(esito);
	}

	@Test
	public void testGeneraOrdineScorteInsufficienti() {
		Map<Integer, Integer> ordine = new HashMap<>();
		boolean esito = true;
		try {
			ordine.put((int)controllerCatalogo.cercaFarmaco(FARMACO4).get("id"), 100);
			controllerOrdini.creaOrdine(ordine);
		} catch (OrderCreationFailedException | FarmacoNotFoundException e) {
			logger.warning(e.getMessage());
			esito = false;
		}

		assertFalse(esito);
	}

	@AfterClass
	public static void tearDown() throws DBException {
		UtenteDAO utenteDAO = new UtenteDAO(TEST_USER_CREA_ORDINE);
		utenteDAO.deleteUtente();
		FarmacoDAO.deleteFarmaco(FARMACO1);
		FarmacoDAO.deleteFarmaco(FARMACO2);
		FarmacoDAO.deleteFarmaco(FARMACO3);
		FarmacoDAO.deleteFarmaco(FARMACO4);
	}
}
