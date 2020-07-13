/**
 *
 */
package ch.konnexions.db_seeder.jdbc.mariadb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a MariaDB DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class MariadbSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(MariadbSeeder.class);

  /**
   * Instantiates a new MariaDB Server seeder.
   * @param args0 
   */
  public MariadbSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.MARIADB;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    tableNameDelimiter    = "`";

    urlBase               = config.getMariadbConnectionPrefix() + config.getMariadbConnectionHost() + ":" + config.getMariadbConnectionPort() + "/";
    url                   = urlBase + config.getMariadbDatabase();
    urlSetup              = urlBase + config.getMariadbDatabaseSys();

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
             CREATE TABLE `CITY` (
                 `PK_CITY_ID`          BIGINT       NOT NULL PRIMARY KEY,
                 `FK_COUNTRY_STATE_ID` BIGINT,
                 `CITY_MAP`            LONGBLOB,
                 `CREATED`             DATETIME     NOT NULL,
                 `MODIFIED`            DATETIME,
                 `NAME`                VARCHAR(100) NOT NULL,
                 CONSTRAINT `FK_CITY_COUNTRY_STATE` FOREIGN KEY `FK_CITY_COUNTRY_STATE` (`FK_COUNTRY_STATE_ID`) REFERENCES `COUNTRY_STATE` (`PK_COUNTRY_STATE_ID`)
              )""";
    case TABLE_NAME_COMPANY:
      return """
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
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE `COUNTRY` (
                `PK_COUNTRY_ID` BIGINT       NOT NULL PRIMARY KEY,
                `COUNTRY_MAP`   LONGBLOB,
                `CREATED`       DATETIME     NOT NULL,
                `ISO3166`       VARCHAR(50),
                `MODIFIED`      DATETIME,
                `NAME`          VARCHAR(100) NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
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
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE `TIMEZONE` (
                `PK_TIMEZONE_ID` BIGINT        NOT NULL PRIMARY KEY,
                `ABBREVIATION`   VARCHAR(50)   NOT NULL,
                `CREATED`        DATETIME      NOT NULL,
                `MODIFIED`       DATETIME,
                `NAME`           VARCHAR(100)  NOT NULL UNIQUE,
                `V_TIME_ZONE`    VARCHAR(4000)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
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

    connection = connect(urlSetup, null, config.getMariadbUserSys(), config.getMariadbPasswordSys());

    // -----------------------------------------------------------------------
    // Drop the database and the database user.
    // -----------------------------------------------------------------------

    String mariadbDatabase = config.getMariadbDatabase();
    String mariadbUser     = config.getMariadbUser();

    try {
      statement = connection.createStatement();

      statement.execute("DROP DATABASE IF EXISTS `" + mariadbDatabase + "`");

      statement.execute("DROP USER IF EXISTS '" + mariadbUser + "'");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create the database, the database user and grant the necessary rights.
    // -----------------------------------------------------------------------

    try {
      statement.execute("CREATE DATABASE `" + mariadbDatabase + "`");

      statement.execute("USE `" + mariadbDatabase + "`");

      statement.execute("CREATE USER '" + mariadbUser + "'@'%' IDENTIFIED BY '" + config.getMariadbPassword() + "'");

      statement.execute("GRANT ALL PRIVILEGES ON *.* TO '" + mariadbUser + "'@'%'");

      statement.execute("FLUSH PRIVILEGES");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(url, null, config.getMariadbUser(), config.getMariadbPassword());

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
    }
  }
}
