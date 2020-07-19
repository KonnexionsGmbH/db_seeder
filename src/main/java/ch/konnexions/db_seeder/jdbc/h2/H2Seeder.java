/**
 *
 */
package ch.konnexions.db_seeder.jdbc.h2;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a H2 Database Engine DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class H2Seeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(AbstractJdbcSeeder.class);

  /**
   * Instantiates a new H2 Database Engine seeder.
   * @param dbmsTickerSymbol 
   */
  public H2Seeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    this.dbmsTickerSymbol = dbmsTickerSymbol;

    init();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  /**
   * Instantiates a new H2 Database Engine seeder.
   * @param dbmsTickerSymbol 
   *
   * @param isClient client database version
   */
  public H2Seeder(String dbmsTickerSymbol, boolean isClient) {
    super(isClient);

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    this.dbmsTickerSymbol = dbmsTickerSymbol;

    init();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  @Override protected final String createDdlStmnt(final String tableName) {
    return H2Schema.databaseTables.get(tableName);
  }

  private final void init() {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- client  =" + isClient);
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- embedded=" + isEmbedded);
    }

    dbms               = Dbms.H2;

    driver             = "org.h2.Driver";

    tableNameDelimiter = "";

    if (isClient) {
      url = config.getConnectionPrefix() + "tcp://" + config.getConnectionHost() + ":" + config.getConnectionPort() + "/" + config.getDatabase();
    } else {
      url = config.getConnectionPrefix() + "file:" + config.getDatabase();
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
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
                         driver,
                         "sa",
                         "",
                         true);

    String password   = config.getPassword();
    String schemaName = config.getSchema().toUpperCase();
    String userName   = config.getUser().toUpperCase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropSchema(schemaName,
               "CASCADE",
               "INFORMATION_SCHEMA.SCHEMATA",
               "schema_name");

    dropUser(userName,
             "",
             "INFORMATION_SCHEMA.USERS",
             "name");

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE USER " + userName + " PASSWORD '" + password + "' ADMIN",
                       "CREATE SCHEMA " + schemaName + " AUTHORIZATION " + userName);

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
                         password);

    try {
      statement = connection.createStatement();

      executeDdlStmnts("SET SCHEMA " + schemaName);

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
