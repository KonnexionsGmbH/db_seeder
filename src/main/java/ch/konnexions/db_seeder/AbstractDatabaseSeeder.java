/**
 * 
 */
package ch.konnexions.db_seeder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

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
    MSSQLSERVER,
    MYSQL,
    ORACLE,
    POSTGRESQL,
    SQLITE
  }

  private static Logger        logger                   = Logger.getLogger(AbstractDatabaseSeeder.class);

  public final static String   FORMAT_IDENTIFIER        = "%-10d";
  // public final static String   FORMAT_IDENTIFIER_RIGHT  = "%010d";
  public final static String   FORMAT_METHOD_NAME       = "%-25s";
  public final static String   FORMAT_ROW_NO            = "%1$10d";
  public final static String   FORMAT_TABLE_NAME        = "%-17s";

  protected int                autoIncrement;

  protected Config             config;

  protected Dbms               dbms;

  protected boolean            isClient                 = true;
  protected boolean            isEmbedded               = !(isClient);

  protected ArrayList<Object>  pkListCity               = new ArrayList<Object>();
  protected ArrayList<Object>  pkListCountry            = new ArrayList<Object>();
  protected ArrayList<Object>  pkListCountryState       = new ArrayList<Object>();
  protected ArrayList<Object>  pkListTimezone           = new ArrayList<Object>();

  protected final String       TABLE_NAME_CITY          = "CITY";
  protected final String       TABLE_NAME_COMPANY       = "COMPANY";
  protected final String       TABLE_NAME_COUNTRY       = "COUNTRY";
  protected final String       TABLE_NAME_COUNTRY_STATE = "COUNTRY_STATE";
  protected final String       TABLE_NAME_TIMEZONE      = "TIMEZONE";
  protected final List<String> TABLE_NAMES_CREATE       = Arrays
      .asList(TABLE_NAME_COUNTRY, TABLE_NAME_TIMEZONE, TABLE_NAME_COUNTRY_STATE, TABLE_NAME_CITY, TABLE_NAME_COMPANY);
  protected final List<String> TABLE_NAMES_DROP         = Arrays
      .asList(TABLE_NAME_COMPANY, TABLE_NAME_CITY, TABLE_NAME_COUNTRY_STATE, TABLE_NAME_COUNTRY, TABLE_NAME_TIMEZONE);

  protected String             tableNameDelimiter       = "tbd";

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
}
