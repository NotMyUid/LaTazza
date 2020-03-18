package application.model.rifornimenti;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import application.utils.TipoCialda;

public class Magazzino {
	
	HashMap<TipoCialda, Integer> mag=new HashMap<TipoCialda,Integer>();
	
	public Magazzino() {	
		mag.put(TipoCialda.caffè, 0);
		mag.put(TipoCialda.caffèArabica, 0);
		mag.put(TipoCialda.thè, 0);
		mag.put(TipoCialda.thèLimone, 0);
		mag.put(TipoCialda.cioccolata, 0);
		mag.put(TipoCialda.camomilla, 0);
	}


	public void load(Connection c) throws SQLException {
		String query = "SELECT * FROM LATAZZASCHEMA.MAGAZZINO";
		Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int Numero = 0;
        String Cialda = "";
        while(rs.next()) {
	        Numero = rs.getInt("qta");
	        Cialda = rs.getString("tipo");
	        mag.put(TipoCialda.fromString(Cialda), Numero);
        }   stmt.close();
	}
	
	
	public int numeroCialdeDisponibili(TipoCialda tipoCialda) {	
		return mag.get(tipoCialda);
	}
	
	
	public boolean aggiungiRifornimento(int numScatole, TipoCialda tipoCialda) {
		if(numScatole<1)
			return false;
		mag.put(tipoCialda, mag.get(tipoCialda)+50*numScatole);
		return true;
	}
	
	
	
	public boolean rimuoviCialde(int numeroCialde,TipoCialda tipoCialda) {
		if(numeroCialde<1)
			return false;
		mag.put(tipoCialda,mag.get(tipoCialda)-numeroCialde);
		return true;
	}
	
	public String print() {
		String magString="MAGAZZINO\n";
		for(TipoCialda tipoCialda : mag.keySet())
			magString+=tipoCialda.toString()+" "+mag.get(tipoCialda)+'\n';
		return magString+="\n";
	}
	
	public void print(Connection c) throws SQLException {
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
