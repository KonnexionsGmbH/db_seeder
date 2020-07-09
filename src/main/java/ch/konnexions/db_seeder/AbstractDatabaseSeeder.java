/**
 * 
 */
package ch.konnexions.db_seeder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.utils.Config;

/**
 * Test Data Generator for a Database - Abstract Database Seeder.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public abstract class AbstractDatabaseSeeder {

  protected enum Dbms {

    DERBY,
    CRATEDB,
    CUBRID,
    FIREBIRD,
    H2,
    HSQLDB,
    IBMDB2,
    INFORMIX,
    MARIADB,
    MIMER,
    MSSQLSERVER,
    MYSQL,
    ORACLE,
    POSTGRESQL,
    SQLITE
  }

  private static Logger           logger                   = Logger.getLogger(AbstractDatabaseSeeder.class);

  public final static String      FORMAT_IDENTIFIER        = "%-10d";

  // protected final static String   FORMAT_IDENTIFIER_RIGHT  = "%010d";
  public final static String      FORMAT_METHOD_NAME       = "%-25s";
  protected final static String   FORMAT_ROW_NO            = "%1$10d";
  protected final static String   FORMAT_TABLE_NAME        = "%-17s";

  protected int                   autoIncrement;

  protected Config                config;

  protected Dbms                  dbms;
  protected String                dbmsTickerSymbol;
  protected Map<String, String[]> dbmsValues               = null;

  protected boolean               isClient                 = true;
  protected boolean               isEmbedded               = !(isClient);

  protected ArrayList<Object>     pkListCity               = new ArrayList<Object>();
  protected ArrayList<Object>     pkListCountry            = new ArrayList<Object>();
  protected ArrayList<Object>     pkListCountryState       = new ArrayList<Object>();
  protected ArrayList<Object>     pkListTimezone           = new ArrayList<Object>();

  protected final String          TABLE_NAME_CITY          = "CITY";
  protected final String          TABLE_NAME_COMPANY       = "COMPANY";
  protected final String          TABLE_NAME_COUNTRY       = "COUNTRY";
  protected final String          TABLE_NAME_COUNTRY_STATE = "COUNTRY_STATE";
  protected final String          TABLE_NAME_TIMEZONE      = "TIMEZONE";
  protected final List<String>    TABLE_NAMES_CREATE       = Arrays
      .asList(TABLE_NAME_COUNTRY, TABLE_NAME_TIMEZONE, TABLE_NAME_COUNTRY_STATE, TABLE_NAME_CITY, TABLE_NAME_COMPANY);
  protected final List<String>    TABLE_NAMES_DROP         = Arrays
      .asList(TABLE_NAME_COMPANY, TABLE_NAME_CITY, TABLE_NAME_COUNTRY_STATE, TABLE_NAME_COUNTRY, TABLE_NAME_TIMEZONE);

  protected String                tableNameDelimiter       = "tbd";

  /**
   * Initialises a new abstract database seeder object.
   */
  public AbstractDatabaseSeeder() {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    isClient   = true;
    isEmbedded = !(this.isClient);

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- client  =" + isClient);
    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- embedded=" + isEmbedded);

    dbmsValues = initDbmsValues();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  /**
   * Initialises a new abstract database seeder object.
   *
   * @param isClient client database version
   */
  public AbstractDatabaseSeeder(boolean isClient) {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    this.isClient = isClient;
    isEmbedded    = !(this.isClient);

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- client  =" + isClient);
    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- embedded=" + isEmbedded);

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  private final Map<String, String[]> initDbmsValues() {
    Map<String, String[]> dbmsValues = new HashMap<>();

    dbmsValues.put("cratedb",
                   new String[] {
                       "CrateDB",
                       "client" });
    dbmsValues.put("cubris",
                   new String[] {
                       "CUBRID",
                       "client" });
    dbmsValues.put("derby",
                   new String[] {
                       "Apache Derby [client]",
                       "client" });
    dbmsValues.put("derby_emb",
                   new String[] {
                       "Apache Derby [embedded]",
                       "embedded" });
    dbmsValues.put("firebird",
                   new String[] {
                       "Firebird",
                       "client" });
    dbmsValues.put("h2",
                   new String[] {
                       "H2 Database Engine [client]",
                       "client" });
    dbmsValues.put("h2_emb",
                   new String[] {
                       "H2 Database Engine [embedded]",
                       "embedded" });
    dbmsValues.put("hsqldb",
                   new String[] {
                       "HyperSQL Database [client]",
                       "client" });
    dbmsValues.put("hsqldb_emb",
                   new String[] {
                       "HyperSQL Database [embedded]",
                       "embedded" });
    dbmsValues.put("ibmdb2",
                   new String[] {
                       "IBM Db2 Database",
                       "client" });
    dbmsValues.put("informix",
                   new String[] {
                       "IBM Informix",
                       "client" });
    dbmsValues.put("mariadb",
                   new String[] {
                       "MariaDB Server",
                       "client" });
    dbmsValues.put("mimer",
                   new String[] {
                       "Mimer SQL",
                       "client" });
    dbmsValues.put("mssqlserver",
                   new String[] {
                       "MS SQL Server",
                       "client" });
    dbmsValues.put("mysql",
                   new String[] {
                       "MySQL Database",
                       "client" });
    dbmsValues.put("oracle",
                   new String[] {
                       "Oracle Database",
                       "client" });
    dbmsValues.put("postgresql",
                   new String[] {
                       "PostgreSQL Database",
                       "client" });
    dbmsValues.put("sqlite",
                   new String[] {
                       "SQLite",
                       "embedded" });

    return dbmsValues;
  }
}
