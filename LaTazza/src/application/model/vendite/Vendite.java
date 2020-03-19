package application.model.vendite;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import DataAccessObject.VenditeDAO;
import application.model.utenti.Cliente;
import application.model.vendite.Vendita;
import application.utils.TipoCialda;

public class Vendite {
	private VenditeDAO dao;
	private ArrayList<Vendita> vendite;
	
	public Vendite() {
		dao = new VenditeDAO();
		vendite = new ArrayList<Vendita>();
	}
	
	
	public ArrayList<Vendita> getVendite() {
		return vendite;
	}
	
	
	public void load(Connection c) throws SQLException {
        dao.load(c,vendite);
	}
	
	
	public boolean addVendita (Cliente cl, int quantita, TipoCialda tipoCialda, boolean cont) {
		return vendite.add(new Vendita(cl, quantita, tipoCialda, cont));	
	}
	
	public boolean addVendita (Cliente cl, int quantita, TipoCialda tipoCialda, boolean cont, Date date) {
		return vendite.add(new Vendita(cl, quantita, tipoCialda, cont, date));	
	}	
	
	
	public String print() {
		String venditeString="VENDITE\n";
		for(Vendita vend : vendite)
			venditeString+=vend.getEpoch()+" "+vend.getCliente().getNome()+" "+vend.getQuantita()+" "+vend.getTipoCialda().toString()+" "+vend.isContanti()+'\n';
		return venditeString+='\n';
	}
	
	public void print(Connection c) throws SQLException {
		dao.print(c, vendite);
	}
	
}
