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

  private int                                                          cratedbConnectionPort;
  private String                                                       cratedbConnectionPrefix;
  private String                                                       cratedbPassword;
  private String                                                       cratedbUser;
  private String                                                       cratedbUserSys;

  private int                                                          cubridConnectionPort;
  private String                                                       cubridConnectionPrefix;
  private String                                                       cubridConnectionSuffix;
  private String                                                       cubridDatabase;
  private String                                                       cubridPassword;
  private String                                                       cubridUser;
  private String                                                       cubridUserSys;

  private int                                                          derbyConnectionPort;
  private String                                                       derbyConnectionPrefix;
  private String                                                       derbyDatabase;

  private boolean                                                      encodingIso_8859_1;
  private boolean                                                      encodingUtf_8;

  private final FileBasedConfigurationBuilder<PropertiesConfiguration> fileBasedConfigurationBuilder;
  private String                                                       fileConfigurationName;
  private String                                                       fileStatisticsDelimiter;
  private String                                                       fileStatisticsHeader;
  private String                                                       fileStatisticsName;

  private int                                                          firebirdConnectionPort;
  private String                                                       firebirdConnectionPrefix;
  private String                                                       firebirdConnectionSuffix;
  private String                                                       firebirdDatabase;
  private String                                                       firebirdPassword;
  private String                                                       firebirdPasswordSys;
  private String                                                       firebirdUser;
  private String                                                       firebirdUserSys;

  private int                                                          h2ConnectionPort;
  private String                                                       h2ConnectionPrefix;
  private String                                                       h2Database;
  private String                                                       h2Password;
  private String                                                       h2Schema;
  private String                                                       h2User;

  private int                                                          hsqldbConnectionPort;
  private String                                                       hsqldbConnectionPrefix;
  private String                                                       hsqldbConnectionSuffix;
  private String                                                       hsqldbDatabase;
  private String                                                       hsqldbPassword;
  private String                                                       hsqldbSchema;
  private String                                                       hsqldbUser;
  private String                                                       hsqldbUserSys;

  private int                                                          ibmdb2ConnectionPort;
  private String                                                       ibmdb2ConnectionPrefix;
  private String                                                       ibmdb2Database;
  private String                                                       ibmdb2PasswordSys;
  private String                                                       ibmdb2Schema;
  private String                                                       ibmdb2UserSys;

  private String                                                       informixConnectionPrefix;
  private String                                                       informixConnectionSuffix;
  private String                                                       informixDatabase;
  private String                                                       informixDatabaseSys;
  private String                                                       informixPasswordSys;
  private String                                                       informixUserSys;
  private int                                                          informixConnectionPort;

  private String                                                       jdbcConnectionHost;

  private ArrayList<String>                                            keysSorted = new ArrayList<>();

  private int                                                          mariadbConnectionPort;
  private String                                                       mariadbConnectionPrefix;
  private String                                                       mariadbDatabase;
  private String                                                       mariadbDatabaseSys;
  private String                                                       mariadbPassword;
  private String                                                       mariadbPasswordSys;
  private String                                                       mariadbUser;
  private String                                                       mariadbUserSys;

  private int                                                          maxRowCity;
  private int                                                          maxRowCompany;
  private int                                                          maxRowCountry;
  private int                                                          maxRowCountryState;
  private int                                                          maxRowTimezone;

  private int                                                          mimerConnectionPort;
  private String                                                       mimerConnectionPrefix;
  private String                                                       mimerDatabase;
  private String                                                       mimerDatabaseSys;
  private String                                                       mimerPassword;
  private String                                                       mimerPasswordSys;
  private String                                                       mimerUser;
  private String                                                       mimerUserSys;

  private int                                                          mssqlserverConnectionPort;
  private String                                                       mssqlserverConnectionPrefix;
  private String                                                       mssqlserverDatabase;
  private String                                                       mssqlserverDatabaseSys;
  private String                                                       mssqlserverPassword;
  private String                                                       mssqlserverPasswordSys;
  private String                                                       mssqlserverSchema;
  private String                                                       mssqlserverUser;
  private String                                                       mssqlserverUserSys;

  private int                                                          mysqlConnectionPort;
  private String                                                       mysqlConnectionPrefix;
  private String                                                       mysqlConnectionSuffix;
  private String                                                       mysqlDatabase;
  private String                                                       mysqlDatabaseSys;
  private String                                                       mysqlPassword;
  private String                                                       mysqlPasswordSys;
  private String                                                       mysqlUser;
  private String                                                       mysqlUserSys;

  private int                                                          oracleConnectionPort;
  private String                                                       oracleConnectionPrefix;
  private String                                                       oracleConnectionService;
  private String                                                       oraclePassword;
  private String                                                       oraclePasswordSys;
  private String                                                       oracleUser;
  private String                                                       oracleUserSys;

  private int                                                          postgresqlConnectionPort;
  private String                                                       postgresqlConnectionPrefix;
  private String                                                       postgresqlDatabase;
  private String                                                       postgresqlDatabaseSys;
  private String                                                       postgresqlPassword;
  private String                                                       postgresqlPasswordSys;
  private String                                                       postgresqlUser;
  private String                                                       postgresqlUserSys;

  private PropertiesConfiguration                                      propertiesConfiguration;

  private String                                                       sqliteConnectionPrefix;
  private String                                                       sqliteDatabase;

  /**
   * Initialises a new configuration object.
   */
  public Config() {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

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

    logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  // Apache Derby ------------------------------------------------------------

  /**
   * @return the Apache Derby port number where the database server is listening for requests
   */
  public final int getApachederbyConnectionPort() {
    return derbyConnectionPort;
  }

  /**
   * @return the prefix of the Apache Derby connection string
   */
  public final String getApachederbyConnectionPrefix() {
    return derbyConnectionPrefix;
  }

  /**
   * @return the IBM Db2 Database name of the normal database
   */
  public final String getApachederbyDatabase() {
    return derbyDatabase;
  }

  @SuppressWarnings("unused")
  private final List<String> getBooleanProperties() {

    List<String> list = new ArrayList<>();

    list.add("db_seeder.encoding.iso_8859_1");
    list.add("db_seeder.encoding.utf_8");

    return list;
  }

  // CrateDB ----------------------------------------------------------

  /**
   * @return the CrateDB port number where the database server is listening for requests
   */
  public final int getCratedbConnectionPort() {
    return cratedbConnectionPort;
  }

  /**
   * @return the prefix of the CrateDB connection string
   */
  public final String getCratedbConnectionPrefix() {
    return cratedbConnectionPrefix;
  }

  /**
   * @return the CrateDB password to connect as normal user to the database
   */
  public final String getCratedbPassword() {
    return cratedbPassword;
  }

  /**
   * @return the CrateDB user name to connect as normal user to the database
   */
  public final String getCratedbUser() {
    return cratedbUser;
  }

  /**
   * @return the CrateDB user name to connect as privileged user to the database
   */
  public final String getCratedbUserSys() {
    return cratedbUserSys;
  }

  // CUBRID ------------------------------------------------------------------

  /**
   * @return the CUBRID port number where the database server is listening for requests
   */
  public final int getCubridConnectionPort() {
    return cubridConnectionPort;
  }

  /**
   * @return the prefix of the CUBRID connection string
   */
  public final String getCubridConnectionPrefix() {
    return cubridConnectionPrefix;
  }

  /**
   * @return the suffix of the CUBRID connection string
   */
  public final String getCubridConnectionSuffix() {
    return cubridConnectionSuffix;
  }

  /**
   * @return the CUBRID name of the normal database
   */
  public final String getCubridDatabase() {
    return cubridDatabase;
  }

  /**
   * @return the CUBRID  password to connect as normal user to the database
   */
  public final String getCubridPassword() {
    return cubridPassword;
  }

  /**
   * @return the CUBRID  user name to connect as normal user to the database
   */
  public final String getCubridUser() {
    return cubridUser.toUpperCase();
  }

  /**
   * @return the CUBRID  user name to connect as privileged user to the database
   */
  public final String getCubridUserSys() {
    return cubridUserSys.toUpperCase();
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

  // File Statistics ---------------------------------------------------------

  /**
   * @return the file statistics delimiter
   */
  public final String getFileStatisticsDelimiter() {
    return fileStatisticsDelimiter;
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

  // Firebird ----------------------------------------------------------------

  /**
   * @return the Firebird port number where the database server is listening for requests
   */
  public final int getFirebirdConnectionPort() {
    return firebirdConnectionPort;
  }

  /**
   * @return the prefix of the Firebird connection string
   */
  public final String getFirebirdConnectionPrefix() {
    return firebirdConnectionPrefix;
  }

  /**
   * @return the suffix of the Firebird connection string
   */
  public final String getFirebirdConnectionSuffix() {
    return firebirdConnectionSuffix;
  }

  /**
   * @return the Firebird name of the normal database
   */
  public final String getFirebirdDatabase() {
    return firebirdDatabase;
  }

  /**
   * @return the Firebird password to connect as normal user to the database
   */
  public final String getFirebirdPassword() {
    return firebirdPassword;
  }

  /**
   * @return the Firebird password to connect as privileged user to the database
   */
  public final String getFirebirdPasswordSys() {
    return firebirdPasswordSys;
  }

  /**
   * @return the MariaDB user name to connect as normal user to the database
   */
  public final String getFirebirdUser() {
    return firebirdUser.toUpperCase();
  }

  /**
   * @return the MariaDB user name to connect as privileged user to the database
   */
  public final String getFirebirdUserSys() {
    return firebirdUserSys.toUpperCase();
  }

  // H2 Database Engine-------------------------------------------------------

  /**
   * @return the H2 Database Engine port number where the database server is listening for requests
   */
  public final int getH2ConnectionPort() {
    return h2ConnectionPort;
  }

  /**
   * @return the prefix of the H2 Database Engine connection string
   */
  public final String getH2ConnectionPrefix() {
    return h2ConnectionPrefix;
  }

  /**
   * @return the H2 Database Engine name of the normal database
   */
  public final String getH2Database() {
    return h2Database;
  }

  /**
   * @return the H2 Database Engine password to connect as normal user to the database
   */
  public final String getH2Password() {
    return h2Password;
  }

  /**
   * @return the H2 Database Engine schema
   */
  public final String getH2Schema() {
    return h2Schema.toUpperCase();
  }

  /**
   * @return the H2 Database Engine user name to connect as normal user to the database
   */
  public final String getH2User() {
    return h2User.toUpperCase();
  }

  // HyperSQL Database--------------------------------------------------------

  /**
   * @return the HyperSQL Database port number where the database server is listening for requests
   */
  public final int getHsqldbConnectionPort() {
    return hsqldbConnectionPort;
  }

  /**
   * @return the prefix of the HyperSQL Database connection string
   */
  public final String getHsqldbConnectionPrefix() {
    return hsqldbConnectionPrefix;
  }

  /**
   * @return the suffix of the HyperSQL Database connection string
   */
  public final String getHsqldbConnectionSuffix() {
    return hsqldbConnectionSuffix;
  }

  /**
   * @return the HyperSQL Database name of the normal database
   */
  public final String getHsqldbDatabase() {
    return hsqldbDatabase;
  }

  /**
   * @return the HyperSQL Database password to connect as normal user to the database
   */
  public final String getHsqldbPassword() {
    return hsqldbPassword;
  }

  /**
   * @return the HyperSQL Database schema
   */
  public final String getHsqldbSchema() {
    return hsqldbSchema.toUpperCase();
  }

  /**
   * @return the HyperSQL Database user name to connect as normal user to the database
   */
  public final String getHsqldbUser() {
    return hsqldbUser.toUpperCase();
  }

  /**
   * @return the HyperSQL Database user name to connect as privileged user to the database
   */
  public final String getHsqldbUserSys() {
    return hsqldbUserSys.toUpperCase();
  }

  // IBM Db2 Database --------------------------------------------------------

  /**
   * @return the IBM Db2 port number where the database server is listening for requests
   */
  public final int getIbmdb2ConnectionPort() {
    return ibmdb2ConnectionPort;
  }

  /**
   * @return the prefix of the IBM Db2 connection string
   */
  public final String getIbmdb2ConnectionPrefix() {
    return ibmdb2ConnectionPrefix;
  }

  /**
   * @return the IBM Db2 Database name of the normal database
   */
  public final String getIbmdb2Database() {
    return ibmdb2Database;
  }

  /**
   * @return the IBM Db2 password to connect as privileged user to the database
   */
  public final String getIbmdb2PasswordSys() {
    return ibmdb2PasswordSys;
  }

  /**
   * @return the IBM Db2 schema name
   */
  public final String getIbmdb2Schema() {
    return ibmdb2Schema.toUpperCase();
  }

  /**
   * @return the IBM Db2 user name to connect as privileged user to the database
   */
  public final String getIbmdb2UserSys() {
    return ibmdb2UserSys;
  }

  // IBM Informix ------------------------------------------------------------

  /**
   * @return the IBM Informix port number where the database server is listening for requests
   */
  public final int getInformixConnectionPort() {
    return informixConnectionPort;
  }

  /**
   * @return the prefix of the IBM Informix connection string
   */
  public final String getInformixConnectionPrefix() {
    return informixConnectionPrefix;
  }

  /**
   * @return the suffix of the IBM Informix connection string
   */
  public final String getInformixConnectionSuffix() {
    return informixConnectionSuffix;
  }

  /**
   * @return the IBM Informix name of the normal database
   */
  public final String getInformixDatabase() {
    return informixDatabase;
  }

  /**
   * @return the IBM Informix name of the privileged database
   */
  public final String getInformixDatabaseSys() {
    return informixDatabaseSys;
  }

  /**
   * @return the IBM Informix password of the privileged user
   */
  public final String getInformixPasswordSys() {
    return informixPasswordSys;
  }

  /**
   * @return the IBM Informix user name of the privileged user
   */
  public final String getInformixUserSys() {
    return informixUserSys;
  }

  // JDBC Connection ---------------------------------------------------------

  /**
   * @return the host name or the IP address of the database
   */
  public final String getJdbcConnectionHost() {
    return jdbcConnectionHost;
  }

  private final ArrayList<String> getKeysSorted() {

    for (final Iterator<String> iterator = propertiesConfiguration.getKeys(); iterator.hasNext();) {
      keysSorted.add(iterator.next());
    }

    Collections.sort(keysSorted);

    return keysSorted;
  }

  // MariaDB Server ----------------------------------------------------------

  /**
   * @return the MariaDB port number where the database server is listening for requests
   */
  public final int getMariadbConnectionPort() {
    return mariadbConnectionPort;
  }

  /**
   * @return the prefix of the MariaDB connection string
   */
  public final String getMariadbConnectionPrefix() {
    return mariadbConnectionPrefix;
  }

  /**
   * @return the MariaDB database name of the normal database
   */
  public final String getMariadbDatabase() {
    return mariadbDatabase;
  }

  /**
   * @return the MariaDB database name of the privileged database
   */
  public final String getMariadbDatabaseSys() {
    return mariadbDatabaseSys;
  }

  /**
   * @return the MariaDB password to connect as normal user to the database
   */
  public final String getMariadbPassword() {
    return mariadbPassword;
  }

  /**
   * @return the MariaDB password to connect as privileged user to the database
   */
  public final String getMariadbPasswordSys() {
    return mariadbPasswordSys;
  }

  /**
   * @return the MariaDB user name to connect as normal user to the database
   */
  public final String getMariadbUser() {
    return mariadbUser;
  }

  /**
   * @return the MariaDB user name to connect as privileged user to the database
   */
  public final String getMariadbUserSys() {
    return mariadbUserSys;
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

  // Mimer SQL ---------------------------------------------------------------

  /**
   * @return the Mimer SQL port number where the database server is listening for requests
   */
  public final int getMimerConnectionPort() {
    return mimerConnectionPort;
  }

  /**
   * @return the prefix of the Mimer SQL connection string
   */
  public final String getMimerConnectionPrefix() {
    return mimerConnectionPrefix;
  }

  /**
   * @return the Mimer SQL database name of the normal database
   */
  public final String getMimerDatabase() {
    return mimerDatabase;
  }

  /**
   * @return the Mimer SQL database name of the privileged database
   */
  public final String getMimerDatabaseSys() {
    return mimerDatabaseSys;
  }

  /**
   * @return the Mimer SQL password to connect as normal user to the database
   */
  public final String getMimerPassword() {
    return mimerPassword;
  }

  /**
   * @return the Mimer SQL password to connect as privileged user to the database
   */
  public final String getMimerPasswordSys() {
    return mimerPasswordSys;
  }

  /**
   * @return the Mimer SQL user name to connect as normal user to the database
   */
  public final String getMimerUser() {
    return mimerUser;
  }

  /**
   * @return the Mimer SQL user name to connect as privileged user to the database
   */
  public final String getMimerUserSys() {
    return mimerUserSys;
  }

  // Microsoft SQL Server ----------------------------------------------------

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
   * @return the Microsoft SQL Server database name of the normal database
   */
  public final String getMssqlserverDatabase() {
    return mssqlserverDatabase;
  }

  /**
   * @return the Microsoft SQL Server database name of the privileged database
   */
  public final String getMssqlserverDatabaseSys() {
    return mssqlserverDatabaseSys;
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
   * @return the Microsoft SQL Server user name to connect as privileged user to the database
   */
  public final String getMssqlserverUserSys() {
    return mssqlserverUserSys;
  }

  // MySQL Database ----------------------------------------------------------

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
   * @return the MySQL database name of the normal database
   */
  public final String getMysqlDatabase() {
    return mysqlDatabase;
  }

  /**
   * @return the MySQL database name of the privileged database
   */
  public final String getMysqlDatabaseSys() {
    return mysqlDatabaseSys;
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
   * @return the MySQL user name to connect as normal user to the database
   */
  public final String getMysqlUser() {
    return mysqlUser;
  }

  /**
   * @return the MySQL user name to connect as privileged user to the database
   */
  public final String getMysqlUserSys() {
    return mysqlUserSys;
  }

  @SuppressWarnings("unused")
  private final List<String> getNumericProperties() {

    List<String> list = new ArrayList<>();

    list.add("db_seeder.derby.connection.port");
    list.add("db_seeder.cratedb.connection.port");
    list.add("db_seeder.cubrid.connection.port");
    list.add("db_seeder.firebird.connection.port");
    list.add("db_seeder.h2.connection.port");
    list.add("db_seeder.hsqldb.connection.port");
    list.add("db_seeder.ibmdb2.connection.port");
    list.add("db_seeder.informix.connection.port");
    list.add("db_seeder.mariadb.connection.port");
    list.add("db_seeder.mimer.connection.port");
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

  // Oracle Database ---------------------------------------------------------

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
   * @return the Oracle user name to connect as privileged user to the database
   */
  public final String getOracleUserSys() {
    return oracleUserSys.toUpperCase();
  }

  // PostgreSQL Database -----------------------------------------------------

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
   * @return the PostgreSQL Database name of the normal database
   */
  public final String getPostgresqlDatabase() {
    return postgresqlDatabase;
  }

  /**
   * @return the PostgreSQL Database name of the privileged database
   */
  public final String getPostgresqlDatabaseSys() {
    return postgresqlDatabaseSys;
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

  /**
   * @return the PostgreSQL Database user name to connect as privileged user to the database
   */
  public final String getPostgresqlUserSys() {
    return postgresqlUserSys;
  }

  // SQLite ---------------------------------------------------------

  /**
   * @return the prefix of the SQLite connection string
   */
  public final String getSQLiteConnectionPrefix() {
    return sqliteConnectionPrefix;
  }

  /**
   * @return the SQLite database name of the normal database
   */
  public final String getSQLiteDatabase() {
    return sqliteDatabase;
  }

  private final void storeConfiguration() {

    propertiesConfiguration.setThrowExceptionOnMissing(true);

    cratedbConnectionPort       = propertiesConfiguration.getInt("db_seeder.cratedb.connection.port");
    cratedbConnectionPrefix     = propertiesConfiguration.getString("db_seeder.cratedb.connection.prefix");
    cratedbPassword             = propertiesConfiguration.getString("db_seeder.cratedb.password");
    cratedbUser                 = propertiesConfiguration.getString("db_seeder.cratedb.user");
    cratedbUserSys              = propertiesConfiguration.getString("db_seeder.cratedb.user.sys");

    cubridConnectionPort        = propertiesConfiguration.getInt("db_seeder.cubrid.connection.port");
    cubridConnectionPrefix      = propertiesConfiguration.getString("db_seeder.cubrid.connection.prefix");
    cubridConnectionSuffix      = propertiesConfiguration.getString("db_seeder.cubrid.connection.suffix");
    cubridDatabase              = propertiesConfiguration.getString("db_seeder.cubrid.database");
    cubridPassword              = propertiesConfiguration.getString("db_seeder.cubrid.password");
    cubridUser                  = propertiesConfiguration.getString("db_seeder.cubrid.user");
    cubridUserSys               = propertiesConfiguration.getString("db_seeder.cubrid.user.sys");

    derbyConnectionPort         = propertiesConfiguration.getInt("db_seeder.derby.connection.port");
    derbyConnectionPrefix       = propertiesConfiguration.getString("db_seeder.derby.connection.prefix");
    derbyDatabase               = propertiesConfiguration.getString("db_seeder.derby.database");

    encodingIso_8859_1          = propertiesConfiguration.getBoolean("db_seeder.encoding.iso_8859_1");
    encodingUtf_8               = propertiesConfiguration.getBoolean("db_seeder.encoding.utf_8");

    fileConfigurationName       = propertiesConfiguration.getString("db_seeder.file.configuration.name");
    fileStatisticsDelimiter     = propertiesConfiguration.getString("db_seeder.file.statistics.delimiter");
    fileStatisticsHeader        = propertiesConfiguration.getString("db_seeder.file.statistics.header").replace(";", fileStatisticsDelimiter);
    fileStatisticsName          = propertiesConfiguration.getString("db_seeder.file.statistics.name");

    firebirdConnectionPort      = propertiesConfiguration.getInt("db_seeder.firebird.connection.port");
    firebirdConnectionPrefix    = propertiesConfiguration.getString("db_seeder.firebird.connection.prefix");
    firebirdConnectionSuffix    = propertiesConfiguration.getString("db_seeder.firebird.connection.suffix");
    firebirdDatabase            = propertiesConfiguration.getString("db_seeder.firebird.database");
    firebirdPassword            = propertiesConfiguration.getString("db_seeder.firebird.password");
    firebirdPasswordSys         = propertiesConfiguration.getString("db_seeder.firebird.password.sys");
    firebirdUser                = propertiesConfiguration.getString("db_seeder.firebird.user");
    firebirdUserSys             = propertiesConfiguration.getString("db_seeder.firebird.user.sys");

    h2ConnectionPort            = propertiesConfiguration.getInt("db_seeder.h2.connection.port");
    h2ConnectionPrefix          = propertiesConfiguration.getString("db_seeder.h2.connection.prefix");
    h2Database                  = propertiesConfiguration.getString("db_seeder.h2.database");
    h2Password                  = propertiesConfiguration.getString("db_seeder.h2.password");
    h2Schema                    = propertiesConfiguration.getString("db_seeder.h2.schema");
    h2User                      = propertiesConfiguration.getString("db_seeder.h2.user");

    hsqldbConnectionPort        = propertiesConfiguration.getInt("db_seeder.hsqldb.connection.port");
    hsqldbConnectionPrefix      = propertiesConfiguration.getString("db_seeder.hsqldb.connection.prefix");
    hsqldbConnectionSuffix      = propertiesConfiguration.getString("db_seeder.hsqldb.connection.suffix");
    hsqldbDatabase              = propertiesConfiguration.getString("db_seeder.hsqldb.database");
    hsqldbPassword              = propertiesConfiguration.getString("db_seeder.hsqldb.password");
    hsqldbSchema                = propertiesConfiguration.getString("db_seeder.hsqldb.schema");
    hsqldbUser                  = propertiesConfiguration.getString("db_seeder.hsqldb.user");
    hsqldbUserSys               = propertiesConfiguration.getString("db_seeder.hsqldb.user.sys");

    ibmdb2ConnectionPort        = propertiesConfiguration.getInt("db_seeder.ibmdb2.connection.port");
    ibmdb2ConnectionPrefix      = propertiesConfiguration.getString("db_seeder.ibmdb2.connection.prefix");
    ibmdb2Database              = propertiesConfiguration.getString("db_seeder.ibmdb2.database");
    ibmdb2PasswordSys           = propertiesConfiguration.getString("db_seeder.ibmdb2.password.sys");
    ibmdb2Schema                = propertiesConfiguration.getString("db_seeder.ibmdb2.schema");
    ibmdb2UserSys               = propertiesConfiguration.getString("db_seeder.ibmdb2.user.sys");

    informixConnectionPort      = propertiesConfiguration.getInt("db_seeder.informix.connection.port");
    informixConnectionPrefix    = propertiesConfiguration.getString("db_seeder.informix.connection.prefix");
    informixConnectionSuffix    = propertiesConfiguration.getString("db_seeder.informix.connection.suffix");
    informixDatabase            = propertiesConfiguration.getString("db_seeder.informix.database");
    informixDatabaseSys         = propertiesConfiguration.getString("db_seeder.informix.database.sys");
    informixPasswordSys         = propertiesConfiguration.getString("db_seeder.informix.password.sys");
    informixUserSys             = propertiesConfiguration.getString("db_seeder.informix.user.sys");

    jdbcConnectionHost          = propertiesConfiguration.getString("db_seeder.jdbc.connection.host");

    mariadbConnectionPort       = propertiesConfiguration.getInt("db_seeder.mariadb.connection.port");
    mariadbConnectionPrefix     = propertiesConfiguration.getString("db_seeder.mariadb.connection.prefix");
    mariadbDatabase             = propertiesConfiguration.getString("db_seeder.mariadb.database");
    mariadbDatabaseSys          = propertiesConfiguration.getString("db_seeder.mariadb.database.sys");
    mariadbPassword             = propertiesConfiguration.getString("db_seeder.mariadb.password");
    mariadbPasswordSys          = propertiesConfiguration.getString("db_seeder.mariadb.password.sys");
    mariadbUser                 = propertiesConfiguration.getString("db_seeder.mariadb.user");
    mariadbUserSys              = propertiesConfiguration.getString("db_seeder.mariadb.user.sys");

    maxRowCity                  = propertiesConfiguration.getInt("db_seeder.max.row.city");
    maxRowCompany               = propertiesConfiguration.getInt("db_seeder.max.row.company");
    maxRowCountry               = propertiesConfiguration.getInt("db_seeder.max.row.country");
    maxRowCountryState          = propertiesConfiguration.getInt("db_seeder.max.row.country_state");
    maxRowTimezone              = propertiesConfiguration.getInt("db_seeder.max.row.timezone");

    mimerConnectionPort         = propertiesConfiguration.getInt("db_seeder.mimer.connection.port");
    mimerConnectionPrefix       = propertiesConfiguration.getString("db_seeder.mimer.connection.prefix");
    mimerDatabase               = propertiesConfiguration.getString("db_seeder.mimer.database");
    mimerDatabaseSys            = propertiesConfiguration.getString("db_seeder.mimer.database.sys");
    mimerPassword               = propertiesConfiguration.getString("db_seeder.mimer.password");
    mimerPasswordSys            = propertiesConfiguration.getString("db_seeder.mimer.password.sys");
    mimerUser                   = propertiesConfiguration.getString("db_seeder.mimer.user");
    mimerUserSys                = propertiesConfiguration.getString("db_seeder.mimer.user.sys");

    mssqlserverConnectionPort   = propertiesConfiguration.getInt("db_seeder.mssqlserver.connection.port");
    mssqlserverConnectionPrefix = propertiesConfiguration.getString("db_seeder.mssqlserver.connection.prefix");
    mssqlserverDatabase         = propertiesConfiguration.getString("db_seeder.mssqlserver.database");
    mssqlserverDatabaseSys      = propertiesConfiguration.getString("db_seeder.mssqlserver.database.sys");
    mssqlserverPassword         = propertiesConfiguration.getString("db_seeder.mssqlserver.password");
    mssqlserverPasswordSys      = propertiesConfiguration.getString("db_seeder.mssqlserver.password.sys");
    mssqlserverSchema           = propertiesConfiguration.getString("db_seeder.mssqlserver.schema");
    mssqlserverUser             = propertiesConfiguration.getString("db_seeder.mssqlserver.user");
    mssqlserverUserSys          = propertiesConfiguration.getString("db_seeder.mssqlserver.user.sys");

    mysqlConnectionPort         = propertiesConfiguration.getInt("db_seeder.mysql.connection.port");
    mysqlConnectionPrefix       = propertiesConfiguration.getString("db_seeder.mysql.connection.prefix");
    mysqlConnectionSuffix       = propertiesConfiguration.getString("db_seeder.mysql.connection.suffix");
    mysqlDatabase               = propertiesConfiguration.getString("db_seeder.mysql.database");
    mysqlDatabaseSys            = propertiesConfiguration.getString("db_seeder.mysql.database.sys");
    mysqlPassword               = propertiesConfiguration.getString("db_seeder.mysql.password");
    mysqlPasswordSys            = propertiesConfiguration.getString("db_seeder.mysql.password.sys");
    mysqlUser                   = propertiesConfiguration.getString("db_seeder.mysql.user");
    mysqlUserSys                = propertiesConfiguration.getString("db_seeder.mysql.user.sys");

    oracleConnectionPort        = propertiesConfiguration.getInt("db_seeder.oracle.connection.port");
    oracleConnectionPrefix      = propertiesConfiguration.getString("db_seeder.oracle.connection.prefix");
    oracleConnectionService     = propertiesConfiguration.getString("db_seeder.oracle.connection.service");
    oraclePassword              = propertiesConfiguration.getString("db_seeder.oracle.password");
    oraclePasswordSys           = propertiesConfiguration.getString("db_seeder.oracle.password.sys");
    oracleUser                  = propertiesConfiguration.getString("db_seeder.oracle.user");
    oracleUserSys               = propertiesConfiguration.getString("db_seeder.oracle.user.sys");

    postgresqlConnectionPort    = propertiesConfiguration.getInt("db_seeder.postgresql.connection.port");
    postgresqlConnectionPrefix  = propertiesConfiguration.getString("db_seeder.postgresql.connection.prefix");
    postgresqlDatabase          = propertiesConfiguration.getString("db_seeder.postgresql.database");
    postgresqlDatabaseSys       = propertiesConfiguration.getString("db_seeder.postgresql.database.sys");
    postgresqlPassword          = propertiesConfiguration.getString("db_seeder.postgresql.password");
    postgresqlPasswordSys       = propertiesConfiguration.getString("db_seeder.postgresql.password.sys");
    postgresqlUser              = propertiesConfiguration.getString("db_seeder.postgresql.user");
    postgresqlUserSys           = propertiesConfiguration.getString("db_seeder.postgresql.user.sys");

    sqliteConnectionPrefix      = propertiesConfiguration.getString("db_seeder.sqlite.connection.prefix");
    sqliteDatabase              = propertiesConfiguration.getString("db_seeder.sqlite.database");
  }

  private final void updatePropertiesFromOs() {

    Map<String, String> environmentVariables = System.getenv();

    // Apache Derby ------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_DERBY_CONNECTION_PORT")) {
      derbyConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_DERBY_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", derbyConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_DERBY_CONNECTION_PREFIX")) {
      derbyConnectionPrefix = environmentVariables.get("DB_SEEDER_DERBY_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", derbyConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_DERBY_DATABASE")) {
      derbyDatabase = environmentVariables.get("DB_SEEDER_DERBY_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.derby.database", derbyDatabase);
    }

    // CrateDB ----------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_CRATEDB_CONNECTION_PORT")) {
      cratedbConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_CRATEDB_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", cratedbConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CRATEDB_CONNECTION_PREFIX")) {
      cratedbConnectionPrefix = environmentVariables.get("DB_SEEDER_CRATEDB_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", cratedbConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CRATEDB_PASSWORD")) {
      cratedbPassword = environmentVariables.get("DB_SEEDER_CRATEDB_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.cratedb.password", cratedbPassword);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CRATEDB_USER")) {
      cratedbUser = environmentVariables.get("DB_SEEDER_CRATEDB_USER");
      propertiesConfiguration.setProperty("db_seeder.cratedb.user", cratedbUser);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CRATEDB_USER_SYS")) {
      cratedbUserSys = environmentVariables.get("DB_SEEDER_CRATEDB_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.cratedb.user.sys", cratedbUserSys);
    }

    // --------------------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_FILE_CONFIGURATION_NAME")) {
      fileConfigurationName = environmentVariables.get("DB_SEEDER_FILE_CONFIGURATION_NAME");
      propertiesConfiguration.setProperty("db_seeder.file.configuration.name", fileConfigurationName);
    }

    // CUBRID ------------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_CUBRID_CONNECTION_PORT")) {
      cubridConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_CUBRID_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", cubridConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CUBRID_CONNECTION_PREFIX")) {
      cubridConnectionPrefix = environmentVariables.get("DB_SEEDER_CUBRID_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", cubridConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CUBRID_CONNECTION_SUFFIX")) {
      cubridConnectionSuffix = environmentVariables.get("DB_SEEDER_CUBRID_CONNECTION_SUFFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.suffix", cubridConnectionSuffix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CUBRID_DATABASE")) {
      cubridDatabase = environmentVariables.get("DB_SEEDER_CUBRID_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.cubrid.database", cubridDatabase);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CUBRID_PASSWORD")) {
      cubridPassword = environmentVariables.get("DB_SEEDER_CUBRID_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.cubrid.password", cubridPassword);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CUBRID_USER")) {
      cubridUser = environmentVariables.get("DB_SEEDER_CUBRID_USER");
      propertiesConfiguration.setProperty("db_seeder.cubrid.user", cubridUser);
    }

    if (environmentVariables.containsKey("DB_SEEDER_CUBRID_USER_SYS")) {
      cubridUserSys = environmentVariables.get("DB_SEEDER_CUBRID_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.cubrid.user.sys", cubridUserSys);
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

    // Firebird ------------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_FIREBIRD_CONNECTION_PORT")) {
      firebirdConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_FIREBIRD_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", firebirdConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FIREBIRD_CONNECTION_PREFIX")) {
      firebirdConnectionPrefix = environmentVariables.get("DB_SEEDER_FIREBIRD_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", firebirdConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FIREBIRD_CONNECTION_SUFFIX")) {
      firebirdConnectionSuffix = environmentVariables.get("DB_SEEDER_FIREBIRD_CONNECTION_SUFFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.suffix", firebirdConnectionSuffix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FIREBIRD_DATABASE")) {
      firebirdDatabase = environmentVariables.get("DB_SEEDER_FIREBIRD_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.firebird.database", firebirdDatabase);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FIREBIRD_PASSWORD")) {
      firebirdPassword = environmentVariables.get("DB_SEEDER_FIREBIRD_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.firebird.password", firebirdPassword);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FIREBIRD_PASSWORD_SYS")) {
      firebirdPasswordSys = environmentVariables.get("DB_SEEDER_FIREBIRD_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.firebird.password.sys", firebirdPasswordSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FIREBIRD_USER")) {
      firebirdUser = environmentVariables.get("DB_SEEDER_FIREBIRD_USER");
      propertiesConfiguration.setProperty("db_seeder.firebird.user", firebirdUser);
    }

    if (environmentVariables.containsKey("DB_SEEDER_FIREBIRD_USER_SYS")) {
      firebirdUserSys = environmentVariables.get("DB_SEEDER_FIREBIRD_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.firebird.user.sys", firebirdUserSys);
    }

    // H2 database Engine --------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_H2_CONNECTION_PORT")) {
      h2ConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_H2_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", h2ConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_H2_CONNECTION_PREFIX")) {
      h2ConnectionPrefix = environmentVariables.get("DB_SEEDER_H2_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", h2ConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_H2_DATABASE")) {
      h2Database = environmentVariables.get("DB_SEEDER_H2_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.h2.database", h2Database);
    }

    if (environmentVariables.containsKey("DB_SEEDER_H2_PASSWORD")) {
      h2Password = environmentVariables.get("DB_SEEDER_H2_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.h2.password", h2Password);
    }

    if (environmentVariables.containsKey("DB_SEEDER_H2_SCHEMA")) {
      h2Schema = environmentVariables.get("DB_SEEDER_H2_SCHEMA");
      propertiesConfiguration.setProperty("db_seeder.h2.password.sys", h2Schema);
    }

    if (environmentVariables.containsKey("DB_SEEDER_H2_USER")) {
      h2User = environmentVariables.get("DB_SEEDER_H2_USER");
      propertiesConfiguration.setProperty("db_seeder.h2.user", h2User);
    }

    // HyperSQL Database --------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_HSQLDB_CONNECTION_PORT")) {
      hsqldbConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_HSQLDB_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", hsqldbConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_HSQLDB_CONNECTION_PREFIX")) {
      hsqldbConnectionPrefix = environmentVariables.get("DB_SEEDER_HSQLDB_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", hsqldbConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_HSQLDB_CONNECTION_SUFFIX")) {
      hsqldbConnectionSuffix = environmentVariables.get("DB_SEEDER_HSQLDB_CONNECTION_SUFFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.suffix", hsqldbConnectionSuffix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_HSQLDB_DATABASE")) {
      hsqldbDatabase = environmentVariables.get("DB_SEEDER_HSQLDB_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.hsqldb.database", hsqldbDatabase);
    }

    if (environmentVariables.containsKey("DB_SEEDER_HSQLDB_PASSWORD")) {
      hsqldbPassword = environmentVariables.get("DB_SEEDER_HSQLDB_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.hsqldb.password", hsqldbPassword);
    }

    if (environmentVariables.containsKey("DB_SEEDER_HSQLDB_SCHEMA")) {
      hsqldbSchema = environmentVariables.get("DB_SEEDER_HSQLDB_SCHEMA");
      propertiesConfiguration.setProperty("db_seeder.hsqldb.password.sys", hsqldbSchema);
    }

    if (environmentVariables.containsKey("DB_SEEDER_HSQLDB_USER")) {
      hsqldbUser = environmentVariables.get("DB_SEEDER_HSQLDB_USER");
      propertiesConfiguration.setProperty("db_seeder.hsqldb.user", hsqldbUser);
    }

    if (environmentVariables.containsKey("DB_SEEDER_HSQLDB_USER_SYS")) {
      hsqldbUserSys = environmentVariables.get("DB_SEEDER_HSQLDB_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.hsqldb.user.sys", hsqldbUserSys);
    }

    // IBM Db2 Database ----------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_IBMDB2_CONNECTION_PORT")) {
      ibmdb2ConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_IBMDB2_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", ibmdb2ConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_IBMDB2_CONNECTION_PREFIX")) {
      ibmdb2ConnectionPrefix = environmentVariables.get("DB_SEEDER_IBMDB2_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", ibmdb2ConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_IBMDB2_DATABASE")) {
      ibmdb2Database = environmentVariables.get("DB_SEEDER_IBMDB2_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.ibmdb2.database", ibmdb2Database);
    }

    if (environmentVariables.containsKey("DB_SEEDER_IBMDB2_PASSWORD_SYS")) {
      ibmdb2PasswordSys = environmentVariables.get("DB_SEEDER_IBMDB2_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.ibmdb2.password.sys", ibmdb2PasswordSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_IBMDB2_SCHEMA")) {
      ibmdb2Schema = environmentVariables.get("DB_SEEDER_IBMDB2_SCHEMA");
      propertiesConfiguration.setProperty("db_seeder.ibmdb2.schema", ibmdb2Schema);
    }

    if (environmentVariables.containsKey("DB_SEEDER_IBMDB2_USER_SYS")) {
      ibmdb2UserSys = environmentVariables.get("DB_SEEDER_IBMDB2_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.ibmdb2.user.sys", ibmdb2UserSys);
    }

    // IBM Informix ------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_INFORMIX_CONNECTION_PORT")) {
      informixConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_INFORMIX_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", informixConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_INFORMIX_CONNECTION_PREFIX")) {
      informixConnectionPrefix = environmentVariables.get("DB_SEEDER_INFORMIX_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", informixConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_INFORMIX_CONNECTION_SUFFIX")) {
      informixConnectionSuffix = environmentVariables.get("DB_SEEDER_INFORMIX_CONNECTION_SUFFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.suffix", informixConnectionSuffix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_INFORMIX_DATABASE")) {
      informixDatabase = environmentVariables.get("DB_SEEDER_INFORMIX_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.informix.database", informixDatabase);
    }

    if (environmentVariables.containsKey("DB_SEEDER_INFORMIX_DATABASE_SYS")) {
      informixDatabaseSys = environmentVariables.get("DB_SEEDER_INFORMIX_DATABASE_SYS");
      propertiesConfiguration.setProperty("db_seeder.informix.database.sys", informixDatabase);
    }

    if (environmentVariables.containsKey("DB_SEEDER_INFORMIX_PASSWORD_SYS")) {
      informixPasswordSys = environmentVariables.get("DB_SEEDER_INFORMIX_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.informix.password.sys", informixPasswordSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_INFORMIX_USER_SYS")) {
      informixUserSys = environmentVariables.get("DB_SEEDER_INFORMIX_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.informix.user.sys", informixUserSys);
    }

    // JDBC Connection ---------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_JDBC_CONNECTION_HOST")) {
      jdbcConnectionHost = environmentVariables.get("DB_SEEDER_JDBC_CONNECTION_HOST");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.host", jdbcConnectionHost);
    }

    // MariaDB Server ----------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_MARIADB_CONNECTION_PORT")) {
      mariadbConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_MARIADB_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", mariadbConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MARIADB_CONNECTION_PREFIX")) {
      mariadbConnectionPrefix = environmentVariables.get("DB_SEEDER_MARIADB_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", mariadbConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MARIADB_DATABASE")) {
      mariadbDatabase = environmentVariables.get("DB_SEEDER_MARIADB_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.mariadb.database", mariadbDatabase);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MARIADB_DATABASE_SYS")) {
      mariadbDatabaseSys = environmentVariables.get("DB_SEEDER_MARIADB_DATABASE_SYS");
      propertiesConfiguration.setProperty("db_seeder.mariadb.database.sys", mariadbDatabaseSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MARIADB_PASSWORD")) {
      mariadbPassword = environmentVariables.get("DB_SEEDER_MARIADB_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.mariadb.password", mariadbPassword);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MARIADB_PASSWORD_SYS")) {
      mariadbPasswordSys = environmentVariables.get("DB_SEEDER_MARIADB_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.mariadb.password.sys", mariadbPasswordSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MARIADB_USER")) {
      mariadbUser = environmentVariables.get("DB_SEEDER_MARIADB_USER");
      propertiesConfiguration.setProperty("db_seeder.mariadb.user", mariadbUser);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MARIADB_USER_SYS")) {
      mariadbUserSys = environmentVariables.get("DB_SEEDER_MARIADB_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.mariadb.user.sys", mariadbUserSys);
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

    // Mimer SQL ---------------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_MIMER_CONNECTION_PORT")) {
      mimerConnectionPort = Integer.parseInt(environmentVariables.get("DB_SEEDER_MIMER_CONNECTION_PORT"));
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.port", mimerConnectionPort);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MIMER_CONNECTION_PREFIX")) {
      mimerConnectionPrefix = environmentVariables.get("DB_SEEDER_MIMER_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", mimerConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MIMER_DATABASE")) {
      mimerDatabase = environmentVariables.get("DB_SEEDER_MIMER_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.mimer.database", mimerDatabase);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MIMER_DATABASE_SYS")) {
      mimerDatabaseSys = environmentVariables.get("DB_SEEDER_MIMER_DATABASE_SYS");
      propertiesConfiguration.setProperty("db_seeder.mimer.database.sys", mimerDatabaseSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MIMER_PASSWORD")) {
      mimerPassword = environmentVariables.get("DB_SEEDER_MIMER_PASSWORD");
      propertiesConfiguration.setProperty("db_seeder.mimer.password", mimerPassword);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MIMER_PASSWORD_SYS")) {
      mimerPasswordSys = environmentVariables.get("DB_SEEDER_MIMER_PASSWORD_SYS");
      propertiesConfiguration.setProperty("db_seeder.mimer.password.sys", mimerPasswordSys);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MIMER_USER")) {
      mimerUser = environmentVariables.get("DB_SEEDER_MIMER_USER");
      propertiesConfiguration.setProperty("db_seeder.mimer.user", mimerUser);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MIMER_USER_SYS")) {
      mimerUserSys = environmentVariables.get("DB_SEEDER_MIMER_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.mimer.user.sys", mimerUserSys);
    }

    // Microsoft SQL Server ----------------------------------------------------

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

    if (environmentVariables.containsKey("DB_SEEDER_MSSQLSERVER_DATABASE_SYS")) {
      mssqlserverDatabaseSys = environmentVariables.get("DB_SEEDER_MSSQLSERVER_DATABASE_SYS");
      propertiesConfiguration.setProperty("db_seeder.mssqlserver.database.sys", mssqlserverDatabaseSys);
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

    if (environmentVariables.containsKey("DB_SEEDER_MSSQLSERVER_USER_SYS")) {
      mssqlserverUserSys = environmentVariables.get("DB_SEEDER_MSSQLSERVER_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.mssqlserver.user.sys", mssqlserverUserSys);
    }

    // MySQL Database ----------------------------------------------------------

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

    if (environmentVariables.containsKey("DB_SEEDER_MYSQL_DATABASE")) {
      mysqlDatabase = environmentVariables.get("DB_SEEDER_MYSQL_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.mysql.database", mysqlDatabase);
    }

    if (environmentVariables.containsKey("DB_SEEDER_MYSQL_DATABASE_SYS")) {
      mysqlDatabaseSys = environmentVariables.get("DB_SEEDER_MYSQL_DATABASE_SYS");
      propertiesConfiguration.setProperty("db_seeder.mysql.database.sys", mysqlDatabaseSys);
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

    if (environmentVariables.containsKey("DB_SEEDER_MYSQL_USER_SYS")) {
      mysqlUserSys = environmentVariables.get("DB_SEEDER_MYSQL_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.mysql.user.sys", mysqlUserSys);
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

    if (environmentVariables.containsKey("DB_SEEDER_ORACLE_USER_SYS")) {
      oracleUserSys = environmentVariables.get("DB_SEEDER_ORACLE_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.oracle.user.sys", oracleUserSys);
    }

    // PostgreSQL Database -----------------------------------------------------

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

    if (environmentVariables.containsKey("DB_SEEDER_POSTGRESQL_DATABASE_SYS")) {
      postgresqlDatabaseSys = environmentVariables.get("DB_SEEDER_POSTGRESQL_DATABASE_SYS");
      propertiesConfiguration.setProperty("db_seeder.postgresql.database.sys", postgresqlDatabaseSys);
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

    if (environmentVariables.containsKey("DB_SEEDER_POSTGRESQL_USER_SYS")) {
      postgresqlUserSys = environmentVariables.get("DB_SEEDER_POSTGRESQL_USER_SYS");
      propertiesConfiguration.setProperty("db_seeder.postgresql.user.sys", postgresqlUserSys);
    }

    // SQLite ---------------------------------------------------------

    if (environmentVariables.containsKey("DB_SEEDER_SQLITE_CONNECTION_PREFIX")) {
      sqliteConnectionPrefix = environmentVariables.get("DB_SEEDER_SQLITE_CONNECTION_PREFIX");
      propertiesConfiguration.setProperty("db_seeder.jdbc.connection.prefix", sqliteConnectionPrefix);
    }

    if (environmentVariables.containsKey("DB_SEEDER_SQLITE_DATABASE")) {
      sqliteDatabase = environmentVariables.get("DB_SEEDER_SQLITE_DATABASE");
      propertiesConfiguration.setProperty("db_seeder.sqlite.database", sqliteDatabase);
    }
  }

  private final void validateProperties() {

    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- Start");

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
      }
      logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME, methodName) + "- End");
    }
  }
}
