package ch.konnexions.db_seeder.generated;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Test Data Generator for a Database - Abstract Generated Seeder.
 * <br>
 * @author  GenerateSchema.class
 * @version 2.0.0
 * @since   2020-07-26
 */
abstract class AbstractGenSeeder extends AbstractGenSchema {

  private static final Logger logger = Logger.getLogger(AbstractGenSeeder.class);

  /**
   * Initialises a new abstract generated seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   */
  public AbstractGenSeeder(String dbmsTickerSymbol) {
    super(dbmsTickerSymbol);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Initialises a new abstract generated seeder object.
   *
   * @param dbmsTickerSymbol DBMS ticker symbol 
   * @param isClient client database version
   */
  public AbstractGenSeeder(String dbmsTickerSymbol, boolean isClient) {
    super(dbmsTickerSymbol, isClient);

    if (isDebug) {
      logger.debug("Start Constructor - dbmsTickerSymbol=" + dbmsTickerSymbol + " - isClient=" + isClient);
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  protected final void insertTable(PreparedStatement preparedStatement,
                                   final String tableName,
                                   final int rowCount,
                                   final int rowNo,
                                   final ArrayList<Object> pkList) {
    if (isDebug) {
      logger.debug("Start");
    }

    switch (tableName) {
    case TABLE_NAME_CITY -> prepDmlStmntInsertCity(preparedStatement,
                                                   rowCount);
    case TABLE_NAME_COMPANY -> prepDmlStmntInsertCompany(preparedStatement,
                                                         rowCount);
    case TABLE_NAME_COUNTRY -> prepDmlStmntInsertCountry(preparedStatement,
                                                         rowCount);
    case TABLE_NAME_COUNTRY_STATE -> prepDmlStmntInsertCountryState(preparedStatement,
                                                                    rowCount);
    case TABLE_NAME_TIMEZONE -> prepDmlStmntInsertTimezone(preparedStatement,
                                                           rowCount);
    default -> throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME,
                                                                                                    tableName));
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void prepDmlStmntInsertCity(PreparedStatement preparedStatement, int rowCount) {
    int i = 0;

    prepStmntInsertColBigint(preparedStatement,
                             "CITY",
                             "PK_CITY_ID",
                             ++i,
                             rowCount);
    prepStmntInsertColFKOpt(preparedStatement,
                            "CITY",
                            "FK_COUNTRY_STATE_ID",
                            ++i,
                            rowCount,
                            pkLists.get(TABLE_NAME_COUNTRY_STATE));
    prepStmntInsertColBlobOpt(preparedStatement,
                              "CITY",
                              "CITY_MAP",
                              ++i,
                              rowCount);
    prepStmntInsertColTimestamp(preparedStatement,
                                "CITY",
                                "CREATED",
                                ++i,
                                rowCount);
    prepStmntInsertColTimestampOpt(preparedStatement,
                                   "CITY",
                                   "MODIFIED",
                                   ++i,
                                   rowCount);
    prepStmntInsertColString(preparedStatement,
                             "CITY",
                             "NAME",
                             ++i,
                             rowCount,
                             autoIncrement);
  }

  private void prepDmlStmntInsertCompany(PreparedStatement preparedStatement, int rowCount) {
    int i = 0;

    prepStmntInsertColBigint(preparedStatement,
                             "COMPANY",
                             "PK_COMPANY_ID",
                             ++i,
                             rowCount);
    prepStmntInsertColFK(preparedStatement,
                         "COMPANY",
                         "FK_CITY_ID",
                         ++i,
                         rowCount,
                         pkLists.get(TABLE_NAME_CITY));
    prepStmntInsertColString(preparedStatement,
                             "COMPANY",
                             "ACTIVE",
                             ++i,
                             rowCount,
                             autoIncrement);
    prepStmntInsertColStringOpt(preparedStatement,
                                "COMPANY",
                                "ADDRESS1",
                                ++i,
                                rowCount,
                                autoIncrement);
    prepStmntInsertColStringOpt(preparedStatement,
                                "COMPANY",
                                "ADDRESS2",
                                ++i,
                                rowCount,
                                autoIncrement);
    prepStmntInsertColStringOpt(preparedStatement,
                                "COMPANY",
                                "ADDRESS3",
                                ++i,
                                rowCount,
                                autoIncrement);
    prepStmntInsertColTimestamp(preparedStatement,
                                "COMPANY",
                                "CREATED",
                                ++i,
                                rowCount);
    prepStmntInsertColClobOpt(preparedStatement,
                              "COMPANY",
                              "DIRECTIONS",
                              ++i,
                              rowCount);
    prepStmntInsertColStringOpt(preparedStatement,
                                "COMPANY",
                                "EMAIL",
                                ++i,
                                rowCount,
                                autoIncrement);
    prepStmntInsertColStringOpt(preparedStatement,
                                "COMPANY",
                                "FAX",
                                ++i,
                                rowCount,
                                autoIncrement);
    prepStmntInsertColTimestampOpt(preparedStatement,
                                   "COMPANY",
                                   "MODIFIED",
                                   ++i,
                                   rowCount);
    prepStmntInsertColString(preparedStatement,
                             "COMPANY",
                             "NAME",
                             ++i,
                             rowCount,
                             autoIncrement);
    prepStmntInsertColStringOpt(preparedStatement,
                                "COMPANY",
                                "PHONE",
                                ++i,
                                rowCount,
                                autoIncrement);
    prepStmntInsertColStringOpt(preparedStatement,
                                "COMPANY",
                                "POSTAL_CODE",
                                ++i,
                                rowCount,
                                autoIncrement);
    prepStmntInsertColStringOpt(preparedStatement,
                                "COMPANY",
                                "URL",
                                ++i,
                                rowCount,
                                autoIncrement);
    prepStmntInsertColStringOpt(preparedStatement,
                                "COMPANY",
                                "VAT_ID_NUMBER",
                                ++i,
                                rowCount,
                                autoIncrement);
  }

  private void prepDmlStmntInsertCountry(PreparedStatement preparedStatement, int rowCount) {
    int i = 0;

    prepStmntInsertColBigint(preparedStatement,
                             "COUNTRY",
                             "PK_COUNTRY_ID",
                             ++i,
                             rowCount);
    prepStmntInsertColBlobOpt(preparedStatement,
                              "COUNTRY",
                              "COUNTRY_MAP",
                              ++i,
                              rowCount);
    prepStmntInsertColTimestamp(preparedStatement,
                                "COUNTRY",
                                "CREATED",
                                ++i,
                                rowCount);
    prepStmntInsertColStringOpt(preparedStatement,
                                "COUNTRY",
                                "ISO3166",
                                ++i,
                                rowCount,
                                autoIncrement);
    prepStmntInsertColTimestampOpt(preparedStatement,
                                   "COUNTRY",
                                   "MODIFIED",
                                   ++i,
                                   rowCount);
    prepStmntInsertColString(preparedStatement,
                             "COUNTRY",
                             "NAME",
                             ++i,
                             rowCount,
                             autoIncrement);
  }

  private void prepDmlStmntInsertCountryState(PreparedStatement preparedStatement, int rowCount) {
    int i = 0;

    prepStmntInsertColBigint(preparedStatement,
                             "COUNTRY_STATE",
                             "PK_COUNTRY_STATE_ID",
                             ++i,
                             rowCount);
    prepStmntInsertColFK(preparedStatement,
                         "COUNTRY_STATE",
                         "FK_COUNTRY_ID",
                         ++i,
                         rowCount,
                         pkLists.get(TABLE_NAME_COUNTRY));
    prepStmntInsertColFK(preparedStatement,
                         "COUNTRY_STATE",
                         "FK_TIMEZONE_ID",
                         ++i,
                         rowCount,
                         pkLists.get(TABLE_NAME_TIMEZONE));
    prepStmntInsertColBlobOpt(preparedStatement,
                              "COUNTRY_STATE",
                              "COUNTRY_STATE_MAP",
                              ++i,
                              rowCount);
    prepStmntInsertColTimestamp(preparedStatement,
                                "COUNTRY_STATE",
                                "CREATED",
                                ++i,
                                rowCount);
    prepStmntInsertColTimestampOpt(preparedStatement,
                                   "COUNTRY_STATE",
                                   "MODIFIED",
                                   ++i,
                                   rowCount);
    prepStmntInsertColString(preparedStatement,
                             "COUNTRY_STATE",
                             "NAME",
                             ++i,
                             rowCount,
                             autoIncrement);
    prepStmntInsertColStringOpt(preparedStatement,
                                "COUNTRY_STATE",
                                "SYMBOL",
                                ++i,
                                rowCount,
                                autoIncrement);
  }

  private void prepDmlStmntInsertTimezone(PreparedStatement preparedStatement, int rowCount) {
    int i = 0;

    prepStmntInsertColBigint(preparedStatement,
                             "TIMEZONE",
                             "PK_TIMEZONE_ID",
                             ++i,
                             rowCount);
    prepStmntInsertColString(preparedStatement,
                             "TIMEZONE",
                             "ABBREVIATION",
                             ++i,
                             rowCount,
                             autoIncrement);
    prepStmntInsertColTimestamp(preparedStatement,
                                "TIMEZONE",
                                "CREATED",
                                ++i,
                                rowCount);
    prepStmntInsertColTimestampOpt(preparedStatement,
                                   "TIMEZONE",
                                   "MODIFIED",
                                   ++i,
                                   rowCount);
    prepStmntInsertColString(preparedStatement,
                             "TIMEZONE",
                             "NAME",
                             ++i,
                             rowCount,
                             autoIncrement);
    prepStmntInsertColStringOpt(preparedStatement,
                                "TIMEZONE",
                                "V_TIME_ZONE",
                                ++i,
                                rowCount,
                                autoIncrement);
  }

}
