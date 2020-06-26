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
   */
  public CubridSeeder() {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    dbms     = Dbms.CUBRID;

    driver   = "cubrid.jdbc.driver.CUBRIDDriver";

    urlBase  = config.getCubridConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getCubridConnectionPort() + ":" + config.getCubridDatabase();

    url      = urlBase + ":::";
    urlSetup = urlBase + ":dba::";

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
                 pk_city_id          INT            NOT NULL PRIMARY KEY,
                 fk_country_state_id INT,
                 city_map            BLOB,
                 created             TIMESTAMP      NOT NULL,
                 modified            TIMESTAMP,
                 name                VARCHAR (100)  NOT NULL,
                 CONSTRAINT fk_city_country_state   FOREIGN KEY (fk_country_state_id) REFERENCES "COUNTRY_STATE" (pk_country_state_id)
             )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE "COMPANY"
             (
                 pk_company_id       INT            NOT NULL PRIMARY KEY,
                 fk_city_id          INT            NOT NULL,
                 active              VARCHAR (1)    NOT NULL,
                 address1            VARCHAR (50),
                 address2            VARCHAR (50),
                 address3            VARCHAR (50),
                 created             TIMESTAMP      NOT NULL,
                 directions          CLOB,
                 email               VARCHAR (100),
                 fax                 VARCHAR (20),
                 modified            TIMESTAMP,
                 name                VARCHAR (250)  NOT NULL UNIQUE,
                 phone               VARCHAR (50),
                 postal_code         VARCHAR (20),
                 url                 VARCHAR (250),
                 vat_id_number       VARCHAR (50),
                 CONSTRAINT fk_company_city         FOREIGN KEY (fk_city_id)          REFERENCES "CITY" (pk_city_id)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE "COUNTRY"
             (
                 pk_country_id INT            NOT NULL PRIMARY KEY,
                 country_map   BLOB,
                 created       TIMESTAMP      NOT NULL,
                 iso3166       VARCHAR (2),
                 modified      TIMESTAMP,
                 name          VARCHAR (100)  NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE "COUNTRY_STATE"
             (
                 pk_country_state_id INT            NOT NULL PRIMARY KEY,
                 fk_country_id       INT            NOT NULL,
                 fk_timezone_id      INT            NOT NULL,
                 country_state_map   BLOB,
                 created             TIMESTAMP      NOT NULL,
                 modified            TIMESTAMP,
                 name                VARCHAR (100)  NOT NULL,
                 symbol              VARCHAR (10),
                 CONSTRAINT fk_country_state_country  FOREIGN KEY (fk_country_id)  REFERENCES "COUNTRY"  (pk_country_id),
                 CONSTRAINT fk_country_state_timezone FOREIGN KEY (fk_timezone_id) REFERENCES "TIMEZONE" (pk_timezone_id),
                 CONSTRAINT uq_country_state          UNIQUE (fk_country_id, name)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE "TIMEZONE"
             (
                 pk_timezone_id INT             NOT NULL PRIMARY KEY,
                 abbreviation   VARCHAR (20)    NOT NULL,
                 created        TIMESTAMP       NOT NULL,
                 modified       TIMESTAMP,
                 name           VARCHAR (100)   NOT NULL UNIQUE,
                 v_time_zone    VARCHAR (4000)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  private final void dropAllTables() {
    try {
      statement = connection.createStatement();

      for (String tableName : TABLE_NAMES) {
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

      preparedStatement = connection.prepareStatement("SELECT count(*) FROM db_user WHERE name = UPPER(?)");
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

    connection = connect(url, driver, config.getCubridUser(), config.getCubridPassword());

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }
}
