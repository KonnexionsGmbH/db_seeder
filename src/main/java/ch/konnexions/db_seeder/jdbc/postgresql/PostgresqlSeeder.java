/**
 *
 */
package ch.konnexions.db_seeder.jdbc.postgresql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a PostgreSQL DBMS. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class PostgresqlSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(PostgresqlSeeder.class);

  /**
   * Instantiates a new PostgreSQL Database seeder.
   */
  public PostgresqlSeeder() {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    dbms     = Dbms.POSTGRESQL;

    urlBase  = config.getPostgresqlConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getPostgresqlConnectionPort() + "/";

    url      = urlBase + config.getPostgresqlDatabase() + "?user=" + config.getPostgresqlUser() + "&password=" + config.getPostgresqlPassword();
    urlSetup = urlBase + "kxn_db_sys?user=kxn_user_sys&password=" + config.getPostgresqlPasswordSys();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE CITY (
                 PK_CITY_ID          BIGSERIAL      NOT NULL PRIMARY KEY,
                 FK_COUNTRY_STATE_ID BIGINT,
                 CITY_MAP            BYTEA,
                 CREATED             TIMESTAMP      NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                VARCHAR(100)   NOT NULL,
                 FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGSERIAL    NOT NULL PRIMARY KEY,
                 FK_CITY_ID    BIGINT       NOT NULL,
                 ACTIVE        VARCHAR(1)   NOT NULL,
                 ADDRESS1      VARCHAR(50),
                 ADDRESS2      VARCHAR(50),
                 ADDRESS3      VARCHAR(50),
                 CREATED       TIMESTAMP    NOT NULL,
                 DIRECTIONS    TEXT,
                 EMAIL         VARCHAR(100),
                 FAX           VARCHAR(20),
                 MODIFIED      TIMESTAMP,
                 NAME          VARCHAR(250) NOT NULL UNIQUE,
                 PHONE         VARCHAR(50),
                 POSTAL_CODE   VARCHAR(20),
                 URL           VARCHAR(250),
                 VAT_ID_NUMBER VARCHAR(50),
                 FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGSERIAL      NOT NULL PRIMARY KEY,
                COUNTRY_MAP   BYTEA,
                CREATED       TIMESTAMP      NOT NULL,
                ISO3166       VARCHAR(2),
                MODIFIED      TIMESTAMP,
                NAME          VARCHAR(100)   NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGSERIAL      NOT NULL PRIMARY KEY,
                FK_COUNTRY_ID       BIGINT         NOT NULL,
                FK_TIMEZONE_ID      BIGINT         NOT NULL,
                COUNTRY_STATE_MAP   BYTEA,
                CREATED             TIMESTAMP      NOT NULL,
                MODIFIED            TIMESTAMP,
                NAME                VARCHAR(100)   NOT NULL,
                SYMBOL              VARCHAR(10),
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
                MODIFIED       TIMESTAMP,
                NAME           VARCHAR(100)  NOT NULL UNIQUE,
                V_TIME_ZONE    VARCHAR(4000)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  @Override
  protected final void prepStmntInsertColBlob(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
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

  @Override
  protected final void setupDatabase() {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSetup);

    try {
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Drop the database and the database user.
    // -----------------------------------------------------------------------

    String postgresqlDatabase = config.getPostgresqlDatabase();
    String postgresqlUser     = config.getPostgresqlUser();

    try {
      statement = connection.createStatement();

      statement.execute("DROP DATABASE IF EXISTS " + postgresqlDatabase);

      statement.execute("DROP USER IF EXISTS " + postgresqlUser);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create the database, the database user and grant the necessary rights.
    // -----------------------------------------------------------------------

    try {
      statement.execute("CREATE DATABASE " + postgresqlDatabase);

      statement.execute("CREATE USER " + postgresqlUser + " WITH ENCRYPTED PASSWORD '" + config.getPostgresqlPassword() + "'");

      statement.execute("GRANT ALL PRIVILEGES ON DATABASE " + postgresqlDatabase + " TO " + postgresqlUser);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(url);

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }

}
