package farmacia.test.controllerordini;

import farmacia.controller.ControllerCatalogo;
import farmacia.controller.ControllerOrdini;
import farmacia.controller.ControllerUtenti;
import farmacia.database.FarmacoDAO;
import farmacia.database.UtenteDAO;
import farmacia.dto.DTO;
import farmacia.exceptions.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class VisualizzaStoricoOrdiniTest {
	private static ControllerOrdini controllerOrdini;
	private static ControllerUtenti controllerUtenti;
	private static ControllerCatalogo controllerCatalogo;

	@BeforeClass
	public static void setUp() throws ParseException {
		controllerOrdini = ControllerOrdini.getInstance();
		controllerUtenti = ControllerUtenti.getInstance();
		controllerCatalogo = ControllerCatalogo.getInstance();

		String dateString = "2023-07-06";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = formatter.parse(dateString);
		Date dataNascita = new Date(date.getTime());
		boolean esito = true;
		try {
			controllerUtenti.registraCliente("testUser", "MiaPassword", "Utente", "Di Prova", dataNascita, "cliente@gmail.com");
		} catch (RegistrationFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
	}

	@AfterClass
	public static void tearDown() throws DBException {
		UtenteDAO utenteDAO = new UtenteDAO("testUser");
		utenteDAO.deleteUtente();
		FarmacoDAO.deleteFarmaco("test1");
		FarmacoDAO.deleteFarmaco("test2");
		FarmacoDAO.deleteFarmaco("test3");
	}

	@Test
	public void testVisualizzaStoricoOrdini() throws DBException {
		boolean esito = true;
		try {
			controllerUtenti.loginUtente("testUser", "MiaPassword");
			controllerCatalogo.aggiungiFarmaco(23, false, "test1", 23);
			controllerCatalogo.aggiungiFarmaco(34, true, "test2", 34);
			controllerCatalogo.aggiungiFarmaco(45, true, "test3", 45);

			int id1 = (int)controllerCatalogo.cercaFarmaco("test1").get("id");
			int id2 = (int)controllerCatalogo.cercaFarmaco("test2").get("id");
			int id3 = (int)controllerCatalogo.cercaFarmaco("test3").get("id");

			Map<Integer, Integer> farmaciQuantita = new HashMap<>();
			farmaciQuantita.put(id1, 3);
			farmaciQuantita.put(id2, 4);
			farmaciQuantita.put(id3, 5);

			controllerOrdini.creaOrdine(farmaciQuantita);

			List<DTO> ordini = controllerOrdini.visualizzaStoricoOrdini();
			DTO ordine = ordini.getFirst();
			assertEquals("testUser", ordine.get("cliente"));
			Map<DTO, Integer> quantitaFarmaci = (Map<DTO, Integer>)ordine.get("quantitaFarmaci");
			for (Map.Entry<DTO, Integer> entry: quantitaFarmaci.entrySet()) {
				DTO farmaco  = entry.getKey();
				int quantita = farmaciQuantita.get(farmaco.get("id"));
				assertEquals(quantita, (int) entry.getValue());
			}

		} catch (LoginFailedException | FarmacoNotFoundException | FarmacoCreationFailedException |
		         OrderCreationFailedException e) {
			fail("Errore nelle dipendenze: " + e.getMessage());
		}
		assertTrue(esito);
	}
}
