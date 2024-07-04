package farmacia.entity;

/**
 * Classe del package entity nel modello BCED, essa implementa la sessione utente.
 */
public class Sessione {

	/**
	 * L'unica istanza di Sessione che implementa il pattern Singleton.
	 */
	private static Sessione uniqueInstance;

	/**
	 * L'utente corrente di cui mantenere attiva la sessione.
	 */
	private EntityUtente utenteCorrente;

	/**
	 * Costruttore privato per impedire la creazione di istanze multiple.
	 */
	private Sessione() {}

	/**
	 * Funzione statica per richiamare l'unica istanza di Sessione o crearne una se non esiste già.
	 * @return l'istanza singleton di Sessione.
	 */
	public static Sessione getInstance() {
		if(uniqueInstance == null) {
			uniqueInstance = new Sessione();
		}
		return uniqueInstance;
	}

	/**
	 * Funzione che restituisce l'utente mantenuto nella sessione.
	 * @return l'utente corrente.
	 */
	public EntityUtente getUtenteCorrente() {
		return utenteCorrente;
	}

	/**
	 * Funzione che permette di sostituire l'utente mantenuto della sessione.
	 * @param utenteCorrente l'utente di cui mantenere la sessione.
	 */
	public void setUtenteCorrente(EntityUtente utenteCorrente) {
		this.utenteCorrente = utenteCorrente;
	}
}


