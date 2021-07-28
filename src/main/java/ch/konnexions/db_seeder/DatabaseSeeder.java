package ch.konnexions.db_seeder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import ch.konnexions.db_seeder.jdbc.omnisci.OmnisciSeeder;
import ch.konnexions.db_seeder.jdbc.oracle.OracleSeeder;
import ch.konnexions.db_seeder.jdbc.percona.PerconaSeeder;
import ch.konnexions.db_seeder.jdbc.postgresql.PostgresqlSeeder;
import ch.konnexions.db_seeder.jdbc.sqlite.SqliteSeeder;
import ch.konnexions.db_seeder.jdbc.sqlserver.SqlserverSeeder;
import ch.konnexions.db_seeder.jdbc.timescale.TimescaleSeeder;
import ch.konnexions.db_seeder.jdbc.voltdb.VoltdbSeeder;
import ch.konnexions.db_seeder.jdbc.yugabyte.YugabyteSeeder;
import ch.konnexions.db_seeder.utils.Config;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Data Generator for a Database - Application.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class DatabaseSeeder {

  private static final String DBMS_OPTION_CLIENT   = "client";
  private static final String DBMS_OPTION_EMBEDDED = "embedded";
  private static final String DBMS_OPTION_TRINO    = "trino";

  private static final Logger logger               = LogManager.getLogger(DatabaseSeeder.class);

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

    Config config = new Config();

    for (String tickerSymbolExtern : tickerSymbolsExtern) {
      logger.info("tickerSymbolExtern='" + tickerSymbolExtern + "'");

      String tickerSymbol = tickerSymbolExtern.toLowerCase();

      if ("yes".equals(config.getDropConstraints())) {
        switch (tickerSymbol) {
        case "cockroach":
        case "cratedb":
        case "firebird":
        case "h2":
        case "h2_emb":
        case "hsqldb":
        case "hsqldb_emb":
        case "mysql_trino":
        case "omnisci":
        case "oracle_trino":
        case "postgresql_trino":
        case "sqlite":
        case "sqlserver_trino":
        case "voltdb":
        case "yugabyte":
          logger.info("==============================================================================================================================");
          logger.info("The run variant with parameter 'DB_SEEDER_DROP_CONSTRAINTS=yes' is not yet supported by the DBMS '" + tickerSymbol + "'!");
          logger.info("==============================================================================================================================");
          logger.info("End   DBMS Processing");
          System.exit(0);
        default:
        }
      }

      switch (Objects.requireNonNull(tickerSymbol)) {
      case "agens":
        logger.info("Start AgensGraph");
        AgensSeeder agensSeeder = new AgensSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        agensSeeder.createData();
        logger.info("End   AgensGraph");
        break;
      case "cockroach":
        logger.info("Start CockroachDB");
        CockroachSeeder cockroachSeeder = new CockroachSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        cockroachSeeder.createData();
        logger.info("End   CockroachDB");
        break;
      case "cratedb":
        logger.info("Start CrateDB");
        CratedbSeeder cratedbSeeder = new CratedbSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        cratedbSeeder.createData();
        logger.info("End   CrateDB");
        break;
      case "cubrid":
        logger.info("Start CUBRID");
        CubridSeeder cubridSeeder = new CubridSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        cubridSeeder.createData();
        logger.info("End   CUBRID");
        break;
      case "derby":
        logger.info("Start Apache Derby [client]");
        DerbySeeder derbySeeder = new DerbySeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        derbySeeder.createData();
        logger.info("End   Apache Derby [client]");
        break;
      case "derby_emb":
        logger.info("Start Apache Derby [embedded]");
        DerbySeeder derbySeederEmbedded = new DerbySeeder(tickerSymbol, DBMS_OPTION_EMBEDDED);
        derbySeederEmbedded.createData();
        logger.info("End   Apache Derby [embedded]");
        break;
      case "exasol":
        logger.info("Start Exasol [client]");
        ExasolSeeder exasolSeeder = new ExasolSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        exasolSeeder.createData();
        logger.info("End   Exasol [client]");
        break;
      case "firebird":
        logger.info("Start Firebird [client]");
        FirebirdSeeder firebirdSeeder = new FirebirdSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        firebirdSeeder.createData();
        logger.info("End   Firebird [client]");
        break;
      case "h2":
        logger.info("Start H2 Database Engine [client]");
        H2Seeder h2Seeder = new H2Seeder(tickerSymbol, DBMS_OPTION_CLIENT);
        h2Seeder.createData();
        logger.info("End   H2 Database Engine [client]");
        break;
      case "h2_emb":
        logger.info("Start H2 Database Engine [embedded]");
        H2Seeder h2SeederEmbedded = new H2Seeder(tickerSymbol, DBMS_OPTION_EMBEDDED);
        h2SeederEmbedded.createData();
        logger.info("End   H2 Database Engine [embedded]");
        break;
      case "hsqldb":
        logger.info("Start HSQLDB [client]");
        HsqldbSeeder hsqldbSeeder = new HsqldbSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        hsqldbSeeder.createData();
        logger.info("End   HSQLDB [client]");
        break;
      case "hsqldb_emb":
        logger.info("Start HSQLDB [embedded]");
        HsqldbSeeder hsqldbSeederEmbedded = new HsqldbSeeder(tickerSymbol, DBMS_OPTION_EMBEDDED);
        hsqldbSeederEmbedded.createData();
        logger.info("End   HSQLDB [embedded]");
        break;
      case "ibmdb2":
        logger.info("Start IBM Db2 Database");
        Ibmdb2Seeder ibmdb2Seeder = new Ibmdb2Seeder(tickerSymbol, DBMS_OPTION_CLIENT);
        ibmdb2Seeder.createData();
        logger.info("End   IBM Db2 Database");
        break;
      case "informix":
        logger.info("Start IBM Informix");
        InformixSeeder informixSeeder = new InformixSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        informixSeeder.createData();
        logger.info("End   IBM Informix");
        break;
      case "mariadb":
        logger.info("Start MariaDB Server");
        MariadbSeeder mariadbSeeder = new MariadbSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        mariadbSeeder.createData();
        logger.info("End   MariaDB Server");
        break;
      case "mimer":
        logger.info("Start Mimer SQL");
        MimerSeeder mimerSeeder = new MimerSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        mimerSeeder.createData();
        logger.info("End   Mimer SQL");
        break;
      case "monetdb":
        logger.info("Start MonetDB");
        MonetdbSeeder monetdbSeeder = new MonetdbSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        monetdbSeeder.createData();
        logger.info("End   MonetDB");
        break;
      case "mysql":
        logger.info("Start MySQL Database");
        MysqlSeeder mysqlSeeder = new MysqlSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        mysqlSeeder.createData();
        logger.info("End   MySQL Database");
        break;
      case "mysql_trino":
        logger.info("Start MySQL Database via trino");
        MysqlSeeder mysqlSeederTrino = new MysqlSeeder(tickerSymbol, DBMS_OPTION_TRINO);
        mysqlSeederTrino.createData();
        logger.info("End   MySQL Database via trino");
        break;
      case "omnisci":
        logger.info("Start OmniSciDB");
        OmnisciSeeder omnisciSeeder = new OmnisciSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        omnisciSeeder.createData();
        logger.info("End   OmniSciDB");
        break;
      case "oracle":
        logger.info("Start Oracle Database");
        OracleSeeder oracleSeeder = new OracleSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        oracleSeeder.createData();
        logger.info("End   Oracle Database");
        break;
      case "oracle_trino":
        logger.info("Start Oracle Database via trino");
        OracleSeeder oracleSeederTrino = new OracleSeeder(tickerSymbol, DBMS_OPTION_TRINO);
        oracleSeederTrino.createData();
        logger.info("End   Oracle Database via trino");
        break;
      case "percona":
        logger.info("Start Percona Server for MySQL");
        PerconaSeeder perconaSeeder = new PerconaSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        perconaSeeder.createData();
        logger.info("End   Percona Server for MySQL");
        break;
      case "postgresql":
        logger.info("Start PostgreSQL");
        PostgresqlSeeder postgresqlSeeder = new PostgresqlSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        postgresqlSeeder.createData();
        logger.info("End   PostgreSQL");
        break;
      case "postgresql_trino":
        logger.info("Start PostgreSQL via trino");
        PostgresqlSeeder postgresqlSeederTrino = new PostgresqlSeeder(tickerSymbol, DBMS_OPTION_TRINO);
        postgresqlSeederTrino.createData();
        logger.info("End   PostgreSQL via trino");
        break;
      case "sqlite":
        logger.info("Start SQLite");
        SqliteSeeder sqliteSeeder = new SqliteSeeder(tickerSymbol, DBMS_OPTION_EMBEDDED);
        sqliteSeeder.createData();
        logger.info("End   SQLite");
        break;
      case "sqlserver":
        logger.info("Start SQL Server");
        SqlserverSeeder sqlserverSeeder = new SqlserverSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        sqlserverSeeder.createData();
        logger.info("End   SQL Server");
        break;
      case "sqlserver_trino":
        logger.info("Start SQL Server via trino");
        SqlserverSeeder sqlserverSeederTrino = new SqlserverSeeder(tickerSymbol, DBMS_OPTION_TRINO);
        sqlserverSeederTrino.createData();
        logger.info("End   SQL Server via trino");
        break;
      case "timescale":
        logger.info("Start TimescaleDB");
        TimescaleSeeder timescaleSeeder = new TimescaleSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        timescaleSeeder.createData();
        logger.info("End   TimescaleDB");
        break;
      case "voltdb":
        logger.info("Start VoltDB");
        VoltdbSeeder voltdbSeeder = new VoltdbSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
        voltdbSeeder.createData();
        logger.info("End   VoltDB");
        break;
      case "yugabyte":
        logger.info("Start YugabyteDB");
        YugabyteSeeder yugabyteSeeder = new YugabyteSeeder(tickerSymbol, DBMS_OPTION_CLIENT);
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

  final boolean isDebug = logger.isDebugEnabled();
}
