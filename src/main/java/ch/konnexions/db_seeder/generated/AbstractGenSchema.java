package ch.konnexions.db_seeder.generated;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a Database - Abstract Generated Schema.
 * <br>
 * @author  GenerateSchema.class
 * @version 2.0.0
 * @since   2020-07-30
 */
abstract class AbstractGenSchema extends AbstractJdbcSeeder {

  protected static final String TABLE_NAME_TEST_TABLE_1 = "TEST_TABLE_1";
  protected static final String TABLE_NAME_TEST_TABLE_2 = "TEST_TABLE_2";
  protected static final String TABLE_NAME_TEST_TABLE_3 = "TEST_TABLE_3";
  protected static final String TABLE_NAME_TEST_TABLE_4 = "TEST_TABLE_4";
  protected static final String TABLE_NAME_TEST_TABLE_5 = "TEST_TABLE_5";
  protected static final String TABLE_NAME_TEST_TABLE_6 = "TEST_TABLE_6";

  private static final Logger   logger                  = Logger.getLogger(AbstractGenSchema.class);

  /**
   * Initialises a new abstract generated schema object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public AbstractGenSchema(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    initConstants();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Initialises a new abstract generated schema object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   * @param isClient client database version
   */
  public AbstractGenSchema(String dbmsTickerSymbol, boolean isClient) {
    super(dbmsTickerSymbol, isClient);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol + " - isClient=" + isClient);
    }

    initConstants();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  protected final Properties createColumnNames() {
    if (isDebug) {
      logger.debug("Start");
    }

    Properties columnName = new Properties();

    // Encoding ASCII
    columnName.setProperty("COLUMN_1_06_0",
                           "");
    columnName.setProperty("COLUMN_2_03_0",
                           "");
    columnName.setProperty("COLUMN_2_04_0",
                           "");
    columnName.setProperty("COLUMN_2_05_0",
                           "");
    columnName.setProperty("COLUMN_2_06_0",
                           "");
    columnName.setProperty("COLUMN_2_09_0",
                           "");
    columnName.setProperty("COLUMN_2_10_0",
                           "");
    columnName.setProperty("COLUMN_2_12_0",
                           "");
    columnName.setProperty("COLUMN_2_13_0",
                           "");
    columnName.setProperty("COLUMN_2_14_0",
                           "");
    columnName.setProperty("COLUMN_2_15_0",
                           "");
    columnName.setProperty("COLUMN_2_16_0",
                           "");
    columnName.setProperty("COLUMN_3_04_0",
                           "");
    columnName.setProperty("COLUMN_3_06_0",
                           "");
    columnName.setProperty("COLUMN_4_07_0",
                           "");
    columnName.setProperty("COLUMN_4_08_0",
                           "");
    columnName.setProperty("COLUMN_5_02_0",
                           "");
    columnName.setProperty("COLUMN_5_05_0",
                           "");
    columnName.setProperty("COLUMN_5_06_0",
                           "");
    columnName.setProperty("COLUMN_6_03_0",
                           "");
    columnName.setProperty("COLUMN_6_04_0",
                           "");

    // Encoding ISO_8859_1
    boolean isIso_8859_1 = config.getEncodingIso_8859_1();

    columnName.setProperty("COLUMN_1_06_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_2_03_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_2_04_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_2_05_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_2_06_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_2_09_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_2_10_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_2_12_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_2_13_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_2_14_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_2_15_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_2_16_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_3_04_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_3_06_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_4_07_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_4_08_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_5_02_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_5_05_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_5_06_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_6_03_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_6_04_1",
                           isIso_8859_1
                               ? "ÁÇÉÍÑÓ_"
                               : "NO_ISO_8859_1_");

    // Encoding UTF_8
    boolean isUtf_8 = config.getEncodingUtf_8();

    columnName.setProperty("COLUMN_1_06_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_2_03_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_2_04_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_2_05_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_2_06_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_2_09_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_2_10_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_2_12_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_2_13_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_2_14_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_2_15_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_2_16_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_3_04_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_3_06_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_4_07_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_4_08_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_5_02_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_5_05_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_5_06_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_6_03_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");
    columnName.setProperty("COLUMN_6_04_2",
                           isUtf_8
                               ? "缩略语地址电子邮件传真_"
                               : "NO_UTF_8_");

    if (isDebug) {
      logger.debug("End");
    }

    return columnName;
  }

  /**
   * Initialising constants.
   */
  @SuppressWarnings("serial")
  private void initConstants() {
    if (isDebug) {
      logger.debug("Start");
    }

    dmlStatements      = new HashMap<>() {
                         {
                           put(TABLE_NAME_TEST_TABLE_1,
                               "COLUMN_1_01,COLUMN_1_02,COLUMN_1_03,COLUMN_1_04,COLUMN_1_05,COLUMN_1_06) VALUES (?,?,?,?,?,?");
                           put(TABLE_NAME_TEST_TABLE_2,
                               "COLUMN_2_01,COLUMN_2_02,COLUMN_2_03,COLUMN_2_04,COLUMN_2_05,COLUMN_2_06,COLUMN_2_07,COLUMN_2_08,COLUMN_2_09,COLUMN_2_10,COLUMN_2_11,COLUMN_2_12,COLUMN_2_13,COLUMN_2_14,COLUMN_2_15,COLUMN_2_16) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
                           put(TABLE_NAME_TEST_TABLE_3,
                               "COLUMN_3_01,COLUMN_3_02,COLUMN_3_03,COLUMN_3_04,COLUMN_3_05,COLUMN_3_06) VALUES (?,?,?,?,?,?");
                           put(TABLE_NAME_TEST_TABLE_4,
                               "COLUMN_4_01,COLUMN_4_02,COLUMN_4_03,COLUMN_4_04,COLUMN_4_05,COLUMN_4_06,COLUMN_4_07,COLUMN_4_08) VALUES (?,?,?,?,?,?,?,?");
                           put(TABLE_NAME_TEST_TABLE_5,
                               "COLUMN_5_01,COLUMN_5_02,COLUMN_5_03,COLUMN_5_04,COLUMN_5_05,COLUMN_5_06) VALUES (?,?,?,?,?,?");
                           put(TABLE_NAME_TEST_TABLE_6,
                               "COLUMN_6_01,COLUMN_6_02,COLUMN_6_03,COLUMN_6_04) VALUES (?,?,?,?");
                         }
                       };

    maxRowSizes        = new HashMap<>() {
                         {
                           put(TABLE_NAME_TEST_TABLE_1,
                               1800);
                           put(TABLE_NAME_TEST_TABLE_2,
                               5400);
                           put(TABLE_NAME_TEST_TABLE_3,
                               200);
                           put(TABLE_NAME_TEST_TABLE_4,
                               600);
                           put(TABLE_NAME_TEST_TABLE_5,
                               11);
                           put(TABLE_NAME_TEST_TABLE_6,
                               1000);
                         }
                       };

    TABLE_NAMES_CREATE = Arrays.asList(TABLE_NAME_TEST_TABLE_1,
                                       TABLE_NAME_TEST_TABLE_2,
                                       TABLE_NAME_TEST_TABLE_3,
                                       TABLE_NAME_TEST_TABLE_4,
                                       TABLE_NAME_TEST_TABLE_5,
                                       TABLE_NAME_TEST_TABLE_6);

    TABLE_NAMES_DROP   = Arrays.asList(TABLE_NAME_TEST_TABLE_6,
                                       TABLE_NAME_TEST_TABLE_5,
                                       TABLE_NAME_TEST_TABLE_4,
                                       TABLE_NAME_TEST_TABLE_3,
                                       TABLE_NAME_TEST_TABLE_2,
                                       TABLE_NAME_TEST_TABLE_1);

    if (isDebug) {
      logger.debug("End");
    }
  }
}
