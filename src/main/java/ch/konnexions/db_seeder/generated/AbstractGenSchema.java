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
    columnName.setProperty("COLUMN_6_03_0",
                           "");
    columnName.setProperty("COLUMN_6_04_0",
                           "");

    // Encoding ISO_8859_1
    boolean isIso_8859_1 = config.getEncodingIso_8859_1();

    columnName.setProperty("COLUMN_6_03_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("COLUMN_6_04_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");

    // Encoding UTF_8
    boolean isUtf_8 = config.getEncodingUtf_8();

    columnName.setProperty("COLUMN_6_03_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("COLUMN_6_04_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");

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
                           put(TABLE_NAME_TEST_TABLE_6,
                               "COLUMN_6_01,COLUMN_6_02,COLUMN_6_03,COLUMN_6_04) VALUES (?,?,?,?");
                         }
                       };

    maxRowSizes        = new HashMap<>() {
                         {
                           put(TABLE_NAME_TEST_TABLE_6,
                               10);
                         }
                       };

    TABLE_NAMES_CREATE = Arrays.asList(TABLE_NAME_TEST_TABLE_6);

    TABLE_NAMES_DROP   = Arrays.asList(TABLE_NAME_TEST_TABLE_6);

    if (isDebug) {
      logger.debug("End");
    }
  }
}
