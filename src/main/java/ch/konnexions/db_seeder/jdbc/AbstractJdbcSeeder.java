/**
 *
 */
package ch.konnexions.db_seeder.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
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
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.AbstractDatabaseSeeder;
import ch.konnexions.db_seeder.Config;
import ch.konnexions.db_seeder.DatabaseSeeder;

/**
 * <h1> Test Data Generator for a Database. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public abstract class AbstractJdbcSeeder extends AbstractDatabaseSeeder {

  private static Logger       logger               = Logger.getLogger(AbstractJdbcSeeder.class);

  protected final byte[]      BLOB_DATA            = readBlobFile();
  protected String            BLOB_FILE;

  private final String        CLOB_DATA            = readClobFile();
  protected Connection        connection           = null;
  protected Connection        connectionAlt        = null;

  protected String            driver               = "";
  protected String            dropTableStmnt       = "";

  private final int           MAX_ROW_SIZE         = Integer.MAX_VALUE;

  protected PreparedStatement preparedStatement    = null;
  protected PreparedStatement preparedStatementAlt = null;

  private final int           RANDOM_NUMBER        = 4;
  private Random              randomInt            = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
  protected ResultSet         resultSet            = null;

  protected Statement         statement            = null;

  protected String            url                  = "";
  protected String            urlBase              = "";
  protected String            urlSetup             = "";

  /**
   *
   */
  public AbstractJdbcSeeder() {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    config = new Config();

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
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

  /**
   * Add optional foreign keys related to current database table.
   *
   * @param tableName    the table name
   * @param pkcolumnName the primary key column name
   * @param pkList       the primary key list
   * @param fkColumnName the foreign key column name
   * @param fkList       the foreign key list
   */
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

  /**
   * Checks if the required number of rows is available in the database.
   *
   * @param tableName the table name
   * @param expectedRows the expected number of rows
   */
  private final void checkData(String tableName, int expectedRows) {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start");

    int count = 0;

    try {
      statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM " + tableName);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (expectedRows == count) {
      logger.info(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- database table "
          + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName) + " - " + String.format(DatabaseSeeder.FORMAT_ROW_NO, count) + " rows created");
    } else {
      logger.fatal(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- database table "
          + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName) + " is incomplete - expected" + String.format(DatabaseSeeder.FORMAT_ROW_NO, expectedRows)
          + " rows - found " + String.format(DatabaseSeeder.FORMAT_ROW_NO, count) + " rows");
      System.exit(1);
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");
  }

  /**
   * Create a database connection.
   */
  protected final Connection connect(String url) {
    return connect(url, null);
  }

  /**
   * Create a database connection.
   */
  protected final Connection connect(String url, String driver) {
    return connect(url, driver, null, null);
  }

  /**
   * Create a database connection.
   */
  protected final Connection connect(String url, String driver, String user, String password) {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start");

    if (driver != null) {
      try {
        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      if (user == null && password == null) {
        connection = DriverManager.getConnection(url);
      } else {
        connection = DriverManager.getConnection(url, user, password);
      }

      if (dbms == Dbms.CRATEDB) {
        connection.setAutoCommit(true);
      } else {
        connection.setAutoCommit(false);
      }
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");

    return connection;
  }

  /**
   * Count the test data from a single database table.
   *
   * @param tableName the database table name
   * @return the number of existing rows
   */
  private final int countData(final String tableName) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start");

    int count = 0;

    try {
      statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM " + tableName);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");

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
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start");

    resetAndCreateDatabase();

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

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");
  }

  /**
   * Generate test data for specific database table.
   * 
   * @param tableName the name of the database table
   * @param rowCount  number of rows to be created
   */
  private final void createData(String tableName, int rowCount) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName)
        + "- Start - database table \" + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName) + \" - \"\n"
        + "        + String.format(DatabaseSeeder.FORMAT_ROW_NO, rowCount) + \" rows to be created");

    tableName = tableName.toUpperCase();

    if (rowCount < 1) {
      rowCount = getDefaultRowSize(tableName);
    } else if (rowCount > MAX_ROW_SIZE) {
      rowCount = MAX_ROW_SIZE;
    }

    try {
      statement = connection.createStatement();
      statement.execute(createDdlStmnt(tableName));

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    final int countExisting = countData(tableName);

    if (countExisting != 0) {
      logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - found existing test data in database table "
          + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));

      return;
    }

    ArrayList<Object> pkList = new ArrayList<Object>();

    retrieveFkList(tableName);
    createFkList(tableName);

    if (dbms == Dbms.CRATEDB) {
      autoIncrement = 0;
    }

    createDataInsert(preparedStatement, tableName, rowCount, pkList);

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

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");
  }

  /**
   * Creates the data insert.
   *
   * @param preparedStatement the prepared statement
   * @param tableName the table name
   * @param rowCount the total row count
   * @param pkList current primary key list
   */
  protected final void createDataInsert(PreparedStatement preparedStatement, String tableName, int rowCount, ArrayList<Object> pkList) {
    if (dbms == Dbms.CRATEDB) {
      createDataInsertCratedb(preparedStatement, tableName, rowCount, pkList);
      return;
    }

    if (dbms == Dbms.POSTGRESQL) {
      createDataInsertPostgresql(preparedStatement, tableName, rowCount, pkList);
      return;
    }

    final String sqlStmnt = "INSERT INTO " + tableName + " (" + createDmlStmnt(tableName) + ")";

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

        ResultSet resultSet = preparedStatement.getGeneratedKeys();

        while (resultSet.next()) {
          pkList.add((int) resultSet.getLong(1));
        }
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
  }

  private final void createDataInsertCratedb(PreparedStatement preparedStatement, String tableName, int rowCount, ArrayList<Object> pkList) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName)
        + "- Start - database table \" + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName) + \" - \"\n"
        + "        + String.format(DatabaseSeeder.FORMAT_ROW_NO, rowCount) + \" rows to be created");

    final String sqlStmnt = "INSERT INTO " + tableName + " (" + createDmlStmnt(tableName) + ")";

    try {
      preparedStatement = connection.prepareStatement(sqlStmnt);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    for (int rowNo = 1; rowNo <= rowCount; rowNo++) {
      prepDmlStmntInsert(preparedStatement, tableName, rowCount, rowNo, pkList);

      try {
        preparedStatement.executeUpdate();

        pkList.add(autoIncrement);
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      statement = connection.createStatement();
      statement.execute("REFRESH TABLE " + tableName);
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");
  }

  private final void createDataInsertPostgresql(PreparedStatement preparedStatement, String tableName, int rowCount, ArrayList<Object> pkList) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName)
        + "- Start - database table \" + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName) + \" - \"\n"
        + "        + String.format(DatabaseSeeder.FORMAT_ROW_NO, rowCount) + \" rows to be created");

    final String sqlStmnt = "INSERT INTO " + tableName + " (" + createDmlStmnt(tableName) + ") RETURNING PK_" + tableName + "_ID";

    try {
      preparedStatement = connection.prepareStatement(sqlStmnt);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    for (int rowNo = 1; rowNo <= rowCount; rowNo++) {
      prepDmlStmntInsert(preparedStatement, tableName, rowCount, rowNo, pkList);

      try {
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
          pkList.add((int) resultSet.getLong(1));
        }
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");
  }

  /**
   * Create the DDL statement: CREATE TABLE.
   *
   * @param tableName the database table name
   *
   * @return the create table statement
   */
  protected abstract String createDdlStmnt(String tableName);

  /**
   * Create the DML statement: INSERT.
   *
   * @param tableName the database table name
   *
   * @return the insert statement
   */
  protected final String createDmlStmnt(final String tableName) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start");

    String statement = switch (tableName) {
    case TABLE_NAME_CITY -> "fk_country_state_id,city_map,created,modified,name) VALUES (?,?,?,?,?";
    case TABLE_NAME_COMPANY -> "fk_city_id,active,address1,address2,address3,created,directions,email,fax,modified,name,phone,postal_code,url,vat_id_number) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
    case TABLE_NAME_COUNTRY -> "country_map,created,iso3166,modified,name) VALUES (?,?,?,?,?";
    case TABLE_NAME_COUNTRY_STATE -> "fk_country_id,fk_timezone_id,country_state_map,created,modified,name,symbol) VALUES (?,?,?,?,?,?,?";
    case TABLE_NAME_TIMEZONE -> "abbreviation,created,modified,name,v_time_zone) VALUES (?,?,?,?,?";
    default -> throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    };

    if (dbms == Dbms.CRATEDB) {
      return "pk_" + tableName.toLowerCase() + "_id," + statement.replace("(?", "(?,?");
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");

    return statement;
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

  /**
   * Close the database connection.
   */
  protected final void disconnect() {
    disconnect(connection);
  }

  /**
   * Close the database connection.
   */
  protected final void disconnect(Connection connection) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start");

    if (connection == null) {
      return;
    }

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

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");
  }

  protected void dropAllTables(String url, String sqlStmnt) {
    dropAllTables(url, sqlStmnt, null);
  }

  protected void dropAllTables(String url, String sqlStmnt, String schema) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start");

    try {
      connectionAlt     = connect(url);

      preparedStatement = connection.prepareStatement(sqlStmnt);

      statement         = connectionAlt.createStatement();

      for (String tableName : TABLE_NAMES) {
        preparedStatement.setString(1, tableName);

        if (schema != null) {
          preparedStatement.setString(2, schema);
        }

        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
          statement.executeUpdate(resultSet.getString(2));
        }
      }

      statement.close();

      preparedStatement.close();

      disconnect(connectionAlt);

    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");
  }

  /**
   * Execute update with tolerance of non-existence.
   *
   * @param preparedStatement the prepared statement
   */
  protected void executeUpdateExistence(PreparedStatement preparedStatement) {
    try {
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      if (!(e.getErrorCode() == -204 && "42704".equals(e.getSQLState()))) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Get the default number of required database rows.
   *
   * @param tableName the database table name
   * @return the default number of required database rows
   */
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
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }

  /**
   * Get a random double of the interval lowerLimit..upperLimit.
   *
   * @param lowerLimit the lower limit of the random number
   * @param upperLimit the upper limit of the random number
   * @return the random double value
   */
  protected final double getRandomDouble(final double lowerLimit, final double upperLimit) {
    return ThreadLocalRandom.current().nextDouble(lowerLimit, upperLimit);
  }

  /**
   * Get a random integer of the interval 0..upperLimit-1.
   *
   * @param upperLimit the upper limit of the random integer
   * @return the random integer value
   */
  protected final int getRandomIntExcluded(final int upperLimit) {
    return randomInt.nextInt(upperLimit);
  }

  /**
   * Get a random integer of the interval 1..upperLimit.
   *
   * @param upperLimit the upper limit of the random integer
   * @return the random integer value
   */
  protected final int getRandomIntIncluded(final int upperLimit) {
    return randomInt.nextInt(upperLimit) + 1;
  }

  /**
   * Get a random time stamp.
   *
   * @return the random time stamp
   */
  protected final Timestamp getRandomTimestamp() {

    return new Timestamp(System.currentTimeMillis() + randomInt.nextInt(2147483647));
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
    String identifier04 = String.format(DatabaseSeeder.FORMAT_IDENTIFIER, rowNo);

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
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - CITY.
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   * @throws IOException 
   */
  protected final void prepDmlStmntInsertCity(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      int i = 1;

      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setInt(i++, autoIncrement++);
      }

      prepStmntInsertColFKOpt(i++, pkListCountryState, preparedStatement, rowCount);
      prepStmntInsertColBlobOpt(i++, preparedStatement, rowCount);
      preparedStatement.setTimestamp(i++, getRandomTimestamp());
      prepStmntInsertColDatetimeOpt(i++, preparedStatement, rowCount);
      preparedStatement.setString(i, "NAME_" + identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - COMPANY.
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  protected final void prepDmlStmntInsertCompany(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      int i = 1;

      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setInt(i++, autoIncrement++);
      }

      preparedStatement.setObject(i++, pkListCity.get(getRandomIntExcluded(pkListCity.size())));
      prepStmntInsertColFlagNY(i++, preparedStatement, rowCount);
      prepStmntInsertColStringOpt(i++, "ADDRESS1_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(i++, "ADDRESS2_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(i++, "ADDRESS3_", preparedStatement, rowCount, identifier04);
      preparedStatement.setTimestamp(i++, getRandomTimestamp());
      prepStmntInsertColClobOpt(i++, preparedStatement, rowCount);
      prepStmntInsertColStringOpt(i++, "EMAIL_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(i++, "FAX_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColDatetimeOpt(i++, preparedStatement, rowCount);
      preparedStatement.setString(i++, "NAME_" + identifier04);
      prepStmntInsertColStringOpt(i++, "PHONE_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(i++, "POSTAL_CODE_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(i++, "URL_", preparedStatement, rowCount, identifier04);
      prepStmntInsertColStringOpt(i, "VAT_ID_NUMBER__", preparedStatement, rowCount, identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - COUNTRY.
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  protected final void prepDmlStmntInsertCountry(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      int i = 1;

      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setInt(i++, autoIncrement++);
      }

      prepStmntInsertColBlobOpt(i++, preparedStatement, rowCount);
      preparedStatement.setTimestamp(i++, getRandomTimestamp());
      prepStmntInsertColStringOpt(i++, "", preparedStatement, rowCount, identifier04.substring(2));
      prepStmntInsertColDatetimeOpt(i++, preparedStatement, rowCount);
      preparedStatement.setString(i, "NAME_" + identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - COUNTRY_STATE.
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  protected final void prepDmlStmntInsertCountryState(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      int i = 1;

      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setInt(i++, autoIncrement++);
      }

      preparedStatement.setObject(i++, pkListCountry.get(getRandomIntExcluded(pkListCountry.size())));
      preparedStatement.setObject(i++, pkListTimezone.get(getRandomIntExcluded(pkListTimezone.size())));
      prepStmntInsertColBlobOpt(i++, preparedStatement, rowCount);
      preparedStatement.setTimestamp(i++, getRandomTimestamp());
      prepStmntInsertColDatetimeOpt(i++, preparedStatement, rowCount);
      preparedStatement.setString(i++, "NAME_" + identifier04);
      prepStmntInsertColStringOpt(i, "SYMBOL_", preparedStatement, rowCount, identifier04.substring(1));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Prepare the variable part of the INSERT statement - TIMEZONE.
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount number of rows to be created
   * @param identifier04 number of the current row (4 figures)
   */
  protected final void prepDmlStmntInsertTimezone(final PreparedStatement preparedStatement, final int rowCount, final String identifier04) {
    try {
      int i = 1;

      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setInt(i++, autoIncrement++);
      }

      preparedStatement.setString(i++, "ABBREVIATION_" + identifier04);
      preparedStatement.setTimestamp(i++, getRandomTimestamp());
      prepStmntInsertColDatetimeOpt(i++, preparedStatement, rowCount);
      preparedStatement.setString(i++, "NAME_" + identifier04);
      prepStmntInsertColStringOpt(i, "V_TIME_ZONE_", preparedStatement, rowCount, identifier04);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional parameter to a BLOB value.
   *
   * @param columnPos         the column position
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   */
  protected void prepStmntInsertColBlob(final int columnPos, PreparedStatement preparedStatement, int rowCount) {
    try {
      preparedStatement.setBytes(columnPos, BLOB_DATA);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional parameter randomly to a BLOB value.
   *
   * @param columnPos         the column position
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   */
  protected final void prepStmntInsertColBlobOpt(final int columnPos, PreparedStatement preparedStatement, int rowCount) {
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
        prepStmntInsertColBlob(columnPos, preparedStatement, rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional parameter to a CLOB value.
   *
   * @param columnPos         the column position
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   */
  protected final void prepStmntInsertColClob(final int columnPos, PreparedStatement preparedStatement, int rowCount) {
    try {
      preparedStatement.setString(columnPos, CLOB_DATA);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional parameter randomly to a CLOB value.
   *
   * @param columnPos         the column position
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   */
  protected final void prepStmntInsertColClobOpt(final int columnPos, PreparedStatement preparedStatement, int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        if (dbms == Dbms.CRATEDB) {
          preparedStatement.setNull(columnPos, Types.VARCHAR);
        } else {
          preparedStatement.setNull(columnPos, java.sql.Types.CLOB);
        }
      } else {
        prepStmntInsertColClob(columnPos, preparedStatement, rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional parameter to a random date time value.
   *
   * @param columnPos         the column position
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   */
  protected final void prepStmntInsertColDatetimeOpt(final int columnPos, PreparedStatement preparedStatement, int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos, java.sql.Types.TIMESTAMP);
      } else {
        Timestamp randomTimestamp = getRandomTimestamp();
        preparedStatement.setTimestamp(columnPos, randomTimestamp);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional parameter to a random double value.
   *
   * @param columnPos         the column position
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   * @param lowerLimit        the lower limit
   * @param upperLimit        the upper limit
   */
  protected final void
            prepStmntInsertColDoubleOpt(final int columnPos, PreparedStatement preparedStatement, final int rowCount, double lowerLimit, double upperLimit) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos, java.sql.Types.DECIMAL);
      } else {
        preparedStatement.setBigDecimal(columnPos, BigDecimal.valueOf(getRandomDouble(lowerLimit, upperLimit)));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional parameter to one of the given foreign keys.
   *
   * @param columnPos         the column position
   * @param fkList            the foreign key list
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   */
  protected final void prepStmntInsertColFKOpt(final int columnPos, final ArrayList<Object> fkList, PreparedStatement preparedStatement, final int rowCount) {
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

  /**
   * Sets the designated parameter to one of the flags 'N' or 'Y'.
   *
   * @param columnPos         the column position
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   */
  protected final void prepStmntInsertColFlagNY(final int columnPos, PreparedStatement preparedStatement, final int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % 10 == 0) {
        preparedStatement.setString(columnPos, "N");
      } else {
        preparedStatement.setString(columnPos, "Y");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional parameter to one of the flags 'N' or 'Y'.
   *
   * @param columnPos         the column position
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   */
  protected final void prepStmntInsertColFlagNYOpt(final int columnPos, PreparedStatement preparedStatement, final int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos, java.sql.Types.VARCHAR);
      } else {
        prepStmntInsertColFlagNY(columnPos, preparedStatement, rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional parameter to a random integer value.
   *
   * @param columnPos         the column position
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   * @param upperLimit        the upper limit
   */
  protected final void prepStmntInsertColIntOpt(final int columnPos, PreparedStatement preparedStatement, final int rowCount, final int upperLimit) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos, java.sql.Types.INTEGER);
      } else {
        preparedStatement.setInt(columnPos, getRandomIntIncluded(upperLimit));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional parameter to the given Java String value.
   *
   * @param columnPos         the column position
   * @param columnName        the column name
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   * @param identifier        the identifier
   */
  protected final void prepStmntInsertColStringOpt(final int columnPos,
                                                   final String columnName,
                                                   final PreparedStatement preparedStatement,
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

  private final byte[] readBlobFile() {
    BLOB_FILE = Paths.get("src", "main", "resources").toAbsolutePath().toString() + File.separator + "blob.png";

    byte[] blob = null;

    try {
      blob = Files.readAllBytes(new File(BLOB_FILE).toPath());
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    return blob;
  }

  private final String readClobFile() {
    String         CLOB_FILE      = Paths.get("src", "main", "resources").toAbsolutePath().toString() + File.separator + "clob.md";

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
        clobData.append(nextLine);
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

  /**
   * Recreate the database table specific primary key list.
   *
   * @param tableName the database table name
   */
  private final void recreatePkList(final String tableName) {
    ArrayList<Object> pkList    = new ArrayList<Object>();

    Statement         statement = null;

    try {
      statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT PK_" + tableName + "_ID FROM " + tableName);

      while (resultSet.next()) {
        pkList.add(resultSet.getInt(1));
      }

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    savePkList(tableName, pkList);
  }

  /**
   * Reset the database.
   * 
   * Drop & create database - optional
   * Drop & create user - optional
   * Drop & create schema - optional
   * Drop tables
   */

  protected abstract void resetAndCreateDatabase();

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
  private final void savePkList(final String tableName, final ArrayList<Object> pkList) {
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
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }
}
