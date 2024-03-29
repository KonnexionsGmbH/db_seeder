package ch.konnexions.db_seeder.jdbc.heavy;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenHeavySchema;

/**
 * Data Generator for an HeavyDB DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2021-06-06
 */
public final class HeavySeeder extends AbstractGenHeavySchema {

  private static final Logger logger = LogManager.getLogger(ch.konnexions.db_seeder.jdbc.heavy.HeavySeeder.class);

  /**
   * Gets the connection URL.
   *
   * @param connectionHost    the connection host name
   * @param connectionPort    the connection port number
   * @param connectionPrefix  the connection prefix
   * @param database          the database
   * @return the connection URL
   */
  private static String getUrl(String connectionHost, int connectionPort, String connectionPrefix, String database) {
    return connectionPrefix + connectionHost + ":" + connectionPort + ":" + database;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new HeavyDB seeder object.
   *
   * @param tickerSymbolExtern the DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public HeavySeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.HEAVY;

    driver   = "com.omnisci.jdbc.omnisciDriver";

    urlSys   = getUrl(config.getConnectionHost(),
                      config.getConnectionPort(),
                      config.getConnectionPrefix(),
                      config.getDatabaseSys());

    urlUser  = getUrl(config.getConnectionHost(),
                      config.getConnectionPort(),
                      config.getConnectionPrefix(),
                      config.getDatabase());

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
    return AbstractGenHeavySchema.createTableStmnts.get(tableName);
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
                         driver,
                         config.getUserSys().toLowerCase(),
                         config.getPasswordSys());

    // -----------------------------------------------------------------------
    // Tear down an existing database.
    // -----------------------------------------------------------------------

    String databaseName = config.getDatabase();
    String userName     = config.getUser();

    try {
      statement = connection.createStatement();

      executeSQLStmnts(statement,
                       "DROP DATABASE IF EXISTS " + databaseName,
                       "DROP USER IF EXISTS " + userName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database and user.
    // -----------------------------------------------------------------------

    String password = config.getPassword();

    try {
      executeSQLStmnts(statement,
                       "CREATE DATABASE " + databaseName,
                       "CREATE USER " + userName + "(password = '" + password + "', is_super = 'true', default_db = '" + databaseName + "')");

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
                         driver,
                         userName,
                         password);

    try {
      statement = connection.createStatement();

      createSchema(connection);

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
