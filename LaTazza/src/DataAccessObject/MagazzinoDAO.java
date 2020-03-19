package DataAccessObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import application.utils.TipoCialda;

public class MagazzinoDAO {
	
	public void load(Connection c,HashMap<TipoCialda, Integer> mag) throws SQLException {
		String query = "SELECT * FROM LATAZZASCHEMA.MAGAZZINO";
		Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int Numero = 0;
        String Cialda = "";
		while(rs.next()) {
			   Numero = rs.getInt("qta");
			   Cialda = rs.getString("tipo");
			   mag.put(TipoCialda.fromString(Cialda), Numero);
			} 
		stmt.close();
	}
	
	public void print(Connection c,HashMap<TipoCialda, Integer> mag) throws SQLException {
		String query;
		Statement stmt = c.createStatement();
		for(TipoCialda tipoCialda : mag.keySet()) {
			String Cialda = tipoCialda.toString();
			int qta = mag.get(tipoCialda);
			query = "MERGE into LATAZZASCHEMA.MAGAZZINO values ('"+Cialda+"',"+qta+")";
			stmt.executeUpdate(query);
		}
	}
	
}