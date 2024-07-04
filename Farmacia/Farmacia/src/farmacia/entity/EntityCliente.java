package farmacia.entity;

import farmacia.database.UtenteDAO;
import farmacia.exceptions.DBException;

import java.util.ArrayList;
import java.util.Date;

public class EntityCliente extends  EntityUtente {
	ArrayList<EntityOrdine> storicoOrdini;

	public EntityCliente(String username, String password, String nome, String cognome, Date dataNascita, String email) {
		super(username, password, nome, cognome, dataNascita, email);
	}

	public EntityCliente(UtenteDAO utenteDAO) {
		// se chiamo questo costruttore, significa che sono già registrato
		super(utenteDAO);
		storicoOrdini = new ArrayList<>();
		// TODO: popolare lo storico ordini attraverso OrdineDAO
	}

	public ArrayList<EntityOrdine> visualizzaStoricoOrdini() {
		return storicoOrdini;
	}

	/**
	 * Funzione che permette di creare un nuovo Cliente nel DB a partire da
	 * un'istanza di <code>EntityCliente</code> già popolata.
	 * @throws DBException Lanciata quando si cerca di salvare un utente già registrato
	 */
	public void salvaInDB() throws DBException {
		UtenteDAO utenteDAO = new UtenteDAO();
		utenteDAO.createUtente(this.getNome(), this.getCognome(), this.getUsername(), this.getPassword(), this.getDataNascita(), this.getEmail());
	}
}
