/**
 * 
 */
package ch.konnexions.db_seeder.generated;

import java.util.HashMap;

/**
 * CREATE TABLE statements for a Mimer DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class MimerSchema extends AbstractSchema {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  /**
   * Creates the CREATE TABLE statements.
   */
  @SuppressWarnings("preview")
  private static final HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<String, String>();

    statements.put(TABLE_NAME_CITY,
                   """
                   CREATE TABLE CITY (
                       PK_CITY_ID          BIGINT        NOT NULL PRIMARY KEY,
                       FK_COUNTRY_STATE_ID BIGINT,
                       CITY_MAP            BLOB,
                       CREATED             TIMESTAMP     NOT NULL,
                       MODIFIED            TIMESTAMP,
                       NAME                NVARCHAR(100) NOT NULL,
                       FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
                   )
                   """);

    statements.put(TABLE_NAME_COMPANY,
                   """
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
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY,
                   """
                   CREATE TABLE COUNTRY (
                       PK_COUNTRY_ID BIGINT        NOT NULL PRIMARY KEY,
                       COUNTRY_MAP   BLOB,
                       CREATED       TIMESTAMP     NOT NULL,
                       ISO3166       NVARCHAR(50),
                       MODIFIED      TIMESTAMP,
                       NAME          NVARCHAR(100) NOT NULL UNIQUE
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY_STATE,
                   """
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
                   )
                   """);

    statements.put(TABLE_NAME_TIMEZONE,
                   """
                   CREATE TABLE TIMEZONE (
                       PK_TIMEZONE_ID BIGINT         NOT NULL PRIMARY KEY,
                       ABBREVIATION   NVARCHAR(50)   NOT NULL,
                       CREATED        TIMESTAMP      NOT NULL,
                       MODIFIED       TIMESTAMP,
                       NAME           NVARCHAR(100)  NOT NULL UNIQUE,
                       V_TIME_ZONE    NVARCHAR(4000)
                   )
                   """);

    return statements;
  }

}
