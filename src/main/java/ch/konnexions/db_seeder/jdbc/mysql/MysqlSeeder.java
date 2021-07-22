package ch.konnexions.db_seeder.jdbc.mysql;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenMysqlSchema;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Data Generator for a MySQL DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class MysqlSeeder extends AbstractGenMysqlSchema {

  private static final Logger logger = LogManager.getLogger(MysqlSeeder.class);

  /**
   * Gets the connection URL.
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param connectionSuffix the connection suffix
   * @param database         the database
   * @param user             the user
   * @param password         the password
   * @return the connection URL
   */
  private static String getUrl(String connectionHost,
                               int connectionPort,
                               String connectionPrefix,
                               String connectionSuffix,
                               String database,
                               String user,
                               String password) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database + "?user=" + user + "&password=" + password + connectionSuffix;
  }

  /**
   * Gets the connection URL for trino (used by TrinoEnvironment).
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param connectionSuffix the connection suffix
   * @return the connection URL for non-privileged access
   */
  public static String getUrlTrino(String connectionHost, int connectionPort, String connectionPrefix, String connectionSuffix) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + connectionSuffix;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new MySQL seeder object.
   *
   * @param tickerSymbol the DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public MysqlSeeder(String tickerSymbol, String dbmsOption) {
    super(tickerSymbol, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbol=" + tickerSymbol + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.MYSQL;

    driver   = "com.mysql.cj.jdbc.Driver";

    if (isTrino) {
      urlTrino = AbstractJdbcSeeder.getUrlTrino(tickerSymbol,
                                                config.getConnectionHostTrino(),
                                                config.getConnectionPortTrino(),
                                                config.getDatabase());
    }

    urlSys  = getUrl(config.getConnectionHost(),
                     config.getConnectionPort(),
                     config.getConnectionPrefix(),
                     config.getConnectionSuffix(),
                     config.getDatabaseSys(),
                     config.getUserSys(),
                     config.getPasswordSys());

    urlUser = getUrl(config.getConnectionHost(),
                     config.getConnectionPort(),
                     config.getConnectionPrefix(),
                     config.getConnectionSuffix(),
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
    return AbstractGenMysqlSchema.createTableStmnts.get(tableName);
  }

  /**
   * Delete any existing relevant database schema objects (database, user,
   * schema or valTableNames)and initialise the database for a new run.
   */
  @Override
  protected final void setupDatabase() {
    if (isDebug) {
      logger.debug("Start");
    }

    connection = setupMysql(driver,
                            urlSys,
                            urlUser);

    // -----------------------------------------------------------------------
    // Disconnect and reconnect - trino.
    // -----------------------------------------------------------------------

    if (isTrino) {
      disconnectDDL(connection);

      connection = connect(urlTrino,
                           driver_trino,
                           true);

      try {
        statement = connection.createStatement();

        executeDdlStmnts(statement,
                         "USE " + getCatalogName(tickerSymbol) + "." + config.getDatabase());

        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    if (isDebug) {
      logger.debug("End");
    }
  }
}
