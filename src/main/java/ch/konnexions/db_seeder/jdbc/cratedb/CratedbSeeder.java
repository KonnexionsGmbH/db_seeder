/**
 *
 */
package ch.konnexions.db_seeder.jdbc.cratedb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a CrateDB DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class CratedbSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(CratedbSeeder.class);

  /**
   * Instantiates a new CrateDB seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public CratedbSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.CRATEDB;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    tableNameDelimiter    = "";

    urlBase               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/?strict=true&user=";
    url                   = urlBase + config.getUser() + "&password=" + config.getPassword();
    urlSetup              = urlBase + config.getUserSys();

    dropTableStmnt        = "SELECT 'DROP TABLE ' || table_name FROM information_schema.tables WHERE table_schema = 'doc' AND table_name = '?'";

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  @Override protected final String createDdlStmnt(final String tableName) {
    return CratedbSchema.databaseTables.get(tableName);
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
                         true);

    String userName = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP USER IF EXISTS " + userName);

      dropAllTables(dropTableStmnt);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE USER " + userName + " WITH (PASSWORD = '" + config.getPassword() + "')",
                       "GRANT ALL PRIVILEGES TO " + userName);

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
                         true);

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }
}
