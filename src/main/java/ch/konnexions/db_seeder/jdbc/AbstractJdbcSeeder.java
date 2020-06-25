/**
 *
 */
package ch.konnexions.db_seeder.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.AbstractDatabaseSeeder;
import ch.konnexions.db_seeder.Config;

/**
 * Test Data Generator for a Database.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public abstract class AbstractJdbcSeeder extends AbstractDatabaseSeeder {

  private static Logger       logger            = Logger.getLogger(AbstractJdbcSeeder.class);

  protected final String      BLOB_FILE         = Paths.get("src", "main", "resources").toAbsolutePath().toString() + File.separator + "blob.png";
  protected final byte[]      BLOB_DATA_BYTES   = readBlobFile2Bytes();

  protected final String      CLOB_FILE         = Paths.get("src", "main", "resources").toAbsolutePath().toString() + File.separator + "clob.md";
  private final String        CLOB_DATA         = readClobFile();
  protected Connection        connection        = null;

  protected String            driver            = "";
  protected String            dropTableStmnt    = "";

  private final int           MAX_ROW_SIZE      = Integer.MAX_VALUE;

  protected PreparedStatement preparedStatement = null;

  private final int           RANDOM_NUMBER     = 4;
  private Random              randomInt         = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
  protected ResultSet         resultSet         = null;

  protected Statement         statement         = null;

  protected String            url               = "";
  protected String            urlBase           = "";
  protected String            urlSetup          = "";

  /**
   * Instantiates a new abstract JDBC seeder.
   */
  public AbstractJdbcSeeder() {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    config = new Config();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  /**
   * Instantiates a new abstract JDBC seeder.
   *
   * @param isClient client database version
   */
  public AbstractJdbcSeeder(boolean isClient) {
    super(isClient);

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    config = new Config();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  private final void addOptionalFk(final String tableName, final ArrayList<Object> fKList) {
    switch (tableName) {
    case TABLE_NAME_COUNTRY_STATE:
      if (pkListCity == null) {
        recreatePkList(TABLE_NAME_CITY);
      }

      addOptionalFk(TABLE_NAME_CITY, "PK_CITY_ID", pkListCity, "FK_COUNTRY_STATE_ID", fKList);
      break;
    }
  }

  private final void addOptionalFk(final String tableName,
                                   final String pkcolumnName,
                                   final ArrayList<Object> pkList,
                                   final String fkColumnName,
                                   final ArrayList<Object> fkList) {
    int pkListSize = pkList.size();
    if (pkListSize == 0) {
      return;
    }

    int fkListSize = fkList.size();
    if (fkListSize == 0) {
      return;
    }

    pkListSize++;

    try {
      preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET " + fkColumnName + " = ? WHERE " + pkcolumnName + " = ?");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    for (Object pkIndex : pkList) {
      if (getRandomIntExcluded(pkListSize) % RANDOM_NUMBER == 0) {
        continue;
      }

      try {
        preparedStatement.setObject(1, fkList.get(getRandomIntExcluded(fkListSize)));
        preparedStatement.setObject(2, pkIndex);

        preparedStatement.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void checkData(String tableName, int expectedRows) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    int count = 0;

    try {
      statement = connection.createStatement();

      resultSet = statement.executeQuery("SELECT count(*) FROM " + tableName);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (expectedRows == count) {
      logger.info(String.format(FORMAT_METHOD_NAME, methodName) + "- database table " + String.format(FORMAT_TABLE_NAME, tableName) + " - "
          + String.format(FORMAT_ROW_NO, count) + " rows created");
    } else {
      logger.fatal(String.format(FORMAT_METHOD_NAME, methodName) + "- database table " + String.format(FORMAT_TABLE_NAME, tableName)
          + " is incomplete - expected" + String.format(FORMAT_ROW_NO, expectedRows) + " rows - found " + String.format(FORMAT_ROW_NO, count) + " rows");
      System.exit(1);
    }

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @return the database connection
   */
  protected final Connection connect(String url) {
    return connect(url, null);
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @param driver the database driver
   * 
   * @return the database connection
   */
  protected final Connection connect(String url, String driver) {
    return connect(url, driver, null, null);
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @param driver the database driver
   * @param user the user
   * @param password the password
   * 
   * @return the database connection
   */
  protected final Connection connect(String url, String driver, String user, String password) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    if (driver != null) {
      try {
        logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- driver='" + driver + "'");
        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    Connection connection = null;

    try {
      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- url   ='" + url + "'");
      if (user == null && password == null) {
        connection = DriverManager.getConnection(url);
      } else {
        logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- user  ='" + user + "' password='" + password + "'");
        connection = DriverManager.getConnection(url, user, password);
      }

      if (dbms == Dbms.CRATEDB || dbms == Dbms.FIREBIRD) {
        connection.setAutoCommit(true);
      } else {
        connection.setAutoCommit(false);
      }

      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- auto  =" + connection.getAutoCommit());
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   [" + connection.toString() + "]");

    return connection;
  }

  private final int countData(final String tableName) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    int count = 0;

    try {
      statement = connection.createStatement();

      String sqlStmnt = "SELECT count(*) FROM " + tableName;

      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- sql='" + sqlStmnt + "'");

      resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");

    return count;
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
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    setupDatabase();

    // Level 1 -------------------------------------------------------------
    createData(TABLE_NAME_COUNTRY, config.getMaxRowCountry());
    createData(dbms == Dbms.CUBRID ? TABLE_NAME_TIMEZONE_CUBRID : TABLE_NAME_TIMEZONE, config.getMaxRowTimezone());

    // Level 2 -------------------------------------------------------------
    createData(TABLE_NAME_COUNTRY_STATE, config.getMaxRowCountryState());

    // Level 3 -------------------------------------------------------------
    createData(TABLE_NAME_CITY, config.getMaxRowCity());

    // Level 4 -------------------------------------------------------------
    createData(TABLE_NAME_COMPANY, config.getMaxRowCompany());

    disconnect(connection);

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }

  private final void createData(String tableName, int rowCount) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start - database table " + String.format(FORMAT_TABLE_NAME, tableName) + " - "
        + String.format(FORMAT_ROW_NO, rowCount) + " rows to be created");

    tableName = tableName.toUpperCase();

    if (rowCount < 1) {
      rowCount = getDefaultRowSize(tableName);
    } else if (rowCount > MAX_ROW_SIZE) {
      rowCount = MAX_ROW_SIZE;
    }

    try {
      statement = connection.createStatement();

      String sqlStmnt = createDdlStmnt(tableName);

      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- sql='" + sqlStmnt + "'");

      statement.execute(sqlStmnt);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    final int countExisting = countData(tableName);

    if (countExisting != 0) {
      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + " - found existing test data in database table "
          + String.format(FORMAT_TABLE_NAME, tableName));

      return;
    }

    ArrayList<Object> pkList = new ArrayList<Object>();

    retrieveFkList(tableName);
    createFkList(tableName);

    if (dbms == Dbms.CRATEDB) {
      autoIncrement = 0;
    }

    createDataInsert(tableName, rowCount, pkList);

    addOptionalFk(tableName, pkList);

    if (!(dbms == Dbms.CRATEDB)) {
      try {
        connection.commit();
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    savePkList(tableName, pkList);

    checkData(tableName, rowCount);

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }

  /**
   * Creates the data insert.
   *
   * @param tableName the table name
   * @param rowCount the total row count
   * @param pkList current primary key list
   */
  protected void createDataInsert(String tableName, int rowCount, ArrayList<Object> pkList) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    final String      sqlStmnt          = "INSERT INTO " + tableName + " (" + createDmlStmnt(tableName) + ")";

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- sql='" + sqlStmnt + "'");

    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(sqlStmnt,
                                                      new String[] {
                                                          "PK_" + tableName + "_ID" });
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    for (int rowNo = 1; rowNo <= rowCount; rowNo++) {
      prepDmlStmntInsert(preparedStatement, tableName, rowCount, rowNo, pkList);

      try {
        preparedStatement.executeUpdate();

        resultSet = preparedStatement.getGeneratedKeys();

        while (resultSet.next()) {
          pkList.add((int) resultSet.getLong(1));
        }

        resultSet.close();
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }

  /**
   * Create the DDL statement: CREATE TABLE.
   *
   * @param tableName the database table name
   *
   * @return the 'CREATE TABLE' statement
   */
  protected abstract String createDdlStmnt(String tableName);

  /**
   * Create the DML statement: INSERT.
   *
   * @param tableName the database table name
   *
   * @return the variable part of the 'INSERt' statement
   */
  protected final String createDmlStmnt(final String tableName) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    String statement = switch (tableName) {
    case TABLE_NAME_CITY -> "fk_country_state_id,city_map,created,modified,name) VALUES (?,?,?,?,?";
    case TABLE_NAME_COMPANY -> "fk_city_id,active,address1,address2,address3,created,directions,email,fax,modified,name,phone,postal_code,url,vat_id_number) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
    case TABLE_NAME_COUNTRY -> "country_map,created,iso3166,modified,name) VALUES (?,?,?,?,?";
    case TABLE_NAME_COUNTRY_STATE -> "fk_country_id,fk_timezone_id,country_state_map,created,modified,name,symbol) VALUES (?,?,?,?,?,?,?";
    case TABLE_NAME_TIMEZONE -> "abbreviation,created,modified,name,v_time_zone) VALUES (?,?,?,?,?";
    case TABLE_NAME_TIMEZONE_CUBRID -> "abbreviation,created,modified,name,v_time_zone) VALUES (?,?,?,?,?";
    default -> throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    };

    if (dbms == Dbms.CRATEDB || dbms == Dbms.FIREBIRD) {
      return "pk_" + tableName.toLowerCase() + "_id," + statement.replace("(?", "(?,?");
    }

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");

    return statement;
  }

  private final void createFkList(final String tableName) {
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
        createData(dbms == Dbms.CUBRID ? TABLE_NAME_TIMEZONE_CUBRID : TABLE_NAME_TIMEZONE, config.getMaxRowTimezone());
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
        if (countData(dbms == Dbms.CUBRID ? TABLE_NAME_TIMEZONE_CUBRID : TABLE_NAME_TIMEZONE) == 0) {
          createData(dbms == Dbms.CUBRID ? TABLE_NAME_TIMEZONE_CUBRID : TABLE_NAME_TIMEZONE, config.getMaxRowTimezone());
        } else {
          recreatePkList(dbms == Dbms.CUBRID ? TABLE_NAME_TIMEZONE_CUBRID : TABLE_NAME_TIMEZONE);
        }
      }
    }
  }

  /**
   * Close the database connection.
   *
   * @param connection the database connection
   */
  protected final void disconnect(Connection connection) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start [" + connection.toString() + "]");

    try {
      if (!(connection.getAutoCommit())) {
        connection.commit();
      }

      connection.close();

      connection = null;
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }

  private final int getDefaultRowSize(final String tableName) {
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
    case TABLE_NAME_TIMEZONE_CUBRID:
      return 11;
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  private final int getRandomIntExcluded(final int upperLimit) {
    return randomInt.nextInt(upperLimit);
  }

  //  private final double getRandomDouble(final double lowerLimit, final double upperLimit) {
  //    return ThreadLocalRandom.current().nextDouble(lowerLimit, upperLimit);
  //  }

  private final int getRandomIntIncluded(final int upperLimit) {
    return randomInt.nextInt(upperLimit) + 1;
  }

  private final Timestamp getRandomTimestamp() {

    return new java.sql.Timestamp(System.currentTimeMillis() + randomInt.nextInt(2147483647));
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
    String identifier10      = String.format(FORMAT_IDENTIFIER, rowNo);
    String identifier10Right = String.format(FORMAT_IDENTIFIER_RIGHT, rowNo);

    switch (tableName) {
    case TABLE_NAME_CITY:
      prepDmlStmntInsertCity(preparedStatement, rowCount, identifier10);
      break;
    case TABLE_NAME_COMPANY:
      prepDmlStmntInsertCompany(preparedStatement, rowCount, identifier10, identifier10Right);
      break;
    case TABLE_NAME_COUNTRY:
      prepDmlStmntInsertCountry(preparedStatement, rowCount, identifier10, identifier10Right);
      break;
    case TABLE_NAME_COUNTRY_STATE:
      prepDmlStmntInsertCountryState(preparedStatement, rowCount, identifier10, identifier10Right);
      break;
    case TABLE_NAME_TIMEZONE:
      prepDmlStmntInsertTimezone(preparedStatement, rowCount, identifier10, identifier10Right);
      break;
    case TABLE_NAME_TIMEZONE_CUBRID:
      prepDmlStmntInsertTimezone(preparedStatement, rowCount, identifier10, identifier10Right);
      break;
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  private final void prepDmlStmntInsertCity(final PreparedStatement preparedStatement, final int rowCount, final String identifier10) {
    try {
      int i = 1;

      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setInt(i++, autoIncrement++);
      }

      prepStmntInsertColFKOpt(i++, preparedStatement, pkListCountryState, rowCount);
      prepStmntInsertColBlobOpt(preparedStatement, i++, rowCount);
      prepStmntInsertColDatetime(preparedStatement, i++, rowCount);
      prepStmntInsertColDatetimeOpt(preparedStatement, i++, rowCount);
      preparedStatement.setString(i, "NAME_" + identifier10);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void
          prepDmlStmntInsertCompany(final PreparedStatement preparedStatement, final int rowCount, final String identifier10, String identifier10Right) {
    try {
      int i = 1;

      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setInt(i++, autoIncrement++);
      }

      preparedStatement.setObject(i++, pkListCity.get(getRandomIntExcluded(pkListCity.size())));
      prepStmntInsertColFlagNY(preparedStatement, i++, rowCount);
      prepStmntInsertColStringOpt(preparedStatement, i++, "ADDRESS1_", rowCount, identifier10);
      prepStmntInsertColStringOpt(preparedStatement, i++, "ADDRESS2_", rowCount, identifier10);
      prepStmntInsertColStringOpt(preparedStatement, i++, "ADDRESS3_", rowCount, identifier10);
      prepStmntInsertColDatetime(preparedStatement, i++, rowCount);
      prepStmntInsertColClobOpt(preparedStatement, i++, rowCount);
      prepStmntInsertColStringOpt(preparedStatement, i++, "EMAIL_", rowCount, identifier10);
      prepStmntInsertColStringOpt(preparedStatement, i++, "FAX_", rowCount, identifier10);
      prepStmntInsertColDatetimeOpt(preparedStatement, i++, rowCount);
      preparedStatement.setString(i++, "NAME_" + identifier10);
      prepStmntInsertColStringOpt(preparedStatement, i++, "PHONE_", rowCount, identifier10);
      prepStmntInsertColStringOpt(preparedStatement, i++, "POSTAL_CODE_", rowCount, identifier10Right.substring(2));
      prepStmntInsertColStringOpt(preparedStatement, i++, "URL_", rowCount, identifier10);
      prepStmntInsertColStringOpt(preparedStatement, i, "VAT_ID_NUMBER__", rowCount, identifier10);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void
          prepDmlStmntInsertCountry(final PreparedStatement preparedStatement, final int rowCount, final String identifier10, String identifier10Right) {
    try {
      int i = 1;

      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setInt(i++, autoIncrement++);
      }

      prepStmntInsertColBlobOpt(preparedStatement, i++, rowCount);
      prepStmntInsertColDatetime(preparedStatement, i++, rowCount);
      prepStmntInsertColStringOpt(preparedStatement, i++, "", rowCount, identifier10Right.substring(8));
      prepStmntInsertColDatetimeOpt(preparedStatement, i++, rowCount);
      preparedStatement.setString(i, "NAME_" + identifier10);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - COUNTRY_STATE.
   *
   * @param preparedStatement the prepared statement
   * @param rowCount number of rows to be created
   * @param identifier10 number of the current row (10 figures)
   * @param identifier10 number of the current row (10 figures) right aligned
   */
  private final void
          prepDmlStmntInsertCountryState(final PreparedStatement preparedStatement, final int rowCount, final String identifier10, String identifier10Right) {
    try {
      int i = 1;

      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setInt(i++, autoIncrement++);
      }

      preparedStatement.setObject(i++, pkListCountry.get(getRandomIntExcluded(pkListCountry.size())));
      preparedStatement.setObject(i++, pkListTimezone.get(getRandomIntExcluded(pkListTimezone.size())));
      prepStmntInsertColBlobOpt(preparedStatement, i++, rowCount);
      prepStmntInsertColDatetime(preparedStatement, i++, rowCount);
      prepStmntInsertColDatetimeOpt(preparedStatement, i++, rowCount);
      preparedStatement.setString(i++, "NAME_" + identifier10);
      prepStmntInsertColStringOpt(preparedStatement, i, "SYMBOL_", rowCount, identifier10Right.substring(7));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void
          prepDmlStmntInsertTimezone(final PreparedStatement preparedStatement, final int rowCount, final String identifier10, final String identifier10Right) {
    try {
      int i = 1;

      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setInt(i++, autoIncrement++);
      }

      preparedStatement.setString(i++, "ABBREVIATION_" + identifier10Right.substring(3));
      prepStmntInsertColDatetime(preparedStatement, i++, rowCount);
      prepStmntInsertColDatetimeOpt(preparedStatement, i++, rowCount);
      preparedStatement.setString(i++, "NAME_" + identifier10);
      prepStmntInsertColStringOpt(preparedStatement, i, "V_TIME_ZONE_", rowCount, identifier10);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional parameter to a BLOB value.
   * 
   * @param preparedStatement the prepared statement
   * @param columnPos         the column position
   * @param rowCount          the row count
   */
  protected void prepStmntInsertColBlob(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      preparedStatement.setBytes(columnPos, BLOB_DATA_BYTES);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColBlobOpt(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setNull(columnPos, Types.NULL);
      } else if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        if (dbms == Dbms.POSTGRESQL) {
          preparedStatement.setNull(columnPos, Types.NULL);
        } else {
          preparedStatement.setNull(columnPos, Types.BLOB);
        }
      } else {
        prepStmntInsertColBlob(preparedStatement, columnPos, rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColClob(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      preparedStatement.setString(columnPos, CLOB_DATA);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColClobOpt(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        if (dbms == Dbms.CRATEDB) {
          preparedStatement.setNull(columnPos, Types.VARCHAR);
        } else {
          preparedStatement.setNull(columnPos, java.sql.Types.CLOB);
        }
      } else {
        prepStmntInsertColClob(preparedStatement, columnPos, rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColDatetime(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      preparedStatement.setTimestamp(columnPos, getRandomTimestamp());
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColDatetimeOpt(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos, java.sql.Types.TIMESTAMP);
      } else {
        prepStmntInsertColDatetime(preparedStatement, columnPos, rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColFKOpt(final int columnPos, PreparedStatement preparedStatement, final ArrayList<Object> fkList, final int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos, java.sql.Types.INTEGER);
      } else {
        preparedStatement.setObject(columnPos, fkList.get(getRandomIntExcluded(fkList.size())));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  //  private final void
  //            prepStmntInsertColDoubleOpt(PreparedStatement preparedStatement, final int columnPos, final int rowCount, double lowerLimit, double upperLimit) {
  //    try {
  //      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
  //        preparedStatement.setNull(columnPos, java.sql.Types.DECIMAL);
  //      } else {
  //        preparedStatement.setBigDecimal(columnPos, BigDecimal.valueOf(getRandomDouble(lowerLimit, upperLimit)));
  //      }
  //    } catch (SQLException e) {
  //      e.printStackTrace();
  //      System.exit(1);
  //    }
  //  }

  private final void prepStmntInsertColFlagNY(PreparedStatement preparedStatement, final int columnPos, final int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setString(columnPos, "N");
      } else {
        preparedStatement.setString(columnPos, "Y");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColStringOpt(final PreparedStatement preparedStatement,
                                                 final int columnPos,
                                                 final String columnName,
                                                 final int rowCount,
                                                 final String identifier) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos, java.sql.Types.VARCHAR);
      } else {
        preparedStatement.setString(columnPos, columnName + identifier);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  //  private final void prepStmntInsertColFlagNYOpt(PreparedStatement preparedStatement, final int columnPos, final int rowCount) {
  //    try {
  //      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
  //        preparedStatement.setNull(columnPos, java.sql.Types.VARCHAR);
  //      } else {
  //        prepStmntInsertColFlagNY(preparedStatement, columnPos, rowCount);
  //      }
  //    } catch (SQLException e) {
  //      e.printStackTrace();
  //      System.exit(1);
  //    }
  //  }

  //  private final void prepStmntInsertColIntOpt(PreparedStatement preparedStatement, final int columnPos, final int rowCount, final int upperLimit) {
  //    try {
  //      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
  //        preparedStatement.setNull(columnPos, java.sql.Types.INTEGER);
  //      } else {
  //        preparedStatement.setInt(columnPos, getRandomIntIncluded(upperLimit));
  //      }
  //    } catch (SQLException e) {
  //      e.printStackTrace();
  //      System.exit(1);
  //    }
  //  }

  private byte[] readBlobFile2Bytes() {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- BLOB_FILE ='" + BLOB_FILE + "'");

    File      file       = new File(BLOB_FILE);

    final int fileLength = (int) file.length();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- fileLength=" + fileLength);

    byte[]          blobDataBytesArray = new byte[(int) file.length()];

    FileInputStream fileInputStream    = null;

    try {
      fileInputStream = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    try {
      fileInputStream.read(blobDataBytesArray);

      fileInputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- byteLength=" + blobDataBytesArray.length);

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");

    return blobDataBytesArray;
  }

  private final String readClobFile() {
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader(CLOB_FILE));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
      System.exit(1);
    }

    StringBuffer clobData = new StringBuffer();
    String       nextLine = "";

    try {
      while ((nextLine = bufferedReader.readLine()) != null) {
        clobData.append(nextLine).append(System.lineSeparator());
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      bufferedReader.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    return clobData.toString();
  }

  private final void recreatePkList(final String tableName) {
    ArrayList<Object> pkList    = new ArrayList<Object>();

    Statement         statement = null;

    try {
      statement = connection.createStatement();

      resultSet = statement.executeQuery("SELECT PK_" + tableName + "_ID FROM " + tableName);

      while (resultSet.next()) {
        pkList.add(resultSet.getInt(1));
      }

      resultSet.close();

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    savePkList(tableName, pkList);
  }

  private final void retrieveFkList(final String tableName) {
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
        recreatePkList(dbms == Dbms.CUBRID ? TABLE_NAME_TIMEZONE_CUBRID : TABLE_NAME_TIMEZONE);
      }

      break;
    }
  }

  private final void savePkList(final String tableName, final ArrayList<Object> pkList) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      pkListCity = pkList;
      break;
    case TABLE_NAME_COMPANY:
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
    case TABLE_NAME_TIMEZONE_CUBRID:
      pkListTimezone = pkList;
      break;
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  /**
   * Delete any existing relevant database objects (database, user, 
   * schema or tables)and initialise the database for a new run.
   */

  protected abstract void setupDatabase();
}
