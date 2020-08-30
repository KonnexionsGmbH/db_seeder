package ch.konnexions.db_seeder.jdbc.agens;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenAgensSchema;

/**
 * Test Data Generator for a AgensGraph DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class AgensSeeder extends AbstractGenAgensSchema {

  private static final Logger logger = Logger.getLogger(AgensSeeder.class);

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
   * Instantiates a new AgensGraph seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public AgensSeeder(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Instantiates a new AgensGraph seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   * @param dbmsOption client, embedded or presto
   */
  public AgensSeeder(String tickerSymbolExtern, String dbmsOption) {
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
                      config.getUserSys(),
                      config.getPasswordSys());

    urlUser  = getUrl(config.getConnectionHost(),
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
   *
   * @return the 'CREATE TABLE' statement
   */
  @Override
  protected final String createDdlStmnt(String tableName) {
    return AbstractGenAgensSchema.createTableStmnts.get(tableName);
  }

  @Override
  protected final void setupDatabase() {
    if (isDebug) {
      logger.debug("Start");
    }

    connection = setupPostgresql(statement,
                                 driver,
                                 urlSys,
                                 urlUser);

    if (isDebug) {
      logger.debug("End");
    }
  }

}
