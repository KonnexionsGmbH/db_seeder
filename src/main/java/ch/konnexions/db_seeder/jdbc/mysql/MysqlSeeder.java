/**
 *
 */
package ch.konnexions.db_seeder.jdbc.mysql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialClob;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.Config;
import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a Database. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @version 1.0.0
 * @since   2020-05-01
 */
public class MysqlSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(MysqlSeeder.class);

  /**
   * 
   */
  public MysqlSeeder() {
    super();
  }

  /**
   * Create a database connection.
   */
  @Override
  protected final void connect() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - Start");

    try {
      connection = DriverManager.getConnection(config.getMysqlConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getMysqlConnectionPort() + "/"
          + config.getMysqlConnectionDatabase() + config.getMysqlConnectionSuffix(), config.getMysqlUser(), config.getMysqlPassword());

      connection.setAutoCommit(false);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - End");
  }

  /**
   * Create the DDL statement: CREATE TABLE.
   *
   * @param tableName the database table name
   *
   * @return the insert statement
   */
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
CREATE TABLE `CITY` (
    `PK_CITY_ID`          BIGINT       NOT NULL AUTO_INCREMENT,
    `FK_COUNTRY_STATE_ID` BIGINT       NULL,
    `CITY_MAP`            LONGBLOB     NULL,
    `CREATED`             DATETIME     NOT NULL,
    `MODIFIED`            DATETIME     NULL,
    `NAME`                VARCHAR(100) NOT NULL,
    CONSTRAINT `PK_CITY`               PRIMARY KEY (`PK_CITY_ID`),
    CONSTRAINT `FK_CITY_COUNTRY_STATE` FOREIGN KEY `FK_CITY_COUNTRY_STATE` (`FK_COUNTRY_STATE_ID`) REFERENCES `COUNTRY_STATE` (`PK_COUNTRY_STATE_ID`)
 ) ENGINE = INNODB CHARSET = UTF8MB4""";
    case TABLE_NAME_COMPANY:
      return """
CREATE TABLE `COMPANY` (
    `PK_COMPANY_ID` BIGINT       NOT NULL AUTO_INCREMENT,
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
    `NAME`          VARCHAR(250) NOT NULL,
    `PHONE`         VARCHAR(50)  NULL,
    `POSTAL_CODE`   VARCHAR(20)  NULL,
    `URL`           VARCHAR(250) NULL,
    `VAT_ID_NUMBER` VARCHAR(50)  NULL,
    CONSTRAINT `PK_COMPANY`      PRIMARY KEY (`PK_COMPANY_ID`),
    CONSTRAINT `FK_COMPANY_CITY` FOREIGN KEY `FK_COMPANY_CITY` (`FK_CITY_ID`) REFERENCES `CITY` (`PK_CITY_ID`),
    CONSTRAINT `UQ_COMPANY_NAME` UNIQUE (`NAME`)
) ENGINE = INNODB CHARSET = UTF8MB4""";
    case TABLE_NAME_COUNTRY:
      return """
CREATE TABLE `COUNTRY` (
   `PK_COUNTRY_ID` BIGINT       NOT NULL AUTO_INCREMENT,
   `COUNTRY_MAP`   LONGBLOB     NULL,
   `CREATED`       DATETIME     NOT NULL,
   `ISO3166`       VARCHAR(2)   NULL,
   `MODIFIED`      DATETIME     NULL,
   `NAME`          VARCHAR(100) NOT NULL,
   CONSTRAINT `PK_COUNTRY`       PRIMARY KEY (`PK_COUNTRY_ID`),
   CONSTRAINT `UQ_COUNTRY_NAME`  UNIQUE (`NAME`)
) ENGINE = INNODB CHARSET = UTF8MB4""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
CREATE TABLE `COUNTRY_STATE` (
   `PK_COUNTRY_STATE_ID` BIGINT       NOT NULL AUTO_INCREMENT,
   `FK_COUNTRY_ID`       BIGINT       NOT NULL,
   `FK_TIMEZONE_ID`      BIGINT       NOT NULL,
   `COUNTRY_STATE_MAP`   LONGBLOB     NULL,
   `CREATED`             DATETIME     NOT NULL,
   `MODIFIED`            DATETIME     NULL,
   `NAME`                VARCHAR(100) NOT NULL,
   `SYMBOL`              VARCHAR(10)  NULL,
   CONSTRAINT `PK_COUNTRY_STATE`          PRIMARY KEY (`PK_COUNTRY_STATE_ID`),
   CONSTRAINT `FK_COUNTRY_STATE_COUNTRY`  FOREIGN KEY `FK_COUNTRY_STATE_COUNTRY`  (`FK_COUNTRY_ID`)  REFERENCES `COUNTRY`  (`PK_COUNTRY_ID`),
   CONSTRAINT `FK_COUNTRY_STATE_TIMEZONE` FOREIGN KEY `FK_COUNTRY_STATE_TIMEZONE` (`FK_TIMEZONE_ID`) REFERENCES `TIMEZONE` (`PK_TIMEZONE_ID`),
   CONSTRAINT `UQ_COUNTRY_STATE`          UNIQUE (`FK_COUNTRY_ID`,`NAME`)
) ENGINE = INNODB CHARSET = UTF8MB4""";
    case TABLE_NAME_TIMEZONE:
      return """
