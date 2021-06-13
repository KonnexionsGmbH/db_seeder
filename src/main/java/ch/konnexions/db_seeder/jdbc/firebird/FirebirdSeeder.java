package ch.konnexions.db_seeder.jdbc.firebird;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenFirebirdSchema;

/**
 * Data Generator for a Firebird DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class FirebirdSeeder extends AbstractGenFirebirdSchema {

  private static final Logger logger = LogManager.getLogger(FirebirdSeeder.class);

  /**
   * Gets the connection URL.
   *
   * @param connectionHost   the connection host name
   * @param connectionPort   the connection port number
   * @param connectionPrefix the connection prefix
   * @param connectionSuffix the connection suffix
   * @param database         the database
   * @return the connection URL
   */
  private static String getUrl(String connectionHost, int connectionPort, String connectionPrefix, String connectionSuffix, String database) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database + connectionSuffix;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new Firebird seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   */
  public FirebirdSeeder(String tickerSymbolExtern) {
    super(tickerSymbolExtern);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsEnum       = DbmsEnum.FIREBIRD;

    driver         = "org.firebirdsql.jdbc.FBDriver";

    dropTableStmnt = "SELECT 'DROP TABLE \"' || RDB$RELATION_NAME || '\";' FROM RDB$RELATIONS WHERE RDB$OWNER_NAME = 'userName' AND RDB$RELATION_NAME = '?'";

    urlUser        = getUrl(config.getConnectionHost(),
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
   * @return the 'CREATE TABLE' statement
   */
  @Override
  protected final String createDdlStmnt(String tableName) {
    return AbstractGenFirebirdSchema.createTableStmnts.get(tableName);
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

    connection = connect(urlUser,
                         driver,
                         config.getUserSys().toUpperCase(),
                         config.getPasswordSys(),
                         true);

    String userName = config.getUser().toUpperCase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropAllTables(dropTableStmnt.replace("userName",
                                         userName));

    dropUser(userName,
             "",
             "SEC$USERS",
             "sec$user_name");

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts(statement,
                       "CREATE USER " + userName + " PASSWORD '" + config.getPassword() + "' GRANT ADMIN ROLE",
                       "GRANT CREATE TABLE TO " + userName);

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
                         config.getPassword(),
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
