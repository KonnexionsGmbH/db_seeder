package ch.konnexions.db_seeder.samples.presto;

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
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.Scanner;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.AbstractDbmsSeeder;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Example program for Issues PostgreSQL Connector.
 * 
 * @author  walter@konnexions.ch
 * @since   2020-08-11
 */
public final class SamplePostgresql {

  private final static String  BLOB_FILE           = Paths.get("src",
                                                               "main",
                                                               "resources").toAbsolutePath().toString() + File.separator + "blob.png";
  private final static byte[]  BLOB_DATA_BYTES     = readBlobFile2Bytes();

  private final static String  CLOB_FILE           = Paths.get("src",
                                                               "main",
                                                               "resources").toAbsolutePath().toString() + File.separator + "clob.md";
  private final static String  CLOB_DATA           = readClobFile();
  private static Connection    connection;
  private final static String  connectionHost      = "localhost";
  private final static int     connectionPort      = 5432;

  private final static String  databaseName        = "kxn_db";
  private final static String  databaseNameSys     = "kxn_db_sys";
  private final static String  driverOriginal      = "org.postgresql.Driver";
  private final static String  driverPresto        = "io.prestosql.jdbc.PrestoDriver";

  public static final String   FORMAT_ROW_NO       = "%1$6d";

  private final static Logger  logger              = Logger.getLogger(SamplePostgresql.class);

  private final static int     nullFactor          = 4;

  private final static String  password            = "postgresql";

  private final static int     rowMaxSize          = 2500;

  private final static String  sqlStmntCreateTable = """
                                                     CREATE TABLE issue_table (
                                                         column_pk        BIGINT         NOT NULL
                                                                                         PRIMARY KEY,
                                                         column_blob      BYTEA,
                                                         column_clob      TEXT,
                                                         column_timestamp TIMESTAMP      NOT NULL,
                                                         column_varchar   VARCHAR(100)   NOT NULL
                                                                                         UNIQUE
                                                     )
                                                     """;
  private final static String  sqlStmntInsert      = "INSERT INTO issue_table (column_pk, column_blob, column_clob, column_timestamp, column_varchar) VALUES (?, ?, ?, ?, ?)";
  private static LocalDateTime startDateTime;
  private static Statement     statement;

  private final static String  userName            = "kxn_user";
  private final static String  userNameSys         = "kxn_user_sys";

  private final static String  urlSys              = "jdbc:postgresql://" + connectionHost + ":" + connectionPort + "/" + databaseNameSys + "?user="
      + userNameSys + "&password=" + password;
  private final static String  urlPresto           = "jdbc:presto://localhost:8080/db_seeder_postgresql/public?user=presto";
  private final static String  urlUser             = "jdbc:postgresql://" + connectionHost + ":" + connectionPort + "/" + databaseName + "?user=" + userName
      + "&password=" + password;

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
                                Types.NULL);
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

    startDateTime = LocalDateTime.now();

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
    logger.info("Start SamplePostgresql");

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

    logger.info("End   SamplePostgresql");
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

      executeDdlStmnts("DROP DATABASE IF EXISTS " + databaseName,
                       "DROP USER IF EXISTS " + userName);

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

      executeDdlStmnts("CREATE DATABASE " + databaseName,
                       "CREATE USER " + userName + " WITH ENCRYPTED PASSWORD '" + password + "'",
                       "GRANT ALL PRIVILEGES ON DATABASE " + databaseName + " TO " + userName);

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
