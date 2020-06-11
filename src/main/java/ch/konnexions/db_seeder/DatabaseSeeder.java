/**
 * 
 */
package ch.konnexions.db_seeder;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.mssqlserver.MssqlserverSeeder;
import ch.konnexions.db_seeder.jdbc.mysql.MysqlSeeder;
import ch.konnexions.db_seeder.jdbc.oracle.OracleSeeder;
import ch.konnexions.db_seeder.jdbc.postgresql.PostgresqlSeeder;

/**
 * <h1> Test Data Generator for a Database. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class DatabaseSeeder {

  public final static String FORMAT_IDENTIFIER  = "%04d";
  public final static String FORMAT_METHOD_NAME = "%-17s";
  public final static String FORMAT_ROW_NO      = "%1$5d";
  public final static String FORMAT_TABLE_NAME  = "%-17s";

  /**
   * @param args
   */
  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    String methodName = new Object() {
                      }.getClass().getEnclosingMethod().getName();

    Logger logger     = Logger.getLogger(DatabaseSeeder.class);

    logger.info(String.format(FORMAT_METHOD_NAME, methodName) + " - Start");

    String args0 = null;
    if (args.length > 0) {
      args0 = args[0];
    }

    logger.info("args[0]='" + args0 + "'");

    if (null == args0) {
      logger.error("Command line argument missing");
      System.exit(1);
    } else if (args0.equals("mssqlserver")) {
      logger.info("Start Microsoft SQL Server");
      MssqlserverSeeder mssqlserverSeeder = new MssqlserverSeeder();
      mssqlserverSeeder.createData();
      logger.info("End   Microsoft SQL Server");
    } else if (args0.equals("mysql")) {
      logger.info("Start MySQL Database");
      MysqlSeeder mysqlSeeder = new MysqlSeeder();
      mysqlSeeder.createData();
      logger.info("End   MySQL Database");
    } else if (args0.equals("oracle")) {
      logger.info("Start Oracle Database");
      OracleSeeder oracleSeeder = new OracleSeeder();
      oracleSeeder.createData();
      logger.info("End   Oracle Database");
    } else if (args0.equals("postgresql")) {
      logger.info("Start PostgreSQL Database");
      PostgresqlSeeder postgresqlSeeder = new PostgresqlSeeder();
      postgresqlSeeder.createData();
      logger.info("End   PostgreSQL Database");
    } else if (args0.contentEquals("")) {
      logger.error("Command line argument missing");
      System.exit(1);
    } else {
      logger.error("Unknown command line argument");
    }

    logger.info(String.format(FORMAT_METHOD_NAME, methodName) + " - End");

    System.exit(0);
  }

}
