package ch.konnexions.db_seeder;

import ch.konnexions.db_seeder.jdbc.agens.AgensSeeder;
import ch.konnexions.db_seeder.jdbc.cockroach.CockroachSeeder;
import ch.konnexions.db_seeder.jdbc.cratedb.CratedbSeeder;
import ch.konnexions.db_seeder.jdbc.cubrid.CubridSeeder;
import ch.konnexions.db_seeder.jdbc.derby.DerbySeeder;
import ch.konnexions.db_seeder.jdbc.exasol.ExasolSeeder;
import ch.konnexions.db_seeder.jdbc.firebird.FirebirdSeeder;
import ch.konnexions.db_seeder.jdbc.h2.H2Seeder;
import ch.konnexions.db_seeder.jdbc.hsqldb.HsqldbSeeder;
import ch.konnexions.db_seeder.jdbc.ibmdb2.Ibmdb2Seeder;
import ch.konnexions.db_seeder.jdbc.informix.InformixSeeder;
import ch.konnexions.db_seeder.jdbc.mariadb.MariadbSeeder;
import ch.konnexions.db_seeder.jdbc.mimer.MimerSeeder;
import ch.konnexions.db_seeder.jdbc.monetdb.MonetdbSeeder;
import ch.konnexions.db_seeder.jdbc.mysql.MysqlSeeder;
import ch.konnexions.db_seeder.jdbc.oracle.OracleSeeder;
import ch.konnexions.db_seeder.jdbc.percona.PerconaSeeder;
import ch.konnexions.db_seeder.jdbc.postgresql.PostgresqlSeeder;
import ch.konnexions.db_seeder.jdbc.sqlite.SqliteSeeder;
import ch.konnexions.db_seeder.jdbc.sqlserver.SqlserverSeeder;
import ch.konnexions.db_seeder.jdbc.voltdb.VoltdbSeeder;
import ch.konnexions.db_seeder.jdbc.yugabyte.YugabyteSeeder;
import ch.konnexions.db_seeder.utils.MessageHandling;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    if (args.length == 0) {
      MessageHandling.abortProgram(logger,
                                   "Program abort: no command line arguments found");
    }

    List<String> tickerSymbolsExtern = Arrays.asList(args);

    Collections.sort(tickerSymbolsExtern);

    for (String tickerSymbolExtern : tickerSymbolsExtern) {
      logger.info("tickerSymbolExtern='" + tickerSymbolExtern + "'");

      switch (Objects.requireNonNull(tickerSymbolExtern)) {
      case "agens":
        logger.info("Start AgensGraph");
        AgensSeeder agensSeeder = new AgensSeeder(tickerSymbolExtern);
        agensSeeder.createData();
        logger.info("End   AgensGraph");
        break;
      case "cockroach":
        logger.info("Start CockroachDB");
        CockroachSeeder cockroachSeeder = new CockroachSeeder(tickerSymbolExtern);
        cockroachSeeder.createData();
        logger.info("End   CockroachDB");
        break;
      case "cratedb":
        logger.info("Start CrateDB");
        CratedbSeeder cratedbSeeder = new CratedbSeeder(tickerSymbolExtern);
        cratedbSeeder.createData();
        logger.info("End   CrateDB");
        break;
      case "cubrid":
        logger.info("Start CUBRID");
        CubridSeeder cubridSeeder = new CubridSeeder(tickerSymbolExtern);
        cubridSeeder.createData();
        logger.info("End   CUBRID");
        break;
      case "derby":
        logger.info("Start Apache Derby [client]");
        DerbySeeder derbySeeder = new DerbySeeder(tickerSymbolExtern);
        derbySeeder.createData();
        logger.info("End   Apache Derby [client]");
        break;
      case "derby_emb":
        logger.info("Start Apache Derby [embedded]");
        DerbySeeder derbySeederEmbedded = new DerbySeeder(tickerSymbolExtern, "embedded");
        derbySeederEmbedded.createData();
        logger.info("End   Apache Derby [embedded]");
        break;
      case "exasol":
        logger.info("Start Exasol [client]");
        ExasolSeeder exasolSeeder = new ExasolSeeder(tickerSymbolExtern);
        exasolSeeder.createData();
        logger.info("End   Exasol [client]");
        break;
      case "firebird":
        logger.info("Start Firebird [client]");
        FirebirdSeeder firebirdSeeder = new FirebirdSeeder(tickerSymbolExtern);
        firebirdSeeder.createData();
        logger.info("End   Firebird [client]");
        break;
      case "h2":
        logger.info("Start H2 Database Engine [client]");
        H2Seeder h2Seeder = new H2Seeder(tickerSymbolExtern);
        h2Seeder.createData();
        logger.info("End   H2 Database Engine [client]");
        break;
      case "h2_emb":
        logger.info("Start H2 Database Engine [embedded]");
        H2Seeder h2SeederEmbedded = new H2Seeder(tickerSymbolExtern, "embedded");
        h2SeederEmbedded.createData();
        logger.info("End   H2 Database Engine [embedded]");
        break;
      case "hsqldb":
        logger.info("Start HyperSQL Database [client]");
        HsqldbSeeder hsqldbSeeder = new HsqldbSeeder(tickerSymbolExtern);
        hsqldbSeeder.createData();
        logger.info("End   HyperSQL Database [client]");
        break;
      case "hsqldb_emb":
        logger.info("Start HyperSQL Database [embedded]");
        HsqldbSeeder hsqldbSeederEmbedded = new HsqldbSeeder(tickerSymbolExtern, "embedded");
        hsqldbSeederEmbedded.createData();
        logger.info("End   HyperSQL Database [embedded]");
        break;
      case "ibmdb2":
        logger.info("Start IBM Db2 Database");
        Ibmdb2Seeder ibmdb2Seeder = new Ibmdb2Seeder(tickerSymbolExtern);
        ibmdb2Seeder.createData();
        logger.info("End   IBM Db2 Database");
        break;
      case "informix":
        logger.info("Start IBM Informix");
        InformixSeeder informixSeeder = new InformixSeeder(tickerSymbolExtern);
        informixSeeder.createData();
        logger.info("End   IBM Informix");
        break;
      case "mariadb":
        logger.info("Start MariaDB Server");
        MariadbSeeder mariadbSeeder = new MariadbSeeder(tickerSymbolExtern);
        mariadbSeeder.createData();
        logger.info("End   MariaDB Server");
        break;
      case "mimer":
        logger.info("Start Mimer SQL");
        MimerSeeder mimerSeeder = new MimerSeeder(tickerSymbolExtern);
        mimerSeeder.createData();
        logger.info("End   Mimer SQL");
        break;
      case "monetdb":
        logger.info("Start MonetDB");
        MonetdbSeeder monetdbSeeder = new MonetdbSeeder(tickerSymbolExtern);
        monetdbSeeder.createData();
        logger.info("End   MonetDB");
        break;
      case "mysql":
        logger.info("Start MySQL Database");
        MysqlSeeder mysqlSeeder = new MysqlSeeder(tickerSymbolExtern);
        mysqlSeeder.createData();
        logger.info("End   MySQL Database");
        break;
      case "mysql_trino":
        logger.info("Start MySQL Database via Trino");
        MysqlSeeder mysqlSeederTrino = new MysqlSeeder(tickerSymbolExtern, "trino");
        mysqlSeederTrino.createData();
        logger.info("End   MySQL Database via Trino");
        break;
      case "oracle":
        logger.info("Start Oracle Database");
        OracleSeeder oracleSeeder = new OracleSeeder(tickerSymbolExtern);
        oracleSeeder.createData();
        logger.info("End   Oracle Database");
        break;
      case "oracle_trino":
        logger.info("Start Oracle Database via Trino");
        OracleSeeder oracleSeederTrino = new OracleSeeder(tickerSymbolExtern, "trino");
        oracleSeederTrino.createData();
        logger.info("End   Oracle Database via Trino");
        break;
      case "percona":
        logger.info("Start Percona Server for MySQL");
        PerconaSeeder perconaSeeder = new PerconaSeeder(tickerSymbolExtern);
        perconaSeeder.createData();
        logger.info("End   Percona Server for MySQL");
        break;
      case "postgresql":
        logger.info("Start PostgreSQL Database");
        PostgresqlSeeder postgresqlSeeder = new PostgresqlSeeder(tickerSymbolExtern);
        postgresqlSeeder.createData();
        logger.info("End   PostgreSQL Database");
        break;
      case "postgresql_trino":
        logger.info("Start PostgreSQL Database via Trino");
        PostgresqlSeeder postgresqlSeederTrino = new PostgresqlSeeder(tickerSymbolExtern, "trino");
        postgresqlSeederTrino.createData();
        logger.info("End   PostgreSQL Database via Trino");
        break;
      case "sqlite":
        logger.info("Start SQLite");
        SqliteSeeder sqliteSeeder = new SqliteSeeder(tickerSymbolExtern, "embedded");
        sqliteSeeder.createData();
        logger.info("End   SQLite");
        break;
      case "sqlserver":
        logger.info("Start Microsoft SQL Server");
        SqlserverSeeder sqlserverSeeder = new SqlserverSeeder(tickerSymbolExtern);
        sqlserverSeeder.createData();
        logger.info("End   Microsoft SQL Server");
        break;
      case "sqlserver_trino":
        logger.info("Start Microsoft SQL Server via Trino");
        SqlserverSeeder sqlserverSeederTrino = new SqlserverSeeder(tickerSymbolExtern, "trino");
        sqlserverSeederTrino.createData();
        logger.info("End   Microsoft SQL Server via Trino");
        break;
      case "voltdb":
        logger.info("Start VoltDB");
        VoltdbSeeder voltdbSeeder = new VoltdbSeeder(tickerSymbolExtern);
        voltdbSeeder.createData();
        logger.info("End   VoltDB");
        break;
      case "yugabyte":
        logger.info("Start YugabyteDB");
        YugabyteSeeder yugabyteSeeder = new YugabyteSeeder(tickerSymbolExtern);
        yugabyteSeeder.createData();
        logger.info("End   YugabyteDB");
        break;
      case "":
        MessageHandling.abortProgram(logger,
                                     "Program abort: command line argument missing");
      default:
        MessageHandling.abortProgram(logger,
                                     "Program abort: unknown command line argument");
      }
    }

    logger.info("End");

    System.exit(0);
  }

  protected final boolean isDebug = logger.isDebugEnabled();
}
