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
   */
  @Override
  protected final long getContentBigint(String tableName, String columnName, long rowNo) {
    long result = super.getContentBigint(tableName,
                                         columnName,
                                         rowNo);

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
  protected final byte[] getContentBlob(String tableName, String columnName, int rowNo) {
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
  protected final String getContentClob(String tableName, String columnName, int rowNo) {
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
  protected final Object getContentFk(String tableName, String columnName, int rowNo, ArrayList<Object> fkList) {
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
  protected final Timestamp getContentTimestamp(String tableName, String columnName, int rowNo) {
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
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  @Override
  protected final String getContentVarchar(String tableName,
                                           String columnName,
                                           int rowNo,
                                           int size,
                                           String lowerRange,
                                           String upperRange,
                                           List<String> validValues) {
    String result = super.getContentVarchar(tableName,
                                            columnName,
                                            rowNo,
                                            size,
                                            lowerRange,
                                            upperRange,
                                            validValues);

    return result;
  }

  protected final void insertTable(PreparedStatement preparedStatement, final String tableName, final int rowNo) {
    if (isDebug) {
      logger.debug("Start");
    }

    switch (tableName) {
    case TABLE_NAME_TEST_TABLE_6 -> prepDmlStmntInsertTestTable6(preparedStatement,
                                                                 rowNo);
    default -> throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME,
                                                                                                    tableName));
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void prepDmlStmntInsertTestTable6(PreparedStatement preparedStatement, int rowNo) {
    if (isDebug) {
      logger.debug("Start");
    }

    int i = 0;

    prepStmntColBigintOpt(preparedStatement,
                          "TEST_TABLE_6",
                          "COLUMN_6_01",
                          ++i,
                          rowNo);
    prepStmntColBigintOpt(preparedStatement,
                          "TEST_TABLE_6",
                          "COLUMN_6_02",
                          ++i,
                          rowNo);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_6",
                           "COLUMN_6_03",
                           ++i,
                           rowNo,
                           1,
                           "3",
                           "6",
                           null);
    prepStmntColVarcharOpt(preparedStatement,
                           "TEST_TABLE_6",
                           "COLUMN_6_04",
                           ++i,
                           rowNo,
                           5,
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
