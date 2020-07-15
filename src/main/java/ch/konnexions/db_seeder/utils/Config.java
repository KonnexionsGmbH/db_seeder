/*
 *
 */

package ch.konnexions.db_seeder.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.AbstractDatabaseSeeder;

/**
 * The configuration parameters for the supported database management systems. 
 * 
 * The configuration parameters are made available to the configuration object
 * in a text file.
 * 
 * The parameter name and parameter value must be separated by an equal sign (=).
 */
public class Config {

  @SuppressWarnings("unused")
  private static Logger                                                logger     = Logger.getLogger(Config.class);

  private String                                                       connectionHost;
  private int                                                          connectionPort;
  private String                                                       connectionPrefix;
  private String                                                       connectionService;
  private String                                                       connectionSuffix;

  private String                                                       database;
  private String                                                       databaseSys;

  private boolean                                                      encodingIso_8859_1;
  private boolean                                                      encodingUtf_8;

  private final FileBasedConfigurationBuilder<PropertiesConfiguration> fileBasedConfigurationBuilder;
  private String                                                       fileConfigurationName;
  private String                                                       fileStatisticsDelimiter;
  private String                                                       fileStatisticsHeader;
  private String                                                       fileStatisticsName;

  private final boolean                                                isDebug    = logger.isDebugEnabled();

  private ArrayList<String>                                            keysSorted = new ArrayList<>();

  private int                                                          maxRowCity;
  private int                                                          maxRowCompany;
  private int                                                          maxRowCountry;
  private int                                                          maxRowCountryState;
  private int                                                          maxRowTimezone;

  private String                                                       password;
  private String                                                       passwordSys;
  private PropertiesConfiguration                                      propertiesConfiguration;

  private String                                                       schema;

  private String                                                       user;
  private String                                                       userSys;

  /**
   * Initialises a new configuration object.
   */
  public Config() {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start Constructor");
    }

