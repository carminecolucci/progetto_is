package farmacia.database;

import java.sql.*;

/**
 * Classe del package database nel modello BCED, essa implementa il Database Manager.
 */
public class DBManager {

	/**
	 * Url di accesso al database.
	 */
	public static final String URL = "jdbc:mysql://localhost:3306/";

	/**
	 * Nome del database.
	 */
	public static final String DBNAME = "farmacia";

	/**
	 * Nome del driver JDBC.
	 */
	public static final String DRIVER = "com.mysql.cj.jdbc.Driver";

	/**
	 * Username per l'accesso al database.
	 */
	public static final String USERNAME = "root";

	/**
	 * Password di accesso al database.
	 */
	public static final String PASSWORD = "carmine";

	/**
	 * L'unica istanza di DBManager che implementa il pattern Singleton.
	 */
	private static DBManager uniqueInstance;

	/**
	 * Costruttore privato per non permettere la creazione erronea di un'istanza dall'esterno.
	 */
	private DBManager() {}

	/**
	 * Funzione statica per richiamare l'unica istanza di DBManager o crearne una se non esiste già.
	 * @return l'istanza singleton di DBManager.
	 */
	public static DBManager getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new DBManager();
		}
		return uniqueInstance;
	}

	/**
	 * Metodo per ottenere una nuova connessione al Database.
	 * @return la connessione instaurata.
	 * @throws ClassNotFoundException se la classe non viene trovata.
	 * @throws SQLException in caso di errore nella chiusura della connessione.
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		return DriverManager.getConnection(URL + DBNAME, USERNAME, PASSWORD);
	}

	/**
	 * Metodo per chiudere una connessione esistente al Database.
	 * @param c connessione al database.
	 * @throws SQLException in caso di errore nella chiusura della connessione.
	 */
	public void closeConnection(Connection c) throws SQLException {
		c.close();
	}

	/**
	 * Metodo per eseguire una query CREATE, UPDATE o DELETE sul Database.
	 * @param query la query da eseguire sul database.
	 * @return il numero di righe della tabella affette dalla query.
	 * @throws ClassNotFoundException se la classe non viene trovata.
	 * @throws SQLException in caso di errore nell'esecuzione della query.
	 */
	public int executeQuery(String query) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		int ret;
		try (Statement statement = conn.createStatement()) {
			ret = statement.executeUpdate(query);
		}
		conn.close();

		return ret;
	}

	/**
	 * Metodo per eseguire una query SELECT sul Database.
	 * @param query la query da eseguire sul database.
	 * @return il ResultSet contenente il risultato della query.
	 * @throws ClassNotFoundException se la classe non viene trovata.
	 * @throws SQLException in caso di errore nell'esecuzione della query.
	 */
	public ResultSet selectQuery(String query) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		try (Statement statement = conn.createStatement()) {
			return statement.executeQuery(query);
		}
	}
}
