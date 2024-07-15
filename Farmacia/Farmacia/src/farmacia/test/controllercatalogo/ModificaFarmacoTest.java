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

import static org.junit.Assert.assertTrue;

public class ModificaFarmacoTest {

	private static ControllerCatalogo controllerCatalogo;
	private static final String SUPER_AULIN1 = "SuperAulin1";
	private static final String SUPER_AULIN2 = "SuperAulin2";

	@BeforeClass
	public static void setUpBeforeClass() {
		controllerCatalogo = ControllerCatalogo.getInstance();
	}

	@AfterClass
	public static void tearDown() throws DBException {
		FarmacoDAO.deleteFarmaco(SUPER_AULIN1);
		FarmacoDAO.deleteFarmaco(SUPER_AULIN2);
	}

	@Test
	public void testModificaFarmacoPrescrizioneTrueSuccess() throws FarmacoCreationFailedException, FarmacoNotFoundException {
		float prezzo = 10;
		boolean prescrizione = true;
		String nome = "Aulin1";
		int scorte = 50;
		controllerCatalogo.aggiungiFarmaco(prezzo, prescrizione, nome, scorte);
		controllerCatalogo.modificaFarmaco((int)controllerCatalogo.cercaFarmaco(nome).get("id"), 20, false, SUPER_AULIN1, 50);
		DTO dto = controllerCatalogo.cercaFarmaco(SUPER_AULIN1);
		assertTrue((float)dto.get("prezzo") == 20
				&& !(boolean) dto.get("prescrizione")
				&& SUPER_AULIN1.equals(dto.get("nome"))
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
		controllerCatalogo.modificaFarmaco((int)controllerCatalogo.cercaFarmaco(nome).get("id"), 20, true, SUPER_AULIN2, 50);
		DTO dto = controllerCatalogo.cercaFarmaco(SUPER_AULIN2);
		assertTrue((float)dto.get("prezzo") == 20
			&& (boolean) dto.get("prescrizione")
			&& SUPER_AULIN2.equals(dto.get("nome"))
			&& (int)dto.get("scorte") == 50
		);
	}
}
