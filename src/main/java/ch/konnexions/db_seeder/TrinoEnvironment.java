package ch.konnexions.db_seeder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
public final class TrinoEnvironment { // NO_UCD (unused code)

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
   * @param tickerSymbolIntern the internal DBMS ticker symbol
   */
  private static void createCatalog(String tickerSymbolIntern) {
    if (isDebug) {
      logger.debug("Start tickerSymbolIntern='" + tickerSymbolIntern + "'");
    }

    entries.clear();

    entries.add("connector.name=" + tickerSymbolIntern);
    entries.add("case-insensitive-name-matching=true");
    entries.add("connection-url=" + url.replace("\"",
                                                ""));
    if (!("postgresql".equals(tickerSymbolIntern) || "sqlserver".equals(tickerSymbolIntern))) {
      entries.add("connection-user=" + user);
      entries.add("connection-password=" + password);
    }

    entries.add("insert.non-transactional-insert.enabled=true");

    // issue #4764 wwe
    if ("oracle".equals(tickerSymbolIntern)) {
      entries.add("oracle.number.default-scale=10");
    }

    createCatalogFile(tickerSymbolIntern,
                      entries);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create a catalog file.
   *
   * @param tickerSymbolIntern the internal DBMS ticker symbol
   * @param entries the catalog entries
   */
  private static void createCatalogFile(String tickerSymbolIntern, ArrayList<String> entries) {
    if (isDebug) {
      logger.debug("Start tickerSymbolIntern='\"+tickerSymbolIntern+\"'\"");
    }

    try {
      String fileName = directoryCatalogProperty + File.separator + AbstractJdbcSeeder.getCatalogName(tickerSymbolIntern) + ".properties";

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
   * @param tickerSymbolIntern the internal DBMS ticker symbol
   */
  private static void createCatalogFileMysql(String tickerSymbolIntern) {
    if (isDebug) {
      logger.debug("Start tickerSymbolIntern='" + tickerSymbolIntern + "'");
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

    createCatalog(tickerSymbolIntern);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create the Oracle catalog file.
   *
   * @param tickerSymbolIntern the internal DBMS ticker symbol
   */
  private static void createCatalogFileOracle(String tickerSymbolIntern) {
    if (isDebug) {
      logger.debug("Start tickerSymbolIntern='" + tickerSymbolIntern + "'");
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

    createCatalog(tickerSymbolIntern);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create the PostgreSQL catalog file.
   *
   * @param tickerSymbolIntern the internal DBMS ticker symbol
   */
  private static void createCatalogFilePostgresql(String tickerSymbolIntern) {
    if (isDebug) {
      logger.debug("Start tickerSymbolIntern='" + tickerSymbolIntern + "'");
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

    createCatalog(tickerSymbolIntern);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create the SQL Server catalog file.
   *
   * @param tickerSymbolIntern the internal DBMS ticker symbol
   */
  private static void createCatalogFileSqlserver(String tickerSymbolIntern) {
    if (isDebug) {
      logger.debug("Start tickerSymbolIntern='" + tickerSymbolIntern + "'");
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

    if (osEnvironment.containsKey("DB_SEEDER_SQLSERVER_CONNECTION_SUFFIX")) {
      connectionSuffix = osEnvironment.get("DB_SEEDER_SQLSERVER_CONNECTION_SUFFIX");
    } else {
      MessageHandling.abortProgram(logger,
              "Program abort: parameter missing (null): DB_SEEDER_SQLSERVER_CONNECTION_SUFFIX");
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
                                      password)+connectionSuffix;

    createCatalog(tickerSymbolIntern);

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

    for (String tickerSymbolAnyCase : args[0].split(" ")) {
      logger.info("tickerSymbolAnyCase='" + tickerSymbolAnyCase + "'");
      String tickerSymbolExtern = tickerSymbolAnyCase.toLowerCase();
      logger.info("tickerSymbolExtern=" + tickerSymbolExtern);

      String tickerSymbolIntern = AbstractDbmsSeeder.dbmsDetails.get(tickerSymbolExtern)[AbstractDbmsSeeder.DBMS_DETAILS_TICKER_SYMBOL_LOWER];
      logger.info("tickerSymbolIntern=" + tickerSymbolIntern);

      switch (Objects.requireNonNull(tickerSymbolIntern)) {
      case "mysql":
        logger.info("Start MySQL Database");
        createCatalogFileMysql(tickerSymbolIntern);
        logger.info("End   MySQL Database");
        break;
      case "oracle":
        logger.info("Start Oracle Database");
        createCatalogFileOracle(tickerSymbolIntern);
        logger.info("End   Oracle Database");
        break;
      case "postgresql":
        logger.info("Start PostgreSQL");
        createCatalogFilePostgresql(tickerSymbolIntern);
        logger.info("End   PostgreSQL");
        break;
      case "sqlserver":
        logger.info("Start SQL Server");
        createCatalogFileSqlserver(tickerSymbolIntern);
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
