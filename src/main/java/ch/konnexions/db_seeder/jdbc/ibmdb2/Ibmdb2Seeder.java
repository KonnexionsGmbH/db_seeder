package ch.konnexions.db_seeder.jdbc.ibmdb2;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenIbmdb2Schema;

/**
 * Data Generator for an IBM Db2 DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class Ibmdb2Seeder extends AbstractGenIbmdb2Schema {

  private static final Logger logger = LogManager.getLogger(Ibmdb2Seeder.class);

  /**
   * Gets the connection URL.
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param database         the database
   * @param user             the user
   * @param password         the password
   * @return the connection URL
   */
  private static String getUrl(String connectionHost, int connectionPort, String connectionPrefix, String database, String user, String password) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database + ":user=" + user + ";password=" + password + ";";
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new IBM Db2 seeder object.
   *
   * @param tickerSymbol the DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public Ibmdb2Seeder(String tickerSymbol, String dbmsOption) {
    super(tickerSymbol, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbol=" + tickerSymbol + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum       = DbmsEnum.IBMDB2;

    dropTableStmnt = "SELECT 'DROP TABLE \"' || TABSCHEMA || '\".\"' || TABNAME || '\";' FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABSCHEMA = 'schemaName' AND TABNAME = '?'";

    urlSys         = getUrl(config.getConnectionHost(),
                            config.getConnectionPort(),
                            config.getConnectionPrefix(),
                            config.getDatabase(),
                            config.getUserSys(),
                            config.getPasswordSys());

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
    return AbstractGenIbmdb2Schema.createTableStmnts.get(tableName);
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

    connection = connect(urlSys);

    String schemaName = config.getSchema().toUpperCase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropAllTables(dropTableStmnt.replace("schemaName",
                                         schemaName));

    dropSchema(schemaName,
               "RESTRICT",
               "SYSIBM.SYSSCHEMATA",
               "name");

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts(statement,
                       "CREATE SCHEMA " + schemaName,
                       "SET CURRENT SCHEMA " + schemaName + ";");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    try {
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
