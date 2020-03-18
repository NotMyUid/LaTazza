package application.model.rifornimenti;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import application.model.rifornimenti.Rifornimento;
import application.utils.TipoCialda;

public class Rifornimenti {
	
	private ArrayList<Rifornimento> rifornimenti;
	
	public Rifornimenti() {
		rifornimenti=new ArrayList<Rifornimento>();
	}
	
	
	public ArrayList<Rifornimento> getRifornimenti() {
		return rifornimenti;
	}

	
	public void load(Connection c) throws SQLException {
		String query = "SELECT * FROM LATAZZASCHEMA.RIFORNIMENTO";
		Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        long Data = 0;
        int Numero = 0;
        String Cialda = "";
        while(rs.next()) {
	        Data   = rs.getLong("dataR");
	        Numero = rs.getInt("numero_cialde");
	        Cialda = rs.getString("tipo_cialda");
	        rifornimenti.add(new Rifornimento(
					  Numero, 
					  TipoCialda.fromString(Cialda), 
					  new Date(Data))
					  );
        }   stmt.close();
	}
	

	public boolean addRifornimento (int numeroScatole, TipoCialda tc) {		
		return rifornimenti.add(new Rifornimento(numeroScatole, tc));	
	}
	
	public String print() {
		String rifornimentoString="RIFORNIMENTI\n";
		for(Rifornimento rif : rifornimenti)
			rifornimentoString+=String.valueOf(rif.getEpoch())+" "+rif.getNumeroScatole()+" "+rif.getTipoCialda()+'\n';
		return rifornimentoString+='\n';
	}
	
	public void print(Connection c) throws SQLException {
		String query;
		Statement stmt = c.createStatement();
		for(Rifornimento rif : rifornimenti) {
			long Data = rif.getEpoch();
			int  Numero = rif.getNumeroScatole();
			String Cialda = rif.getTipoCialda().toString();
			query = "MERGE into LATAZZASCHEMA.RIFORNIMENTO values ('"+Data+"',"+Numero+",'"+Cialda+"')";
			stmt.executeUpdate(query);
		}
	}
	
}