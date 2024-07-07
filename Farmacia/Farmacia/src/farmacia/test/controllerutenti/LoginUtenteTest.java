package farmacia.test.controllerutenti;

import farmacia.controller.ControllerUtenti;
import farmacia.database.UtenteDAO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.LoginFailedException;
import farmacia.exceptions.RegistrationFailedException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginUtenteTest {

	private static ControllerUtenti controllerUtenti;

	@BeforeClass
	public static void setUp() throws ParseException {
		controllerUtenti = ControllerUtenti.getInstance();
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

	@AfterClass
	public static void tearDown() throws DBException {
		UtenteDAO utenteDAO = new UtenteDAO("user");
		utenteDAO.deleteUtente();
	}

	@Test
	public void testLoginUtenteRegistrato() throws DBException {
		boolean esito = true;
		try {
			controllerUtenti.loginUtente("user", "MiaPassword");
		} catch (LoginFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
	}

	@Test
	public void testLoginUtenteNonRegistrato() throws DBException {
		boolean esito = true;
		try {
			controllerUtenti.loginUtente("user_sconosciuto", "passwd");
		} catch (LoginFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertFalse(esito);
	}

	@Test
	public void testLoginUtenteRegistratoPasswordErrata() throws DBException {
		boolean esito = true;
		try {
			controllerUtenti.loginUtente("user", "passwd");
		} catch (LoginFailedException e) {
			System.err.println(e.getMessage());
			esito = false;
		}
		assertFalse(esito);
	}
}
