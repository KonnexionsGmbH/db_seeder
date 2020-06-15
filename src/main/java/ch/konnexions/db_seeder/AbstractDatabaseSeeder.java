/**
 * 
 */
package ch.konnexions.db_seeder;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * <h1> Test Data Generator for a Database. </h1>
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public abstract class AbstractDatabaseSeeder {

  protected enum DatabaseBrand {
    CRATEDB, IBMDB2, MARIADB, MSSQLSERVER, MYSQL, ORACLE, POSTGRESQL
  }

  protected Config            config;

  protected Connection        connection;

  protected DatabaseBrand     databaseBrand;

  // private static Logger logger                   = Logger.getLogger(AbstractDatabaseSeeder.class);

  protected ArrayList<Object> pkListCity               = new ArrayList<Object>();
  protected ArrayList<Object> pkListCompany            = new ArrayList<Object>();
  protected ArrayList<Object> pkListCountry            = new ArrayList<Object>();
  protected ArrayList<Object> pkListCountryState       = new ArrayList<Object>();
  protected ArrayList<Object> pkListTimezone           = new ArrayList<Object>();

  protected final String      TABLE_NAME_CITY          = "CITY";
  protected final String      TABLE_NAME_COMPANY       = "COMPANY";
  protected final String      TABLE_NAME_COUNTRY       = "COUNTRY";
  protected final String      TABLE_NAME_COUNTRY_STATE = "COUNTRY_STATE";
  protected final String      TABLE_NAME_TIMEZONE      = "TIMEZONE";

  /**
   *
   */
  public AbstractDatabaseSeeder() {
    super();
  }

}
