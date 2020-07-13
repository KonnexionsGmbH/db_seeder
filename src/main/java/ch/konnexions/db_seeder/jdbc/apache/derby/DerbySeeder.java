/**
 *
 */
package ch.konnexions.db_seeder.jdbc.apache.derby;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a Apache Derby DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class DerbySeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(AbstractJdbcSeeder.class);

  /**
   * Instantiates a new Apache Derby seeder.
   * @param args0 
   */
  public DerbySeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");
    }

    this.dbmsTickerSymbol = dbmsTickerSymbol;

    init();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
    }
  }

  /**
   * Instantiates a new Apache Derby seeder.
   * @param dbmsTickerSymbol 
   *
   * @param isClient client database version
   */
  public DerbySeeder(String dbmsTickerSymbol, boolean isClient) {
    super(isClient);

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");
    }

    this.dbmsTickerSymbol = dbmsTickerSymbol;

    init();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
    }
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE CITY (
                 PK_CITY_ID          BIGINT         NOT NULL PRIMARY KEY,
                 FK_COUNTRY_STATE_ID BIGINT,
                 CITY_MAP            BLOB,
                 CREATED             TIMESTAMP      NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                VARCHAR(100)   NOT NULL,
                 CONSTRAINT FK_CITY_COUNTRY_STATE FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGINT       NOT NULL PRIMARY KEY,
                 FK_CITY_ID    BIGINT       NOT NULL,
                 ACTIVE        VARCHAR(1)   NOT NULL,
                 ADDRESS1      VARCHAR(50),
                 ADDRESS2      VARCHAR(50),
                 ADDRESS3      VARCHAR(50),
                 CREATED       TIMESTAMP    NOT NULL,
                 DIRECTIONS    CLOB,
                 EMAIL         VARCHAR(100),
                 FAX           VARCHAR(50),
                 MODIFIED      TIMESTAMP,
                 NAME          VARCHAR(250) NOT NULL UNIQUE,
                 PHONE         VARCHAR(50),
                 POSTAL_CODE   VARCHAR(50),
                 URL           VARCHAR(250),
                 VAT_ID_NUMBER VARCHAR(100),
                 CONSTRAINT FK_COMPANY_CITY FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGINT         NOT NULL PRIMARY KEY,
                COUNTRY_MAP   BLOB,
                CREATED       TIMESTAMP      NOT NULL,
                ISO3166       VARCHAR(50),
                MODIFIED      TIMESTAMP,
                NAME          VARCHAR(100)   NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGINT         NOT NULL PRIMARY KEY,
                FK_COUNTRY_ID       BIGINT         NOT NULL,
                FK_TIMEZONE_ID      BIGINT         NOT NULL,
                COUNTRY_STATE_MAP   BLOB,
                CREATED             TIMESTAMP      NOT NULL,
                MODIFIED            TIMESTAMP,
                NAME                VARCHAR(100)   NOT NULL,
                SYMBOL              VARCHAR(50),
                CONSTRAINT FK_COUNTRY_STATE_COUNTRY  FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID),
                CONSTRAINT FK_COUNTRY_STATE_TIMEZONE FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID),
                CONSTRAINT UQ_COUNTRY_STATE          UNIQUE      (FK_COUNTRY_ID,NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID BIGINT        NOT NULL PRIMARY KEY,
                ABBREVIATION   VARCHAR(50)   NOT NULL,
                CREATED        TIMESTAMP     NOT NULL,
                MODIFIED       TIMESTAMP,
                NAME           VARCHAR(100)  NOT NULL UNIQUE,
                V_TIME_ZONE    VARCHAR(4000)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  protected final void dropAllTables(String sqlStmnt) {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");
    }

    try {
      Connection connectionLocal = connect(urlSetup, null, true);

      preparedStatement = connection.prepareStatement(sqlStmnt);

      statement         = connectionLocal.createStatement();

      for (String tableName : TABLE_NAMES_DROP) {
        preparedStatement.setString(1, tableName);

        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
          statement.execute(resultSet.getString(2));
        }

        resultSet.close();
      }

      statement.close();

      preparedStatement.close();

      disconnect(connectionLocal);

    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
    }
  }

  private final void init() {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- client  =" + isClient);
      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- embedded=" + isEmbedded);
    }

    dbms = Dbms.DERBY;

    if (isClient) {
      driver  = "org.apache.derby.jdbc.ClientDriver";
      urlBase = config.getApachederbyConnectionPrefix() + "//" + config.getApachederbyConnectionHost() + ":" + config.getApachederbyConnectionPort() + "/"
          + config.getApachederbyDatabase() + ";create=";
    } else {
      driver  = "org.apache.derby.jdbc.EmbeddedDriver";
      urlBase = config.getApachederbyConnectionPrefix() + ";databaseName=" + config.getApachederbyDatabase() + ";create=";
    }

    tableNameDelimiter = "";

    url                = urlBase + "false";
    urlSetup           = urlBase + "true";

    dropTableStmnt     = "SELECT T.TABLENAME, 'DROP TABLE \"' || T.TABLENAME || '\"' FROM SYS.SYSTABLES T INNER JOIN SYS.SYSSCHEMAS S ON T.SCHEMAID = S.SCHEMAID WHERE T.TABLENAME = ? AND S.SCHEMANAME = 'APP'";

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
    }
  }

  @Override
  protected final void setupDatabase() {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSetup, driver, true);

    // -----------------------------------------------------------------------
    // Drop the database tables if already existing
    // -----------------------------------------------------------------------

    dropAllTables(dropTableStmnt);

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(url);

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
    }
  }
}
