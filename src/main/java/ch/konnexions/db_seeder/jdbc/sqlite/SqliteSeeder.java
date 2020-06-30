/**
 *
 */
package ch.konnexions.db_seeder.jdbc.sqlite;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for am SQLite DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class SqliteSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(SqliteSeeder.class);

  /**
   * Instantiates a new SQLite seeder.
   */
  public SqliteSeeder() {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    dbms = Dbms.SQLITE;

    url  = config.getSQLiteConnectionPrefix() + config.getSQLiteDatabase();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE "CITY"
             (
                 PK_CITY_ID          INTEGER        NOT NULL PRIMARY KEY,
                 FK_COUNTRY_STATE_ID BIGINT,
                 CITY_MAP            BLOB,
                 CREATED             DATETIME       NOT NULL,
                 MODIFIED            DATETIME,
                 NAME                VARCHAR2 (100) NOT NULL,
                 FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES "COUNTRY_STATE" (PK_COUNTRY_STATE_ID)
             )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE "COMPANY"
             (
                 PK_COMPANY_ID       INTEGER        NOT NULL PRIMARY KEY,
                 FK_CITY_ID          BIGINT         NOT NULL,
                 ACTIVE              VARCHAR2 (1)   NOT NULL,
                 ADDRESS1            VARCHAR2 (50),
                 ADDRESS2            VARCHAR2 (50),
                 ADDRESS3            VARCHAR2 (50),
                 CREATED             DATETIME       NOT NULL,
                 DIRECTIONS          CLOB,
                 EMAIL               VARCHAR2 (100),
                 FAX                 VARCHAR2 (20),
                 MODIFIED            DATETIME,
                 NAME                VARCHAR2 (250) NOT NULL UNIQUE,
                 PHONE               VARCHAR2 (50),
                 POSTAL_CODE         VARCHAR2 (20),
                 URL                 VARCHAR2 (250),
                 VAT_ID_NUMBER       VARCHAR2 (50),
                 FOREIGN KEY (FK_CITY_ID)          REFERENCES "CITY" (PK_CITY_ID)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE "COUNTRY"
             (
                 PK_COUNTRY_ID INTEGER        NOT NULL PRIMARY KEY,
                 COUNTRY_MAP   BLOB,
                 CREATED       DATETIME       NOT NULL,
                 ISO3166       VARCHAR2 (2),
                 MODIFIED      DATETIME,
                 NAME          VARCHAR2 (100) NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE "COUNTRY_STATE"
             (
                 PK_COUNTRY_STATE_ID INTEGER        NOT NULL PRIMARY KEY,
                 FK_COUNTRY_ID       BIGINT         NOT NULL,
                 FK_TIMEZONE_ID      BIGINT         NOT NULL,
                 COUNTRY_STATE_MAP   BLOB,
                 CREATED             DATETIME       NOT NULL,
                 MODIFIED            DATETIME,
                 NAME                VARCHAR2 (100) NOT NULL,
                 SYMBOL              VARCHAR2 (10),
                 FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES "COUNTRY"  (PK_COUNTRY_ID),
                 FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES "TIMEZONE" (PK_TIMEZONE_ID),
                 UNIQUE (FK_COUNTRY_ID, NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE "TIMEZONE"
             (
                 PK_TIMEZONE_ID INTEGER         NOT NULL PRIMARY KEY,
                 ABBREVIATION   VARCHAR2 (20)   NOT NULL,
                 CREATED        DATETIME        NOT NULL,
                 MODIFIED       DATETIME,
                 NAME           VARCHAR2 (100)  NOT NULL UNIQUE,
                 V_TIME_ZONE    VARCHAR2 (4000)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  private final void dropAllTables() {
    try {
      statement = connection.createStatement();

      for (String tableName : TABLE_NAMES_DROP) {
        statement.execute("DROP TABLE IF EXISTS \"" + tableName + "\"");
      }

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  protected void setupDatabase() {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(url);

    // -----------------------------------------------------------------------
    // Drop the database tables if already existing.
    // -----------------------------------------------------------------------

    dropAllTables();

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }
}
