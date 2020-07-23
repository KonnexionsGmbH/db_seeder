package ch.konnexions.db_seeder.jdbc.mysql;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenMysqlSchema;

/**
 * Test Data Generator for a MySQL DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class MysqlSeeder extends AbstractGenMysqlSchema {

  private static final Logger logger = Logger.getLogger(MysqlSeeder.class);

  /**
   * Instantiates a new MySQL seeder object.
   * 
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public MysqlSeeder(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsEnum              = DbmsEnum.MYSQL;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    driver                = "com.mysql.cj.jdbc.Driver";

    tableNameDelimiter    = "`";

    urlBase               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/";
    url                   = urlBase + config.getDatabase() + config.getConnectionSuffix();
    urlSetup              = urlBase + config.getDatabaseSys() + config.getConnectionSuffix();

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
    return AbstractGenMysqlSchema.createTableStmnts.get(tableName);
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
                         config.getPasswordSys());

    String databaseName = config.getDatabase();
    String userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP DATABASE IF EXISTS `" + databaseName + "`",
                       "DROP USER IF EXISTS `" + userName + "`");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE DATABASE `" + databaseName + "`",
                       "USE `" + databaseName + "`",
                       "CREATE USER `" + userName + "` IDENTIFIED BY '" + config.getPassword() + "'",
                       "GRANT ALL ON " + databaseName + ".* TO `" + userName + "`");

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
                         config.getPassword());

    if (isDebug) {
      logger.debug("End");
    }
  }
}
