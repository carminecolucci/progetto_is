package farmacia.test.controllercatalogo;

import farmacia.controller.ControllerCatalogo;
import farmacia.database.FarmacoDAO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoCreationFailedException;
import org.junit.*;

import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AggiungiFarmacoTest {

	private static ControllerCatalogo catalogo;
	private static final String OKI_TASK = "OkiTask";
	private static final Logger logger = Logger.getLogger("AggiungiFarmacoTest");


	@BeforeClass
	public static void setUpBeforeClass() {
		catalogo = ControllerCatalogo.getInstance();
	}

	@After
	public void tearDown() throws DBException {
		FarmacoDAO.deleteFarmaco(OKI_TASK);
	}

	@Test
	public void testAggiungiFarmaco() {
		// risolto bug della , nel formato di rappresentazione standard di Java
		float prezzo = 50;
		boolean prescrizione = false;
		int scorte = 20;
		boolean esito = true;
		try {
			catalogo.aggiungiFarmaco(prezzo, prescrizione, OKI_TASK, scorte);
		} catch (FarmacoCreationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
	}

	@Test
	public void testAggiungiFarmacoGiaEsistente() {
		float prezzo = 50;
		boolean prescrizione = false;
		int scorte = 20;
		boolean esito = true;
		try {
			catalogo.aggiungiFarmaco(prezzo, prescrizione, OKI_TASK, scorte);
		} catch (FarmacoCreationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
		try {
			catalogo.aggiungiFarmaco(prezzo, prescrizione, OKI_TASK, scorte);
		} catch (FarmacoCreationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertFalse(esito);
	}

}
