package ch.konnexions.db_seeder;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.utils.Config;

/**
 * Test Data Generator for a Database - Abstract DBMS Seeder.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public abstract class AbstractDbmsSeeder {

  public enum DbmsEnum {

    DERBY(
        "derby"
    ),
    CRATEDB(
        "cratedb"
    ),
    CUBRID(
        "cubrid"
    ),
    FIREBIRD(
        "firebird"
    ),
    H2(
        "h2"
    ),
    HSQLDB(
        "hsqldb"
    ),
    IBMDB2(
        "ibmdb2"
    ),
    INFORMIX(
        "informix"
    ),
    MARIADB(
        "mariadb"
    ),
    MIMER(
        "mimer"
    ),
    MSSQLSERVER(
        "mssqlserver"
    ),
    MYSQL(
        "mysql"
    ),
    ORACLE(
        "oracle"
    ),
    POSTGRESQL(
        "postgresql"
    ),
    SQLITE(
        "sqlite"
    );

    private final String ticketSymbol;

    DbmsEnum(String ticketSymbol) {
      this.ticketSymbol = ticketSymbol;
    }

    public String getTicketSymbol() {
      return ticketSymbol;
    }
  }

  public static final String      FORMAT_IDENTIFIER  = "%-10d";
  // protected static final String   FORMAT_IDENTIFIER_RIGHT  = "%010d";
  protected static final String   FORMAT_ROW_NO      = "%1$10d";
  protected static final String   FORMAT_TABLE_NAME  = "%-17s";

  private static final Logger     logger             = Logger.getLogger(AbstractDbmsSeeder.class);

  protected Config                config;

  protected DbmsEnum              dbmsEnum;
  protected String                dbmsTickerSymbol;
  protected Map<String, String[]> dbmsValues         = new HashMap<String, String[]>();

  protected final boolean         isDebug            = logger.isDebugEnabled();

  protected String                tableNameDelimiter = "tbd";

  /**
   * Initialises a new abstract DBMS seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public AbstractDbmsSeeder(String dbmsTickerSymbol) {
    super();

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    dbmsValues = initDbmsValues();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Initialises a new abstract DBMS seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   * @param isClient client database version
   */
  public AbstractDbmsSeeder(String dbmsTickerSymbol, boolean isClient) {
    super();

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol + " - isClient=" + isClient);
    }

    dbmsValues = initDbmsValues();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Initialises the DBMSs descriptions.
   *
   * @return the map containing the DBMSs descriptions
   */
  private Map<String, String[]> initDbmsValues() {
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
