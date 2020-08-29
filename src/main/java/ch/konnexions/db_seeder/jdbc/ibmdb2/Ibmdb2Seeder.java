package ch.konnexions.db_seeder.jdbc.ibmdb2;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenIbmdb2Schema;

/**
 * Test Data Generator for an IBM Db2 DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class Ibmdb2Seeder extends AbstractGenIbmdb2Schema {

  private static final Logger logger = Logger.getLogger(Ibmdb2Seeder.class);

  /**
   * Gets the connection URL for non-privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param database the database 
   *
   * @return the connection URL for non-privileged access
   */
  private final static String getUrlUser(String connectionHost, int connectionPort, String connectionPrefix, String database) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new IBM Db2 seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public Ibmdb2Seeder(String tickerSymbolExtern) {
    super(tickerSymbolExtern);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsEnum       = DbmsEnum.IBMDB2;

    dropTableStmnt = "SELECT 'DROP TABLE \"' || TABSCHEMA || '\".\"' || TABNAME || '\";' FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABSCHEMA = 'schemaName' AND TABNAME = '?'";

    urlUser        = getUrlUser(config.getConnectionHost(),
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
   *
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

    connection = connect(urlUser,
                         null,
                         config.getUserSys(),
                         config.getPasswordSys());

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
