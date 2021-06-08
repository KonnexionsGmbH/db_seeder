package ch.konnexions.db_seeder.generated;

import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * CREATE TABLE statements for a Oracle DBMS. <br>
 * 
 * @author  CreateSummaryFile.class
 * @version 2.9.1
 */
public abstract class AbstractGenOracleSchema extends AbstractGenSeeder {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  private static final Logger                 logger            = LogManager.getLogger(AbstractGenOracleSchema.class);

  /**
   * Create the CREATE TABLE statements.
   */
  private static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<>();

    statements.put(TABLE_NAME_CITY,
                   """
                   CREATE TABLE city (
                       pk_city_id                       NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       fk_country_state_id              NUMBER                    REFERENCES country_state                    (pk_country_state_id),
                       city_map                         BLOB,
                       created                          TIMESTAMP                 NOT NULL,
                       modified                         TIMESTAMP,
                       name                             VARCHAR2(100)             NOT NULL
                   )
                   """);

    statements.put(TABLE_NAME_COMPANY,
                   """
                   CREATE TABLE company (
                       pk_company_id                    NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       fk_city_id                       NUMBER                    NOT NULL
                                                                                  REFERENCES city                             (pk_city_id),
                       active                           VARCHAR2(1)               NOT NULL,
                       address1                         VARCHAR2(50),
                       address2                         VARCHAR2(50),
                       address3                         VARCHAR2(50),
                       created                          TIMESTAMP                 NOT NULL,
                       directions                       CLOB,
                       email                            VARCHAR2(100),
                       fax                              VARCHAR2(50),
                       modified                         TIMESTAMP,
                       name                             VARCHAR2(100)             NOT NULL
                                                                                  UNIQUE,
                       phone                            VARCHAR2(50),
                       postal_code                      VARCHAR2(50),
                       url                              VARCHAR2(250),
                       vat_id_number                    VARCHAR2(100)
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY,
                   """
                   CREATE TABLE country (
                       pk_country_id                    NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       country_map                      BLOB,
                       created                          TIMESTAMP                 NOT NULL,
                       iso3166                          VARCHAR2(50),
                       modified                         TIMESTAMP,
                       name                             VARCHAR2(100)             NOT NULL
                                                                                  UNIQUE
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY_STATE,
                   """
                   CREATE TABLE country_state (
                       pk_country_state_id              NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       fk_country_id                    NUMBER                    NOT NULL
                                                                                  REFERENCES country                          (pk_country_id),
                       fk_timezone_id                   NUMBER                    NOT NULL
                                                                                  REFERENCES timezone                         (pk_timezone_id),
                       country_state_map                BLOB,
                       created                          TIMESTAMP                 NOT NULL,
                       modified                         TIMESTAMP,
                       name                             VARCHAR2(100)             NOT NULL,
                       symbol                           VARCHAR2(50),
                       CONSTRAINT CONSTRAINT_KXN_13   UNIQUE      (fk_country_id, name)
                   )
                   """);

    statements.put(TABLE_NAME_TIMEZONE,
                   """
                   CREATE TABLE timezone (
                       pk_timezone_id                   NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       abbreviation                     VARCHAR2(50)              NOT NULL,
                       created                          TIMESTAMP                 NOT NULL,
                       modified                         TIMESTAMP,
                       name                             VARCHAR2(100)             NOT NULL
                                                                                  UNIQUE,
                       v_time_zone                      VARCHAR2(4000)
                   )
                   """);

    return statements;
  }

  private final boolean                        isDebug           = logger.isDebugEnabled();

  /**
   * Initialises a new abstract Oracle schema object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   */
  public AbstractGenOracleSchema(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Initialises a new abstract Oracle schema object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption client, embedded or trino
   */
  public AbstractGenOracleSchema(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    createColumnNames(true, true);

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }


  protected final void createColumnNames(boolean isEncodingIso_8859_1, boolean isEncodingUtf_8) {
    if (isDebug) {
      logger.debug("Start");
    }

    encodedColumnNames = new Properties();

    // Encoding ASCII
    encodedColumnNames.setProperty("ABBREVIATION_0",
                           "");
    encodedColumnNames.setProperty("ACTIVE_0",
                           "");
    encodedColumnNames.setProperty("ADDRESS1_0",
                           "");
    encodedColumnNames.setProperty("ADDRESS2_0",
                           "");
    encodedColumnNames.setProperty("ADDRESS3_0",
                           "");
    encodedColumnNames.setProperty("EMAIL_0",
                           "");
    encodedColumnNames.setProperty("FAX_0",
                           "");
    encodedColumnNames.setProperty("ISO3166_0",
                           "");
    encodedColumnNames.setProperty("NAME_0",
                           "");
    encodedColumnNames.setProperty("PHONE_0",
                           "");
    encodedColumnNames.setProperty("POSTAL_CODE_0",
                           "");
    encodedColumnNames.setProperty("SYMBOL_0",
                           "");
    encodedColumnNames.setProperty("URL_0",
                           "");
    encodedColumnNames.setProperty("VAT_ID_NUMBER_0",
                           "");
    encodedColumnNames.setProperty("V_TIME_ZONE_0",
                           "");

    // Encoding ISO_8859_1

    encodedColumnNames.setProperty("ABBREVIATION_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("ACTIVE_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("ADDRESS1_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("ADDRESS2_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("ADDRESS3_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("EMAIL_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("FAX_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("ISO3166_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("NAME_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("PHONE_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("POSTAL_CODE_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("SYMBOL_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("URL_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("VAT_ID_NUMBER_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("V_TIME_ZONE_1",
                           isEncodingIso_8859_1 ? "ÁÇÉÍÑÓ_" : "NO_ISO_8859_1_");

    // Encoding UTF_8

    encodedColumnNames.setProperty("ABBREVIATION_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("ACTIVE_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("ADDRESS1_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("ADDRESS2_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("ADDRESS3_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("EMAIL_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("FAX_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("ISO3166_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("NAME_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("PHONE_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("POSTAL_CODE_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("SYMBOL_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("URL_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("VAT_ID_NUMBER_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");
    encodedColumnNames.setProperty("V_TIME_ZONE_2",
                           isEncodingUtf_8 ? "缩略语地址电子邮件传真_" : "NO_UTF_8_");

    if (isDebug) {
      logger.debug("End");
    }  }
}

