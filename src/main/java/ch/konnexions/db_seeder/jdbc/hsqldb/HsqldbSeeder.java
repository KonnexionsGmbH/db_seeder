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

  private static final Logger logger  = Logger.getLogger(HsqldbSeeder.class);
  private final boolean       isDebug = logger.isDebugEnabled();

  /**
   * Initialises a new HyperSQL seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public HsqldbSeeder(String dbmsTickerSymbol) {
    this(dbmsTickerSymbol, "client");
  }

  /**
   * Initialises a new HyperSQL seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   * @param dbmsOption client, embedded or presto
   */
  public HsqldbSeeder(String dbmsTickerSymbol, String dbmsOption) {
    super(dbmsTickerSymbol, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol + " - dbmsOption=" + dbmsOption);
    }

    this.dbmsTickerSymbol = dbmsTickerSymbol;

    dbmsEnum              = DbmsEnum.HSQLDB;

    driver                = "org.hsqldb.jdbc.JDBCDriver";

    if (isClient) {
      urlUser = config.getConnectionPrefix() + "hsql://" + config.getConnectionHost() + ":" + config.getConnectionPort() + "/" + config.getDatabase() + config
          .getConnectionSuffix();
    } else {
      urlUser = config.getConnectionPrefix() + "file:" + config.getDatabase() + config.getConnectionSuffix();
    }

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
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(urlUser,
                         null,
                         userName,
                         password);

    try {
      statement = connection.createStatement();

      executeDdlStmnts("SET SCHEMA " + schemaName);

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
