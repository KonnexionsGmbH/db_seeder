/**
 *
 */
package ch.konnexions.db_seeder.jdbc.mssqlserver;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a Microsoft SQL Server DBMS. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class MssqlserverSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(MssqlserverSeeder.class);

  /**
   * 
   */
  public MssqlserverSeeder() {
    super();

    databaseDbms = DatabaseDbms.MSSQLSERVER;
  }

  @Override
  protected final void connect() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - Start");

    try {
      connection = DriverManager
          .getConnection(config.getMssqlserverConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getMssqlserverConnectionPort()
              + ";databaseName=" + config.getMssqlserverDatabase() + ";user=" + config.getMssqlserverUser() + ";password=" + config.getMssqlserverPassword());

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
             CREATE TABLE CITY (
                 PK_CITY_ID          BIGINT         NOT NULL PRIMARY KEY IDENTITY (1,1),
                 FK_COUNTRY_STATE_ID BIGINT         NULL,
                 CITY_MAP            VARBINARY(MAX) NULL,
                 CREATED             DATETIME2      NOT NULL,
                 MODIFIED            DATETIME2      NULL,
                 NAME                VARCHAR(100)   NOT NULL,
                 CONSTRAINT FK_CITY_COUNTRY_STATE FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGINT       NOT NULL PRIMARY KEY IDENTITY (1,1),
                 FK_CITY_ID    BIGINT       NOT NULL,
                 ACTIVE        VARCHAR(1)   NOT NULL,
                 ADDRESS1      VARCHAR(50)  NULL,
                 ADDRESS2      VARCHAR(50)  NULL,
                 ADDRESS3      VARCHAR(50)  NULL,
                 CREATED       DATETIME2    NOT NULL,
                 DIRECTIONS    VARCHAR(MAX) NULL,
                 EMAIL         VARCHAR(100) NULL,
                 FAX           VARCHAR(20)  NULL,
                 MODIFIED      DATETIME2    NULL,
                 NAME          VARCHAR(250) NOT NULL UNIQUE,
                 PHONE         VARCHAR(50)  NULL,
                 POSTAL_CODE   VARCHAR(20)  NULL,
                 URL           VARCHAR(250) NULL,
                 VAT_ID_NUMBER VARCHAR(50)  NULL,
                 CONSTRAINT FK_COMPANY_CITY FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGINT         NOT NULL PRIMARY KEY IDENTITY (1,1),
                COUNTRY_MAP   VARBINARY(MAX) NULL,
                CREATED       DATETIME2      NOT NULL,
                ISO3166       VARCHAR(2)     NULL,
                MODIFIED      DATETIME2      NULL,
                NAME          VARCHAR(100)   NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGINT         NOT NULL PRIMARY KEY IDENTITY (1,1),
                FK_COUNTRY_ID       BIGINT         NOT NULL,
                FK_TIMEZONE_ID      BIGINT         NOT NULL,
                COUNTRY_STATE_MAP   VARBINARY(MAX) NULL,
                CREATED             DATETIME2      NOT NULL,
                MODIFIED            DATETIME2      NULL,
                NAME                VARCHAR(100)   NOT NULL,
                SYMBOL              VARCHAR(10)    NULL,
                CONSTRAINT FK_COUNTRY_STATE_COUNTRY  FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID),
                CONSTRAINT FK_COUNTRY_STATE_TIMEZONE FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID),
                CONSTRAINT UQ_COUNTRY_STATE          UNIQUE      (FK_COUNTRY_ID,NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID BIGINT        NOT NULL PRIMARY KEY IDENTITY (1,1),
                ABBREVIATION   VARCHAR(20)   NOT NULL,
                CREATED        DATETIME2     NOT NULL,
                MODIFIED       DATETIME2     NULL,
                NAME           VARCHAR(100)  NOT NULL UNIQUE,
                V_TIME_ZONE    VARCHAR(4000) NULL
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }

  @Override
  protected void dropCreateSchemaUser() {
    // -----------------------------------------------------------------------
    // Connect as privileged user
    // -----------------------------------------------------------------------

    final String mssqlserverDatabase = config.getMssqlserverDatabase();
    final String mssqlserverSchema   = config.getMssqlserverSchema();
    final String mssqlserverUser     = config.getMssqlserverUser();

    try {
      connection = DriverManager.getConnection(config.getMssqlserverConnectionPrefix() + config.getJdbcConnectionHost() + ":"
          + config.getMssqlserverConnectionPort() + ";databaseName=master;user=sa;password=" + config.getMssqlserverPasswordSys());

      connection.setAutoCommit(true);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Drop the database, the schema and the user if already existing
    // -----------------------------------------------------------------------

    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement("DROP DATABASE IF EXISTS " + mssqlserverDatabase);
      preparedStatement.executeUpdate();

      // -----------------------------------------------------------------------
      // Create the database, schema and user and grant the necessary rights.
      // -----------------------------------------------------------------------

      preparedStatement = connection.prepareStatement("sp_configure 'contained database authentication', 1");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("RECONFIGURE");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("USE master");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("CREATE DATABASE " + mssqlserverDatabase);
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("USE master");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("ALTER DATABASE " + mssqlserverDatabase + " SET CONTAINMENT = PARTIAL");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("USE " + mssqlserverDatabase);
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("CREATE SCHEMA " + mssqlserverSchema);
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("CREATE USER " + mssqlserverUser + " WITH PASSWORD = '" + config.getMssqlserverPassword()
          + "', DEFAULT_SCHEMA=" + mssqlserverSchema);
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("sp_addrolemember 'db_owner', '" + mssqlserverUser + "'");
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
