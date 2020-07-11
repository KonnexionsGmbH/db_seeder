/**
 *
 */
package ch.konnexions.db_seeder.jdbc.cubrid;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a CUBRID DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class CubridSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(CubridSeeder.class);

  /**
   * Instantiates a new CUBRID seeder.
   * @param args0 
   */
  public CubridSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    dbms                  = Dbms.CUBRID;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    driver                = "cubrid.jdbc.driver.CUBRIDDriver";

    tableNameDelimiter    = "\"";

    urlBase               = config.getCubridConnectionPrefix() + config.getCubridConnectionHost() + ":" + config.getCubridConnectionPort() + ":"
        + config.getCubridDatabase() + ":";
    url                   = urlBase + config.getCubridConnectionSuffix();
    urlSetup              = urlBase + config.getCubridUserSys() + config.getCubridConnectionSuffix();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE "CITY"
             (
                 PK_CITY_ID          INT            NOT NULL PRIMARY KEY,
                 FK_COUNTRY_STATE_ID INT,
                 CITY_MAP            BLOB,
                 CREATED             TIMESTAMP      NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                VARCHAR (100)  NOT NULL,
                 CONSTRAINT FK_CITY_COUNTRY_STATE   FOREIGN KEY (FK_COUNTRY_STATE_ID) REFERENCES "COUNTRY_STATE" (PK_COUNTRY_STATE_ID)
             )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE "COMPANY"
             (
                 PK_COMPANY_ID       INT            NOT NULL PRIMARY KEY,
                 FK_CITY_ID          INT            NOT NULL,
                 ACTIVE              VARCHAR (1)    NOT NULL,
                 ADDRESS1            VARCHAR (50),
                 ADDRESS2            VARCHAR (50),
                 ADDRESS3            VARCHAR (50),
                 CREATED             TIMESTAMP      NOT NULL,
                 DIRECTIONS          CLOB,
                 EMAIL               VARCHAR (100),
                 FAX                 VARCHAR (50),
                 MODIFIED            TIMESTAMP,
                 NAME                VARCHAR (250)  NOT NULL UNIQUE,
                 PHONE               VARCHAR (50),
                 POSTAL_CODE         VARCHAR (50),
                 URL                 VARCHAR (250),
                 VAT_ID_NUMBER       VARCHAR (100),
                 CONSTRAINT FK_COMPANY_CITY         FOREIGN KEY (FK_CITY_ID)          REFERENCES "CITY" (PK_CITY_ID)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE "COUNTRY"
             (
                 PK_COUNTRY_ID INT            NOT NULL PRIMARY KEY,
                 COUNTRY_MAP   BLOB,
                 CREATED       TIMESTAMP      NOT NULL,
                 ISO3166       VARCHAR (50),
                 MODIFIED      TIMESTAMP,
                 NAME          VARCHAR (100)  NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE "COUNTRY_STATE"
             (
                 PK_COUNTRY_STATE_ID INT            NOT NULL PRIMARY KEY,
                 FK_COUNTRY_ID       INT            NOT NULL,
                 FK_TIMEZONE_ID      INT            NOT NULL,
                 COUNTRY_STATE_MAP   BLOB,
                 CREATED             TIMESTAMP      NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                VARCHAR (100)  NOT NULL,
                 SYMBOL              VARCHAR (50),
                 CONSTRAINT FK_COUNTRY_STATE_COUNTRY  FOREIGN KEY (FK_COUNTRY_ID)  REFERENCES "COUNTRY"  (PK_COUNTRY_ID),
                 CONSTRAINT FK_COUNTRY_STATE_TIMEZONE FOREIGN KEY (FK_TIMEZONE_ID) REFERENCES "TIMEZONE" (PK_TIMEZONE_ID),
                 CONSTRAINT UQ_COUNTRY_STATE          UNIQUE (FK_COUNTRY_ID, NAME)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE "TIMEZONE"
             (
                 PK_TIMEZONE_ID INT             NOT NULL PRIMARY KEY,
                 ABBREVIATION   VARCHAR (50)    NOT NULL,
                 CREATED        TIMESTAMP       NOT NULL,
                 MODIFIED       TIMESTAMP,
                 NAME           VARCHAR (100)   NOT NULL UNIQUE,
                 V_TIME_ZONE    VARCHAR (4000)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  private final void dropAllTables() {
    try {
      statement = connection.createStatement();

      for (String tableName : TABLE_NAMES_DROP) {
        statement.execute("DROP TABLE IF EXISTS \"" + tableName + "\"");
      }

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void dropUser(String cubridUser) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    try {
      int count = 0;

      preparedStatement = connection.prepareStatement("SELECT count(*) FROM db_user WHERE name = ?");
      preparedStatement.setString(1, cubridUser);

      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      preparedStatement.close();

      if (count > 0) {
        statement = connection.createStatement();

        statement.execute("DROP USER " + cubridUser);

        statement.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }

  @Override
  protected final void setupDatabase() {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSetup, driver);

    // -----------------------------------------------------------------------
    // Drop the database user if already existing.
    // -----------------------------------------------------------------------

    String cubridUser = config.getCubridUser();

    dropAllTables();

    dropUser(cubridUser);

    // -----------------------------------------------------------------------
    // Create the database user.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      statement.execute("CREATE USER " + cubridUser + " PASSWORD '" + config.getCubridPassword() + "' GROUPS dba");

      statement.close();

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(url, null, config.getCubridUser(), config.getCubridPassword());

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }
}
