package ch.konnexions.db_seeder.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.AbstractDbmsSeeder;
import ch.konnexions.db_seeder.utils.Config;
import ch.konnexions.db_seeder.utils.MessageHandling;
import ch.konnexions.db_seeder.utils.Statistics;

/**
 * Data Generator for a Database - Abstract JDBC Seeder.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public abstract class AbstractJdbcSeeder extends AbstractJdbcSchema {

  private static final int    ENCODING_MAX     = 3;

  private static final Logger logger           = LogManager.getLogger(AbstractJdbcSeeder.class);

  private static final int    XLOB_OMNISCI_MAX = 32767 / 2;

  /**
   * Gets the catalog name.
   *
   * @param tickerSymbolLower the lower case DBMS ticker symbol
   * @return the catalog name
   */
  public static String getCatalogName(String tickerSymbolLower) {
    return "db_seeder_" + tickerSymbolLower;
  }

  /**
   * Gets the trino URL string.
   *
   * @param tickerSymbolLower the lower case DBMS ticker symbol
   * @param connectionHost    the connection host name
   * @param connectionPort    the connection port
   * @param databaseSchema    the database schema
   * @return the trino URL string
   */
  public static String getUrlTrino(String tickerSymbolLower, String connectionHost, int connectionPort, String databaseSchema) {
    return "jdbc:trino://" + connectionHost + ":" + connectionPort + "/" + getCatalogName(tickerSymbolLower) + "/" + databaseSchema + "?user=trino";
  }

  private final boolean   isDebug                = logger.isDebugEnabled();

  private final String    BLOB_FILE              = Paths.get("src",
                                                             "main",
                                                             "resources").toAbsolutePath() + File.separator + "blob.png";
  private final byte[]    BLOB_DATA_BYTES        = readBlobFile2Bytes();
  private final String    BLOB_DATA_BYTES_STRING = new String(readBlobFile2Bytes(), StandardCharsets.UTF_8);

  private final String    CLOB_FILE              = Paths.get("src",
                                                             "main",
                                                             "resources").toAbsolutePath() + File.separator + "clob.md";
  private final String    CLOB_DATA              = readClobFile();
  protected Connection    connection             = null;

  protected String        driver                 = "";
  protected final String  driver_trino           = "io.trino.jdbc.TrinoDriver";
  protected String        dropTableStmnt         = "";

  protected Properties    encodedColumnNames     = new Properties();

  protected final boolean isClient;
  protected final boolean isTrino;

  protected int           nullFactor;

  private final Random    randomInt              = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
  protected ResultSet     resultSet              = null;

  protected Statement     statement              = null;

  protected String        urlTrino               = "";
  protected String        urlSys                 = "";
  protected String        urlUser                = "";

  /**
   * Initialises a new abstract JDBC seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public AbstractJdbcSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    config = new Config();

    boolean isEmbedded;

    if ("embedded".equals(dbmsOption)) {
      isClient   = false;
      isEmbedded = true;
      isTrino    = false;
    } else if ("trino".equals(dbmsOption)) {
      isClient   = false;
      isEmbedded = false;
      isTrino    = true;
    } else {
      isClient   = true;
      isEmbedded = false;
      isTrino    = false;
    }

    if (isDebug) {
      logger.debug("client  =" + isClient);
      logger.debug("embedded=" + isEmbedded);
      logger.debug("trino  =" + isTrino);

      logger.debug("End   Constructor");
    }
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
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
   * @param url        the URL
   * @param autoCommit the auto commit option
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
   * @param url    the URL
   * @param driver the database driver
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
   * @param url        the URL
   * @param driver     the database driver
   * @param autoCommit the auto commit option
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
   * @param url      the URL
   * @param driver   the database driver
   * @param user     the user name
   * @param password the password
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
   * @param urlIn      the URL
   * @param driver     the database driver
   * @param user       the user name
   * @param password   the password
   * @param autoCommit the auto commit option
   * @return the database connection
   */
  protected final Connection connect(String urlIn, String driver, String user, String password, boolean autoCommit) {
    if (isDebug) {
      logger.debug("Start");
    }

    if (driver != null) {
      try {
        if (isDebug) {
          logger.debug("driver='" + driver + "'");
        }

        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    Connection connection = null;

    String     url        = urlIn.replace("\"",
                                          "");

    try {
      if (isDebug) {
        logger.debug("url='" + url + "'");
      }

      if (user == null && password == null) {
        connection = DriverManager.getConnection(url);
      } else {
        if (isDebug) {
          logger.debug("user='" + user + "' password='" + password + "'");
        }
        connection = DriverManager.getConnection(url,
                                                 user,
                                                 password);
      }

      connection.setAutoCommit(autoCommit);

      if (isDebug) {
        logger.debug("auto=" + connection.getAutoCommit());
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End   [" + connection + "]");
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

      String sqlStmnt = "SELECT COUNT(*) FROM " + identifierDelimiter + tableName + identifierDelimiter;

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
   * Create the column names with encoding variations.
   *
   * @param isEncodingIso_8859_1 the is encoding ISO_8859_1 8859 1
   * @param isEncodingUtf_8      the is encoding UTF_8 required
   */
  protected abstract void createColumnNames(boolean isEncodingIso_8859_1, boolean isEncodingUtf_8);

  /**
   * Create the data for all database valTableNames.
   */
  public final void createData() {
    if (isDebug) {
      logger.debug("Start");
    }

    Statistics statistics = new Statistics(config, tickerSymbolExtern, dbmsDetails);

    setupDatabase();

    statistics.setStartDateTimeDML();

    for (String tableName : TABLE_NAMES_CREATE) {
      LocalDateTime startDateTime = LocalDateTime.now();
      createData(tableName);
      logger.info(String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                Duration.between(startDateTime,
                                                 LocalDateTime.now()).toMillis()) + " ms - total DML database table " + tableName);
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

    String editedTableName;

    if (dbmsEnum == DbmsEnum.MYSQL
        || dbmsEnum == DbmsEnum.OMNISCI
        || dbmsEnum == DbmsEnum.ORACLE
        || dbmsEnum == DbmsEnum.PERCONA
        || dbmsEnum == DbmsEnum.POSTGRESQL
        || dbmsEnum == DbmsEnum.SQLSERVER) {
      editedTableName = tableName.toLowerCase();
    } else {
      editedTableName = tableName.toUpperCase();
    }

    final int countExisting = countData(editedTableName);

    if (countExisting != 0) {
      if (isDebug) {
        logger.debug("found existing data in database table " + String.format(FORMAT_TABLE_NAME,
                                                                              tableName));
      }

      return;
    }

    ArrayList<Object> pkList = new ArrayList<>();

    createDataInsert(tableName,
                     rowMaxSize,
                     pkList);

    try {
      if (!(connection.getAutoCommit())) {
        connection.commit();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    pkLists.put(tableName,
                pkList);
    pkListSizes.put(tableName,
                    pkList.size());

    validateNumberRows(editedTableName,
                       rowMaxSize);

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void createDataInsert(String tableName, int rowMaxSize, ArrayList<Object> pkList) {
    if (isDebug) {
      logger.debug("Start");
    }

    String editedTableName;

    if (dbmsEnum == DbmsEnum.MYSQL
        || dbmsEnum == DbmsEnum.OMNISCI
        || dbmsEnum == DbmsEnum.ORACLE
        || dbmsEnum == DbmsEnum.PERCONA
        || dbmsEnum == DbmsEnum.POSTGRESQL
        || dbmsEnum == DbmsEnum.SQLSERVER) {
      editedTableName = tableName.toLowerCase();
    } else {
      editedTableName = tableName.toUpperCase();
    }

    final String sqlStmnt = "INSERT INTO " + identifierDelimiter + editedTableName + identifierDelimiter + " (" + (dbmsEnum == DbmsEnum.OMNISCI
        ? dmlStatements.get(tableName).toLowerCase()
        : dmlStatements.get(tableName)) + ")";

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

    boolean isToBeExecuted = false;

    for (long rowNo = 1; rowNo <= rowMaxSize; rowNo++) {

      if (rowNo % 500 == 0) {
        logger.info("database table " + String.format(FORMAT_TABLE_NAME,
                                                      tableName.toUpperCase()) + " - " + String.format(FORMAT_ROW_NO + " rows so far",
                                                                                                       rowNo));
      }

      insertTable(preparedStatement,
                  tableName,
                  rowNo);

      try {
        preparedStatement.addBatch();

        if (rowNo % batchSize == 0) {
          preparedStatement.executeBatch();
          isToBeExecuted = false;
        } else {
          isToBeExecuted = true;
        }

        pkList.add(rowNo);
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      if (isToBeExecuted) {
        preparedStatement.executeBatch();
      }

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (dbmsEnum == DbmsEnum.CRATEDB) {
      try {
        statement = connection.createStatement();

        statement.execute("REFRESH TABLE " + editedTableName);

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
   * @return the 'CREATE TABLE' statement
   */
  protected abstract String createDdlStmnt(String tableName);

  /**
   * Create the all database tables.
   *
   * @param connection the database connection
   */
  protected final void createSchema(Connection connection) {
    if (isDebug) {
      logger.debug("Start");
    }

    for (String tableName : TABLE_NAMES_CREATE) {
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
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

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
                                             dbmsEnum == DbmsEnum.CRATEDB || dbmsEnum == DbmsEnum.OMNISCI

                                                 ? tableName.toLowerCase()
                                                 : tableName.toUpperCase());

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
        String sqlStmnt = "DROP TABLE" + (dbmsEnum == DbmsEnum.VOLTDB
            ? " " + identifierDelimiter + tableName + identifierDelimiter + " "
            : " ") + "IF EXISTS" + (dbmsEnum == DbmsEnum.VOLTDB
                ? ""
                : " " + identifierDelimiter + tableName + identifierDelimiter);

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
   * @param databaseName    the database name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName       the table name
   * @param columnName      the column name
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
        sqlStmnt = "DROP " + (dbmsEnum == DbmsEnum.MIMER
            ? "DATABANK"
            : "DATABASE") + " " + databaseName + (cascadeRestrict != null
                ? " " + cascadeRestrict
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
   * @param schemaName      the schema name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName       the table name
   * @param columnName      the column name
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
        sqlStmnt = "DROP SCHEMA " + schemaName + (cascadeRestrict != null
            ? " " + cascadeRestrict
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
   * Drop the database user.
   *
   * @param userName        the user name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName       the table name
   * @param columnName      the column name
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
        sqlStmnt = "DROP " + (dbmsEnum == DbmsEnum.MIMER
            ? "IDENT"
            : "USER") + "  " + userName + (cascadeRestrict != null
                ? " " + cascadeRestrict
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
   * Execute DDL statements.
   *
   * @param statement          the JDBC statement
   * @param firstDdlStmnt      the first DDL statement
   * @param remainingDdlStmnts the remaining DDL statements
   */
  protected final void executeDdlStmnts(Statement statement, String firstDdlStmnt, String... remainingDdlStmnts) {
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

  protected long getContentBigint(String tableName,
                                  String columnName,
                                  long rowNo,
                                  Integer defaultValue,
                                  Integer lowerRange,
                                  Integer upperRange,
                                  List<Integer> validValues) {
    if (validValues != null) {
      return validValues.get(new Random().nextInt(validValues.size()));
    } else if (lowerRange != null) {
      return new Random().nextInt(upperRange - lowerRange + 1) + lowerRange - 1;
    }

    return rowNo;
  }

  protected byte[] getContentBlob(String tableName, String columnName, long rowNo) {

    return BLOB_DATA_BYTES;
  }

  protected String getContentBlobString(String tableName, String columnName, long rowNo) {

    return BLOB_DATA_BYTES_STRING;
  }

  protected String getContentClob(String tableName, String columnName, long rowNo) {

    return CLOB_DATA;
  }

  private Object getContentFk(String tableName, String columnName, long rowNo, ArrayList<Object> fkList) {
    Random random = new Random();

    return fkList.get(random.nextInt(fkList.size()));
  }

  private int getContentFkInt(String tableName, String columnName, long rowNo, ArrayList<Object> fkList) {
    Random random = new Random();

    return random.nextInt(fkList.size());
  }

  protected Timestamp getContentTimestamp(String tableName, String columnName, long rowNo) {

    return new java.sql.Timestamp(System.currentTimeMillis() + randomInt.nextInt(2147483647));
  }

  protected String getContentVarchar(String tableName,
                                     String columnName,
                                     long rowNo,
                                     int size,
                                     String defaultValue,
                                     String lowerRange,
                                     String upperRange,
                                     List<String> validValues) {
    if (isDebug) {
      logger.debug("Start");
    }

    String columnValue;

    if (validValues != null) {
      return validValues.get(new Random().nextInt(validValues.size())).stripTrailing();
    } else if (lowerRange != null) {
      columnValue = RandomStringUtils.randomGraph(1,
                                                  size + 1);

      if (columnValue.compareTo(lowerRange) < 0) {
        return lowerRange;
      }

      if (columnValue.compareTo(upperRange) > 0) {
        return upperRange;
      }
    } else {
      columnValue = (columnName + "_" + encodedColumnNames.getProperty(columnName + "_" + rowNo % ENCODING_MAX) + String.format(FORMAT_IDENTIFIER,
                                                                                                                                rowNo)).stripTrailing();
      if (getLengthUTF_8(columnValue) > size) {
        return columnValue.substring(columnValue.length() - size);
      }
    }

    if (isDebug) {
      logger.debug("End");
    }

    return columnValue;
  }

  private int getLengthUTF_8(String stringUTF_8) {
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

  private int getMaxRowSize(String tableName) {
    int maxRowSize   = maxRowSizes.get(tableName);

    int MAX_ROW_SIZE = Integer.MAX_VALUE;

    return Math.min(maxRowSize,
                    MAX_ROW_SIZE);
  }

  protected abstract void insertTable(PreparedStatement preparedStatement, String tableName, long rowNo);

  /**
   * Sets the designated column to a BIGINT value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   * @param defaultValue      the default value
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  protected void prepStmntColBigint(PreparedStatement preparedStatement,
                                    String tableName,
                                    String columnName,
                                    int columnPos,
                                    long rowNo,
                                    Integer defaultValue,
                                    Integer lowerRange,
                                    Integer upperRange,
                                    List<Integer> validValues) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      preparedStatement.setLong(columnPos,
                                getContentBigint(tableName,
                                                 columnName,
                                                 rowNo,
                                                 defaultValue,
                                                 lowerRange,
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
   * Sets the designated optional column to a BIGINT value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   * @param defaultValue      the lower value
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  @SuppressWarnings("ucd")
  protected final void prepStmntColBigintOpt(PreparedStatement preparedStatement,
                                             String tableName,
                                             String columnName,
                                             int columnPos,
                                             long rowNo,
                                             Integer defaultValue,
                                             Integer lowerRange,
                                             Integer upperRange,
                                             List<Integer> validValues) {
    try {
      if (rowNo % nullFactor == 0) {
        if (defaultValue == null) {
          preparedStatement.setNull(columnPos,
                                    java.sql.Types.INTEGER);
        } else {
          preparedStatement.setLong(columnPos,
                                    defaultValue);
        }
        return;
      }

      prepStmntColBigint(preparedStatement,
                         tableName,
                         columnName,
                         columnPos,
                         rowNo,
                         defaultValue,
                         lowerRange,
                         upperRange,
                         validValues);
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
  private void prepStmntColBlob(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
    try {
      if (dbmsEnum == DbmsEnum.EXASOL) {
        preparedStatement.setString(columnPos,
                                    getContentBlobString(tableName,
                                                         columnName,
                                                         rowNo));
      } else if (dbmsEnum == DbmsEnum.OMNISCI) {
        String dataValue = getContentBlobString(tableName,
                                                columnName,
                                                rowNo);
        preparedStatement.setString(columnPos,
                                    dataValue.substring(0,
                                                        Math.min(dataValue.length(),
                                                                 XLOB_OMNISCI_MAX)));
      } else {
        preparedStatement.setBytes(columnPos,
                                   getContentBlob(tableName,
                                                  columnName,
                                                  rowNo));
      }
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
  @SuppressWarnings("ucd")
  protected final void prepStmntColBlobOpt(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
    try {
      if (dbmsEnum == DbmsEnum.CRATEDB) {
        preparedStatement.setNull(columnPos,
                                  Types.NULL);
        return;
      }

      if (rowNo % nullFactor == 0) {
        if (dbmsEnum == DbmsEnum.EXASOL || dbmsEnum == DbmsEnum.POSTGRESQL || dbmsEnum == DbmsEnum.VOLTDB || dbmsEnum == DbmsEnum.YUGABYTE) {
          preparedStatement.setNull(columnPos,
                                    Types.NULL);
        } else {
          preparedStatement.setNull(columnPos,
                                    Types.BLOB);
        }
        return;
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
  private void prepStmntColClob(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
    try {
      if (dbmsEnum == DbmsEnum.OMNISCI) {
        String dataValue = getContentClob(tableName,
                                          columnName,
                                          rowNo);
        preparedStatement.setString(columnPos,
                                    dataValue.substring(0,
                                                        Math.min(dataValue.length(),
                                                                 XLOB_OMNISCI_MAX)));
      } else {
        preparedStatement.setString(columnPos,
                                    getContentClob(tableName,
                                                   columnName,
                                                   rowNo));
      }
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
  @SuppressWarnings("ucd")
  protected final void prepStmntColClobOpt(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
    try {
      if (rowNo % nullFactor == 0) {
        if (dbmsEnum == DbmsEnum.CRATEDB || dbmsEnum == DbmsEnum.EXASOL || dbmsEnum == DbmsEnum.VOLTDB) {
          preparedStatement.setNull(columnPos,
                                    Types.VARCHAR);
        } else {
          preparedStatement.setNull(columnPos,
                                    java.sql.Types.CLOB);
        }
        return;
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
                                      long rowNo,
                                      ArrayList<Object> fkList) {

    try {
      if (dbmsEnum == DbmsEnum.OMNISCI) {
        preparedStatement.setLong(columnPos,
                                  getContentFkInt(tableName,
                                                  columnName,
                                                  rowNo,
                                                  fkList));
        return;
      }

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
  @SuppressWarnings("ucd")
  protected final void prepStmntColFkOpt(PreparedStatement preparedStatement,
                                         String tableName,
                                         String columnName,
                                         int columnPos,
                                         long rowNo,
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
  protected final void prepStmntColTimestamp(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
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
  @SuppressWarnings("ucd")
  protected final void prepStmntColTimestampOpt(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
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
   * @param defaultValue      the default value
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  protected final void prepStmntColVarchar(PreparedStatement preparedStatement,
                                           String tableName,
                                           String columnName,
                                           int columnPos,
                                           long rowNo,
                                           int size,
                                           String defaultValue,
                                           String lowerRange,
                                           String upperRange,
                                           List<String> validValues) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      if (dbmsEnum == DbmsEnum.FIREBIRD || dbmsEnum == DbmsEnum.MARIADB || dbmsEnum == DbmsEnum.SQLSERVER || dbmsEnum == DbmsEnum.ORACLE) {
        preparedStatement.setNString(columnPos,
                                     getContentVarchar(tableName,
                                                       columnName,
                                                       rowNo,
                                                       size,
                                                       defaultValue,
                                                       lowerRange,
                                                       upperRange,
                                                       validValues));
        return;
      }

      preparedStatement.setString(columnPos,
                                  getContentVarchar(tableName,
                                                    columnName,
                                                    rowNo,
                                                    size,
                                                    defaultValue,
                                                    lowerRange,
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
   * @param defaultValue      the default value
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  protected final void prepStmntColVarcharOpt(PreparedStatement preparedStatement,
                                              String tableName,
                                              String columnName,
                                              int columnPos,
                                              long rowNo,
                                              int size,
                                              String defaultValue,
                                              String lowerRange,
                                              String upperRange,
                                              List<String> validValues) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      if (rowNo % nullFactor == 0) {
        if (defaultValue == null) {
          preparedStatement.setNull(columnPos,
                                    java.sql.Types.VARCHAR);
        } else {
          if (dbmsEnum == DbmsEnum.FIREBIRD || dbmsEnum == DbmsEnum.MARIADB || dbmsEnum == DbmsEnum.SQLSERVER || dbmsEnum == DbmsEnum.ORACLE) {
            preparedStatement.setNString(columnPos,
                                         defaultValue);
          } else {
            preparedStatement.setString(columnPos,
                                        defaultValue);
          }
        }
      } else {
        prepStmntColVarchar(preparedStatement,
                            tableName,
                            columnName,
                            columnPos,
                            rowNo,
                            size,
                            defaultValue,
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

  private byte[] readBlobFile2Bytes() {
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
                                     "Program abort: no BLOB data found");
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

  private String readClobFile() {
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

  /**
   * Delete any existing relevant database schema objects (database, user,
   * schema or valTableNames)and initialise the database for a new run.
   *
   * @param driver  the database driver
   * @param urlSys  the database URL for privileged access
   * @param urlUser the database URL for non-privileged access
   * @return the database connection
   */
  public Connection setupMysql(String driver, String urlSys, String urlUser) {
    if (isDebug) {
      logger.debug("Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSys,
                         driver);

    String databaseName = config.getDatabase();
    String userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts(statement,
                       "DROP DATABASE IF EXISTS `" + databaseName + "`",
                       "DROP USER IF EXISTS `" + userName + "`");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts(statement,
                       "CREATE DATABASE `" + databaseName + "`",
                       "USE `" + databaseName + "`",
                       "CREATE USER `" + userName + "` IDENTIFIED BY '" + config.getPassword() + "'",
                       "GRANT ALL ON " + databaseName + ".* TO `" + userName + "`");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(urlUser);

    try {
      statement = connection.createStatement();

      createSchema(connection);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }

    return connection;
  }

  /**
   * Delete any existing relevant database schema objects (database, user,
   * schema or valTableNames)and initialise the database for a new run.
   *
   * @param driver  the database driver
   * @param urlSys  the database URL for privileged access
   * @param urlUser the database URL for non-privileged access
   * @return the database connection
   */
  public Connection setupPostgresql(String driver, String urlSys, String urlUser) {
    if (isDebug) {
      logger.debug("Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    Connection connection   = connect(urlSys,
                                      true);

    String     databaseName = config.getDatabase();
    String     schemaName   = config.getSchema();
    String     userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts(statement,
                       "DROP SCHEMA IF EXISTS " + schemaName + " CASCADE",
                       "DROP DATABASE IF EXISTS " + databaseName,
                       "DROP USER IF EXISTS " + userName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts(statement,
                       "CREATE USER " + userName + " WITH ENCRYPTED PASSWORD '" + config.getPassword() + "'",
                       "CREATE DATABASE " + databaseName + " WITH OWNER " + userName,
                       "GRANT ALL PRIVILEGES ON DATABASE " + databaseName + " TO " + userName);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(urlUser);

    try {
      statement = connection.createStatement();

      executeDdlStmnts(statement,
                       "CREATE SCHEMA " + schemaName,
                       "SET search_path = " + schemaName);

      createSchema(connection);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }

    return connection;
  }

  private void validateNumberRows(String tableName, int expectedRows) {
    if (isDebug) {
      logger.debug("Start");
    }

    int count = 0;

    try {
      statement = connection.createStatement();

      String sqlStmnt = "SELECT COUNT(*) FROM " + identifierDelimiter + tableName + identifierDelimiter;

      if (isDebug) {
        logger.debug("sqlStmnt='" + sqlStmnt + "'");
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

    if (expectedRows == count) {
      logger.info("database table " + String.format(FORMAT_TABLE_NAME,
                                                    tableName.toUpperCase()) + " - " + String.format(FORMAT_ROW_NO,
                                                                                                     count) + " rows in total");
    } else {
      logger.fatal("database table " + String.format(FORMAT_TABLE_NAME,
                                                     tableName.toUpperCase()) + " is incomplete - expected" + String.format(FORMAT_ROW_NO,
                                                                                                                            expectedRows) + " rows - found "
          + String.format(FORMAT_ROW_NO,
                          count) + " rows");
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }
}
