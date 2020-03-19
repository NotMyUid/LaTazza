package DataAccessObject.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBasePopulator {

    private DataBaseConnection databaseConnectionHandler;


    public DataBasePopulator(DataBaseConnection database) {
        databaseConnectionHandler = database;
    }

    /**
     * Controlla se il database è già configurato.
     * @return true se già configurato,false altrimenti.
     */
    public boolean existsSchema() {
        PreparedStatement stat;
        ResultSet rs;
        Connection connection = databaseConnectionHandler.getConnection();
        try {
            //faccio una select su una delle tabelle dello schema, se non c'è vuol dire che non è stato creato lo schema
            stat = connection.prepareStatement("SELECT * from LATAZZASCHEMA.CIALDE ");
            rs = stat.executeQuery();
            rs.next();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


    public void createSchema() {
        try {
            updateTable(confiDatabaseSchemaString);
        } catch (Exception e) {
            throw  new Error("Impossibile configurare database",e);
        }
    }


    /**
     * @brief Se viene passato true esegue alcuni insseriment nel db per la presentazione del progetto.
     */
    public  void inserimentiTabellePresentazione(boolean eseguireInserimenti) {
        if(eseguireInserimenti){
            try {
                updateTable(inserimentiPresentazione);
            } catch (Exception e) {
                throw  new Error("Impossibile inserire inserimentiPresentazione ",e);
            }
        }
    }


    /**PRIVATE METHODS**/


    private void updateTable(String queryes) throws SQLException {
        PreparedStatement stmt;
        Connection connection = databaseConnectionHandler.getConnection();
        stmt = connection.prepareStatement(queryes);
        stmt.execute();stmt.close();
    }



    private static final String confiDatabaseSchemaString =
            "create schema LATAZZASCHEMA;" +

            "create table LATAZZASCHEMA.cialde(" +
            "  tipo varchar(64) not null primary key," +
            "  prezzo_euro int not null default (0) check ( prezzo_euro >=0)," +
            "  prezzo_centesimi int not null default (50) check( prezzo_centesimi >= 0 and prezzo_centesimi <100)" +
            ");" +

            "create table LATAZZASCHEMA.visitatore(" +
            "  nome varchar(64) not null," +
            "  primary key(nome)" +
            ");" +


            "create table LATAZZASCHEMA.rifornimento(" +
            "" +
            "  dataR BIGINT default CURRENT_TIMESTAMP not null," +
            "  numero_cialde integer not null,"+
            "  tipo_cialda varchar(64) not null references LATAZZASCHEMA.cialde(tipo) on update cascade on delete restrict," +
            "  primary key (dataR,tipo_cialda)" +
            ");" +

            "create table LATAZZASCHEMA.personale(" +
            "  nome varchar(64) not null," +
            "  euro BIGINT," +
            "  centesimi int," +
            "  primary key (nome)" +
            ");" +

            "create table LATAZZASCHEMA.pagamento_debito(" +
            "" +
            "  nome varchar(64) not null," +
            "  data BIGINT default CURRENT_TIMESTAMP not null," +
            "  euro bigint not null check( euro >= 0)," +
            "  centesimi int not null check(centesimi>=0 and centesimi < 100)," +
            "  primary key (nome, data)," +
            "  foreign key(nome) references LATAZZASCHEMA.personale(nome) on update cascade on delete restrict" +
            ");" +

            "create table LATAZZASCHEMA.compra_visitatore(" +
            "" +
            "  nome varchar(64) not null," +
            "  tipo_cialda varchar(64) not null references LATAZZASCHEMA.cialde(tipo) on update cascade on delete restrict," +
            "  numero_cialde integer not null check (numero_cialde > 0)," +
            "  data BIGINT default CURRENT_TIMESTAMP not null," +
            "  contanti boolean not null," +
            "  primary key(data, nome)," +
            "  foreign key(nome) references LATAZZASCHEMA.visitatore(nome) on update cascade on delete restrict" +
            ");" +

            "create table LATAZZASCHEMA.compra_dipendente(" +
            "" +
            "  nome varchar(64) not null," +
            "  tipo_cialda varchar(64) not null references LATAZZASCHEMA.cialde(tipo) on update cascade on delete restrict," +
            "  numero_cialde integer not null check (numero_cialde > 0)," +
            "  data BIGINT default CURRENT_TIMESTAMP not null," +
            "  contanti boolean not null," +
            "  primary key (data, nome)," +
            "  foreign key (nome) references LATAZZASCHEMA.personale(nome) on update cascade on delete restrict" +
            "" +
            ");" +


            "create table LATAZZASCHEMA.Magazzino" +
            "(" +
            "  tipo varchar(64) not null primary key," +
            "  qta integer not null default(0)" +
            ");" +
            "" +
            "create table LATAZZASCHEMA.Debito(" +
            "" +
            "  nome varchar(64) not null," +
            "  euro bigint not null check( euro >= 0)," +
            "  centesimi int not null check(centesimi>=0 and centesimi < 100)," +
            "  attivo boolean not null," +
            "  primary key (nome)" +
            "" +
            ");" +

            "create table LATAZZASCHEMA.Cassa(" +
            "  euro bigint not null default (500)check( euro >= 0)," +
            "  centesimi int not null default (0) check(centesimi>=0 and centesimi < 100)" +
            ");" +
            "insert into LATAZZASCHEMA.Cassa values ();";
// "SET LOCK_MODE 1;" +
    private static final String inserimentiPresentazione="" +
            "insert into LATAZZASCHEMA.CIALDE values ('thè',0,50 );" +
            "insert into LATAZZASCHEMA.CIALDE values('caffè',0,50 );" +
            "insert into LATAZZASCHEMA.CIALDE values('camomilla',0,50 );" +
            "insert into LATAZZASCHEMA.CIALDE values('caffèArabica',0,50 );" +
            "insert into LATAZZASCHEMA.CIALDE values('cioccolata',0,50 );" +
            "insert into LATAZZASCHEMA.CIALDE values('the limone',0,50 );" +
            "insert into LATAZZASCHEMA.PERSONALE values ( 'Alessia',1600,0 );" +
            "insert into LATAZZASCHEMA.PERSONALE values ( 'Davide',50,0 );" +
            "insert into LATAZZASCHEMA.PERSONALE values ( 'Denise',1250,0 );" +
            "insert into LATAZZASCHEMA.PERSONALE values ( 'Pedro',30,0 );" +
            "insert into LATAZZASCHEMA.PERSONALE values ( 'Daniele',0,0 );" +
            "insert into LATAZZASCHEMA.VISITATORE values ( 'Alessio' );" +
            "insert into LATAZZASCHEMA.RIFORNIMENTO values ('1553644647815', 7,'thè');" +
            "insert into LATAZZASCHEMA.RIFORNIMENTO values ('1553644652506', 10,'thè');" +
            "insert into LATAZZASCHEMA.RIFORNIMENTO values ('1553644658142', 3,'camomilla');" +
            "insert into LATAZZASCHEMA.RIFORNIMENTO values ('1553644662563', 3,'cioccolata');" +
            "insert into LATAZZASCHEMA.RIFORNIMENTO values ('1553644666805', 9,'camomilla');" +
            "insert into LATAZZASCHEMA.RIFORNIMENTO values ('1553706015338', 1,'thè');" +
            "insert into LATAZZASCHEMA.MAGAZZINO values ('thè',18);" +
            "insert into LATAZZASCHEMA.MAGAZZINO values ('camomilla',12);" +
            "insert into LATAZZASCHEMA.MAGAZZINO values ('cioccolata',3);" +
            "commit;"+
            "insert into LATAZZASCHEMA.COMPRA_DIPENDENTE values ('Alessia', 'thè',12,'1553643193039',false);" +

            "insert into LATAZZASCHEMA.COMPRA_DIPENDENTE values ('Denise', 'thè',7,'1553643202342',false);" +
            "insert into LATAZZASCHEMA.COMPRA_DIPENDENTE values ('Alessia', 'thè',8,'1553644110936',true);" +
            "insert into LATAZZASCHEMA.COMPRA_DIPENDENTE values ('Davide', 'thè',15,'1553644125899',true);" +
            "insert into LATAZZASCHEMA.COMPRA_DIPENDENTE values ('Denise', 'thè',20,'1553644140141',false);" +
            "insert into LATAZZASCHEMA.COMPRA_DIPENDENTE values ('Alessia', 'camomilla',800,'1553710827905',false);" +
            "insert into LATAZZASCHEMA.COMPRA_VISITATORE values ('Alessio', 'camomilla',600,'1553710827906',false);" +

            "insert into LATAZZASCHEMA.PAGAMENTO_DEBITO values ('Alessia', '1553710827905',2, 50);";

}