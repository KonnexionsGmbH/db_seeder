package ch.konnexions.db_seeder.generated;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Test Data Generator for a Database - Abstract Generated Seeder.
 * <br>
 * @author  GenerateSchema.class
 * @version 2.0.0
 * @since   2020-07-30
 */
abstract class AbstractGenSeeder extends AbstractGenSchema {

  private static final Logger logger = Logger.getLogger(AbstractGenSeeder.class);

  /**
   * Initialises a new abstract generated seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public AbstractGenSeeder(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Initialises a new abstract generated seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   * @param isClient client database version
   */
  public AbstractGenSeeder(String dbmsTickerSymbol, boolean isClient) {
    super(dbmsTickerSymbol, isClient);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol + " - isClient=" + isClient);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Creates a content value of type BIGINT.
   *
   * @param tableName         the table name
   * @param columnName        the column name
   * @param rowNo             the current row number
   * @param defaultValue      the defaultValue
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  @Override
  protected final long getContentBigint(String tableName,
                                        String columnName,
                                        long rowNo,
                                        Integer defaultValue,
                                        Integer lowerRange,
                                        Integer upperRange,
                                        List<Integer> validValues) {
    long result = super.getContentBigint(tableName,
                                         columnName,
                                         rowNo,
                                         defaultValue,
                                         lowerRange,
                                         upperRange,
                                         validValues);

    return result;
  }

  /**
   * Creates a content value of type BLOB.
   *
   * @param tableName         the table name
   * @param columnName        the column name
   * @param rowNo             the current row number
   */
  @Override
  protected final byte[] getContentBlob(String tableName, String columnName, long rowNo) {
    byte[] result = super.getContentBlob(tableName,
                                         columnName,
                                         rowNo);

    return result;
  }

  /**
   * Creates a content value of type CLOB.
   *
   * @param tableName         the table name
   * @param columnName        the column name
   * @param rowNo             the current row number
   */
  @Override
  protected final String getContentClob(String tableName, String columnName, long rowNo) {
    String result = super.getContentClob(tableName,
                                         columnName,
                                         rowNo);

    return result;
  }

  /**
   * Creates a content value of type foreign key value.
   *
   * @param tableName         the table name
   * @param columnName        the column name
   * @param rowNo             the current row number
   * @param fkList            the existing foreign keys
   */
  @Override
  protected final Object getContentFk(String tableName, String columnName, long rowNo, ArrayList<Object> fkList) {
    Object result = super.getContentFk(tableName,
                                       columnName,
                                       rowNo,
                                       fkList);

    return result;
  }

  /**
   * Creates a content value of type TIMESTAMP.
   *
   * @param tableName         the table name
   * @param columnName        the column name
   * @param rowNo             the current row number
   */
  @Override
  protected final Timestamp getContentTimestamp(String tableName, String columnName, long rowNo) {
    Timestamp result = super.getContentTimestamp(tableName,
                                                 columnName,
                                                 rowNo);

    return result;
  }

