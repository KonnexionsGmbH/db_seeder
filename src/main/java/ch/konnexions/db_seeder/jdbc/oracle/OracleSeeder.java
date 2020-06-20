/**
 *
 */
package ch.konnexions.db_seeder.jdbc.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for an Oracle DBMS. </h1>
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

    dbms = Dbms.ORACLE;

    url  = config.getOracleConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getOracleConnectionPort() + "/"
        + config.getOracleConnectionService();
  }

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
             )""";
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
                 name                VARCHAR2 (250) NOT NULL UNIQUE,
                 phone               VARCHAR2 (50),
                 postal_code         VARCHAR2 (20),
                 url                 VARCHAR2 (250),
                 vat_id_number       VARCHAR2 (50),
                 CONSTRAINT pk_company              PRIMARY KEY (pk_company_id),
                 CONSTRAINT fk_company_city         FOREIGN KEY (fk_city_id)          REFERENCES city (pk_city_id)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE country
             (
                 pk_country_id NUMBER         GENERATED ALWAYS AS IDENTITY,
                 country_map   BLOB,
                 created       TIMESTAMP      NOT NULL,
                 iso3166       VARCHAR2 (2),
                 modified      TIMESTAMP,
                 name          VARCHAR2 (100) NOT NULL UNIQUE,
                 CONSTRAINT pk_country        PRIMARY KEY (pk_country_id)
             )""";
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
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE timezone
             (
                 pk_timezone_id NUMBER          GENERATED ALWAYS AS IDENTITY,
                 abbreviation   VARCHAR2 (20)   NOT NULL,
                 created        TIMESTAMP       NOT NULL,
                 modified       TIMESTAMP,
                 name           VARCHAR2 (100)  NOT NULL UNIQUE,
                 v_time_zone    VARCHAR2 (4000),
                 CONSTRAINT pk_timezone         PRIMARY KEY (pk_timezone_id)
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

    connection = connect(url, null, "sys AS SYSDBA", config.getOraclePasswordSys());

    // -----------------------------------------------------------------------
    // Drop the database user if already existing.
    // -----------------------------------------------------------------------

    String oracleUser = config.getOracleUser();

    try {
      int count = 0;

      preparedStatement = connection.prepareStatement("SELECT count(*) FROM ALL_USERS WHERE username = UPPER(?)");
      preparedStatement.setString(1, oracleUser);
      preparedStatement.executeQuery();

      ResultSet resultSet = preparedStatement.getResultSet();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      statement = connection.createStatement();

      if (count > 0) {
        statement.execute("DROP USER " + oracleUser + " CASCADE");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create the database user and grant the necessary rights.
    // -----------------------------------------------------------------------

    try {
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

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - End");
  }
}
