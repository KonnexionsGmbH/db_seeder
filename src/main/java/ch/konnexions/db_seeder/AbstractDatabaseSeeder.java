/**
 * 
 */
package ch.konnexions.db_seeder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h1> Test Data Generator for a Database. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public abstract class AbstractDatabaseSeeder {

  protected enum Dbms {
    DERBY,
    CRATEDB,
    CUBRID,
    IBMDB2,
    MARIADB,
    MSSQLSERVER,
    MYSQL,
    ORACLE,
    POSTGRESQL,
    SQLITE
  }

  protected int                autoIncrement;

  protected Config             config;
  protected Connection         connection;

  protected Dbms               dbms;

  // private static Logger logger                   = Logger.getLogger(AbstractDatabaseSeeder.class);

  protected ArrayList<Object>  pkListCity               = new ArrayList<Object>();
  protected ArrayList<Object>  pkListCompany            = new ArrayList<Object>();
  protected ArrayList<Object>  pkListCountry            = new ArrayList<Object>();
  protected ArrayList<Object>  pkListCountryState       = new ArrayList<Object>();
  protected ArrayList<Object>  pkListTimezone           = new ArrayList<Object>();

  protected final String       TABLE_NAME_CITY          = "CITY";
  protected final String       TABLE_NAME_COMPANY       = "COMPANY";
  protected final String       TABLE_NAME_COUNTRY       = "COUNTRY";
  protected final String       TABLE_NAME_COUNTRY_STATE = "COUNTRY_STATE";
  protected final String       TABLE_NAME_TIMEZONE      = "TIMEZONE";
  protected final List<String> TABLE_NAMES              = Arrays
      .asList(TABLE_NAME_COMPANY, TABLE_NAME_CITY, TABLE_NAME_COUNTRY_STATE, TABLE_NAME_COUNTRY, TABLE_NAME_TIMEZONE);

  /**
   *
   */
  public AbstractDatabaseSeeder() {
    super();
  }

}
