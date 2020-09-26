package ch.konnexions.db_seeder.jdbc.voltdb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenVoltdbSchema;

/**
 * Test Data Generator for a HyperQL Database DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class VoltdbSeeder extends AbstractGenVoltdbSchema {

  private static final Logger logger = Logger.getLogger(VoltdbSeeder.class);

  /**
   * Gets the connection URL.
   *
   * @param isClient database client version 
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param connectionSuffix the connection suffix
   *
   * @return the connection URL
   */
  private static String getUrl(boolean isClient, String connectionHost, int connectionPort, String connectionPrefix, String connectionSuffix) {
    return connectionPrefix + connectionHost + ":" + connectionPort + connectionSuffix;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Initialises a new HyperSQL seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public VoltdbSeeder(String tickerSymbolExtern) {
    super(tickerSymbolExtern);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern);
    }

    dbmsEnum = DbmsEnum.VOLTDB;

    driver   = "org.voltdb.jdbc.Driver";

    urlSys   = getUrl(isClient,
                      config.getConnectionHost(),
                      config.getConnectionPort(),
                      config.getConnectionPrefix(),
                      config.getConnectionSuffix());

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
    return AbstractGenVoltdbSchema.createTableStmnts.get(tableName);
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
                         driver,
                         config.getUser().toUpperCase(),
                         config.getPassword(),
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

    dropAllTablesIfExists();

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

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
