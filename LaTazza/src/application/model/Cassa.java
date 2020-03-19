package application.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

import DataAccessObject.CassaDAO;
import application.utils.Euro;

public class Cassa {

	private CassaDAO dao;
	private Euro disponibilita;

	public Cassa() {
		dao = new CassaDAO();
		disponibilita = new Euro(0);
	}

	public void load(Connection c) throws SQLException {
		disponibilita = dao.load(c);
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
		dao.print(c,disponibilita);
	}

}
