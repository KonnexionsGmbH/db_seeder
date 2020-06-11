/*
 *
 */

package ch.konnexions.db_seeder;

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
import org.apache.log4j.Logger;

/**
 * The configuration parameters for the supported database brands. The 
 * configuration parameters are made available to the configuration object
 * in a text file.
 * 
 * The parameter name and parameter value must be separated by an equal sign
 * (=).
 */
public class Config {

  @SuppressWarnings("unused")
  private static Logger                                                logger     = Logger.getLogger(Config.class);
  private final FileBasedConfigurationBuilder<PropertiesConfiguration> fileBasedConfigurationBuilder;
  private String                                                       fileConfigurationName;

  @SuppressWarnings("unused")
  private final DateTimeFormatter                                      formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnn");
  private String                                                       jdbcConnectionHost;

  private ArrayList<String>                                            keysSorted = new ArrayList<>();

  private int                                                          maxRowCity;
  private int                                                          maxRowCompany;
  private int                                                          maxRowCountry;
  private int                                                          maxRowCountryState;
  private int                                                          maxRowTimezone;

  private String                                                       mssqlserverDatabase;
  private int                                                          mssqlserverConnectionPort;
  private String                                                       mssqlserverConnectionPrefix;
  private String                                                       mssqlserverPassword;
  private String                                                       mssqlserverPasswordSys;
  private String                                                       mssqlserverSchema;
  private String                                                       mssqlserverUser;

  private int                                                          mysqlConnectionPort;
  private String                                                       mysqlConnectionPrefix;
  private String                                                       mysqlConnectionSuffix;
  private String                                                       mysqlPassword;
  private String                                                       mysqlPasswordSys;
  private String                                                       mysqlSchema;
  private String                                                       mysqlUser;

  private int                                                          oracleConnectionPort;
  private String                                                       oracleConnectionPrefix;
  private String                                                       oracleConnectionService;
  private String                                                       oraclePassword;
  private String                                                       oraclePasswordSys;
  private String                                                       oracleUser;

  private String                                                       postgresqlDatabase;
  private int                                                          postgresqlConnectionPort;
  private String                                                       postgresqlConnectionPrefix;
  private String                                                       postgresqlPassword;
  private String                                                       postgresqlPasswordSys;
  private String                                                       postgresqlUser;

  private PropertiesConfiguration                                      propertiesConfiguration;

  /**
   * Constructs a Configuration object.
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

  /**
   * @return the Microsoft SQL Server port number where the database server is listening for requests
   */
  public final int getMssqlserverConnectionPort() {
    return mssqlserverConnectionPort;
  }

  /**
   * @return the prefix of the Microsoft SQL Server connection string
   */
  public final String getMssqlserverConnectionPrefix() {
    return mssqlserverConnectionPrefix;
  }

  /**
   * @return the Microsoft SQL Server database name
   */
  public final String getMssqlserverDatabase() {
    return mssqlserverDatabase;
  }

  /**
   * @return the Microsoft SQL Server password to connect as normal user to the database
   */
  public final String getMssqlserverPassword() {
    return mssqlserverPassword;
  }

  /**
   * @return the Microsoft SQL Server password to connect as privileged user to the database
   */
  public final String getMssqlserverPasswordSys() {
    return mssqlserverPasswordSys;
  }

  /**
   * @return the Microsoft SQL Server schema name
   */
  public final String getMssqlserverSchema() {
    return mssqlserverSchema;
  }

  /**
   * @return the Microsoft SQL Server user name to connect as normal user to the database
   */
  public final String getMssqlserverUser() {
    return mssqlserverUser;
  }

  /**
   * @return the MySQL port number where the database server is listening for requests
   */
  public final int getMysqlConnectionPort() {
    return mysqlConnectionPort;
  }

  /**
   * @return the prefix of the MySQL connection string
   */
  public final String getMysqlConnectionPrefix() {
    return mysqlConnectionPrefix;
  }

  /**
   * @return the suffix of the MySQL connection string
   */
  public final String getMysqlConnectionSuffix() {
    return mysqlConnectionSuffix;
  }

  /**
   * @return the MySQL password to connect as normal user to the database
   */
  public final String getMysqlPassword() {
    return mysqlPassword;
  }

