package ch.konnexions.db_seeder.samples.presto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * Example program for Issues Oracle Connector.
 * 
 * @author  walter@konnexions.ch
 * @since   2020-10-22
 */
public final class I5648 {

  private static Connection connection;
  private static String     connectionHost      = "localhost";
  private static int        connectionPort      = 1521;

  private static String     driverOriginal      = "oracle.jdbc.driver.OracleDriver";
  private static String     driverPresto        = "io.prestosql.jdbc.PrestoDriver";

  private static Logger     logger              = Logger.getLogger(I5648.class);

  private static String     password            = "oracle";

  private static String     service             = "orclpdb1";
  private static String     sqlStmntCreateTable = """
                                                  CREATE TABLE issue_table (
                                                      column_pk        NUMBER         NOT NULL
                                                  )
                                                  """;
  private static String     sqlStmntInsert      = "INSERT INTO issue_table (column_pk) VALUES (?)";
  private static Statement  statement;

  private static String     userName            = "kxn_user";
  private static String     userNameSys         = "SYS AS SYSDBA";

  private static String     urlSys              = "jdbc:oracle:thin:@//" + connectionHost + ":" + connectionPort + "/" + service;
  private static String     urlPresto           = "jdbc:presto://localhost:8080/db_seeder_oracle/" + userName + "?user=presto";
  private static String     urlUser             = "jdbc:oracle:thin:@//" + connectionHost + ":" + connectionPort + "/" + service;

  private static Connection connect(String url, String driver, String user, String password, boolean autoCommit) {
    logger.info("connecting ...");
    logger.info("driver    ='" + driver + "'");
    logger.info("url       ='" + url + "'");
    logger.info("user      ='" + user + "'");
    logger.info("password  ='" + password + "'");
    logger.info("autocommit='" + autoCommit + "'");

    try {
      Class.forName(driver);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    Connection connection = null;

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

    logger.info("==========> now connected");

    return connection;
  }

  private static void demonstratePresto() {
    logger.info("Run the demo with the Presto JDBC driver ======================");

    connection = connect(urlPresto,
                         driverPresto,
                         null,
                         null,
                         true);

    insertData();

    disconnect(connection);
  }

  private static void disconnect(Connection connection) {
    logger.info("disconnecting ...");

    try {
      if (!(connection.getAutoCommit())) {
        logger.info("commiting ...");
        connection.commit();
      }

      connection.close();

    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    logger.info("==========> now disconnected");
  }

  private static void dropUser(String userName) {
    try {
      int    count    = 0;

      String sqlStmnt = "SELECT COUNT(*) FROM ALL_USERS WHERE username = '" + userName + "'";

      logger.info("sqlStmnt='" + sqlStmnt + "'");

      ResultSet resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        executeDdlStmnts("DROP USER " + userName + " CASCADE");
      }
    } catch (SQLException e) {
      e.printStackTrace();
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
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sqlStmntInsert);

      preparedStatement.setLong(1,
                                1);

      if (preparedStatement.executeUpdate() != 1) {
        logger.fatal("Program abort: row not inserted");
      }

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

  }

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    logger.info("Start main() ==================================================");

    resetDatabase();
    setupDatabase();
    demonstratePresto();
    resetDatabase();

    logger.info("End   main() ==================================================");
  }

  private static void resetDatabase() {
    logger.info("Reset the database ============================================");

    connection = connect(urlSys,
                         driverOriginal,
                         userNameSys,
                         password,
                         false);

    try {
      statement = connection.createStatement();

      dropUser(userName.toUpperCase());

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    disconnect(connection);
  }

  private static void setupDatabase() {
    logger.info("Setup the database ============================================");

    connection = connect(urlSys,
                         driverOriginal,
                         userNameSys,
                         password,
                         false);

    try {
      statement = connection.createStatement();

      executeDdlStmnts("CREATE USER " + userName.toUpperCase() + " IDENTIFIED BY \"" + password + "\"",
                       "ALTER USER " + userName.toUpperCase() + " QUOTA UNLIMITED ON users",
                       "GRANT CREATE SEQUENCE TO " + userName.toUpperCase(),
                       "GRANT CREATE SESSION TO " + userName.toUpperCase(),
                       "GRANT CREATE TABLE TO " + userName.toUpperCase(),
                       "GRANT UNLIMITED TABLESPACE TO " + userName.toUpperCase());

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    disconnect(connection);

    connection = connect(urlUser,
                         driverOriginal,
                         userName.toUpperCase(),
                         password,
                         false);

    try {
      statement = connection.createStatement();

      executeDdlStmnts(sqlStmntCreateTable);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    disconnect(connection);
  }
}
