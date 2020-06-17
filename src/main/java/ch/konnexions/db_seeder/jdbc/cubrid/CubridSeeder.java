/**
 *
 */
package ch.konnexions.db_seeder.jdbc.cubrid;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    databaseDbms = DatabaseDbms.CUBRID;
  }

  @Override
  protected final void connect() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - Start");

    try {
      connection = DriverManager.getConnection(config.getCubridConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getCubridConnectionPort()
          + ":" + config.getCubridDatabase() + ":dba::", config.getCubridUser(), config.getCubridPassword());

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
  protected void dropCreateSchemaUser() {
    try {
      Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Connect as privileged user
    // -----------------------------------------------------------------------

    final String cubridUser = config.getCubridUser();

    try {
      String url = config.getCubridConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getCubridConnectionPort() + ":"
          + config.getCubridDatabase() + ":::";

      logger.info("url='" + url + "'");

      connection = DriverManager.getConnection(url);

      connection.setAutoCommit(true);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Drop the schema / user if already existing
    // -----------------------------------------------------------------------

    PreparedStatement preparedStatement = null;

    try {
      int count = 0;

      preparedStatement = connection.prepareStatement("SELECT count(*) FROM DB_USER WHERE name = UPPER(?)");
      preparedStatement.setString(1, cubridUser);
      preparedStatement.executeQuery();

      ResultSet resultSet = preparedStatement.getResultSet();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        preparedStatement = connection.prepareStatement("DROP USER " + cubridUser);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create the schema / user and grant the necessary rights.
    // -----------------------------------------------------------------------

    try {
      preparedStatement = connection.prepareStatement("CREATE USER " + cubridUser + " PASSWORD \"" + config.getCubridPassword() + "\" GROUPS dba");
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
}
