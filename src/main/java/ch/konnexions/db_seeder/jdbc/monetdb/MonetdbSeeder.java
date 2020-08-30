package ch.konnexions.db_seeder.jdbc.monetdb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenMonetdbSchema;

/**
 * Test Data Generator for a MonetDB DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class MonetdbSeeder extends AbstractGenMonetdbSchema {

  private static final Logger logger = Logger.getLogger(MonetdbSeeder.class);

  /**
   * Gets the connection URL.
   *
   * @param connectionHost the connection host name
   * @param connectionPort the connection port number
   * @param connectionPrefix the connection prefix
   * @param database the database
   * @param user the user
   * @param password the password
   *
   * @return the connection URL
   */
  private final static String getUrl(String connectionHost, int connectionPort, String connectionPrefix, String database, String user, String password) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database + "?user=" + user + "&password=" + password;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new MonetDB seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public MonetdbSeeder(String tickerSymbolExtern) {
    super(tickerSymbolExtern);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsEnum = DbmsEnum.MONETDB;

    driver   = "nl.cwi.monetdb.jdbc.MonetDriver";

    urlSys   = getUrl(config.getConnectionHost(),
                      config.getConnectionPort(),
                      config.getConnectionPrefix(),
                      config.getDatabaseSys(),
                      config.getUserSys(),
                      config.getPasswordSys());

    urlUser  = getUrl(config.getConnectionHost(),
                      config.getConnectionPort(),
                      config.getConnectionPrefix(),
                      config.getDatabaseSys(),
                      config.getUser(),
                      config.getPassword());

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
    return AbstractGenMonetdbSchema.createTableStmnts.get(tableName);
  }

  /**
   * Drop the database user and schema.
   *
   * @param userName the user name
   * @param schemaName the schema name
   */
  private void dropUser(String userName, String schemaName) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      int    count    = 0;

      String sqlStmnt = "SELECT count(*) FROM sys.users WHERE name = '" + userName + "'";

      if (isDebug) {
        logger.debug("next SQL statement=" + sqlStmnt);
      }

      resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        executeDdlStmnts(statement,
                         "ALTER USER " + userName + " SET SCHEMA sys",
                         "DROP SCHEMA " + schemaName + " CASCADE",
                         "CREATE SCHEMA " + schemaName + " AUTHORIZATION monetdb;",
                         "DROP USER " + userName,
                         "DROP SCHEMA " + schemaName);
      }
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

    connection = connect(urlSys,
                         driver,
                         true);

    String schemaName = config.getSchema();
    String userName   = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropUser(userName,
             schemaName);

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts(statement,
                       "CREATE USER " + userName + " WITH UNENCRYPTED PASSWORD '" + config.getPassword() + "' NAME 'Dbseeder User' SCHEMA sys",
                       "CREATE SCHEMA " + schemaName + " AUTHORIZATION " + userName,
                       "ALTER USER " + userName + " SET SCHEMA " + schemaName);

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
                         true);

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
