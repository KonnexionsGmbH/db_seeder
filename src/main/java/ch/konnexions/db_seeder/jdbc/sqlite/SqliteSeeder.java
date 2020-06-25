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
             CREATE TABLE city
             (
                 pk_city_id          INTEGER        NOT NULL PRIMARY KEY AUTOINCREMENT,
                 fk_country_state_id BIGINT,
                 city_map            BLOB,
                 created             DATETIME       NOT NULL,
                 modified            DATETIME,
                 name                VARCHAR2 (100) NOT NULL,
                 FOREIGN KEY (fk_country_state_id) REFERENCES country_state (pk_country_state_id)
             )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE company
             (
                 pk_company_id       INTEGER        NOT NULL PRIMARY KEY AUTOINCREMENT,
                 fk_city_id          BIGINT         NOT NULL,
                 active              VARCHAR2 (1)   NOT NULL,
                 address1            VARCHAR2 (50),
                 address2            VARCHAR2 (50),
                 address3            VARCHAR2 (50),
                 created             DATETIME       NOT NULL,
                 directions          CLOB,
                 email               VARCHAR2 (100),
                 fax                 VARCHAR2 (20),
                 modified            DATETIME,
                 name                VARCHAR2 (250) NOT NULL UNIQUE,
                 phone               VARCHAR2 (50),
                 postal_code         VARCHAR2 (20),
                 url                 VARCHAR2 (250),
                 vat_id_number       VARCHAR2 (50),
                 FOREIGN KEY (fk_city_id)          REFERENCES city (pk_city_id)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE country
             (
                 pk_country_id INTEGER        NOT NULL PRIMARY KEY AUTOINCREMENT,
                 country_map   BLOB,
                 created       DATETIME       NOT NULL,
                 iso3166       VARCHAR2 (2),
                 modified      DATETIME,
                 name          VARCHAR2 (100) NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE country_state
             (
                 pk_country_state_id INTEGER        NOT NULL PRIMARY KEY AUTOINCREMENT,
                 fk_country_id       BIGINT         NOT NULL,
                 fk_timezone_id      BIGINT         NOT NULL,
                 country_state_map   BLOB,
                 created             DATETIME       NOT NULL,
                 modified            DATETIME,
                 name                VARCHAR2 (100) NOT NULL,
                 symbol              VARCHAR2 (10),
                 FOREIGN KEY (fk_country_id)  REFERENCES country  (pk_country_id),
                 FOREIGN KEY (fk_timezone_id) REFERENCES timezone (pk_timezone_id),
                 UNIQUE (fk_country_id, name)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE timezone
             (
                 pk_timezone_id INTEGER         NOT NULL PRIMARY KEY AUTOINCREMENT,
                 abbreviation   VARCHAR2 (20)   NOT NULL,
                 created        DATETIME        NOT NULL,
                 modified       DATETIME,
                 name           VARCHAR2 (100)  NOT NULL UNIQUE,
                 v_time_zone    VARCHAR2 (4000)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  private final void dropAllTables() {
    try {
      statement = connection.createStatement();

      for (String tableName : TABLE_NAMES) {
        statement.execute("DROP TABLE IF EXISTS " + tableName);
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
