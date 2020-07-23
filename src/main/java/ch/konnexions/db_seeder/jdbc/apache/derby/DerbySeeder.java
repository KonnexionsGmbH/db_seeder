package ch.konnexions.db_seeder.jdbc.apache.derby;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenDerbySchema;

/**
 * Test Data Generator for a Apache Derby DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class DerbySeeder extends AbstractGenDerbySchema {

  private static final Logger logger = Logger.getLogger(DerbySeeder.class);

  /**
   * Instantiates a new Apache Derby seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public DerbySeeder(String dbmsTickerSymbol) {
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
   * Instantiates a new Apache Derby seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   * @param isClient client database version
   */
  public DerbySeeder(String dbmsTickerSymbol, boolean isClient) {
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
  protected final String createDdlStmnt(final String tableName) {
    return AbstractGenDerbySchema.createTableStmnts.get(tableName);
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

    dbmsEnum = DbmsEnum.DERBY;

    if (isClient) {
      driver  = "org.apache.derby.jdbc.ClientDriver";
      urlBase = config.getConnectionPrefix() + "//" + config.getConnectionHost() + ":" + config.getConnectionPort() + "/" + config.getDatabase() + ";create=";
    } else {
      driver  = "org.apache.derby.jdbc.EmbeddedDriver";
      urlBase = config.getConnectionPrefix() + ";databaseName=" + config.getDatabase() + ";create=";
    }

    tableNameDelimiter = "";

    url                = urlBase + "false";
    urlSetup           = urlBase + "true";

    dropTableStmnt     = "SELECT 'DROP TABLE \"' || T.TABLENAME || '\"' FROM SYS.SYSTABLES T INNER JOIN SYS.SYSSCHEMAS S ON T.SCHEMAID = S.SCHEMAID WHERE T.TABLENAME = '?' AND S.SCHEMANAME = 'APP'";

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

    connection = connect(urlSetup,
                         driver,
                         true);

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropAllTables(dropTableStmnt);

    try {
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(url);

    if (isDebug) {
      logger.debug("End");
    }
  }
}
