package ch.konnexions.db_seeder.jdbc.yugabyte;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenYugabyteSchema;

/**
 * Test Data Generator for a YugabyteDB DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class YugabyteSeeder extends AbstractGenYugabyteSchema {

  private static final Logger logger = Logger.getLogger(YugabyteSeeder.class);

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
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + database + "?user=" + user + ("".equals(password)
        ? ""
        : "&password=" + password);
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new YugabyteDB seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   */
  public YugabyteSeeder(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Instantiates a new YugabyteDB seeder object.
   * 
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   * @param dbmsOption client, embedded or presto
   */
  private YugabyteSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.YUGABYTE;

    urlSys   = getUrl(config.getConnectionHost(),
                      config.getConnectionPort(),
                      config.getConnectionPrefix(),
                      config.getDatabaseSys(),
                      config.getUserSys(),
                      "");

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
    return AbstractGenYugabyteSchema.createTableStmnts.get(tableName);
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
