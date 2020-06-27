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
   */
  public OracleSeeder() {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    dbms = Dbms.ORACLE;

    url  = config.getOracleConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getOracleConnectionPort() + "/"
        + config.getOracleConnectionService();

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
                 pk_city_id          NUMBER         NOT NULL PRIMARY KEY,
                 fk_country_state_id NUMBER,
                 city_map            BLOB,
                 created             TIMESTAMP      NOT NULL,
                 modified            TIMESTAMP,
                 name                VARCHAR2 (100) NOT NULL,
                 CONSTRAINT fk_city_country_state   FOREIGN KEY (fk_country_state_id) REFERENCES "COUNTRY_STATE" (pk_country_state_id)
             )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE "COMPANY"
             (
                 pk_company_id       NUMBER         NOT NULL PRIMARY KEY,
                 fk_city_id          NUMBER         NOT NULL,
                 active              VARCHAR2 (1)   NOT NULL,
                 address1            VARCHAR2 (50),
                 address2            VARCHAR2 (50),
                 address3            VARCHAR2 (50),
                 created             TIMESTAMP      NOT NULL,
                 directions          CLOB,
                 email               VARCHAR2 (100),
                 fax                 VARCHAR2 (50),
                 modified            TIMESTAMP,
                 name                VARCHAR2 (250) NOT NULL UNIQUE,
                 phone               VARCHAR2 (50),
                 postal_code         VARCHAR2 (50),
                 url                 VARCHAR2 (250),
                 vat_id_number       VARCHAR2 (100),
                 CONSTRAINT fk_company_city         FOREIGN KEY (fk_city_id)          REFERENCES "CITY" (pk_city_id)
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE "COUNTRY"
             (
                 pk_country_id NUMBER         NOT NULL PRIMARY KEY,
                 country_map   BLOB,
                 created       TIMESTAMP      NOT NULL,
                 iso3166       VARCHAR2 (50),
                 modified      TIMESTAMP,
                 name          VARCHAR2 (100) NOT NULL UNIQUE
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE "COUNTRY_STATE"
             (
                 pk_country_state_id NUMBER         NOT NULL PRIMARY KEY,
                 fk_country_id       NUMBER         NOT NULL,
                 fk_timezone_id      NUMBER         NOT NULL,
                 country_state_map   BLOB,
                 created             TIMESTAMP      NOT NULL,
                 modified            TIMESTAMP,
                 name                VARCHAR2 (100) NOT NULL,
                 symbol              VARCHAR2 (50),
                 CONSTRAINT fk_country_state_country  FOREIGN KEY (fk_country_id)  REFERENCES "COUNTRY"  (pk_country_id),
                 CONSTRAINT fk_country_state_timezone FOREIGN KEY (fk_timezone_id) REFERENCES "TIMEZONE" (pk_timezone_id),
                 CONSTRAINT uq_country_state          UNIQUE (fk_country_id, name)
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE "TIMEZONE"
             (
                 pk_timezone_id NUMBER          NOT NULL PRIMARY KEY,
                 abbreviation   VARCHAR2 (50)   NOT NULL,
                 created        TIMESTAMP       NOT NULL,
                 modified       TIMESTAMP,
                 name           VARCHAR2 (100)  NOT NULL UNIQUE,
                 v_time_zone    VARCHAR2 (4000)
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  private final void dropUser(String oracleUser) {
    try {
      int count = 0;

      preparedStatement = connection.prepareStatement("SELECT count(*) FROM ALL_USERS WHERE username = UPPER(?)");
      preparedStatement.setString(1, oracleUser);

      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      preparedStatement.close();

      if (count > 0) {
        statement = connection.createStatement();

        statement.execute("DROP USER " + oracleUser + " CASCADE");

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

    connection = connect(url, null, "sys AS SYSDBA", config.getOraclePasswordSys());

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
