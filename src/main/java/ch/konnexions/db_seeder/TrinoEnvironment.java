package ch.konnexions.db_seeder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;
import ch.konnexions.db_seeder.jdbc.mysql.MysqlSeeder;
import ch.konnexions.db_seeder.jdbc.oracle.OracleSeeder;
import ch.konnexions.db_seeder.jdbc.postgresql.PostgresqlSeeder;
import ch.konnexions.db_seeder.jdbc.sqlserver.SqlserverSeeder;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Data Generator for a Database - Application.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-07-15
 */
public final class TrinoEnvironment {

  private static String                    connectionHost;
  private static int                       connectionPort;
  private static String                    connectionPrefix;
  private static String                    connectionService;
  private static String                    connectionSuffix;

  private static String                    database;
  private static String                    directoryCatalogProperty;

  private static final ArrayList<String>   entries       = new ArrayList<>();

  private static final Logger              logger        = LogManager.getLogger(TrinoEnvironment.class);
  private static final boolean             isDebug       = logger.isDebugEnabled();

  private static final Map<String, String> osEnvironment = System.getenv();

  private static String                    password;

  private static String                    url;
  private static String                    user;

  /**
   * Create the catalog data and the catalog file.
   *
   * @param tickerSymbol the lower case DBMS ticker symbol
   */
  private static void createCatalog(String tickerSymbol) {
    if (isDebug) {
      logger.debug("Start tickerSymbol='" + tickerSymbol + "'");
    }

    entries.clear();

    entries.add("connector.name=" + tickerSymbol);
    entries.add("case-insensitive-name-matching=true");
    entries.add("connection-url=" + url.replace("\"",
                                                ""));
    if (!("postgresql".equals(tickerSymbol) || "sqlserver".equals(tickerSymbol))) {
      entries.add("connection-user=" + user);
      entries.add("connection-password=" + password);
    }

    // issue #4764 wwe
    if ("oracle".equals(tickerSymbol)) {
      entries.add("oracle.number.default-scale=10");
    }

    createCatalogFile(tickerSymbol,
                      entries);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create a catalog file.
   *
   * @param tickerSymbol the lower case DBMS ticker symbol
   * @param entries the catalog entries
   */
  private static void createCatalogFile(String tickerSymbol, ArrayList<String> entries) {
    if (isDebug) {
      logger.debug("Start tickerSymbol='\"+tickerSymbol+\"'\"");
    }

    try {
      String fileName = directoryCatalogProperty + File.separator + AbstractJdbcSeeder.getCatalogName(tickerSymbol) + ".properties";

      if (isDebug) {
        logger.debug("fileName='" + fileName + "'");
      }

      BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, false)));

      for (String entry : entries) {
        bufferedWriter.append(entry);
        bufferedWriter.newLine();
      }

      bufferedWriter.close();

