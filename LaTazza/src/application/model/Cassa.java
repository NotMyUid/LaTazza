package application.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import application.utils.Euro;

public class Cassa {

	private Euro disponibilita;

	public Cassa() {
		disponibilita = new Euro(0);
	}

	public void load(Connection c) throws SQLException {
		String query = "SELECT * FROM LATAZZASCHEMA.CASSA";
		Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        long Euro = 0;
        int Cent = 0;
        while(rs.next()) {
	        Euro = rs.getLong("euro");
	        Cent = rs.getInt("centesimi");
        }   stmt.close();
        disponibilita=new Euro(Euro,Cent);	
	}
	
	public boolean riceviPagamento(Euro euro) {
		if (euro.minoreDi(new Euro(0)))
			return false;
		disponibilita.somma(euro);
		return true;
	}

	public boolean effettuaPagamento(Euro euro) {
		if (euro.minoreDi(new Euro(0)) || disponibilita.minoreDi(euro))
			return false;
		disponibilita.sottrai(euro);
		return true;
	}

	public Euro getDisponibilita() {
		return disponibilita;
	}

	@Override
	public String toString() {
		return String.format(Locale.US, "%,.2f", (double)disponibilita.getValore() / 100);
	}
	
	public String print() {
		return "CASSA\n"+disponibilita.getValore() + "\n\n";
	}


	public void print(Connection c) throws SQLException {
		String query = "UPDATE LATAZZASCHEMA.CASSA SET euro = '"+disponibilita.getValore()/100+"', centesimi = '"+disponibilita.toString().substring(disponibilita.toString().length()-4, disponibilita.toString().length()-2)+"'";
		Statement stmt = c.createStatement();
        stmt.executeUpdate(query);
	}

}
