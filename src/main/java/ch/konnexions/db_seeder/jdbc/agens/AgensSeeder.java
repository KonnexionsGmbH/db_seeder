package ch.konnexions.db_seeder.jdbc.agens;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenAgensSchema;

/**
 * Test Data Generator for a AgensGraph DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class AgensSeeder extends AbstractGenAgensSchema {

  private static final Logger logger = Logger.getLogger(AgensSeeder.class);

  /**
   * Gets the connection URL for privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param databaseSys the database with privileged access
   * @param userSys the user with privileged access
   * @param passwordSys the password with privileged access
   *
   * @return the connection URL for privileged access
   */
  private final static String getUrlSys(String connectionHost,
                                        int connectionPort,
                                        String connectionPrefix,
                                        String databaseSys,
                                        String userSys,
                                        String passwordSys) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + databaseSys + "?user=" + userSys + "&password=" + passwordSys;
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
   * Instantiates a new AgensGraph seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public AgensSeeder(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Instantiates a new AgensGraph seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   * @param dbmsOption client, embedded or presto
   */
  public AgensSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.POSTGRESQL;

    driver   = "net.bitnine.agensgraph.Driver";

    urlSys   = getUrlSys(config.getConnectionHost(),
                         config.getConnectionPort(),
                         config.getConnectionPrefix(),
                         config.getDatabaseSys(),
                         config.getUserSys(),
                         config.getPasswordSys());

    urlUser  = getUrlUser(config.getConnectionHost(),
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
    return AbstractGenAgensSchema.createTableStmnts.get(tableName);
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

      executeDdlStmnts("DROP SCHEMA IF EXISTS " + schemaName + " CASCADE",
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
      executeDdlStmnts("CREATE USER " + userName + " WITH ENCRYPTED PASSWORD '" + config.getPassword() + "'",
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

      executeDdlStmnts("CREATE SCHEMA " + schemaName,
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
