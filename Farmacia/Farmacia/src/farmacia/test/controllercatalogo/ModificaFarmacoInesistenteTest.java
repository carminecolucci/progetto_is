package farmacia.test.controllercatalogo;

import farmacia.controller.ControllerCatalogo;
import farmacia.exceptions.FarmacoNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ModificaFarmacoInesistenteTest {

	private static ControllerCatalogo controllerCatalogo;

	@BeforeClass
	public static void setUpBeforeClass() {
		controllerCatalogo = ControllerCatalogo.getInstance();
	}

	@Test
	public void testModificaFarmacoCheNonEsiste() {
		boolean esito = true;
		try {
			controllerCatalogo.modificaFarmaco((int)controllerCatalogo.cercaFarmaco("FarmacoNonEsistente").get("id"), 20, true, "SuperAulin", 50);
		} catch (FarmacoNotFoundException e) {
			esito = false;
		}
		assertFalse(esito);
	}
}
