package ch.konnexions.db_seeder.samples.presto;

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
 * Example program for Issues SQL Server Connector.
 * 
 * @author  walter@konnexions.ch
 * @since   2020-08-11
 */
public final class SampleSqlserver {

  private static String      BLOB_FILE           = Paths.get("src",
                                                             "main",
                                                             "resources").toAbsolutePath().toString() + File.separator + "blob.png";
  private static byte[]      BLOB_DATA_BYTES     = readBlobFile2Bytes();

  private static String      CLOB_FILE           = Paths.get("src",
                                                             "main",
                                                             "resources").toAbsolutePath().toString() + File.separator + "clob.md";
  private static String      CLOB_DATA           = readClobFile();
  private static Connection  connection;
  private static String      connectionHost      = "localhost";
  private static int         connectionPort      = 1433;

  private static String      databaseName        = "kxn_db";
  private static String      databaseNameSys     = "master";
  private static String      driverOriginal      = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  private static String      driverPresto        = "io.prestosql.jdbc.PrestoDriver";

  public static final String FORMAT_ROW_NO       = "%1$6d";

  private static Logger      logger              = Logger.getLogger(SampleSqlserver.class);

  private static int         nullFactor          = 4;

  private static String      password            = "sqlserver_2019";

  private static int         rowMaxSize          = 500;

  private static String      schemaName          = "kxn_schema";
  private static String      sqlStmntCreateTable = """
                                                   CREATE TABLE issue_table (
                                                       column_pk        BIGINT         NOT NULL
                                                                                       PRIMARY KEY,
                                                       column_blob      VARBINARY(MAX),
                                                       column_clob      VARCHAR(MAX),
                                                       column_timestamp DATETIME2      NOT NULL,
                                                       column_varchar   VARCHAR(100)   NOT NULL
                                                                                       UNIQUE
                                                   )
                                                   """;
  private static String      sqlStmntInsert      = "INSERT INTO issue_table (column_pk, column_blob, column_clob, column_timestamp, column_varchar) VALUES (?, ?, ?, ?, ?)";
  private static Statement   statement;

  private static String      userName            = "kxn_user";
  private static String      userNameSys         = "sa";

  private static String      urlSys              = "jdbc:sqlserver://" + connectionHost + ":" + connectionPort + ";databaseName=" + databaseNameSys + ";user="
      + userNameSys + ";password=" + password;
  private static String      urlPresto           = "jdbc:presto://localhost:8080/db_seeder_sqlserver/kxn_schema?user=presto";
  private static String      urlUser             = "jdbc:sqlserver://" + connectionHost + ":" + connectionPort + ";databaseName=" + databaseName + ";user="
      + userName + ";password=" + password;

  private static Connection connect(String url, String driver, String user, String password, boolean autoCommit) {
    if (driver != null) {
      try {
        logger.info("driver  ='" + driver + "'");
        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    Connection connection = null;

    logger.info("url     ='" + url + "'");

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

    preparedStatement.setString(columnPos++,
                                "varchar column #" + rowNo);
  }

  private static void demonstrateOriginal() {
    logger.info("Run the demo with the original JDBC driver");
    logger.info("");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlUser,
                         driverOriginal,
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
  }

  private static void demonstratePresto() {
    logger.info("Run the demo with the Presto JDBC driver");
    logger.info("");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlPresto,
                         driverPresto,
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
  }

  private static void disconnect(Connection connection) {
    try {
      if (!(connection.getAutoCommit())) {
        connection.commit();
      }

      connection.close();

    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }
  }

  private static void executeDdlStmnts(String firstDdlStmnt, String... remainingDdlStmnts) {
    try {
      logger.info("sqlStmnt='" + firstDdlStmnt + "'");
      statement.execute(firstDdlStmnt);

      for (String sqlStmnt : remainingDdlStmnts) {
        logger.info("sqlStmnt='" + sqlStmnt + "'");
        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void insertData() {
    PreparedStatement preparedStatement = null;

    logger.info("");

    LocalDateTime startDateTime = LocalDateTime.now();

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

        if (rowNo % 50 == 0) {
          logger.info(String.format(FORMAT_ROW_NO + " rows inserted so far",
                                    rowNo));
        }
      }

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    long duration = Duration.between(startDateTime,
                                     LocalDateTime.now()).toSeconds();

    logger.info("");
    logger.info(String.format(FORMAT_ROW_NO,
                              rowMaxSize) + " rows inserted totally - duration in seconds: " + String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                                                                                             duration));
  }

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    logger.info("Start SampleSqlserver");

    int     choice  = 0;

    Scanner scanner = new Scanner(System.in);

    while (choice != 9) {
      logger.info("");
      logger.info("----------------------------------------------");
      logger.info("1 - Setup the database (includes reset)");
      logger.info("2 - Run the demo with the original JDBC driver");
      logger.info("3 - Run the demo with the Presto JDBC driver");
      logger.info("4 - Reset the database");
      logger.info("9 - Terminate the processing");
      logger.info("----------------------------------------------");
      logger.info("");
      logger.info("Please make a choice:");

      choice = scanner.nextInt();

      switch (choice) {
      case 1:
        resetDatabase();
        logger.info("");
        setupDatabase();
        break;
      case 2:
        demonstrateOriginal();
        break;
      case 3:
        demonstratePresto();
        break;
      case 4:
        resetDatabase();
        break;
      case 9:
        break;
      default:
        logger.info("Invalid choice !!!");
      }
    }

    scanner.close();

    logger.info("End   SampleSqlserver");
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
    logger.info("Reset the database");
    logger.info("");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSys,
                         driverOriginal,
                         null,
                         null,
                         true);

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP DATABASE IF EXISTS " + databaseName);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);
  }

  private static void setupDatabase() {
    logger.info("Setup the database");
    logger.info("");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSys,
                         driverOriginal,
                         null,
                         null,
                         true);

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("sp_configure 'contained database authentication', 1",
                       "RECONFIGURE",
                       "USE master",
                       "CREATE DATABASE " + databaseName,
                       "USE master",
                       "ALTER DATABASE " + databaseName + " SET CONTAINMENT = PARTIAL",
                       "USE " + databaseName,
                       "CREATE SCHEMA " + schemaName,
                       "CREATE USER " + userName + " WITH PASSWORD = '" + password + "', DEFAULT_SCHEMA=" + schemaName,
                       "sp_addrolemember 'db_owner', '" + userName + "'");

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
                         null,
                         null,
                         true);

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
  }

}
