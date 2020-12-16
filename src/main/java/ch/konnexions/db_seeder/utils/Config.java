/*
 *
 */

package ch.konnexions.db_seeder.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

/**
 * The configuration parameters for the supported database management systems.
 * <p>
 * The configuration parameters are made available to the configuration object
 * in a text file.
 * <p>
 * The parameter name and parameter value must be separated by an equal sign (=).
 */
public final class Config {

  private static final Logger     logger     = Logger.getLogger(Config.class);
  //  private final boolean           isDebug    = logger.isDebugEnabled();

  private String                  connectionHost;
  private String                  connectionHostPresto;
  private int                     connectionPort;
  private int                     connectionPortPresto;
  private String                  connectionPrefix;
  private String                  connectionService;
  private String                  connectionSuffix;

  private String                  database;
  private String                  databaseSys;

  private String                  fileConfigurationName;
  private String                  fileJsonName;
  private String                  fileStatisticsDelimiter;
  private String                  fileStatisticsHeader;
  private String                  fileStatisticsName;
  private String                  fileStatisticsSummaryName;
  private String                  fileStatisticsSummarySource;

  private ArrayList<String>       keysSorted = new ArrayList<>();

  private String                  password;
  private String                  passwordSys;
  private PropertiesConfiguration propertiesConfiguration;

  private String                  schema;

  private String                  user;
  private String                  userSys;

  /**
   * Initialises a new configuration object.
   */
  public Config() {
    super();

    boolean isDebug = logger.isDebugEnabled();
    if (isDebug) {
      logger.debug("Start Constructor");
    }

    FileBasedConfigurationBuilder<PropertiesConfiguration> fileBasedConfigurationBuilder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class);

    File                                                   configFile                    = new File(System.getenv("DB_SEEDER_FILE_CONFIGURATION_NAME"));
    fileBasedConfigurationBuilder.configure(new Parameters().properties().setFile(configFile));

    try {
      propertiesConfiguration = fileBasedConfigurationBuilder.getConfiguration();
      updatePropertiesFromOs();
      keysSorted = getKeysSorted();
    } catch (ConfigurationException e) {
      e.printStackTrace();
      System.exit(1);
    }

    storeConfiguration();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  //  private ArrayList<String> getBooleanProperties() {
  //
  //    ArrayList<String> list = new ArrayList<>();
  //
  //    list.add("db_seeder.encoding.iso_8859_1");
  //    list.add("db_seeder.encoding.utf_8");
  //
  //    return list;
  //  }

  // CONNECTION -------------------------------------------------------

  /**
   * @return the host name where the database server is listening for requests
   */
  public final String getConnectionHost() {
    return connectionHost;
  }

  /**
   * @return the host name where the Presto server is listening for requests
   */
  public String getConnectionHostPresto() {
    return connectionHostPresto;
  }

  /**
   * @return the port number where the database server is listening for requests
   */
  public final int getConnectionPort() {
    return connectionPort;
  }

  /**
   * @return the port number where the Presto server is listening for requests
   */
  public final int getConnectionPortPresto() {
    return connectionPortPresto;
  }

  /**
   * @return the prefix of the connection string
   */
  public final String getConnectionPrefix() {
    return connectionPrefix;
  }

  /**
   * @return the service name to connect to the database
   */
  public final String getConnectionService() {
    return connectionService;
  }

  /**
   * @return the suffix of the connection string
   */
  public final String getConnectionSuffix() {
    return connectionSuffix;
  }

  // DATABASE ---------------------------------------------------------

  /**
   * @return the name of the normal database
   */
  public final String getDatabase() {
    return database;
  }

  /**
   * @return the name of the privileged database
   */
  public final String getDatabaseSys() {
    return databaseSys;
  }

  // -------------------------------------------------------------------------

  /**
   * @return the file JSON name
   */
  public final String getFileJsonName() {
    return fileJsonName;
  }

  // File Statistics ---------------------------------------------------------

  /**
   * @return the file statistics delimiter
   */
  public final String getFileStatisticsDelimiter() {
    return "\\t".equals(fileStatisticsDelimiter)
        ? Character.toString('\t')
        : fileStatisticsDelimiter;
  }

  /**
   * @return the file statistics header
   */
  public final String getFileStatisticsHeader() {
    return fileStatisticsHeader;
  }

  /**
   * @return the file statistics name
   */
  public final String getFileStatisticsName() {
    return fileStatisticsName;
  }

