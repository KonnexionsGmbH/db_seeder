package ch.konnexions.db_seeder.generated;

import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * CREATE TABLE statements for a IBM Db2 DBMS. <br>
 * 
 * @author  GenerateSchema.class
 * @version 2.5.1
 */
public abstract class AbstractGenIbmdb2Schema extends AbstractGenSeeder {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  private static final Logger                 logger            = Logger.getLogger(AbstractGenIbmdb2Schema.class);

  /**
   * Create the CREATE TABLE statements.
   */
  private static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<>();

    statements.put(TABLE_NAME_CITY,
                   """
                   CREATE TABLE CITY (
                       PK_CITY_ID                       BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       FK_COUNTRY_STATE_ID              BIGINT                    REFERENCES COUNTRY_STATE                    (PK_COUNTRY_STATE_ID),
                       CITY_MAP                         BLOB,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR(100)              NOT NULL
                   )
                   """);

    statements.put(TABLE_NAME_COMPANY,
                   """
                   CREATE TABLE COMPANY (
                       PK_COMPANY_ID                    BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       FK_CITY_ID                       BIGINT                    NOT NULL
                                                                                  REFERENCES CITY                             (PK_CITY_ID),
                       ACTIVE                           VARCHAR(1)                NOT NULL,
                       ADDRESS1                         VARCHAR(50),
                       ADDRESS2                         VARCHAR(50),
                       ADDRESS3                         VARCHAR(50),
                       CREATED                          TIMESTAMP                 NOT NULL,
                       DIRECTIONS                       CLOB,
                       EMAIL                            VARCHAR(100),
                       FAX                              VARCHAR(50),
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR(100)              NOT NULL
                                                                                  UNIQUE,
                       PHONE                            VARCHAR(50),
                       POSTAL_CODE                      VARCHAR(50),
                       URL                              VARCHAR(250),
                       VAT_ID_NUMBER                    VARCHAR(100)
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY,
                   """
                   CREATE TABLE COUNTRY (
                       PK_COUNTRY_ID                    BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       COUNTRY_MAP                      BLOB,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       ISO3166                          VARCHAR(50),
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR(100)              NOT NULL
                                                                                  UNIQUE
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY_STATE,
                   """
                   CREATE TABLE COUNTRY_STATE (
                       PK_COUNTRY_STATE_ID              BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       FK_COUNTRY_ID                    BIGINT                    NOT NULL
                                                                                  REFERENCES COUNTRY                          (PK_COUNTRY_ID),
                       FK_TIMEZONE_ID                   BIGINT                    NOT NULL
                                                                                  REFERENCES TIMEZONE                         (PK_TIMEZONE_ID),
                       COUNTRY_STATE_MAP                BLOB,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR(100)              NOT NULL,
                       SYMBOL                           VARCHAR(50),
                       CONSTRAINT CONSTRAINT_7        UNIQUE      (fk_country_id, name)
                   )
                   """);

    statements.put(TABLE_NAME_TIMEZONE,
                   """
                   CREATE TABLE TIMEZONE (
                       PK_TIMEZONE_ID                   BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       ABBREVIATION                     VARCHAR(50)               NOT NULL,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR(100)              NOT NULL
                                                                                  UNIQUE,
                       V_TIME_ZONE                      VARCHAR(4000)
                   )
                   """);

    return statements;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Initialises a new abstract IBM Db2 schema object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   */
  public AbstractGenIbmdb2Schema(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Initialises a new abstract IBM Db2 schema object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption client, embedded or presto
   */
  public AbstractGenIbmdb2Schema(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    createColumnNames(true,
                      true);

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
    }
  }
}