    fileBasedConfigurationBuilder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class);

    File configFile = new File(System.getenv("DB_SEEDER_FILE_CONFIGURATION_NAME"));
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
    validateProperties();

    if (isDebug) {
      logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
    }
  }

  @SuppressWarnings("unused")
  private final List<String> getBooleanProperties() {

    List<String> list = new ArrayList<>();

    list.add("db_seeder.encoding.iso_8859_1");
    list.add("db_seeder.encoding.utf_8");

    return list;
  }

  // CONNECTION -------------------------------------------------------

  /**
   * @return the host name where the database server is listening for requests
   */
  public final String getConnectionHost() {
    return connectionHost;
  }

  /**
   * @return the port number where the database server is listening for requests
   */
  public final int getConnectionPort() {
    return connectionPort;
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

  // Encoding ----------------------------------------------------------------

  /**
   * @return the encoding option for ISO-8859-1
   */
  public final boolean getEncodingIso_8859_1() {
    return encodingIso_8859_1;
  }

  /**
   * @return the encoding option for UTF-8
   */
  public final boolean getEncodingUtf_8() {
    return encodingUtf_8;
  }

  // -------------------------------------------------------------------------

  /**
   * @return the file configuration name
   */
  public final String getFileConfigurationName() {
    return fileConfigurationName;
  }

  // File Statistics ---------------------------------------------------------

  /**
   * @return the file statistics delimiter
   */
  public final String getFileStatisticsDelimiter() {
    return "\\t".equals(fileStatisticsDelimiter) ? Character.toString('\t') : fileStatisticsDelimiter;
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

  // -------------------------------------------------------------------------

  private final ArrayList<String> getKeysSorted() {

    for (final Iterator<String> iterator = propertiesConfiguration.getKeys(); iterator.hasNext();) {
      keysSorted.add(iterator.next());
    }

    Collections.sort(keysSorted);

    return keysSorted;
  }

  // MAX (rows) --------------------------------------------------------------

  /**
   * @return the maximum number of rows to be generated for database table CITY
   */
  public final int getMaxRowCity() {
    return maxRowCity;
  }

  /**
   * @return the maximum number of rows to be generated for database table COMPANY
   */
  public final int getMaxRowCompany() {
    return maxRowCompany;
  }

  /**
   * @return the maximum number of rows to be generated for database table COUNTRY
   */
  public final int getMaxRowCountry() {
    return maxRowCountry;
  }

  /**
   * @return the maximum number of rows to be generated for database table COUNTRY_STATE
   */
  public final int getMaxRowCountryState() {
    return maxRowCountryState;
  }

  /**
   * @return the maximum number of rows to be generated for database table COUNTRY
   */
  public final int getMaxRowTimezone() {
    return maxRowTimezone;
  }

  // -------------------------------------------------------------------------

  @SuppressWarnings("unused")
  private final List<String> getNumericProperties() {

    List<String> list = new ArrayList<>();

    list.add("db_seeder.connection.port");
    list.add("db_seeder.max.row.city");
    list.add("db_seeder.max.row.company");
    list.add("db_seeder.max.row.country");
    list.add("db_seeder.max.row.country_state");
    list.add("db_seeder.max.row.timezone");

    return list;
  }

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

  private final void storeConfiguration() {

    propertiesConfiguration.setThrowExceptionOnMissing(true);

    connectionHost          = propertiesConfiguration.getString("db_seeder.connection.host");
    connectionPort          = propertiesConfiguration.getInt("db_seeder.connection.port");
    connectionPrefix        = propertiesConfiguration.getString("db_seeder.connection.prefix");
    connectionService       = propertiesConfiguration.getString("db_seeder.connection.service");
    connectionSuffix        = propertiesConfiguration.getString("db_seeder.connection.suffix");

    database                = propertiesConfiguration.getString("db_seeder.database");
    databaseSys             = propertiesConfiguration.getString("db_seeder.database.sys");

    encodingIso_8859_1      = propertiesConfiguration.getBoolean("db_seeder.encoding.iso_8859_1");
    encodingUtf_8           = propertiesConfiguration.getBoolean("db_seeder.encoding.utf_8");

    fileConfigurationName   = propertiesConfiguration.getString("db_seeder.file.configuration.name");
    fileStatisticsDelimiter = propertiesConfiguration.getString("db_seeder.file.statistics.delimiter");
    fileStatisticsHeader    = propertiesConfiguration.getString("db_seeder.file.statistics.header");
    fileStatisticsName      = propertiesConfiguration.getString("db_seeder.file.statistics.name");

    maxRowCity              = propertiesConfiguration.getInt("db_seeder.max.row.city");
    maxRowCompany           = propertiesConfiguration.getInt("db_seeder.max.row.company");
    maxRowCountry           = propertiesConfiguration.getInt("db_seeder.max.row.country");
    maxRowCountryState      = propertiesConfiguration.getInt("db_seeder.max.row.country_state");
    maxRowTimezone          = propertiesConfiguration.getInt("db_seeder.max.row.timezone");

    password                = propertiesConfiguration.getString("db_seeder.password");
    passwordSys             = propertiesConfiguration.getString("db_seeder.password.sys");

    schema                  = propertiesConfiguration.getString("db_seeder.schema");

    user                    = propertiesConfiguration.getString("db_seeder.user");
    userSys                 = propertiesConfiguration.getString("db_seeder.user.sys");
  }

  private final void updatePropertiesFromOs() {

    Map<String, String> environmentVariables = System.getenv();

    // CONNECTION -------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_HOST")) {
      connectionHost = environmentVariables.get("DB_SEEDER_CONNECTION_HOST");
      propertiesConfiguration.setProperty("db_seeder.connection.host", connectionHost);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_PORT")) {
      connectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.connection.port", connectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_PREFIX")) {
      connectionPrefix = environmentVariables.get("DB_SEEDER_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.connection.prefix", connectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_SERVICE")) {
      connectionService = environmentVariables.get("DB_SEEDER_CONNECTION_SERVICE");
      propertiesConfiguration.setProperty("db_seeder.connection.service", connectionService);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CONNECTION_SUFFIX")) {
      connectionSuffix = environmentVariables.get("DB_SEEDER_CONNECTION_SUFFIX");
      propertiesConfiguration.setProperty("db_seeder.connection.suffix", connectionSuffix);
    }

    // DATABASE ---------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_DATABASE")) {
      database = environmentVariables.get("DB_SEEDER_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.database", database);
    }

    if (environmentVariables.containsKey("DB_SEEDER_DATABASE_SYS")) {
      databaseSys = environmentVariables.get("DB_SEEDER_DATABASE_SYS");
      propertiesConfiguration.setProperty("db_seeder.database.sys", databaseSys);
    }

    // Encoding ----------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_ENCODING_ISO_8859_1")) {
      String encodingIso_8859_1Helper = environmentVariables.get("DB_SEEDER_ENCODING_ISO_8859_1");
      propertiesConfiguration.setProperty("db_seeder.encoding.is_8859_1", "true".equals(encodingIso_8859_1Helper.toLowerCase()) ? true : false);
    }

    if (environmentVariables.containsKey("DB_SEEDER_ENCODING_UTF_8")) {
      String encodingUtf_8Helper = environmentVariables.get("DB_SEEDER_ENCODING_UTF_8");
      propertiesConfiguration.setProperty("db_seeder.encoding.utf_8", "true".equals(encodingUtf_8Helper.toLowerCase()) ? true : false);
    }

    // File Configuration -------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_FILE_CONFIGURATION_NAME")) {
      fileConfigurationName = environmentVariables.get("DB_SEEDER_FILE_CONFIGURATION_NAME");
      propertiesConfiguration.setProperty("db_seeder.file.configuration.name", fileConfigurationName);
    }

    // File Statistics ---------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_FILE_STATISTICS_DELIMITER")) {
      fileStatisticsDelimiter = environmentVariables.get("DB_SEEDER_FILE_STATISTICS_DELIMITER");
      propertiesConfiguration.setProperty("db_seeder.file.statistics.delimiter", fileStatisticsDelimiter);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FILE_STATISTICS_HEADER")) {
      fileStatisticsHeader = environmentVariables.get("DB_SEEDER_FILE_STATISTICS_HEADER");
      propertiesConfiguration.setProperty("db_seeder.file.statistics.header", fileStatisticsHeader);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FILE_STATISTICS_NAME")) {
      fileStatisticsName = environmentVariables.get("DB_SEEDER_FILE_STATISTICS_NAME");
      propertiesConfiguration.setProperty("db_seeder.file.statistics.name", fileStatisticsName);
    }

    // MAX (rows) --------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_MAX_ROW_CITY")) {
      maxRowCity = Integer.parseInt(environmentVariables.get("DB_SEEDER_MAX_ROW_CITY"));
      propertiesConfiguration.setProperty("db_seeder.max.row.city", maxRowCity);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MAX_ROW_COMPANY")) {
      maxRowCompany = Integer.parseInt(environmentVariables.get("DB_SEEDER_MAX_ROW_COMPANY"));
      propertiesConfiguration.setProperty("db_seeder.max.row.company", maxRowCompany);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MAX_ROW_COUNTRY")) {
      maxRowCountry = Integer.parseInt(environmentVariables.get("DB_SEEDER_MAX_ROW_COUNTRY"));
      propertiesConfiguration.setProperty("db_seeder.max.row.country", maxRowCountry);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MAX_ROW_COUNTRY_STATE")) {
      maxRowCountryState = Integer.parseInt(environmentVariables.get("DB_SEEDER_MAX_ROW_COUNTRY_STATE"));
      propertiesConfiguration.setProperty("db_seeder.max.row.country_state", maxRowCountryState);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MAX_ROW_TIMEZONE")) {
      maxRowTimezone = Integer.parseInt(environmentVariables.get("DB_SEEDER_MAX_ROW_TIMEZONE"));
      propertiesConfiguration.setProperty("db_seeder.max.row.timezone", maxRowTimezone);
    }

    // PASSWORD ---------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_PASSWORD")) {
      password = environmentVariables.get("DB_SEEDER_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.password", password);
    }

    if (environmentVariables.containsKey("DB_SEEDER_PASSWORD_SYS")) {
      passwordSys = environmentVariables.get("DB_SEEDER_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.password.sys", passwordSys);
    }

    // SCHEMA -----------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_SCHEMA")) {
      schema = environmentVariables.get("DB_SEEDER_SCHEMA");
      propertiesConfiguration.setProperty("db_seeder.schema", schema);
    }

    // USER -------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_USER")) {
      user = environmentVariables.get("DB_SEEDER_USER");
      propertiesConfiguration.setProperty("db_seeder.user", user);
    }

    if (environmentVariables.containsKey("DB_SEEDER_USER_SYS")) {
      userSys = environmentVariables.get("DB_SEEDER_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.user.sys", userSys);
    }
  }

  private final void validateProperties() {

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start");
    }

    boolean isChanged = false;

    //    if (benchmarkBatchSize < 0) {
    //      logger.error(String.format(FORMAT_METHOD_NAME, methodName) 
    //          + "Attention: The value of the configuration parameter 'benchmark.batch.size' ["
    //          + benchmarkBatchSize + "] must not be less than 0, the specified value is replaced by 0.");
    //      benchmarkBatchSize = 0;
    //      propertiesConfiguration.setProperty("benchmark.batch.size", benchmarkBatchSize);
    //      isChanged = true;
    //    }

    if (isChanged) {
      try {
        fileBasedConfigurationBuilder.save();
        propertiesConfiguration = fileBasedConfigurationBuilder.getConfiguration();
      } catch (ConfigurationException e) {
        e.printStackTrace();
        System.exit(1);
      }

      if (isDebug) {
        logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");
      }
    }
  }
}
