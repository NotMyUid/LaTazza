package application.model.rifornimenti;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import DataAccessObject.RifornimentiDAO;
import application.model.rifornimenti.Rifornimento;
import application.utils.TipoCialda;

public class Rifornimenti {
	private RifornimentiDAO dao;
	private ArrayList<Rifornimento> rifornimenti;
	
	public Rifornimenti() {
		dao = new RifornimentiDAO();
		rifornimenti=new ArrayList<Rifornimento>();
	}
	
	
	public ArrayList<Rifornimento> getRifornimenti() {
		return rifornimenti;
	}

	
	public void load(Connection c) throws SQLException {
		dao.load(c, rifornimenti);
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
		dao.print(c, rifornimenti);
	}
	
}