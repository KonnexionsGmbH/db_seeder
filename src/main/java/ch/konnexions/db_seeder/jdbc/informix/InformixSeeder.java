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
   * Gets the connection URL for privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param connectionSuffix the connection suffix
   * @param databaseSys the database with privileged access
   *
   * @return the connection URL for privileged access
   */
  private final static String getUrlSys(String connectionHost, int connectionPort, String connectionPrefix, String connectionSuffix, String databaseSys) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + databaseSys + connectionSuffix;
  }

  /**
   * Gets the connection URL for non-privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param connectionSuffix the connection suffix
   * @param database the database with non-privileged access
   *
   * @return the connection URL for non-privileged access
   */
  private final static String getUrlUser(String connectionHost, int connectionPort, String connectionPrefix, String connectionSuffix, String database) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database + connectionSuffix;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new IBM Informix seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public InformixSeeder(String tickerSymbolExtern) {
    super(tickerSymbolExtern);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsEnum       = DbmsEnum.INFORMIX;

    driver         = "com.informix.jdbc.IfxDriver";

    dropTableStmnt = "SELECT 'DROP TABLE \"' || TABUSER || '\".\"' || TABNAME || '\";' FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABNAME = ? AND TABUSER = ?";

    urlSys         = getUrlSys(config.getConnectionHost(),
                               config.getConnectionPort(),
                               config.getConnectionPrefix(),
                               config.getConnectionSuffix(),
                               config.getDatabaseSys());

    urlUser        = getUrlUser(config.getConnectionHost(),
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

    connection = connect(urlSys,
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

      executeDdlStmnts(statement,
                       "DROP DATABASE IF EXISTS " + databaseName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts(statement,
                       "CREATE DATABASE " + databaseName + " WITH LOG",
                       "GRANT CONNECT TO PUBLIC",
                       "GRANT RESOURCE TO PUBLIC");

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
                         config.getUserSys(),
                         config.getPasswordSys());

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
