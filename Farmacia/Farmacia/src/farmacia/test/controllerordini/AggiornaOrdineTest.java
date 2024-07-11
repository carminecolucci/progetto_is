package farmacia.test.controllerordini;

import farmacia.controller.ControllerCatalogo;
import farmacia.controller.ControllerOrdini;
import farmacia.controller.ControllerUtenti;
import farmacia.database.FarmacoDAO;
import farmacia.database.UtenteDAO;
import farmacia.dto.DTO;
import farmacia.exceptions.*;
import org.junit.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AggiornaOrdineTest {

	private static ControllerOrdini controllerOrdini;
	private static ControllerUtenti controllerUtenti;
	private static ControllerCatalogo controllerCatalogo;
	private static String idOrdineSuccess;

	@BeforeClass
	public static void setUp() throws ParseException, FarmacoCreationFailedException, FarmacoNotFoundException, OrderCreationFailedException, DBException {

		// inizializzazione dei controller
		controllerOrdini = ControllerOrdini.getInstance();
		controllerUtenti = ControllerUtenti.getInstance();
		controllerCatalogo = ControllerCatalogo.getInstance();

		// registrazione di un cliente
		String dateString = "2023-07-06";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = formatter.parse(dateString);
		Date dataNascita = new Date(date.getTime());
		boolean esito = true;
		try {
			controllerUtenti.registraCliente("testUserOrdine", "MiaPassword", "Utente", "Di Prova", dataNascita, "clienteordine@gmail.com");
		} catch (RegistrationFailedException | DBException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		// login di un cliente
		esito = true;
		try {
			controllerUtenti.loginUtente("testUserOrdine", "MiaPassword");
		} catch (LoginFailedException | DBException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		// aggiunta di un farmaco e creazione di un ordine
		controllerCatalogo.aggiungiFarmaco(50, true, "FarmacoOrdine", 20);
		Map<Integer, Integer> ordine = new HashMap<>();
		ordine.put((int)controllerCatalogo.cercaFarmaco("FarmacoOrdine").get("id"), 20);
		idOrdineSuccess = controllerOrdini.creaOrdine(ordine);

		// login di un farmacista (per caricare gli ordini di tutti i clienti)
		esito = true;
		try {
			controllerUtenti.loginUtente("farmacista", "farmacista"); // il test assume che ESISTA questo account nel DB
		} catch (LoginFailedException | DBException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
	}


	@AfterClass
	public static void tearDown() throws DBException {
		FarmacoDAO.deleteFarmaco("FarmacoOrdine");
		UtenteDAO utenteDAO = new UtenteDAO("testUserOrdine");
		utenteDAO.deleteUtente();
		// TODO: aggiungere codice per eliminare un ordine di acquisto a partire dal suo ID
	}

	@Test
	public void testAggiornaOrdineSuccess() throws DBException {
		boolean esito = true;
		try {
			controllerOrdini.aggiornaOrdine(idOrdineSuccess);
		} catch (OrderNotFoundException e) {
			esito = false;
		}
		assertTrue(esito);

		esito = false;
		for (DTO ordineAcquisto : controllerOrdini.visualizzaOrdiniFarmacia()) {
			String id = (String)ordineAcquisto.get("id");
			if (id.equals(idOrdineSuccess)) {
				assertTrue((boolean)ordineAcquisto.get("ritirato"));
				esito = true;
			}
		}
		assertTrue(esito);
	}

	@Test
	public void testAggiornaOrdineCheNonEsiste() {
		boolean esito = true;
		try {
			controllerOrdini.aggiornaOrdineAcquisto("id-inventato");
		} catch (OrderNotFoundException | FarmacoNotFoundException e) {
			esito = false;
		}
		assertFalse(esito);
	}

}
