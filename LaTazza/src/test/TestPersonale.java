package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.controller.DataBaseConnection;
import application.model.utenti.Persona;
import application.model.utenti.Personale;
import application.utils.Euro;


class TestPersonale {
	static DataBaseConnection conn;
	Personale personale;
	
	@BeforeAll
	static void connect() throws ClassNotFoundException, SQLException {
		conn = new DataBaseConnection();
		conn.initDataBase();
	}
	
	@AfterAll
	static void close() throws SQLException {
		conn.closeDataBase();
	}
	
	@BeforeEach
	void setUp() throws Exception {
		personale = new Personale();
		personale.addPersona("persona1");
		personale.addPersona("persona2");
		personale.addPersona("persona3");
	}
	
	
	@Test
	void testGetPersonale() {
		assertEquals(personale.getPersonale().size(),3);
	}
	
	@Test
	void testGetIndebitatiSize() {
		for(Persona persona : personale.getPersonale()) {
			persona.aumentaDebito(new Euro(5));
		}
		assertEquals(personale.getIndebitati().size(),3);
	}
	
	@Test
	void testGetIndebitatiDebito() {
		for(Persona persona : personale.getPersonale()) {
			persona.aumentaDebito(new Euro(5));
		}
		for(Persona persona : personale.getIndebitati()) {
			assertEquals(persona.getDebito(),new Euro(5));
		}
	}
	
	
	@Test
	void testAddPersonaTrue() {
		assertTrue(personale.addPersona("persona4"));
	}
	
	@Test
	void testAddPersonaFalse() {
		assertFalse(personale.addPersona("persona1"));
	}
	
	@Test
	void testRemovePersonaTrue() {
		assertTrue(personale.removePersona(new Persona("persona1")));
	}
	
	@Test
	void testRemovePersonaFalse() {
		Persona pers=null;
		for(Persona persona : personale.getPersonale()) {
			pers=persona;
			persona.aumentaDebito(new Euro(5));
		}
		assertFalse(personale.removePersona(pers));
	}
	
	@Test
	void testDiminuisciDebitoTrue() {
		for(Persona persona : personale.getPersonale()) {
			persona.aumentaDebito(new Euro(5));
			assertTrue(personale.diminuisciDebito(persona, new Euro(5)));
		}
		assertEquals(personale.getPagamentiDebito().size(),3);
	}
	
	@Test
	void testDiminuisciDebitoFalseAmmontareMaggioreDebito() {
		for(Persona persona : personale.getPersonale()) {
			persona.aumentaDebito(new Euro(5));
			assertFalse(personale.diminuisciDebito(persona, new Euro(8)));
		}
	}
	
	@Test
	void testDiminuisciDebito() {
		for(Persona persona : personale.getPersonale()) {
			persona.aumentaDebito(new Euro(5));
			personale.diminuisciDebito(persona, new Euro(3));
		}
		for(Persona persona : personale.getIndebitati()) {
			assertEquals(persona.getDebito(),new Euro(2));
		}
	}
	
	@Test
	void testDiminuisciDebitoSize() {
		for(Persona persona : personale.getPersonale()) {
			persona.aumentaDebito(new Euro(5));
			personale.diminuisciDebito(persona, new Euro(5));
		}
		assertEquals(personale.getIndebitati().size(),0);
	}
	
	@Test
	void testLoad() throws SQLException {
		personale.print(conn.getConnection());
		personale=new Personale();
		personale.load(conn.getConnection());
		String str=personale.print();
		personale=new Personale();
		personale.load(conn.getConnection());
		assertEquals(personale.print(),str);
	}
	
}