package ch.konnexions.db_seeder.jdbc.postgresql;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenPostgresqlSchema;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a PostgreSQL DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class PostgresqlSeeder extends AbstractGenPostgresqlSchema {

  private static final Logger logger = Logger.getLogger(PostgresqlSeeder.class);

  /**
   * Gets the connection URL.
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param database         the database with non-privileged access
   * @param user             the user
   * @param password         the password
   * @return the connection URL
   */
  private static String getUrl(String connectionHost, int connectionPort, String connectionPrefix, String database, String user, String password) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database + "?user=" + user + "&password=" + password;
  }

  /**
   * Gets the connection URL for Trino (used by TrinoEnvironment).
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param database         the database with non-privileged access
   * @param user             the user with non-privileged access
   * @param password         the password with non-privileged access
   * @return the connection URL for non-privileged access
   */
  public static String getUrlTrino(String connectionHost, int connectionPort, String connectionPrefix, String database, String user, String password) {
    return getUrl(connectionHost,
                  connectionPort,
                  connectionPrefix,
                  database,
                  user,
                  password);
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new PostgreSQL seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   */
  public PostgresqlSeeder(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Instantiates a new PostgreSQL seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public PostgresqlSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.POSTGRESQL;

    if (isTrino) {
      urlTrino = AbstractJdbcSeeder.getUrlTrino(tickerSymbolLower,
                                                  config.getConnectionHostTrino(),
                                                  config.getConnectionPortTrino(),
                                                  config.getSchema());
    }

    urlSys  = getUrl(config.getConnectionHost(),
                     config.getConnectionPort(),
                     config.getConnectionPrefix(),
                     config.getDatabaseSys(),
                     config.getUserSys(),
                     config.getPasswordSys());

    urlUser = getUrl(config.getConnectionHost(),
                     config.getConnectionPort(),
                     config.getConnectionPrefix(),
                     config.getDatabase(),
                     config.getUser(),
                     config.getPassword());

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
  protected final String createDdlStmnt(String tableName) {
    return AbstractGenPostgresqlSchema.createTableStmnts.get(tableName);
  }

  @Override
  protected final void setupDatabase() {
    if (isDebug) {
      logger.debug("Start");
    }

    connection = setupPostgresql(driver,
                                 urlSys,
                                 urlUser);

    // -----------------------------------------------------------------------
    // Disconnect and reconnect - Trino.
    // -----------------------------------------------------------------------

    if (isTrino) {
      disconnect(connection);

      connection = connect(urlTrino,
                           driver_trino,
                           true);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

}
