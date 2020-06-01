/**
 *
 */
package ch.konnexions.db_seeder.jdbc.oracle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialClob;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.Config;
import ch.konnexions.db_seeder.DatabaseSeeder;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * <h1> Test Data Generator for a Database. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @version 1.0.0
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

    final String jdbcConnectionHost      = config.getJdbcConnectionHost();
    final String oracleConnectionService = config.getOracleConnectionService();
    final int    jdbcConnectionPort      = config.getJdbcConnectionPort();
    final String oraclePassword          = config.getOraclePassword();
    final String oracleUser              = config.getOracleUser();

    try {
      connection = DriverManager
          .getConnection("jdbc:oracle:thin:@//" + jdbcConnectionHost + ":" + jdbcConnectionPort + "/" + oracleConnectionService, oracleUser, oraclePassword);

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
    CONSTRAINT pk_city PRIMARY KEY (pk_city_id),
    CONSTRAINT fk_city_country_state FOREIGN KEY (fk_country_state_id) REFERENCES country_state (pk_country_state_id)
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
    CONSTRAINT pk_company            PRIMARY KEY (pk_company_id),
    CONSTRAINT fk_company_city       FOREIGN KEY (fk_city_id)          REFERENCES city (pk_city_id),
    CONSTRAINT uq_company_name       UNIQUE (name)
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
    CONSTRAINT pk_country      PRIMARY KEY (pk_country_id),
    CONSTRAINT uq_country_name UNIQUE (name)
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
    CONSTRAINT fk_country_state_country  FOREIGN KEY (fk_country_id) REFERENCES country (pk_country_id),
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
    CONSTRAINT pk_timezone PRIMARY KEY (pk_timezone_id),
    CONSTRAINT uq_timezone UNIQUE (name)
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
    int               count                   = 0;

    PreparedStatement preparedStatement       = null;

    // -----------------------------------------------------------------------
    // Connect as privileged user
    // -----------------------------------------------------------------------

    final String      jdbcConnectionHost      = config.getJdbcConnectionHost();
    final int         jdbcConnectionPort      = config.getJdbcConnectionPort();
    final String      oracleConnectionService = config.getOracleConnectionService();
    final String      oraclePassword          = config.getOraclePasswordSys();
    final String      oracleUser              = config.getOracleUserSys();

    try {
      connection = DriverManager.getConnection("jdbc:oracle:thin:@//" + jdbcConnectionHost + ":" + jdbcConnectionPort + "/" + oracleConnectionService,
                                               oracleUser + " AS SYSDBA",
                                               oraclePassword);

      connection.setAutoCommit(false);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Drop the schema / user if already existing
    // -----------------------------------------------------------------------

    try {
      preparedStatement = connection.prepareStatement("SELECT count(*) FROM ALL_USERS WHERE username = ?");
      preparedStatement.setString(1, config.getOracleUser());
      preparedStatement.executeUpdate();

      ResultSet resultSet = preparedStatement.getResultSet();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        preparedStatement = connection.prepareStatement("DROP USER " + config.getOracleUser() + " CASCADE");
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
      preparedStatement = connection.prepareStatement("CREATE USER " + config.getOracleUser() + " IDENTIFIED BY \"" + config.getOraclePassword() + "\"");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("ALTER USER " + config.getOracleUser() + " QUOTA UNLIMITED ON users");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT CREATE SEQUENCE TO " + config.getOracleUser());
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT CREATE SESSION TO " + config.getOracleUser());
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT CREATE TABLE TO " + config.getOracleUser());
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT UNLIMITED TABLESPACE TO " + config.getOracleUser());
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

  /**
   * Prepare the variable part of the INSERT statement - CITY.
   *
   * PK_CITY_ID NUMBER   NUMBER     PK
   * FK_COUNTRY_STATE_ID NUMBER     FK NULLABLE
   * CITY_MAP            BLOB          NULLABLE
   * CREATED             TIMESTAMP    
   * MODIFIED            TIMESTAMP     NULLABLE
   * NAME 	             VARCHAR   100
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   * @throws IOException 
   */
  @Override
  protected final void prepDmlStmntInsertCity(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      prepStmntInsertColFKOpt(1, pkListCountryState, preparedStatement, rowCount);
      prepStmntInsertColBlobOpt(2, preparedStatement, rowCount);
      preparedStatement.setTimestamp(3, getRandomTimestamp());
      preparedStatement.setTimestamp(4, getRandomTimestamp());
      preparedStatement.setString(5, "NAME_" + identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - COMPANY.
   *
   * PK_COMPANY_ID         NUMBER     PK
   * FK_CITY_ID            NUMBER     FK
   * ACTIVE                VARCHAR     1
   * ADDRESS1              VARCHAR    50 NULLABLE
   * ADDRESS2              VARCHAR    50 NULLABLE
   * ADDRESS3              VARCHAR    50 NULLABLE
   * CREATED               TIMESTAMP    
   * DIRECTIONS            CLOB          NULLABLE
   * EMAIL                 VARCHAR   100 NULLABLE
   * FAX                   VARCHAR    20 NULLABLE
   * MODIFIED              TIMESTAMP     NULLABLE
   * NAME 	               VARCHAR   250
   * PHONE                 VARCHAR    50 NULLABLE
   * POSTAL_CODE           VARCHAR    20 NULLABLE
   * URL                   VARCHAR   250 NULLABLE
   * VAT_ID_NUMBER         VARCHAR    50 NULLABLE
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  @Override
  protected final void prepDmlStmntInsertCompany(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      preparedStatement.setObject(1, pkListCity.get(getRandomIntExcluded(pkListCity.size())));
      prepStmntInsertColFlagNY(2, preparedStatement, rowCount);
      prepStmntInsertColStringOpt(3, "ADDRESS1_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(4, "ADDRESS2_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(5, "ADDRESS3_", preparedStatement, rowCount, identifier04);
      preparedStatement.setTimestamp(6, getRandomTimestamp());
      prepStmntInsertColClobOpt(7, preparedStatement, rowCount);
      prepStmntInsertColStringOpt(8, "EMAIL_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(9, "FAX_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColDatetimeOpt(10, preparedStatement, rowCount);
      preparedStatement.setString(11, "NAME_" + identifier04);
      prepStmntInsertColStringOpt(12, "PHONE_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(13, "POSTAL_CODE_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(14, "URL_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(15, "VAT_ID_NUMBER__", preparedStatement, rowCount, identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - COUNTRY.
   *
   * PK_COUNTRY_ID NUMBER    PK
   * COUNTRY_MAP   BLOB          NULLABLE
   * CREATED       TIMESTAMP    
   * ISO3166       VARCHAR     2 NULLABLE
   * MODIFIED      TIMESTAMP     NULLABLE
   * NAME 	       VARCHAR   100
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  protected final void prepDmlStmntInsertCountry(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      prepStmntInsertColBlobOpt(1, preparedStatement, rowCount);
      preparedStatement.setTimestamp(2, getRandomTimestamp());
      prepStmntInsertColStringOpt(3, "", preparedStatement, rowCount, identifier04.substring(2));
      prepStmntInsertColDatetimeOpt(4, preparedStatement, rowCount);
      preparedStatement.setString(5, "NAME_" + identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - COUNTRY_STATE.
   *
   * PK_COUNTRY_STATE_ID NUMBER   PK
   * FK_COUNTRY_ID       NUMBER   FK
   * FK_TIMEZONE_ID      NUMBER   FK
   * COUNTRY_STATE_MAP   BLOB        NULLABLE
   * NAME 	             VARCHAR 100
   * SYMBOL              VARCHAR  10 NULLABLE
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  @Override
  protected final void prepDmlStmntInsertCountryState(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      preparedStatement.setObject(1, pkListCountry.get(getRandomIntExcluded(pkListCountry.size())));
      preparedStatement.setObject(2, pkListTimezone.get(getRandomIntExcluded(pkListTimezone.size())));
      prepStmntInsertColBlobOpt(3, preparedStatement, rowCount);
      preparedStatement.setTimestamp(4, getRandomTimestamp());
      prepStmntInsertColDatetimeOpt(5, preparedStatement, rowCount);
      preparedStatement.setString(6, "NAME_" + identifier04);
      prepStmntInsertColStringOpt(7, "SYMBOL_", preparedStatement, rowCount, identifier04.substring(1));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - TIMEZONE.
   *
   * PK_TIMEZONE_ID NUMBER      PK
   * ABBREVIATION   VARCHAR     20
   * CREATED        TIMESTAMP    
   * MODIFIED       TIMESTAMP      NULLABLE
   * NAME 	        VARCHAR    100
   * V_TIME_ZONE    VARCHAR   4000 NULLABLE
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  @Override
  protected final void prepDmlStmntInsertTimezone(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      preparedStatement.setString(1, "ABBREVIATION_" + identifier04);
      preparedStatement.setTimestamp(2, getRandomTimestamp());
      preparedStatement.setTimestamp(3, getRandomTimestamp());
      preparedStatement.setString(4, "NAME_" + identifier04);
      prepStmntInsertColStringOpt(5, "V_TIME_ZONE_", preparedStatement, rowCount, identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

}
