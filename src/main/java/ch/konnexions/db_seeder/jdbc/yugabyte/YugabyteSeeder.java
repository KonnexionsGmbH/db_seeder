package ch.konnexions.db_seeder.jdbc.yugabyte;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenYugabyteSchema;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a YugabyteDB DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class YugabyteSeeder extends AbstractGenYugabyteSchema {

  private static final Logger logger = Logger.getLogger(YugabyteSeeder.class);

  /**
   * Gets the connection URL for privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param databaseSys the database with privileged access
   * @param userSys the user with privileged access
   *
   * @return the connection URL for privileged access
   */
  private final static String getUrlSys(String connectionHost, int connectionPort, String connectionPrefix, String databaseSys, String userSys) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + databaseSys + "?user=" + userSys;
  }

  /**
   * Gets the connection URL for non-privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param database the database with non-privileged access
   * @param user the user with non-privileged access
   * @param password the password with non-privileged access
   *
   * @return the connection URL for non-privileged access
   */
  private final static String getUrlUser(String connectionHost, int connectionPort, String connectionPrefix, String database, String user, String password) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database + "?user=" + user + "&password=" + password;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new YugabyteDB seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public YugabyteSeeder(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Instantiates a new YugabyteDB seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   * @param dbmsOption client, embedded or presto
   */
  private YugabyteSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.YUGABYTE;

    if (isPresto) {
      urlPresto = AbstractJdbcSeeder.getUrlPresto(tickerSymbolLower,
                                                  config.getConnectionHostPresto(),
                                                  config.getConnectionPortPresto(),
                                                  config.getSchema());
    }

    urlSys  = getUrlSys(config.getConnectionHost(),
                        config.getConnectionPort(),
                        config.getConnectionPrefix(),
                        config.getDatabaseSys(),
                        config.getUserSys());

    urlUser = getUrlUser(config.getConnectionHost(),
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
   *
   * @return the 'CREATE TABLE' statement
   */
  @Override
  protected final String createDdlStmnt(String tableName) {
    return AbstractGenYugabyteSchema.createTableStmnts.get(tableName);
  }

  @Override
  protected final void setupDatabase() {
    if (isDebug) {
      logger.debug("Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSys,
                         true);

    String databaseName = config.getDatabase();
    String schemaName   = config.getSchema();
    String userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts(statement,
                       "DROP SCHEMA IF EXISTS " + schemaName + " CASCADE",
                       "DROP DATABASE IF EXISTS " + databaseName,
                       "DROP USER IF EXISTS " + userName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts(statement,
                       "CREATE USER " + userName + " WITH ENCRYPTED PASSWORD '" + config.getPassword() + "'",
                       "CREATE DATABASE " + databaseName + " WITH OWNER " + userName,
                       "GRANT ALL PRIVILEGES ON DATABASE " + databaseName + " TO " + userName);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(urlUser);

    try {
      statement = connection.createStatement();

      executeDdlStmnts(statement,
                       "CREATE SCHEMA " + schemaName,
                       "SET search_path = " + schemaName);

      createSchema();

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

}
