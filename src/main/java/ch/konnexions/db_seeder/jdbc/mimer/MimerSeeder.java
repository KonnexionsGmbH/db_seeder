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

  @SuppressWarnings("preview") @Override protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE CITY (
                 PK_CITY_ID          BIGINT        NOT NULL PRIMARY KEY,
                 FK_COUNTRY_STATE_ID BIGINT,
                 CITY_MAP            BLOB,
                 CREATED             TIMESTAMP     NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                NVARCHAR(100) NOT NULL,
                 FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGINT        NOT NULL PRIMARY KEY,
                 FK_CITY_ID    BIGINT        NOT NULL,
                 ACTIVE        NVARCHAR(1)   NOT NULL,
                 ADDRESS1      NVARCHAR(50),
                 ADDRESS2      NVARCHAR(50),
                 ADDRESS3      NVARCHAR(50),
                 CREATED       TIMESTAMP     NOT NULL,
                 DIRECTIONS    CLOB,
                 EMAIL         NVARCHAR(100),
                 FAX           NVARCHAR(50),
                 MODIFIED      TIMESTAMP,
                 NAME          NVARCHAR(250) NOT NULL UNIQUE,
                 PHONE         NVARCHAR(50),
                 POSTAL_CODE   NVARCHAR(50),
                 URL           NVARCHAR(250),
                 VAT_ID_NUMBER NVARCHAR(100),
                 FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGINT        NOT NULL PRIMARY KEY,
                COUNTRY_MAP   BLOB,
                CREATED       TIMESTAMP     NOT NULL,
                ISO3166       NVARCHAR(50),
                MODIFIED      TIMESTAMP,
                NAME          NVARCHAR(100) NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGINT        NOT NULL PRIMARY KEY,
                FK_COUNTRY_ID       BIGINT        NOT NULL,
                FK_TIMEZONE_ID      BIGINT        NOT NULL,
                COUNTRY_STATE_MAP   BLOB,
                CREATED             TIMESTAMP     NOT NULL,
                MODIFIED            TIMESTAMP,
                NAME                NVARCHAR(100) NOT NULL,
                SYMBOL              NVARCHAR(50),
                FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID),
                FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID),
                UNIQUE      (FK_COUNTRY_ID,NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID BIGINT         NOT NULL PRIMARY KEY,
                ABBREVIATION   NVARCHAR(50)   NOT NULL,
                CREATED        TIMESTAMP      NOT NULL,
                MODIFIED       TIMESTAMP,
                NAME           NVARCHAR(100)  NOT NULL UNIQUE,
                V_TIME_ZONE    NVARCHAR(4000)
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
