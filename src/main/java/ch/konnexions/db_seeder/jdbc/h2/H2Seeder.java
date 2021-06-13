package ch.konnexions.db_seeder.jdbc.h2;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenH2Schema;

/**
 * Data Generator for a H2 Database Engine DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class H2Seeder extends AbstractGenH2Schema {

  private static final Logger logger = LogManager.getLogger(H2Seeder.class);

  /**
   * Gets the connection URL.
   *
   * @param isClient         client version
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param database         the database
   * @param user             the user
   * @param password         the password
   * @return the connection URL
   */
  private static String getUrl(boolean isClient,
                               String connectionHost,
                               int connectionPort,
                               String connectionPrefix,
                               String database,
                               String user,
                               String password) {
    if (isClient) {
      return connectionPrefix + "tcp://" + connectionHost + ":" + connectionPort + "/" + database + ";USER=" + user + ("".equals(password)
          ? ""
          : ";PASSWORD=" + password);
    } else {
      return connectionPrefix + "file:" + database + ";USER=" + user + ("".equals(password)
          ? ""
          : ";PASSWORD=" + password);
    }
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new H2 seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   */
  public H2Seeder(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Instantiates a new H2 seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public H2Seeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.H2;

    driver   = "org.h2.Driver";

    urlSys   = getUrl(isClient,
                      config.getConnectionHost(),
                      config.getConnectionPort(),
                      config.getConnectionPrefix(),
                      config.getDatabase(),
                      config.getUserSys(),
                      "");

    urlUser  = getUrl(isClient,
                      config.getConnectionHost(),
                      config.getConnectionPort(),
                      config.getConnectionPrefix(),
                      config.getDatabase(),
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
   * @return the 'CREATE TABLE' statement
   */
  @Override
  protected final String createDdlStmnt(String tableName) {
    return AbstractGenH2Schema.createTableStmnts.get(tableName);
  }

  @Override
  protected void dropTableConstraints(Connection connection) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void restoreTableConstraints(Connection connection) {
    // TODO Auto-generated method stub

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

    String password   = config.getPassword();
    String schemaName = config.getSchema().toUpperCase();
    String userName   = config.getUser().toUpperCase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropSchema(schemaName,
               "CASCADE",
               "INFORMATION_SCHEMA.SCHEMATA",
               "schema_name");

    dropUser(userName,
             "",
             "INFORMATION_SCHEMA.USERS",
             "name");

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts(statement,
                       "CREATE USER " + userName + " PASSWORD '" + password + "' ADMIN",
                       "CREATE SCHEMA " + schemaName + " AUTHORIZATION " + userName);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(urlUser);

    try {
      statement = connection.createStatement();

      executeDdlStmnts(statement,
                       "SET SCHEMA " + schemaName);

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
