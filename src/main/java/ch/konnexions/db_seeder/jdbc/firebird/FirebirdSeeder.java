/**
 *
 */
package ch.konnexions.db_seeder.jdbc.firebird;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a Firebird DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class FirebirdSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(FirebirdSeeder.class);

  /**
   * Instantiates a new Firebird Server seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public FirebirdSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.FIREBIRD;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    driver                = "org.firebirdsql.jdbc.FBDriver";

    tableNameDelimiter    = "";

    url                   = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/" + config.getDatabase() + config
        .getConnectionSuffix();

    dropTableStmnt        = "SELECT 'DROP TABLE \"' || RDB$RELATION_NAME || '\";' FROM RDB$RELATIONS WHERE RDB$OWNER_NAME = 'userName' AND RDB$RELATION_NAME = '?'";

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
                 PK_CITY_ID          INTEGER      NOT NULL PRIMARY KEY,
                 FK_COUNTRY_STATE_ID INTEGER,
                 CITY_MAP            BLOB,
                 CREATED             TIMESTAMP    NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                VARCHAR(100) NOT NULL,
                 CONSTRAINT FK_CITY_COUNTRY_STATE FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID INTEGER        NOT NULL PRIMARY KEY,
                 FK_CITY_ID    INTEGER        NOT NULL,
                 ACTIVE        VARCHAR(1)     NOT NULL,
                 ADDRESS1      VARCHAR(50),
                 ADDRESS2      VARCHAR(50),
                 ADDRESS3      VARCHAR(50),
                 CREATED       TIMESTAMP       NOT NULL,
                 DIRECTIONS    BLOB SUB_TYPE 1,
                 EMAIL         VARCHAR(100),
                 FAX           VARCHAR(50),
                 MODIFIED      TIMESTAMP,
                 NAME          VARCHAR(250)    NOT NULL UNIQUE,
                 PHONE         VARCHAR(50),
                 POSTAL_CODE   VARCHAR(50),
                 URL           VARCHAR(250),
                 VAT_ID_NUMBER VARCHAR(100),
                 CONSTRAINT FK_COMPANY_CITY FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID INTEGER      NOT NULL PRIMARY KEY,
                COUNTRY_MAP   BLOB,
                CREATED       TIMESTAMP    NOT NULL,
                ISO3166       VARCHAR(50),
                MODIFIED      TIMESTAMP,
                NAME          VARCHAR(100) NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID INTEGER      NOT NULL PRIMARY KEY,
                FK_COUNTRY_ID       INTEGER      NOT NULL,
                FK_TIMEZONE_ID      INTEGER      NOT NULL,
                COUNTRY_STATE_MAP   BLOB,
                CREATED             TIMESTAMP    NOT NULL,
                MODIFIED            TIMESTAMP,
                NAME                VARCHAR(100) NOT NULL,
                SYMBOL              VARCHAR(50),
                CONSTRAINT FK_COUNTRY_STATE_COUNTRY  FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID),
                CONSTRAINT FK_COUNTRY_STATE_TIMEZONE FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID),
                CONSTRAINT UQ_COUNTRY_STATE          UNIQUE (FK_COUNTRY_ID,NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID INTEGER       NOT NULL PRIMARY KEY,
                ABBREVIATION   VARCHAR(50)   NOT NULL,
                CREATED        TIMESTAMP     NOT NULL,
                MODIFIED       TIMESTAMP,
                NAME           VARCHAR(100)  NOT NULL UNIQUE,
                V_TIME_ZONE    VARCHAR(4000)
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
