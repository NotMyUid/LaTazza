package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import application.controller.DataBaseConnection;
import application.model.Cassa;
import application.utils.Euro;



public class TestCassa {
	static DataBaseConnection conn;
	Cassa cassa;
	
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
	void setUp() {
		cassa=new Cassa();
	}
	
	@Test
	void testVuotaOnCreate() {
		assertTrue(cassa.getDisponibilita().ugualeA(new Euro(0)));
	}
	
	@Test
	void testEffettuaPagamentoSuperiore() {
		assertFalse(cassa.effettuaPagamento(new Euro(42)));
	}

	
	@Test
	void testEffettuaPagamento() {
		cassa.riceviPagamento(new Euro(100));
		assertTrue(cassa.effettuaPagamento(new Euro(42)));
	}
	
	
	@ParameterizedTest
	@CsvSource({ "42","10"})
	void testRiceviPagamento(int ammontareInt) {
		Euro disponibilitaIniziale = cassa.getDisponibilita();
		Euro ammontareEuro = new Euro(ammontareInt); 
		cassa.riceviPagamento(ammontareEuro);
		assertTrue(cassa.getDisponibilita().ugualeA(disponibilitaIniziale.somma(ammontareEuro)));
	}
	
	@ParameterizedTest
	@CsvSource({ "-42","-10"})
	void testRiceviPagamentoNeg(int ammontareInt) {
		assertFalse(cassa.riceviPagamento(new Euro(ammontareInt)));
	}
	
	
	@Test
	void testToString() {
		assertEquals(cassa.toString(),"0.00");
	}
	
	
	@Test
	void testPrint() {
		assertEquals(cassa.print(),"CASSA\n0\n\n");
	}
	
	
	@Test 
	void testLoad() throws SQLException {
		cassa.load(conn.getConnection());
		String str=cassa.print();
		cassa=new Cassa();
		cassa.load(conn.getConnection());
		assertEquals(cassa.print(),str);
	}
	
}
