/**
 *
 */
package ch.konnexions.db_seeder.jdbc.cratedb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a CrateDB DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class CratedbSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(CratedbSeeder.class);

  /**
   * Instantiates a new CrateDB seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public CratedbSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.CRATEDB;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    tableNameDelimiter    = "";

    urlBase               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/?strict=true&user=";
    url                   = urlBase + config.getUser() + "&password=" + config.getPassword();
    urlSetup              = urlBase + config.getUserSys();

    dropTableStmnt        = "SELECT 'DROP TABLE ' || table_name FROM information_schema.tables WHERE table_schema = 'doc' AND table_name = '?'";

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  @SuppressWarnings("preview") @Override protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE CITY (
                 PK_CITY_ID          BIGINT    NOT NULL PRIMARY KEY,
                 FK_COUNTRY_STATE_ID BIGINT,
                 CITY_MAP            OBJECT,
                 CREATED             TIMESTAMP NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                TEXT      NOT NULL
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGINT    NOT NULL PRIMARY KEY,
                 FK_CITY_ID    BIGINT    NOT NULL,
                 ACTIVE        TEXT      NOT NULL,
                 ADDRESS1      TEXT,
                 ADDRESS2      TEXT,
                 ADDRESS3      TEXT,
                 CREATED       TIMESTAMP NOT NULL,
                 DIRECTIONS    TEXT,
                 EMAIL         TEXT,
                 FAX           TEXT,
                 MODIFIED      TIMESTAMP,
                 NAME          TEXT      NOT NULL,
                 PHONE         TEXT,
                 POSTAL_CODE   TEXT,
                 URL           TEXT,
                 VAT_ID_NUMBER TEXT
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGINT    NOT NULL PRIMARY KEY,
                COUNTRY_MAP   OBJECT,
                CREATED       TIMESTAMP NOT NULL,
                ISO3166       TEXT,
                MODIFIED      TIMESTAMP,
                NAME          TEXT      NOT NULL
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGINT    NOT NULL PRIMARY KEY,
                FK_COUNTRY_ID       BIGINT    NOT NULL,
                FK_TIMEZONE_ID      BIGINT    NOT NULL,
                COUNTRY_STATE_MAP   OBJECT,
                CREATED             TIMESTAMP NOT NULL,
                MODIFIED            TIMESTAMP,
                NAME                TEXT      NOT NULL,
                SYMBOL              TEXT
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID BIGINT     NOT NULL PRIMARY KEY,
                ABBREVIATION   TEXT       NOT NULL,
                CREATED        TIMESTAMP  NOT NULL,
                MODIFIED       TIMESTAMP,
                NAME           TEXT       NOT NULL,
                V_TIME_ZONE    TEXT
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME,
                                                                                           tableName));
    }
  }

  @Override protected final void setupDatabase() {
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

    connection = connect(urlSetup,
                         true);

    String userName = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP USER IF EXISTS " + userName);

      dropAllTables(dropTableStmnt);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE USER " + userName + " WITH (PASSWORD = '" + config.getPassword() + "')",
                       "GRANT ALL PRIVILEGES TO " + userName);

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
                         true);

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }
}
