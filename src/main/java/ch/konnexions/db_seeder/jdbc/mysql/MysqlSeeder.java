/**
 *
 */
package ch.konnexions.db_seeder.jdbc.mysql;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a MySQL DBMS. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class MysqlSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(MysqlSeeder.class);

  /**
   * 
   */
  public MysqlSeeder() {
    super();

    dbms     = Dbms.MYSQL;

    driver   = "com.mysql.cj.jdbc.Driver";

    urlBase  = config.getMysqlConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getMysqlConnectionPort() + "/";

    url      = urlBase + config.getMysqlDatabase() + config.getMysqlConnectionSuffix();
    urlSetup = urlBase + "sys" + config.getMysqlConnectionSuffix();
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE `CITY` (
                 `PK_CITY_ID`          BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
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
                 `PK_COMPANY_ID` BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
                 `FK_CITY_ID`    BIGINT       NOT NULL,
                 `ACTIVE`        VARCHAR(1)   NOT NULL,
                 `ADDRESS1`      VARCHAR(50),
                 `ADDRESS2`      VARCHAR(50),
                 `ADDRESS3`      VARCHAR(50),
                 `CREATED`       DATETIME     NOT NULL,
                 `DIRECTIONS`    LONGTEXT,
                 `EMAIL`         VARCHAR(100),
                 `FAX`           VARCHAR(20),
                 `MODIFIED`      DATETIME,
                 `NAME`          VARCHAR(250) NOT NULL UNIQUE,
                 `PHONE`         VARCHAR(50),
                 `POSTAL_CODE`   VARCHAR(20),
                 `URL`           VARCHAR(250),
                 `VAT_ID_NUMBER` VARCHAR(50),
                 CONSTRAINT `FK_COMPANY_CITY` FOREIGN KEY `FK_COMPANY_CITY` (`FK_CITY_ID`) REFERENCES `CITY` (`PK_CITY_ID`)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE `COUNTRY` (
                `PK_COUNTRY_ID` BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
                `COUNTRY_MAP`   LONGBLOB,
                `CREATED`       DATETIME     NOT NULL,
                `ISO3166`       VARCHAR(2),
                `MODIFIED`      DATETIME,
                `NAME`          VARCHAR(100) NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE `COUNTRY_STATE` (
                `PK_COUNTRY_STATE_ID` BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
                `FK_COUNTRY_ID`       BIGINT       NOT NULL,
                `FK_TIMEZONE_ID`      BIGINT       NOT NULL,
                `COUNTRY_STATE_MAP`   LONGBLOB,
                `CREATED`             DATETIME     NOT NULL,
                `MODIFIED`            DATETIME,
                `NAME`                VARCHAR(100) NOT NULL,
                `SYMBOL`              VARCHAR(10),
                CONSTRAINT `FK_COUNTRY_STATE_COUNTRY`  FOREIGN KEY `FK_COUNTRY_STATE_COUNTRY`  (`FK_COUNTRY_ID`)  REFERENCES `COUNTRY`  (`PK_COUNTRY_ID`),
                CONSTRAINT `FK_COUNTRY_STATE_TIMEZONE` FOREIGN KEY `FK_COUNTRY_STATE_TIMEZONE` (`FK_TIMEZONE_ID`) REFERENCES `TIMEZONE` (`PK_TIMEZONE_ID`),
                CONSTRAINT `UQ_COUNTRY_STATE`          UNIQUE (`FK_COUNTRY_ID`,`NAME`)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE `TIMEZONE` (
                `PK_TIMEZONE_ID` BIGINT        NOT NULL PRIMARY KEY AUTO_INCREMENT,
                `ABBREVIATION`   VARCHAR(20)   NOT NULL,
                `CREATED`        DATETIME      NOT NULL,
                `MODIFIED`       DATETIME,
                `NAME`           VARCHAR(100)  NOT NULL UNIQUE,
                `V_TIME_ZONE`    VARCHAR(4000)
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

    connection = connect(urlSetup, driver, "root", config.getMysqlPasswordSys());

    // -----------------------------------------------------------------------
    // Drop the database and the database user if already existing.
    // -----------------------------------------------------------------------

    String mysqlDatabase = config.getMysqlDatabase();
    String mysqlUser     = config.getMysqlUser();

    try {
      statement = connection.createStatement();

      statement.execute("DROP DATABASE IF EXISTS `" + mysqlDatabase + "`");

      statement.execute("DROP USER IF EXISTS `" + mysqlUser + "`");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create the user and grant the necessary rights.
    // -----------------------------------------------------------------------

    try {
      statement.execute("CREATE DATABASE `" + mysqlDatabase + "`");

      statement.execute("USE `" + mysqlDatabase + "`");

      statement.execute("USE `" + mysqlDatabase + "`");

      statement.execute("CREATE USER `" + mysqlUser + "` IDENTIFIED BY '" + config.getMysqlPassword() + "'");

      statement.execute("GRANT ALL ON " + mysqlDatabase + ".* TO `" + mysqlUser + "`");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(url, null, config.getMysqlUser(), config.getMysqlPassword());

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - End");
  }
}
