package farmacia.test;

import farmacia.controller.ControllerUtenti;
import farmacia.database.UtenteDAO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.RegistrationFailedException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegistraUtenteTest {

	private static ControllerUtenti controllerUtenti;

	@BeforeClass
	public static void setUp() {
		controllerUtenti = ControllerUtenti.getInstance();
	}

	@After
	public void tearDown() throws DBException {
		UtenteDAO utenteDAO = new UtenteDAO("user");
		utenteDAO.deleteUtente();
	}

	@Test
	public void testRegistraClienteSuccess() throws ParseException {
		String dateString = "2023-07-06";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = formatter.parse(dateString);
		Date dataNascita = new Date(date.getTime());
		boolean esito = true;
		try {
			controllerUtenti.registraCliente("user", "MiaPassword", "Utente", "Di Prova", dataNascita, "cliente@gmail.com");
		} catch (RegistrationFailedException | DBException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
	}

	@Test
	public void testRegistraClienteGiaRegistrato() throws ParseException {
		String dateString = "2023-07-06";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = formatter.parse(dateString);
		Date dataNascita = new Date(date.getTime());
		boolean esito = true;
		try {
			controllerUtenti.registraCliente("user", "MiaPassword", "Utente", "Di Prova", dataNascita, "cliente@gmail.com");
		} catch (RegistrationFailedException | DBException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		// provo a registrare l'utente per la seconda volta
		try {
			controllerUtenti.registraCliente("user", "MiaPassword", "Utente", "Di Prova", dataNascita, "cliente@gmail.com");
		} catch (RegistrationFailedException | DBException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertFalse(esito);
	}

}