  /**
   * Creates a content value of type VARCHAR.
   *
   * @param tableName         the table name
   * @param columnName        the column name
   * @param rowNo             the current row number
   * @param size              the column size
   * @param defaultValue      the defaultValue
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  @Override
  protected final String getContentVarchar(String tableName,
                                           String columnName,
                                           long rowNo,
                                           int size,
                                           String defaultValue,
                                           String lowerRange,
                                           String upperRange,
                                           List<String> validValues) {
    String result = super.getContentVarchar(tableName,
                                            columnName,
                                            rowNo,
                                            size,
                                            defaultValue,
                                            lowerRange,
                                            upperRange,
                                            validValues);

    return result;
  }

  protected final void insertTable(PreparedStatement preparedStatement, final String tableName, final long rowNo) {
    if (isDebug) {
      logger.debug("Start");
    }

    switch (tableName) {
    case TABLE_NAME_TEST_TABLE_1 -> prepDmlStmntInsertTestTable1(preparedStatement,
                                                                 rowNo);
    case TABLE_NAME_TEST_TABLE_2 -> prepDmlStmntInsertTestTable2(preparedStatement,
                                                                 rowNo);
    case TABLE_NAME_TEST_TABLE_3 -> prepDmlStmntInsertTestTable3(preparedStatement,
                                                                 rowNo);
    case TABLE_NAME_TEST_TABLE_4 -> prepDmlStmntInsertTestTable4(preparedStatement,
                                                                 rowNo);
    case TABLE_NAME_TEST_TABLE_5 -> prepDmlStmntInsertTestTable5(preparedStatement,
                                                                 rowNo);
    case TABLE_NAME_TEST_TABLE_6 -> prepDmlStmntInsertTestTable6(preparedStatement,
                                                                 rowNo);
    default -> throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME,
                                                                                                    tableName));
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void prepDmlStmntInsertTestTable1(PreparedStatement preparedStatement, long rowNo) {
    if (isDebug) {
      logger.debug("Start");
    }

    int i = 0;

    prepStmntColBigint(preparedStatement,
                       "TEST_TABLE_1",
                       "COLUMN_1_01",
                       ++i,
                       rowNo,
                       null,
                       null,
                       null,
                       null);
    prepStmntColBigintOpt(preparedStatement,
                          "TEST_TABLE_1",
                          "COLUMN_1_02",
                          ++i,
                          rowNo,
                          null,
                          null,
                          null,
                          null);
    prepStmntColBlobOpt(preparedStatement,
                        "TEST_TABLE_1",
                        "COLUMN_1_03",
                        ++i,
                        rowNo);
    prepStmntColTimestamp(preparedStatement,
                          "TEST_TABLE_1",
                          "COLUMN_1_04",
                          ++i,
                          rowNo);
    prepStmntColTimestampOpt(preparedStatement,
                             "TEST_TABLE_1",
                             "COLUMN_1_05",
                             ++i,
                             rowNo);
    prepStmntColVarchar(preparedStatement,
                        "TEST_TABLE_1",
                        "COLUMN_1_06",
                        ++i,
                        rowNo,
                        100,
                        null,
                        null,
                        null,
                        null);
    if (isDebug) {
      logger.debug("End");
    }
  }

  private void prepDmlStmntInsertTestTable2(PreparedStatement preparedStatement, long rowNo) {
    if (isDebug) {
      logger.debug("Start");
    }

    int i = 0;

    prepStmntColBigint(preparedStatement,
                       "TEST_TABLE_2",
                       "COLUMN_2_01",
                       ++i,
                       rowNo,
                       null,
                       null,
                       null,
                       null);
    prepStmntColBigint(preparedStatement,
                       "TEST_TABLE_2",
                       "COLUMN_2_02",
                       ++i,
                       rowNo,
                       null,
                       null,
                       null,
                       null);
    prepStmntColVarchar(preparedStatement,
                        "TEST_TABLE_2",
                        "COLUMN_2_03",
                        ++i,
                        rowNo,
                        1,
                        null,
                        null,
                        null,
                        Arrays.asList("N",
                                      "Y"));
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_2",
                           "COLUMN_2_04",
                           ++i,
                           rowNo,
                           50,
                           null,
                           null,
                           null,
                           null);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_2",
                           "COLUMN_2_05",
                           ++i,
                           rowNo,
                           50,
                           null,
                           null,
                           null,
                           null);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_2",
                           "COLUMN_2_06",
                           ++i,
                           rowNo,
                           50,
                           null,
                           null,
                           null,
                           null);
    prepStmntColTimestamp(preparedStatement,
                          "TEST_TABLE_2",
                          "COLUMN_2_07",
                          ++i,
                          rowNo);
    prepStmntColClobOpt(preparedStatement,
                        "TEST_TABLE_2",
                        "COLUMN_2_08",
                        ++i,
                        rowNo);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_2",
                           "COLUMN_2_09",
                           ++i,
                           rowNo,
                           100,
                           null,
                           null,
                           null,
                           null);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_2",
                           "COLUMN_2_10",
                           ++i,
                           rowNo,
                           50,
                           null,
                           null,
                           null,
                           null);
    prepStmntColTimestampOpt(preparedStatement,
                             "TEST_TABLE_2",
                             "COLUMN_2_11",
                             ++i,
                             rowNo);
    prepStmntColVarchar(preparedStatement,
                        "TEST_TABLE_2",
                        "COLUMN_2_12",
                        ++i,
                        rowNo,
                        100,
                        null,
                        null,
                        null,
                        null);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_2",
                           "COLUMN_2_13",
                           ++i,
                           rowNo,
                           50,
                           null,
                           null,
                           null,
                           null);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_2",
                           "COLUMN_2_14",
                           ++i,
                           rowNo,
                           50,
                           null,
                           null,
                           null,
                           null);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_2",
                           "COLUMN_2_15",
                           ++i,
                           rowNo,
                           250,
                           null,
                           null,
                           null,
                           null);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_2",
                           "COLUMN_2_16",
                           ++i,
                           rowNo,
                           100,
                           null,
                           null,
                           null,
                           null);
    if (isDebug) {
      logger.debug("End");
    }
  }

  private void prepDmlStmntInsertTestTable3(PreparedStatement preparedStatement, long rowNo) {
    if (isDebug) {
      logger.debug("Start");
    }

    int i = 0;

    prepStmntColBigint(preparedStatement,
                       "TEST_TABLE_3",
                       "COLUMN_3_01",
                       ++i,
                       rowNo,
                       null,
                       null,
                       null,
                       null);
    prepStmntColBlobOpt(preparedStatement,
                        "TEST_TABLE_3",
                        "COLUMN_3_02",
                        ++i,
                        rowNo);
    prepStmntColTimestamp(preparedStatement,
                          "TEST_TABLE_3",
                          "COLUMN_3_03",
                          ++i,
                          rowNo);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_3",
                           "COLUMN_3_04",
                           ++i,
                           rowNo,
                           50,
                           null,
                           null,
                           null,
                           null);
    prepStmntColTimestampOpt(preparedStatement,
                             "TEST_TABLE_3",
                             "COLUMN_3_05",
                             ++i,
                             rowNo);
    prepStmntColVarchar(preparedStatement,
                        "TEST_TABLE_3",
                        "COLUMN_3_06",
                        ++i,
                        rowNo,
                        100,
                        null,
                        null,
                        null,
                        null);
    if (isDebug) {
      logger.debug("End");
    }
  }

  private void prepDmlStmntInsertTestTable4(PreparedStatement preparedStatement, long rowNo) {
    if (isDebug) {
      logger.debug("Start");
    }

    int i = 0;

    prepStmntColBigint(preparedStatement,
                       "TEST_TABLE_4",
                       "COLUMN_4_01",
                       ++i,
                       rowNo,
                       null,
                       null,
                       null,
                       null);
    prepStmntColBigint(preparedStatement,
                       "TEST_TABLE_4",
                       "COLUMN_4_02",
                       ++i,
                       rowNo,
                       null,
                       null,
                       null,
                       null);
    prepStmntColBigint(preparedStatement,
                       "TEST_TABLE_4",
                       "COLUMN_4_03",
                       ++i,
                       rowNo,
                       null,
                       null,
                       null,
                       null);
    prepStmntColBlobOpt(preparedStatement,
                        "TEST_TABLE_4",
                        "COLUMN_4_04",
                        ++i,
                        rowNo);
    prepStmntColTimestamp(preparedStatement,
                          "TEST_TABLE_4",
                          "COLUMN_4_05",
                          ++i,
                          rowNo);
    prepStmntColTimestampOpt(preparedStatement,
                             "TEST_TABLE_4",
                             "COLUMN_4_06",
                             ++i,
                             rowNo);
    prepStmntColVarchar(preparedStatement,
                        "TEST_TABLE_4",
                        "COLUMN_4_07",
                        ++i,
                        rowNo,
                        100,
                        null,
                        null,
                        null,
                        null);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_4",
                           "COLUMN_4_08",
                           ++i,
                           rowNo,
                           50,
                           null,
                           null,
                           null,
                           null);
    if (isDebug) {
      logger.debug("End");
    }
  }

  private void prepDmlStmntInsertTestTable5(PreparedStatement preparedStatement, long rowNo) {
    if (isDebug) {
      logger.debug("Start");
    }

    int i = 0;

    prepStmntColBigint(preparedStatement,
                       "TEST_TABLE_5",
                       "COLUMN_5_01",
                       ++i,
                       rowNo,
                       null,
                       null,
                       null,
                       null);
    prepStmntColVarchar(preparedStatement,
                        "TEST_TABLE_5",
                        "COLUMN_5_02",
                        ++i,
                        rowNo,
                        50,
                        null,
                        null,
                        null,
                        null);
    prepStmntColTimestamp(preparedStatement,
                          "TEST_TABLE_5",
                          "COLUMN_5_03",
                          ++i,
                          rowNo);
    prepStmntColTimestampOpt(preparedStatement,
                             "TEST_TABLE_5",
                             "COLUMN_5_04",
                             ++i,
                             rowNo);
    prepStmntColVarchar(preparedStatement,
                        "TEST_TABLE_5",
                        "COLUMN_5_05",
                        ++i,
                        rowNo,
                        100,
                        null,
                        null,
                        null,
                        null);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_5",
                           "COLUMN_5_06",
                           ++i,
                           rowNo,
                           4000,
                           null,
                           null,
                           null,
                           null);
    if (isDebug) {
      logger.debug("End");
    }
  }

  private void prepDmlStmntInsertTestTable6(PreparedStatement preparedStatement, long rowNo) {
    if (isDebug) {
      logger.debug("Start");
    }

    int i = 0;

    prepStmntColBigintOpt(preparedStatement,
                          "TEST_TABLE_6",
                          "COLUMN_6_01",
                          ++i,
                          rowNo,
                          4711,
                          4,
                          8,
                          null);
    prepStmntColBigintOpt(preparedStatement,
                          "TEST_TABLE_6",
                          "COLUMN_6_02",
                          ++i,
                          rowNo,
                          5,
                          null,
                          null,
                          Arrays.asList(0,
                                        2,
                                        4,
                                        6,
                                        8));
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_6",
                           "COLUMN_6_03",
                           ++i,
                           rowNo,
                           10,
                           "default",
                           "A",
                           "zzzzzzzzzz",
                           null);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_6",
                           "COLUMN_6_04",
                           ++i,
                           rowNo,
                           5,
                           "x",
                           null,
                           null,
                           Arrays.asList("a",
                                         "B",
                                         "c",
                                         "D",
                                         "e",
                                         "f",
                                         "G",
                                         "h",
                                         "I"));
    if (isDebug) {
      logger.debug("End");
    }
  }

}
