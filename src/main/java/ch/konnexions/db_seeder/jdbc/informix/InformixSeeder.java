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
   */
  public InformixSeeder() {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    dbms               = Dbms.INFORMIX;

    driver             = "com.informix.jdbc.IfxDriver";

    tableNameDelimiter = "";

    url                = config.getInformixConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getInformixConnectionPort() + "/"
        + config.getInformixDatabase() + config.getInformixConnectionSuffix();

    urlSetup           = config.getInformixConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getInformixConnectionPort() + "/"
        + config.getInformixDatabaseSys() + config.getInformixConnectionSuffix();

    dropTableStmnt     = "SELECT 'DROP TABLE \"' || TABUSER || '\".\"' || TABNAME || '\";' FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABNAME = ? AND TABUSER = ?";

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
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
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  @Override
  protected final void setupDatabase() {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSetup, driver, config.getInformixUserSys(), config.getInformixPasswordSys(), true);

    String informixDatabase = config.getInformixDatabase();

    // -----------------------------------------------------------------------
    // Drop the database and user if already existing
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      statement.execute("DROP DATABASE IF EXISTS " + informixDatabase);

    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create the database and user.
    // -----------------------------------------------------------------------

    try {
      statement.execute("CREATE DATABASE " + informixDatabase + " WITH LOG");

      statement.execute("GRANT CONNECT TO PUBLIC");

      statement.execute("GRANT RESOURCE TO PUBLIC");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(url, null, config.getInformixUserSys(), config.getInformixPasswordSys());

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }
}