CREATE TABLE `TIMEZONE` (
   `PK_TIMEZONE_ID` BIGINT        NOT NULL AUTO_INCREMENT,
   `ABBREVIATION`   VARCHAR(20)   NOT NULL,
   `CREATED`        DATETIME      NOT NULL,
   `MODIFIED`       DATETIME      NULL,
   `NAME`           VARCHAR(100)  NOT NULL,
   `V_TIME_ZONE`    VARCHAR(4000) NULL,
   CONSTRAINT `PK_TIMEZONE`             PRIMARY KEY (`PK_TIMEZONE_ID`),
   CONSTRAINT `UQ_TIMEZONE_UQ_TIMEZONE` UNIQUE (`NAME`)
) ENGINE = INNODB CHARSET = UTF8MB4""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }

  /**
   * Drop the schema / user if existing and create it new.
   */
  @Override
  protected void dropCreateSchemaUser() {
    PreparedStatement preparedStatement       = null;

    // -----------------------------------------------------------------------
    // Connect as privileged user
    // -----------------------------------------------------------------------

    final String      jdbcUser                = config.getMysqlUser();
    final String      mysqlConnectionDatabase = config.getMysqlConnectionDatabase();

    try {
      connection = DriverManager.getConnection(config.getMysqlConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getMysqlConnectionPort() + "/"
          + "sys" + config.getMysqlConnectionSuffix(), config.getMysqlUserSys(), config.getMysqlPasswordSys());

      connection.setAutoCommit(false);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Drop the database and user if already existing
    // -----------------------------------------------------------------------

    try {
      preparedStatement = connection.prepareStatement("DROP DATABASE IF EXISTS `" + mysqlConnectionDatabase + "`");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("DROP USER IF EXISTS `" + jdbcUser + "`");
      preparedStatement.executeUpdate();

      // -----------------------------------------------------------------------
      // Create the schema / user and grant the necessary rights.
      // -----------------------------------------------------------------------

      preparedStatement = connection.prepareStatement("CREATE DATABASE `" + mysqlConnectionDatabase + "`");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("USE `" + mysqlConnectionDatabase + "`");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("CREATE USER `" + jdbcUser + "` IDENTIFIED BY '" + config.getMysqlPassword() + "'");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT ALL ON " + mysqlConnectionDatabase + ".* TO `" + jdbcUser + "`");
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

  /**
   * Prepare the variable part of the INSERT statement - CITY.
   *
   * PK_CITY_ID NUMBER   NUMBER     PK
   * FK_COUNTRY_STATE_ID NUMBER     FK NULLABLE
   * CITY_MAP            BLOB          NULLABLE
   * CREATED             TIMESTAMP    
   * MODIFIED            TIMESTAMP     NULLABLE
   * NAME 	             VARCHAR   100
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   * @throws IOException 
   */
  @Override
  protected final void prepDmlStmntInsertCity(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      prepStmntInsertColFKOpt(1, pkListCountryState, preparedStatement, rowCount);
      prepStmntInsertColBlobOpt(2, preparedStatement, rowCount);
      preparedStatement.setTimestamp(3, getRandomTimestamp());
      preparedStatement.setTimestamp(4, getRandomTimestamp());
      preparedStatement.setString(5, "NAME_" + identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - COMPANY.
   *
   * PK_COMPANY_ID         NUMBER     PK
   * FK_CITY_ID            NUMBER     FK
   * ACTIVE                VARCHAR     1
   * ADDRESS1              VARCHAR    50 NULLABLE
   * ADDRESS2              VARCHAR    50 NULLABLE
   * ADDRESS3              VARCHAR    50 NULLABLE
   * CREATED               TIMESTAMP    
   * DIRECTIONS            CLOB          NULLABLE
   * EMAIL                 VARCHAR   100 NULLABLE
   * FAX                   VARCHAR    20 NULLABLE
   * MODIFIED              TIMESTAMP     NULLABLE
   * NAME 	               VARCHAR   250
   * PHONE                 VARCHAR    50 NULLABLE
   * POSTAL_CODE           VARCHAR    20 NULLABLE
   * URL                   VARCHAR   250 NULLABLE
   * VAT_ID_NUMBER         VARCHAR    50 NULLABLE
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  @Override
  protected final void prepDmlStmntInsertCompany(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      preparedStatement.setObject(1, pkListCity.get(getRandomIntExcluded(pkListCity.size())));
      prepStmntInsertColFlagNY(2, preparedStatement, rowCount);
      prepStmntInsertColStringOpt(3, "ADDRESS1_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(4, "ADDRESS2_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(5, "ADDRESS3_", preparedStatement, rowCount, identifier04);
      preparedStatement.setTimestamp(6, getRandomTimestamp());
      prepStmntInsertColClobOpt(7, preparedStatement, rowCount);
      prepStmntInsertColStringOpt(8, "EMAIL_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(9, "FAX_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColDatetimeOpt(10, preparedStatement, rowCount);
      preparedStatement.setString(11, "NAME_" + identifier04);
      prepStmntInsertColStringOpt(12, "PHONE_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(13, "POSTAL_CODE_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(14, "URL_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(15, "VAT_ID_NUMBER__", preparedStatement, rowCount, identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - COUNTRY.
   *
   * PK_COUNTRY_ID NUMBER    PK
   * COUNTRY_MAP   BLOB          NULLABLE
   * CREATED       TIMESTAMP    
   * ISO3166       VARCHAR     2 NULLABLE
   * MODIFIED      TIMESTAMP     NULLABLE
   * NAME 	       VARCHAR   100
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  protected final void prepDmlStmntInsertCountry(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      prepStmntInsertColBlobOpt(1, preparedStatement, rowCount);
      preparedStatement.setTimestamp(2, getRandomTimestamp());
      prepStmntInsertColStringOpt(3, "", preparedStatement, rowCount, identifier04.substring(2));
      prepStmntInsertColDatetimeOpt(4, preparedStatement, rowCount);
      preparedStatement.setString(5, "NAME_" + identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - COUNTRY_STATE.
   *
   * PK_COUNTRY_STATE_ID NUMBER   PK
   * FK_COUNTRY_ID       NUMBER   FK
   * FK_TIMEZONE_ID      NUMBER   FK
   * COUNTRY_STATE_MAP   BLOB        NULLABLE
   * NAME 	             VARCHAR 100
   * SYMBOL              VARCHAR  10 NULLABLE
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  @Override
  protected final void prepDmlStmntInsertCountryState(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      preparedStatement.setObject(1, pkListCountry.get(getRandomIntExcluded(pkListCountry.size())));
      preparedStatement.setObject(2, pkListTimezone.get(getRandomIntExcluded(pkListTimezone.size())));
      prepStmntInsertColBlobOpt(3, preparedStatement, rowCount);
      preparedStatement.setTimestamp(4, getRandomTimestamp());
      prepStmntInsertColDatetimeOpt(5, preparedStatement, rowCount);
      preparedStatement.setString(6, "NAME_" + identifier04);
      prepStmntInsertColStringOpt(7, "SYMBOL_", preparedStatement, rowCount, identifier04.substring(1));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - TIMEZONE.
   *
   * PK_TIMEZONE_ID NUMBER      PK
   * ABBREVIATION   VARCHAR     20
   * CREATED        TIMESTAMP    
   * MODIFIED       TIMESTAMP      NULLABLE
   * NAME 	        VARCHAR    100
   * V_TIME_ZONE    VARCHAR   4000 NULLABLE
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  @Override
  protected final void prepDmlStmntInsertTimezone(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      preparedStatement.setString(1, "ABBREVIATION_" + identifier04);
      preparedStatement.setTimestamp(2, getRandomTimestamp());
      preparedStatement.setTimestamp(3, getRandomTimestamp());
      preparedStatement.setString(4, "NAME_" + identifier04);
      prepStmntInsertColStringOpt(5, "V_TIME_ZONE_", preparedStatement, rowCount, identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

}
