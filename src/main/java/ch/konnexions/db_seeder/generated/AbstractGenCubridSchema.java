package ch.konnexions.db_seeder.generated;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * CREATE TABLE statements for a CUBRID DBMS. <br>
 * 
 * @author  GenerateSchema.class
 * @version 2.0.0
 * @since   2020-07-30
 */
public abstract class AbstractGenCubridSchema extends AbstractGenSeeder {

  public static final HashMap<String, String> createTableStmnts = createTableStmnts();

  private static final Logger                 logger            = Logger.getLogger(AbstractGenCubridSchema.class);

  /**
   * Creates the CREATE TABLE statements.
   */
  @SuppressWarnings("preview")
  private static HashMap<String, String> createTableStmnts() {
    HashMap<String, String> statements = new HashMap<>();

    statements.put(TABLE_NAME_CITY,
                   """
                   CREATE TABLE "CITY" (
                       PK_CITY_ID                       INT                       PRIMARY KEY,
                       FK_COUNTRY_STATE_ID              INT                       REFERENCES "COUNTRY_STATE"                  (PK_COUNTRY_STATE_ID),
                       CITY_MAP                         BLOB,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR(100)              NOT NULL
                   )
                   """);

    statements.put(TABLE_NAME_COMPANY,
                   """
                   CREATE TABLE "COMPANY" (
                       PK_COMPANY_ID                    INT                       PRIMARY KEY,
                       FK_CITY_ID                       INT                       NOT NULL
                                                                                  REFERENCES "CITY"                           (PK_CITY_ID),
                       FK_CITY_ID_DEFAULT               INT                       DEFAULT 1
                                                                                  REFERENCES "CITY"                           (PK_CITY_ID),
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
                   CREATE TABLE "COUNTRY" (
                       PK_COUNTRY_ID                    INT                       PRIMARY KEY,
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
                   CREATE TABLE "COUNTRY_STATE" (
                       PK_COUNTRY_STATE_ID              INT,
                       FK_COUNTRY_ID                    INT                       NOT NULL
                                                                                  REFERENCES "COUNTRY"                        (PK_COUNTRY_ID),
                       FK_TIMEZONE_ID                   INT                       NOT NULL
                                                                                  REFERENCES "TIMEZONE"                       (PK_TIMEZONE_ID),
                       COUNTRY_STATE_MAP                BLOB,
                       CREATED                          TIMESTAMP                 NOT NULL,
                       MODIFIED                         TIMESTAMP,
                       NAME                             VARCHAR(100)              NOT NULL,
                       SYMBOL                           VARCHAR(50),
                       CONSTRAINT CONSTRAINT_1        UNIQUE      ("FK_COUNTRY_ID", "NAME")
                   )
                   """);

    statements.put(TABLE_NAME_TIMEZONE,
                   """
                   CREATE TABLE "TIMEZONE" (
                       PK_TIMEZONE_ID                   INT                       PRIMARY KEY,
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

  /**
   * Initialises a new abstract CUBRID schema object.
   *
   * @param dbmsTickerSymbol
   *            DBMS ticker symbol
   */
  public AbstractGenCubridSchema(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }
}
