/**
 *
 */
package ch.konnexions.db_seeder.jdbc;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.AbstractDatabaseSeeder;
import ch.konnexions.db_seeder.Config;
import ch.konnexions.db_seeder.DatabaseSeeder;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <h1> Test Data Generator for a Database. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @version 1.0.0
 * @since   2020-05-01
 */
public abstract class AbstractJdbcSeeder extends AbstractDatabaseSeeder {

  private static Logger       logger                   = Logger.getLogger(AbstractJdbcSeeder.class);

  protected Config            config;

  protected Connection        connection;

  protected final String      FILE_IMAGE_MIME_TYPE     = "image/png";
  protected final String      FILE_IMAGE_NAME          = "blob.png";

  protected final File        IMAGE_FILE               = new File(
      Paths.get("src", "main", "resources").toAbsolutePath().toString() + File.separator + FILE_IMAGE_NAME);

  protected final int         MAX_ROW_SIZE             = Integer.MAX_VALUE;

  protected ArrayList<Object> pkListCity               = new ArrayList<Object>();

  protected ArrayList<Object> pkListCompany            = new ArrayList<Object>();
  protected ArrayList<Object> pkListCountry            = new ArrayList<Object>();
  protected ArrayList<Object> pkListCountryState       = new ArrayList<Object>();
  protected ArrayList<Object> pkListTimezone           = new ArrayList<Object>();
  protected Random            randomInt                = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

  protected Random            randomLong               = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
  protected final String      TABLE_NAME_CITY          = "CITY";

  protected final String      TABLE_NAME_COMPANY       = "COMPANY";
  protected final String      TABLE_NAME_COUNTRY       = "COUNTRY";
  protected final String      TABLE_NAME_COUNTRY_STATE = "COUNTRY_STATE";
  protected final String      TABLE_NAME_TIMEZONE      = "TIMEZONE";

