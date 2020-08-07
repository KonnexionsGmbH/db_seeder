package ch.konnexions.db_seeder.generated;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a Database - Abstract Generated Schema.
 * <br>
 * @author  GenerateSchema.class
 * @version 2.1.0
 */
abstract class AbstractGenSchema extends AbstractJdbcSeeder {

  protected static final String TABLE_NAME_CITY          = "CITY";
  protected static final String TABLE_NAME_COMPANY       = "COMPANY";
  protected static final String TABLE_NAME_COUNTRY       = "COUNTRY";
  protected static final String TABLE_NAME_COUNTRY_STATE = "COUNTRY_STATE";
  protected static final String TABLE_NAME_TIMEZONE      = "TIMEZONE";

  private static final Logger   logger                   = Logger.getLogger(AbstractGenSchema.class);
  private final boolean         isDebug                  = logger.isDebugEnabled();

  /**
   * Initialises a new abstract generated schema object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol 
   * @param dbmsOption client, embedded or presto
   */
  public AbstractGenSchema(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    initConstants();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
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
                               10);
                           put(TABLE_NAME_COMPANY,
                               10);
                           put(TABLE_NAME_COUNTRY,
                               10);
                           put(TABLE_NAME_COUNTRY_STATE,
                               10);
                           put(TABLE_NAME_TIMEZONE,
                               10);
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
