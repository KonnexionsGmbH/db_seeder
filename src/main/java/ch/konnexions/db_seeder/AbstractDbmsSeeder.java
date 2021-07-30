package ch.konnexions.db_seeder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.utils.Config;

/**
 * Data Generator for a Database - Abstract DBMS Seeder.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public abstract class AbstractDbmsSeeder {

  public enum DbmsEnum {

    AGENS(
        "agens"
    ),
    COCKROACH(
        "cockroach"
    ),
    CRATEDB(
        "cratedb"
    ),
    CUBRID(
        "cubrid"
    ),
    DERBY(
        "derby"
    ),
    EXASOL(
        "exasol"
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
    MONETDB(
        "monetdb"
    ),
    MYSQL(
        "mysql"
    ),
    OMNISCI(
        "omnisci"
    ),
    ORACLE(
        "oracle"
    ),
    PERCONA(
        "percona"
    ),
    POSTGRESQL(
        "postgresql"
    ),
    SQLITE(
        "sqlite"
    ),
    SQLSERVER(
        "sqlserver"
    ),
    TIMESCALE(
        "timescale"
    ),
    VOLTDB(
        "voltdb"
    ),
    YUGABYTE(
        "yugabyte"
    );

    private final String tickerSymbolIntern;

    DbmsEnum(String tickerSymbolIntern) {
      this.tickerSymbolIntern = tickerSymbolIntern;
    }

    public String getTickerSymbolIntern() {
      return tickerSymbolIntern;
    }
  }

  public static final int                   DBMS_DETAILS_CLIENT_EMBEDDED      = 2;

  private static final int                  DBMS_DETAILS_IDENTIFIER_DELIMITER = 4;

  private static final int                  DBMS_DETAILS_NAME                 = 3;
  public static final int                   DBMS_DETAILS_NAME_CHOICE          = 1;
  static final int                          DBMS_DETAILS_TICKER_SYMBOL_LOWER  = 0;
  public static final Map<String, String[]> dbmsDetails                       = initDbmsDetails();
  public static final String                FORMAT_IDENTIFIER                 = "%-10d";

  // protected static final String   FORMAT_IDENTIFIER_RIGHT  = "%010d";
  public static final String                FORMAT_ROW_NO                     = "%1$10d";
  protected static final String             FORMAT_TABLE_NAME                 = "%-17s";
  private static final Logger               logger                            = LogManager.getLogger(AbstractDbmsSeeder.class);

  /**
   * Initialises the DBMS details.
   * 
   * Attributes: 0 - DBMS name long
   *             1 - db type
   *             2 - DBMS name short
   *             3 - identifier delimiter
   *
   * @return the map containing the DBMS details
   */
  private static Map<String, String[]> initDbmsDetails() {
    Map<String, String[]> dbmsDetails = new HashMap<>();

    dbmsDetails.put("agens",
                    new String[] {
                        "agens",
                        "AgensGraph",
                        "client",
                        "AgensGraph",
                        "" });
    dbmsDetails.put("cockroach",
                    new String[] {
                        "cockroach",
                        "CockroachDB",
                        "client",
                        "CockroachDB",
                        "" });
    dbmsDetails.put("cratedb",
                    new String[] {
                        "cratedb",
                        "CrateDB",
                        "client",
                        "CrateDB",
                        "" });
    dbmsDetails.put("cubrid",
                    new String[] {
                        "cubrid",
                        "CUBRID",
                        "client",
                        "CUBRID",
                        "\"" });
    dbmsDetails.put("derby",
                    new String[] {
                        "derby",
                        "Apache Derby",
                        "client",
                        "Apache Derby",
                        "" });
    dbmsDetails.put("derby_emb",
                    new String[] {
                        "derby",
                        "Apache Derby",
                        "embedded",
                        "Apache Derby",
                        "" });
    dbmsDetails.put("exasol",
                    new String[] {
                        "exasol",
                        "Exasol",
                        "client",
                        "Exasol",
                        "" });
    dbmsDetails.put("firebird",
                    new String[] {
                        "firebird",
                        "Firebird",
                        "client",
                        "Firebird",
                        "" });
    dbmsDetails.put("h2",
                    new String[] {
                        "h2",
                        "H2 Database Engine",
                        "client",
                        "H2",
                        "" });
    dbmsDetails.put("h2_emb",
                    new String[] {
                        "h2",
                        "H2 Database Engine",
                        "embedded",
                        "H2",
                        "" });
    dbmsDetails.put("hsqldb",
                    new String[] {
                        "hsqldb",
                        "HSQLDB",
                        "client",
                        "HSQLDB",
                        "" });
    dbmsDetails.put("hsqldb_emb",
                    new String[] {
                        "hsqldb",
                        "HSQLDB",
                        "embedded",
                        "HSQLDB",
                        "" });
    dbmsDetails.put("ibmdb2",
                    new String[] {
                        "ibmdb2",
                        "IBM Db2 Database",
                        "client",
                        "IBM Db2",
                        "" });
    dbmsDetails.put("informix",
                    new String[] {
                        "informix",
                        "IBM Informix",
                        "client",
                        "IBM Informix",
                        "" });
    dbmsDetails.put("mariadb",
                    new String[] {
                        "mariadb",
                        "MariaDB Server",
                        "client",
                        "MariaDB",
                        "`" });
    dbmsDetails.put("mimer",
                    new String[] {
                        "mimer",
                        "Mimer SQL",
                        "client",
                        "Mimer",
                        "" });
    dbmsDetails.put("monetdb",
                    new String[] {
                        "monetdb",
                        "MonetDB",
                        "client",
                        "MonetDB",
                        "" });
    dbmsDetails.put("mysql",
                    new String[] {
                        "mysql",
                        "MySQL Database",
                        "client",
                        "MySQL",
                        "" });
    dbmsDetails.put("mysql_trino",
                    new String[] {
                        "mysql",
                        "MySQL Database",
                        "trino",
                        "MySQL",
                        "" });
    dbmsDetails.put("omnisci",
                    new String[] {
                        "omnisci",
                        "OmniSciDB",
                        "client",
                        "OmniSciDB",
                        "" });
    dbmsDetails.put("oracle",
                    new String[] {
                        "oracle",
                        "Oracle Database",
                        "client",
                        "Oracle",
                        "" });
    dbmsDetails.put("oracle_trino",
                    new String[] {
                        "oracle",
                        "Oracle Database",
                        "trino",
                        "Oracle",
                        "" });
    dbmsDetails.put("percona",
                    new String[] {
                        "percona",
                        "Percona Server for MySQL",
                        "client",
                        "Percona Server",
                        "" });
    dbmsDetails.put("postgresql",
                    new String[] {
                        "postgresql",
                        "PostgreSQL",
                        "client",
                        "PostgreSQL",
                        "" });
    dbmsDetails.put("postgresql_trino",
                    new String[] {
                        "postgresql",
                        "PostgreSQL",
                        "trino",
                        "PostgreSQL",
                        "" });
    dbmsDetails.put("sqlite",
                    new String[] {
                        "sqlite",
                        "SQLite",
                        "embedded",
                        "SQLite",
                        "" });
    dbmsDetails.put("sqlserver",
                    new String[] {
                        "sqlserver",
                        "MS SQL Server",
                        "client",
                        "MS SQL Server",
                        "" });
    dbmsDetails.put("sqlserver_trino",
                    new String[] {
                        "sqlserver",
                        "MS SQL Server",
                        "trino",
                        "MS SQL Server",
                        "" });
    dbmsDetails.put("timescale",
                    new String[] {
                        "timescale",
                        "TimescaleDB",
                        "client",
                        "TimescaleDB",
                        "" });
    dbmsDetails.put("voltdb",
                    new String[] {
                        "voltdb",
                        "VoltDB",
                        "client",
                        "VoltDB",
                        "" });
    dbmsDetails.put("yugabyte",
                    new String[] {
                        "yugabyte",
                        "YugabyteDB",
                        "client",
                        "YugabyteDB",
                        "" });

    return dbmsDetails;
  }

  protected int          batchSize;

  protected Config       config;

  protected DbmsEnum     dbmsEnum;
  protected String       dropConstraints;

  protected final String identifierDelimiter;

  protected final String tickerSymbolIntern;

  /**
   * Initialises a new abstract DBMS seeder object.
   */
  protected AbstractDbmsSeeder() {
    super();

    boolean isDebug = logger.isDebugEnabled();
    if (isDebug) {
      logger.debug("Start Constructor");
    }

    identifierDelimiter = dbmsDetails.get("sqlite")[DBMS_DETAILS_IDENTIFIER_DELIMITER];
    tickerSymbolIntern  = dbmsDetails.get("sqlite")[DBMS_DETAILS_TICKER_SYMBOL_LOWER];

    logger.info("tickerSymbolIntern =" + tickerSymbolIntern);

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Initialises a new abstract DBMS seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   * @param dbmsOption client, embedded or trino
   */
  public AbstractDbmsSeeder(String tickerSymbolExtern, String dbmsOption) {
    super();

    boolean isDebug = logger.isDebugEnabled();
    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    identifierDelimiter = dbmsDetails.get(tickerSymbolExtern)[DBMS_DETAILS_IDENTIFIER_DELIMITER];
    tickerSymbolIntern  = dbmsDetails.get(tickerSymbolExtern)[DBMS_DETAILS_TICKER_SYMBOL_LOWER];

    logger.info("tickerSymbolIntern =" + tickerSymbolIntern);

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Gets the DBMS name.
   *
   * @param tickerSymbolIntern the intern DBMS ticker symbol
   *
   * @return the DBMS name
   */
  public final String getDbmsName(String tickerSymbolIntern) {
    return dbmsDetails.get(tickerSymbolIntern)[DBMS_DETAILS_NAME];
  }

  /**
   * Gets the identifier delimiter.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   *
   * @return the identifier delimiter
   */
  public final String getIdentifierDelimiter(String tickerSymbolExtern) {
    return dbmsDetails.get(tickerSymbolExtern)[DBMS_DETAILS_IDENTIFIER_DELIMITER];
  }

  /**
   * Put the identifier in the correct case.
   *
   * @param identifier the identifier
   * 
   * @return the converted identifier
   */
  public final String setCaseIdentifier(String identifier) {
    if ("agens".equals(tickerSymbolIntern) || "omnisci".equals(tickerSymbolIntern)) {
      return identifier.toLowerCase();
    }

    return identifier.toUpperCase();
  }

  /**
   * Put the identifier list in the correct case.
   *
   * @param identifiers the identifier list
   */
  public final void setCaseIdentifiers(ArrayList<String> identifiers) {
    if ("agens".equals(tickerSymbolIntern) || "omnisci".equals(tickerSymbolIntern)) {
      identifiers.forEach(String::toLowerCase);
    }

    identifiers.forEach(String::toUpperCase);
  }
}
