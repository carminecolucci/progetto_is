package farmacia.test.controllerordini;

import farmacia.controller.ControllerCatalogo;
import farmacia.controller.ControllerOrdini;
import farmacia.controller.ControllerUtenti;
import farmacia.database.FarmacoDAO;
import farmacia.database.OrdineDAO;
import farmacia.database.UtenteDAO;
import farmacia.dto.DTO;
import farmacia.exceptions.*;
import org.junit.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AggiornaOrdineTest {
	private static ControllerOrdini controllerOrdini;
	private static String idOrdineSuccess;
	private static final Logger logger = Logger.getLogger("AggiornaOrdineTest");
	private static final String TEST_USER_ORDINE = "testUserOrdine";
	private static final String FARMACO_ORDINE = "FarmacoOrdine";


	@BeforeClass
	public static void setUp() throws ParseException, FarmacoCreationFailedException, FarmacoNotFoundException, OrderCreationFailedException {
  ControllerUtenti controllerUtenti;

		// inizializzazione dei controller
		controllerOrdini = ControllerOrdini.getInstance();
		controllerUtenti = ControllerUtenti.getInstance();
		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();

		// registrazione di un cliente
		String dateString = "2023-07-06";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = formatter.parse(dateString);
		Date dataNascita = new Date(date.getTime());
		boolean esito = true;
		try {
			controllerUtenti.registraCliente(TEST_USER_ORDINE, "MiaPassword", "Utente", "Di Prova", dataNascita, "clienteordine@gmail.com");
		} catch (RegistrationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		// login di un cliente
		try {
			controllerUtenti.loginUtente(TEST_USER_ORDINE, "MiaPassword");
		} catch (LoginFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		// aggiunta di un farmaco e creazione di un ordine
		controllerCatalogo.aggiungiFarmaco(50, true, FARMACO_ORDINE, 20);
		Map<Integer, Integer> ordine = new HashMap<>();
		ordine.put((int) controllerCatalogo.cercaFarmaco(FARMACO_ORDINE).get("id"), 20);
		idOrdineSuccess = controllerOrdini.creaOrdine(ordine);

		// login di un farmacista (per caricare gli ordini di tutti i clienti)
		try {
			controllerUtenti.loginUtente("farmacista", "farmacista"); // il test assume che ESISTA questo account nel DB
		} catch (LoginFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
	}


	@AfterClass
	public static void tearDown() throws DBException {
		FarmacoDAO.deleteFarmaco(FARMACO_ORDINE);
		UtenteDAO utenteDAO = new UtenteDAO(TEST_USER_ORDINE);
		utenteDAO.deleteUtente();
		OrdineDAO.deleteOrdine(idOrdineSuccess);
	}

	@Test
	public void testAggiornaOrdineSuccess() throws DBException {
		boolean esito = true;
		try {
			controllerOrdini.aggiornaOrdine(idOrdineSuccess);
		} catch (OrderNotFoundException e) {
			esito = false;
		}
		assertTrue(esito);

		esito = false;
		for (DTO ordineAcquisto : controllerOrdini.visualizzaOrdiniFarmacia()) {
			String id = (String)ordineAcquisto.get("id");
			if (id.equals(idOrdineSuccess)) {
				assertTrue((boolean)ordineAcquisto.get("ritirato"));
				esito = true;
			}
		}
		assertTrue(esito);
	}

	@Test
	public void testAggiornaOrdineCheNonEsiste() {
		boolean esito = true;
		try {
			controllerOrdini.aggiornaOrdineAcquisto("id-inventato");
		} catch (OrderNotFoundException | FarmacoNotFoundException e) {
			esito = false;
		}
		assertFalse(esito);
	}

}