  /**
   * @return the MySQL password to connect as privileged user to the database
   */
  public final String getMysqlPasswordSys() {
    return mysqlPasswordSys;
  }

  /**
   * @return the MySQL schema name
   */
  public final String getMysqlSchema() {
    return mysqlSchema;
  }

  /**
   * @return the MySQL user name to connect as normal user to the database
   */
  public final String getMysqlUser() {
    return mysqlUser;
  }

  @SuppressWarnings("unused")
  private List<String> getNumericProperties() {

    List<String> list = new ArrayList<>();

    list.add("db_seeder.max.row.city");
    list.add("db_seeder.max.row.company");
    list.add("db_seeder.max.row.country");
    list.add("db_seeder.max.row.country_state");
    list.add("db_seeder.max.row.timezone");
    list.add("db_seeder.mssqlserver.connection.port");
    list.add("db_seeder.mysql.connection.port");
    list.add("db_seeder.oracle.connection.port");
    list.add("db_seeder.postgresql.connection.port");

    return list;
  }

  /**
   * @return the Oracle port number where the database server is listening for requests
   */
  public final int getOracleConnectionPort() {
    return oracleConnectionPort;
  }

  /**
   * @return the prefix of the Oracle connection string
   */
  public final String getOracleConnectionPrefix() {
    return oracleConnectionPrefix;
  }

  /**
   * @return the Oracle service name to connect to the database
   */
  public final String getOracleConnectionService() {
    return oracleConnectionService;
  }

  /**
   * @return the Oracle password to connect as normal user to the database
   */
  public final String getOraclePassword() {
    return oraclePassword;
  }

  /**
   * @return the Oracle password to connect as privileged user to the database
   */
  public final String getOraclePasswordSys() {
    return oraclePasswordSys;
  }

  /**
   * @return the Oracle user name to connect as normal user to the database
   */
  public final String getOracleUser() {
    return oracleUser.toUpperCase();
  }

  /**
   * @return the PostgreSQL Database port number where the database server is listening for requests
   */
  public final int getPostgresqlConnectionPort() {
    return postgresqlConnectionPort;
  }

  /**
   * @return the prefix of the PostgreSQL Database connection string
   */
  public final String getPostgresqlConnectionPrefix() {
    return postgresqlConnectionPrefix;
  }

  /**
   * @return the PostgreSQL Database database name
   */
  public final String getPostgresqlDatabase() {
    return postgresqlDatabase;
  }

  /**
   * @return the PostgreSQL Database password to connect as normal user to the database
   */
  public final String getPostgresqlPassword() {
    return postgresqlPassword;
  }

  /**
   * @return the PostgreSQL Database password to connect as privileged user to the database
   */
  public final String getPostgresqlPasswordSys() {
    return postgresqlPasswordSys;
  }

  /**
   * @return the PostgreSQL Database user name to connect as normal user to the database
   */
  public final String getPostgresqlUser() {
    return postgresqlUser;
  }

