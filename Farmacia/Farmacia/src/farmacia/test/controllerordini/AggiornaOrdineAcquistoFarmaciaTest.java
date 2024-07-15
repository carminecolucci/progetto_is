package farmacia.test.controllerordini;

import farmacia.controller.ControllerCatalogo;
import farmacia.controller.ControllerOrdini;
import farmacia.controller.ControllerUtenti;
import farmacia.database.FarmacoDAO;
import farmacia.database.OrdineAcquistoDAO;
import farmacia.dto.DTO;
import farmacia.exceptions.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AggiornaOrdineAcquistoFarmaciaTest {
	private static ControllerOrdini controllerOrdini;
	private static String idOrdineAcquistoSuccess;
	private static final String FARMACO_ACQUISTO = "FarmacoAcquisto";
	private static final Logger logger = Logger.getLogger("AggiornaOrdineAcquistoFarmaciaTest");



	@BeforeClass
	public static void setUp() throws FarmacoCreationFailedException, FarmacoNotFoundException, OrderCreationFailedException {

		// inizializzazione dei controller
		controllerOrdini = ControllerOrdini.getInstance();
		ControllerUtenti controllerUtenti = ControllerUtenti.getInstance();
		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();

		// login di un farmacista
		boolean esito = true;
		try {
			controllerUtenti.loginUtente("farmacista", "farmacista"); // il test assume che ESISTA questo account nel DB
		} catch (LoginFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		// aggiunta di un farmaco e creazione di un ordine di acquisto
		controllerCatalogo.aggiungiFarmaco(50, true, FARMACO_ACQUISTO, 20);
		Map<Integer, Integer> ordineAcquisto = new HashMap<>();
		ordineAcquisto.put((int) controllerCatalogo.cercaFarmaco(FARMACO_ACQUISTO).get("id"), 20);
		idOrdineAcquistoSuccess = controllerOrdini.creaOrdineAcquistoFarmacia(ordineAcquisto);
	}

	@AfterClass
	public static void tearDown() throws DBException {
		FarmacoDAO.deleteFarmaco(FARMACO_ACQUISTO);
		OrdineAcquistoDAO.deleteOrdineAcquisto(idOrdineAcquistoSuccess);
	}

	@Test
	public void testAggiornaOrdineAcquistoSuccess() throws FarmacoNotFoundException {
		boolean esito = true;
		try {
			controllerOrdini.aggiornaOrdineAcquisto(idOrdineAcquistoSuccess);
		} catch (OrderNotFoundException e) {
			esito = false;
		}
		assertTrue(esito);

		esito = false;
		for (DTO ordineAcquisto : controllerOrdini.visualizzaOrdiniAcquistoFarmacia()) {
			String id = (String)ordineAcquisto.get("id");
			if (id.equals(idOrdineAcquistoSuccess)) {
				assertTrue((boolean)ordineAcquisto.get("ricevuto"));
				esito = true;
			}
		}
		assertTrue(esito);
	}

	@Test
	public void testAggiornaOrdineAcquistoCheNonEsiste() {
		boolean esito = true;
		try {
			controllerOrdini.aggiornaOrdineAcquisto("id-inventato");
		} catch (OrderNotFoundException | FarmacoNotFoundException e) {
			esito = false;
		}
		assertFalse(esito);
	}

}
