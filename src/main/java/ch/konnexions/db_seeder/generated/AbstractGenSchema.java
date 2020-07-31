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
 * @since   2020-07-31
 */
abstract class AbstractGenSchema extends AbstractJdbcSeeder {

  protected static final String TABLE_NAME_CITY          = "CITY";
  protected static final String TABLE_NAME_COMPANY       = "COMPANY";
  protected static final String TABLE_NAME_COUNTRY       = "COUNTRY";
  protected static final String TABLE_NAME_COUNTRY_STATE = "COUNTRY_STATE";
  protected static final String TABLE_NAME_TIMEZONE      = "TIMEZONE";

  private static final Logger   logger                   = Logger.getLogger(AbstractGenSchema.class);

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
    columnName.setProperty("ABBREVIATION_0",
                           "");
    columnName.setProperty("ACTIVE_0",
                           "");
    columnName.setProperty("ADDRESS1_0",
                           "");
    columnName.setProperty("ADDRESS2_0",
                           "");
    columnName.setProperty("ADDRESS3_0",
                           "");
    columnName.setProperty("EMAIL_0",
                           "");
    columnName.setProperty("FAX_0",
                           "");
    columnName.setProperty("ISO3166_0",
                           "");
    columnName.setProperty("NAME_0",
                           "");
    columnName.setProperty("PHONE_0",
                           "");
    columnName.setProperty("POSTAL_CODE_0",
                           "");
    columnName.setProperty("SYMBOL_0",
                           "");
    columnName.setProperty("URL_0",
                           "");
    columnName.setProperty("VAT_ID_NUMBER_0",
                           "");
    columnName.setProperty("V_TIME_ZONE_0",
                           "");

    // Encoding ISO_8859_1
    boolean isIso_8859_1 = config.getEncodingIso_8859_1();

    columnName.setProperty("ABBREVIATION_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("ACTIVE_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("ADDRESS1_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("ADDRESS2_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("ADDRESS3_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("EMAIL_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("FAX_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("ISO3166_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("NAME_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("PHONE_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("POSTAL_CODE_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("SYMBOL_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("URL_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("VAT_ID_NUMBER_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    columnName.setProperty("V_TIME_ZONE_1",
                           isIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");

    // Encoding UTF_8
    boolean isUtf_8 = config.getEncodingUtf_8();

    columnName.setProperty("ABBREVIATION_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("ACTIVE_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("ADDRESS1_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("ADDRESS2_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("ADDRESS3_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("EMAIL_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("FAX_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("ISO3166_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("NAME_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("PHONE_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("POSTAL_CODE_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("SYMBOL_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("URL_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("VAT_ID_NUMBER_2",
                           isUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    columnName.setProperty("V_TIME_ZONE_2",
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
                           put(TABLE_NAME_CITY,
                               "PK_CITY_ID,FK_COUNTRY_STATE_ID,CITY_MAP,CREATED,MODIFIED,NAME) VALUES (?,?,?,?,?,?");
                           put(TABLE_NAME_COMPANY,
                               "PK_COMPANY_ID,FK_CITY_ID,ACTIVE,ADDRESS1,ADDRESS2,ADDRESS3,CREATED,DIRECTIONS,EMAIL,FAX,MODIFIED,NAME,PHONE,POSTAL_CODE,URL,VAT_ID_NUMBER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
                           put(TABLE_NAME_COUNTRY,
                               "PK_COUNTRY_ID,COUNTRY_MAP,CREATED,ISO3166,MODIFIED,NAME) VALUES (?,?,?,?,?,?");
                           put(TABLE_NAME_COUNTRY_STATE,
                               "PK_COUNTRY_STATE_ID,FK_COUNTRY_ID,FK_TIMEZONE_ID,COUNTRY_STATE_MAP,CREATED,MODIFIED,NAME,SYMBOL) VALUES (?,?,?,?,?,?,?,?");
                           put(TABLE_NAME_TIMEZONE,
                               "PK_TIMEZONE_ID,ABBREVIATION,CREATED,MODIFIED,NAME,V_TIME_ZONE) VALUES (?,?,?,?,?,?");
                         }
                       };

    maxRowSizes        = new HashMap<>() {
                         {
                           put(TABLE_NAME_CITY,
                               1800);
                           put(TABLE_NAME_COMPANY,
                               5400);
                           put(TABLE_NAME_COUNTRY,
                               200);
                           put(TABLE_NAME_COUNTRY_STATE,
                               600);
                           put(TABLE_NAME_TIMEZONE,
                               11);
                         }
                       };

    TABLE_NAMES_CREATE = Arrays.asList(TABLE_NAME_COUNTRY,
                                       TABLE_NAME_TIMEZONE,
                                       TABLE_NAME_COUNTRY_STATE,
                                       TABLE_NAME_CITY,
                                       TABLE_NAME_COMPANY);

    TABLE_NAMES_DROP   = Arrays.asList(TABLE_NAME_COMPANY,
                                       TABLE_NAME_CITY,
                                       TABLE_NAME_COUNTRY_STATE,
                                       TABLE_NAME_TIMEZONE,
                                       TABLE_NAME_COUNTRY);

    if (isDebug) {
      logger.debug("End");
    }
  }
}
