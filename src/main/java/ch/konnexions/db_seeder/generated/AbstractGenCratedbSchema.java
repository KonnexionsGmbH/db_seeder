package ch.konnexions.db_seeder.generated;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * CREATE TABLE statements for a CrateDB DBMS. <br>
 * 
 * @author  GenerateSchema.class
 * @version 2.0.0
 * @since   2020-07-30
 */
public abstract class AbstractGenCratedbSchema extends AbstractGenSeeder {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  private static final Logger                 logger            = Logger.getLogger(AbstractGenCratedbSchema.class);

  /**
   * Creates the CREATE TABLE statements.
   */
  @SuppressWarnings("preview")
  private static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<>();

    statements.put(TABLE_NAME_CITY,
                   """
                   CREATE TABLE CITY (
                       PK_CITY_ID                       BIGINT                    PRIMARY KEY,
                       FK_COUNTRY_STATE_ID              BIGINT                    REFERENCES COUNTRY_STATE                    (PK_COUNTRY_STATE_ID),
                       CITY_MAP                         OBJECT,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             TEXT                      NOT NULL
                   )
                   """);

    statements.put(TABLE_NAME_COMPANY,
                   """
                   CREATE TABLE COMPANY (
                       PK_COMPANY_ID                    BIGINT                    PRIMARY KEY,
                       FK_CITY_ID                       BIGINT                    NOT NULL
                                                                                  REFERENCES CITY                             (PK_CITY_ID),
                       FK_CITY_ID_DEFAULT               BIGINT                    DEFAULT 1
                                                                                  REFERENCES CITY                             (PK_CITY_ID),
                       ACTIVE                           TEXT                      NOT NULL,
                       ADDRESS1                         TEXT,
                       ADDRESS2                         TEXT,
                       ADDRESS3                         TEXT,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       DIRECTIONS                       TEXT,
                       EMAIL                            TEXT,
                       FAX                              TEXT,
                       MODIFIED                         TIMESTAMP,
                       NAME                             TEXT                      NOT NULL
                                                                                  UNIQUE,
                       PHONE                            TEXT,
                       POSTAL_CODE                      TEXT,
                       URL                              TEXT,
                       VAT_ID_NUMBER                    TEXT
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY,
                   """
                   CREATE TABLE COUNTRY (
                       PK_COUNTRY_ID                    BIGINT                    PRIMARY KEY,
                       COUNTRY_MAP                      OBJECT,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       ISO3166                          TEXT,
                       MODIFIED                         TIMESTAMP,
                       NAME                             TEXT                      NOT NULL
                                                                                  UNIQUE
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY_STATE,
                   """
                   CREATE TABLE COUNTRY_STATE (
                       PK_COUNTRY_STATE_ID              BIGINT,
                       FK_COUNTRY_ID                    BIGINT                    NOT NULL
                                                                                  REFERENCES COUNTRY                          (PK_COUNTRY_ID),
                       FK_TIMEZONE_ID                   BIGINT                    NOT NULL
                                                                                  REFERENCES TIMEZONE                         (PK_TIMEZONE_ID),
                       COUNTRY_STATE_MAP                OBJECT,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             TEXT                      NOT NULL,
                       SYMBOL                           TEXT
                   )
                   """);

    statements.put(TABLE_NAME_TIMEZONE,
                   """
                   CREATE TABLE TIMEZONE (
                       PK_TIMEZONE_ID                   BIGINT                    PRIMARY KEY,
                       ABBREVIATION                     TEXT                      NOT NULL,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             TEXT                      NOT NULL
                                                                                  UNIQUE,
                       V_TIME_ZONE                      TEXT
                   )
                   """);

    return statements;
  }

  /**
   * Initialises a new abstract CrateDB schema object.
   *
   * @param dbmsTickerSymbol
   *            DBMS ticker symbol
   */
  public AbstractGenCratedbSchema(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }
}