  private void storeConfiguration() {

    propertiesConfiguration.setThrowExceptionOnMissing(true);

    fileConfigurationName       = propertiesConfiguration.getString("db_seeder.file.configuration.name");

    jdbcConnectionHost          = propertiesConfiguration.getString("db_seeder.jdbc.connection.host");

    maxRowCity                  = propertiesConfiguration.getInt("db_seeder.max.row.city");
    maxRowCompany               = propertiesConfiguration.getInt("db_seeder.max.row.company");
    maxRowCountry               = propertiesConfiguration.getInt("db_seeder.max.row.country");
    maxRowCountryState          = propertiesConfiguration.getInt("db_seeder.max.row.country_state");
    maxRowTimezone              = propertiesConfiguration.getInt("db_seeder.max.row.timezone");

    mssqlserverConnectionPort   = propertiesConfiguration.getInt("db_seeder.mssqlserver.connection.port");
    mssqlserverConnectionPrefix = propertiesConfiguration.getString("db_seeder.mssqlserver.connection.prefix");
    mssqlserverDatabase         = propertiesConfiguration.getString("db_seeder.mssqlserver.database");
    mssqlserverPassword         = propertiesConfiguration.getString("db_seeder.mssqlserver.password");
    mssqlserverPasswordSys      = propertiesConfiguration.getString("db_seeder.mssqlserver.password.sys");
    mssqlserverSchema           = propertiesConfiguration.getString("db_seeder.mssqlserver.schema");
    mssqlserverUser             = propertiesConfiguration.getString("db_seeder.mssqlserver.user");

    mysqlConnectionPort         = propertiesConfiguration.getInt("db_seeder.mysql.connection.port");
    mysqlConnectionPrefix       = propertiesConfiguration.getString("db_seeder.mysql.connection.prefix");
    mysqlConnectionSuffix       = propertiesConfiguration.getString("db_seeder.mysql.connection.suffix");
    mysqlPassword               = propertiesConfiguration.getString("db_seeder.mysql.password");
    mysqlPasswordSys            = propertiesConfiguration.getString("db_seeder.mysql.password.sys");
    mysqlSchema                 = propertiesConfiguration.getString("db_seeder.mysql.schema");
    mysqlUser                   = propertiesConfiguration.getString("db_seeder.mysql.user");

    oracleConnectionPort        = propertiesConfiguration.getInt("db_seeder.oracle.connection.port");
    oracleConnectionPrefix      = propertiesConfiguration.getString("db_seeder.oracle.connection.prefix");
    oracleConnectionService     = propertiesConfiguration.getString("db_seeder.oracle.connection.service");
    oraclePassword              = propertiesConfiguration.getString("db_seeder.oracle.password");
    oraclePasswordSys           = propertiesConfiguration.getString("db_seeder.oracle.password.sys");
    oracleUser                  = propertiesConfiguration.getString("db_seeder.oracle.user");

    postgresqlConnectionPort    = propertiesConfiguration.getInt("db_seeder.postgresql.connection.port");
    postgresqlConnectionPrefix  = propertiesConfiguration.getString("db_seeder.postgresql.connection.prefix");
    postgresqlDatabase          = propertiesConfiguration.getString("db_seeder.postgresql.database");
    postgresqlPassword          = propertiesConfiguration.getString("db_seeder.postgresql.password");
    postgresqlPasswordSys       = propertiesConfiguration.getString("db_seeder.postgresql.password.sys");
    postgresqlUser              = propertiesConfiguration.getString("db_seeder.postgresql.user");
  }

