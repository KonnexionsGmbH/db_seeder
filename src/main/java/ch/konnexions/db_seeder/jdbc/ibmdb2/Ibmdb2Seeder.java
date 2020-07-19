/**
 *
 */
package ch.konnexions.db_seeder.jdbc.ibmdb2;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for an IBM Db2 DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class Ibmdb2Seeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(Ibmdb2Seeder.class);

  /**
   * Instantiates a new IBM Db2 Database seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public Ibmdb2Seeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.IBMDB2;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    tableNameDelimiter    = "";

    url                   = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/" + config.getDatabase();

    dropTableStmnt        = "SELECT 'DROP TABLE \"' || TABSCHEMA || '\".\"' || TABNAME || '\";' FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABSCHEMA = 'schemaName' AND TABNAME = '?'";

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  @Override protected final String createDdlStmnt(final String tableName) {
    return Ibmdb2Schema.databaseTables.get(tableName);
  }

  @Override protected final void setupDatabase() {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(url,
                         null,
                         config.getUserSys(),
                         config.getPasswordSys());

    String schemaName = config.getSchema().toUpperCase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropAllTables(dropTableStmnt.replace("schemaName",
                                         schemaName));

    dropSchema(schemaName,
               "RESTRICT",
               "SYSIBM.SYSSCHEMATA",
               "name");

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE SCHEMA " + schemaName,
                       "SET CURRENT SCHEMA " + schemaName + ";");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }
}
