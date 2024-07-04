package farmacia.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import farmacia.exceptions.DBException;

public class FarmacoDAO {
	private int id;
	private float prezzo;
	private boolean prescrizione;
	private String nome;
	private int scorta;

	private static final Logger logger = Logger.getLogger("FarmacoDAO");

	public FarmacoDAO() { }

	public FarmacoDAO(int id) throws DBException {
		this.id = id;
		this.caricaDaDB(id);
	}

	public FarmacoDAO(int id, float prezzo, boolean prescrizione, String nome, int scorta) {
		this.id = id;
		this.prezzo = prezzo;
		this.prescrizione = prescrizione;
		this.nome = nome;
		this.scorta = scorta;
	}

	public void createFarmaco(float prezzo, boolean prescrizione, String nome, int scorta) throws DBException {
		if (cercaInDB(nome) != 0) {
			throw new DBException(String.format("Farmaco '%s' già esistente", nome));
		}

		if (salvaInDB(prezzo, prescrizione, nome, scorta) == 0)
			throw new DBException(String.format("Errore durante la registrazione del farmaco '%s'.", nome));

		this.id = cercaInDB(nome);
		this.prezzo = prezzo;
		this.prescrizione = prescrizione;
		this.nome = nome;
		this.scorta = scorta;
	}

	private void caricaDaDB(int id) throws DBException {
		String query = String.format("SELECT * FROM farmaci WHERE id = %d;", id);

		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (rs.next()) {
				this.id = rs.getInt("id");
				this.prezzo = rs.getFloat("prezzo");
				this.prescrizione = rs.getBoolean("prescrizione");
				this.nome = rs.getString("nome");
				this.scorta = rs.getInt("scorta");
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante il caricamento dal database del farmaco con" +
				"id %d.%n%s", id, e.getMessage()));
			throw new DBException("Errore nel caricamento di un farmaco.");
		}
	}

	private int cercaInDB(String nome) throws DBException {
		String query = String.format("SELECT * FROM farmaci WHERE nome = '%s';", nome);
		logger.info(query);
		int id = -1;
		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			if (!rs.next())
				return 0;
			id = rs.getInt("id");
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore nella ricerca del farmaco '%s'.%n%s",
				nome, e.getMessage()));
			throw new DBException(String.format("Errore nella ricerca del farmaco '%s'", nome));
		}
		return id;
	}

	private int salvaInDB(float prezzo, boolean prescrizione, String nome, int scorta) throws DBException {
		String query = String.format("INSERT INTO farmaci (prezzo, prescrizione, nome, scorta)" +
				"VALUES (%f, %d, '%s', %d);",
			prezzo, prescrizione ? 1 : 0, nome, scorta
		);

		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);

		} catch (ClassNotFoundException | SQLException e) {
			logger.warning(String.format("Errore durante l'inserimento del farmaco " +
					"'%s' nel database.%n%s",
				nome, e.getMessage()));
			throw new DBException(String.format("Errore nel salvataggio del farmaco '%s'", nome));
		}
		return rs;
	}

	private int eliminaDaDB() {
		String query = String.format("DELETE FROM farmaci WHERE id = %d;", this.id);
		logger.info(query);
		int rs = -1;
		try {
			rs = DBManager.getInstance().executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning("Errore durante l'eliminazione dal DB del farmaco " + this.id);
		}
		return rs;
	}

	public ArrayList<FarmacoDAO> getFarmaci() throws DBException {

		String query = "SELECT * FROM farmaci;";
		ArrayList<FarmacoDAO> arrayListFarmacoDAO = new ArrayList<FarmacoDAO>();
		logger.info(query);

		try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
			while (rs.next()) {
				int id = rs.getInt("id");
				float prezzo = rs.getFloat("prezzo");
				boolean prescrizione = rs.getBoolean("prescrizione");
				String nome = rs.getString("nome");
				int scorta = rs.getInt("scorta");
				FarmacoDAO queryFarmacoDAO = new FarmacoDAO(id, prezzo, prescrizione, nome, scorta);
				arrayListFarmacoDAO.add(queryFarmacoDAO);
			}
		} catch (ClassNotFoundException | SQLException e) {
			logger.warning("Errore durante il caricamento dal database.");
			throw new DBException("Errore nel caricamento di un farmaco.");
		}

		return arrayListFarmacoDAO;
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

	public int getScorta() {
		return scorta;
	}

	public void setScorta(int scorta) {
		this.scorta = scorta;
	}

	public int getId() {
		return id;
	}

}