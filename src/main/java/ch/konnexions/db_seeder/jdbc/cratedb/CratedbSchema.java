/**
 * 
 */
package ch.konnexions.db_seeder.jdbc.cratedb;

import java.util.HashMap;

import ch.konnexions.db_seeder.jdbc.JdbcSchema;

/**
 * CREATE TABLE statements for a CrateDB.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class CratedbSchema implements JdbcSchema {

  protected final static HashMap<String, String> databaseTables = new HashMap<String, String>();

  /**
   * Instantiates a new derby schema.
   */
  @SuppressWarnings("preview") public CratedbSchema() {
    super();

    databaseTables.put(TABLE_NAME_CITY,
                       """
                       CREATE TABLE CITY (
                           PK_CITY_ID          BIGINT    NOT NULL PRIMARY KEY,
                           FK_COUNTRY_STATE_ID BIGINT,
                           CITY_MAP            OBJECT,
                           CREATED             TIMESTAMP NOT NULL,
                           MODIFIED            TIMESTAMP,
                           NAME                TEXT      NOT NULL
                                  )""");

    databaseTables.put(TABLE_NAME_COMPANY,
                       """
                       CREATE TABLE COMPANY (
                           PK_COMPANY_ID BIGINT    NOT NULL PRIMARY KEY,
                           FK_CITY_ID    BIGINT    NOT NULL,
                           ACTIVE        TEXT      NOT NULL,
                           ADDRESS1      TEXT,
                           ADDRESS2      TEXT,
                           ADDRESS3      TEXT,
                           CREATED       TIMESTAMP NOT NULL,
                           DIRECTIONS    TEXT,
                           EMAIL         TEXT,
                           FAX           TEXT,
                           MODIFIED      TIMESTAMP,
                           NAME          TEXT      NOT NULL,
                           PHONE         TEXT,
                           POSTAL_CODE   TEXT,
                           URL           TEXT,
                           VAT_ID_NUMBER TEXT
                                 )""");

    databaseTables.put(TABLE_NAME_COUNTRY,
                       """
                       CREATE TABLE COUNTRY (
                          PK_COUNTRY_ID BIGINT    NOT NULL PRIMARY KEY,
                          COUNTRY_MAP   OBJECT,
                          CREATED       TIMESTAMP NOT NULL,
                          ISO3166       TEXT,
                          MODIFIED      TIMESTAMP,
                          NAME          TEXT      NOT NULL
                                 )""");

    databaseTables.put(TABLE_NAME_COUNTRY_STATE,
                       """
                       CREATE TABLE COUNTRY_STATE (
                          PK_COUNTRY_STATE_ID BIGINT    NOT NULL PRIMARY KEY,
                          FK_COUNTRY_ID       BIGINT    NOT NULL,
                          FK_TIMEZONE_ID      BIGINT    NOT NULL,
                          COUNTRY_STATE_MAP   OBJECT,
                          CREATED             TIMESTAMP NOT NULL,
                          MODIFIED            TIMESTAMP,
                          NAME                TEXT      NOT NULL,
                          SYMBOL              TEXT
                                 )""");

    databaseTables.put(TABLE_NAME_TIMEZONE,
                       """
                       CREATE TABLE TIMEZONE (
                          PK_TIMEZONE_ID BIGINT     NOT NULL PRIMARY KEY,
                          ABBREVIATION   TEXT       NOT NULL,
                          CREATED        TIMESTAMP  NOT NULL,
                          MODIFIED       TIMESTAMP,
                          NAME           TEXT       NOT NULL,
                          V_TIME_ZONE    TEXT
                                 )""");
  }

}
