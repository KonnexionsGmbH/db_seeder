/**
 *
 */
package ch.konnexions.db_seeder.jdbc.mimer;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a Mimer SQL DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class MimerSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(MimerSeeder.class);

  /**
   * Instantiates a new Mimer SQL seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public MimerSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.MIMER;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    driver                = "com.mimer.jdbc.Driver";

    tableNameDelimiter    = "";

    url                   = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/" + config.getDatabaseSys();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
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
    return MimerSchema.createTableStmnts.get(tableName);
  }

  /**
   * Delete any existing relevant database schema objects (database, user, 
   * schema or tables)and initialise the database for a new run.
   */
  @Override
  protected final void setupDatabase() {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(url,
                         driver,
                         config.getUserSys(),
                         config.getPasswordSys(),
                         true);

    String databaseName = config.getDatabase();
    String userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropDatabase(databaseName,
                 "CASCADE",
                 "SYSTEM.DATABANKS",
                 "databank_name");

    dropUser(userName,
             "CASCADE",
             "SYSTEM.USERS",
             "user_name");

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE DATABANK " + databaseName + " SET OPTION TRANSACTION",
                       "CREATE IDENT " + userName + " AS USER USING '" + config.getPassword() + "'",
                       "GRANT TABLE ON DATABANK " + databaseName + " TO " + userName);

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
                         config.getPassword(),
                         true);

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }
}
