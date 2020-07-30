package ch.konnexions.db_seeder.generated;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * CREATE TABLE statements for a CrateDB DBMS. <br>
 * 
 * @author  GenerateSchema.class
 * @version 2.0.0
 * @since   2020-07-30
 */
public abstract class AbstractGenCratedbSchema extends AbstractGenSeeder {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  private static final Logger                 logger            = Logger.getLogger(AbstractGenCratedbSchema.class);

  /**
   * Creates the CREATE TABLE statements.
   */
  @SuppressWarnings("preview")
  private static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<>();

    statements.put(TABLE_NAME_TEST_TABLE_1,
                   """
                   CREATE TABLE TEST_TABLE_1 (
                       COLUMN_1_01                      BIGINT                    NOT NULL,
                       COLUMN_1_02                      BIGINT,
                       COLUMN_1_03                      OBJECT,
                       COLUMN_1_04                      TIMESTAMP                 NOT NULL,
                       COLUMN_1_05                      TIMESTAMP,
                       COLUMN_1_06                      TEXT                      NOT NULL
                                                                                  PRIMARY KEY
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_2,
                   """
                   CREATE TABLE TEST_TABLE_2 (
                       COLUMN_2_01                      BIGINT                    NOT NULL,
                       COLUMN_2_02                      BIGINT                    NOT NULL,
                       COLUMN_2_03                      TEXT                      NOT NULL,
                       COLUMN_2_04                      TEXT,
                       COLUMN_2_05                      TEXT,
                       COLUMN_2_06                      TEXT,
                       COLUMN_2_07                      TIMESTAMP                 NOT NULL,
                       COLUMN_2_08                      TEXT,
                       COLUMN_2_09                      TEXT,
                       COLUMN_2_10                      TEXT,
                       COLUMN_2_11                      TIMESTAMP,
                       COLUMN_2_12                      TEXT                      NOT NULL,
                       COLUMN_2_13                      TEXT,
                       COLUMN_2_14                      TEXT,
                       COLUMN_2_15                      TEXT,
                       COLUMN_2_16                      TEXT
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_3,
                   """
                   CREATE TABLE TEST_TABLE_3 (
                       COLUMN_3_01                      BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       COLUMN_3_02                      OBJECT,
                       COLUMN_3_03                      TIMESTAMP                 NOT NULL,
                       COLUMN_3_04                      TEXT,
                       COLUMN_3_05                      TIMESTAMP,
                       COLUMN_3_06                      TEXT                      NOT NULL
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_4,
                   """
                   CREATE TABLE TEST_TABLE_4 (
                       COLUMN_4_01                      BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       COLUMN_4_02                      BIGINT                    NOT NULL,
                       COLUMN_4_03                      BIGINT                    NOT NULL,
                       COLUMN_4_04                      OBJECT,
                       COLUMN_4_05                      TIMESTAMP                 NOT NULL,
                       COLUMN_4_06                      TIMESTAMP,
                       COLUMN_4_07                      TEXT                      NOT NULL,
                       COLUMN_4_08                      TEXT
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_5,
                   """
                   CREATE TABLE TEST_TABLE_5 (
                       COLUMN_5_01                      BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       COLUMN_5_02                      TEXT                      NOT NULL,
                       COLUMN_5_03                      TIMESTAMP                 NOT NULL,
                       COLUMN_5_04                      TIMESTAMP,
                       COLUMN_5_05                      TEXT                      NOT NULL,
                       COLUMN_5_06                      TEXT
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_6,
                   """
                   CREATE TABLE TEST_TABLE_6 (
                       COLUMN_6_01                      BIGINT                    DEFAULT 4711,
                       COLUMN_6_02                      BIGINT                    DEFAULT 5,
                       COLUMN_6_03                      TEXT                      DEFAULT 'default',
                       COLUMN_6_04                      TEXT                      DEFAULT 'x'
                   )
                   """);

    return statements;
  }

  /**
   * Initialises a new abstract CrateDB schema object.
   *
   * @param dbmsTickerSymbol
   *            DBMS ticker symbol
   */
  public AbstractGenCratedbSchema(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }
}