  private void updatePropertiesFromOs() {

    Map<String, String> environmentVariables = System.getenv();

    if (environmentVariables.containsKey("DB_SEEDER_FILE_CONFIGURATION_NAME")) {
      fileConfigurationName = environmentVariables.get("DB_SEEDER_FILE_CONFIGURATION_NAME");
      propertiesConfiguration.setProperty("db_seeder.file.configuration.name", fileConfigurationName);
    }

    // JDBC Connection ---------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_JDBC_CONNECTION_HOST")) {
      jdbcConnectionHost = environmentVariables.get("DB_SEEDER_JDBC_CONNECTION_HOST");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.host", jdbcConnectionHost);
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

    // Microsoft SQL Server Database ----------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_MSSQLSERVER_CONNECTION_PORT")) {
      mssqlserverConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_MSSQLSERVER_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", mssqlserverConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MSSQLSERVER_CONNECTION_PREFIX")) {
      mssqlserverConnectionPrefix = environmentVariables.get("DB_SEEDER_MSSQLSERVER_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", mssqlserverConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MSSQLSERVER_DATABASE")) {
      mssqlserverDatabase = environmentVariables.get("DB_SEEDER_MSSQLSERVER_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.mssqlserver.database", mssqlserverDatabase);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MSSQLSERVER_PASSWORD")) {
      mssqlserverPassword = environmentVariables.get("DB_SEEDER_MSSQLSERVER_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.mssqlserver.password", mssqlserverPassword);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MSSQLSERVER_PASSWORD_SYS")) {
      mssqlserverPasswordSys = environmentVariables.get("DB_SEEDER_MSSQLSERVER_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.mssqlserver.password.sys", mssqlserverPasswordSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MSSQLSERVER_SCHEMA")) {
      mssqlserverSchema = environmentVariables.get("DB_SEEDER_MSSQLSERVER_SCHEMA");
      propertiesConfiguration.setProperty("db_seeder.mssqlserver.schema", mssqlserverSchema);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MSSQLSERVER_USER")) {
      mssqlserverUser = environmentVariables.get("DB_SEEDER_MSSQLSERVER_USER");
      propertiesConfiguration.setProperty("db_seeder.mssqlserver.user", mssqlserverUser);
    }

    // MySQL Database ----------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_MYSQL_SCHEMA")) {
      mysqlSchema = environmentVariables.get("DB_SEEDER_MYSQL_SCHEMA");
      propertiesConfiguration.setProperty("db_seeder.mysql.schema", mysqlSchema);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MYSQL_CONNECTION_PORT")) {
      mysqlConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_MYSQL_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", mysqlConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MYSQL_CONNECTION_PREFIX")) {
      mysqlConnectionPrefix = environmentVariables.get("DB_SEEDER_MYSQL_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", mysqlConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MYSQL_CONNECTION_SUFFIX")) {
      mysqlConnectionSuffix = environmentVariables.get("DB_SEEDER_MYSQL_CONNECTION_SUFFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.suffix", mysqlConnectionSuffix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MYSQL_PASSWORD")) {
      mysqlPassword = environmentVariables.get("DB_SEEDER_MYSQL_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.mysql.password", mysqlPassword);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MYSQL_PASSWORD_SYS")) {
      mysqlPasswordSys = environmentVariables.get("DB_SEEDER_MYSQL_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.mysql.password.sys", mysqlPasswordSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MYSQL_USER")) {
      mysqlUser = environmentVariables.get("DB_SEEDER_MYSQL_USER");
      propertiesConfiguration.setProperty("db_seeder.mysql.user", mysqlUser);
    }

    // Oracle Database ---------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_ORACLE_CONNECTION_PORT")) {
      oracleConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_ORACLE_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", oracleConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_ORACLE_CONNECTION_PREFIX")) {
      oracleConnectionPrefix = environmentVariables.get("DB_SEEDER_ORACLE_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", oracleConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_ORACLE_CONNECTION_SERVICE")) {
      oracleConnectionService = environmentVariables.get("DB_SEEDER_ORACLE_CONNECTION_SERVICE");
      propertiesConfiguration.setProperty("db_seeder.oracle.connection.service", oracleConnectionService);
    }

    if (environmentVariables.containsKey("DB_SEEDER_ORACLE_PASSWORD")) {
      oraclePassword = environmentVariables.get("DB_SEEDER_ORACLE_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.oracle.password", oraclePassword);
    }

    if (environmentVariables.containsKey("DB_SEEDER_ORACLE_PASSWORD_SYS")) {
      oraclePasswordSys = environmentVariables.get("DB_SEEDER_ORACLE_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.oracle.password.sys", oraclePasswordSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_ORACLE_USER")) {
      oracleUser = environmentVariables.get("DB_SEEDER_ORACLE_USER");
      propertiesConfiguration.setProperty("db_seeder.oracle.user", oracleUser);
    }

    // PostgreSQL Database Database ----------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_POSTGRESQL_CONNECTION_PORT")) {
      postgresqlConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_POSTGRESQL_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", postgresqlConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX")) {
      postgresqlConnectionPrefix = environmentVariables.get("DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", postgresqlConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_POSTGRESQL_DATABASE")) {
      postgresqlDatabase = environmentVariables.get("DB_SEEDER_POSTGRESQL_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.postgresql.database", postgresqlDatabase);
    }

    if (environmentVariables.containsKey("DB_SEEDER_POSTGRESQL_PASSWORD")) {
      postgresqlPassword = environmentVariables.get("DB_SEEDER_POSTGRESQL_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.postgresql.password", postgresqlPassword);
    }

    if (environmentVariables.containsKey("DB_SEEDER_POSTGRESQL_PASSWORD_SYS")) {
      postgresqlPasswordSys = environmentVariables.get("DB_SEEDER_POSTGRESQL_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.postgresql.password.sys", postgresqlPasswordSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_POSTGRESQL_USER")) {
      postgresqlUser = environmentVariables.get("DB_SEEDER_POSTGRESQL_USER");
      propertiesConfiguration.setProperty("db_seeder.postgresql.user", postgresqlUser);
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
