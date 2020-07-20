/**
 * 
 */
package ch.konnexions.db_seeder.generated;

import java.util.HashMap;

/**
 * CREATE TABLE statements for a Oracle DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class OracleSchema implements JdbcSchema {

  public final static HashMap<String, String> createTableStmnts = createTableStmnts();

  /**
   * Creates the CREATE TABLE statements.
   */
  @SuppressWarnings("preview")
  private final static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<String, String>();

    statements.put(TABLE_NAME_CITY,
                   """
                   CREATE TABLE CITY (
                       PK_CITY_ID          NUMBER         NOT NULL PRIMARY KEY,
                       FK_COUNTRY_STATE_ID NUMBER,
                       CITY_MAP            BLOB,
                       CREATED             TIMESTAMP      NOT NULL,
                       MODIFIED            TIMESTAMP,
                       NAME                VARCHAR2 (100) NOT NULL,
                       CONSTRAINT FK_CITY_COUNTRY_STATE   FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
                   )
                   """);

    statements.put(TABLE_NAME_COMPANY,
                   """
                   CREATE TABLE COMPANY (
                       PK_COMPANY_ID       NUMBER         NOT NULL PRIMARY KEY,
                       FK_CITY_ID          NUMBER         NOT NULL,
                       ACTIVE              VARCHAR2 (1)   NOT NULL,
                       ADDRESS1            VARCHAR2 (50),
                       ADDRESS2            VARCHAR2 (50),
                       ADDRESS3            VARCHAR2 (50),
                       CREATED             TIMESTAMP      NOT NULL,
                       DIRECTIONS          CLOB,
                       EMAIL               VARCHAR2 (100),
                       FAX                 VARCHAR2 (50),
                       MODIFIED            TIMESTAMP,
                       NAME                VARCHAR2 (250) NOT NULL UNIQUE,
                       PHONE               VARCHAR2 (50),
                       POSTAL_CODE         VARCHAR2 (50),
                       URL                 VARCHAR2 (250),
                       VAT_ID_NUMBER       VARCHAR2 (100),
                       CONSTRAINT FK_COMPANY_CITY         FOREIGN KEY (FK_CITY_ID)          REFERENCES CITY (PK_CITY_ID)
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY,
                   """
                   CREATE TABLE COUNTRY (
                       PK_COUNTRY_ID NUMBER         NOT NULL PRIMARY KEY,
                       COUNTRY_MAP   BLOB,
                       CREATED       TIMESTAMP      NOT NULL,
                       ISO3166       VARCHAR2 (50),
                       MODIFIED      TIMESTAMP,
                       NAME          VARCHAR2 (100) NOT NULL UNIQUE
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY_STATE,
                   """
                   CREATE TABLE COUNTRY_STATE (
                       PK_COUNTRY_STATE_ID NUMBER         NOT NULL PRIMARY KEY,
                       FK_COUNTRY_ID       NUMBER         NOT NULL,
                       FK_TIMEZONE_ID      NUMBER         NOT NULL,
                       COUNTRY_STATE_MAP   BLOB,
                       CREATED             TIMESTAMP      NOT NULL,
                       MODIFIED            TIMESTAMP,
                       NAME                VARCHAR2 (100) NOT NULL,
                       SYMBOL              VARCHAR2 (50),
                       CONSTRAINT FK_COUNTRY_STATE_COUNTRY  FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID),
                       CONSTRAINT FK_COUNTRY_STATE_TIMEZONE FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID),
                       CONSTRAINT UQ_COUNTRY_STATE          UNIQUE (FK_COUNTRY_ID, NAME)
                   )
                   """);

    statements.put(TABLE_NAME_TIMEZONE,
                   """
                   CREATE TABLE TIMEZONE (
                       PK_TIMEZONE_ID NUMBER          NOT NULL PRIMARY KEY,
                       ABBREVIATION   VARCHAR2 (50)   NOT NULL,
                       CREATED        TIMESTAMP       NOT NULL,
                       MODIFIED       TIMESTAMP,
                       NAME           VARCHAR2 (100)  NOT NULL UNIQUE,
                       V_TIME_ZONE    VARCHAR2 (4000)
                   )
                   """);

    return statements;
  }

}
