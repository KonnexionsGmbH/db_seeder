package ch.konnexions.db_seeder.samples.presto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * Demonstration program for Issue 4812.
 * 
 * https://github.com/prestosql/presto/issues/4812
 * 
 * @author  walter@konnexions.ch
 * @since   2020-08-11
 */
public class I4812 {
  private static Logger        logger  = Logger.getLogger(I4812.class);
  private static final boolean isDebug = logger.isDebugEnabled();

  public static void main(String[] args) throws Exception {
    if (isDebug) {
      logger.debug("Start");
    }

    Class.forName("com.mysql.cj.jdbc.Driver");
    String     url        = "jdbc:mysql://192.168.1.109:3306/?serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false&rewriteBatchedStatements=true";
    Connection connection = DriverManager.getConnection(url,
                                                        "root",
                                                        "mysql");
    Statement  statement  = connection.createStatement();

    statement.execute("DROP DATABASE IF EXISTS `kxn_db`");
    statement.execute("DROP USER IF EXISTS `kxn_user`");
    statement.execute("CREATE DATABASE `kxn_db`");
    statement.execute("CREATE USER `kxn_user` IDENTIFIED BY 'mysql'");
    statement.execute("GRANT ALL ON kxn_db.* TO `kxn_user`");
    statement.execute("CREATE TABLE kxn_db.test (c1 int)");

    statement.close();
    connection.close();

    Class.forName("io.prestosql.jdbc.PrestoDriver");
    url        = "jdbc:presto://localhost:8080/i4812";
    connection = DriverManager.getConnection(url,
                                             "test",
                                             null);
    statement  = connection.createStatement();

    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO kxn_db.test VALUES (?)");

    for (int i = 0; i < 2500; i++) {
      preparedStatement.setInt(1,
                               i);

      preparedStatement.executeUpdate();

      if (i % 50 == 0) {
        logger.info(String.format("%1$6d" + " row(s) inserted so far",
                                  i + 1));
      }
    }

    statement.close();
    connection.close();

    if (isDebug) {
      logger.debug("End");
    }
  }
}
