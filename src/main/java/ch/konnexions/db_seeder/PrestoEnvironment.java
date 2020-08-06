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
import ch.konnexions.db_seeder.jdbc.mysql.MysqlSeeder;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Test Data Generator for a Database - Application.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-07-15
 */
@SuppressWarnings("ucd")
public final class PrestoEnvironment {

  private static String              connectionHost   = "";
  private static int                 connectionPort   = 0;
  private static String              connectionPrefix = "";
  private static String              connectionSuffix = "";

  private static String              directoryCatalogProperty;

  private static ArrayList<String>   entries          = new ArrayList<>();

  private static final Logger        logger           = Logger.getLogger(PrestoEnvironment.class);
  private static final boolean       isDebug          = logger.isDebugEnabled();

  private static Map<String, String> osEnvironment    = System.getenv();

  private static String              password         = "";

  private static String              url              = "";
  private static String              user             = "";

  /**
   * Creates the catalog data and the catalog file.
   *
   * @param dbmsTickerSymbol the DBMS ticker symbol
   * @param catalogType the catalog type
   */
  private static void createCatalog(String dbmsTickerSymbol) {
    if (isDebug) {
      logger.debug("Start");
    }

    entries.clear();

    entries.add("connector.name=" + dbmsTickerSymbol);
    entries.add("connection-url=" + url);
    entries.add("connection-user=" + user);
    entries.add("connection-password=" + password);

    createCatalogFile(dbmsTickerSymbol,
                      entries);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Creates a catalog file.
   *
   * @param dbmsTickerSymbol the DBMS ticker symbol
   * @param entries the catalog entries
   */
  private static void createCatalogFile(String dbmsTickerSymbol, ArrayList<String> entries) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      String fileName = directoryCatalogProperty + "/" + AbstractJdbcSeeder.getCatalogName(dbmsTickerSymbol);

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
   * Creates the MySQL catalog file.
   *
   * @param dbmsTickerSymbol the DBMS ticker symbol
   */
  private static void createCatalogFileMysql(String dbmsTickerSymbol) {
    if (isDebug) {
      logger.debug("Start");
    }

    // =========================================================================
    // Common variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_CONNECTION_HOST")) {
      connectionHost = osEnvironment.get("DB_SEEDER_MYSQL_CONNECTION_HOST");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_MYSQL_CONNECTION_HOST");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_CONNECTION_PORT")) {
      String connectionPortString = osEnvironment.get("DB_SEEDER_MYSQL_CONNECTION_PORT");
      try {
        connectionPort = Integer.parseInt(connectionPortString);
      } catch (NumberFormatException e) {
        MessageHandling.abortProgram(logger,
                                     "Parameter is not an integer: DB_SEEDER_MYSQL_CONNECTION_PORT");
      }
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_MYSQL_CONNECTION_PORT");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_CONNECTION_PREFIX")) {
      connectionPrefix = osEnvironment.get("DB_SEEDER_MYSQL_CONNECTION_PREFIX");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_MYSQL_CONNECTION_PREFIX");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_CONNECTION_SUFFIX")) {
      connectionSuffix = osEnvironment.get("DB_SEEDER_MYSQL_CONNECTION_SUFFIX");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_MYSQL_CONNECTION_SUFFIX");
    }

    // =========================================================================
    // Non-Privileged access variables.
    // -------------------------------------------------------------------------

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_PASSWORD")) {
      password = osEnvironment.get("DB_SEEDER_MYSQL_PASSWORD");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_MYSQL_PASSWORD");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_USER")) {
      user = osEnvironment.get("DB_SEEDER_MYSQL_USER");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_MYSQL_USER");
    }

    // =========================================================================
    // Non-Privileged catalog creation.
    // -------------------------------------------------------------------------

    url = MysqlSeeder.getUrlPresto(connectionHost,
                                   connectionPort,
                                   connectionPrefix,
                                   connectionSuffix);

    createCatalog(dbmsTickerSymbol);

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
                                   "No command line arguments found");
    }

    if (osEnvironment.containsKey("DB_SEEDER_DIRECTORY_CATALOG_PROPERTY")) {
      directoryCatalogProperty = osEnvironment.get("DB_SEEDER_DIRECTORY_CATALOG_PROPERTY");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_DIRECTORY_CATALOG_PROPERTY");
    }

    List<String> dbmsTickerSymbols = Arrays.asList(args);

    Collections.sort(dbmsTickerSymbols);

    for (String dbmsTickerSymbol : dbmsTickerSymbols) {
      logger.info("dbmsTickerSymbol='" + dbmsTickerSymbol + "'");

      switch (Objects.requireNonNull(dbmsTickerSymbol)) {
      case "mysql":
        logger.info("Start MySQL Database");
        createCatalogFileMysql(dbmsTickerSymbol);
        logger.info("End   MySQL Database");
        break;
      case "":
        MessageHandling.abortProgram(logger,
                                     "dbmsTickerSymbol is null");
      default:
        MessageHandling.abortProgram(logger,
                                     "Unknown dbmsTickerSymbol, e.g. not yet implemented");
      }
    }

    logger.info("End");

    System.exit(0);
  }
}
