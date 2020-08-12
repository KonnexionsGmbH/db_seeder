package ch.konnexions.db_seeder.jdbc.mimer;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenMimerSchema;

/**
 * Test Data Generator for a Mimer SQL DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class MimerSeeder extends AbstractGenMimerSchema {

  private static final Logger logger = Logger.getLogger(MimerSeeder.class);

  /**
   * Gets the connection URL for non-privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param databaseSys the database with privileged access
   *
   * @return the connection URL for non-privileged access
   */
  private final static String getUrlUser(String connectionHost, int connectionPort, String connectionPrefix, String databaseSys) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + databaseSys;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new Mimer SQL seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public MimerSeeder(String tickerSymbolExtern) {
    super(tickerSymbolExtern);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsEnum = DbmsEnum.MIMER;

    driver   = "com.mimer.jdbc.Driver";

    urlUser  = getUrlUser(config.getConnectionHost(),
                          config.getConnectionPort(),
                          config.getConnectionPrefix(),
                          config.getDatabaseSys());

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
    return AbstractGenMimerSchema.createTableStmnts.get(tableName);
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

    connection = connect(urlUser,
                         driver,
                         config.getUserSys(),
                         config.getPasswordSys(),
                         true);

    String databaseName = config.getDatabase();
    String userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropDatabase(databaseName,
                 "CASCADE",
                 "SYSTEM.DATABANKS",
                 "databank_name");

    dropUser(userName,
             "CASCADE",
             "SYSTEM.USERS",
             "user_name");

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE DATABANK " + databaseName + " SET OPTION TRANSACTION",
                       "CREATE IDENT " + userName + " AS USER USING '" + config.getPassword() + "'",
                       "GRANT TABLE ON DATABANK " + databaseName + " TO " + userName);

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
                         null,
                         userName,
                         config.getPassword(),
                         true);

    try {
      statement = connection.createStatement();

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
