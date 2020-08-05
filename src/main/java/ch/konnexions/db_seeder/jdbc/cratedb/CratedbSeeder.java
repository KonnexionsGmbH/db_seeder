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
   * Instantiates a new CrateDB seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public CratedbSeeder(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    dbmsEnum              = DbmsEnum.CRATEDB;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    urlBase               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/?strict=true&user=";
    urlUser               = urlBase + config.getUser() + "&password=" + config.getPassword();
    urlSys                = urlBase + config.getUserSys();

    dropTableStmnt        = "SELECT 'DROP TABLE ' || table_name FROM information_schema.tables WHERE table_schema = 'doc' AND table_name = '?'";

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
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(urlUser,
                         true);

    if (isDebug) {
      logger.debug("End");
    }
  }
}
