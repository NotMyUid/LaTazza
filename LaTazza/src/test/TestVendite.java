package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import DataAccessObject.Database.DataBaseConnection;
import application.model.utenti.Persona;
import application.model.vendite.Vendita;
import application.model.vendite.Vendite;
import application.utils.TipoCialda;


public class TestVendite {
	static DataBaseConnection conn;
	Vendite vendite, venditeEmpty;
	Date actualDate = new Date();
	
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
		venditeEmpty = new Vendite();
		vendite = new Vendite();
		vendite.getVendite().add(new Vendita (new Persona("Siji"), 100, TipoCialda.fromString("camomilla"), true));
		vendite.getVendite().add(new Vendita(new Persona("Bon"), 150, TipoCialda.fromString("cioccolata"), false, actualDate));
		vendite.getVendite().add(new Vendita (new Persona("Roll"), 15, TipoCialda.fromString("caffè"), false));
	}
	
	@Test
	void TestVenditeIsEmpty() {
		assertTrue(venditeEmpty.getVendite().isEmpty());
	}
	
	@Test
	void TestVenditeIsNotEmpty() {
		assertFalse(vendite.getVendite().isEmpty());
	}
	
	@Test 
	void testLoad() throws SQLException {
		vendite.print(conn.getConnection());
		vendite=new Vendite();
		vendite.load(conn.getConnection());
		String str=vendite.print();
		vendite=new Vendite();
		vendite.load(conn.getConnection());
		assertEquals(vendite.print(),str);
	}
	
	@ParameterizedTest
	@EnumSource(TipoCialda.class)
	void TestAddVenditaSize(TipoCialda tc) {
		int oldSize = vendite.getVendite().size();
		vendite.addVendita(new Persona("NewAdded"), 75, tc, true);
		int newSize = vendite.getVendite().size();
		assertEquals(oldSize+1,newSize);
	}
	
	@ParameterizedTest
	@EnumSource(TipoCialda.class)
	void TestAddVenditaByGetCliente(TipoCialda tc) {
		int oldSize = vendite.getVendite().size();
		vendite.addVendita(new Persona("NewAdded"), 75, tc, true, actualDate);
		int newSize = oldSize+1;
		assertEquals((Persona)vendite.getVendite().get(newSize-1).getCliente(), new Persona("NewAdded"));
	}
	
	@Test
	void testPrintEmpty() {
		assertEquals(venditeEmpty.print(),"VENDITE\n\n");
		
	}
	
	@Test
	void testPrint() {
		venditeEmpty.addVendita(new Persona("NewAdded"), 75, TipoCialda.fromString("caffèArabica"), true, actualDate);
		assertEquals(venditeEmpty.print(),	"VENDITE\n" +
											actualDate.getTime() + " NewAdded 75 caffèArabica true\n\n");
	}
}