  /**
   *
   */
  public AbstractJdbcSeeder() {
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
   * Add optional foreign keys related to current database table.
   *
   * @param tableName    the table name
   * @param pkcolumnName the primary key column name
   * @param pkList       the primary key list
   * @param fkColumnName the foreign key column name
   * @param fkList       the foreign key list
   */
  protected void addOptionalFk(final String tableName,
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

    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET " + fkColumnName + " = ? WHERE " + pkcolumnName + " = ?");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    for (Object pkIndex : pkList) {
      if (getRandomIntExcluded(pkListSize) % 4 == 0) {
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
  }

  /**
   * Create a database connection.
   */
  protected abstract void connect();

  /**
   * Count the test data from a single database table.
   *
   * @param tableName the database table name
   * @return the number of existing rows
   */
  protected int countData(final String tableName) {
    int       count     = 0;

    Statement statement = null;

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
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - Start");

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

    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - End");
  }

  /**
   * Generate test data for specific database table.
   * 
   * @param tableName the name of the database table
   * @param rowCount  number of rows to be created
   */
  protected void createData(String tableName, int rowCount) {
    String methodName = null;

    methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    logger.debug(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName)
        + " - Start - database table \" + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName) + \" - \"\n"
        + "        + String.format(DatabaseSeeder.FORMAT_ROW_NO, rowCount) + \" rows to be created");

    tableName = tableName.toUpperCase();

    if (rowCount < 1) {
      rowCount = getDefaultRowSize(tableName);
    } else if (rowCount > MAX_ROW_SIZE) {
      rowCount = MAX_ROW_SIZE;
    }

    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(createDdlStmnt(tableName));
      preparedStatement.executeUpdate();
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

    final String sqlStmnt = "INSERT INTO " + tableName + " (" + createDmlStmnt(tableName) + ")";

    try {
      preparedStatement = connection.prepareStatement(sqlStmnt, new String[] { "PK_" + tableName + "_ID" });
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

    addOptionalFk(tableName, pkList);

    logger.info(String.format(DatabaseSeeder.FORMAT_METHOD_NAME, methodName) + " - End   - database table "
        + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName) + " - " + String.format(DatabaseSeeder.FORMAT_ROW_NO, rowCount) + " rows created");

    try {
      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      connection.commit();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    savePkList(tableName, pkList);
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
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
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

  /**
   * Close the database connection.
   */
  protected void disconnect() {
    if (connection != null) {
      try {
        connection.close();

        connection = null;
      } catch (SQLException ec) {
        ec.printStackTrace();
        System.exit(1);
      }
    }
  }

  /**
   * Drop the schema / user if existing and create it new.
   */
  protected abstract void dropCreateSchemaUser();

  /**
   * Get the default number of required database rows.
   *
   * @param tableName the database table name
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
  protected double getRandomDouble(final double lowerLimit, final double upperLimit) {
    return ThreadLocalRandom.current().nextDouble(lowerLimit, upperLimit);
  }

  /**
   * Get a random integer of the interval 0..upperLimit-1.
   *
   * @param upperLimit the upper limit of the random integer
   * @return the random integer value
   */
  protected int getRandomIntExcluded(final int upperLimit) {
    return randomInt.nextInt(upperLimit);
  }

  /**
   * Get a random integer of the interval 1..upperLimit.
   *
   * @param upperLimit the upper limit of the random integer
   * @return the random integer value
   */
  protected int getRandomIntIncluded(final int upperLimit) {
    return randomInt.nextInt(upperLimit) + 1;
  }

  /**
   * Get a random time stamp.
   *
   * @return the random time stamp
   */
  protected Timestamp getRandomTimestamp() {

    return new Timestamp(System.currentTimeMillis() + randomInt.nextInt(2147483647));
  }

  /**
   * Gets a unique primary key combination for the given database table.
   *
   * @param tableName   the database table name
   * @param columnName1 the name of the first foreign key column
   * @param pkList1     the first primary key list
   * @param columnName2 the name of the second foreign key column
   * @param pkList2     the second primary key list
   * @return a unique primary key combination for the given database table
   */
  protected Object[] getUniqueKeyCombination(String tableName, String columnName1, ArrayList<Object> pkList1, String columnName2, ArrayList<Object> pkList2) {
    int       count       = 0;

    ResultSet resultSet   = null;

    int       sizePkList1 = pkList1.size();
    int       sizePkList2 = pkList2.size();
    Statement statement   = null;

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    for (int i = 0; i < sizePkList1 * sizePkList2; i++) {
      Object valueFK1 = pkList1.get(getRandomIntExcluded(sizePkList1));
      Object valueFK2 = pkList2.get(getRandomIntExcluded(sizePkList2));

      try {
        resultSet = statement
            .executeQuery("SELECT count(*) FROM " + tableName + " WHERE " + columnName1 + " = " + valueFK1 + " AND " + columnName2 + " = " + valueFK2);

        while (resultSet.next()) {
          count = resultSet.getInt(1);
        }

      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }

      if (count == 0) {
        try {
          statement.close();
        } catch (SQLException e) {
          e.printStackTrace();
          System.exit(1);
        }
        return new Object[] { valueFK1, valueFK2 };
      }
    }

    throw new RuntimeException("Not unique key combination available - database table : " + tableName);
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
   * @param rowCount          number of rows to be created
   * @param identifier04      number of the current row (4 figures)
   */
  protected abstract void prepDmlStmntInsertCity(final PreparedStatement preparedStatement, final int rowCount, final String identifier04);

  /**
   * Prepare the variable part of the INSERT statement - COMPANY.
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount          number of rows to be created
   * @param identifier04      number of the current row (4 figures)
   */
  protected abstract void prepDmlStmntInsertCompany(final PreparedStatement preparedStatement, final int rowCount, final String identifier04);

  /**
   * Prepare the variable part of the INSERT statement - COUNTRY.
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount          number of rows to be created
   * @param identifier04      number of the current row (4 figures)
   */
  protected abstract void prepDmlStmntInsertCountry(final PreparedStatement preparedStatement, final int rowCount, final String identifier04);

  /**
   * Prepare the variable part of the INSERT statement - COUNTRY_STATE.
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount          number of rows to be created
   * @param identifier04      number of the current row (4 figures)
   */
  protected abstract void prepDmlStmntInsertCountryState(final PreparedStatement preparedStatement, final int rowCount, final String identifier04);

  /**
   * Prepare the variable part of the INSERT statement - TIMEZONE.
   *
   * @param preparedStatement preparedStatement object
   * @param rowCount          number of rows to be created
   * @param identifier04      number of the current row (4 figures)
   */
  protected abstract void prepDmlStmntInsertTimezone(final PreparedStatement preparedStatement, final int rowCount, final String identifier04);

  /**
   * Sets the designated optional parameter to a random date time value.
   *
   * @param columnPos         the column position
   * @param preparedStatement the prepared statement
   * @param rowCount          the row count
   */
  protected void prepStmntInsertColDatetimeOpt(final int columnPos, PreparedStatement preparedStatement, int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(columnPos, java.sql.Types.DATE);
      } else {
        preparedStatement.setTimestamp(columnPos, getRandomTimestamp());
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
  protected void
            prepStmntInsertColDoubleOpt(final int columnPos, PreparedStatement preparedStatement, final int rowCount, double lowerLimit, double upperLimit) {
    try {
      if (getRandomIntIncluded(rowCount) % 4 == 0) {
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
  protected void prepStmntInsertColFKOpt(final int columnPos, final ArrayList<Object> fkList, PreparedStatement preparedStatement, final int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % 4 == 0) {
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
  protected void prepStmntInsertColFlagNY(final int columnPos, PreparedStatement preparedStatement, final int rowCount) {
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
  protected void prepStmntInsertColFlagNYOpt(final int columnPos, PreparedStatement preparedStatement, final int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % 4 == 0) {
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
  protected void prepStmntInsertColIntOpt(final int columnPos, PreparedStatement preparedStatement, final int rowCount, final int upperLimit) {
    try {
      if (getRandomIntIncluded(rowCount) % 4 == 0) {
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
  protected void prepStmntInsertColStringOpt(final int columnPos,
                                             final String columnName,
                                             final PreparedStatement preparedStatement,
                                             final int rowCount,
                                             final String identifier) {
    try {
      if (getRandomIntIncluded(rowCount) % 4 == 0) {
        preparedStatement.setNull(columnPos, java.sql.Types.VARCHAR);
      } else {
        preparedStatement.setString(columnPos, columnName + identifier);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Recreate the database table specific primary key list.
   *
   * @param tableName the database table name
   */
  protected void recreatePkList(final String tableName) {
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
      throw new RuntimeException("Not yet implemented - database table : " + String.format(DatabaseSeeder.FORMAT_TABLE_NAME, tableName));
    }
  }
}
