package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import application.controller.DataBaseConnection;
import application.model.rifornimenti.Magazzino;
import application.utils.TipoCialda;

class TestMagazzino {
	static DataBaseConnection conn;
	Magazzino magazzino;
	
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
		magazzino=new Magazzino();
	}
	
	
	@ParameterizedTest
	@EnumSource(TipoCialda.class)
	void testVuotoOnCreate(TipoCialda tipoCialda) {
		assertEquals(magazzino.numeroCialdeDisponibili(tipoCialda),0);
	}
	
	@ParameterizedTest
	@EnumSource(TipoCialda.class)
	void testNumeroCialdeDisponibili(TipoCialda tipoCialda) {
		assertEquals(magazzino.numeroCialdeDisponibili(tipoCialda),0);
	}
	
	@ParameterizedTest
	@EnumSource(TipoCialda.class)
	void testAggiungiRifornimento(TipoCialda tipoCialda) {
		magazzino.aggiungiRifornimento(1,tipoCialda);
		assertEquals(magazzino.numeroCialdeDisponibili(tipoCialda),50);
	}
	
	@ParameterizedTest
	@EnumSource(TipoCialda.class)
	void testAggiungiRifornimentoNeg(TipoCialda tipoCialda) {
		assertFalse(magazzino.aggiungiRifornimento(-1,tipoCialda));
	}
	
	@ParameterizedTest
	@EnumSource(TipoCialda.class)
	void testRimuoviCialde(TipoCialda tipoCialda) {
		magazzino.aggiungiRifornimento(1,tipoCialda);
		magazzino.rimuoviCialde(34, tipoCialda);
		assertEquals(magazzino.numeroCialdeDisponibili(tipoCialda),16);
	}
	
	@ParameterizedTest
	@EnumSource(TipoCialda.class)
	void testRimuoviCialdeNeg(TipoCialda tipoCialda) {
		assertFalse(magazzino.rimuoviCialde(-34, tipoCialda));
	}
	

	@ParameterizedTest
	@EnumSource(TipoCialda.class)
	void testGetRifornimentiSize(TipoCialda tipoCialda) {
		int before = magazzino.numeroCialdeDisponibili(tipoCialda);
		magazzino.aggiungiRifornimento(1,tipoCialda);
		magazzino.aggiungiRifornimento(1,tipoCialda);
		magazzino.aggiungiRifornimento(1,tipoCialda);
		assertEquals(magazzino.numeroCialdeDisponibili(tipoCialda),before+150);
	}
	
	@Test
	void testLoad() throws SQLException {
		magazzino.load(conn.getConnection());
		String str=magazzino.print();
		magazzino=new Magazzino();
		magazzino.load(conn.getConnection());
		assertEquals(magazzino.print(),str);
	}

}
