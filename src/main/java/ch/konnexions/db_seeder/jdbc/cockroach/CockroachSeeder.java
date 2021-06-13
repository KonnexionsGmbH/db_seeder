package ch.konnexions.db_seeder.jdbc.cockroach;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenCockroachSchema;

/**
 * Data Generator for a CockroachDB DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class CockroachSeeder extends AbstractGenCockroachSchema {

  private static final Logger logger = LogManager.getLogger(CockroachSeeder.class);

  /**
   * Gets the connection URL.
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param database         the database with non-privileged access
   * @param user             the user
   * @return the connection URL
   */
  private static String getUrl(String connectionHost, int connectionPort, String connectionPrefix, String database, String user) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database + "?user=" + user;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new CockroachDB seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   */
  public CockroachSeeder(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Instantiates a new CockroachDB seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public CockroachSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.POSTGRESQL;

    driver   = "net.bitnine.agensgraph.Driver";

    urlSys   = getUrl(config.getConnectionHost(),
                      config.getConnectionPort(),
                      config.getConnectionPrefix(),
                      config.getDatabaseSys(),
                      config.getUserSys());

    urlUser  = getUrl(config.getConnectionHost(),
                      config.getConnectionPort(),
                      config.getConnectionPrefix(),
                      config.getDatabase(),
                      config.getUser());

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
    return AbstractGenCockroachSchema.createTableStmnts.get(tableName);
  }

  @Override
  protected void dropTableConstraints(Connection connection) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void restoreTableConstraints(Connection connection) {
    // TODO Auto-generated method stub

  }

  @Override
  protected final void setupDatabase() {
    if (isDebug) {
      logger.debug("Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSys,
                         true);

    String databaseName = config.getDatabase();
    String userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing database.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts(statement,
                       "DROP DATABASE IF EXISTS " + databaseName + " CASCADE",
                       "DROP USER IF EXISTS " + userName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts(statement,
                       "CREATE USER " + userName,
                       "CREATE DATABASE " + databaseName,
                       "GRANT ALL ON DATABASE " + databaseName + " TO " + userName);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Connect non-priviledged user.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(urlUser);

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
