package application.model.utenti;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import DataAccessObject.PersonaleDAO;
import application.utils.Euro;

public class Personale {
	private PersonaleDAO dao;
	private LinkedHashSet<Persona> personale;
	private LinkedHashSet<PagamentoDebito> pagamentiDebito;
	
	public Personale() {
		dao = new PersonaleDAO();
		personale = new LinkedHashSet<Persona>();
		pagamentiDebito = new LinkedHashSet<PagamentoDebito>();
	}
	
	
	public LinkedHashSet<Persona> getPersonale() {
		return personale;
	}

	public LinkedHashSet<PagamentoDebito> getPagamentiDebito() {
		return pagamentiDebito;
	}


	public void load(Connection c) throws SQLException {
		dao.load(c, personale, pagamentiDebito);
	}


	public boolean addPersona (String p) {	
		return personale.add(new Persona(p));	
	}
	
	
	public boolean removePersona (Persona p) {	
		if(!p.getDebito().ugualeA(new Euro(0)))	return false;
		return personale.remove(p);
	}
	
	
	public Set<Persona> getIndebitati(){
		LinkedHashSet<Persona> personaleConDebiti = new LinkedHashSet<Persona>();
		for(Persona pers : personale) {
			if(!pers.getDebito().ugualeA(new Euro(0))) {
				personaleConDebiti.add(pers);
			}
		}
		return personaleConDebiti;
	}
	
	
	public boolean diminuisciDebito(Persona pers, Euro ammontare) {
		if(!pers.diminuisciDebito(ammontare))
			return false;
		return pagamentiDebito.add(new PagamentoDebito(pers, ammontare));
	}
	
	public String print() {
		String personaleString="PERSONALE\n";
		for(Persona pers : personale)
			personaleString+=pers.toString()+" "+pers.getDebito().getValore()+'\n';
		String pagamentiDebitiString="PAGAMENTO\n";
		for(PagamentoDebito pagDeb : pagamentiDebito) 
			pagamentiDebitiString+=pagDeb.getEpoch()+" "+pagDeb.getPersona()+' '+pagDeb.getAmmontare().getValore()+'\n';
		return personaleString+='\n'+pagamentiDebitiString+'\n';
	}
	
	public void print(Connection c) throws SQLException {
		dao.print(c, personale, pagamentiDebito);
	}

}