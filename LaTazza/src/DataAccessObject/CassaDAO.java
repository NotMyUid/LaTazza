package DataAccessObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.utils.Euro;

public class CassaDAO {
	
	public Euro load(Connection c) throws SQLException {
		String query = "SELECT * FROM LATAZZASCHEMA.CASSA";
		Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        long Euro = 0;
        int Cent = 0;
        while(rs.next()) {
	        Euro = rs.getLong("euro");
	        Cent = rs.getInt("centesimi");
        }
        stmt.close();
        return new Euro(Euro,Cent);	
	}
	
	public void print(Connection c,Euro disponibilita) throws SQLException {
		String query = "UPDATE LATAZZASCHEMA.CASSA SET euro = '"+disponibilita.getValore()/100+"', centesimi = '"+disponibilita.toString().substring(disponibilita.toString().length()-4, disponibilita.toString().length()-2)+"'";
		Statement stmt = c.createStatement();
        stmt.executeUpdate(query);
	}
	
}