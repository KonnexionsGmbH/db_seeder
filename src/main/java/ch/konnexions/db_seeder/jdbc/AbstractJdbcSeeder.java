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
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.utils.Config;
import ch.konnexions.db_seeder.utils.MessageHandling;
import ch.konnexions.db_seeder.utils.Statistics;

/**
 * Test Data Generator for a Database - Abstract JDBC Seeder.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public abstract class AbstractJdbcSeeder extends AbstractJdbcSchema {

  private static final int    ENCODING_MAX    = 3;
  private static final Logger logger          = Logger.getLogger(AbstractJdbcSeeder.class);

  private final String        BLOB_FILE       = Paths.get("src",
                                                          "main",
                                                          "resources").toAbsolutePath().toString() + File.separator + "blob.png";
  private final byte[]        BLOB_DATA_BYTES = readBlobFile2Bytes();
  private final String        CLOB_FILE       = Paths.get("src",
                                                          "main",
                                                          "resources").toAbsolutePath().toString() + File.separator + "clob.md";

  private final String        CLOB_DATA       = readClobFile();
  protected Connection        connection      = null;

  protected String            driver          = "";
  protected String            dropTableStmnt  = "";

  private final Properties    encodedColumnNames;

  protected boolean           isClient        = true;
  protected boolean           isEmbedded      = !(isClient);

  private final int           nullFactor;

  private final Random        randomInt       = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
  private ResultSet           resultSet       = null;

  protected Statement         statement       = null;

  protected String            url             = "";
  protected String            urlBase         = "";
  protected String            urlSetup        = "";

  /**
   * Initialises a new abstract JDBC seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public AbstractJdbcSeeder(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    config             = new Config();

    encodedColumnNames = createColumnNames();

    isClient           = true;
    isEmbedded         = !(this.isClient);

    nullFactor         = config.getNullFactor();

    if (isDebug) {
      logger.debug("client  =" + isClient);
      logger.debug("embedded=" + isEmbedded);

      logger.debug("End   Constructor");
    }
  }

  /**
   * Initialises a new abstract JDBC seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   * @param isClient client database version
   */
  public AbstractJdbcSeeder(String dbmsTickerSymbol, boolean isClient) {
    super(dbmsTickerSymbol, isClient);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol + " - isClient=" + isClient);
    }

    config             = new Config();

    encodedColumnNames = createColumnNames();

    this.isClient      = isClient;
    isEmbedded         = !(this.isClient);

    nullFactor         = config.getNullFactor();

    if (isDebug) {
      logger.debug("client  =" + isClient);
      logger.debug("embedded=" + isEmbedded);

      logger.debug("End   Constructor");
    }
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   *
   * @return the database connection
   */
  protected final Connection connect(String url) {
    return connect(url,
                   null,
                   null,
                   null,
                   false);
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @param autoCommit the auto commit option
   *
   * @return the database connection
   */
  protected final Connection connect(String url, boolean autoCommit) {
    return connect(url,
                   null,
                   null,
                   null,
                   autoCommit);
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
    return connect(url,
                   driver,
                   null,
                   null,
                   false);
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @param driver the database driver
   * @param autoCommit the auto commit option
   *
   * @return the database connection
   */
  protected final Connection connect(String url, String driver, boolean autoCommit) {
    return connect(url,
                   driver,
                   null,
                   null,
                   autoCommit);
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @param driver the database driver
   * @param user the user name
   * @param password the password
   *
   * @return the database connection
   */
  protected final Connection connect(String url, String driver, String user, String password) {
    return connect(url,
                   driver,
                   user,
                   password,
                   false);
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @param driver the database driver
   * @param user the user name
   * @param password the password
   * @param autoCommit the auto commit option
   *
   * @return the database connection
   */
  protected final Connection connect(String url, String driver, String user, String password, boolean autoCommit) {
    if (isDebug) {
      logger.debug("Start");
    }

    if (driver != null) {
      try {
        logger.debug("driver='" + driver + "'");
        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    Connection connection = null;

    try {
      if (isDebug) {
        logger.debug("url   ='" + url + "'");
      }
      if (user == null && password == null) {
        connection = DriverManager.getConnection(url);
      } else {
        if (isDebug) {
          logger.debug("user  ='" + user + "' password='" + password + "'");
        }
        connection = DriverManager.getConnection(url,
                                                 user,
                                                 password);
      }

      connection.setAutoCommit(autoCommit);

      if (isDebug) {
        logger.debug("auto  =" + connection.getAutoCommit());
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End   [" + connection.toString() + "]");
    }

    return connection;
  }

  private int countData(String tableName) {
    if (isDebug) {
      logger.debug("Start");
    }

    int count = 0;

    try {
      statement = connection.createStatement();

      String sqlStmnt = "SELECT COUNT(*) FROM " + tableNameDelimiter + tableName + tableNameDelimiter;

      if (isDebug) {
        logger.debug("sql='" + sqlStmnt + "'");
      }

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

    if (isDebug) {
      logger.debug("End");
    }

    return count;
  }

  /**
   * Creates the column names with encoding variations.
   *
   * @return the properties
   */
  protected abstract Properties createColumnNames();

  /**
   * Create the test data for all database valTableNames.
   */
  public final void createData() {
    if (isDebug) {
      logger.debug("Start");
    }

    Statistics statistics = new Statistics(config, dbmsTickerSymbol, dbmsValues);

    setupDatabase();

    for (String tableName : TABLE_NAMES_CREATE) {
      createData(tableName);
    }

    disconnect(connection);

    statistics.createMeasuringEntry();

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void createData(String tableName) {

    int rowMaxSize = getMaxRowSize(tableName);

    if (isDebug) {
      logger.debug("Start - database table " + String.format(FORMAT_TABLE_NAME,
                                                             tableName) + " - " + String.format(FORMAT_ROW_NO,
                                                                                                rowMaxSize) + " rows to be created");
    }

    tableName = tableName.toUpperCase();

    try {
      statement = connection.createStatement();

      String sqlStmnt = createDdlStmnt(tableName);

      if (isDebug) {
        logger.debug("next SQL statement=" + sqlStmnt);
      }

      statement.execute(sqlStmnt);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    final int countExisting = countData(tableName);

    if (countExisting != 0) {
      if (isDebug) {
        logger.debug("found existing test data in database table " + String.format(FORMAT_TABLE_NAME,
                                                                                   tableName));
      }

      return;
    }

    ArrayList<Object> pkList = new ArrayList<>();

    createDataInsert(tableName,
                     rowMaxSize,
                     pkList);

    if (!(dbmsEnum == DbmsEnum.CRATEDB || dbmsEnum == DbmsEnum.FIREBIRD)) {
      try {
        connection.commit();
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    pkLists.put(tableName,
                pkList);
    pkListSizes.put(tableName,
                    pkList.size());

    validateNumberRows(tableName,
                       rowMaxSize);

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void createDataInsert(String tableName, int rowMaxSize, ArrayList<Object> pkList) {
    if (isDebug) {
      logger.debug("Start");
    }

    final String sqlStmnt = "INSERT INTO " + tableNameDelimiter + tableName + tableNameDelimiter + " (" + dmlStatements.get(tableName) + ")";

    if (isDebug) {
      logger.debug("sql='" + sqlStmnt + "'");
    }

    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(sqlStmnt);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    for (int rowNo = 1; rowNo <= rowMaxSize; rowNo++) {
      insertTable(preparedStatement,
                  tableName,
                  rowNo,
                  pkList);

      try {
        preparedStatement.executeUpdate();

        pkList.add(rowNo);
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

    if (dbmsEnum == DbmsEnum.CRATEDB) {
      try {
        statement = connection.createStatement();

        statement.execute("REFRESH TABLE " + tableName);

        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    if (isDebug) {
      logger.debug("End");
    }
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
   * Close the database connection.
   *
   * @param connection the database connection
   */
  protected final void disconnect(Connection connection) {
    if (isDebug) {
      logger.debug("Start [" + connection.toString() + "]");
    }

    try {
      if (!(connection.getAutoCommit())) {
        connection.commit();
      }

      connection.close();

    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Drop all valTableNames based on q metadata query.
   *
   * @param sqlStmnt the SQL statement
   */
  protected final void dropAllTables(String sqlStmnt) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      for (String tableName : TABLE_NAMES_DROP) {
        String queryStmnt = sqlStmnt.replace("?",
                                             dbmsEnum == DbmsEnum.CRATEDB ? tableName.toLowerCase() : tableName.toUpperCase());

        if (isDebug) {
          logger.debug("next SQL statement=" + queryStmnt);
        }

        resultSet = statement.executeQuery(queryStmnt);

        if (resultSet.next()) {
          String dropStmnt = resultSet.getString(1);

          if (isDebug) {
            logger.debug("next SQL statement=" + dropStmnt);
          }

          statement.execute(dropStmnt);
        }

        resultSet.close();
      }

    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Drop all existing valTableNames.
   */
  protected final void dropAllTablesIfExists() {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      for (String tableName : TABLE_NAMES_DROP) {
        String sqlStmnt = "DROP TABLE IF EXISTS \"" + tableName + "\"";

        if (isDebug) {
          logger.debug("next SQL statement=" + sqlStmnt);
        }

        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Drop the database.
   *
   * @param databaseName the database name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName the table name
   * @param columnName the column name
   */
  protected final void dropDatabase(String databaseName, String cascadeRestrict, String tableName, String columnName) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      int    count    = 0;

      String sqlStmnt = "SELECT count(*) FROM " + tableName + " WHERE " + columnName + " = '" + databaseName + "'";

      if (isDebug) {
        logger.debug("next SQL statement=" + sqlStmnt);
      }

      resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        sqlStmnt = "DROP " + (dbmsEnum == DbmsEnum.MIMER ? "DATABANK" : "DATABASE") + " " + databaseName + (cascadeRestrict != null ? " " + cascadeRestrict
            : "");

        if (isDebug) {
          logger.debug("next SQL statement=" + sqlStmnt);
        }

        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Drop the database schema.
   *
   * @param schemaName the schema name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName the table name
   * @param columnName the column name
   */
  protected final void dropSchema(String schemaName, String cascadeRestrict, String tableName, String columnName) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      int    count    = 0;

      String sqlStmnt = "SELECT count(*) FROM " + tableName + " WHERE " + columnName + " = '" + schemaName + "'";

      if (isDebug) {
        logger.debug("next SQL statement=" + sqlStmnt);
      }

      resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        sqlStmnt = "DROP SCHEMA " + schemaName + (cascadeRestrict != null ? " " + cascadeRestrict : "");

        if (isDebug) {
          logger.debug("next SQL statement=" + sqlStmnt);
        }

        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Drop the database user.
   *
   * @param userName the user name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName the table name
   * @param columnName the column name
   */
  protected final void dropUser(String userName, String cascadeRestrict, String tableName, String columnName) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      int    count    = 0;

      String sqlStmnt = "SELECT count(*) FROM " + tableName + " WHERE " + columnName + " = '" + userName + "'";

      if (isDebug) {
        logger.debug("next SQL statement=" + sqlStmnt);
      }

      resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        sqlStmnt = "DROP " + (dbmsEnum == DbmsEnum.MIMER ? "IDENT" : "USER") + "  " + userName + (cascadeRestrict != null ? " " + cascadeRestrict : "");

        if (isDebug) {
          logger.debug("next SQL statement=" + sqlStmnt);
        }

        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Execute DDL statements.
   *
   * @param firstDdlStmnt the first DDL statement
   * @param remainingDdlStmnts the remaining DDL statements
   */
  protected final void executeDdlStmnts(String firstDdlStmnt, String... remainingDdlStmnts) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      if (isDebug) {
        logger.debug("next SQL statement=" + firstDdlStmnt);
      }

      statement.execute(firstDdlStmnt);

      for (String sqlStmnt : remainingDdlStmnts) {

        if (isDebug) {
          logger.debug("next SQL statement=" + sqlStmnt);
        }

        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  protected long getContentBigint(String tableName, String columnName, long rowNo) {

    return rowNo;
  }

  protected byte[] getContentBlob(String tableName, String columnName, int rowNo) {

    return BLOB_DATA_BYTES;
  }

  protected String getContentClob(String tableName, String columnName, int rowNo) {

    return CLOB_DATA;
  }

  protected Object getContentFk(String tableName, String columnName, int rowNo, ArrayList<Object> fkList) {
    Random random = new Random();

    return fkList.get(random.nextInt(fkList.size()));
  }

  protected Timestamp getContentTimestamp(String tableName, String columnName, int rowNo) {

    return new java.sql.Timestamp(System.currentTimeMillis() + randomInt.nextInt(2147483647));
  }

  protected String getContentVarchar(String tableName, String columnName, int rowNo, int size, String lowerRange, String upperRange, List<String> validValues) {
    if (isDebug) {
      logger.debug("Start");
    }

    String columnValue = "";
    Random random      = new Random();

    if (validValues != null) {
      columnValue = validValues.get(random.nextInt(validValues.size()));
    } else {
      columnValue = columnName + "_" + encodedColumnNames.getProperty(columnName + "_" + rowNo % ENCODING_MAX) + String.format(FORMAT_IDENTIFIER,
                                                                                                                               rowNo);
    }

    int length = getLengthUTF_8(columnValue);

    if (length > size) {
      columnValue = columnValue.substring(length - size);
    }

    if (lowerRange != null) {
      if (columnValue.compareTo(lowerRange) < 0) {
        return lowerRange;
      }
    }

    if (upperRange != null) {
      if (columnValue.compareTo(upperRange) > 0) {
        return upperRange;
      }
    }

    if (isDebug) {
      logger.debug("End");
    }

    return columnValue;
  }

  private final int getLengthUTF_8(String stringUTF_8) {
    int count = 0;

    for (int i = 0, len = stringUTF_8.length(); i < len; i++) {
      char ch = stringUTF_8.charAt(i);
      if (ch <= 0x7F) {
        count++;
      } else if (ch <= 0x7FF) {
        count += 2;
      } else if (Character.isHighSurrogate(ch)) {
        count += 4;
        ++i;
      } else {
        count += 3;
      }
    }

    return count;
  }

  private final int getMaxRowSize(String tableName) {
    int maxRowSize   = maxRowSizes.get(tableName);

    int MAX_ROW_SIZE = Integer.MAX_VALUE;
    if (maxRowSize > MAX_ROW_SIZE) {
      return MAX_ROW_SIZE;
    }

    return maxRowSize;
  }

  //  private final double getContentDouble( double lowerLimit,  double upperLimit) {
  //    return ThreadLocalRandom.current().nextDouble(lowerLimit, upperLimit);
  //  }

  protected abstract void insertTable(PreparedStatement preparedStatement, String tableName, int rowNo, ArrayList<Object> pkList);

  /**
   * Sets the designated column to a BIGINT value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  protected final void prepStmntColBigint(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, int rowNo) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      preparedStatement.setLong(1,
                                getContentBigint(tableName,
                                                 columnName,
                                                 rowNo));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Sets the designated optional column to a BIGINT value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  @SuppressWarnings("ucd")
  protected final void prepStmntColBigintOpt(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, int rowNo) {
    try {
      if (rowNo % nullFactor == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.INTEGER);
        return;
      }

      prepStmntColBigint(preparedStatement,
                         tableName,
                         columnName,
                         columnPos,
                         rowNo);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated column to a BLOB value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  protected void prepStmntColBlob(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, int rowNo) {
    try {
      preparedStatement.setBytes(columnPos,
                                 getContentBlob(tableName,
                                                columnName,
                                                rowNo));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional column to a BLOB value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  protected final void prepStmntColBlobOpt(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, int rowNo) {
    try {
      if (dbmsEnum == DbmsEnum.CRATEDB) {
        preparedStatement.setNull(columnPos,
                                  Types.NULL);
        return;
      }

      if (rowNo % nullFactor == 0) {
        if (dbmsEnum == DbmsEnum.POSTGRESQL) {
          preparedStatement.setNull(columnPos,
                                    Types.NULL);
          return;
        } else {
          preparedStatement.setNull(columnPos,
                                    Types.BLOB);
          return;
        }
      }

      prepStmntColBlob(preparedStatement,
                       tableName,
                       columnName,
                       columnPos,
                       rowNo);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated column to a CLOB value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  private void prepStmntColClob(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, int rowNo) {
    try {
      preparedStatement.setString(columnPos,
                                  getContentClob(tableName,
                                                 columnName,
                                                 rowNo));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional column to a CLOB value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  protected final void prepStmntColClobOpt(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, int rowNo) {
    try {
      if (rowNo % nullFactor == 0) {
        if (dbmsEnum == DbmsEnum.CRATEDB) {
          preparedStatement.setNull(columnPos,
                                    Types.VARCHAR);
          return;
        } else {
          preparedStatement.setNull(columnPos,
                                    java.sql.Types.CLOB);
          return;
        }
      }

      prepStmntColClob(preparedStatement,
                       tableName,
                       columnName,
                       columnPos,
                       rowNo);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated column to an existing foreign key value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   * @param fkList            the existing foreign keys
   */
  protected final void prepStmntColFk(PreparedStatement preparedStatement,
                                      String tableName,
                                      String columnName,
                                      int columnPos,
                                      int rowNo,
                                      ArrayList<Object> fkList) {
    try {
      preparedStatement.setObject(columnPos,
                                  getContentFk(tableName,
                                               columnName,
                                               rowNo,
                                               fkList));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional column to an existing foreign key value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   * @param fkList            the existing foreign keys
   */
  protected final void prepStmntColFkOpt(PreparedStatement preparedStatement,
                                         String tableName,
                                         String columnName,
                                         int columnPos,
                                         int rowNo,
                                         ArrayList<Object> fkList) {
    try {
      if (rowNo % nullFactor == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.INTEGER);
        return;
      }

      prepStmntColFk(preparedStatement,
                     tableName,
                     columnName,
                     columnPos,
                     rowNo,
                     fkList);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated column to a TIMESTAMP value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  protected final void prepStmntColTimestamp(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, int rowNo) {
    try {
      preparedStatement.setTimestamp(columnPos,
                                     getContentTimestamp(tableName,
                                                         columnName,
                                                         rowNo));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional column to a TIMESTAMP value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  protected final void prepStmntColTimestampOpt(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, int rowNo) {
    try {
      if (rowNo % nullFactor == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.TIMESTAMP);
        return;
      }

      prepStmntColTimestamp(preparedStatement,
                            tableName,
                            columnName,
                            columnPos,
                            rowNo);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated column to a VARCHAR value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   * @param size              the column size
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  protected final void prepStmntColVarchar(PreparedStatement preparedStatement,
                                           String tableName,
                                           String columnName,
                                           int columnPos,
                                           int rowNo,
                                           int size,
                                           String lowerRange,
                                           String upperRange,
                                           List<String> validValues) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      if (dbmsEnum == DbmsEnum.FIREBIRD || dbmsEnum == DbmsEnum.MARIADB || dbmsEnum == DbmsEnum.MSSQLSERVER || dbmsEnum == DbmsEnum.ORACLE) {
        preparedStatement.setNString(columnPos,
                                     getContentVarchar(tableName,
                                                       columnName,
                                                       rowNo,
                                                       size,
                                                       upperRange,
                                                       upperRange,
                                                       validValues));
        return;
      }

      preparedStatement.setString(columnPos,
                                  getContentVarchar(tableName,
                                                    columnName,
                                                    rowNo,
                                                    size,
                                                    upperRange,
                                                    upperRange,
                                                    validValues));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Sets the designated optional column to a VARCHAR value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   * @param size              the column size
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  protected final void prepStmntColVarcharOpt(PreparedStatement preparedStatement,
                                              String tableName,
                                              String columnName,
                                              int columnPos,
                                              int rowNo,
                                              int size,
                                              String lowerRange,
                                              String upperRange,
                                              ArrayList<String> validValues) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      if (rowNo % nullFactor == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.VARCHAR);
      } else {
        prepStmntColVarchar(preparedStatement,
                            tableName,
                            columnName,
                            columnPos,
                            rowNo,
                            size,
                            lowerRange,
                            upperRange,
                            validValues);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private final byte[] readBlobFile2Bytes() {
    if (isDebug) {
      logger.debug("Start");

      logger.debug("BLOB_FILE ='" + BLOB_FILE + "'");
    }

    File      file       = new File(BLOB_FILE);

    final int fileLength = (int) file.length();

    if (isDebug) {
      logger.debug("fileLength=" + fileLength);
    }

    byte[]          blobDataBytesArray = new byte[(int) file.length()];

    FileInputStream fileInputStream;

    try {
      fileInputStream = new FileInputStream(file);

      int size = fileInputStream.read(blobDataBytesArray);

      if (size == 0) {
        MessageHandling.abortProgram(logger,
                                     "No BLOB data found");
      }

      fileInputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("byteLength=" + blobDataBytesArray.length);

      logger.debug("End");
    }

    return blobDataBytesArray;
  }

  private final String readClobFile() {
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader(CLOB_FILE));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    StringBuilder clobData = new StringBuilder();
    String        nextLine;

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

  /**
   * Delete any existing relevant database schema objects (database, user, 
   * schema or valTableNames)and initialise the database for a new run.
   */
  protected abstract void setupDatabase();

  private final void validateNumberRows(String tableName, int expectedRows) {
    if (isDebug) {
      logger.debug("Start");
    }

    int count = 0;

    try {
      statement = connection.createStatement();

      resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + tableNameDelimiter + tableName + tableNameDelimiter);

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
      logger.info("database table " + String.format(FORMAT_TABLE_NAME,
                                                    tableName) + " - " + String.format(FORMAT_ROW_NO,
                                                                                       count) + " rows created");
    } else {
      logger.fatal("database table " + String.format(FORMAT_TABLE_NAME,
                                                     tableName) + " is incomplete - expected" + String.format(FORMAT_ROW_NO,
                                                                                                              expectedRows) + " rows - found " + String.format(
                                                                                                                                                               FORMAT_ROW_NO,
                                                                                                                                                               count)
          + " rows");
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }
}
