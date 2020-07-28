package ch.konnexions.db_seeder.jdbc.h2;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenH2Schema;

/**
 * Test Data Generator for a H2 Database Engine DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class H2Seeder extends AbstractGenH2Schema {

  private static final Logger logger = Logger.getLogger(H2Seeder.class);

  /**
   * Instantiates a new H2 seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public H2Seeder(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    this.dbmsTickerSymbol = dbmsTickerSymbol;

    init();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Instantiates a new H2 seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   * @param isClient client database version
   */
  public H2Seeder(String dbmsTickerSymbol, boolean isClient) {
    super(dbmsTickerSymbol, isClient);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol + " - isClient=" + isClient);
    }

    this.dbmsTickerSymbol = dbmsTickerSymbol;

    init();

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
    return AbstractGenH2Schema.createTableStmnts.get(tableName);
  }

  /**
   * The common initialisation part.
   */
  private void init() {
    if (isDebug) {
      logger.debug("Start");

      logger.debug("client  =" + isClient);
      logger.debug("embedded=" + isEmbedded);
    }

    dbmsEnum = DbmsEnum.H2;

    driver   = "org.h2.Driver";

    if (isClient) {
      url = config.getConnectionPrefix() + "tcp://" + config.getConnectionHost() + ":" + config.getConnectionPort() + "/" + config.getDatabase();
    } else {
      url = config.getConnectionPrefix() + "file:" + config.getDatabase();
    }

    if (isDebug) {
      logger.debug("End");
    }
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

    connection = connect(url,
                         driver,
                         "sa",
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
               "INFORMATION_SCHEMA.SCHEMATA",
               "schema_name");

    dropUser(userName,
             "",
             "INFORMATION_SCHEMA.USERS",
             "name");

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

    connection = connect(url,
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
