package ch.konnexions.db_seeder.generated;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * CREATE TABLE statements for a MariaDB DBMS. <br>
 * 
 * @author  GenerateSchema.class
 * @version 2.0.0
 * @since   2020-07-30
 */
public abstract class AbstractGenMariadbSchema extends AbstractGenSeeder {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  private static final Logger                 logger            = Logger.getLogger(AbstractGenMariadbSchema.class);

  /**
   * Creates the CREATE TABLE statements.
   */
  @SuppressWarnings("preview")
  private static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<>();

    statements.put(TABLE_NAME_TEST_TABLE_1,
                   """
                   CREATE TABLE `TEST_TABLE_1` (
                       COLUMN_1_01                      BIGINT                    NOT NULL
                                                                                  UNIQUE,
                       COLUMN_1_02                      BIGINT,
                       COLUMN_1_03                      LONGBLOB,
                       COLUMN_1_04                      DATETIME                  NOT NULL,
                       COLUMN_1_05                      DATETIME,
                       COLUMN_1_06                      VARCHAR(100)              NOT NULL
                                                                                  PRIMARY KEY
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_2,
                   """
                   CREATE TABLE `TEST_TABLE_2` (
                       COLUMN_2_01                      BIGINT                    NOT NULL,
                       COLUMN_2_02                      BIGINT                    NOT NULL,
                       COLUMN_2_03                      VARCHAR(1)                NOT NULL,
                       COLUMN_2_04                      VARCHAR(50),
                       COLUMN_2_05                      VARCHAR(50),
                       COLUMN_2_06                      VARCHAR(50),
                       COLUMN_2_07                      DATETIME                  NOT NULL,
                       COLUMN_2_08                      LONGTEXT,
                       COLUMN_2_09                      VARCHAR(100),
                       COLUMN_2_10                      VARCHAR(50),
                       COLUMN_2_11                      DATETIME,
                       COLUMN_2_12                      VARCHAR(100)              NOT NULL
                                                                                  UNIQUE,
                       COLUMN_2_13                      VARCHAR(50),
                       COLUMN_2_14                      VARCHAR(50),
                       COLUMN_2_15                      VARCHAR(250),
                       COLUMN_2_16                      VARCHAR(100),
                       CONSTRAINT CONSTRAINT_19       UNIQUE      (`COLUMN_2_13`, `COLUMN_2_14`),
                       CONSTRAINT CONSTRAINT_20       PRIMARY KEY (`COLUMN_2_01`, `COLUMN_2_02`, `COLUMN_2_03`)
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_3,
                   """
                   CREATE TABLE `TEST_TABLE_3` (
                       COLUMN_3_01                      BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       COLUMN_3_02                      LONGBLOB,
                       COLUMN_3_03                      DATETIME                  NOT NULL,
                       COLUMN_3_04                      VARCHAR(50),
                       COLUMN_3_05                      DATETIME,
                       COLUMN_3_06                      VARCHAR(100)              NOT NULL
                                                                                  UNIQUE
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_4,
                   """
                   CREATE TABLE `TEST_TABLE_4` (
                       COLUMN_4_01                      BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       COLUMN_4_02                      BIGINT                    NOT NULL,
                       COLUMN_4_03                      BIGINT                    NOT NULL,
                       COLUMN_4_04                      LONGBLOB,
                       COLUMN_4_05                      DATETIME                  NOT NULL,
                       COLUMN_4_06                      DATETIME,
                       COLUMN_4_07                      VARCHAR(100)              NOT NULL
                                                                                  UNIQUE,
                       COLUMN_4_08                      VARCHAR(50),
                       CONSTRAINT CONSTRAINT_21       UNIQUE      (`COLUMN_4_02`, `COLUMN_4_07`)
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_5,
                   """
                   CREATE TABLE `TEST_TABLE_5` (
                       COLUMN_5_01                      BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       COLUMN_5_02                      VARCHAR(50)               NOT NULL
                                                                                  UNIQUE,
                       COLUMN_5_03                      DATETIME                  NOT NULL,
                       COLUMN_5_04                      DATETIME,
                       COLUMN_5_05                      VARCHAR(100)              NOT NULL
                                                                                  UNIQUE,
                       COLUMN_5_06                      VARCHAR(4000)
                   )
                   """);

    statements.put(TABLE_NAME_TEST_TABLE_6,
                   """
                   CREATE TABLE `TEST_TABLE_6` (
                       COLUMN_6_01                      BIGINT                    DEFAULT 4711,
                       COLUMN_6_02                      BIGINT                    DEFAULT 5,
                       COLUMN_6_03                      VARCHAR(10)               DEFAULT 'default',
                       COLUMN_6_04                      VARCHAR(5)                DEFAULT 'x'
                   )
                   """);

    return statements;
  }

  /**
   * Initialises a new abstract MariaDB schema object.
   *
   * @param dbmsTickerSymbol
   *            DBMS ticker symbol
   */
  public AbstractGenMariadbSchema(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }
}
