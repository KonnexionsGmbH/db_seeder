/**
 * 
 */
package ch.konnexions.db_seeder;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.apache.derby.DerbySeeder;
import ch.konnexions.db_seeder.jdbc.cratedb.CratedbSeeder;
import ch.konnexions.db_seeder.jdbc.cubrid.CubridSeeder;
import ch.konnexions.db_seeder.jdbc.ibmdb2.Ibmdb2Seeder;
import ch.konnexions.db_seeder.jdbc.mariadb.MariadbSeeder;
import ch.konnexions.db_seeder.jdbc.mssqlserver.MssqlserverSeeder;
import ch.konnexions.db_seeder.jdbc.mysql.MysqlSeeder;
import ch.konnexions.db_seeder.jdbc.oracle.OracleSeeder;
import ch.konnexions.db_seeder.jdbc.postgresql.PostgresqlSeeder;
import ch.konnexions.db_seeder.jdbc.sqlite.SqliteSeeder;

/**
 * <h1> Test Data Generator for a Database - Application. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class DatabaseSeeder {

  private final static Logger logger = Logger.getLogger(DatabaseSeeder.class);

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start");

    String args0 = null;
    if (args.length > 0) {
      args0 = args[0];
    }

    logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "args[0]='" + args0 + "'");

    if (null == args0) {
      logger.error(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "Command line argument missing (null)");
      System.exit(1);
    }

    switch (args0) {
    case "cratedb":
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  Start CrateDB");
      CratedbSeeder cratedbSeeder = new CratedbSeeder();
      cratedbSeeder.createData();
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  End   CrateDB");
      break;
    case "cubrid":
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  Start CUBRID");
      CubridSeeder cubridSeeder = new CubridSeeder();
      cubridSeeder.createData();
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  End   CUBRID");
      break;
    case "derby":
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  Start Apache Derby");
      DerbySeeder derbySeeder = new DerbySeeder();
      derbySeeder.createData();
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  End   Apache Derby");
      break;
    case "derby_emb":
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  Start Apache Derby");
      DerbySeeder derbySeederEmbedded = new DerbySeeder(false);
      derbySeederEmbedded.createData();
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  End   Apache Derby");
      break;
    case "ibmdb2":
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  Start IBM Db2 Database");
      Ibmdb2Seeder ibmdb2Seeder = new Ibmdb2Seeder();
      ibmdb2Seeder.createData();
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  End   IBM Db2 Database");
      break;
    case "mariadb":
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  Start MariaDB Server");
      MariadbSeeder mariadbSeeder = new MariadbSeeder();
      mariadbSeeder.createData();
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  End   MariaDB Server");
      break;
    case "mssqlserver":
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  Start Microsoft SQL Server");
      MssqlserverSeeder mssqlserverSeeder = new MssqlserverSeeder();
      mssqlserverSeeder.createData();
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  End   Microsoft SQL Server");
      break;
    case "mysql":
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  Start MySQL Database");
      MysqlSeeder mysqlSeeder = new MysqlSeeder();
      mysqlSeeder.createData();
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  End   MySQL Database");
      break;
    case "oracle":
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  Start Oracle Database");
      OracleSeeder oracleSeeder = new OracleSeeder();
      oracleSeeder.createData();
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  End   Oracle Database");
      break;
    case "postgresql":
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  Start PostgreSQL Database");
      PostgresqlSeeder postgresqlSeeder = new PostgresqlSeeder();
      postgresqlSeeder.createData();
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  End   PostgreSQL Database");
      break;
    case "sqlite":
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  Start SQLite");
      SqliteSeeder sqliteSeeder = new SqliteSeeder();
      sqliteSeeder.createData();
      logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "  End   SQLite");
      break;
    case "":
      logger.error(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "Command line argument missing");
      System.exit(1);
    default:
      logger.error(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "Unknown command line argument");
      System.exit(1);
    }

    logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");

    System.exit(0);
  }
}
