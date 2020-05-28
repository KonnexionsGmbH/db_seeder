/*
 *
 */

package ch.konnexions.db_seeder;

import org.apache.log4j.Logger;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * The configuration parameters for the Oracle database. The configuration 
 * parameters are made available to the configuration object in a text
 * file. This text file must contain the values of the following
 * configuration parameters:
 * <ul>
 * <li>db_seeder.file.configuration.name
 * <li>db_seeder.jdbc.connection.host
 * <li>db_seeder.jdbc.connection.port
 * <li>db_seeder.jdbc.connection.service
 * <li>db_seeder.jdbc.oracle.password
 * <li>db_seeder.jdbc.oracle.password.sys
 * <li>db_seeder.jdbc.oracle.user
 * <li>db_seeder.jdbc.oracle.user.sys
 * </ul>
 * The parameter name and parameter value must be separated by an equal sign
 * (=).
 */
public class Config {

  @SuppressWarnings("unused")
  private static Logger                                                logger     = Logger.getLogger(Config.class);
  private final FileBasedConfigurationBuilder<PropertiesConfiguration> fileBasedConfigurationBuilder;
  @SuppressWarnings("unused")
  private final DateTimeFormatter                                      formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnn");

  private String                                                       fileConfigurationName;
  private String                                                       jdbcConnectionHost;
  private int                                                          jdbcConnectionPort;
  private String                                                       jdbcConnectionService;
  private String                                                       jdbcOraclePassword;
  private String                                                       jdbcOraclePasswordSys;
  private String                                                       jdbcOracleUser;
  private String                                                       jdbcOracleUserSys;

  private ArrayList<String>                                            keysSorted = new ArrayList<>();

  private int                                                          maxRowCity;
  private int                                                          maxRowCompany;
  private int                                                          maxRowCountry;
  private int                                                          maxRowCountryState;
  private int                                                          maxRowTimezone;

  private PropertiesConfiguration                                      propertiesConfiguration;

  /**
   * Constructs a Config object.
   */
  public Config() {
    super();

    fileBasedConfigurationBuilder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class);

    File configFile = new File(System.getenv("DB_SEEDER_FILE_CONFIGURATION_NAME"));
    fileBasedConfigurationBuilder.configure(new Parameters().properties().setFile(configFile));

    try {
      propertiesConfiguration = fileBasedConfigurationBuilder.getConfiguration();
      updatePropertiesFromOs();
      keysSorted = getKeysSorted();
    } catch (ConfigurationException e) {
      e.printStackTrace();
    }

