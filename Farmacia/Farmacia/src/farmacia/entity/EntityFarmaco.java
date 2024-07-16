package farmacia.entity;

import farmacia.database.FarmacoDAO;
import farmacia.exceptions.DBException;

import java.util.UUID;

/**
 * Classe che rappresenta un farmaco.
 */
public class EntityFarmaco {
	private int id;
	private float prezzo;
	private boolean prescrizione;
	private String nome;
	private int scorte;
	private final String codice;

	/**
	 * Costruttore che permette di popolare una <code>EntityFarmaco</code> a partire da un <code>FarmacoDA0</code>.
	 * @param farmacoDAO istanza di <code>FarmacoDA0</code> già popolata.
	 */
	public EntityFarmaco(FarmacoDAO farmacoDAO) {
		this.id = farmacoDAO.getId();
		this.prezzo = farmacoDAO.getPrezzo();
		this.prescrizione = farmacoDAO.isPrescrizione();
		this.nome = farmacoDAO.getNome();
		this.scorte = farmacoDAO.getScorte();
		this.codice = farmacoDAO.getCodice();
	}

	/**
	 * Costruttore di <code>EntityFarmaco</code>
	 * @param prezzo prezzo del farmaco.
	 * @param prescrizione se il farmaco necessita di una prescrizione per essere acquistato.
	 * @param nome nome del farmaco.
	 * @param scorte scorte del farmaco.
	 */
	public EntityFarmaco(float prezzo, boolean prescrizione, String nome, int scorte) {
		this.prezzo = prezzo;
		this.prescrizione = prescrizione;
		this.nome = nome;
		this.scorte = scorte;
		this.codice = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
	}

	/**
	 * Funzione che permette di salvare un nuovo Farmaco nel DB a partire da
	 * un'istanza di <code>EntityFarmaco</code> già popolata.
	 * @throws DBException Se il farmaco esiste già, o non è possibile accedere al DB.
	 */
	public void salvaInDB() throws DBException {
		FarmacoDAO farmaco = new FarmacoDAO(this.prezzo, this.prescrizione, this.nome, this.scorte, this.codice);
		farmaco.createFarmaco();
		this.id = farmaco.getId();
	}

	/**
	 * Funzione che permette di eliminare un farmaco dal DB a partire da
	 * un'istanza di <code>EntityFarmaco</code> già popolata.
	 * @throws DBException Se il farmaco non esiste, o non è possibile accedere al DB.
	 */
	public void eliminaDaDB() throws DBException {
		FarmacoDAO farmaco = new FarmacoDAO(this.id);
		farmaco.deleteFarmaco();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}

	public boolean isPrescrizione() {
		return prescrizione;
	}

	public void setPrescrizione(boolean prescrizione) {
		this.prescrizione = prescrizione;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getScorte() {
		return scorte;
	}

	public void setScorte(int scorte) {
		this.scorte = scorte;
	}

	public String getCodice() {
		return codice;
	}
}
