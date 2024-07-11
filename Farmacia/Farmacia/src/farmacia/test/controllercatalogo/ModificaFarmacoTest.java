package farmacia.test.controllercatalogo;

import farmacia.controller.ControllerCatalogo;
import farmacia.database.FarmacoDAO;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoCreationFailedException;
import farmacia.exceptions.FarmacoNotFoundException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ModificaFarmacoTest {

	private static ControllerCatalogo controllerCatalogo;

	@BeforeClass
	public static void setUpBeforeClass() {
		controllerCatalogo = ControllerCatalogo.getInstance();
	}

	@AfterClass
	public static void tearDown() throws DBException {
		FarmacoDAO.deleteFarmaco("SuperAulin1");
		FarmacoDAO.deleteFarmaco("SuperAulin2");
	}

	@Test
	public void testModificaFarmacoPrescrizioneTrueSuccess() throws FarmacoCreationFailedException, FarmacoNotFoundException {
		float prezzo = 10;
		boolean prescrizione = true;
		String nome = "Aulin1";
		int scorte = 50;
		controllerCatalogo.aggiungiFarmaco(prezzo, prescrizione, nome, scorte);
		// TODO: rifare il test con codice più pulito, farsi restituire i farmaci con visualizzaCatalogo come se stessimo usando la GUI
		controllerCatalogo.modificaFarmaco((int)controllerCatalogo.cercaFarmaco(nome).get("id"), 20, false, "SuperAulin1", 50);
		DTO dto = controllerCatalogo.cercaFarmaco("SuperAulin1");
		assertTrue((float)dto.get("prezzo") == 20
				&& !(boolean) dto.get("prescrizione")
				&& "SuperAulin1".equals(dto.get("nome"))
				&& (int)dto.get("scorte") == 50
		);
	}

	@Test
	public void testModificaFarmacoPrescrizioneFalseSuccess() throws FarmacoCreationFailedException, FarmacoNotFoundException {
		float prezzo = 10;
		boolean prescrizione = false;
		String nome = "Aulin2";
		int scorte = 50;
		controllerCatalogo.aggiungiFarmaco(prezzo, prescrizione, nome, scorte);
		// TODO: rifare il test con codice più pulito, farsi restituire i farmaci con visualizzaCatalogo come se stessimo usando la GUI
		controllerCatalogo.modificaFarmaco((int)controllerCatalogo.cercaFarmaco(nome).get("id"), 20, true, "SuperAulin2", 50);
		DTO dto = controllerCatalogo.cercaFarmaco("SuperAulin2");
		assertTrue((float)dto.get("prezzo") == 20
			&& (boolean) dto.get("prescrizione")
			&& "SuperAulin2".equals(dto.get("nome"))
			&& (int)dto.get("scorte") == 50
		);
	}

}
