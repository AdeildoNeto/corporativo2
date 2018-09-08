package br.edu.ifpe.recife.avalon.cucumber.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;

public class DbUnitUtil {

    private static String xml_file = "src/main/resources/dbunit/dataset.xml";
    private static boolean cucumberTest = false;

    public static void setDataSet(DataSetEnum dataSet) {
        xml_file = dataSet.getSource();
        cucumberTest = true;
    }

    @SuppressWarnings("UseSpecificCatch")
    public static void inserirDados() {
        Connection conn = null;
        IDatabaseConnection db_conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/avalondb", "root", "root");
            db_conn = new DatabaseConnection(conn, "avalondb");
            if (cucumberTest) {
                limparBase(conn);
            }
            
            DatabaseConfig dbConfig = db_conn.getConfig();
            dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
            dbConfig.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
            FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            builder.setColumnSensing(true);
            InputStream in = new FileInputStream(new File(xml_file));
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

    private static void limparBase(Connection connection) throws SQLException {

        Statement stmt = connection.createStatement();

        try {

            String sql;

            sql = "DELETE FROM TB_SIMULADO_ALUNO_QUESTAO";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_SIMULADO_ALUNO";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_QUESTAO_SIMULADO";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_SIMULADO";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_PROVA_ALUNO_QUESTAO";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_PROVA_ALUNO";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_QUESTAO_PROVA";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_PROVA";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_ALTERNATIVA";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_MULTIPLA_ESCOLHA";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_VERDADEIRO_FALSO";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_QUESTAO";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM TB_COMPONENTE_CURRICULAR";
            stmt.executeUpdate(sql);

        } finally {
            stmt.close();
        }

    }

}
