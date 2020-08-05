package ch.konnexions.db_seeder;

import java.util.Objects;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.cratedb.CratedbSeeder;
import ch.konnexions.db_seeder.jdbc.cubrid.CubridSeeder;
import ch.konnexions.db_seeder.jdbc.derby.DerbySeeder;
import ch.konnexions.db_seeder.jdbc.firebird.FirebirdSeeder;
import ch.konnexions.db_seeder.jdbc.h2.H2Seeder;
import ch.konnexions.db_seeder.jdbc.hsqldb.HsqldbSeeder;
import ch.konnexions.db_seeder.jdbc.ibmdb2.Ibmdb2Seeder;
import ch.konnexions.db_seeder.jdbc.informix.InformixSeeder;
import ch.konnexions.db_seeder.jdbc.mariadb.MariadbSeeder;
import ch.konnexions.db_seeder.jdbc.mimer.MimerSeeder;
import ch.konnexions.db_seeder.jdbc.mssqlserver.MssqlserverSeeder;
import ch.konnexions.db_seeder.jdbc.mysql.MysqlSeeder;
import ch.konnexions.db_seeder.jdbc.oracle.OracleSeeder;
import ch.konnexions.db_seeder.jdbc.postgresql.PostgresqlSeeder;
import ch.konnexions.db_seeder.jdbc.sqlite.SqliteSeeder;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Test Data Generator for a Database - Application.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
@SuppressWarnings("ucd")
public final class DatabaseSeeder {

  private static final Logger logger = Logger.getLogger(DatabaseSeeder.class);

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    logger.info("Start");

    String dbmsTickerSymbol = null;

    if (args.length > 0) {
      dbmsTickerSymbol = args[0];
    }

    logger.info("args[0]='" + dbmsTickerSymbol + "'");

    if (null == dbmsTickerSymbol) {
      MessageHandling.abortProgram(logger,
                                   "Command line argument missing (null)");
    }

    switch (Objects.requireNonNull(dbmsTickerSymbol)) {
    case "cratedb":
      logger.info("Start CrateDB");
      CratedbSeeder cratedbSeeder = new CratedbSeeder(dbmsTickerSymbol);
      cratedbSeeder.createData();
      logger.info("End   CrateDB");
      break;
    case "cubrid":
      logger.info("Start CUBRID");
      CubridSeeder cubridSeeder = new CubridSeeder(dbmsTickerSymbol);
      cubridSeeder.createData();
      logger.info("End   CUBRID");
      break;
    case "derby":
      logger.info("Start Apache Derby [client]");
      DerbySeeder derbySeeder = new DerbySeeder(dbmsTickerSymbol);
      derbySeeder.createData();
      logger.info("End   Apache Derby [client]");
      break;
    case "derby_emb":
      logger.info("Start Apache Derby [embedded]");
      DerbySeeder derbySeederEmbedded = new DerbySeeder(dbmsTickerSymbol, "embedded");
      derbySeederEmbedded.createData();
      logger.info("End   Apache Derby [embedded]");
      break;
    case "firebird":
      logger.info("Start Firebird [client]");
      FirebirdSeeder firebirdSeeder = new FirebirdSeeder(dbmsTickerSymbol);
      firebirdSeeder.createData();
      logger.info("End   Firebird [client]");
      break;
    case "h2":
      logger.info("Start H2 Database Engine [client]");
      H2Seeder h2Seeder = new H2Seeder(dbmsTickerSymbol);
      h2Seeder.createData();
      logger.info("End   H2 Database Engine [client]");
      break;
    case "h2_emb":
      logger.info("Start H2 Database Engine [embedded]");
      H2Seeder h2SeederEmbedded = new H2Seeder(dbmsTickerSymbol, "embedded");
      h2SeederEmbedded.createData();
      logger.info("End   H2 Database Engine [embedded]");
      break;
    case "hsqldb":
      logger.info("Start HyperSQL Database [client]");
      HsqldbSeeder hsqldbSeeder = new HsqldbSeeder(dbmsTickerSymbol);
      hsqldbSeeder.createData();
      logger.info("End   HyperSQL Database [client]");
      break;
    case "hsqldb_emb":
      logger.info("Start HyperSQL Database [embedded]");
      HsqldbSeeder hsqldbSeederEmbedded = new HsqldbSeeder(dbmsTickerSymbol, "embedded");
      hsqldbSeederEmbedded.createData();
      logger.info("End   HyperSQL Database [embedded]");
      break;
    case "ibmdb2":
      logger.info("Start IBM Db2 Database");
      Ibmdb2Seeder ibmdb2Seeder = new Ibmdb2Seeder(dbmsTickerSymbol);
      ibmdb2Seeder.createData();
      logger.info("End   IBM Db2 Database");
      break;
    case "informix":
      logger.info("Start IBM Informix");
      InformixSeeder informixSeeder = new InformixSeeder(dbmsTickerSymbol);
      informixSeeder.createData();
      logger.info("End   IBM Informix");
      break;
    case "mariadb":
      logger.info("Start MariaDB Server");
      MariadbSeeder mariadbSeeder = new MariadbSeeder(dbmsTickerSymbol);
      mariadbSeeder.createData();
      logger.info("End   MariaDB Server");
      break;
    case "mimer":
      logger.info("Start Mimer SQL");
      MimerSeeder mimerSeeder = new MimerSeeder(dbmsTickerSymbol);
      mimerSeeder.createData();
      logger.info("End   Mimer SQL");
      break;
    case "mssqlserver":
      logger.info("Start Microsoft SQL Server");
      MssqlserverSeeder mssqlserverSeeder = new MssqlserverSeeder(dbmsTickerSymbol);
      mssqlserverSeeder.createData();
      logger.info("End   Microsoft SQL Server");
      break;
    case "mysql":
      logger.info("Start MySQL Database");
      MysqlSeeder mysqlSeeder = new MysqlSeeder(dbmsTickerSymbol);
      mysqlSeeder.createData();
      logger.info("End   MySQL Database");
      break;
    case "mysql_presto":
      logger.info("Start MySQL Database");
      MysqlSeeder mysqlSeederPresto = new MysqlSeeder(dbmsTickerSymbol,"presto");
      mysqlSeederPresto.createData();
      logger.info("End   MySQL Database");
      break;
    case "oracle":
      logger.info("Start Oracle Database");
      OracleSeeder oracleSeeder = new OracleSeeder(dbmsTickerSymbol);
      oracleSeeder.createData();
      logger.info("End   Oracle Database");
      break;
    case "postgresql":
      logger.info("Start PostgreSQL Database");
      PostgresqlSeeder postgresqlSeeder = new PostgresqlSeeder(dbmsTickerSymbol);
      postgresqlSeeder.createData();
      logger.info("End   PostgreSQL Database");
      break;
    case "sqlite":
      logger.info("Start SQLite");
      SqliteSeeder sqliteSeeder = new SqliteSeeder(dbmsTickerSymbol);
      sqliteSeeder.createData();
      logger.info("End   SQLite");
      break;
    case "":
      MessageHandling.abortProgram(logger,
                                   "Command line argument missing");
    default:
      MessageHandling.abortProgram(logger,
                                   "Unknown command line argument");
    }

    logger.info("End");

    System.exit(0);
  }
}
