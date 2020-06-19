/**
 *
 */
package ch.konnexions.db_seeder.jdbc.apache.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a Microsoft SQL Server DBMS. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class DerbySeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(DerbySeeder.class);

  /**
   *
   */
  public DerbySeeder() {
    super();

    dbms = Dbms.DERBY;
  }

  @Override
  protected final void connect() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - Start");

    try {
      connection = DriverManager.getConnection(config.getApachederbyConnectionPrefix() + config.getJdbcConnectionHost() + ":"
          + config.getApachederbyConnectionPort() + "/" + config.getApachederbyDatabase() + ";create=false");

      connection.setAutoCommit(false);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - End");
  }

  private final Connection connectInt() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - Start");

    Connection connectionInt = null;

    try {
      connectionInt = DriverManager.getConnection(config.getApachederbyConnectionPrefix() + config.getJdbcConnectionHost() + ":"
          + config.getApachederbyConnectionPort() + "/" + config.getApachederbyDatabase() + ";create=false");

      connectionInt.setAutoCommit(false);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - End");

    return connectionInt;
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE CITY (
                 PK_CITY_ID          BIGINT         NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                 FK_COUNTRY_STATE_ID BIGINT,
                 CITY_MAP            BLOB,
                 CREATED             TIMESTAMP      NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                VARCHAR(100)   NOT NULL,
                 CONSTRAINT FK_CITY_COUNTRY_STATE FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGINT       NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                 FK_CITY_ID    BIGINT       NOT NULL,
                 ACTIVE        VARCHAR(1)   NOT NULL,
                 ADDRESS1      VARCHAR(50),
                 ADDRESS2      VARCHAR(50),
                 ADDRESS3      VARCHAR(50),
                 CREATED       TIMESTAMP    NOT NULL,
                 DIRECTIONS    CLOB,
                 EMAIL         VARCHAR(100),
                 FAX           VARCHAR(20),
                 MODIFIED      TIMESTAMP,
                 NAME          VARCHAR(250) NOT NULL UNIQUE,
                 PHONE         VARCHAR(50),
                 POSTAL_CODE   VARCHAR(20),
                 URL           VARCHAR(250),
                 VAT_ID_NUMBER VARCHAR(50),
                 CONSTRAINT FK_COMPANY_CITY FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGINT         NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                COUNTRY_MAP   BLOB,
                CREATED       TIMESTAMP      NOT NULL,
                ISO3166       VARCHAR(2),
                MODIFIED      TIMESTAMP,
                NAME          VARCHAR(100)   NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGINT         NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                FK_COUNTRY_ID       BIGINT         NOT NULL,
                FK_TIMEZONE_ID      BIGINT         NOT NULL,
                COUNTRY_STATE_MAP   BLOB,
                CREATED             TIMESTAMP      NOT NULL,
                MODIFIED            TIMESTAMP,
                NAME                VARCHAR(100)   NOT NULL,
                SYMBOL              VARCHAR(10),
                CONSTRAINT FK_COUNTRY_STATE_COUNTRY  FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID),
                CONSTRAINT FK_COUNTRY_STATE_TIMEZONE FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID),
                CONSTRAINT UQ_COUNTRY_STATE          UNIQUE      (FK_COUNTRY_ID,NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID BIGINT        NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                ABBREVIATION   VARCHAR(20)   NOT NULL,
                CREATED        TIMESTAMP     NOT NULL,
                MODIFIED       TIMESTAMP,
                NAME           VARCHAR(100)  NOT NULL UNIQUE,
                V_TIME_ZONE    VARCHAR(4000)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }

  private final void disconnectInt(Connection connectionInt) {
    if (connectionInt != null) {
      try {
        if (!(connectionInt.getAutoCommit())) {
          connectionInt.commit();
        }

        connectionInt.close();

        connectionInt = null;
      } catch (SQLException ec) {
        ec.printStackTrace();
        System.exit(1);
      }
    }
  }

  @Override
  protected void dropCreateSchemaUser() {
    try {
      Class.forName("org.apache.derby.jdbc.ClientDriver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database
    // -----------------------------------------------------------------------

    try {
      connection = DriverManager.getConnection(config.getApachederbyConnectionPrefix() + config.getJdbcConnectionHost() + ":"
          + config.getApachederbyConnectionPort() + "/" + config.getApachederbyDatabase() + ";create=true");

      connection.setAutoCommit(true);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Drop the database tables if already existing
    // -----------------------------------------------------------------------

    try {
      Connection connectionInt = connectInt();

      Statement  statement     = connection.createStatement();
      Statement  statement2    = connectionInt.createStatement();
      ResultSet  resultSet     = null;

      for (String tableName : TABLE_NAMES) {

        resultSet = statement
            .executeQuery("SELECT T.TABLENAME, 'DROP TABLE \"' || T.TABLENAME || '\"' FROM SYS.SYSTABLES T INNER JOIN SYS.SYSSCHEMAS S ON T.SCHEMAID = S.SCHEMAID WHERE T.TABLENAME = '"
                + tableName + "' AND S.SCHEMANAME = 'APP'");

        while (resultSet.next()) {
          statement2.executeUpdate(resultSet.getString(2));
        }
      }

      statement2.close();

      disconnectInt(connectionInt);

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
