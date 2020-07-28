package ch.konnexions.db_seeder.jdbc.informix;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenInformixSchema;

/**
 * Test Data Generator for an IBM Informix DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class InformixSeeder extends AbstractGenInformixSchema {

  private static final Logger logger = Logger.getLogger(InformixSeeder.class);

  /**
   * Instantiates a new IBM Informix seeder object.
   * 
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public InformixSeeder(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsEnum              = DbmsEnum.INFORMIX;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    driver                = "com.informix.jdbc.IfxDriver";

    urlBase               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/";
    url                   = urlBase + config.getDatabase() + config.getConnectionSuffix();
    urlSetup              = urlBase + config.getDatabaseSys() + config.getConnectionSuffix();

    dropTableStmnt        = "SELECT 'DROP TABLE \"' || TABUSER || '\".\"' || TABNAME || '\";' FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABNAME = ? AND TABUSER = ?";

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
    return AbstractGenInformixSchema.createTableStmnts.get(tableName);
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

    connection = connect(urlSetup,
                         driver,
                         config.getUserSys(),
                         config.getPasswordSys(),
                         true);

    String databaseName = config.getDatabase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP DATABASE IF EXISTS " + databaseName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE DATABASE " + databaseName + " WITH LOG",
                       "GRANT CONNECT TO PUBLIC",
                       "GRANT RESOURCE TO PUBLIC");

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
                         config.getUserSys(),
                         config.getPasswordSys());

    if (isDebug) {
      logger.debug("End");
    }
  }
}
