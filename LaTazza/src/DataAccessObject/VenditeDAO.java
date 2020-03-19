package DataAccessObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import application.model.utenti.Persona;
import application.model.vendite.Vendita;
import application.utils.TipoCialda;

public class VenditeDAO {
	
	public void load(Connection c,ArrayList<Vendita> vendite) throws SQLException {
        String Nome,Cialda;
        int Numero = 0;
        long Data = 0;
        boolean Contanti;
        String query = "SELECT * FROM LATAZZASCHEMA.compra_visitatore";
        for(int i=0;i<=1;++i) {
        	Statement stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while(rs.next()) {
	        	Nome     = rs.getString("nome");
	        	Cialda   = rs.getString("tipo_cialda");
	        	Numero   = rs.getInt("numero_cialde");
		        Data     = rs.getLong("data");
		        Contanti = rs.getBoolean("contanti");
		        vendite.add(new Vendita(
							new Persona(Nome),
							Numero, 
							TipoCialda.fromString(Cialda), 
							Contanti,
							new Date(Data)
							)
		        		);
	        }   stmt.close(); query = "SELECT * FROM LATAZZASCHEMA.compra_dipendente";
        }
	}
	
	public boolean isIn(ArrayList<String> Tabella,String Nome) {
		for(String Curr : Tabella) {
			if(Curr==Nome) return true;
		}
		return false;
	}
	
	public ArrayList<String> extractPersonale(Connection c, String cond) throws SQLException {
		ArrayList<String> extracted = new ArrayList<String>();
		String query;
		if(cond=="Personale") query = "SELECT nome FROM LATAZZASCHEMA.PERSONALE";
		else query = "SELECT nome FROM LATAZZASCHEMA.VISITATORE";
		Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String Nome = "";
        while(rs.next()) {
	        Nome = rs.getString("nome");
	        extracted.add(Nome);
        }   stmt.close();
        return extracted;
	}
	
	public void print(Connection c,ArrayList<Vendita> vendite) throws SQLException {
		String query;
		Statement stmt = c.createStatement();
		ArrayList<String> Personale = extractPersonale(c,"Personale");
		ArrayList<String> Visitatori = extractPersonale(c,"Visitatori");
		for(Vendita vend : vendite) {
			long Epoch=vend.getEpoch();
			String Nome=vend.getCliente().getNome();
			int qta = vend.getQuantita();
			String Cialda = vend.getTipoCialda().toString();
			boolean Contanti = vend.isContanti();
			
			if(isIn(Personale,Nome))
				query = "MERGE into LATAZZASCHEMA.compra_dipendente values ('"+Nome+"','"+Cialda+"',"+qta+",'"+Epoch+"',"+Contanti+")";
			else if (isIn (Visitatori,Nome)) query = "MERGE into LATAZZASCHEMA.compra_visitatore values ('"+Nome+"','"+Cialda+"',"+qta+",'"+Epoch+"',"+Contanti+")";
			else {
					query = "MERGE INTO LATAZZASCHEMA.VISITATORE values ('"+Nome+"')";
					stmt.executeUpdate(query);
					query = "MERGE into LATAZZASCHEMA.compra_visitatore values ('"+Nome+"','"+Cialda+"',"+qta+",'"+Epoch+"',"+Contanti+")";
			}
			stmt.executeUpdate(query);
		}
	}
	
}