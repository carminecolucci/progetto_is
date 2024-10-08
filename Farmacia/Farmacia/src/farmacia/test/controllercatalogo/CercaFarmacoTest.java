package farmacia.test.controllercatalogo;

import farmacia.controller.ControllerCatalogo;
import farmacia.database.FarmacoDAO;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoCreationFailedException;
import farmacia.exceptions.FarmacoNotFoundException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class CercaFarmacoTest {

	private static ControllerCatalogo controllerCatalogo;
	private static final String CLAVULIN = "Clavulin";
	private static final Logger logger = Logger.getLogger("CercaFarmacoTest");

	@BeforeClass
	public static void setUpBeforeClass() {
		controllerCatalogo = ControllerCatalogo.getInstance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws DBException {
		FarmacoDAO.deleteFarmaco(CLAVULIN);
	}

	@Test
	public void testCercaFarmaco() throws FarmacoCreationFailedException {
		float prezzo = 10;
		boolean prescrizione = false;
		int scorte = 50;
		controllerCatalogo.aggiungiFarmaco(prezzo, prescrizione, CLAVULIN, scorte);
		boolean esito = true;
		DTO dto;
		try {
			dto = controllerCatalogo.cercaFarmaco(CLAVULIN);
			assertTrue((float)dto.get("prezzo") == 10 && !((boolean) dto.get("prescrizione")) && dto.get("nome").equals(CLAVULIN) && (int)dto.get("scorte") == 50);
		} catch (FarmacoNotFoundException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
	}
}
