package ch.konnexions.db_seeder.jdbc.mariadb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenMariadbSchema;

/**
 * Test Data Generator for a MariaDB DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class MariadbSeeder extends AbstractGenMariadbSchema {

  private static final Logger logger = Logger.getLogger(MariadbSeeder.class);

  /**
   * Gets the connection URL for privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param databaseSys the database with privileged access
   *
   * @return the connection URL for privileged access
   */
  private final static String getUrlSys(String connectionHost, int connectionPort, String connectionPrefix, String databaseSys) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + databaseSys;
  }

  /**
   * Gets the connection URL for non-privileged access.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param database the database with non-privileged access
   *
   * @return the connection URL for non-privileged access
   */
  private final static String getUrlUser(String connectionHost, int connectionPort, String connectionPrefix, String database) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new MariaDB seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public MariadbSeeder(String tickerSymbolExtern) {
    super(tickerSymbolExtern);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsEnum = DbmsEnum.MARIADB;

    urlSys   = getUrlSys(config.getConnectionHost(),
                         config.getConnectionPort(),
                         config.getConnectionPrefix(),
                         config.getDatabaseSys());

    urlUser  = getUrlUser(config.getConnectionHost(),
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
    return AbstractGenMariadbSchema.createTableStmnts.get(tableName);
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
                         null,
                         config.getUserSys(),
                         config.getPasswordSys());

    String databaseName = config.getDatabase();
    String userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts(statement,
                       "DROP DATABASE IF EXISTS `" + databaseName + "`",
                       "DROP USER IF EXISTS '" + userName + "'");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts(statement,
                       "CREATE DATABASE `" + databaseName + "`",
                       "USE `" + databaseName + "`",
                       "CREATE USER '" + userName + "'@'%' IDENTIFIED BY '" + config.getPassword() + "'",
                       "GRANT ALL PRIVILEGES ON *.* TO '" + userName + "'@'%'",
                       "FLUSH PRIVILEGES");

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
                         userName,
                         config.getPassword());

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
