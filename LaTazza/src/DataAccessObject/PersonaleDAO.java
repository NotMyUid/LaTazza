package DataAccessObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashSet;

import application.model.utenti.PagamentoDebito;
import application.model.utenti.Persona;
import application.utils.Euro;

public class PersonaleDAO {
	
	public void load(Connection c,LinkedHashSet<Persona> personale,LinkedHashSet<PagamentoDebito> pagamentiDebito) throws SQLException {
		String query = "SELECT * FROM LATAZZASCHEMA.PERSONALE";
		String cond  = "p";
		for (int i=0;i<=1;++i) {
			if(cond=="p") {
				Statement stmt = c.createStatement();
		        ResultSet rs = stmt.executeQuery(query);
		        String Nome="";
		        long Euro = 0;
		        int Cent = 0;
		        while(rs.next()) {
		        	Nome = rs.getString("nome"); 
			        Euro = rs.getLong("euro");
			        Cent = rs.getInt("centesimi");
			        personale.add(new Persona(Nome, new Euro(Euro,Cent)));
		        }   stmt.close();
		        query = "SELECT * FROM LATAZZASCHEMA.PAGAMENTO_DEBITO";
		        cond = "d";
			} else {
					Statement stmt = c.createStatement();
					ResultSet rs = stmt.executeQuery(query);
			        String Nome="";
			        long Data = 0;
			        long Euro = 0;
			        int Cent = 0;
			        while(rs.next()) {
			        	Nome = rs.getString("nome");
			        	Data = rs.getLong("data");
				        Euro = rs.getLong("euro");
				        Cent = rs.getInt("centesimi");
				        pagamentiDebito.add(new PagamentoDebito(new Persona(Nome),new Euro(Euro,Cent),new Date(Data)));
			        }   stmt.close();	
			}
		}
	}
	
	public void print(Connection c,LinkedHashSet<Persona> personale,LinkedHashSet<PagamentoDebito> pagamentiDebito) throws SQLException {
		String query;
		Statement stmt = c.createStatement();
		for(Persona pers : personale) {
			String Nome = pers.toString();
			long   Euro = pers.getDebito().getValore()/100;
			String Cent = pers.getDebito().toString().substring(pers.getDebito().toString().length()-4, pers.getDebito().toString().length()-2);
			query = "MERGE into LATAZZASCHEMA.PERSONALE values ('"+Nome+"','"+Euro+"',"+Cent+")";
			stmt.executeUpdate(query);
		}
		
		for(PagamentoDebito pagDeb : pagamentiDebito) {
			long   Data = pagDeb.getEpoch();
			String Nome = pagDeb.getPersona().getNome();
			long   Euro = pagDeb.getAmmontare().getValore()/100;
			String Cent = pagDeb.getAmmontare().toString().substring(pagDeb.getAmmontare().toString().length()-4, pagDeb.getAmmontare().toString().length()-2);
			query = "MERGE into LATAZZASCHEMA.PAGAMENTO_DEBITO values ('"+Nome+"','"+Data+"','"+Euro+"',"+Cent+")";
			stmt.executeUpdate(query);
		}
	}
	
	
	
}