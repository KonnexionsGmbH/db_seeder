/**
 *
 */
package ch.konnexions.db_seeder.jdbc.hsqldb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a HyperQL Database DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class HsqldbSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(AbstractJdbcSeeder.class);

  /**
   * Instantiates a new HyperQL Database seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public HsqldbSeeder(String dbmsTickerSymbol) {
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
   * Instantiates a new HyperQL Database seeder.
   * @param dbmsTickerSymbol 
   *
   * @param isClient client database version
   */
  public HsqldbSeeder(String dbmsTickerSymbol, boolean isClient) {
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
    return HsqldbSchema.databaseTables.get(tableName);
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

    dbms               = Dbms.HSQLDB;

    driver             = "org.hsqldb.jdbc.JDBCDriver";

    tableNameDelimiter = "";

    if (isClient) {
      url = config.getConnectionPrefix() + "hsql://" + config.getConnectionHost() + ":" + config.getConnectionPort() + "/" + config.getDatabase() + config
          .getConnectionSuffix();
    } else {
      url = config.getConnectionPrefix() + "file:" + config.getDatabase() + config.getConnectionSuffix();
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
                         config.getUserSys().toUpperCase(),
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
               "INFORMATION_SCHEMA.SYSTEM_SCHEMAS",
               "table_schem");

    dropUser(userName,
             "",
             "INFORMATION_SCHEMA.SYSTEM_USERS",
             "user_name");

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
