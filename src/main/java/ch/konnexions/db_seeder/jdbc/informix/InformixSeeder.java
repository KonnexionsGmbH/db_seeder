/**
 *
 */
package ch.konnexions.db_seeder.jdbc.informix;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for an IBM Informix DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class InformixSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(InformixSeeder.class);

  /**
   * Instantiates a new IBM Informix seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public InformixSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.INFORMIX;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    driver                = "com.informix.jdbc.IfxDriver";

    tableNameDelimiter    = "";

    urlBase               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/";
    url                   = urlBase + config.getDatabase() + config.getConnectionSuffix();
    urlSetup              = urlBase + config.getDatabaseSys() + config.getConnectionSuffix();

    dropTableStmnt        = "SELECT 'DROP TABLE \"' || TABUSER || '\".\"' || TABNAME || '\";' FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABNAME = ? AND TABUSER = ?";

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
                 PK_CITY_ID          BIGINT                    NOT NULL PRIMARY KEY,
                 FK_COUNTRY_STATE_ID BIGINT,
                 CITY_MAP            BLOB,
                 CREATED             DATETIME YEAR TO FRACTION NOT NULL,
                 MODIFIED            DATETIME YEAR TO FRACTION,
                 NAME                VARCHAR(100)              NOT NULL,
                 FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGINT                    NOT NULL PRIMARY KEY,
                 FK_CITY_ID    BIGINT                    NOT NULL,
                 ACTIVE        VARCHAR(1)                NOT NULL,
                 ADDRESS1      VARCHAR(50),
                 ADDRESS2      VARCHAR(50),
                 ADDRESS3      VARCHAR(50),
                 CREATED       DATETIME YEAR TO FRACTION NOT NULL,
                 DIRECTIONS    CLOB,
                 EMAIL         VARCHAR(100),
                 FAX           VARCHAR(50),
                 MODIFIED      DATETIME YEAR TO FRACTION,
                 NAME          VARCHAR(250)              NOT NULL UNIQUE,
                 PHONE         VARCHAR(50),
                 POSTAL_CODE   VARCHAR(50),
                 URL           VARCHAR(250),
                 VAT_ID_NUMBER VARCHAR(100),
                 FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGINT                    NOT NULL PRIMARY KEY,
                COUNTRY_MAP   BLOB,
                CREATED       DATETIME YEAR TO FRACTION NOT NULL,
                ISO3166       VARCHAR(50),
                MODIFIED      DATETIME YEAR TO FRACTION,
                NAME          VARCHAR(100)              NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGINT                    NOT NULL PRIMARY KEY,
                FK_COUNTRY_ID       BIGINT                    NOT NULL,
                FK_TIMEZONE_ID      BIGINT                    NOT NULL,
                COUNTRY_STATE_MAP   BLOB,
                CREATED             DATETIME YEAR TO FRACTION NOT NULL,
                MODIFIED            DATETIME YEAR TO FRACTION,
                NAME                VARCHAR(100)              NOT NULL,
                SYMBOL              VARCHAR(50),
                FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID),
                FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID),
                UNIQUE (FK_COUNTRY_ID,NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID BIGINT                    NOT NULL PRIMARY KEY,
                ABBREVIATION   VARCHAR(50)               NOT NULL,
                CREATED        DATETIME YEAR TO FRACTION NOT NULL,
                MODIFIED       DATETIME YEAR TO FRACTION,
                NAME           VARCHAR(100)              NOT NULL UNIQUE,
                V_TIME_ZONE    LVARCHAR(4000)
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
                         driver,
                         config.getUserSys(),
                         config.getPasswordSys(),
                         true);

    String databaseName = config.getDatabase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP DATABASE IF EXISTS " + databaseName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE DATABASE " + databaseName + " WITH LOG",
                       "GRANT CONNECT TO PUBLIC",
                       "GRANT RESOURCE TO PUBLIC");

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
                         config.getUserSys(),
                         config.getPasswordSys());

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }
}
