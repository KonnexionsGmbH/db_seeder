package ch.konnexions.db_seeder.jdbc.cubrid;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenCubridSchema;

/**
 * Test Data Generator for a CUBRID DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class CubridSeeder extends AbstractGenCubridSchema {

  private static final Logger logger = Logger.getLogger(CubridSeeder.class);

  /**
   * Gets the connection URL for privileged access.
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param database         the database
   * @param userSys          the user with privileged access
   * @return the connection URL for privileged access
   */
  private static String getUrlSys(String connectionHost, int connectionPort, String connectionPrefix, String database, String userSys) {
    return connectionPrefix + connectionHost + ":" + connectionPort + ":" + database + ":" + userSys.toUpperCase() + "::";
  }

  /**
   * Gets the connection URL for non-privileged access.
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param database         the database
   * @param user             the user with non-privileged access
   * @param password         the password with non-privileged access
   * @return the connection URL for non-privileged access
   */
  private static String getUrlUser(String connectionHost, int connectionPort, String connectionPrefix, String database, String user, String password) {
    return connectionPrefix + connectionHost + ":" + connectionPort + ":" + database + ":" + user + ":" + password + ":";
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new CUBRID seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   */
  public CubridSeeder(String tickerSymbolExtern) {
    super(tickerSymbolExtern);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsEnum = DbmsEnum.CUBRID;

    driver   = "cubrid.jdbc.driver.CUBRIDDriver";

    urlSys   = getUrlSys(config.getConnectionHost(),
                         config.getConnectionPort(),
                         config.getConnectionPrefix(),
                         config.getDatabase(),
                         config.getUserSys());

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
   * @return the 'CREATE TABLE' statement
   */
  @Override
  protected final String createDdlStmnt(String tableName) {
    return AbstractGenCubridSchema.createTableStmnts.get(tableName);
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
                         driver);

    String userName = config.getUser().toUpperCase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      dropAllTablesIfExists();

      dropUser(userName,
               "",
               "db_user",
               "name");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts(statement,
                       "CREATE USER " + userName + " PASSWORD '" + config.getPassword() + "' GROUPS dba");

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
