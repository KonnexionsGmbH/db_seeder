/**
 *
 */
package ch.konnexions.db_seeder.jdbc.mssqlserver;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a Microsoft SQL Server. </h1>
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

  @Override
  protected void createDataInsert(PreparedStatement preparedStatement, String tableName, int rowCount, ArrayList<Object> pkList) {
    final String sqlStmnt = "INSERT INTO " + tableName + " (" + createDmlStmnt(tableName) + ")";

    try {
      preparedStatement = connection.prepareStatement(sqlStmnt, new String[] { "PK_" + tableName + "_ID" });
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    for (int rowNo = 1; rowNo <= rowCount; rowNo++) {
      prepDmlStmntInsert(preparedStatement, tableName, rowCount, rowNo, pkList);

      try {
        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();

        while (resultSet.next()) {
          pkList.add((int) resultSet.getLong(1));
        }
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE CITY (
                 PK_CITY_ID          BIGINT         NOT NULL IDENTITY (1,1),
                 FK_COUNTRY_STATE_ID BIGINT         NULL,
                 CITY_MAP            VARBINARY(MAX) NULL,
                 CREATED             DATETIME2      NOT NULL,
                 MODIFIED            DATETIME2      NULL,
                 NAME                VARCHAR(100)   NOT NULL,
                 CONSTRAINT PK_CITY               PRIMARY KEY (PK_CITY_ID),
                 CONSTRAINT FK_CITY_COUNTRY_STATE FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGINT       NOT NULL IDENTITY (1,1),
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
                 NAME          VARCHAR(250) NOT NULL,
                 PHONE         VARCHAR(50)  NULL,
                 POSTAL_CODE   VARCHAR(20)  NULL,
                 URL           VARCHAR(250) NULL,
                 VAT_ID_NUMBER VARCHAR(50)  NULL,
                 CONSTRAINT PK_COMPANY      PRIMARY KEY (PK_COMPANY_ID),
                 CONSTRAINT FK_COMPANY_CITY FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID),
                 CONSTRAINT UQ_COMPANY_NAME UNIQUE      (NAME)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGINT         NOT NULL IDENTITY (1,1),
                COUNTRY_MAP   VARBINARY(MAX) NULL,
                CREATED       DATETIME2      NOT NULL,
                ISO3166       VARCHAR(2)     NULL,
                MODIFIED      DATETIME2      NULL,
                NAME          VARCHAR(100)   NOT NULL,
                CONSTRAINT PK_COUNTRY       PRIMARY KEY (PK_COUNTRY_ID),
                CONSTRAINT UQ_COUNTRY_NAME  UNIQUE (NAME)
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGINT         NOT NULL IDENTITY (1,1),
                FK_COUNTRY_ID       BIGINT         NOT NULL,
                FK_TIMEZONE_ID      BIGINT         NOT NULL,
                COUNTRY_STATE_MAP   VARBINARY(MAX) NULL,
                CREATED             DATETIME2      NOT NULL,
                MODIFIED            DATETIME2      NULL,
                NAME                VARCHAR(100)   NOT NULL,
                SYMBOL              VARCHAR(10)    NULL,
                CONSTRAINT PK_COUNTRY_STATE          PRIMARY KEY (PK_COUNTRY_STATE_ID),
                CONSTRAINT FK_COUNTRY_STATE_COUNTRY  FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID),
                CONSTRAINT FK_COUNTRY_STATE_TIMEZONE FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID),
                CONSTRAINT UQ_COUNTRY_STATE          UNIQUE      (FK_COUNTRY_ID,NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID BIGINT        NOT NULL IDENTITY (1,1),
                ABBREVIATION   VARCHAR(20)   NOT NULL,
                CREATED        DATETIME2     NOT NULL,
                MODIFIED       DATETIME2     NULL,
                NAME           VARCHAR(100)  NOT NULL,
                V_TIME_ZONE    VARCHAR(4000) NULL,
                CONSTRAINT PK_TIMEZONE             PRIMARY KEY (PK_TIMEZONE_ID),
                CONSTRAINT UQ_TIMEZONE_UQ_TIMEZONE UNIQUE      (NAME)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }

  @Override
  protected void dropCreateSchemaUser() {
    PreparedStatement preparedStatement   = null;

    // -----------------------------------------------------------------------
    // Connect as privileged user
    // -----------------------------------------------------------------------

    final String      mssqlserverDatabase = config.getMssqlserverDatabase();
    final String      mssqlserverSchema   = config.getMssqlserverSchema();
    final String      mssqlserverUser     = config.getMssqlserverUser();

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

    try {
      preparedStatement = connection.prepareStatement("DROP DATABASE IF EXISTS " + mssqlserverDatabase);
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("DROP SCHEMA IF EXISTS " + mssqlserverSchema);
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("DROP USER IF EXISTS " + mssqlserverUser);
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

  @Override
  protected void prepStmntInsertColBlob(final int columnPos, PreparedStatement preparedStatement, int rowCount) {
    try {
      preparedStatement.setBytes(columnPos, BLOB_DATA);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

}
