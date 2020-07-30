package ch.konnexions.db_seeder.generated;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * CREATE TABLE statements for a CUBRID DBMS. <br>
 * 
 * @author  GenerateSchema.class
 * @version 2.0.0
 * @since   2020-07-30
 */
public abstract class AbstractGenCubridSchema extends AbstractGenSeeder {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  private static final Logger                 logger            = Logger.getLogger(AbstractGenCubridSchema.class);

  /**
   * Creates the CREATE TABLE statements.
   */
  @SuppressWarnings("preview")
  private static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<>();

    statements.put(TABLE_NAME_TEST_TABLE_6,
                   """
                   CREATE TABLE "TEST_TABLE_6" (
                       "COLUMN_6_01"                    INT                                DEFAULT 4711,
                       "COLUMN_6_02"                    INT                                DEFAULT 5,
                       "COLUMN_6_03"                    VARCHAR(1)                         DEFAULT "x",
                       "COLUMN_6_04"                    VARCHAR(5)                         DEFAULT "x"
                   )
                   """);

    return statements;
  }

  /**
   * Initialises a new abstract CUBRID schema object.
   *
   * @param dbmsTickerSymbol
   *            DBMS ticker symbol
   */
  public AbstractGenCubridSchema(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }
}
