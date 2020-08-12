package ch.konnexions.db_seeder.jdbc.hsqldb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenHsqldbSchema;

/**
 * Test Data Generator for a HyperQL Database DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class HsqldbSeeder extends AbstractGenHsqldbSchema {

  private static final Logger logger = Logger.getLogger(HsqldbSeeder.class);

  /**
   * Gets the connection URL for non-privileged access.
   *
   * @param isClient database client version 
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param connectionSuffix the connection suffix
   * @param database the database with non-privileged access
   *
   * @return the connection URL for non-privileged access
   */
  private final static String getUrlUser(boolean isClient,
                                         String connectionHost,
                                         int connectionPort,
                                         String connectionPrefix,
                                         String connectionSuffix,
                                         String database) {
    if (isClient) {
      return connectionPrefix + "hsql://" + connectionHost + ":" + connectionPort + "/" + database + connectionSuffix;
    } else {
      return connectionPrefix + "file:" + database + connectionSuffix;
    }
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Initialises a new HyperSQL seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public HsqldbSeeder(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Initialises a new HyperSQL seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   * @param dbmsOption client, embedded or presto
   */
  public HsqldbSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.HSQLDB;

    driver   = "org.hsqldb.jdbc.JDBCDriver";

    urlUser  = getUrlUser(isClient,
                          config.getConnectionHost(),
                          config.getConnectionPort(),
                          config.getConnectionPrefix(),
                          config.getConnectionSuffix(),
                          config.getDatabase());

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
    return AbstractGenHsqldbSchema.createTableStmnts.get(tableName);
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
                         config.getUserSys().toUpperCase(),
                         "",
                         true);

    String password   = config.getPassword();
    String schemaName = config.getSchema().toUpperCase();
    String userName   = config.getUser().toUpperCase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropSchema(schemaName,
               "CASCADE",
               "INFORMATION_SCHEMA.SYSTEM_SCHEMAS",
               "table_schem");

    dropUser(userName,
             "",
             "INFORMATION_SCHEMA.SYSTEM_USERS",
             "user_name");

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE USER " + userName + " PASSWORD '" + password + "' ADMIN",
                       "CREATE SCHEMA " + schemaName + " AUTHORIZATION " + userName);

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
                         password);

    try {
      statement = connection.createStatement();

      executeDdlStmnts("SET SCHEMA " + schemaName);

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
