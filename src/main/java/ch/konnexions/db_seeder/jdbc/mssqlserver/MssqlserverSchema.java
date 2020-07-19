/**
 * 
 */
package ch.konnexions.db_seeder.jdbc.mssqlserver;

import java.util.HashMap;

import ch.konnexions.db_seeder.jdbc.JdbcSchema;

/**
 * CREATE TABLE statements for a Microsoft SQL Server DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class MssqlserverSchema implements JdbcSchema {

  protected final static HashMap<String, String> createTableStmnts = createTableStmnts();

  /**
   * Creates the CREATE TABLE statements.
   */
  @SuppressWarnings("preview")
  private final static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<String, String>();

    statements.put(TABLE_NAME_CITY,
                   """
                   CREATE TABLE CITY (
                        PK_CITY_ID          BIGINT         NOT NULL PRIMARY KEY,
                        FK_COUNTRY_STATE_ID BIGINT,
                        CITY_MAP            VARBINARY(MAX),
                        CREATED             DATETIME2      NOT NULL,
                        MODIFIED            DATETIME2,
                        NAME                VARCHAR(100)   NOT NULL,
                        CONSTRAINT FK_CITY_COUNTRY_STATE FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
                   )
                   """);

    statements.put(TABLE_NAME_COMPANY,
                   """
                   CREATE TABLE COMPANY (
                        PK_COMPANY_ID BIGINT       NOT NULL PRIMARY KEY,
                        FK_CITY_ID    BIGINT       NOT NULL,
                        ACTIVE        VARCHAR(1)   NOT NULL,
                        ADDRESS1      VARCHAR(50),
                        ADDRESS2      VARCHAR(50),
                        ADDRESS3      VARCHAR(50),
                        CREATED       DATETIME2    NOT NULL,
                        DIRECTIONS    VARCHAR(MAX),
                        EMAIL         VARCHAR(100),
                        FAX           VARCHAR(50),
                        MODIFIED      DATETIME2,
                        NAME          VARCHAR(250) NOT NULL UNIQUE,
                        PHONE         VARCHAR(50),
                        POSTAL_CODE   VARCHAR(50),
                        URL           VARCHAR(250),
                        VAT_ID_NUMBER VARCHAR(100),
                        CONSTRAINT FK_COMPANY_CITY FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID)
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY,
                   """
                   CREATE TABLE COUNTRY (
                       PK_COUNTRY_ID BIGINT         NOT NULL PRIMARY KEY,
                       COUNTRY_MAP   VARBINARY(MAX),
                       CREATED       DATETIME2      NOT NULL,
                       ISO3166       VARCHAR(50),
                       MODIFIED      DATETIME2,
                       NAME          VARCHAR(100)   NOT NULL UNIQUE
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY_STATE,
                   """
                   CREATE TABLE COUNTRY_STATE (
                       PK_COUNTRY_STATE_ID BIGINT         NOT NULL PRIMARY KEY,
                       FK_COUNTRY_ID       BIGINT         NOT NULL,
                       FK_TIMEZONE_ID      BIGINT         NOT NULL,
                       COUNTRY_STATE_MAP   VARBINARY(MAX),
                       CREATED             DATETIME2      NOT NULL,
                       MODIFIED            DATETIME2,
                       NAME                VARCHAR(100)   NOT NULL,
                       SYMBOL              VARCHAR(50),
                       CONSTRAINT FK_COUNTRY_STATE_COUNTRY  FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID),
                       CONSTRAINT FK_COUNTRY_STATE_TIMEZONE FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID),
                       CONSTRAINT UQ_COUNTRY_STATE          UNIQUE      (FK_COUNTRY_ID,NAME)
                   )
                   """);

    statements.put(TABLE_NAME_TIMEZONE,
                   """
                   CREATE TABLE TIMEZONE (
                       PK_TIMEZONE_ID BIGINT        NOT NULL PRIMARY KEY,
                       ABBREVIATION   VARCHAR(50)   NOT NULL,
                       CREATED        DATETIME2     NOT NULL,
                       MODIFIED       DATETIME2,
                       NAME           VARCHAR(100)  NOT NULL UNIQUE,
                       V_TIME_ZONE    VARCHAR(4000)
                   )
                   """);

    return statements;
  }

}
