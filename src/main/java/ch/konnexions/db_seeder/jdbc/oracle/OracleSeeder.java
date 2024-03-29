package ch.konnexions.db_seeder.jdbc.oracle;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenOracleSchema;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Data Generator for an Oracle DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class OracleSeeder extends AbstractGenOracleSchema {

  private static final Logger logger = LogManager.getLogger(OracleSeeder.class);

  /**
   * Gets the connection URL.
   *
   * @param connectionHost    the connection host name
   * @param connectionPort    the connection port number
   * @param connectionPrefix  the connection prefix
   * @param connectionService the connection service
   * @return the connection URL
   */
  private static String getUrl(String connectionHost, int connectionPort, String connectionPrefix, String connectionService) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + connectionService + "?oracle.net.disableOob=true";
  }

  /**
   * Gets the connection URL for trino (used by TrinoEnvironment).
   *
   * @param connectionHost    the connection host name
   * @param connectionPort    the connection port number
   * @param connectionPrefix  the connection prefix
   * @param connectionService the connection service
   * @return the connection URL for non-privileged access
   */
  public static String getUrlTrino(String connectionHost, int connectionPort, String connectionPrefix, String connectionService) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + connectionService + "?oracle.net.disableOob=true";
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new Oracle seeder object.
   *
   * @param tickerSymbolExtern the DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public OracleSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.ORACLE;

    if (isTrino) {
      urlTrino = AbstractJdbcSeeder.getUrlTrino(tickerSymbolIntern,
                                                config.getConnectionHostTrino(),
                                                config.getConnectionPortTrino(),
                                                config.getUser());
    }

    urlUser = getUrl(config.getConnectionHost(),
                     config.getConnectionPort(),
                     config.getConnectionPrefix(),
                     config.getConnectionService());

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Create the DDL statement: CREATE TABLE.
   *
   * @param tableName the database table name
   * @return the 'CREATE TABLE' statement
   */
  @Override
  protected String createDdlStmnt(String tableName) {
    return AbstractGenOracleSchema.createTableStmnts.get(tableName);
  }

  /**
   * Delete any existing relevant database schema objects (database, user,
   * schema or valTableNames)and initialise the database for a new run.
   */
  @Override
  protected void setupDatabase() {
    if (isDebug) {
      logger.debug("Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlUser,
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
      executeSQLStmnts(statement,
                       "CREATE USER " + userName + " IDENTIFIED BY \"" + config.getPassword() + "\"",
                       "ALTER USER " + userName + " QUOTA UNLIMITED ON users",
                       "GRANT CREATE SESSION TO " + userName,
                       "GRANT CREATE TABLE TO " + userName,
                       "GRANT UNLIMITED TABLESPACE TO " + userName);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    disconnectDDL(connection);

    connection = connect(urlUser,
                         null,
                         userName,
                         config.getPassword());

    try {
      statement = connection.createStatement();

      createSchema(connection);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect - trino.
    // -----------------------------------------------------------------------

    if (isTrino) {
      disconnectDDL(connection);

      connection = connect(urlTrino,
                           driver_trino,
                           true);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }
}
