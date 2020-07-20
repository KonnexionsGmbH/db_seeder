/**
 * 
 */
package ch.konnexions.db_seeder;

import java.util.HashMap;
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

  private static Logger           logger             = Logger.getLogger(AbstractDatabaseSeeder.class);

  public final static String      FORMAT_IDENTIFIER  = "%-10d";

  // protected final static String   FORMAT_IDENTIFIER_RIGHT  = "%010d";
  protected final static String   FORMAT_ROW_NO      = "%1$10d";
  protected final static String   FORMAT_TABLE_NAME  = "%-17s";

  protected int                   autoIncrement;

  protected Config                config;

  protected Dbms                  dbms;
  protected String                dbmsTickerSymbol;
  protected Map<String, String[]> dbmsValues         = null;

  protected final boolean         isDebug            = logger.isDebugEnabled();

  protected String                tableNameDelimiter = "tbd";

  /**
   * Initialises a new abstract database seeder object.
   */
  public AbstractDatabaseSeeder() {
    super();

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsValues = initDbmsValues();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Initialises a new abstract database seeder object.
   *
   * @param isClient client database version
   */
  public AbstractDatabaseSeeder(boolean isClient) {
    super();

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbmsValues = initDbmsValues();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  private final Map<String, String[]> initDbmsValues() {
    Map<String, String[]> dbmsValues = new HashMap<>();

    dbmsValues.put("cratedb",
                   new String[] {
                       "CrateDB",
                       "client" });
    dbmsValues.put("cubrid",
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
