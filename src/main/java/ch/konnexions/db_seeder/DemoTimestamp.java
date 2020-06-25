package ch.konnexions.db_seeder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DemoTimestamp {

  private static Connection connect() {
    try {
      Class.forName("org.firebirdsql.jdbc.FBDriver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    Connection connection = null;

    try {
      connection = DriverManager.getConnection("jdbc:firebirdsql://localhost:3050/kxn_db?encoding=UTF8&useFirebirdAutocommit=true&useStreamBlobs=true");
      connection.setAutoCommit(true);
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }
    return connection;
  }

  private static void disconnect(Connection connection) {
    try {
      connection.close();
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }
  }

  @SuppressWarnings("preview")
  public static void main(String[] args) {

    Connection connection = connect();

    try {
      Statement statement = connection.createStatement();

      statement.execute("""
                        RECREATE TABLE CITY (
                            COL_ID        INTEGER      NOT NULL PRIMARY KEY,
                            COL_BLOB      BLOB,
                            COL_TIMESTAMP TIMESTAMP    NOT NULL
                         )""");

    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    disconnect(connection);
  }
}
