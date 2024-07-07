package farmacia.test.controllercatalogo;

import farmacia.controller.ControllerCatalogo;
import farmacia.exceptions.FarmacoNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

// questo test si trova in un'altra classe per problemi di concorrenza con CercaFarmacoTest
public class CercaFarmacoInesistenteTest {

	private static ControllerCatalogo controllerCatalogo;

	@BeforeClass
	public static void setUpBeforeClass() {
		controllerCatalogo = ControllerCatalogo.getInstance();
	}

	@Test
	public void testCercaFarmacoInesistente() {
		boolean esito = true;
		try {
			controllerCatalogo.cercaFarmaco("Augmentin");
		} catch (FarmacoNotFoundException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertFalse(esito);
	}
}
