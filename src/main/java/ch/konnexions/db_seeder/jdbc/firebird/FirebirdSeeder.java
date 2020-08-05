package ch.konnexions.db_seeder.jdbc.firebird;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenFirebirdSchema;

/**
 * Test Data Generator for a Firebird DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class FirebirdSeeder extends AbstractGenFirebirdSchema {

  private static final Logger logger  = Logger.getLogger(FirebirdSeeder.class);
  private final boolean       isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new Firebird seeder object.
   * 
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public FirebirdSeeder(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsEnum              = DbmsEnum.FIREBIRD;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    driver                = "org.firebirdsql.jdbc.FBDriver";

    urlUser               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/" + config.getDatabase() + config
        .getConnectionSuffix();

    dropTableStmnt        = "SELECT 'DROP TABLE \"' || RDB$RELATION_NAME || '\";' FROM RDB$RELATIONS WHERE RDB$OWNER_NAME = 'userName' AND RDB$RELATION_NAME = '?'";

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
    return AbstractGenFirebirdSchema.createTableStmnts.get(tableName);
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
      executeDdlStmnts("CREATE USER " + userName + " PASSWORD '" + config.getPassword() + "' GRANT ADMIN ROLE",
                       "GRANT CREATE TABLE TO " + userName);

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
                         config.getPassword(),
                         true);

    if (isDebug) {
      logger.debug("End");
    }
  }
}
