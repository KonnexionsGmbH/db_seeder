package ch.konnexions.db_seeder.generated;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * CREATE TABLE statements for a Oracle DBMS. <br>
 * 
 * @author  GenerateSchema.class
 * @version 2.0.0
 * @since   2020-07-30
 */
public abstract class AbstractGenOracleSchema extends AbstractGenSeeder {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  private static final Logger                 logger            = Logger.getLogger(AbstractGenOracleSchema.class);

  /**
   * Creates the CREATE TABLE statements.
   */
  @SuppressWarnings("preview")
  private static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<>();

    statements.put(TABLE_NAME_TEST_TABLE_1,
                   """
                   CREATE TABLE TEST_TABLE_1 (
                       COLUMN_1_01                      NUMBER                    NOT NULL
                                                                                  UNIQUE,
                       COLUMN_1_02                      NUMBER,
                       COLUMN_1_03                      BLOB,
                       COLUMN_1_04                      TIMESTAMP                 NOT NULL,
                       COLUMN_1_05                      TIMESTAMP,
                       COLUMN_1_06                      VARCHAR2(100)             NOT NULL
                                                                                  PRIMARY KEY
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_2,
                   """
                   CREATE TABLE TEST_TABLE_2 (
                       COLUMN_2_01                      NUMBER                    NOT NULL,
                       COLUMN_2_02                      NUMBER                    NOT NULL,
                       COLUMN_2_03                      VARCHAR2(1)               NOT NULL,
                       COLUMN_2_04                      VARCHAR2(50),
                       COLUMN_2_05                      VARCHAR2(50),
                       COLUMN_2_06                      VARCHAR2(50),
                       COLUMN_2_07                      TIMESTAMP                 NOT NULL,
                       COLUMN_2_08                      CLOB,
                       COLUMN_2_09                      VARCHAR2(100),
                       COLUMN_2_10                      VARCHAR2(50),
                       COLUMN_2_11                      TIMESTAMP,
                       COLUMN_2_12                      VARCHAR2(100)             NOT NULL
                                                                                  UNIQUE,
                       COLUMN_2_13                      VARCHAR2(50),
                       COLUMN_2_14                      VARCHAR2(50),
                       COLUMN_2_15                      VARCHAR2(250),
                       COLUMN_2_16                      VARCHAR2(100),
                       CONSTRAINT CONSTRAINT_31       UNIQUE      (COLUMN_2_13, COLUMN_2_14),
                       CONSTRAINT CONSTRAINT_32       PRIMARY KEY (COLUMN_2_01, COLUMN_2_02, COLUMN_2_03)
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_3,
                   """
                   CREATE TABLE TEST_TABLE_3 (
                       COLUMN_3_01                      NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       COLUMN_3_02                      BLOB,
                       COLUMN_3_03                      TIMESTAMP                 NOT NULL,
                       COLUMN_3_04                      VARCHAR2(50),
                       COLUMN_3_05                      TIMESTAMP,
                       COLUMN_3_06                      VARCHAR2(100)             NOT NULL
                                                                                  UNIQUE
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_4,
                   """
                   CREATE TABLE TEST_TABLE_4 (
                       COLUMN_4_01                      NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       COLUMN_4_02                      NUMBER                    NOT NULL,
                       COLUMN_4_03                      NUMBER                    NOT NULL,
                       COLUMN_4_04                      BLOB,
                       COLUMN_4_05                      TIMESTAMP                 NOT NULL,
                       COLUMN_4_06                      TIMESTAMP,
                       COLUMN_4_07                      VARCHAR2(100)             NOT NULL
                                                                                  UNIQUE,
                       COLUMN_4_08                      VARCHAR2(50),
                       CONSTRAINT CONSTRAINT_33       UNIQUE      (COLUMN_4_02, COLUMN_4_07)
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_5,
                   """
                   CREATE TABLE TEST_TABLE_5 (
                       COLUMN_5_01                      NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       COLUMN_5_02                      VARCHAR2(50)              NOT NULL
                                                                                  UNIQUE,
                       COLUMN_5_03                      TIMESTAMP                 NOT NULL,
                       COLUMN_5_04                      TIMESTAMP,
                       COLUMN_5_05                      VARCHAR2(100)             NOT NULL
                                                                                  UNIQUE,
                       COLUMN_5_06                      VARCHAR2(4000)
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_6,
                   """
                   CREATE TABLE TEST_TABLE_6 (
                       COLUMN_6_01                      NUMBER                    DEFAULT 4711,
                       COLUMN_6_02                      NUMBER                    DEFAULT 5,
                       COLUMN_6_03                      VARCHAR2(10)              DEFAULT 'default',
                       COLUMN_6_04                      VARCHAR2(5)               DEFAULT 'x'
                   )
                   """);

    return statements;
  }

  /**
   * Initialises a new abstract Oracle schema object.
   *
   * @param dbmsTickerSymbol
   *            DBMS ticker symbol
   */
  public AbstractGenOracleSchema(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }
}
