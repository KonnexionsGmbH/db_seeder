package ch.konnexions.db_seeder.generated;

import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * CREATE TABLE statements for a SQLite DBMS. <br>
 * 
 * @author  CreateSummaryFile.class
 * @version 3.0.7
 */
public abstract class AbstractGenSqliteSchema extends AbstractGenSeeder {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  private static final Logger                 logger            = LogManager.getLogger(AbstractGenSqliteSchema.class);

  /**
   * Create the CREATE TABLE statements.
   */
  private static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<>();

    statements.put(TABLE_NAME_CITY,
                   """
                   CREATE TABLE CITY (
                       PK_CITY_ID                       INTEGER                   NOT NULL
                                                                                  PRIMARY KEY,
                       FK_COUNTRY_STATE_ID              INTEGER                   REFERENCES COUNTRY_STATE                    (PK_COUNTRY_STATE_ID),
                       CITY_MAP                         BLOB,
                       CREATED                          DATETIME                  NOT NULL,
                       MODIFIED                         DATETIME,
                       NAME                             VARCHAR2(100)             NOT NULL
                   )
                   """);

    statements.put(TABLE_NAME_COMPANY,
                   """
                   CREATE TABLE COMPANY (
                       PK_COMPANY_ID                    INTEGER                   NOT NULL
                                                                                  PRIMARY KEY,
                       FK_CITY_ID                       INTEGER                   NOT NULL
                                                                                  REFERENCES CITY                             (PK_CITY_ID),
                       ACTIVE                           VARCHAR2(1)               NOT NULL,
                       ADDRESS1                         VARCHAR2(50),
                       ADDRESS2                         VARCHAR2(50),
                       ADDRESS3                         VARCHAR2(50),
                       CREATED                          DATETIME                  NOT NULL,
                       DIRECTIONS                       CLOB,
                       EMAIL                            VARCHAR2(100),
                       FAX                              VARCHAR2(50),
                       MODIFIED                         DATETIME,
                       NAME                             VARCHAR2(100)             NOT NULL,
                       PHONE                            VARCHAR2(50),
                       POSTAL_CODE                      VARCHAR2(50),
                       URL                              VARCHAR2(250),
                       VAT_ID_NUMBER                    VARCHAR2(100),
                       CONSTRAINT KXN_61                UNIQUE      (name)
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY,
                   """
                   CREATE TABLE COUNTRY (
                       PK_COUNTRY_ID                    INTEGER                   NOT NULL
                                                                                  PRIMARY KEY,
                       COUNTRY_MAP                      BLOB,
                       CREATED                          DATETIME                  NOT NULL,
                       ISO3166                          VARCHAR2(50),
                       MODIFIED                         DATETIME,
                       NAME                             VARCHAR2(100)             NOT NULL,
                       CONSTRAINT KXN_62                UNIQUE      (name)
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY_STATE,
                   """
                   CREATE TABLE COUNTRY_STATE (
                       PK_COUNTRY_STATE_ID              INTEGER                   NOT NULL
                                                                                  PRIMARY KEY,
                       FK_COUNTRY_ID                    INTEGER                   NOT NULL
                                                                                  REFERENCES COUNTRY                          (PK_COUNTRY_ID),
                       FK_TIMEZONE_ID                   INTEGER                   NOT NULL
                                                                                  REFERENCES TIMEZONE                         (PK_TIMEZONE_ID),
                       COUNTRY_STATE_MAP                BLOB,
                       CREATED                          DATETIME                  NOT NULL,
                       MODIFIED                         DATETIME,
                       NAME                             VARCHAR2(100)             NOT NULL,
                       SYMBOL                           VARCHAR2(50),
                       CONSTRAINT KXN_63                UNIQUE      (fk_country_id, name)
                   )
                   """);

    statements.put(TABLE_NAME_TIMEZONE,
                   """
                   CREATE TABLE TIMEZONE (
                       PK_TIMEZONE_ID                   INTEGER                   NOT NULL
                                                                                  PRIMARY KEY,
                       ABBREVIATION                     VARCHAR2(50)              NOT NULL,
                       CREATED                          DATETIME                  NOT NULL,
                       MODIFIED                         DATETIME,
                       NAME                             VARCHAR2(100)             NOT NULL,
                       V_TIME_ZONE                      VARCHAR2(4000),
                       CONSTRAINT KXN_64                UNIQUE      (name)
                   )
                   """);

    return statements;
  }

  private final boolean isDebug = logger.isDebugEnabled();

  /**
   * Initialises a new abstract SQLite schema object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption client, embedded or trino
   */
  public AbstractGenSqliteSchema(String tickerSymbolExtern, String dbmsOption) {
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
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("ACTIVE_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("ADDRESS1_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("ADDRESS2_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("ADDRESS3_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("EMAIL_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("FAX_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("ISO3166_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("NAME_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("PHONE_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("POSTAL_CODE_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("SYMBOL_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("URL_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("VAT_ID_NUMBER_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");
    encodedColumnNames.setProperty("V_TIME_ZONE_1",
                                   isEncodingIso_8859_1
                                       ? "ÁÇÉÍÑÓ_"
                                       : "NO_ISO_8859_1_");

    // Encoding UTF_8

    encodedColumnNames.setProperty("ABBREVIATION_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("ACTIVE_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("ADDRESS1_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("ADDRESS2_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("ADDRESS3_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("EMAIL_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("FAX_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("ISO3166_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("NAME_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("PHONE_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("POSTAL_CODE_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("SYMBOL_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("URL_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("VAT_ID_NUMBER_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");
    encodedColumnNames.setProperty("V_TIME_ZONE_2",
                                   isEncodingUtf_8
                                       ? "缩略语地址电子邮件传真_"
                                       : "NO_UTF_8_");

    if (isDebug) {
      logger.debug("End");
    }
  }
}
