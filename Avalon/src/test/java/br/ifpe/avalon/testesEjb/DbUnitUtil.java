package br.ifpe.avalon.testesEjb;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;

public class DbUnitUtil {

    //private static final String XML_FILE = "/home/danilop/Documentos/corporativo2/Avalon/src/main/resources/dbunit/dataset.xml";
    private static final String XML_FILE = "/home/aneto/Documents/IFPE/corporativo2/Avalon/src/main/resources/dbunit/dataset.xml";
    //private static final String XML_FILE = "/home/aldenio/NetBeansProjects/corporativo2/Avalon/src/main/resources/dbunit/dataset.xml";
    //private static final String XML_FILE = "/Users/eduardoamaral/NetBeansProjects/corporativo2/Avalon/src/main/resources/dbunit/dataset.xml";
    
    @SuppressWarnings("UseSpecificCatch")
    public static void inserirDados() {
        Connection conn = null;
        IDatabaseConnection db_conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/avalondb", "root", "root");
            db_conn = new DatabaseConnection(conn, "avalondb");
            DatabaseConfig dbConfig = db_conn.getConfig();
            dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
            dbConfig.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
            FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            builder.setColumnSensing(true);
            InputStream in = new FileInputStream(new File(XML_FILE));
            IDataSet dataSet = builder.build(in);
            DatabaseOperation.CLEAN_INSERT.execute(db_conn, dataSet);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }

                if (db_conn != null) {
                    db_conn.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
    }

}
