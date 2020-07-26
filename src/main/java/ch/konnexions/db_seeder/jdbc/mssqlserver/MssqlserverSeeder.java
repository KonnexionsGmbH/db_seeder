package ch.konnexions.db_seeder.jdbc.mssqlserver;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenMssqlserverSchema;

/**
 * Test Data Generator for a Microsoft SQL Server DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class MssqlserverSeeder extends AbstractGenMssqlserverSchema {

  private static final Logger logger = Logger.getLogger(MssqlserverSeeder.class);

  /**
   * Instantiates a new Microsoft SQL Server seeder object.
   * 
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public MssqlserverSeeder(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsEnum              = DbmsEnum.MSSQLSERVER;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    tableNameDelimiter    = "";

    urlBase               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + ";databaseName=";
    url                   = urlBase + config.getDatabase() + ";user=" + config.getUser() + ";password=" + config.getPassword();
    urlSetup              = urlBase + config.getDatabaseSys() + ";user=" + config.getUserSys() + ";password=" + config.getPasswordSys();

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
    return AbstractGenMssqlserverSchema.createTableStmnts.get(tableName);
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
                         true);

    final String databaseNmame = config.getDatabase();
    final String schemaName    = config.getSchema();
    final String userName      = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP DATABASE IF EXISTS " + databaseNmame);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("sp_configure 'contained database authentication', 1",
                       "RECONFIGURE",
                       "USE master",
                       "CREATE DATABASE " + databaseNmame,
                       "USE master",
                       "ALTER DATABASE " + databaseNmame + " SET CONTAINMENT = PARTIAL",
                       "USE " + databaseNmame,
                       "CREATE SCHEMA " + schemaName,
                       "CREATE USER " + userName + " WITH PASSWORD = '" + config.getPassword() + "', DEFAULT_SCHEMA=" + schemaName,
                       "sp_addrolemember 'db_owner', '" + userName + "'");

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
