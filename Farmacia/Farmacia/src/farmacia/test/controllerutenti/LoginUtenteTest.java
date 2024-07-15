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
import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginUtenteTest {

	private static ControllerUtenti controllerUtenti;
	private static final Logger logger = Logger.getLogger("LoginUtenteTest");

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
		} catch (RegistrationFailedException e) {
			logger.warning(e.getMessage());
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
	public void testLoginUtenteRegistrato() {
		boolean esito = true;
		try {
			controllerUtenti.loginUtente("user", "MiaPassword");
		} catch (LoginFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
	}

	@Test
	public void testLoginUtenteNonRegistrato() {
		boolean esito = true;
		try {
			controllerUtenti.loginUtente("user_sconosciuto", "passwd");
		} catch (LoginFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertFalse(esito);
	}

	@Test
	public void testLoginUtenteRegistratoPasswordErrata() {
		boolean esito = true;
		try {
			controllerUtenti.loginUtente("user", "passwd");
		} catch (LoginFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertFalse(esito);
	}
}