    storeConfiguration();
    validateProperties();
  }

  /**
   * @return the host name or the IP address of the database
   */
  public final String getJdbcConnectionHost() {
    return jdbcConnectionHost;
  }

  /**
   * @return the port number where the database server is listening for requests
   */
  public final int getJdbcConnectionPort() {
    return jdbcConnectionPort;
  }

  /**
   * @return the service name to connect to the database
   */
  public final String getJdbcConnectionService() {
    return jdbcConnectionService;
  }

  /**
   * @return the password to connect as normal user to the database
   */
  public String getJdbcOraclePassword() {
    return jdbcOraclePassword;
  }

  /**
   * @return the password to connect as privileged user to the database
   */
  public final String getJdbcOraclePasswordSys() {
    return jdbcOraclePasswordSys;
  }

  /**
   * @return the user name to connect as normal user to the database
   */
  public String getJdbcOracleUser() {
    return jdbcOracleUser.toUpperCase();
  }

  /**
   * @return the user name to connect as privileged user to the database
   */
  public final String getJdbcOracleUserSys() {
    return jdbcOracleUserSys.toUpperCase();
  }

  private ArrayList<String> getKeysSorted() {

    for (final Iterator<String> iterator = propertiesConfiguration.getKeys(); iterator.hasNext();) {
      keysSorted.add(iterator.next());
    }

    Collections.sort(keysSorted);

    return keysSorted;
  }

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

  private List<String> getNotAvailables() {

    List<String> list = new ArrayList<>();

    // wwe list.add("db_seeder.jdbc.connection.service");

    return list;
  }

  @SuppressWarnings("unused")
  private List<String> getNumericProperties() {

    List<String> list = new ArrayList<>();

    list.add("db_seeder.jdbc.connection.port");
    list.add("db_seeder.max.row.city");
    list.add("db_seeder.max.row.company");
    list.add("db_seeder.max.row.country");
    list.add("db_seeder.max.row.country_state");
    list.add("db_seeder.max.row.timezone");

    return list;
  }

  /**
   * Resets selected runtime configuration parameters to the original default
   * value.
   */
  public final void resetNotAvailables() {

    List<String> list      = getNotAvailables();

    boolean      isChanged = false;

    for (final String key : list) {
      if (!(propertiesConfiguration.getString(key).equals("n/a"))) {
        propertiesConfiguration.setProperty(key, "n/a");
        isChanged = true;
      }

    }

    //    if (!(propertiesConfiguration.getString("db_seeder.jdbc.connection.host").equals("0.0.0.0"))) {
    //      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.host", "0.0.0.0");
    //      isChanged = true;
    //    }

    if (isChanged) {
      try {
        fileBasedConfigurationBuilder.save();
        propertiesConfiguration = fileBasedConfigurationBuilder.getConfiguration();
      } catch (ConfigurationException e) {
        e.printStackTrace();
      }
    }
  }

  private void storeConfiguration() {

    propertiesConfiguration.setThrowExceptionOnMissing(true);

    fileConfigurationName = propertiesConfiguration.getString("db_seeder.file.configuration.name");

    jdbcConnectionHost    = propertiesConfiguration.getString("db_seeder.jdbc.connection.host");
    jdbcConnectionPort    = propertiesConfiguration.getInt("db_seeder.jdbc.connection.port");
    jdbcConnectionService = propertiesConfiguration.getString("db_seeder.jdbc.connection.service");

    jdbcOraclePassword    = propertiesConfiguration.getString("db_seeder.jdbc.oracle.password");
    jdbcOraclePasswordSys = propertiesConfiguration.getString("db_seeder.jdbc.oracle.password.sys");
    jdbcOracleUser        = propertiesConfiguration.getString("db_seeder.jdbc.oracle.user");
    jdbcOracleUserSys     = propertiesConfiguration.getString("db_seeder.jdbc.oracle.user.sys");

    maxRowCity            = propertiesConfiguration.getInt("db_seeder.max.row.city");
    maxRowCompany         = propertiesConfiguration.getInt("db_seeder.max.row.company");
    maxRowCountry         = propertiesConfiguration.getInt("db_seeder.max.row.country");
    maxRowCountryState    = propertiesConfiguration.getInt("db_seeder.max.row.country_state");
    maxRowTimezone        = propertiesConfiguration.getInt("db_seeder.max.row.timezone");
  }

  private void updatePropertiesFromOs() {

    Map<String, String> environmentVariables = System.getenv();

    if (environmentVariables.containsKey("DB_SEEDER_FILE_CONFIGURATION_NAME")) {
      fileConfigurationName = environmentVariables.get("DB_SEEDER_FILE_CONFIGURATION_NAME");
      propertiesConfiguration.setProperty("db_seeder.file.configuration.name", fileConfigurationName);
    }

    if (environmentVariables.containsKey("DB_SEEDER_JDBC_CONNECTION_HOST")) {
      jdbcConnectionHost = environmentVariables.get("DB_SEEDER_JDBC_CONNECTION_HOST");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.host", jdbcConnectionHost);
    }

    if (environmentVariables.containsKey("DB_SEEDER_JDBC_CONNECTION_PORT")) {
      jdbcConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_JDBC_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", jdbcConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_JDBC_CONNECTION_SERVICE")) {
      jdbcConnectionService = environmentVariables.get("DB_SEEDER_JDBC_CONNECTION_SERVICE");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.service", jdbcConnectionService);
    }

    if (environmentVariables.containsKey("DB_SEEDER_JDBC_ORACLE_PASSWORD")) {
      jdbcOraclePassword = environmentVariables.get("DB_SEEDER_JDBC_ORACLE_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.jdbc.oracle.password", jdbcOraclePassword);
    }

    if (environmentVariables.containsKey("DB_SEEDER_JDBC_ORACLE_PASSWORD_SYS")) {
      jdbcOraclePasswordSys = environmentVariables.get("DB_SEEDER_JDBC_ORACLE_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.jdbc.oracle.password.sys", jdbcOraclePasswordSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_JDBC_ORACLE_USER")) {
      jdbcOracleUser = environmentVariables.get("DB_SEEDER_JDBC_ORACLE_USER");
      propertiesConfiguration.setProperty("db_seeder.jdbc.oracle.user", jdbcOracleUser);
    }

    if (environmentVariables.containsKey("DB_SEEDER_JDBC_ORACLE_USER_SYS")) {
      jdbcOracleUserSys = environmentVariables.get("DB_SEEDER_JDBC_ORACLE_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.jdbc.oracle.user.sys", jdbcOracleUserSys);
    }

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
  }

  private void validateProperties() {

    boolean isChanged = false;

    //    if (benchmarkBatchSize < 0) {
    //      logger.error("Attention: The value of the configuration parameter 'benchmark.batch.size' ["
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
      }

    }
  }
}
