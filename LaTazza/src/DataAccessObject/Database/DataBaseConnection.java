package DataAccessObject.Database;

import java.sql.*;

public class DataBaseConnection {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:~/LaTazza";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "pass";


    private Connection connection;

    public DataBaseConnection(){
    }
    
    public void initDataBase() throws ClassNotFoundException, SQLException {
    	if(connection==null||connection.isClosed()){
	    	Class.forName(DB_DRIVER); 
			connection = DriverManager.getConnection (DB_CONNECTION, DB_USER,DB_PASSWORD);
    	}
    }

    public void closeDataBase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    //forza chiusura connessione db.
    protected void finalize(){
        try {
            connection.close();
        } catch (SQLException ignored) { }
    }

}