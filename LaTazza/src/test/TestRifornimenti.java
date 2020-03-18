package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.controller.DataBaseConnection;
import application.model.rifornimenti.Rifornimenti;
import application.model.rifornimenti.Rifornimento;
import application.utils.TipoCialda;

class TestRifornimenti {
	static DataBaseConnection conn;
	Rifornimenti rifornimenti, rifornimentiEmpty;
	int size = 10, numScatola = 1;

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
		rifornimentiEmpty= new Rifornimenti();
		
		rifornimenti= new Rifornimenti();
		for(int i=0; i<size; i++) {
			rifornimenti.addRifornimento(numScatola,TipoCialda.caffè);
		}
	}
	
	
	@Test
	void testGetRifornimentiByTipoCialda() {
		for(Rifornimento rif : rifornimenti.getRifornimenti()) {
			assertEquals(rif.getTipoCialda(),TipoCialda.caffè);
			assertEquals(rif.getNumeroScatole(),numScatola);
		}
	}
	
	@Test
	void testGetRifornimentiSize() {
		assertEquals(rifornimenti.getRifornimenti().size(),size);
	}
	
	@Test
	void testLoad() throws SQLException {
		rifornimenti.addRifornimento(numScatola, TipoCialda.caffè);
		rifornimenti.print(conn.getConnection());
		rifornimenti=new Rifornimenti();
		rifornimenti.load(conn.getConnection());
		String str=rifornimenti.print();
		rifornimenti=new Rifornimenti();
		rifornimenti.load(conn.getConnection());
		assertEquals(rifornimenti.print(),str);
	}
	
}