package ch.konnexions.db_seeder.jdbc;

import ch.konnexions.db_seeder.AbstractDbmsSeeder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test Data Generator for a Database - Abstract JDBC Schema.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
abstract class AbstractJdbcSchema extends AbstractDbmsSeeder {

  protected static Map<String, String>                      dmlStatements;

  private static final Logger                               logger      = Logger.getLogger(AbstractJdbcSchema.class);
  protected static Map<String, Integer>                     maxRowSizes;

  protected static final HashMap<String, ArrayList<Object>> pkLists     = new HashMap<>();

  protected static final HashMap<String, Integer>           pkListSizes = new HashMap<>();

  protected static List<String>                             TABLE_NAMES_CREATE;

  protected static List<String>                             TABLE_NAMES_DROP;

  /**
   * Initialises a new abstract JDBC schema object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption         client, embedded or presto
   */
  public AbstractJdbcSchema(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    boolean isDebug = logger.isDebugEnabled();
    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }
}
