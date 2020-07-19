/**
 *
 */
package ch.konnexions.db_seeder.jdbc.mariadb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a MariaDB DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class MariadbSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(MariadbSeeder.class);

  /**
   * Instantiates a new MariaDB Server seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public MariadbSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.MARIADB;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    tableNameDelimiter    = "`";

    urlBase               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/";
    url                   = urlBase + config.getDatabase();
    urlSetup              = urlBase + config.getDatabaseSys();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  @Override protected final String createDdlStmnt(final String tableName) {
    return MariadbSchema.databaseTables.get(tableName);
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
                         null,
                         config.getUserSys(),
                         config.getPasswordSys());

    String databaseName = config.getDatabase();
    String userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP DATABASE IF EXISTS `" + databaseName + "`",
                       "DROP USER IF EXISTS '" + userName + "'");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE DATABASE `" + databaseName + "`",
                       "USE `" + databaseName + "`",
                       "CREATE USER '" + userName + "'@'%' IDENTIFIED BY '" + config.getPassword() + "'",
                       "GRANT ALL PRIVILEGES ON *.* TO '" + userName + "'@'%'",
                       "FLUSH PRIVILEGES");

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
