/**
 *
 */
package ch.konnexions.db_seeder.jdbc.cubrid;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a CUBRID DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class CubridSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(CubridSeeder.class);

  /**
   * Instantiates a new CUBRID seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public CubridSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.CUBRID;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    driver                = "cubrid.jdbc.driver.CUBRIDDriver";

    tableNameDelimiter    = "\"";

    urlBase               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + ":" + config.getDatabase() + ":";
    url                   = urlBase + config.getConnectionSuffix();
    urlSetup              = urlBase + config.getUserSys().toUpperCase() + config.getConnectionSuffix();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  @Override protected final String createDdlStmnt(final String tableName) {
    return CubridSchema.databaseTables.get(tableName);
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
                         driver);

    String userName = config.getUser().toUpperCase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      dropAllTablesIfExists();

      dropUser(userName,
               "",
               "db_user",
               "name");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE USER " + userName + " PASSWORD '" + config.getPassword() + "' GROUPS dba");

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
                         userName,
                         config.getPassword());

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }
}
