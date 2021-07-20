package ch.konnexions.db_seeder.jdbc.omnisci;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenOmnisciSchema;

/**
 * Data Generator for an OmniSci DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2021-06-06
 */
public final class OmnisciSeeder extends AbstractGenOmnisciSchema {

  private static final Logger logger = LogManager.getLogger(OmnisciSeeder.class);

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
   * Instantiates a new OmniSci seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   */
  public OmnisciSeeder(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Instantiates a new OmniSci seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public OmnisciSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.OMNISCI;

    driver   = "com.omnisci.jdbc.OmniSciDriver";

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
  protected final String createDdlStmnt(String tableName) {
    return AbstractGenOmnisciSchema.createTableStmnts.get(tableName);
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

      // ToDo wwe      
      //      executeDdlStmnts(statement,
      //                       "DROP DATABASE IF EXISTS " + databaseName,
      //                       "DROP USER " + userName);
      executeDdlStmnts(statement,
                       "DROP DATABASE IF EXISTS " + databaseName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database and user.
    // -----------------------------------------------------------------------

    String password = config.getPassword();

    try {
      executeDdlStmnts(statement,
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
