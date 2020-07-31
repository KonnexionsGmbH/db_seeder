package ch.konnexions.db_seeder.generated;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * CREATE TABLE statements for a Mimer DBMS. <br>
 * 
 * @author  GenerateSchema.class
 * @version 2.0.0
 * @since   2020-07-31
 */
public abstract class AbstractGenMimerSchema extends AbstractGenSeeder {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  private static final Logger                 logger            = Logger.getLogger(AbstractGenMimerSchema.class);

  /**
   * Creates the CREATE TABLE statements.
   */
  @SuppressWarnings("preview")
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
                       NAME                             NVARCHAR(100)             NOT NULL
                   )
                   """);

    statements.put(TABLE_NAME_COMPANY,
                   """
                   CREATE TABLE COMPANY (
                       PK_COMPANY_ID                    BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       FK_CITY_ID                       BIGINT                    NOT NULL
                                                                                  REFERENCES CITY                             (PK_CITY_ID),
                       ACTIVE                           NVARCHAR(1)               NOT NULL,
                       ADDRESS1                         NVARCHAR(50),
                       ADDRESS2                         NVARCHAR(50),
                       ADDRESS3                         NVARCHAR(50),
                       CREATED                          TIMESTAMP                 NOT NULL,
                       DIRECTIONS                       CLOB,
                       EMAIL                            NVARCHAR(100),
                       FAX                              NVARCHAR(50),
                       MODIFIED                         TIMESTAMP,
                       NAME                             NVARCHAR(100)             NOT NULL
                                                                                  UNIQUE,
                       PHONE                            NVARCHAR(50),
                       POSTAL_CODE                      NVARCHAR(50),
                       URL                              NVARCHAR(250),
                       VAT_ID_NUMBER                    NVARCHAR(100)
                   )
                   """);

    statements.put(TABLE_NAME_COUNTRY,
                   """
                   CREATE TABLE COUNTRY (
                       PK_COUNTRY_ID                    BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       COUNTRY_MAP                      BLOB,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       ISO3166                          NVARCHAR(50),
                       MODIFIED                         TIMESTAMP,
                       NAME                             NVARCHAR(100)             NOT NULL
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
                       NAME                             NVARCHAR(100)             NOT NULL,
                       SYMBOL                           NVARCHAR(50),
                       CONSTRAINT CONSTRAINT_8        UNIQUE      (FK_COUNTRY_ID, NAME)
                   )
                   """);

    statements.put(TABLE_NAME_TIMEZONE,
                   """
                   CREATE TABLE TIMEZONE (
                       PK_TIMEZONE_ID                   BIGINT                    NOT NULL
                                                                                  PRIMARY KEY,
                       ABBREVIATION                     NVARCHAR(50)              NOT NULL,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             NVARCHAR(100)             NOT NULL
                                                                                  UNIQUE,
                       V_TIME_ZONE                      NVARCHAR(4000)
                   )
                   """);

    return statements;
  }

  /**
   * Initialises a new abstract Mimer schema object.
   *
   * @param dbmsTickerSymbol
   *            DBMS ticker symbol
   */
  public AbstractGenMimerSchema(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }
}
