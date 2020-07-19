/**
 *
 */
package ch.konnexions.db_seeder.jdbc.oracle;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for an Oracle DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class OracleSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(OracleSeeder.class);

  /**
   * Instantiates a new Oracle Database seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public OracleSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.ORACLE;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    tableNameDelimiter    = "";

    url                   = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/" + config.getConnectionService();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  @Override protected final String createDdlStmnt(final String tableName) {
    return OracleSchema.databaseTables.get(tableName);
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
                         config.getUserSys().toUpperCase(),
                         config.getPasswordSys());

    String userName = config.getUser().toUpperCase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropUser(userName,
             "CASCADE",
             "ALL_USERS",
             "username");

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE USER " + userName + " IDENTIFIED BY \"" + config.getPassword() + "\"",
                       "ALTER USER " + userName + " QUOTA UNLIMITED ON users",
                       "GRANT CREATE SEQUENCE TO " + userName,
                       "GRANT CREATE SESSION TO " + userName,
                       "GRANT CREATE TABLE TO " + userName,
                       "GRANT UNLIMITED TABLESPACE TO " + userName);

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
