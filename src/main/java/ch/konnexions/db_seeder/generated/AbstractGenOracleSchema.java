package ch.konnexions.db_seeder.generated;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * CREATE TABLE statements for a Oracle DBMS. <br>
 * 
 * @author  GenerateSchema.class
 * @version 2.0.0
 * @since   2020-07-30
 */
public abstract class AbstractGenOracleSchema extends AbstractGenSeeder {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  private static final Logger                 logger            = Logger.getLogger(AbstractGenOracleSchema.class);

  /**
   * Creates the CREATE TABLE statements.
   */
  @SuppressWarnings("preview")
  private static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<>();

    statements.put(TABLE_NAME_CITY,
                   """
                   CREATE TABLE CITY (
                       PK_CITY_ID                       NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       FK_COUNTRY_STATE_ID              NUMBER                    REFERENCES COUNTRY_STATE                    (PK_COUNTRY_STATE_ID),
                       CITY_MAP                         BLOB,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR2(100)             NOT NULL
                   )
                   """);

    statements.put(TABLE_NAME_COMPANY,
                   """
                   CREATE TABLE COMPANY (
                       PK_COMPANY_ID                    NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       FK_CITY_ID                       NUMBER                    NOT NULL
                                                                                  REFERENCES CITY                             (PK_CITY_ID),
                       FK_CITY_ID_DEFAULT               NUMBER                    DEFAULT 1
                                                                                  REFERENCES CITY                             (PK_CITY_ID),
                       ACTIVE                           VARCHAR2(1)               NOT NULL,
                       ADDRESS1                         VARCHAR2(50),
                       ADDRESS2                         VARCHAR2(50),
                       ADDRESS3                         VARCHAR2(50),
                       CREATED                          TIMESTAMP                 NOT NULL,
                       DIRECTIONS                       CLOB,
                       EMAIL                            VARCHAR2(100),
                       FAX                              VARCHAR2(50),
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR2(100)             NOT NULL
                                                                                  UNIQUE,
                       PHONE                            VARCHAR2(50),
                       POSTAL_CODE                      VARCHAR2(50),
                       URL                              VARCHAR2(250),
                       VAT_ID_NUMBER                    VARCHAR2(100)
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY,
                   """
                   CREATE TABLE COUNTRY (
                       PK_COUNTRY_ID                    NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       COUNTRY_MAP                      BLOB,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       ISO3166                          VARCHAR2(50),
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR2(100)             NOT NULL
                                                                                  UNIQUE
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY_STATE,
                   """
                   CREATE TABLE COUNTRY_STATE (
                       PK_COUNTRY_STATE_ID              NUMBER,
                       FK_COUNTRY_ID                    NUMBER                    NOT NULL
                                                                                  REFERENCES COUNTRY                          (PK_COUNTRY_ID),
                       FK_TIMEZONE_ID                   NUMBER                    NOT NULL
                                                                                  REFERENCES TIMEZONE                         (PK_TIMEZONE_ID),
                       COUNTRY_STATE_MAP                BLOB,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR2(100)             NOT NULL,
                       SYMBOL                           VARCHAR2(50),
                       CONSTRAINT CONSTRAINT_11       UNIQUE      (FK_COUNTRY_ID, NAME)
                   )
                   """);

    statements.put(TABLE_NAME_TIMEZONE,
                   """
                   CREATE TABLE TIMEZONE (
                       PK_TIMEZONE_ID                   NUMBER                    NOT NULL
                                                                                  PRIMARY KEY,
                       ABBREVIATION                     VARCHAR2(50)              NOT NULL,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR2(100)             NOT NULL
                                                                                  UNIQUE,
                       V_TIME_ZONE                      VARCHAR2(4000)
                   )
                   """);

    return statements;
  }

  /**
   * Initialises a new abstract Oracle schema object.
   *
   * @param dbmsTickerSymbol
   *            DBMS ticker symbol
   */
  public AbstractGenOracleSchema(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }
}
