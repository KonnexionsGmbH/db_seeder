/**
 *
 */
package ch.konnexions.db_seeder.jdbc.cubrid;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a CUBRID DBMS. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class CubridSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(CubridSeeder.class);

  /**
   *
   */
  public CubridSeeder() {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    dbms     = Dbms.CUBRID;

    driver   = "cubrid.jdbc.driver.CUBRIDDriver";

    urlBase  = config.getCubridConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getCubridConnectionPort() + ":" + config.getCubridDatabase();

    url      = urlBase + ":" + config.getCubridUser() + ":" + config.getCubridPassword();
    urlSetup = urlBase + ":dba::";

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE city
             (
                 pk_city_id          INT            NOT NULL PRIMARY KEY AUTO_INCREMENT,
                 fk_country_state_id BIGINT,
                 city_map            BLOB,
                 created             TIMESTAMP      NOT NULL,
                 modified            TIMESTAMP,
                 name                VARCHAR (100)  NOT NULL,
                 CONSTRAINT fk_city_country_state   FOREIGN KEY (fk_country_state_id) REFERENCES country_state (pk_country_state_id)
             )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE company
             (
                 pk_company_id       INT            NOT NULL PRIMARY KEY AUTO_INCREMENT,
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
                 CONSTRAINT fk_company_city         FOREIGN KEY (fk_city_id)          REFERENCES city (pk_city_id)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE country
             (
                 pk_country_id INT            NOT NULL PRIMARY KEY AUTO_INCREMENT,
                 country_map   BLOB,
                 created       TIMESTAMP      NOT NULL,
                 iso3166       VARCHAR (2),
                 modified      TIMESTAMP,
                 name          VARCHAR (100)  NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE country_state
             (
                 pk_country_state_id INT            NOT NULL PRIMARY KEY AUTO_INCREMENT,
                 fk_country_id       INT            NOT NULL,
                 fk_timezone_id      INT            NOT NULL,
                 country_state_map   BLOB,
                 created             TIMESTAMP      NOT NULL,
                 modified            TIMESTAMP,
                 name                VARCHAR (100)  NOT NULL,
                 symbol              VARCHAR (10),
                 CONSTRAINT fk_country_state_country  FOREIGN KEY (fk_country_id)  REFERENCES country  (pk_country_id),
                 CONSTRAINT fk_country_state_timezone FOREIGN KEY (fk_timezone_id) REFERENCES timezone (pk_timezone_id),
                 CONSTRAINT uq_country_state          UNIQUE (fk_country_id, name)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE timezone
             (
                 pk_timezone_id INT             NOT NULL PRIMARY KEY AUTO_INCREMENT,
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

  private final void dropUser(String cubridUser) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    try {
      int count = 0;

      preparedStatement = connectionSetup.prepareStatement("SELECT count(*) FROM db_user WHERE name = UPPER(?)");
      preparedStatement.setString(1, cubridUser);
      preparedStatement.executeQuery();

      ResultSet resultSet = preparedStatement.getResultSet();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      statement = connectionSetup.createStatement();

      if (count > 0) {
        String sqlStmntLocal = "DROP USER " + cubridUser;
        logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- sqlStmnt='" + sqlStmntLocal + "'");
        statement.execute(sqlStmntLocal);
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

    connectionSetup = connect(urlSetup, driver);

    // -----------------------------------------------------------------------
    // Drop the database user if already existing.
    // -----------------------------------------------------------------------

    String cubridUser = config.getCubridUser();

    dropUser(cubridUser);

    // -----------------------------------------------------------------------
    // Create the database user.
    // -----------------------------------------------------------------------

    try {
      statement.execute("CREATE USER " + cubridUser + " PASSWORD \"" + config.getCubridPassword() + "\" GROUPS dba");

      statement.close();

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connectionSetup);

    connection = connect(url);

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }
}
