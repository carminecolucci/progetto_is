package farmacia.test;

import farmacia.database.FarmacoDAO;
import farmacia.entity.EntityCatalogo;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoCreationFailedException;
import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AggiungiFarmacoTest {

	private static EntityCatalogo catalogo;

	@BeforeClass
	public static void setUpBeforeClass() {
		catalogo = EntityCatalogo.getInstance();
	}

	@After
	public void tearDown() throws DBException {
		FarmacoDAO.deleteFarmacoByNome("OkiTask");
	}

	@Test
	public void testAggiungiFarmaco() {
		// risolto bug della , nel formato di rappresentazione standard di Java
		float prezzo = 50;
		boolean prescrizione = false;
		String nome = "OkiTask";
		int scorte = 20;
		boolean esito = true;
		try {
			catalogo.aggiungiFarmaco(prezzo, prescrizione, nome, scorte);
		} catch (FarmacoCreationFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
	}

	@Test
	public void testAggiungiFarmacoGiaEsistente() {
		float prezzo = 50;
		boolean prescrizione = false;
		String nome = "OkiTask";
		int scorte = 20;
		boolean esito = true;
		try {
			catalogo.aggiungiFarmaco(prezzo, prescrizione, nome, scorte);
		} catch (FarmacoCreationFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
		try {
			catalogo.aggiungiFarmaco(prezzo, prescrizione, nome, scorte);
		} catch (FarmacoCreationFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertFalse(esito);
	}

}
