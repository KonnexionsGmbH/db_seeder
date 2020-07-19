/**
 *
 */
package ch.konnexions.db_seeder.jdbc.informix;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for an IBM Informix DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class InformixSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(InformixSeeder.class);

  /**
   * Instantiates a new IBM Informix seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public InformixSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.INFORMIX;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    driver                = "com.informix.jdbc.IfxDriver";

    tableNameDelimiter    = "";

    urlBase               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/";
    url                   = urlBase + config.getDatabase() + config.getConnectionSuffix();
    urlSetup              = urlBase + config.getDatabaseSys() + config.getConnectionSuffix();

    dropTableStmnt        = "SELECT 'DROP TABLE \"' || TABUSER || '\".\"' || TABNAME || '\";' FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABNAME = ? AND TABUSER = ?";

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  @Override protected final String createDdlStmnt(final String tableName) {
    return InformixSchema.databaseTables.get(tableName);
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

    connection = connect(urlSetup,
                         driver,
                         config.getUserSys(),
                         config.getPasswordSys(),
                         true);

    String databaseName = config.getDatabase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP DATABASE IF EXISTS " + databaseName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE DATABASE " + databaseName + " WITH LOG",
                       "GRANT CONNECT TO PUBLIC",
                       "GRANT RESOURCE TO PUBLIC");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(url,
                         null,
                         config.getUserSys(),
                         config.getPasswordSys());

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }
}
