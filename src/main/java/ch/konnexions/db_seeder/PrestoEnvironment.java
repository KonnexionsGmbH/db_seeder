package ch.konnexions.db_seeder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;

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

  private static final Logger  logger  = Logger.getLogger(PrestoEnvironment.class);

  private static final boolean isDebug = logger.isDebugEnabled();

  private static void createCatalogPropertiesMysql() {
    if (isDebug) {
      logger.debug("Start");
    }

    String              connectionHost           = "";
    int                 connectionPort           = 0;
    String              connectionPrefix         = "";
    String              connectionSuffix         = "";
    String              database                 = "";
    String              databaseSys              = "";
    String              directoryCatalogProperty = "";
    String              password                 = "";
    String              passwordSys              = "";
    String              user                     = "";
    String              userSys                  = "";

    Map<String, String> osEnvironment            = System.getenv();

    if (osEnvironment.containsKey("DB_SEEDER_DIRECTORY_CATALOG_PROPERTY")) {
      directoryCatalogProperty = osEnvironment.get("DB_SEEDER_DIRECTORY_CATALOG_PROPERTY");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_DIRECTORY_CATALOG_PROPERTY");
    }

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

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_DATABASE_SYS")) {
      databaseSys = osEnvironment.get("DB_SEEDER_MYSQL_DATABASE_SYS");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_MYSQL_DATABASE_SYS");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_PASSWORD_SYS")) {
      passwordSys = osEnvironment.get("DB_SEEDER_MYSQL_PASSWORD_SYS");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_MYSQL_PASSWORD_SYS");
    }

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_USER_SYS")) {
      userSys = osEnvironment.get("DB_SEEDER_MYSQL_USER_SYS");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_MYSQL_USER_SYS");
    }

    try {
      BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(directoryCatalogProperty
          + "/db_seeder_mysql_sys.properties"), false)));

      bufferedWriter.append("connector.name=mysql");
      bufferedWriter.newLine();

      bufferedWriter.append("connection-url=" + MysqlSeeder.getUrlSys(connectionHost,
                                                                      connectionPort,
                                                                      connectionPrefix,
                                                                      connectionSuffix,
                                                                      databaseSys));
      bufferedWriter.newLine();

      bufferedWriter.append("connection-user=" + userSys);
      bufferedWriter.newLine();

      bufferedWriter.append("connection-password=" + passwordSys);
      bufferedWriter.newLine();

      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (osEnvironment.containsKey("DB_SEEDER_MYSQL_DATABASE")) {
      database = osEnvironment.get("DB_SEEDER_MYSQL_DATABASE");
    } else {
      MessageHandling.abortProgram(logger,
                                   "Parameter missing (null): DB_SEEDER_MYSQL_DATABASE");
    }

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

    try {
      BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(directoryCatalogProperty
          + "/db_seeder_mysql_user.properties"), false)));

      bufferedWriter.append("connector.name=mysql");
      bufferedWriter.newLine();

      bufferedWriter.append("connection-url=" + MysqlSeeder.getUrlUser(connectionHost,
                                                                       connectionPort,
                                                                       connectionPrefix,
                                                                       connectionSuffix,
                                                                       database));
      bufferedWriter.newLine();

      bufferedWriter.append("connection-user=" + user);
      bufferedWriter.newLine();

      bufferedWriter.append("connection-password=" + password);
      bufferedWriter.newLine();

      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

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
    case "mysql":
      logger.info("Start MySQL Database");
      createCatalogPropertiesMysql();
      logger.info("End   MySQL Database");
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
