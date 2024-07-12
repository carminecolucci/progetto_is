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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AggiornaOrdineAcquistoFarmaciaTest {
	private static ControllerOrdini controllerOrdini;
	private static String idOrdineAcquistoSuccess;

	@BeforeClass
	public static void setUp() throws FarmacoCreationFailedException, FarmacoNotFoundException, OrderCreationFailedException, DBException {

		// inizializzazione dei controller
		controllerOrdini = ControllerOrdini.getInstance();
		ControllerUtenti controllerUtenti = ControllerUtenti.getInstance();
		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();

		// login di un farmacista
		boolean esito = true;
		try {
			controllerUtenti.loginUtente("farmacista", "farmacista"); // il test assume che ESISTA questo account nel DB
		} catch (LoginFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		// aggiunta di un farmaco e creazione di un ordine di acquisto
		controllerCatalogo.aggiungiFarmaco(50, true, "FarmacoAcquisto", 20);
		Map<Integer, Integer> ordineAcquisto = new HashMap<>();
		ordineAcquisto.put((int) controllerCatalogo.cercaFarmaco("FarmacoAcquisto").get("id"), 20);
		idOrdineAcquistoSuccess = controllerOrdini.creaOrdineAcquistoFarmacia(ordineAcquisto);
	}

	@AfterClass
	public static void tearDown() throws DBException {
		FarmacoDAO.deleteFarmaco("FarmacoAcquisto");
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
