package ch.konnexions.db_seeder.samples.trino;

import ch.konnexions.db_seeder.AbstractDbmsSeeder;
import ch.konnexions.db_seeder.utils.MessageHandling;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Paths;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.Scanner;

/**
 * Demonstration program for Issues MySQL Connector.
 *
 * @author walter@konnexions.ch
 * @since 2020-08-11
 */
public final class SampleMysql {
  private static final int     BATCH_SIZE          = 100;

  private static final String  BLOB_FILE           = Paths.get("src",
                                                               "main",
                                                               "resources").toAbsolutePath() + File.separator + "blob.png";
  private static final byte[]  BLOB_DATA_BYTES     = readBlobFile2Bytes();

  private static final String  CLOB_FILE           = Paths.get("src",
                                                               "main",
                                                               "resources").toAbsolutePath() + File.separator + "clob.md";
  private static final String  CLOB_DATA           = readClobFile();
  private static Connection    connection;
  private static final String  connectionHost      = "localhost";
  private static final int     connectionPort      = 3306;

  private static final String  databaseName        = "kxn_db";
  private static final String  databaseNameSys     = "sys";
  private static final String  driverOriginal      = "com.mysql.cj.jdbc.Driver";
  private static final String  driverTrino         = "io.trino.jdbc.TrinoDriver";

  public static final String   FORMAT_ROW_NO       = "%1$6d";
  public static final String   FORMAT_ROW_SEC      = "%1$4d";

  private static final Logger  logger              = Logger.getLogger(SampleMysql.class);
  private final static boolean isDebug             = logger.isDebugEnabled();

  private static final int     nullFactor          = 4;

  private static final String  password            = "mysql";

  private static final int     rowMaxSize          = 1000;

  private static final String  sqlStmntCreateTable = """
                                                     CREATE TABLE issue_table (
                                                         column_pk        BIGINT         NOT NULL
                                                                                         PRIMARY KEY,
                                                         column_blob      LONGBLOB,
                                                         column_clob      LONGTEXT,
                                                         column_timestamp DATETIME       NOT NULL,
                                                         column_varchar   VARCHAR(100)   NOT NULL
                                                                                         UNIQUE
                                                     )
                                                     """;
  private static final String  sqlStmntInsert      = "INSERT INTO issue_table (column_pk, column_blob, column_clob, column_timestamp, column_varchar) VALUES (?, ?, ?, ?, ?)";
  private static Statement     statement;

  private static final String  userName            = "kxn_user";
  private static final String  userNameSys         = "root";

  private static final String  urlSys              = "jdbc:mysql://" + connectionHost + ":" + connectionPort + "/" + databaseNameSys
      + "?serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false&rewriteBatchedStatements=true";
  private static final String  urlTrino            = "jdbc:trino://localhost:8080/db_seeder_mysql/kxn_db?user=trino";
  private static final String  urlUser             = "jdbc:mysql://" + connectionHost + ":" + connectionPort + "/" + databaseName
      + "?serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false&rewriteBatchedStatements=true";

