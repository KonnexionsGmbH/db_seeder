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

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;
import ch.konnexions.db_seeder.jdbc.mssqlserver.MssqlserverSeeder;
import ch.konnexions.db_seeder.jdbc.mysql.MysqlSeeder;
import ch.konnexions.db_seeder.jdbc.oracle.OracleSeeder;
import ch.konnexions.db_seeder.jdbc.postgresql.PostgresqlSeeder;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Test Data Generator for a Database - Application.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-07-15
 */
@SuppressWarnings("ucd")
public final class PrestoEnvironment {

  private static String              connectionHost;
  private static int                 connectionPort;
  private static String              connectionPrefix;
  private static String              connectionSuffix;
  private static String              connectionService;

  private static String              database;
  private static String              directoryCatalogProperty;

  private static ArrayList<String>   entries       = new ArrayList<>();

  private static final Logger        logger        = Logger.getLogger(PrestoEnvironment.class);
  private static final boolean       isDebug       = logger.isDebugEnabled();

  private static Map<String, String> osEnvironment = System.getenv();

  private static String              password;

  private static String              tickerSymbolLower;

  private static String              url;
  private static String              user;

  /**
   * Create the catalog data and the catalog file.
   *
   * @param tickerSymbolLower the lower case DBMS ticker symbol
   */
  private static void createCatalog(String tickerSymbolLower) {
    if (isDebug) {
      logger.debug("Start tickerSymbolLower='" + tickerSymbolLower + "'");
    }

    entries.clear();

    String connectorName = "mssqlserver".equals(tickerSymbolLower)
        ? "sqlserver"
        : tickerSymbolLower;

    entries.add("connector.name=" + connectorName);
    entries.add("connection-url=" + url.replace("\"",
                                                ""));
    entries.add("connection-user=" + user);
    entries.add("connection-password=" + password);

    if ("oracle".equals(tickerSymbolLower)) {
      entries.add("oracle.number.default-scale=38");
    }

    createCatalogFile(tickerSymbolLower,
                      entries);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create a catalog file.
   *
   * @param tickerSymbolLower the lower case DBMS ticker symbol
   * @param entries the catalog entries
   */
  private static void createCatalogFile(String tickerSymbolLower, ArrayList<String> entries) {
    if (isDebug) {
      logger.debug("Start tickerSymbolLower='\"+tickerSymbolLower+\"'\"");
    }

    try {
      String fileName = directoryCatalogProperty + "/" + AbstractJdbcSeeder.getCatalogName(tickerSymbolLower) + ".properties";

      if (isDebug) {
        logger.debug("fileName='" + fileName + "'");
      }

      BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName), false)));

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
   * Create the Microsoft SQL Server catalog file.
   *
   * @param tickerSymbolExtern the DBMS ticker symbol
   */
  private static void createCatalogFileMssqlserver(String tickerSymbolExtern) {
    if (isDebug) {
      logger.debug("Start tickerSymbolExtern='" + tickerSymbolExtern + "'");
    }

    // =========================================================================
    // Common variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_MSSQLSERVER_CONNECTION_HOST")) {
      connectionHost = osEnvironment.get("DB_SEEDER_MSSQLSERVER_CONNECTION_HOST");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_MSSQLSERVER_CONNECTION_HOST");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MSSQLSERVER_CONNECTION_PORT")) {
      String connectionPortString = osEnvironment.get("DB_SEEDER_MSSQLSERVER_CONNECTION_PORT");
      try {
        connectionPort = Integer.parseInt(connectionPortString);
      } catch (NumberFormatException e) {
        MessageHandling.abortProgram(logger,
                                     "Program abort: parameter is not an integer: DB_SEEDER_MSSQLSERVER_CONNECTION_PORT");
      }
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_MSSQLSERVER_CONNECTION_PORT");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MSSQLSERVER_CONNECTION_PREFIX")) {
      connectionPrefix = osEnvironment.get("DB_SEEDER_MSSQLSERVER_CONNECTION_PREFIX");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_MSSQLSERVER_CONNECTION_PREFIX");
    }

    // =========================================================================
    // Non-Privileged access variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_MSSQLSERVER_PASSWORD")) {
      password = osEnvironment.get("DB_SEEDER_MSSQLSERVER_PASSWORD");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_MSSQLSERVER_PASSWORD");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MSSQLSERVER_USER")) {
      user = osEnvironment.get("DB_SEEDER_MSSQLSERVER_USER");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Program abort: parameter missing (null): DB_SEEDER_MSSQLSERVER_USER");
    }

    // =========================================================================
    // Non-Privileged catalog creation.
    // -------------------------------------------------------------------------

    url = MssqlserverSeeder.getUrlPresto(connectionHost,
                                         connectionPort,
                                         connectionPrefix);

    createCatalog(tickerSymbolLower);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create the MySQL catalog file.
   *
   * @param tickerSymbolExtern the DBMS ticker symbol
   */
  private static void createCatalogFileMysql(String tickerSymbolExtern) {
    if (isDebug) {
      logger.debug("Start tickerSymbolExtern='" + tickerSymbolExtern + "'");
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

    url = MysqlSeeder.getUrlPresto(connectionHost,
                                   connectionPort,
                                   connectionPrefix,
                                   connectionSuffix);

    createCatalog(tickerSymbolLower);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create the Oracle catalog file.
   *
   * @param tickerSymbolExtern the DBMS ticker symbol
   */
  private static void createCatalogFileOracle(String tickerSymbolExtern) {
    if (isDebug) {
      logger.debug("Start tickerSymbolExtern='" + tickerSymbolExtern + "'");
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

    url = OracleSeeder.getUrlPresto(connectionHost,
                                    connectionPort,
                                    connectionPrefix,
                                    connectionService);

    createCatalog(tickerSymbolLower);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create the PostgreSQL catalog file.
   *
   * @param tickerSymbolExtern the DBMS ticker symbol
   */
  private static void createCatalogFilePostgresql(String tickerSymbolExtern) {
    if (isDebug) {
      logger.debug("Start tickerSymbolExtern='" + tickerSymbolExtern + "'");
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

    url = PostgresqlSeeder.getUrlPresto(connectionHost,
                                        connectionPort,
                                        connectionPrefix,
                                        database);

    createCatalog(tickerSymbolLower);

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

    for (String tickerSymbolExtern : tickerSymbolsExtern) {
      logger.info("tickerSymbolExtern='" + tickerSymbolExtern + "'");

      tickerSymbolLower = AbstractDbmsSeeder.dbmsDetails.get(tickerSymbolExtern)[AbstractDbmsSeeder.DBMS_DETAILS_TICKER_SYMBOL_LOWER];

      switch (Objects.requireNonNull(tickerSymbolExtern)) {
      case "mssqlserver":
        logger.info("Start MS SQL Server");
        createCatalogFileMssqlserver(tickerSymbolExtern);
        logger.info("End   MS SQL Server");
        break;
      case "mysql":
        logger.info("Start MySQL Database");
        createCatalogFileMysql(tickerSymbolExtern);
        logger.info("End   MySQL Database");
        break;
      case "oracle":
        logger.info("Start Oracle Database");
        createCatalogFileOracle(tickerSymbolExtern);
        logger.info("End   Oracle Database");
        break;
      case "postgresql":
        logger.info("Start PostgreSQL Database");
        createCatalogFilePostgresql(tickerSymbolExtern);
        logger.info("End   PostgreSQL Database");
        break;
      case "":
        MessageHandling.abortProgram(logger,
                                     "Program abort: tickerSymbolExtern is null");
      default:
        MessageHandling.abortProgram(logger,
                                     "Program abort: unknown tickerSymbolExtern, e.g. not yet implemented");
      }
    }

    logger.info("End");

    System.exit(0);
  }
}
