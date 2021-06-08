package ch.konnexions.db_seeder.jdbc.sqlite;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenSqliteSchema;

/**
 * Data Generator for am SQLite DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class SqliteSeeder extends AbstractGenSqliteSchema {

  private static final Logger logger = LogManager.getLogger(SqliteSeeder.class);

  /**
   * Gets the connection URL.
   *
   * @param connectionPrefix the connection prefix
   * @param database         the database
   * @return the connection URL
   */
  private static String getUrl(String connectionPrefix, String database) {
    return connectionPrefix + database;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new SQLite seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public SqliteSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.SQLITE;

    urlSys   = getUrl(config.getConnectionPrefix(),
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
    return AbstractGenSqliteSchema.createTableStmnts.get(tableName);
  }

  private void printDatabaseVersion() {
    if (isDebug) {
      logger.debug("Start");
    }

    String sqlStmnt = "SELECT sqlite_version()";

    try {
      resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        logger.info("SQLite version is " + resultSet.getString(1));
      }

      resultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
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

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
      printDatabaseVersion();
      dropAllTablesIfExists();
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

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
