/**
 *
 */
package ch.konnexions.db_seeder.jdbc.hsqldb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.HsqldbSchema;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a HyperQL Database DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class HsqldbSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(AbstractJdbcSeeder.class);

  /**
   * Instantiates a new HyperQL Database seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public HsqldbSeeder(String dbmsTickerSymbol) {
    super();

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    this.dbmsTickerSymbol = dbmsTickerSymbol;

    init();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Instantiates a new HyperQL Database seeder.
   * @param dbmsTickerSymbol 
   *
   * @param isClient client database version
   */
  public HsqldbSeeder(String dbmsTickerSymbol, boolean isClient) {
    super(isClient);

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    this.dbmsTickerSymbol = dbmsTickerSymbol;

    init();

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
    return HsqldbSchema.createTableStmnts.get(tableName);
  }

  /**
   * The common initialisation part.
   */
  private final void init() {
    if (isDebug) {
      logger.debug("Start");

      logger.debug("client  =" + isClient);
      logger.debug("embedded=" + isEmbedded);
    }

    dbmsEnum               = DbmsEnum.HSQLDB;

    driver             = "org.hsqldb.jdbc.JDBCDriver";

    tableNameDelimiter = "";

    if (isClient) {
      url = config.getConnectionPrefix() + "hsql://" + config.getConnectionHost() + ":" + config.getConnectionPort() + "/" + config.getDatabase() + config
          .getConnectionSuffix();
    } else {
      url = config.getConnectionPrefix() + "file:" + config.getDatabase() + config.getConnectionSuffix();
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

    connection = connect(url,
                         driver,
                         config.getUserSys().toUpperCase(),
                         "",
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
               "INFORMATION_SCHEMA.SYSTEM_SCHEMAS",
               "table_schem");

    dropUser(userName,
             "",
             "INFORMATION_SCHEMA.SYSTEM_USERS",
             "user_name");

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE USER " + userName + " PASSWORD '" + password + "' ADMIN",
                       "CREATE SCHEMA " + schemaName + " AUTHORIZATION " + userName);

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
                         password);

    try {
      statement = connection.createStatement();

      executeDdlStmnts("SET SCHEMA " + schemaName);

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
