/**
 *
 */
package ch.konnexions.db_seeder.jdbc.ibmdb2;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for an IBM Db2 DBMS. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class Ibmdb2Seeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(Ibmdb2Seeder.class);

  /**
   * 
   */
  public Ibmdb2Seeder() {
    super();

    dbms           = Dbms.IBMDB2;

    url            = config.getIbmdb2ConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getIbmdb2ConnectionPort() + "/"
        + config.getIbmdb2Database() + ":user=db2inst1;password=" + config.getIbmdb2Password() + ";";

    dropTableStmnt = "SELECT TABNAME, 'DROP TABLE \"' || TABSCHEMA || '\".\"' || TABNAME || '\";' FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABNAME = ? AND TABSCHEMA = ?";
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE CITY (
                 PK_CITY_ID          BIGINT       NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                 FK_COUNTRY_STATE_ID BIGINT,
                 CITY_MAP            BLOB,
                 CREATED             TIMESTAMP    NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                VARCHAR(100) NOT NULL,
                 CONSTRAINT FK_CITY_COUNTRY_STATE FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID) ON DELETE CASCADE
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGINT       NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                 FK_CITY_ID    BIGINT       NOT NULL,
                 ACTIVE        VARCHAR(1)   NOT NULL,
                 ADDRESS1      VARCHAR(50),
                 ADDRESS2      VARCHAR(50),
                 ADDRESS3      VARCHAR(50),
                 CREATED       TIMESTAMP    NOT NULL,
                 DIRECTIONS    CLOB,
                 EMAIL         VARCHAR(100),
                 FAX           VARCHAR(20),
                 MODIFIED      TIMESTAMP,
                 NAME          VARCHAR(250) NOT NULL UNIQUE,
                 PHONE         VARCHAR(50),
                 POSTAL_CODE   VARCHAR(20),
                 URL           VARCHAR(250),
                 VAT_ID_NUMBER VARCHAR(50),
                 CONSTRAINT FK_COMPANY_CITY FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID) ON DELETE CASCADE
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGINT       NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                COUNTRY_MAP   BLOB,
                CREATED       TIMESTAMP    NOT NULL,
                ISO3166       VARCHAR(2),
                MODIFIED      TIMESTAMP,
                NAME          VARCHAR(100) NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGINT       NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                FK_COUNTRY_ID       BIGINT       NOT NULL,
                FK_TIMEZONE_ID      BIGINT       NOT NULL,
                COUNTRY_STATE_MAP   BLOB,
                CREATED             TIMESTAMP    NOT NULL,
                MODIFIED            TIMESTAMP,
                NAME                VARCHAR(100) NOT NULL,
                SYMBOL              VARCHAR(10),
                CONSTRAINT FK_COUNTRY_STATE_COUNTRY  FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID)  ON DELETE CASCADE,
                CONSTRAINT FK_COUNTRY_STATE_TIMEZONE FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID) ON DELETE CASCADE,
                CONSTRAINT UQ_COUNTRY_STATE          UNIQUE (FK_COUNTRY_ID,NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID BIGINT        NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                ABBREVIATION   VARCHAR(20)   NOT NULL,
                CREATED        TIMESTAMP     NOT NULL,
                MODIFIED       TIMESTAMP,
                NAME           VARCHAR(100)  NOT NULL UNIQUE,
                V_TIME_ZONE    VARCHAR(4000)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }

  @Override
  protected void resetAndCreateDatabase() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName)
        + " - Start - database table \" + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName) + \" - \"\n"
        + "        + String.format(DatabaseSeeder.FORMAT_ROW_NO, rowCount) + \" rows to be created");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(url);

    // -----------------------------------------------------------------------
    // Drop the database user and tables if already existing.
    // -----------------------------------------------------------------------

    String ibmdb2Schema = config.getIbmdb2Schema();

    dropAllTables(url, dropTableStmnt, config.getIbmdb2Schema());

    try {
      statement = connection.createStatement();
      statement.execute("DROP SCHEMA " + ibmdb2Schema + " RESTRICT");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create the database user and grant the necessary rights.
    // -----------------------------------------------------------------------

    try {
      statement.execute("CREATE SCHEMA " + ibmdb2Schema);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - End");
  }
}
