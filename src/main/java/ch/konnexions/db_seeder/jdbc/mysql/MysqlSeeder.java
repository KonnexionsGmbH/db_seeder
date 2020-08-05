package ch.konnexions.db_seeder.jdbc.mysql;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.AbstractDbmsSeeder.DbmsEnum;
import ch.konnexions.db_seeder.generated.AbstractGenMysqlSchema;

/**
 * Test Data Generator for a MySQL DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class MysqlSeeder extends AbstractGenMysqlSchema {

  private static final Logger logger = Logger.getLogger(MysqlSeeder.class);

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
  public final static String getUrlSys(String connectionHost, int connectionPort, String connectionPrefix, String connectionSuffix, String databaseSys) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + databaseSys + connectionSuffix;
  }

  /**
   * Gets the connection URL for non-privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param connectionSuffix the connection suffix
   * @param databaseSys the database with non-privileged access
   * 
   * @return the connection URL for non-privileged access
   */
  public final static String getUrlUser(String connectionHost, int connectionPort, String connectionPrefix, String connectionSuffix, String database) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database + connectionSuffix;
  }

  /**
   * Instantiates a new MySQL seeder object.
   * 
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public MysqlSeeder(String dbmsTickerSymbol) {
    this(dbmsTickerSymbol, "client");
  }

  /**
   * Instantiates a new MySQL seeder object.
   * 
   * @param dbmsTickerSymbol DBMS ticker symbol 
   * @param dbmsOption client, embedded or presto
   */
  public MysqlSeeder(String dbmsTickerSymbol, String dbmsOption) {
    super(dbmsTickerSymbol, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum              = DbmsEnum.MYSQL;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    driver                = "com.mysql.cj.jdbc.Driver";

    urlSys                = getUrlSys(config.getConnectionHost(),
                                      config.getConnectionPort(),
                                      config.getConnectionPrefix(),
                                      config.getConnectionSuffix(),
                                      config.getDatabaseSys());

    urlUser               = getUrlUser(config.getConnectionHost(),
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

    connection = connect(urlSys,
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

    connection = connect(urlUser,
                         null,
                         userName,
                         config.getPassword());

    if (isDebug) {
      logger.debug("End");
    }
  }
}
