package ch.konnexions.db_seeder.jdbc.derby;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenDerbySchema;

/**
 * Data Generator for a Apache Derby DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class DerbySeeder extends AbstractGenDerbySchema {

  private static final Logger logger = LogManager.getLogger(DerbySeeder.class);

  /**
   * Gets the connection URL for privileged access.
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param database         the database
   * @return the connection URL for privileged access
   */
  private static String getUrlSys(boolean isClient, String connectionHost, int connectionPort, String connectionPrefix, String database) {
    String urlBase;

    if (isClient) {
      urlBase = connectionPrefix + "//" + connectionHost + ":" + connectionPort + "/";
    } else {
      urlBase = connectionPrefix + ";databaseName=";
    }
    return urlBase + database + ";create=true";
  }

  /**
   * Gets the connection URL for non-privileged access.
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param database         the database
   * @return the connection URL for non-privileged access
   */
  private static String getUrlUser(boolean isClient, String connectionHost, int connectionPort, String connectionPrefix, String database) {
    String urlBase;

    if (isClient) {
      urlBase = connectionPrefix + "//" + connectionHost + ":" + connectionPort + "/";
    } else {
      urlBase = connectionPrefix + ";databaseName=";
    }
    return urlBase + database + ";create=false";
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new Apache Derby seeder object.
   *
   * @param tickerSymbol the DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public DerbySeeder(String tickerSymbol, String dbmsOption) {
    super(tickerSymbol, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbol=" + tickerSymbol + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.DERBY;

    if (isClient) {
      driver = "org.apache.derby.jdbc.ClientDriver";
    } else {
      driver = "org.apache.derby.jdbc.EmbeddedDriver";
    }

    dropTableStmnt = "SELECT 'DROP TABLE ' || S.SCHEMANAME || '.\"' || T.TABLENAME || '\"' FROM SYS.SYSTABLES T INNER JOIN SYS.SYSSCHEMAS S ON T.SCHEMAID = S.SCHEMAID WHERE T.TABLENAME = '?' AND S.SCHEMANAME = '"
        + config.getSchema().toUpperCase() + "'";

    urlSys         = getUrlSys(isClient,
                               config.getConnectionHost(),
                               config.getConnectionPort(),
                               config.getConnectionPrefix(),
                               config.getDatabase());

    urlUser        = getUrlUser(isClient,
                                config.getConnectionHost(),
                                config.getConnectionPort(),
                                config.getConnectionPrefix(),
                                config.getDatabase());

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
    return AbstractGenDerbySchema.createTableStmnts.get(tableName);
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
                         true);

    String schemaName = config.getSchema();

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

    dropSchema(schemaName.toUpperCase(),
               "RESTRICT",
               "SYS.SYSSCHEMAS",
               "SCHEMANAME");

    try {
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    disconnectDDL(connection);

    connection = connect(urlUser);

    try {
      statement = connection.createStatement();

      executeSQLStmnts(statement,
                       "CREATE SCHEMA " + schemaName,
                       "SET CURRENT SCHEMA = " + schemaName);

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
