/**
 *
 */
package ch.konnexions.db_seeder.jdbc.oracle;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for an Oracle DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class OracleSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(OracleSeeder.class);

  /**
   * Instantiates a new Oracle Database seeder.
   * @param args0 
   */
  public OracleSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    dbms                  = Dbms.ORACLE;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    tableNameDelimiter    = "";

    url                   = config.getOracleConnectionPrefix() + config.getOracleConnectionHost() + ":" + config.getOracleConnectionPort() + "/"
        + config.getOracleConnectionService();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE CITY
             (
                 PK_CITY_ID          NUMBER         NOT NULL PRIMARY KEY,
                 FK_COUNTRY_STATE_ID NUMBER,
                 CITY_MAP            BLOB,
                 CREATED             TIMESTAMP      NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                VARCHAR2 (100) NOT NULL,
                 CONSTRAINT FK_CITY_COUNTRY_STATE   FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES COUNTRY_STATE (PK_COUNTRY_STATE_ID)
             )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY
             (
                 PK_COMPANY_ID       NUMBER         NOT NULL PRIMARY KEY,
                 FK_CITY_ID          NUMBER         NOT NULL,
                 ACTIVE              VARCHAR2 (1)   NOT NULL,
                 ADDRESS1            VARCHAR2 (50),
                 ADDRESS2            VARCHAR2 (50),
                 ADDRESS3            VARCHAR2 (50),
                 CREATED             TIMESTAMP      NOT NULL,
                 DIRECTIONS          CLOB,
                 EMAIL               VARCHAR2 (100),
                 FAX                 VARCHAR2 (50),
                 MODIFIED            TIMESTAMP,
                 NAME                VARCHAR2 (250) NOT NULL UNIQUE,
                 PHONE               VARCHAR2 (50),
                 POSTAL_CODE         VARCHAR2 (50),
                 URL                 VARCHAR2 (250),
                 VAT_ID_NUMBER       VARCHAR2 (100),
                 CONSTRAINT FK_COMPANY_CITY         FOREIGN KEY (FK_CITY_ID)          REFERENCES CITY (PK_CITY_ID)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY
             (
                 PK_COUNTRY_ID NUMBER         NOT NULL PRIMARY KEY,
                 COUNTRY_MAP   BLOB,
                 CREATED       TIMESTAMP      NOT NULL,
                 ISO3166       VARCHAR2 (50),
                 MODIFIED      TIMESTAMP,
                 NAME          VARCHAR2 (100) NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE
             (
                 PK_COUNTRY_STATE_ID NUMBER         NOT NULL PRIMARY KEY,
                 FK_COUNTRY_ID       NUMBER         NOT NULL,
                 FK_TIMEZONE_ID      NUMBER         NOT NULL,
                 COUNTRY_STATE_MAP   BLOB,
                 CREATED             TIMESTAMP      NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                VARCHAR2 (100) NOT NULL,
                 SYMBOL              VARCHAR2 (50),
                 CONSTRAINT FK_COUNTRY_STATE_COUNTRY  FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES COUNTRY  (PK_COUNTRY_ID),
                 CONSTRAINT FK_COUNTRY_STATE_TIMEZONE FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES TIMEZONE (PK_TIMEZONE_ID),
                 CONSTRAINT UQ_COUNTRY_STATE          UNIQUE (FK_COUNTRY_ID, NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE
             (
                 PK_TIMEZONE_ID NUMBER          NOT NULL PRIMARY KEY,
                 ABBREVIATION   VARCHAR2 (50)   NOT NULL,
                 CREATED        TIMESTAMP       NOT NULL,
                 MODIFIED       TIMESTAMP,
                 NAME           VARCHAR2 (100)  NOT NULL UNIQUE,
                 V_TIME_ZONE    VARCHAR2 (4000)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  private final void dropUser(String user) {
    try {
      int count = 0;

      preparedStatement = connection.prepareStatement("SELECT count(*) FROM ALL_USERS WHERE username = ?");
      preparedStatement.setString(1, user);

      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      preparedStatement.close();

      if (count > 0) {
        statement = connection.createStatement();

        statement.execute("DROP USER " + user + " CASCADE");

        statement.close();
      }
    } catch (SQLException e) {
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

    connection = connect(url, null, config.getOracleUserSys(), config.getOraclePasswordSys());

    // -----------------------------------------------------------------------
    // Drop the database user if already existing.
    // -----------------------------------------------------------------------

    String oracleUser = config.getOracleUser();

    dropUser(oracleUser);

    // -----------------------------------------------------------------------
    // Create the database user and grant the necessary rights.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      statement.execute("CREATE USER " + oracleUser + " IDENTIFIED BY \"" + config.getOraclePassword() + "\"");

      statement.execute("ALTER USER " + oracleUser + " QUOTA UNLIMITED ON users");

      statement.execute("GRANT CREATE SEQUENCE TO " + oracleUser);

      statement.execute("GRANT CREATE SESSION TO " + oracleUser);

      statement.execute("GRANT CREATE TABLE TO " + oracleUser);

      statement.execute("GRANT UNLIMITED TABLESPACE TO " + oracleUser);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(url, null, config.getOracleUser(), config.getOraclePassword());

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }
}
