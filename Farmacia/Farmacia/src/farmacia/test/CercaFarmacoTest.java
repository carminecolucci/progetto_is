package farmacia.test;

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

import static org.junit.Assert.*;

public class CercaFarmacoTest {

	private static ControllerCatalogo controllerCatalogo;

	@BeforeClass
	public static void setUpBeforeClass() {
		controllerCatalogo = ControllerCatalogo.getInstance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws DBException {
		FarmacoDAO.deleteFarmacoByNome("Aulin");
	}

	@Test
	public void testCercaFarmaco() throws FarmacoCreationFailedException {
		float prezzo = 10;
		boolean prescrizione = false;
		String nome = "Aulin";
		int scorte = 50;
		controllerCatalogo.aggiungiFarmaco(prezzo, prescrizione, nome, scorte);
		boolean esito = true;
		DTO dto;
		try {
			dto = controllerCatalogo.cercaFarmacoByNome("Aulin");
			assertTrue((float)dto.get("prezzo") == 10 && (boolean)dto.get("prescrizione") == false && ((String)dto.get("nome")).equals("Aulin") && (int)dto.get("scorte") == 50);
		} catch (FarmacoNotFoundException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
	}
}
