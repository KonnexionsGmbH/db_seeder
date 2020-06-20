/**
 *
 */
package ch.konnexions.db_seeder.jdbc.cubrid;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.DatabaseSeeder;
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

    dbms     = Dbms.CUBRID;

    driver   = "cubrid.jdbc.driver.CUBRIDDriver";

    urlBase  = config.getCubridConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getCubridConnectionPort() + ":" + config.getCubridDatabase();

    url      = urlBase + ":" + config.getCubridUser() + ":" + config.getCubridPassword();
    urlSetup = urlBase + ":dba::";
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
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }

  @Override
  protected void resetAndCreateDatabase() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName)
        + " - Start - database table \" + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName) + \" - \"\n"
        + "        + String.format(DatabaseSeeder.FORMAT_ROW_NO, rowCount) + \" rows to be created");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSetup, driver);

    // -----------------------------------------------------------------------
    // Drop the database user if already existing.
    // -----------------------------------------------------------------------

    String cubridUser = config.getCubridUser();

    try {
      int count = 0;

      preparedStatement = connection.prepareStatement("SELECT count(*) FROM db_user WHERE name = UPPER(?)");
      preparedStatement.setString(1, cubridUser);
      preparedStatement.executeQuery();

      ResultSet resultSet = preparedStatement.getResultSet();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      statement = connection.createStatement();

      if (count > 0) {
        statement.execute("DROP USER " + cubridUser);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

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

    disconnect(connection);

    connection = connect(url);

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - End");
  }
}