  /**
   * @return the file statistics summary name
   */
  public final String getFileStatisticsSummaryName() {
    return fileStatisticsSummaryName;
  }

  /**
   * @return the file statistics summary source directories
   */
  public final String getFileStatisticsSummarySource() {
    return fileStatisticsSummarySource;
  }

  // -------------------------------------------------------------------------

  private ArrayList<String> getKeysSorted() {

    for (Iterator<String> iterator = propertiesConfiguration.getKeys(); iterator.hasNext();) {
      keysSorted.add(iterator.next());
    }

    Collections.sort(keysSorted);

    return keysSorted;
  }

  // -------------------------------------------------------------------------

  //  private ArrayList<String> getNumericProperties() {
  //
  //    ArrayList<String> list = new ArrayList<>();
  //
  //    list.add("db_seeder.connection.port");
  //    list.add("db_seeder.connection.port.presto");
  //    list.add("db_seeder.null.factor");
  //
  //    return list;
  //  }

  // PASSWORD ---------------------------------------------------------

  /**
   * @return the password to connect as normal user to the database
   */
  public final String getPassword() {
    return password;
  }

  /**
   * @return the password to connect as privileged user to the database
   */
  public final String getPasswordSys() {
    return passwordSys;
  }

  // SCHEMA -----------------------------------------------------------

  /**
   * @return the schema name
   */
  public final String getSchema() {
    return schema;
  }

  // USER -------------------------------------------------------------

  /**
   * @return the user name to connect as normal user to the database
   */
  public final String getUser() {
    return user;
  }

  /**
   * @return the user name to connect as privileged user to the database
   */
  public final String getUserSys() {
    return userSys;
  }

  private void storeConfiguration() {

    propertiesConfiguration.setThrowExceptionOnMissing(true);

    connectionHost              = propertiesConfiguration.getString("db_seeder.connection.host");
    connectionHostPresto        = propertiesConfiguration.getString("db_seeder.connection.host.presto");
    connectionPort              = propertiesConfiguration.getInt("db_seeder.connection.port");
    connectionPortPresto        = propertiesConfiguration.getInt("db_seeder.connection.port.presto");
    connectionPrefix            = propertiesConfiguration.getString("db_seeder.connection.prefix");
    connectionService           = propertiesConfiguration.getString("db_seeder.connection.service");
    connectionSuffix            = propertiesConfiguration.getString("db_seeder.connection.suffix");

    database                    = propertiesConfiguration.getString("db_seeder.database");
    databaseSys                 = propertiesConfiguration.getString("db_seeder.database.sys");

    fileConfigurationName       = propertiesConfiguration.getString("db_seeder.file.configuration.name");
    fileJsonName                = propertiesConfiguration.getString("db_seeder.file.json.name");
    fileStatisticsDelimiter     = propertiesConfiguration.getString("db_seeder.file.statistics.delimiter");
    fileStatisticsHeader        = propertiesConfiguration.getString("db_seeder.file.statistics.header");
    fileStatisticsName          = propertiesConfiguration.getString("db_seeder.file.statistics.name");
    fileStatisticsSummaryName   = propertiesConfiguration.getString("db_seeder.file.statistics.summary.name");
    fileStatisticsSummarySource = propertiesConfiguration.getString("db_seeder.file.statistics.summary.source");

    password                    = propertiesConfiguration.getString("db_seeder.password");
    passwordSys                 = propertiesConfiguration.getString("db_seeder.password.sys");

    schema                      = propertiesConfiguration.getString("db_seeder.schema");

    user                        = propertiesConfiguration.getString("db_seeder.user");
    userSys                     = propertiesConfiguration.getString("db_seeder.user.sys");
  }

