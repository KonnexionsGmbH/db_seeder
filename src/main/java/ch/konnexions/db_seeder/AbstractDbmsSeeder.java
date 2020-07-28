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

    CRATEDB(
        "cratedb"
    ),
    CUBRID(
        "cubrid"
    ),
    DERBY(
        "derby"
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

    private final String tickerSymbol;

    DbmsEnum(String tickerSymbol) {
      this.tickerSymbol = tickerSymbol;
    }

    public String getTickerSymbol() {
      return tickerSymbol;
    }
  }

  public static final Map<String, String[]> dbmsValues        = initDbmsValues();

  public static final String                FORMAT_IDENTIFIER = "%-10d";
  // protected static final String   FORMAT_IDENTIFIER_RIGHT  = "%010d";
  protected static final String             FORMAT_ROW_NO     = "%1$10d";
  protected static final String             FORMAT_TABLE_NAME = "%-17s";

  private static final Logger               logger            = Logger.getLogger(AbstractDbmsSeeder.class);

  /**
   * Initialises the DBMSs descriptions.
   * 
   * Attributes: 0 - DBMS name long
   *             1 - client / embedded
   *             2 - DBMS name short
   *             3 - identifier delimiter
   *
   * @return the map containing the DBMSs descriptions
   */
  private static Map<String, String[]> initDbmsValues() {
    Map<String, String[]> dbmsValues = new HashMap<String, String[]>();

    dbmsValues.put("cratedb",
                   new String[] {
                       "CrateDB",
                       "client",
                       "CrateDB",
                       "" });
    dbmsValues.put("cubrid",
                   new String[] {
                       "CUBRID",
                       "client",
                       "CUBRID",
                       "\"" });
    dbmsValues.put("derby",
                   new String[] {
                       "Apache Derby [client]",
                       "client",
                       "Apache Derby",
                       "" });
    dbmsValues.put("derby_emb",
                   new String[] {
                       "Apache Derby [embedded]",
                       "embedded",
                       "Apache Derby",
                       "" });
    dbmsValues.put("firebird",
                   new String[] {
                       "Firebird",
                       "client",
                       "Firebird",
                       "" });
    dbmsValues.put("h2",
                   new String[] {
                       "H2 Database Engine [client]",
                       "client",
                       "H2",
                       "" });
    dbmsValues.put("h2_emb",
                   new String[] {
                       "H2 Database Engine [embedded]",
                       "embedded",
                       "H2",
                       "" });
    dbmsValues.put("hsqldb",
                   new String[] {
                       "HyperSQL Database [client]",
                       "client",
                       "HyperSQL",
                       "" });
    dbmsValues.put("hsqldb_emb",
                   new String[] {
                       "HyperSQL Database [embedded]",
                       "embedded",
                       "HyperSQL",
                       "" });
    dbmsValues.put("ibmdb2",
                   new String[] {
                       "IBM Db2 Database",
                       "client",
                       "IBM Db2",
                       "" });
    dbmsValues.put("informix",
                   new String[] {
                       "IBM Informix",
                       "client",
                       "IBM Informix",
                       "" });
    dbmsValues.put("mariadb",
                   new String[] {
                       "MariaDB Server",
                       "client",
                       "MariaDB",
                       "`" });
    dbmsValues.put("mimer",
                   new String[] {
                       "Mimer SQL",
                       "client",
                       "Mimer",
                       "" });
    dbmsValues.put("mssqlserver",
                   new String[] {
                       "MS SQL Server",
                       "client",
                       "MS SQL Server",
                       "" });
    dbmsValues.put("mysql",
                   new String[] {
                       "MySQL Database",
                       "client",
                       "MySQL",
                       "`" });
    dbmsValues.put("oracle",
                   new String[] {
                       "Oracle Database",
                       "client",
                       "Oracle",
                       "" });
    dbmsValues.put("postgresql",
                   new String[] {
                       "PostgreSQL Database",
                       "client",
                       "PostgreSQL",
                       "" });
    dbmsValues.put("sqlite",
                   new String[] {
                       "SQLite",
                       "embedded",
                       "SQLite",
                       "" });

    return dbmsValues;
  }

  protected Config        config;
  protected DbmsEnum      dbmsEnum;

  protected String        dbmsTickerSymbol;

  protected String        identifierDelimiter;
  protected final boolean isDebug = logger.isDebugEnabled();

  /**
   * Initialises a new abstract DBMS seeder object.
   */
  public AbstractDbmsSeeder() {
    super();
  }

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

    identifierDelimiter = dbmsValues.get(dbmsTickerSymbol)[3];

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

    identifierDelimiter = dbmsValues.get(dbmsTickerSymbol)[3];

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Gets the DBMS name.
   *
   * @param tickerSymbolLower the lower case ticker symbol
   *
   * @return the DBMS name
   */
  public final String getDbmsName(String tickerSymbolLower) {
    return dbmsValues.get(tickerSymbolLower)[2];
  }

  /**
   * Gets the identifier delimiter.
   *
   * @param tickerSymbolLower the lower case ticker symbol
   *
   * @return the identifier delimiter
   */
  public final String getIdentifierDelimiter(String tickerSymbolLower) {
    return dbmsValues.get(tickerSymbolLower)[3];
  }
}
