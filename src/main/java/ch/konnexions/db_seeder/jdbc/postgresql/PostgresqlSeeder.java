/**
 *
 */
package ch.konnexions.db_seeder.jdbc.postgresql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a PostgreSQL Database. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class PostgresqlSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(PostgresqlSeeder.class);

  /**
   * 
   */
  public PostgresqlSeeder() {
    super();

    databaseBrand = DatabaseBrand.POSTGRESQL;
  }

  @Override
  protected final void connect() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - Start");

    try {
      connection = DriverManager
          .getConnection(config.getPostgresqlConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getPostgresqlConnectionPort() + "/"
              + config.getPostgresqlDatabase() + "?user=" + config.getPostgresqlUser() + "&password=" + config.getPostgresqlPassword());

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
                 PK_CITY_ID          BIGSERIAL      NOT NULL PRIMARY KEY,
                 FK_COUNTRY_STATE_ID BIGINT         NULL,
                 CITY_MAP            BYTEA          NULL,
                 CREATED             TIMESTAMP      NOT NULL,
                 MODIFIED            TIMESTAMP      NULL,
                 NAME                VARCHAR(100)   NOT NULL,
                 FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGSERIAL    NOT NULL PRIMARY KEY,
                 FK_CITY_ID    BIGINT       NOT NULL,
                 ACTIVE        VARCHAR(1)   NOT NULL,
                 ADDRESS1      VARCHAR(50)  NULL,
                 ADDRESS2      VARCHAR(50)  NULL,
                 ADDRESS3      VARCHAR(50)  NULL,
                 CREATED       TIMESTAMP    NOT NULL,
                 DIRECTIONS    TEXT         NULL,
                 EMAIL         VARCHAR(100) NULL,
                 FAX           VARCHAR(20)  NULL,
                 MODIFIED      TIMESTAMP    NULL,
                 NAME          VARCHAR(250) NOT NULL UNIQUE,
                 PHONE         VARCHAR(50)  NULL,
                 POSTAL_CODE   VARCHAR(20)  NULL,
                 URL           VARCHAR(250) NULL,
                 VAT_ID_NUMBER VARCHAR(50)  NULL,
                 FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGSERIAL               PRIMARY KEY,
                COUNTRY_MAP   BYTEA          NULL,
                CREATED       TIMESTAMP      NOT NULL,
                ISO3166       VARCHAR(2)     NULL,
                MODIFIED      TIMESTAMP      NULL,
                NAME          VARCHAR(100)   NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGSERIAL      NOT NULL PRIMARY KEY,
                FK_COUNTRY_ID       BIGINT         NOT NULL,
                FK_TIMEZONE_ID      BIGINT         NOT NULL,
                COUNTRY_STATE_MAP   BYTEA          NULL,
                CREATED             TIMESTAMP      NOT NULL,
                MODIFIED            TIMESTAMP      NULL,
                NAME                VARCHAR(100)   NOT NULL,
                SYMBOL              VARCHAR(10)    NULL,
                FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID),
                FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID),
                UNIQUE      (FK_COUNTRY_ID,NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID BIGSERIAL     NOT NULL PRIMARY KEY,
                ABBREVIATION   VARCHAR(20)   NOT NULL,
                CREATED        TIMESTAMP     NOT NULL,
                MODIFIED       TIMESTAMP     NULL,
                NAME           VARCHAR(100)  NOT NULL UNIQUE,
                V_TIME_ZONE    VARCHAR(4000) NULL
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }

  @Override
  protected void dropCreateSchemaUser() {
    PreparedStatement preparedStatement  = null;

    // -----------------------------------------------------------------------
    // Connect as privileged user
    // -----------------------------------------------------------------------

    final String      postgresqlDatabase = config.getPostgresqlDatabase();
    final String      postgresqlUser     = config.getPostgresqlUser();

    try {
      connection = DriverManager.getConnection(config.getPostgresqlConnectionPrefix() + config.getJdbcConnectionHost() + ":"
          + config.getPostgresqlConnectionPort() + "/kxn_db_sys?user=kxn_user_sys&password=" + config.getPostgresqlPasswordSys());

      connection.setAutoCommit(true);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Drop the database, the schema and the user if already existing
    // -----------------------------------------------------------------------

    try {
      preparedStatement = connection.prepareStatement("DROP DATABASE IF EXISTS " + postgresqlDatabase);
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("DROP USER IF EXISTS " + postgresqlUser);
      preparedStatement.executeUpdate();

      // -----------------------------------------------------------------------
      // Create the database, schema and user and grant the necessary rights.
      // -----------------------------------------------------------------------

      preparedStatement = connection.prepareStatement("CREATE DATABASE " + postgresqlDatabase);
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("CREATE USER " + postgresqlUser + " WITH ENCRYPTED PASSWORD '" + config.getPostgresqlPassword() + "'");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT ALL PRIVILEGES ON DATABASE " + postgresqlDatabase + " TO " + postgresqlUser);
      preparedStatement.executeUpdate();

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    disconnect();
    connect();
  }

  @Override
  protected final void prepStmntInsertColBlob(final int columnPos, PreparedStatement preparedStatement, int rowCount) {
    FileInputStream blobData = null;

    try {
      blobData = new FileInputStream(new File(Paths.get("src", "main", "resources").toAbsolutePath().toString() + File.separator + "blob.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      preparedStatement.setBinaryStream(columnPos, blobData);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      blobData.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

}
