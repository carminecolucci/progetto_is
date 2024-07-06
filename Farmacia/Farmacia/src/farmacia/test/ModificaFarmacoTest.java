package farmacia.test;

import farmacia.controller.ControllerCatalogo;
import farmacia.database.FarmacoDAO;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoCreationFailedException;
import farmacia.exceptions.FarmacoNotFoundException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ModificaFarmacoTest {

	private static ControllerCatalogo controllerCatalogo;

	@BeforeClass
	public static void setUpBeforeClass() {
		controllerCatalogo = ControllerCatalogo.getInstance();
	}

	@After
	public void tearDownAfter() throws DBException {
		FarmacoDAO.deleteFarmacoByNome("SuperAulin");
	}

	@Test
	public void testModificaFarmacoSuccess() throws FarmacoCreationFailedException, FarmacoNotFoundException {
		float prezzo = 10;
		boolean prescrizione = false;
		String nome = "Aulin";
		int scorte = 50;
		controllerCatalogo.aggiungiFarmaco(prezzo, prescrizione, nome, scorte);
		// TODO: rifare il test con codice più pulito, farsi restituire i farmaci con visualizzaCatalogo come se stessimo usando la GUI
		controllerCatalogo.modificaFarmaco((int)controllerCatalogo.cercaFarmacoByNome(nome).get("id"), 20, true, "SuperAulin", 50);
		DTO dto = controllerCatalogo.cercaFarmacoByNome("SuperAulin");
		assertTrue((float)dto.get("prezzo") == 20 && (boolean)dto.get("prescrizione") == true && ((String)dto.get("nome")).equals("SuperAulin") && (int)dto.get("scorte") == 50);
	}
}
