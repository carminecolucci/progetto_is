package farmacia.controller;

import farmacia.dto.DTO;
import farmacia.entity.EntityFarmacia;
import farmacia.entity.EntityUtente;
import farmacia.entity.Sessione;
import farmacia.exceptions.LoginFailedException;
import farmacia.exceptions.RegistrationFailedException;

import java.util.Date;

public class ControllerUtenti {
	
	/**
	 * L'unica istanza di <code>ControllerUtenti</code> che implementa il pattern Singleton.
	 */
	private static ControllerUtenti uniqueInstance;

	/**
	 * Costruttore privato per impedire la creazione di istanze multiple.
	 */
	private ControllerUtenti() {}

	/**
	 * Funzione statica per richiamare l'unica istanza di <code>ControllerUtenti</code> o crearne una se non esiste già.
	 * @return l'istanza singleton di <code>ControllerUtenti</code>.
	 */
	public static ControllerUtenti getInstance() {
		if(uniqueInstance == null) {
			uniqueInstance = new ControllerUtenti();
		}
		return uniqueInstance;
	}

	public void registraCliente(String username, String password, String nome, String cognome, Date dataNascita, String email) throws RegistrationFailedException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		farmacia.registraCliente(username, password, nome, cognome, dataNascita, email);
	}

	public void loginUtente(String username, String password) throws LoginFailedException {
		EntityFarmacia farmacia = EntityFarmacia.getInstance();
		farmacia.loginUtente(username, password);
	}

	public DTO getUtenteCorrente() {
		EntityUtente utenteCorrente = Sessione.getInstance().getUtenteCorrente();
		DTO dto = new DTO();
		dto.set("nome", utenteCorrente.getNome());
		dto.set("cognome", utenteCorrente.getCognome());
		dto.set("tipoUtente", utenteCorrente.getTipoUtente());
		return dto;
	}
}