  private static Connection connect(String url, String driver, String user, String password, boolean autoCommit) {
    if (isDebug) {
      logger.debug("Start");
    }

    if (driver != null) {
      try {
        logger.debug("driver  ='" + driver + "'");
        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    Connection connection = null;

    logger.debug("url     ='" + url + "'");

    try {
      if (user == null && password == null) {
        connection = DriverManager.getConnection(url);
      } else {
        connection = DriverManager.getConnection(url,
                                                 user,
                                                 password);
      }

      connection.setAutoCommit(autoCommit);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }

    return connection;
  }

  private static void createColumnContent(PreparedStatement preparedStatement, long rowNo) throws SQLException {
    int columnPos = 1;

    preparedStatement.setLong(columnPos++,
                              rowNo);

    if (rowNo % nullFactor == 0) {
      preparedStatement.setNull(columnPos++,
                                Types.BLOB);
    } else {
      preparedStatement.setBytes(columnPos++,
                                 BLOB_DATA_BYTES);
    }

    if (rowNo % nullFactor == 0) {
      preparedStatement.setNull(columnPos++,
                                Types.VARCHAR);
    } else {
      preparedStatement.setString(columnPos++,
                                  CLOB_DATA);
    }

    preparedStatement.setTimestamp(columnPos++,
                                   new java.sql.Timestamp(System.currentTimeMillis() + new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).nextInt(
                                                                                                                                                             2147483647)));

    preparedStatement.setString(columnPos,
                                "varchar column #" + rowNo);
  }

  private static void demonstrateOriginal() {
    if (isDebug) {
      logger.debug("Start");
    }

    logger.info("Run the demo with the original JDBC driver");
    logger.info("");

    resetDatabase();
    setupDatabase();

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlUser,
                         driverOriginal,
                         userName,
                         password,
                         false);

    // -----------------------------------------------------------------------
    // Insert data.
    // -----------------------------------------------------------------------

    insertData();

    // -----------------------------------------------------------------------
    // Disconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    if (isDebug) {
      logger.debug("End");
    }
  }

  private static void demonstrateOriginalBatch() {
    if (isDebug) {
      logger.debug("Start");
    }

    logger.info("Run the demo with the original JDBC driver - with batch operations");
    logger.info("");

    resetDatabase();
    setupDatabase();

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlUser,
                         driverOriginal,
                         userName,
                         password,
                         false);

    // -----------------------------------------------------------------------
    // Insert data.
    // -----------------------------------------------------------------------

    insertDataBatch();

    // -----------------------------------------------------------------------
    // Disconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    if (isDebug) {
      logger.debug("End");
    }
  }

  private static void demonstrateTrino() {
    if (isDebug) {
      logger.debug("Start");
    }

    logger.info("Run the demo with the trino JDBC driver");
    logger.info("");

    resetDatabase();
    setupDatabase();

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlTrino,
                         driverTrino,
                         null,
                         null,
                         true);

    // -----------------------------------------------------------------------
    // Insert data.
    // -----------------------------------------------------------------------

    insertData();

    // -----------------------------------------------------------------------
    // Disconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    if (isDebug) {
      logger.debug("End");
    }
  }

  private static void demonstrateTrinoBatch() {
    if (isDebug) {
      logger.debug("Start");
    }

    logger.info("Run the demo with the trino JDBC driver - with batch operations");
    logger.info("");

    resetDatabase();
    setupDatabase();

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlTrino,
                         driverTrino,
                         null,
                         null,
                         true);

    // -----------------------------------------------------------------------
    // Insert data.
    // -----------------------------------------------------------------------

    insertDataBatch();

    // -----------------------------------------------------------------------
    // Disconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    if (isDebug) {
      logger.debug("End");
    }
  }

  private static void disconnect(Connection connection) {
    if (isDebug) {
      logger.debug("Start");
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

  private static void executeDdlStmnts(String firstDdlStmnt, String... remainingDdlStmnts) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      logger.debug("sqlStmnt='" + firstDdlStmnt + "'");
      statement.execute(firstDdlStmnt);

      for (String sqlStmnt : remainingDdlStmnts) {
        logger.debug("sqlStmnt='" + sqlStmnt + "'");
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

  private static void insertData() {
    if (isDebug) {
      logger.debug("Start");
    }

    PreparedStatement preparedStatement;

    LocalDateTime     startDateTime      = LocalDateTime.now();
    LocalDateTime     startDateTimeBatch = LocalDateTime.now();

    try {
      preparedStatement = connection.prepareStatement(sqlStmntInsert);

      for (long rowNo = 1; rowNo <= rowMaxSize; rowNo++) {

        createColumnContent(preparedStatement,
                            rowNo);

        int count = preparedStatement.executeUpdate();

        if (count != 1) {
          MessageHandling.abortProgram(logger,
                                       "Program abort: insert row # " + String.format(FORMAT_ROW_NO,
                                                                                      rowNo) + " result " + count);
        }

        if (rowNo % BATCH_SIZE == 0) {
          logger.info(String.format(String.format(FORMAT_ROW_NO + " rows inserted so far - milliseconds: ",
                                                  rowNo) + FORMAT_ROW_SEC,
                                    Duration.between(startDateTimeBatch,
                                                     LocalDateTime.now()).toMillis()));
          startDateTimeBatch = LocalDateTime.now();
        }
      }

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    long duration = Duration.between(startDateTime,
                                     LocalDateTime.now()).toMillis();

    logger.info("");
    logger.info(String.format(FORMAT_ROW_NO,
                              rowMaxSize) + " rows inserted totally - duration in ms: " + String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                                                                                        duration));

    logger.info("");

    if (isDebug) {
      logger.debug("End");
    }
  }

  private static void insertDataBatch() {
    if (isDebug) {
      logger.debug("Start");
    }

    PreparedStatement preparedStatement;

    LocalDateTime     startDateTime      = LocalDateTime.now();
    LocalDateTime     startDateTimeBatch = LocalDateTime.now();

    try {
      preparedStatement = connection.prepareStatement(sqlStmntInsert);

      boolean isToBeExecuted = false;

      for (long rowNo = 1; rowNo <= rowMaxSize; rowNo++) {

        createColumnContent(preparedStatement,
                            rowNo);

        preparedStatement.addBatch();

        if (rowNo % BATCH_SIZE == 0) {
          preparedStatement.executeBatch();
          preparedStatement.clearBatch();
          isToBeExecuted = false;

          logger.info(String.format(String.format(FORMAT_ROW_NO + " rows inserted so far - milliseconds: ",
                                                  rowNo) + FORMAT_ROW_SEC,
                                    Duration.between(startDateTimeBatch,
                                                     LocalDateTime.now()).toMillis()));
          startDateTimeBatch = LocalDateTime.now();
        } else {
          isToBeExecuted = true;
        }
      }

      if (isToBeExecuted) {
        preparedStatement.executeBatch();
        preparedStatement.clearBatch();
      }

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    long duration = Duration.between(startDateTime,
                                     LocalDateTime.now()).toMillis();

    logger.info("");
    logger.info(String.format(FORMAT_ROW_NO,
                              rowMaxSize) + " rows inserted totally - duration in ms: " + String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                                                                                        duration));

    logger.info("");

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    if (isDebug) {
      logger.debug("Start");
    }

    logger.info("Start SampleMySQL");

    int     choice  = 0;

    Scanner scanner = new Scanner(System.in);

    while (choice < 8) {
      logger.info("");
      logger.info("----------------------------------------------");
      logger.info("1 - Run the demo with the original JDBC driver");
      logger.info("2 - Run the demo with the original JDBC driver - with batch operations");
      logger.info("3 - Run the demo with the trino JDBC driver");
      logger.info("4 - Run the demo with the trino JDBC driver - with batch operations");
      logger.info("8 - Run all demos");
      logger.info("9 - Terminate the processing");
      logger.info("----------------------------------------------");
      logger.info("");
      logger.info("Please make a choice:");

      choice = scanner.nextInt();

      switch (choice) {
      case 1:
        demonstrateOriginal();
        break;
      case 2:
        demonstrateOriginalBatch();
        break;
      case 3:
        demonstrateTrino();
        break;
      case 4:
        demonstrateTrinoBatch();
        break;
      case 8:
        demonstrateOriginal();
        demonstrateOriginalBatch();
        demonstrateTrino();
        demonstrateTrinoBatch();
        break;
      case 9:
        break;
      default:
        logger.info("Invalid choice !!!");
      }
    }

    scanner.close();

    logger.info("End   SampleMysql");

    if (isDebug) {
      logger.debug("End");
    }
  }

  private static byte[] readBlobFile2Bytes() {
    File            file               = new File(BLOB_FILE);

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

    return blobDataBytesArray;
  }

  private static String readClobFile() {
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

  private static void resetDatabase() {
    if (isDebug) {
      logger.debug("Start");
    }

    logger.info("Reset the database");
    logger.info("");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSys,
                         driverOriginal,
                         userNameSys,
                         password,
                         false);

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP DATABASE IF EXISTS `" + databaseName + "`",
                       "DROP USER IF EXISTS `" + userName + "`");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    if (isDebug) {
      logger.debug("End");
    }
  }

  private static void setupDatabase() {
    if (isDebug) {
      logger.debug("Start");
    }

    logger.info("Setup the database");
    logger.info("");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSys,
                         driverOriginal,
                         userNameSys,
                         password,
                         false);

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("CREATE DATABASE `" + databaseName + "`",
                       "USE `" + databaseName + "`",
                       "CREATE USER `" + userName + "` IDENTIFIED BY '" + password + "'",
                       "GRANT ALL ON " + databaseName + ".* TO `" + userName + "`");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlUser,
                         driverOriginal,
                         userName,
                         password,
                         false);

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts(sqlStmntCreateTable);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    if (isDebug) {
      logger.debug("End");
    }
  }
}
