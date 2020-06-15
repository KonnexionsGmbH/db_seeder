/**
 *
 */
package ch.konnexions.db_seeder.jdbc.mariadb;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a MariaDB Server. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class MariadbSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(MariadbSeeder.class);

  /**
   * 
   */
  public MariadbSeeder() {
    super();

    databaseBrand = DatabaseBrand.MARIADB;
  }

  @Override
  protected final void connect() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - Start");

    try {
      connection = DriverManager.getConnection(config.getMariadbConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getMariadbConnectionPort()
          + "/" + config.getMariadbDatabase(), config.getMariadbUser(), config.getMariadbPassword());

      connection.setAutoCommit(false);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - End");
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE `CITY` (
                 `PK_CITY_ID`          BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
                 `FK_COUNTRY_STATE_ID` BIGINT       NULL,
                 `CITY_MAP`            LONGBLOB     NULL,
                 `CREATED`             DATETIME     NOT NULL,
                 `MODIFIED`            DATETIME     NULL,
                 `NAME`                VARCHAR(100) NOT NULL,
                 CONSTRAINT `FK_CITY_COUNTRY_STATE` FOREIGN KEY `FK_CITY_COUNTRY_STATE` (`FK_COUNTRY_STATE_ID`) REFERENCES `COUNTRY_STATE` (`PK_COUNTRY_STATE_ID`)
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE `COMPANY` (
                 `PK_COMPANY_ID` BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
                 `FK_CITY_ID`    BIGINT       NOT NULL,
                 `ACTIVE`        VARCHAR(1)   NOT NULL,
                 `ADDRESS1`      VARCHAR(50)  NULL,
                 `ADDRESS2`      VARCHAR(50)  NULL,
                 `ADDRESS3`      VARCHAR(50)  NULL,
                 `CREATED`       DATETIME     NOT NULL,
                 `DIRECTIONS`    LONGTEXT     NULL,
                 `EMAIL`         VARCHAR(100) NULL,
                 `FAX`           VARCHAR(20)  NULL,
                 `MODIFIED`      DATETIME     NULL,
                 `NAME`          VARCHAR(250) NOT NULL UNIQUE,
                 `PHONE`         VARCHAR(50)  NULL,
                 `POSTAL_CODE`   VARCHAR(20)  NULL,
                 `URL`           VARCHAR(250) NULL,
                 `VAT_ID_NUMBER` VARCHAR(50)  NULL,
                 CONSTRAINT `FK_COMPANY_CITY` FOREIGN KEY `FK_COMPANY_CITY` (`FK_CITY_ID`) REFERENCES `CITY` (`PK_CITY_ID`)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE `COUNTRY` (
                `PK_COUNTRY_ID` BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
                `COUNTRY_MAP`   LONGBLOB     NULL,
                `CREATED`       DATETIME     NOT NULL,
                `ISO3166`       VARCHAR(2)   NULL,
                `MODIFIED`      DATETIME     NULL,
                `NAME`          VARCHAR(100) NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE `COUNTRY_STATE` (
                `PK_COUNTRY_STATE_ID` BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
                `FK_COUNTRY_ID`       BIGINT       NOT NULL,
                `FK_TIMEZONE_ID`      BIGINT       NOT NULL,
                `COUNTRY_STATE_MAP`   LONGBLOB     NULL,
                `CREATED`             DATETIME     NOT NULL,
                `MODIFIED`            DATETIME     NULL,
                `NAME`                VARCHAR(100) NOT NULL,
                `SYMBOL`              VARCHAR(10)  NULL,
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
                `MODIFIED`       DATETIME      NULL,
                `NAME`           VARCHAR(100)  NOT NULL UNIQUE,
                `V_TIME_ZONE`    VARCHAR(4000) NULL
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }

  @Override
  protected void dropCreateSchemaUser() {
    PreparedStatement preparedStatement = null;

    // -----------------------------------------------------------------------
    // Connect as privileged user
    // -----------------------------------------------------------------------

    final String      mariadbDatabase   = config.getMariadbDatabase();
    final String      mariadbUser       = config.getMariadbUser();

    try {
      connection = DriverManager.getConnection(config.getMariadbConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getMariadbConnectionPort()
          + "/mysql", "root", config.getMariadbPasswordSys());

      connection.setAutoCommit(true);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Drop the database and user if already existing
    // -----------------------------------------------------------------------

    try {
      preparedStatement = connection.prepareStatement("DROP DATABASE IF EXISTS `" + mariadbDatabase + "`");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("DROP USER IF EXISTS '" + mariadbUser + "'");
      preparedStatement.executeUpdate();

      // -----------------------------------------------------------------------
      // Create the schema / user and grant the necessary rights.
      // -----------------------------------------------------------------------

      preparedStatement = connection.prepareStatement("CREATE DATABASE `" + mariadbDatabase + "`");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("USE `" + mariadbDatabase + "`");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("CREATE USER '" + mariadbUser + "'@'%' IDENTIFIED BY '" + config.getMariadbPassword() + "'");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT ALL PRIVILEGES ON *.* TO '" + mariadbUser + "'@'%'");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("FLUSH PRIVILEGES");
      preparedStatement.executeUpdate();

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect.
    // -----------------------------------------------------------------------

    disconnect();
    connect();
  }
}
