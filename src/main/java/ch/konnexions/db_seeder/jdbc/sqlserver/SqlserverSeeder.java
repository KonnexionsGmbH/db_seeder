package ch.konnexions.db_seeder.jdbc.sqlserver;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenSqlserverSchema;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Data Generator for a SQL Server DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class SqlserverSeeder extends AbstractGenSqlserverSchema {

  private static final Logger logger = LogManager.getLogger(SqlserverSeeder.class);

  /**
   * Gets the connection URL.
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param database         the database
   * @param user             the user
   * @param password         the password
   * @return the connection URL
   */
  private static String getUrl(String connectionHost, int connectionPort, String connectionPrefix, String database, String user, String password) {
    return connectionPrefix + connectionHost + ":" + connectionPort + ";databaseName=" + database + ";user=" + user + ";password=" + password;
  }

  /**
   * Gets the connection URL for trino (used by TrinoEnvironment).
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
   * Instantiates a new SQL Server seeder object.
   *
   * @param tickerSymbolExtern the DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public SqlserverSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.SQLSERVER;

    if (isTrino) {
      urlTrino = AbstractJdbcSeeder.getUrlTrino(tickerSymbolIntern,
                                                config.getConnectionHostTrino(),
                                                config.getConnectionPortTrino(),
                                                "kxn_schema");
    }

    urlSys  = getUrl(config.getConnectionHost(),
                     config.getConnectionPort(),
                     config.getConnectionPrefix(),
                     config.getDatabaseSys(),
                     config.getUserSys(),
                     config.getPasswordSys()) + config.getConnectionSuffix();

    urlUser = getUrl(config.getConnectionHost(),
                     config.getConnectionPort(),
                     config.getConnectionPrefix(),
                     config.getDatabase(),
                     config.getUser(),
                     config.getPassword()) + config.getConnectionSuffix();

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
    return AbstractGenSqlserverSchema.createTableStmnts.get(tableName);
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

    connection = connect(urlSys,
                         true);

    final String databaseName = config.getDatabase();
    final String schemaName   = config.getSchema();
    final String userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeSQLStmnts(statement,
                       "DROP DATABASE IF EXISTS " + databaseName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeSQLStmnts(statement,
                       "sp_configure 'contained database authentication', 1",
                       "RECONFIGURE",
                       "USE master",
                       "CREATE DATABASE " + databaseName,
                       "USE master",
                       "ALTER DATABASE " + databaseName + " SET CONTAINMENT = PARTIAL",
                       "USE " + databaseName,
                       "CREATE SCHEMA " + schemaName,
                       "CREATE USER " + userName + " WITH PASSWORD = '" + config.getPassword() + "', DEFAULT_SCHEMA=" + schemaName,
                       "sp_addrolemember 'db_owner', '" + userName + "'");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    disconnectDDL(connection);

    connection = connect(urlUser);

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
