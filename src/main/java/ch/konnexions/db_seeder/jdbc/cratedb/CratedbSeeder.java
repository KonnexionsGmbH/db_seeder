package ch.konnexions.db_seeder.jdbc.cratedb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenCratedbSchema;

/**
 * Test Data Generator for a CrateDB DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class CratedbSeeder extends AbstractGenCratedbSchema {

  private static final Logger logger = Logger.getLogger(CratedbSeeder.class);

  /**
   * Gets the connection URL for privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param connectionSuffix the connection suffix
   * @param userSys the privileged user
   * 
   * @return the connection URL for privileged access
   */
  private final static String getUrlSys(String connectionHost, int connectionPort, String connectionPrefix, String connectionSuffix, String userSys) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/?strict=true&user=" + userSys;
  }

  /**
   * Gets the connection URL for non-privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param connectionSuffix the connection suffix
   * @param user the non-privileged user
   * @param password the non-privileged password
   * 
   * @return the connection URL for non-privileged access
   */
  private final static String getUrlUser(String connectionHost,
                                         int connectionPort,
                                         String connectionPrefix,
                                         String connectionSuffix,
                                         String user,
                                         String password) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/?strict=true&user=" + user + "&password=" + password;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new CrateDB seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public CratedbSeeder(String tickerSymbolExtern) {
    super(tickerSymbolExtern);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern);
    }

    dbmsEnum       = DbmsEnum.CRATEDB;

    dropTableStmnt = "SELECT 'DROP TABLE ' || table_name FROM information_schema.tables WHERE table_schema = 'doc' AND table_name = '?'";

    urlSys         = getUrlSys(config.getConnectionHost(),
                               config.getConnectionPort(),
                               config.getConnectionPrefix(),
                               config.getConnectionSuffix(),
                               config.getUserSys());

    urlUser        = getUrlUser(config.getConnectionHost(),
                                config.getConnectionPort(),
                                config.getConnectionPrefix(),
                                config.getConnectionSuffix(),
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
    return AbstractGenCratedbSchema.createTableStmnts.get(tableName);
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
                         true);

    String userName = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP USER IF EXISTS " + userName);

      dropAllTables(dropTableStmnt);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE USER " + userName + " WITH (PASSWORD = '" + config.getPassword() + "')",
                       "GRANT ALL PRIVILEGES TO " + userName);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(urlUser,
                         true);

    try {
      statement = connection.createStatement();

      createSchema();

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

  }
}
