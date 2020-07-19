/**
 * 
 */
package ch.konnexions.db_seeder.jdbc.mariadb;

import java.util.HashMap;

import ch.konnexions.db_seeder.jdbc.JdbcSchema;

/**
 * CREATE TABLE statements for a MariaDB DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class MariadbSchema implements JdbcSchema {

  protected final static HashMap<String, String> databaseTables = new HashMap<String, String>();

  /**
   * Instantiates a new derby schema.
   */
  @SuppressWarnings("preview") public MariadbSchema() {
    super();

    databaseTables.put(TABLE_NAME_CITY,
                       """
                       CREATE TABLE `CITY` (
                            `PK_CITY_ID`          BIGINT       NOT NULL PRIMARY KEY,
                            `FK_COUNTRY_STATE_ID` BIGINT,
                            `CITY_MAP`            LONGBLOB,
                            `CREATED`             DATETIME     NOT NULL,
                            `MODIFIED`            DATETIME,
                            `NAME`                VARCHAR(100) NOT NULL,
                            CONSTRAINT `FK_CITY_COUNTRY_STATE` FOREIGN KEY `FK_CITY_COUNTRY_STATE` (`FK_COUNTRY_STATE_ID`) REFERENCES `COUNTRY_STATE` (`PK_COUNTRY_STATE_ID`)
                                    )""");

    databaseTables.put(TABLE_NAME_COMPANY,
                       """
                       CREATE TABLE `COMPANY` (
                           `PK_COMPANY_ID` BIGINT       NOT NULL PRIMARY KEY,
                           `FK_CITY_ID`    BIGINT       NOT NULL,
                           `ACTIVE`        VARCHAR(1)   NOT NULL,
                           `ADDRESS1`      VARCHAR(50),
                           `ADDRESS2`      VARCHAR(50),
                           `ADDRESS3`      VARCHAR(50),
                           `CREATED`       DATETIME     NOT NULL,
                           `DIRECTIONS`    LONGTEXT,
                           `EMAIL`         VARCHAR(100),
                           `FAX`           VARCHAR(50),
                           `MODIFIED`      DATETIME,
                           `NAME`          VARCHAR(250) NOT NULL UNIQUE,
                           `PHONE`         VARCHAR(50),
                           `POSTAL_CODE`   VARCHAR(50),
                           `URL`           VARCHAR(250),
                           `VAT_ID_NUMBER` VARCHAR(100),
                           CONSTRAINT `FK_COMPANY_CITY` FOREIGN KEY `FK_COMPANY_CITY` (`FK_CITY_ID`) REFERENCES `CITY` (`PK_CITY_ID`)
                                 )""");

    databaseTables.put(TABLE_NAME_COUNTRY,
                       """
                       CREATE TABLE `COUNTRY` (
                          `PK_COUNTRY_ID` BIGINT       NOT NULL PRIMARY KEY,
                          `COUNTRY_MAP`   LONGBLOB,
                          `CREATED`       DATETIME     NOT NULL,
                          `ISO3166`       VARCHAR(50),
                          `MODIFIED`      DATETIME,
                          `NAME`          VARCHAR(100) NOT NULL UNIQUE
                                 )""");

    databaseTables.put(TABLE_NAME_COUNTRY_STATE,
                       """
                       CREATE TABLE `COUNTRY_STATE` (
                          `PK_COUNTRY_STATE_ID` BIGINT       NOT NULL PRIMARY KEY,
                          `FK_COUNTRY_ID`       BIGINT       NOT NULL,
                          `FK_TIMEZONE_ID`      BIGINT       NOT NULL,
                          `COUNTRY_STATE_MAP`   LONGBLOB,
                          `CREATED`             DATETIME     NOT NULL,
                          `MODIFIED`            DATETIME,
                          `NAME`                VARCHAR(100) NOT NULL,
                          `SYMBOL`              VARCHAR(50),
                          CONSTRAINT `FK_COUNTRY_STATE_COUNTRY`  FOREIGN KEY `FK_COUNTRY_STATE_COUNTRY`  (`FK_COUNTRY_ID`)  REFERENCES `COUNTRY`  (`PK_COUNTRY_ID`),
                          CONSTRAINT `FK_COUNTRY_STATE_TIMEZONE` FOREIGN KEY `FK_COUNTRY_STATE_TIMEZONE` (`FK_TIMEZONE_ID`) REFERENCES `TIMEZONE` (`PK_TIMEZONE_ID`),
                          CONSTRAINT `UQ_COUNTRY_STATE`          UNIQUE (`FK_COUNTRY_ID`,`NAME`)
                                 )""");

    databaseTables.put(TABLE_NAME_TIMEZONE,
                       """
                       CREATE TABLE `TIMEZONE` (
                          `PK_TIMEZONE_ID` BIGINT        NOT NULL PRIMARY KEY,
                          `ABBREVIATION`   VARCHAR(50)   NOT NULL,
                          `CREATED`        DATETIME      NOT NULL,
                          `MODIFIED`       DATETIME,
                          `NAME`           VARCHAR(100)  NOT NULL UNIQUE,
                          `V_TIME_ZONE`    VARCHAR(4000)
                                 )""");
  }

}
