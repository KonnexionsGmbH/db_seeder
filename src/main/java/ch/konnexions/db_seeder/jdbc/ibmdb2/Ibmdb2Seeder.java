/**
 *
 */
package ch.konnexions.db_seeder.jdbc.ibmdb2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a IBM DB2 Database. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class Ibmdb2Seeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(Ibmdb2Seeder.class);

  /**
   * 
   */
  public Ibmdb2Seeder() {
    super();

    databaseBrand = DatabaseBrand.IBMDB2;
  }

  @Override
  protected final void connect() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - Start");

    try {
      connection = DriverManager.getConnection(config.getIbmdb2ConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getIbmdb2ConnectionPort()
          + "/" + config.getIbmdb2Database(), "db2inst1", config.getIbmdb2PasswordSys());

      PreparedStatement preparedStatement = connection.prepareStatement("SET CURRENT SCHEMA = " + config.getIbmdb2Schema());
      preparedStatement.executeUpdate();

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
      connectionInt = DriverManager.getConnection(config.getIbmdb2ConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getIbmdb2ConnectionPort()
          + "/" + config.getIbmdb2Database(), "db2inst1", config.getIbmdb2PasswordSys());

      //      connection = DriverManager.getConnection(config.getIbmdb2ConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getIbmdb2ConnectionPort()
      //          + "/" + config.getIbmdb2Database() + ":USER=" + config.getIbmdb2User() + ";PASSWORD=" + config.getIbmdb2Password());

      PreparedStatement preparedStatement = connectionInt.prepareStatement("SET CURRENT SCHEMA = " + config.getIbmdb2Schema());
      preparedStatement.executeUpdate();

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
                 PK_CITY_ID          BIGINT       NOT NULL GENERATED ALWAYS AS IDENTITY,
                 FK_COUNTRY_STATE_ID BIGINT       NULL,
                 CITY_MAP            BLOB         NULL,
                 CREATED             TIMESTAMP    NOT NULL,
                 MODIFIED            TIMESTAMP    NULL,
                 NAME                VARCHAR(100) NOT NULL,
                 CONSTRAINT PK_CITY               PRIMARY KEY (PK_CITY_ID),
                 CONSTRAINT FK_CITY_COUNTRY_STATE FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID) ON DELETE CASCADE
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGINT       NOT NULL GENERATED ALWAYS AS IDENTITY,
                 FK_CITY_ID    BIGINT       NOT NULL,
                 ACTIVE        VARCHAR(1)   NOT NULL,
                 ADDRESS1      VARCHAR(50)  NULL,
                 ADDRESS2      VARCHAR(50)  NULL,
                 ADDRESS3      VARCHAR(50)  NULL,
                 CREATED       TIMESTAMP    NOT NULL,
                 DIRECTIONS    CLOB         NULL,
                 EMAIL         VARCHAR(100) NULL,
                 FAX           VARCHAR(20)  NULL,
                 MODIFIED      TIMESTAMP    NULL,
                 NAME          VARCHAR(250) NOT NULL,
                 PHONE         VARCHAR(50)  NULL,
                 POSTAL_CODE   VARCHAR(20)  NULL,
                 URL           VARCHAR(250) NULL,
                 VAT_ID_NUMBER VARCHAR(50)  NULL,
                 CONSTRAINT PK_COMPANY      PRIMARY KEY (PK_COMPANY_ID),
                 CONSTRAINT FK_COMPANY_CITY FOREIGN KEY (FK_CITY_ID) REFERENCES CITY (PK_CITY_ID) ON DELETE CASCADE,
                 CONSTRAINT UQ_COMPANY_NAME UNIQUE (NAME)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGINT       NOT NULL GENERATED ALWAYS AS IDENTITY,
                COUNTRY_MAP   BLOB         NULL,
                CREATED       TIMESTAMP    NOT NULL,
                ISO3166       VARCHAR(2)   NULL,
                MODIFIED      TIMESTAMP    NULL,
                NAME          VARCHAR(100) NOT NULL,
                CONSTRAINT PK_COUNTRY       PRIMARY KEY (PK_COUNTRY_ID),
                CONSTRAINT UQ_COUNTRY_NAME  UNIQUE (NAME)
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGINT       NOT NULL GENERATED ALWAYS AS IDENTITY,
                FK_COUNTRY_ID       BIGINT       NOT NULL,
                FK_TIMEZONE_ID      BIGINT       NOT NULL,
                COUNTRY_STATE_MAP   BLOB         NULL,
                CREATED             TIMESTAMP    NOT NULL,
                MODIFIED            TIMESTAMP    NULL,
                NAME                VARCHAR(100) NOT NULL,
                SYMBOL              VARCHAR(10)  NULL,
                CONSTRAINT PK_COUNTRY_STATE          PRIMARY KEY (PK_COUNTRY_STATE_ID),
                CONSTRAINT FK_COUNTRY_STATE_COUNTRY  FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID)  ON DELETE CASCADE,
                CONSTRAINT FK_COUNTRY_STATE_TIMEZONE FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID) ON DELETE CASCADE,
                CONSTRAINT UQ_COUNTRY_STATE          UNIQUE (FK_COUNTRY_ID,NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID BIGINT        NOT NULL GENERATED ALWAYS AS IDENTITY,
                ABBREVIATION   VARCHAR(20)   NOT NULL,
                CREATED        TIMESTAMP     NOT NULL,
                MODIFIED       TIMESTAMP     NULL,
                NAME           VARCHAR(100)  NOT NULL,
                V_TIME_ZONE    VARCHAR(4000) NULL,
                CONSTRAINT PK_TIMEZONE             PRIMARY KEY (PK_TIMEZONE_ID),
                CONSTRAINT UQ_TIMEZONE_UQ_TIMEZONE UNIQUE (NAME)
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
    PreparedStatement preparedStatement = null;

    // -----------------------------------------------------------------------
    // Connect as privileged user
    // -----------------------------------------------------------------------

    final String      ibmdb2Schema      = config.getIbmdb2Schema().toUpperCase();

    try {
      Class.forName("com.ibm.db2.jcc.DB2Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      connection = DriverManager.getConnection(config.getIbmdb2ConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getIbmdb2ConnectionPort()
          + "/" + config.getIbmdb2Database(), "db2inst1", config.getIbmdb2PasswordSys());

      connection.setAutoCommit(true);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Drop the user and schema.
    // -----------------------------------------------------------------------

    try {
      Statement  statement     = connection.createStatement();

      ResultSet  resultSet     = statement
          .executeQuery("SELECT 'DROP TABLE \"' || TRIM(TABSCHEMA) || '\".\"' || TRIM(TABNAME) || '\";' FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABSCHEMA = '"
              + ibmdb2Schema + "'");

      Connection connectionInt = connectInt();

      Statement  statement2    = connectionInt.createStatement();

      while (resultSet.next()) {
        statement2.executeUpdate(resultSet.getString(1));
      }

      statement2.close();

      disconnectInt(connectionInt);

      preparedStatement = connection.prepareStatement("DROP SCHEMA " + ibmdb2Schema + " RESTRICT");
      executeUpdateExistence(preparedStatement);

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
