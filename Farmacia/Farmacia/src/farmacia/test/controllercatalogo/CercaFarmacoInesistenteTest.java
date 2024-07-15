package farmacia.test.controllercatalogo;

import farmacia.controller.ControllerCatalogo;
import farmacia.exceptions.FarmacoNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;

// questo test si trova in un'altra classe per problemi di concorrenza con CercaFarmacoTest
public class CercaFarmacoInesistenteTest {

	private static ControllerCatalogo controllerCatalogo;
	private static final Logger logger = Logger.getLogger("CercaFarmacoInesistenteTest");
	private static final String AUGMENTIN = "Augmentin";


	@BeforeClass
	public static void setUpBeforeClass() {
		controllerCatalogo = ControllerCatalogo.getInstance();
	}

	@Test
	public void testCercaFarmacoInesistente() {
		boolean esito = true;
		try {
			controllerCatalogo.cercaFarmaco(AUGMENTIN);
		} catch (FarmacoNotFoundException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertFalse(esito);
	}
}
