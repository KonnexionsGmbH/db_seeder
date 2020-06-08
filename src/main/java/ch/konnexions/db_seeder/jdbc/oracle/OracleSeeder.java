/**
 *
 */
package ch.konnexions.db_seeder.jdbc.oracle;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a Database. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class OracleSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(OracleSeeder.class);

  /**
   * 
   */
  public OracleSeeder() {
    super();
  }

  /**
   * Create a database connection.
   */
  @Override
  protected final void connect() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - Start");

    try {
      connection = DriverManager.getConnection(config.getOracleConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getOracleConnectionPort()
          + "/" + config.getOracleConnectionService(), config.getOracleUser(), config.getOraclePassword());

      connection.setAutoCommit(false);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - End");
  }

  /**
   * Create the DDL statement: CREATE TABLE.
   *
   * @param tableName the database table name
   *
   * @return the insert statement
   */
  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE city
             (
                 pk_city_id          NUMBER         GENERATED ALWAYS AS IDENTITY,
                 fk_country_state_id NUMBER,
                 city_map            BLOB,
                 created             TIMESTAMP      NOT NULL,
                 modified            TIMESTAMP,
                 name                VARCHAR2 (100) NOT NULL,
                 CONSTRAINT pk_city                 PRIMARY KEY (pk_city_id),
                 CONSTRAINT fk_city_country_state   FOREIGN KEY (fk_country_state_id) REFERENCES country_state (pk_country_state_id)
             )
             TABLESPACE users""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE company
             (
                 pk_company_id       NUMBER         GENERATED ALWAYS AS IDENTITY,
                 fk_city_id          NUMBER         NOT NULL,
                 active              VARCHAR2 (1)   NOT NULL,
                 address1            VARCHAR2 (50),
                 address2            VARCHAR2 (50),
                 address3            VARCHAR2 (50),
                 created             TIMESTAMP      NOT NULL,
                 directions          CLOB,
                 email               VARCHAR2 (100),
                 fax                 VARCHAR2 (20),
                 modified            TIMESTAMP,
                 name                VARCHAR2 (250) NOT NULL,
                 phone               VARCHAR2 (50),
                 postal_code         VARCHAR2 (20),
                 url                 VARCHAR2 (250),
                 vat_id_number       VARCHAR2 (50),
                 CONSTRAINT pk_company              PRIMARY KEY (pk_company_id),
                 CONSTRAINT fk_company_city         FOREIGN KEY (fk_city_id)          REFERENCES city (pk_city_id),
                 CONSTRAINT uq_company_name         UNIQUE (name)
             )
             TABLESPACE users""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE country
             (
                 pk_country_id NUMBER         GENERATED ALWAYS AS IDENTITY,
                 country_map   BLOB,
                 created       TIMESTAMP      NOT NULL,
                 iso3166       VARCHAR2 (2),
                 modified      TIMESTAMP,
                 name          VARCHAR2 (100) NOT NULL,
                 CONSTRAINT pk_country        PRIMARY KEY (pk_country_id),
                 CONSTRAINT uq_country_name   UNIQUE (name)
             )
             TABLESPACE users""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE country_state
             (
                 pk_country_state_id NUMBER         GENERATED ALWAYS AS IDENTITY,
                 fk_country_id       NUMBER         NOT NULL,
                 fk_timezone_id      NUMBER         NOT NULL,
                 country_state_map   BLOB,
                 created             TIMESTAMP      NOT NULL,
                 modified            TIMESTAMP,
                 name                VARCHAR2 (100) NOT NULL,
                 symbol              VARCHAR2 (10),
                 CONSTRAINT pk_country_state          PRIMARY KEY (pk_country_state_id),
                 CONSTRAINT fk_country_state_country  FOREIGN KEY (fk_country_id)  REFERENCES country  (pk_country_id),
                 CONSTRAINT fk_country_state_timezone FOREIGN KEY (fk_timezone_id) REFERENCES timezone (pk_timezone_id),
                 CONSTRAINT uq_country_state          UNIQUE (fk_country_id, name)
             )
             TABLESPACE users""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE timezone
             (
                 pk_timezone_id NUMBER          GENERATED ALWAYS AS IDENTITY,
                 abbreviation   VARCHAR2 (20)   NOT NULL,
                 created        TIMESTAMP       NOT NULL,
                 modified       TIMESTAMP,
                 name           VARCHAR2 (100)  NOT NULL,
                 v_time_zone    VARCHAR2 (4000),
                 CONSTRAINT pk_timezone         PRIMARY KEY (pk_timezone_id),
                 CONSTRAINT uq_timezone         UNIQUE (name)
             )
             TABLESPACE users""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }

  /**
   * Drop the schema / user if existing and create it new.
   */
  @Override
  protected void dropCreateSchemaUser() {
    int               count             = 0;

    PreparedStatement preparedStatement = null;

    // -----------------------------------------------------------------------
    // Connect as privileged user
    // -----------------------------------------------------------------------

    final String      jdbcUser          = config.getOracleUser();

    try {
      connection = DriverManager.getConnection(config.getOracleConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getOracleConnectionPort()
          + "/" + config.getOracleConnectionService(), "sys AS SYSDBA", config.getOraclePasswordSys());

      connection.setAutoCommit(false);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Drop the schema / user if already existing
    // -----------------------------------------------------------------------

    try {
      preparedStatement = connection.prepareStatement("SELECT count(*) FROM ALL_USERS WHERE username = UPPER(?)");
      preparedStatement.setString(1, jdbcUser);
      preparedStatement.executeUpdate();

      ResultSet resultSet = preparedStatement.getResultSet();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        preparedStatement = connection.prepareStatement("DROP USER " + jdbcUser + " CASCADE");
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
      preparedStatement = connection.prepareStatement("CREATE USER " + jdbcUser + " IDENTIFIED BY \"" + config.getOraclePassword() + "\"");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("ALTER USER " + jdbcUser + " QUOTA UNLIMITED ON users");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT CREATE SEQUENCE TO " + jdbcUser);
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT CREATE SESSION TO " + jdbcUser);
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT CREATE TABLE TO " + jdbcUser);
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT UNLIMITED TABLESPACE TO " + jdbcUser);
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
