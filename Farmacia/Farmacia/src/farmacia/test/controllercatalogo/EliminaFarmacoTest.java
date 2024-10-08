package farmacia.test.controllercatalogo;

import farmacia.controller.ControllerCatalogo;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoCreationFailedException;
import farmacia.exceptions.FarmacoNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class EliminaFarmacoTest {

	private static ControllerCatalogo controllerCatalogo;
	private static final String TEST_ELIMINA_FARMACO = "TestEliminaFarmaco";

	@BeforeClass
	public static void setUpBeforeClass() {
		controllerCatalogo = ControllerCatalogo.getInstance();
	}

	@Test
	public void testEliminaFarmacoSuccess() throws FarmacoCreationFailedException {
		float prezzo = 50;
		boolean prescrizione = true;
		int scorte = 10;
		controllerCatalogo.aggiungiFarmaco(prezzo, prescrizione, TEST_ELIMINA_FARMACO, scorte);
		try {
			controllerCatalogo.eliminaFarmaco((int)controllerCatalogo.cercaFarmaco(TEST_ELIMINA_FARMACO).get("id"));
		} catch (FarmacoNotFoundException | DBException e) {
			fail("Errore dovuto alle dipendenze" + e.getMessage());
		}
		boolean esito = true;
		try {
			controllerCatalogo.cercaFarmaco(TEST_ELIMINA_FARMACO);
		} catch (FarmacoNotFoundException e) {
			esito = false;
		}
		assertFalse(esito);
	}

	@Test
	public void testEliminaFarmacoCheNonEsiste() {
		boolean esito = true;
		try {
			controllerCatalogo.eliminaFarmaco((int)controllerCatalogo.cercaFarmaco("FarmacoNonEsistente").get("id"));
		} catch (FarmacoNotFoundException e) {
			esito = false;
		} catch (DBException e) {
			fail("Errore dovuto alle dipendenze" + e.getMessage());
		}
		assertFalse(esito);
	}
}
