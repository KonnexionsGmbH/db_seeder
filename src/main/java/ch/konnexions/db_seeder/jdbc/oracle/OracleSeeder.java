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
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    OracleSeeder oracleSeeder = new OracleSeeder();

    String       methodName   = new Object() {
                              }.getClass().getEnclosingMethod().getName();
    logger.info(String.format(oracleSeeder.FORMAT_METHOD_NAME, methodName) + " - Start =======================================================");

    oracleSeeder.createData();

    logger.info(String.format(oracleSeeder.FORMAT_METHOD_NAME, methodName) + " - End   =======================================================");
  }

  /**
   * 
   */
  protected OracleSeeder() {
    config = new Config();
  }

  /**
   * Add optional foreign keys related to current database table.
   *
   * Foreign Keys              ===> DB Table            
   * ------------- ----------- ---- ------------------- 
   * COUNTRY_STATE             ===> CITY
   *
   * @param tableName the table name
   * @param fKList the foreign key list
   */
  protected final void addOptionalFk(final String tableName, final ArrayList<Object> fKList) {
    switch (tableName) {
    case TABLE_NAME_COUNTRY_STATE:
      if (pkListCity == null) {
        recreatePkList(TABLE_NAME_CITY);
      }

      addOptionalFk(TABLE_NAME_CITY, "PK_CITY_ID", pkListCity, "FK_COUNTRY_STATE_ID", fKList);
      break;
    }
  }

  /**
   * Create a database connection.
   */
  protected final void connect() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + " - Start");

    final String jdbcConnectionHost    = config.getJdbcConnectionHost();
    final String jdbcConnectionService = config.getJdbcConnectionService();
    final int    jdbcConnectionPort    = config.getJdbcConnectionPort();
    final String jdbcOraclePassword    = config.getJdbcOraclePassword();
    final String jdbcOracleUser        = config.getJdbcOracleUser();

    try {
      connection = DriverManager.getConnection("jdbc:oracle:thin:@//" + jdbcConnectionHost + ":" + jdbcConnectionPort + "/" + jdbcConnectionService,
                                               jdbcOracleUser,
                                               jdbcOraclePassword);

      connection.setAutoCommit(false);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + " - End");
  }

  /**
   * Create the test data for all database tables.
   *
   * The following database tables are currently supported:
   * <br>
   * <br>
   * <ul>
   * <li>CITY</li>
   * <li>COMPANY</li>
   * <li>COUNTRY</li>
   * <li>COUNTRY_STATE</li>
   * <li>TIMEZONE</li>
   * </ul>
   */
  public final void createData() {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + " - Start");

    dropCreateSchemaUser();

    // Level 1 -------------------------------------------------------------
    createData(TABLE_NAME_COUNTRY, config.getMaxRowCountry());
    createData(TABLE_NAME_TIMEZONE, config.getMaxRowTimezone());

    // Level 2 -------------------------------------------------------------
    createData(TABLE_NAME_COUNTRY_STATE, config.getMaxRowCountryState());

    // Level 3 -------------------------------------------------------------
    createData(TABLE_NAME_CITY, config.getMaxRowCity());

    // Level 4 -------------------------------------------------------------
    createData(TABLE_NAME_COMPANY, config.getMaxRowCompany());

    disconnect();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + " - End");
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
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  /**
   * Create the DML statement: INSERT.
   *
   * @param tableName the database table name
   *
   * @return the insert statement
   */
  @Override
  protected final String createDmlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return "fk_country_state_id,city_map,created,modified,name) VALUES (?,?,?,?,?";
    case TABLE_NAME_COMPANY:
      return "fk_city_id,active,address1,address2,address3,created,directions,email,fax,modified,name,phone,postal_code,url,vat_id_number) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
    case TABLE_NAME_COUNTRY:
      return "country_map,created,iso3166,modified,name) VALUES (?,?,?,?,?";
    case TABLE_NAME_COUNTRY_STATE:
      return "fk_country_id,fk_timezone_id,country_state_map,created,modified,name,symbol) VALUES (?,?,?,?,?,?,?";
    case TABLE_NAME_TIMEZONE:
      return "abbreviation,created,modified,name,v_time_zone) VALUES (?,?,?,?,?";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  /**
   * Create the not yet existing mandatory foreign keys.
   *
   * DB Table            ===> Foreign Keys
   * ------------------- ---- -------------
   * COMPANY             ===> CITY
   * COUNTRY_STATE       ===> COUNTRY
   *                          TIMEZONE
   *
   * @param tableName the database table name
   */
  protected final void createFkList(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_COMPANY:
      if (pkListCity.size() == 0) {
        createData(TABLE_NAME_CITY, config.getMaxRowCity());
      }

      break;
    case TABLE_NAME_COUNTRY_STATE:
      if (pkListCountry.size() == 0) {
        createData(TABLE_NAME_COUNTRY, config.getMaxRowCountry());
      }

      if (pkListTimezone.size() == 0) {
        createData(TABLE_NAME_TIMEZONE, config.getMaxRowTimezone());
      }

      break;
    }

    if (TABLE_NAME_COMPANY.equals(tableName)) {
      if (pkListCity.size() == 0) {
        if (countData(TABLE_NAME_CITY) == 0) {
          createData(TABLE_NAME_CITY, config.getMaxRowCity());
        } else {
          recreatePkList(TABLE_NAME_CITY);
        }
      }
    }

    if (TABLE_NAME_COUNTRY_STATE.equals(tableName)) {
      if (pkListCountry.size() == 0) {
        if (countData(TABLE_NAME_COUNTRY) == 0) {
          createData(TABLE_NAME_COUNTRY, config.getMaxRowCountry());
        } else {
          recreatePkList(TABLE_NAME_COUNTRY);
        }
      }

      if (pkListTimezone.size() == 0) {
        if (countData(TABLE_NAME_TIMEZONE) == 0) {
          createData(TABLE_NAME_TIMEZONE, config.getMaxRowTimezone());
        } else {
          recreatePkList(TABLE_NAME_TIMEZONE);
        }
      }
    }
  }

  protected void dropCreateSchemaUser() {
    int               count                 = 0;

    PreparedStatement preparedStatement     = null;

    // -----------------------------------------------------------------------
    // Connect as privileged user
    // -----------------------------------------------------------------------

    final String      jdbcConnectionHost    = config.getJdbcConnectionHost();
    final String      jdbcConnectionService = config.getJdbcConnectionService();
    final int         jdbcConnectionPort    = config.getJdbcConnectionPort();
    final String      jdbcOraclePassword    = config.getJdbcOraclePasswordSys();
    final String      jdbcOracleUser        = config.getJdbcOracleUserSys();

    try {
      connection = DriverManager.getConnection("jdbc:oracle:thin:@//" + jdbcConnectionHost + ":" + jdbcConnectionPort + "/" + jdbcConnectionService,
                                               jdbcOracleUser + " AS SYSDBA",
                                               jdbcOraclePassword);

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
      preparedStatement.setString(1, config.getJdbcOracleUser());
      preparedStatement.executeUpdate();

      ResultSet resultSet = preparedStatement.getResultSet();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        preparedStatement = connection.prepareStatement("DROP USER " + config.getJdbcOracleUser() + " CASCADE");
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
      preparedStatement = connection
          .prepareStatement("CREATE USER " + config.getJdbcOracleUser() + " IDENTIFIED BY \"" + config.getJdbcOraclePassword() + "\"");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("ALTER USER " + config.getJdbcOracleUser() + " QUOTA UNLIMITED ON users");
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT CREATE SEQUENCE TO " + config.getJdbcOracleUser());
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT CREATE SESSION TO " + config.getJdbcOracleUser());
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT CREATE TABLE TO " + config.getJdbcOracleUser());
      preparedStatement.executeUpdate();

      preparedStatement = connection.prepareStatement("GRANT UNLIMITED TABLESPACE TO " + config.getJdbcOracleUser());
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
   * Get the default number of required database rows.
   *
   * @param tableName the database table name
   *
   * @return the default number of required database rows
   */
  protected final int getDefaultRowSize(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return 1200;
    case TABLE_NAME_COMPANY:
      return 6000;
    case TABLE_NAME_COUNTRY:
      return 100;
    case TABLE_NAME_COUNTRY_STATE:
      return 400;
    case TABLE_NAME_TIMEZONE:
      return 11;
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  /**
   * Prepare the variable part of the INSERT statement.
   *
   * @param preparedStatement the prepared statement
   * @param tableName the database table name
   * @param rowCount the total row count
   * @param rowNo the current row number
   * @param pkList current primary key list
   */
  protected final void prepDmlStmntInsert(final PreparedStatement preparedStatement,
                                          final String tableName,
                                          final int rowCount,
                                          final int rowNo,
                                          final ArrayList<Object> pkList) {
    String identifier04 = String.format(FORMAT_IDENTIFIER, rowNo);

    switch (tableName) {
    case TABLE_NAME_CITY:
      prepDmlStmntInsertCity(preparedStatement, rowCount, identifier04);
      break;
    case TABLE_NAME_COMPANY:
      prepDmlStmntInsertCompany(preparedStatement, rowCount, identifier04);
      break;
    case TABLE_NAME_COUNTRY:
      prepDmlStmntInsertCountry(preparedStatement, rowCount, identifier04);
      break;
    case TABLE_NAME_COUNTRY_STATE:
      prepDmlStmntInsertCountryState(preparedStatement, rowCount, identifier04);
      break;
    case TABLE_NAME_TIMEZONE:
      prepDmlStmntInsertTimezone(preparedStatement, rowCount, identifier04);
      break;
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
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
   */
  protected final void prepDmlStmntInsertCity(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(1, java.sql.Types.INTEGER);
      } else {
        prepStmntInsertColFKOpt(1, pkListCountryState, preparedStatement, rowCount);
      }

      //      if (getRandomIntIncluded(rowCount) % 4 == 0) {
      //        preparedStatement.setNull(2, java.sql.Types.BLOB);
      //      } else {
      //        try {
      //          preparedStatement.setBinaryStream(2, new FileInputStream(IMAGE_FILE));
      //        } catch (FileNotFoundException e) {
      //          e.printStackTrace();
      //          System.exit(1);
      //        }
      //      }

      preparedStatement.setNull(2, java.sql.Types.BLOB);

      preparedStatement.setTimestamp(3, getRandomTimestamp());

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(4, java.sql.Types.DATE);
      } else {
        preparedStatement.setTimestamp(4, getRandomTimestamp());
      }

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
  protected final void prepDmlStmntInsertCompany(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      preparedStatement.setObject(1, pkListCity.get(getRandomIntExcluded(pkListCity.size())));
      prepStmntInsertColFlagNY(2, preparedStatement, rowCount);

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(3, java.sql.Types.VARCHAR);
      } else {
        preparedStatement.setString(3, "ADDRESS1_" + identifier04);
      }

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(4, java.sql.Types.VARCHAR);
      } else {
        preparedStatement.setString(4, "ADDRESS2_" + identifier04);
      }

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(5, java.sql.Types.VARCHAR);
      } else {
        preparedStatement.setString(5, "ADDRESS3_" + identifier04);
      }

      preparedStatement.setTimestamp(6, getRandomTimestamp());

      //      if (getRandomIntIncluded(rowCount) % 4 == 0) {
      //        preparedStatement.setNull(7, java.sql.Types.CLOB);
      //      } else {
      //        preparedStatement.setClob(7, new SerialClob(("DIRECTIONS_" + identifier04).toCharArray()));
      //      }

      preparedStatement.setNull(7, java.sql.Types.CLOB);

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(8, java.sql.Types.VARCHAR);
      } else {
        preparedStatement.setString(8, "EMAIL_" + identifier04);
      }

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(9, java.sql.Types.VARCHAR);
      } else {
        preparedStatement.setString(9, "FAX_" + identifier04);
      }

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(10, java.sql.Types.DATE);
      } else {
        preparedStatement.setTimestamp(10, getRandomTimestamp());
      }

      preparedStatement.setString(11, "NAME_" + identifier04);

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(12, java.sql.Types.VARCHAR);
      } else {
        preparedStatement.setString(12, "PHONE_" + identifier04);
      }

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(13, java.sql.Types.VARCHAR);
      } else {
        preparedStatement.setString(13, "POSTAL_CODE_" + identifier04);
      }

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(14, java.sql.Types.VARCHAR);
      } else {
        preparedStatement.setString(14, "URL_" + identifier04);
      }

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(15, java.sql.Types.VARCHAR);
      } else {
        preparedStatement.setString(15, "VAT_ID_NUMBER__" + identifier04);
      }
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
      //      if (getRandomIntIncluded(rowCount) % 4 == 0) {
      //        System.out.println("null - start");
      //        preparedStatement.setNull(1, java.sql.Types.BLOB);
      //        System.out.println("null - end");
      //      } else {
      //        try {
      //          System.out.println("not null - start");
      //          System.out.println("not null - length="+IMAGE_FILE.length());
      //          preparedStatement.setBlob(1, new FileInputStream(IMAGE_FILE), IMAGE_FILE.length());
      //          System.out.println("not null - end");
      //        } catch (FileNotFoundException e) {
      //          e.printStackTrace();
      //          System.exit(1);
      //        }
      //      }

      preparedStatement.setNull(1, java.sql.Types.BLOB);

      preparedStatement.setTimestamp(2, getRandomTimestamp());
      prepStmntInsertColStringOpt(3, "", preparedStatement, rowCount, identifier04.substring(2));

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(4, java.sql.Types.DATE);
      } else {
        preparedStatement.setTimestamp(4, getRandomTimestamp());
      }

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
  protected final void prepDmlStmntInsertCountryState(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      preparedStatement.setObject(1, pkListCountry.get(getRandomIntExcluded(pkListCountry.size())));
      preparedStatement.setObject(2, pkListTimezone.get(getRandomIntExcluded(pkListTimezone.size())));

      //    if (getRandomIntIncluded(rowCount) % 4 == 0) {
      //    System.out.println("null - start");
      //    preparedStatement.setNull(1, java.sql.Types.BLOB);
      //    System.out.println("null - end");
      //  } else {
      //    try {
      //      System.out.println("not null - start");
      //      System.out.println("not null - length="+IMAGE_FILE.length());
      //      preparedStatement.setBlob(1, new FileInputStream(IMAGE_FILE), IMAGE_FILE.length());
      //      System.out.println("not null - end");
      //    } catch (FileNotFoundException e) {
      //      e.printStackTrace();
      //      System.exit(1);
      //    }
      //  }

      preparedStatement.setNull(3, java.sql.Types.BLOB);

      preparedStatement.setTimestamp(4, getRandomTimestamp());

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(5, java.sql.Types.DATE);
      } else {
        preparedStatement.setTimestamp(5, getRandomTimestamp());
      }

      preparedStatement.setString(6, "NAME_" + identifier04);

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(7, java.sql.Types.VARCHAR);
      } else {
        prepStmntInsertColStringOpt(7, "SYMBOL_", preparedStatement, rowCount, identifier04.substring(1));
      }
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
  protected final void prepDmlStmntInsertTimezone(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      preparedStatement.setString(1, "ABBREVIATION_" + identifier04);
      preparedStatement.setTimestamp(2, getRandomTimestamp());

      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(3, java.sql.Types.DATE);
      } else {
        preparedStatement.setTimestamp(3, getRandomTimestamp());
      }

      preparedStatement.setString(4, "NAME_" + identifier04);
      prepStmntInsertColStringOpt(5, "V_TIME_ZONE_", preparedStatement, rowCount, identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Retrieve the missing foreign keys from the database.
   *
   * DB Table            ===> Foreign Keys              Open
   * ------------------- ---- ------------- ----------- ----
   * CITY                ===> COUNTRY_STATE
   * COMPANY             ===> CITY
   * COUNTRY_STATE       ===> COUNTRY
   *                          TIMEZONE
   *
   * @param tableName the database table name
   */
  protected final void retrieveFkList(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      if (pkListCountryState.size() == 0) {
        recreatePkList(TABLE_NAME_COUNTRY_STATE);
      }

      break;
    case TABLE_NAME_COMPANY:
      if (pkListCity.size() == 0) {
        recreatePkList(TABLE_NAME_CITY);
      }

      break;
    case TABLE_NAME_COUNTRY_STATE:
      if (pkListCountry.size() == 0) {
        recreatePkList(TABLE_NAME_COUNTRY);
      }

      if (pkListTimezone.size() == 0) {
        recreatePkList(TABLE_NAME_TIMEZONE);
      }

      break;
    }
  }

  /**
   * Save the database table specific primary key list.
   *
   * @param tableName the table name
   * @param pkList current primary key list
   */
  protected final void savePkList(final String tableName, final ArrayList<Object> pkList) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      pkListCity = pkList;
      break;
    case TABLE_NAME_COMPANY:
      pkListCompany = pkList;
      break;
    case TABLE_NAME_COUNTRY:
      pkListCountry = pkList;
      break;
    case TABLE_NAME_COUNTRY_STATE:
      pkListCountryState = pkList;
      break;
    case TABLE_NAME_TIMEZONE:
      pkListTimezone = pkList;
      break;
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }
}
