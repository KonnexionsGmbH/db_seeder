package ch.konnexions.db_seeder.jdbc.informix;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenInformixSchema;

/**
 * Data Generator for an IBM Informix DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class InformixSeeder extends AbstractGenInformixSchema {

  private static final Logger logger = LogManager.getLogger(InformixSeeder.class);

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
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database + ":INFORMIXSERVER=informix;user=" + user + ";password=" + password;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new IBM Informix seeder object.
   *
   * @param tickerSymbolExtern the DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public InformixSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum       = DbmsEnum.INFORMIX;

    driver         = "com.informix.jdbc.IfxDriver";

    dropTableStmnt = "SELECT 'DROP TABLE \"' || TABUSER || '\".\"' || TABNAME || '\";' FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABNAME = ? AND TABUSER = ?";

    urlSys         = getUrl(config.getConnectionHost(),
                            config.getConnectionPort(),
                            config.getConnectionPrefix(),
                            config.getDatabaseSys(),
                            config.getUserSys(),
                            config.getPasswordSys());

    urlUser        = getUrl(config.getConnectionHost(),
                            config.getConnectionPort(),
                            config.getConnectionPrefix(),
                            config.getDatabase(),
                            config.getUserSys(),
                            config.getPasswordSys());

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
    return AbstractGenInformixSchema.createTableStmnts.get(tableName);
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
                         true);

    String databaseName = config.getDatabase();

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
                       "CREATE DATABASE " + databaseName + " WITH LOG",
                       "GRANT CONNECT TO PUBLIC",
                       "GRANT RESOURCE TO PUBLIC");

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

    if (isDebug) {
      logger.debug("End");
    }
  }
}