  private void updatePropertiesFromOs() {

    Map<String, String> environmentVariables = System.getenv();

    // CONNECTION -------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_HOST")) {
      connectionHost = environmentVariables.get("DB_SEEDER_CONNECTION_HOST");
      propertiesConfiguration.setProperty("db_seeder.connection.host",
                                          connectionHost);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_HOST_PRESTO")) {
      connectionHostPresto = environmentVariables.get("DB_SEEDER_CONNECTION_HOST_PRESTO");
      propertiesConfiguration.setProperty("db_seeder.connection.host.presto",
                                          connectionHostPresto);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_PORT")) {
      connectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.connection.port",
                                          connectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_PORT_PRESTO")) {
      connectionPortPresto = Integer.parseInt(environmentVariables.get("DB_SEEDER_CONNECTION_PORT_PRESTO"));
      propertiesConfiguration.setProperty("db_seeder.connection.port.presto",
                                          connectionPortPresto);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_PREFIX")) {
      connectionPrefix = environmentVariables.get("DB_SEEDER_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.connection.prefix",
                                          connectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_SERVICE")) {
      connectionService = environmentVariables.get("DB_SEEDER_CONNECTION_SERVICE");
      propertiesConfiguration.setProperty("db_seeder.connection.service",
                                          connectionService);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_SUFFIX")) {
      connectionSuffix = environmentVariables.get("DB_SEEDER_CONNECTION_SUFFIX");
      propertiesConfiguration.setProperty("db_seeder.connection.suffix",
                                          connectionSuffix);
    }

    // DATABASE ---------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_DATABASE")) {
      database = environmentVariables.get("DB_SEEDER_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.database",
                                          database);
    }

    if (environmentVariables.containsKey("DB_SEEDER_DATABASE_SYS")) {
      databaseSys = environmentVariables.get("DB_SEEDER_DATABASE_SYS");
      propertiesConfiguration.setProperty("db_seeder.database.sys",
                                          databaseSys);
    }

    // File Configuration -------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_FILE_CONFIGURATION_NAME")) {
      fileConfigurationName = environmentVariables.get("DB_SEEDER_FILE_CONFIGURATION_NAME");
      propertiesConfiguration.setProperty("db_seeder.file.configuration.name",
                                          fileConfigurationName);
    }

    // File Json ----------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_FILE_JSON_NAME")) {
      fileJsonName = environmentVariables.get("DB_SEEDER_FILE_JSON_NAME");
      propertiesConfiguration.setProperty("db_seeder.file.json.name",
                                          fileJsonName);
    }

    // File Statistics ---------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_FILE_STATISTICS_DELIMITER")) {
      fileStatisticsDelimiter = environmentVariables.get("DB_SEEDER_FILE_STATISTICS_DELIMITER");
      propertiesConfiguration.setProperty("db_seeder.file.statistics.delimiter",
                                          fileStatisticsDelimiter);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FILE_STATISTICS_HEADER")) {
      fileStatisticsHeader = environmentVariables.get("DB_SEEDER_FILE_STATISTICS_HEADER");
      propertiesConfiguration.setProperty("db_seeder.file.statistics.header",
                                          fileStatisticsHeader);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FILE_STATISTICS_NAME")) {
      fileStatisticsName = environmentVariables.get("DB_SEEDER_FILE_STATISTICS_NAME");
      propertiesConfiguration.setProperty("db_seeder.file.statistics.name",
                                          fileStatisticsName);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FILE_STATISTICS_SUMMARY_NAME")) {
      fileStatisticsSummaryName = environmentVariables.get("DB_SEEDER_FILE_STATISTICS_SUMMARY_NAME");
      propertiesConfiguration.setProperty("db_seeder.file.statistics.summary.name",
                                          fileStatisticsSummaryName);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FILE_STATISTICS_SUMMARY_SOURCE")) {
      fileStatisticsSummarySource = environmentVariables.get("DB_SEEDER_FILE_STATISTICS_SUMMARY_SOURCE");
      propertiesConfiguration.setProperty("db_seeder.file.statistics.summary.source",
                                          fileStatisticsSummarySource);
    }

    // PASSWORD ---------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_PASSWORD")) {
      password = environmentVariables.get("DB_SEEDER_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.password",
                                          password);
    }

    if (environmentVariables.containsKey("DB_SEEDER_PASSWORD_SYS")) {
      passwordSys = environmentVariables.get("DB_SEEDER_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.password.sys",
                                          passwordSys);
    }

    // SCHEMA -----------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_SCHEMA")) {
      schema = environmentVariables.get("DB_SEEDER_SCHEMA");
      propertiesConfiguration.setProperty("db_seeder.schema",
                                          schema);
    }

    // USER -------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_USER")) {
      user = environmentVariables.get("DB_SEEDER_USER");
      propertiesConfiguration.setProperty("db_seeder.user",
                                          user);
    }

    if (environmentVariables.containsKey("DB_SEEDER_USER_SYS")) {
      userSys = environmentVariables.get("DB_SEEDER_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.user.sys",
                                          userSys);
    }
  }
}
