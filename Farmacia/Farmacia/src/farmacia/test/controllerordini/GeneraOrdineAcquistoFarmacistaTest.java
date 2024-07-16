package farmacia.test.controllerordini;

import farmacia.controller.ControllerCatalogo;
import farmacia.controller.ControllerOrdini;
import farmacia.database.FarmacoDAO;
import farmacia.database.OrdineAcquistoDAO;
import farmacia.dto.DTO;
import farmacia.exceptions.*;
import static farmacia.dto.DTO.contains;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GeneraOrdineAcquistoFarmacistaTest {
	private static ControllerOrdini controllerOrdini;
	private static ControllerCatalogo controllerCatalogo;
	private String idOrdineSuccess;
	private static final String FARMACO_ORDINE_ACQUISTO1 = "FarmacoOrdineAcquisto1";
	private static final String FARMACO_ORDINE_ACQUISTO2 = "FarmacoOrdineAcquisto2";

	private static final Logger logger = Logger.getLogger("GeneraOrdineAcquistoFarmacistaTest");

	@BeforeClass
	public static void setUp() throws FarmacoCreationFailedException {

		// inizializzazione dei controller
		controllerOrdini = ControllerOrdini.getInstance();
		controllerCatalogo = ControllerCatalogo.getInstance();

		// aggiunta di un farmaco e creazione di un ordine
		controllerCatalogo.aggiungiFarmaco(50, true, FARMACO_ORDINE_ACQUISTO1, 20);
		controllerCatalogo.aggiungiFarmaco(50, true, FARMACO_ORDINE_ACQUISTO2, 20);
	}

	@Test
	public void testGeneraOrdineAcquistoSuccess() {
		Map<Integer, Integer> ordine = new HashMap<>();

		boolean esito = true;
		try {
			ordine.put((int)controllerCatalogo.cercaFarmaco(FARMACO_ORDINE_ACQUISTO1).get("id"), 5);
			ordine.put((int)controllerCatalogo.cercaFarmaco(FARMACO_ORDINE_ACQUISTO2).get("id"), 10);
			idOrdineSuccess = controllerOrdini.creaOrdineAcquistoFarmacia(ordine);
		} catch (FarmacoNotFoundException | OrderCreationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}

		assertTrue(esito);

		for (DTO ordineAcquisto: controllerOrdini.visualizzaOrdiniAcquistoFarmacia()) {
			if (ordineAcquisto.get("id").equals(idOrdineSuccess)) {
				Map<DTO, Integer> quantitaFarmaci = (Map<DTO, Integer>) ordineAcquisto.get("quantitaFarmaci");
				if (!contains(quantitaFarmaci, FARMACO_ORDINE_ACQUISTO1, 5)) {
					esito = false;
				}
				if (!contains(quantitaFarmaci, FARMACO_ORDINE_ACQUISTO2, 10)) {
					esito = false;
				}
				break;
			}
		}

		assertTrue(esito);
	}

	@Test
	public void testGeneraOrdineAcquistoVuoto() {
		Map<Integer, Integer> ordine = new HashMap<>();
		boolean esito = true;
		try {
			controllerOrdini.creaOrdineAcquistoFarmacia(ordine);
		} catch (OrderCreationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}

		assertFalse(esito);
	}

	@After
	public void tearDown() throws DBException {
		FarmacoDAO.deleteFarmaco(FARMACO_ORDINE_ACQUISTO1);
		FarmacoDAO.deleteFarmaco(FARMACO_ORDINE_ACQUISTO2);
		OrdineAcquistoDAO.deleteOrdineAcquisto(idOrdineSuccess);
	}
}
