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
  private final Properties    COLUMN_NAME;
  protected Connection        connection      = null;

  protected String            driver          = "";
  protected String            dropTableStmnt  = "";

  protected boolean           isClient        = true;
  protected boolean           isEmbedded      = !(isClient);

  private final int           RANDOM_NUMBER   = 4;
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

    config      = new Config();

    COLUMN_NAME = createColumnNames();

    isClient    = true;
    isEmbedded  = !(this.isClient);

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

    config        = new Config();

    COLUMN_NAME   = createColumnNames();

    this.isClient = isClient;
    isEmbedded    = !(this.isClient);

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

  private int countData(final String tableName) {
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
      createData(tableName,
                 getMaxRowSize(tableName));
    }

    disconnect(connection);

    statistics.createMeasuringEntry();

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void createData(String tableName, int rowCount) {
    if (isDebug) {
      logger.debug("Start - database table " + String.format(FORMAT_TABLE_NAME,
                                                             tableName) + " - " + String.format(FORMAT_ROW_NO,
                                                                                                rowCount) + " rows to be created");
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

    autoIncrement = 0;

    createDataInsert(tableName,
                     rowCount,
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
                       rowCount);

    validateEncoding(tableName,
                     "NAME",
                     rowCount);

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void createDataInsert(String tableName, int rowCount, ArrayList<Object> pkList) {
    if (isDebug) {
      logger.debug("Start");
    }

    final String sqlStmnt = "INSERT INTO " + tableNameDelimiter + tableName + tableNameDelimiter + " (" + createDmlStmnt(tableName) + ")";

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

    for (int rowNo = 1; rowNo <= rowCount; rowNo++) {
      try {
        preparedStatement.setInt(1,
                                 autoIncrement);
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }

      insertTable(preparedStatement,
                  tableName,
                  rowCount,
                  rowNo,
                  pkList);

      try {
        preparedStatement.executeUpdate();

        pkList.add(autoIncrement);

        autoIncrement++;
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

  private String createDmlStmnt(final String tableName) {
    return "pk_" + tableName.toLowerCase() + "_id," + dmlStatements.get(tableName);
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

  private String getColumnContent(String columnName, int rowNo) {
    return columnName + COLUMN_NAME.getProperty(columnName + rowNo % ENCODING_MAX) + String.format(FORMAT_IDENTIFIER,
                                                                                                   rowNo);
  }

  private int getMaxRowSize(final String tableName) {
    int maxRowSize = maxRowSizes.get(tableName);

    if (maxRowSize < 1) {
      return config.getDefaultRowSize();
    } else {
      int MAX_ROW_SIZE = Integer.MAX_VALUE;
      if (maxRowSize > MAX_ROW_SIZE) {
        return MAX_ROW_SIZE;
      }
    }

    return maxRowSize;
  }

  protected final int getRandomIntExcluded(final int upperLimit) {
    return randomInt.nextInt(upperLimit);
  }

  private int getRandomIntIncluded(final int upperLimit) {
    return randomInt.nextInt(upperLimit) + 1;
  }

  private Timestamp getRandomTimestamp() {

    return new java.sql.Timestamp(System.currentTimeMillis() + randomInt.nextInt(2147483647));
  }

  //  private final double getRandomDouble(final double lowerLimit, final double upperLimit) {
  //    return ThreadLocalRandom.current().nextDouble(lowerLimit, upperLimit);
  //  }

  protected abstract void insertTable(PreparedStatement preparedStatement2, String tableName, int rowCount, int rowNo, ArrayList<Object> pkList);

  /**
   * Sets the designated optional parameter to a BLOB value.
   *
   * @param preparedStatement the prepared statement
   * @param columnPos         the column position
   * @param rowCount          the row count
   */
  protected void prepStmntInsertColBlob(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      preparedStatement.setBytes(columnPos,
                                 BLOB_DATA_BYTES);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  protected final void prepStmntInsertColBlobOpt(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      if (dbmsEnum == DbmsEnum.CRATEDB) {
        preparedStatement.setNull(columnPos,
                                  Types.NULL);
      } else if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        if (dbmsEnum == DbmsEnum.POSTGRESQL) {
          preparedStatement.setNull(columnPos,
                                    Types.NULL);
        } else {
          preparedStatement.setNull(columnPos,
                                    Types.BLOB);
        }
      } else {
        prepStmntInsertColBlob(preparedStatement,
                               columnPos,
                               rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private void prepStmntInsertColClob(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      preparedStatement.setString(columnPos,
                                  CLOB_DATA);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  protected final void prepStmntInsertColClobOpt(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        if (dbmsEnum == DbmsEnum.CRATEDB) {
          preparedStatement.setNull(columnPos,
                                    Types.VARCHAR);
        } else {
          preparedStatement.setNull(columnPos,
                                    java.sql.Types.CLOB);
        }
      } else {
        prepStmntInsertColClob(preparedStatement,
                               columnPos,
                               rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  protected final void prepStmntInsertColDatetime(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      preparedStatement.setTimestamp(columnPos,
                                     getRandomTimestamp());
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  protected final void prepStmntInsertColDatetimeOpt(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.TIMESTAMP);
      } else {
        prepStmntInsertColDatetime(preparedStatement,
                                   columnPos,
                                   rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  protected final void prepStmntInsertColFKOpt(final int columnPos, PreparedStatement preparedStatement, final ArrayList<Object> fkList, final int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.INTEGER);
      } else {
        preparedStatement.setObject(columnPos,
                                    fkList.get(getRandomIntExcluded(fkList.size())));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  protected final void prepStmntInsertColFlagNY(PreparedStatement preparedStatement, final int columnPos, final int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setString(columnPos,
                                    "N");
      } else {
        preparedStatement.setString(columnPos,
                                    "Y");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  protected final void prepStmntInsertColString(final PreparedStatement preparedStatement, final int columnPos, final String columnName, final int rowNo) {
    try {
      if (dbmsEnum == DbmsEnum.FIREBIRD || dbmsEnum == DbmsEnum.MARIADB || dbmsEnum == DbmsEnum.MSSQLSERVER || dbmsEnum == DbmsEnum.ORACLE) {
        preparedStatement.setNString(columnPos,
                                     getColumnContent(columnName,
                                                      rowNo));
      } else {
        preparedStatement.setString(columnPos,
                                    getColumnContent(columnName,
                                                     rowNo));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  //  protected final void
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

  protected final void prepStmntInsertColStringOpt(final PreparedStatement preparedStatement,
                                                   final int columnPos,
                                                   final String columnName,
                                                   final int rowCount,
                                                   final int rowNo) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.VARCHAR);
      } else {
        prepStmntInsertColString(preparedStatement,
                                 columnPos,
                                 columnName,
                                 rowNo);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
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

  /**
   * Delete any existing relevant database schema objects (database, user, 
   * schema or valTableNames)and initialise the database for a new run.
   */
  protected abstract void setupDatabase();

  private void validateEncoding(String tableName, String columnName, int rowCount) {
    if (isDebug) {
      logger.debug("Start");
    }

    switch (rowCount) {
    case 0 -> logger.info("database table " + String.format(FORMAT_TABLE_NAME,
                                                            tableName) + " - no rows generated");
    case 1 -> validateEncodingType(tableName,
                                   columnName,
                                   0);
    case 2 -> {
      validateEncodingType(tableName,
                           columnName,
                           0);
      validateEncodingType(tableName,
                           columnName,
                           1);
    }
    default -> {
      validateEncodingType(tableName,
                           columnName,
                           0);
      validateEncodingType(tableName,
                           columnName,
                           1);
      validateEncodingType(tableName,
                           columnName,
                           2);
    }
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void validateEncodingType(String tableName, String columnName, int rowNo) {
    if (isDebug) {
      logger.debug("Start");
    }

    int    count        = 0;

    String encodingType = "";

    switch (rowNo) {
    case 0 -> encodingType = "ASCII";
    case 1 -> encodingType = "ISO_8859_1";
    case 2 -> encodingType = "UTF_8";
    default -> MessageHandling.abortProgram(logger,
                                            "Database table " + String.format(FORMAT_TABLE_NAME,
                                                                              tableName) + " - wrong encoding key : " + rowNo);
    }

    try {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM " + tableNameDelimiter + tableName + tableNameDelimiter
          + " WHERE " + columnName + " = ?");
      preparedStatement.setString(1,
                                  getColumnContent(columnName + "_",
                                                   rowNo));

      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      switch (count) {
      case 0:
        MessageHandling.abortProgram(logger,
                                     "Database table " + String.format(FORMAT_TABLE_NAME,
                                                                       tableName) + " - no rows generated - comparison value='" + getColumnContent(columnName
                                                                           + "_",
                                                                                                                                                   rowNo)
                                         + "'");
      case 1:
        if (isDebug) {
          logger.debug("database table " + String.format(FORMAT_TABLE_NAME,
                                                         tableName) + " - encoding " + encodingType + " ok");
        }
        break;
      default:
        MessageHandling.abortProgram(logger,
                                     "Database table " + String.format(FORMAT_TABLE_NAME,
                                                                       tableName) + " - too many hits: " + count);
      }

      resultSet.close();

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void validateNumberRows(String tableName, int expectedRows) {
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