      logger.info("Catalog file created: '" + fileName + "'");
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create the MySQL catalog file.
   *
   * @param tickerSymbol the DBMS ticker symbol
   */
  private static void createCatalogFileMysql(String tickerSymbol) {
    if (isDebug) {
      logger.debug("Start tickerSymbol='" + tickerSymbol + "'");
    }

    // =========================================================================
    // Common variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_CONNECTION_HOST")) {
      connectionHost = osEnvironment.get("DB_SEEDER_MYSQL_CONNECTION_HOST");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_MYSQL_CONNECTION_HOST");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_CONNECTION_PORT")) {
      String connectionPortString = osEnvironment.get("DB_SEEDER_MYSQL_CONNECTION_PORT");
      try {
        connectionPort = Integer.parseInt(connectionPortString);
      } catch (NumberFormatException e) {
        MessageHandling.abortProgram(logger,
                                     "Program abort: parameter is not an integer: DB_SEEDER_MYSQL_CONNECTION_PORT");
      }
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_MYSQL_CONNECTION_PORT");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_CONNECTION_PREFIX")) {
      connectionPrefix = osEnvironment.get("DB_SEEDER_MYSQL_CONNECTION_PREFIX");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_MYSQL_CONNECTION_PREFIX");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_CONNECTION_SUFFIX")) {
      connectionSuffix = osEnvironment.get("DB_SEEDER_MYSQL_CONNECTION_SUFFIX");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_MYSQL_CONNECTION_SUFFIX");
    }

    // =========================================================================
    // Non-Privileged access variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_PASSWORD")) {
      password = osEnvironment.get("DB_SEEDER_MYSQL_PASSWORD");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_MYSQL_PASSWORD");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_USER")) {
      user = osEnvironment.get("DB_SEEDER_MYSQL_USER");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_MYSQL_USER");
    }

    // =========================================================================
    // Non-Privileged catalog creation.
    // -------------------------------------------------------------------------

    url = MysqlSeeder.getUrlTrino(connectionHost,
                                  connectionPort,
                                  connectionPrefix,
                                  connectionSuffix);

    createCatalog(tickerSymbol);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create the Oracle catalog file.
   *
   * @param tickerSymbol the DBMS ticker symbol
   */
  private static void createCatalogFileOracle(String tickerSymbol) {
    if (isDebug) {
      logger.debug("Start tickerSymbol='" + tickerSymbol + "'");
    }

    // =========================================================================
    // Common variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_ORACLE_CONNECTION_HOST")) {
      connectionHost = osEnvironment.get("DB_SEEDER_ORACLE_CONNECTION_HOST");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_ORACLE_CONNECTION_HOST");
    }

    if (osEnvironment.containsKey("DB_SEEDER_ORACLE_CONNECTION_PORT")) {
      String connectionPortString = osEnvironment.get("DB_SEEDER_ORACLE_CONNECTION_PORT");
      try {
        connectionPort = Integer.parseInt(connectionPortString);
      } catch (NumberFormatException e) {
        MessageHandling.abortProgram(logger,
                                     "Program abort: parameter is not an integer: DB_SEEDER_ORACLE_CONNECTION_PORT");
      }
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_ORACLE_CONNECTION_PORT");
    }

    if (osEnvironment.containsKey("DB_SEEDER_ORACLE_CONNECTION_PREFIX")) {
      connectionPrefix = osEnvironment.get("DB_SEEDER_ORACLE_CONNECTION_PREFIX");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_ORACLE_CONNECTION_PREFIX");
    }

    if (osEnvironment.containsKey("DB_SEEDER_ORACLE_CONNECTION_SERVICE")) {
      connectionService = osEnvironment.get("DB_SEEDER_ORACLE_CONNECTION_SERVICE");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_ORACLE_CONNECTION_SERVICE");
    }

    // =========================================================================
    // Non-Privileged access variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_ORACLE_PASSWORD")) {
      password = osEnvironment.get("DB_SEEDER_ORACLE_PASSWORD");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_ORACLE_PASSWORD");
    }

    if (osEnvironment.containsKey("DB_SEEDER_ORACLE_USER")) {
      user = osEnvironment.get("DB_SEEDER_ORACLE_USER");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_ORACLE_USER");
    }

    // =========================================================================
    // Non-Privileged catalog creation.
    // -------------------------------------------------------------------------

    url = OracleSeeder.getUrlTrino(connectionHost,
                                   connectionPort,
                                   connectionPrefix,
                                   connectionService);

    createCatalog(tickerSymbol);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create the PostgreSQL catalog file.
   *
   * @param tickerSymbol the DBMS ticker symbol
   */
  private static void createCatalogFilePostgresql(String tickerSymbol) {
    if (isDebug) {
      logger.debug("Start tickerSymbol='" + tickerSymbol + "'");
    }

    // =========================================================================
    // Common variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_POSTGRESQL_CONNECTION_HOST")) {
      connectionHost = osEnvironment.get("DB_SEEDER_POSTGRESQL_CONNECTION_HOST");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_POSTGRESQL_CONNECTION_HOST");
    }

    if (osEnvironment.containsKey("DB_SEEDER_POSTGRESQL_CONNECTION_PORT")) {
      String connectionPortString = osEnvironment.get("DB_SEEDER_POSTGRESQL_CONNECTION_PORT");
      try {
        connectionPort = Integer.parseInt(connectionPortString);
      } catch (NumberFormatException e) {
        MessageHandling.abortProgram(logger,
                                     "Program abort: parameter is not an integer: DB_SEEDER_POSTGRESQL_CONNECTION_PORT");
      }
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_POSTGRESQL_CONNECTION_PORT");
    }

    if (osEnvironment.containsKey("DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX")) {
      connectionPrefix = osEnvironment.get("DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX");
    }

    if (osEnvironment.containsKey("DB_SEEDER_POSTGRESQL_DATABASE")) {
      database = osEnvironment.get("DB_SEEDER_POSTGRESQL_DATABASE");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_POSTGRESQL_DATABASE");
    }

    // =========================================================================
    // Non-Privileged access variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_POSTGRESQL_PASSWORD")) {
      password = osEnvironment.get("DB_SEEDER_POSTGRESQL_PASSWORD");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_POSTGRESQL_PASSWORD");
    }

    if (osEnvironment.containsKey("DB_SEEDER_POSTGRESQL_USER")) {
      user = osEnvironment.get("DB_SEEDER_POSTGRESQL_USER");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_POSTGRESQL_USER");
    }

    // =========================================================================
    // Non-Privileged catalog creation.
    // -------------------------------------------------------------------------

    url = PostgresqlSeeder.getUrlTrino(connectionHost,
                                       connectionPort,
                                       connectionPrefix,
                                       database,
                                       user,
                                       password);

    createCatalog(tickerSymbol);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create the SQL Server catalog file.
   *
   * @param tickerSymbol the DBMS ticker symbol
   */
  private static void createCatalogFileSqlserver(String tickerSymbol) {
    if (isDebug) {
      logger.debug("Start tickerSymbol='" + tickerSymbol + "'");
    }

    // =========================================================================
    // Common variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_SQLSERVER_CONNECTION_HOST")) {
      connectionHost = osEnvironment.get("DB_SEEDER_SQLSERVER_CONNECTION_HOST");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_SQLSERVER_CONNECTION_HOST");
    }

    if (osEnvironment.containsKey("DB_SEEDER_SQLSERVER_CONNECTION_PORT")) {
      String connectionPortString = osEnvironment.get("DB_SEEDER_SQLSERVER_CONNECTION_PORT");
      try {
        connectionPort = Integer.parseInt(connectionPortString);
      } catch (NumberFormatException e) {
        MessageHandling.abortProgram(logger,
                                     "Program abort: parameter is not an integer: DB_SEEDER_SQLSERVER_CONNECTION_PORT");
      }
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_SQLSERVER_CONNECTION_PORT");
    }

    if (osEnvironment.containsKey("DB_SEEDER_SQLSERVER_CONNECTION_PREFIX")) {
      connectionPrefix = osEnvironment.get("DB_SEEDER_SQLSERVER_CONNECTION_PREFIX");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_SQLSERVER_CONNECTION_PREFIX");
    }

    if (osEnvironment.containsKey("DB_SEEDER_SQLSERVER_DATABASE")) {
      database = osEnvironment.get("DB_SEEDER_SQLSERVER_DATABASE");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_SQLSERVER_DATABASE");
    }

    // =========================================================================
    // Non-Privileged access variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_SQLSERVER_PASSWORD")) {
      password = osEnvironment.get("DB_SEEDER_SQLSERVER_PASSWORD");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_SQLSERVER_PASSWORD");
    }

    if (osEnvironment.containsKey("DB_SEEDER_SQLSERVER_USER")) {
      user = osEnvironment.get("DB_SEEDER_SQLSERVER_USER");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_SQLSERVER_USER");
    }

    // =========================================================================
    // Non-Privileged catalog creation.
    // -------------------------------------------------------------------------

    url = SqlserverSeeder.getUrlTrino(connectionHost,
                                      connectionPort,
                                      connectionPrefix,
                                      database,
                                      user,
                                      password);

    createCatalog(tickerSymbol);

    if (isDebug) {
      logger.debug("End");
    }
  }

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

    if (osEnvironment.containsKey("DB_SEEDER_DIRECTORY_CATALOG_PROPERTY")) {
      directoryCatalogProperty = osEnvironment.get("DB_SEEDER_DIRECTORY_CATALOG_PROPERTY");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_DIRECTORY_CATALOG_PROPERTY");
    }

    List<String> tickerSymbolsExtern = Arrays.asList(args);

    Collections.sort(tickerSymbolsExtern);

    for (String tickerSymbol : tickerSymbolsExtern) {
      logger.info("tickerSymbol='" + tickerSymbol + "'");

      tickerSymbol = AbstractDbmsSeeder.dbmsDetails.get(tickerSymbol)[AbstractDbmsSeeder.DBMS_DETAILS_TICKER_SYMBOL_LOWER];

      switch (Objects.requireNonNull(tickerSymbol)) {
      case "mysql":
        logger.info("Start MySQL Database");
        createCatalogFileMysql(tickerSymbol);
        logger.info("End   MySQL Database");
        break;
      case "oracle":
        logger.info("Start Oracle Database");
        createCatalogFileOracle(tickerSymbol);
        logger.info("End   Oracle Database");
        break;
      case "postgresql":
        logger.info("Start PostgreSQL");
        createCatalogFilePostgresql(tickerSymbol);
        logger.info("End   PostgreSQL");
        break;
      case "sqlserver":
        logger.info("Start SQL Server");
        createCatalogFileSqlserver(tickerSymbol);
        logger.info("End   SQL Server");
        break;
      case "":
        MessageHandling.abortProgram(logger,
                                     "Program abort: tickerSymbol is null");
      default:
        MessageHandling.abortProgram(logger,
                                     "Program abort: unknown tickerSymbol, e.g. not yet implemented");
      }
    }

    logger.info("End");

    System.exit(0);
  }
}
