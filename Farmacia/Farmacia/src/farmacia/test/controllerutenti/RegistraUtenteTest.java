package farmacia.test.controllerutenti;

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
import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegistraUtenteTest {

	private static ControllerUtenti controllerUtenti;
	private static final String DATE_STRING = "2023-07-06";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String PASSWORD = "MiaPassword";
	private static final String NOME = "Utente";
	private static final String COGNOME = "Di Prova";
	private static final String CLIENTE1_EMAIL = "clienteeee@gmail.com";

	private static final Logger logger = Logger.getLogger("RegistraUtenteTest");

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
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		java.util.Date date = formatter.parse(DATE_STRING);
		Date dataNascita = new Date(date.getTime());
		boolean esito = true;
		try {
			controllerUtenti.registraCliente("user", PASSWORD, NOME, COGNOME, dataNascita, CLIENTE1_EMAIL);
		} catch (RegistrationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);
	}

	@Test
	public void testRegistraClienteGiaRegistratoUsername() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		java.util.Date date = formatter.parse(DATE_STRING);
		Date dataNascita = new Date(date.getTime());
		boolean esito = true;
		try {
			controllerUtenti.registraCliente("user", PASSWORD, NOME, COGNOME, dataNascita, CLIENTE1_EMAIL);
		} catch (RegistrationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		// provo a registrare l'utente per la seconda volta con lo stesso username, con email diversa
		try {
			controllerUtenti.registraCliente("user", PASSWORD, NOME, COGNOME, dataNascita, "pippo@gmail.com");
		} catch (RegistrationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertFalse(esito);
	}

	@Test
	public void testRegistraClienteGiaRegistratoEmail() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		java.util.Date date = formatter.parse(DATE_STRING);
		Date dataNascita = new Date(date.getTime());
		boolean esito = true;
		try {
			controllerUtenti.registraCliente("user", PASSWORD, NOME, COGNOME, dataNascita, CLIENTE1_EMAIL);
		} catch (RegistrationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertTrue(esito);

		// provo a registrare l'utente per la seconda volta con la stessa email, con username diverso
		try {
			controllerUtenti.registraCliente("userNuovo", PASSWORD, NOME, COGNOME, dataNascita, CLIENTE1_EMAIL);
		} catch (RegistrationFailedException e) {
			logger.warning(e.getMessage());
			esito = false;
		}
		assertFalse(esito);
	}

}
