package ch.konnexions.db_seeder.generated;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Test Data Generator for a Database - Abstract Generated Seeder.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
abstract class AbstractGenSeeder extends AbstractGenSchema {

  private static final Logger logger = Logger.getLogger(AbstractGenSeeder.class);

  /**
   * Initialises a new abstract generated seeder object.
   */
  public AbstractGenSeeder() {
    super();

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  protected final void insertTable(final PreparedStatement preparedStatement,
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

  private void prepDmlStmntInsertCity(final PreparedStatement preparedStatement, final int rowCount) {
    int i = 2;

    prepStmntInsertColFKOpt(i++,
                            preparedStatement,
                            pkLists.get(TABLE_NAME_COUNTRY_STATE),
                            rowCount);
    prepStmntInsertColBlobOpt(preparedStatement,
                              i++,
                              rowCount);
    prepStmntInsertColDatetime(preparedStatement,
                               i++,
                               rowCount);
    prepStmntInsertColDatetimeOpt(preparedStatement,
                                  i++,
                                  rowCount);
    prepStmntInsertColString(preparedStatement,
                             i,
                             "NAME_",
                             autoIncrement);
  }

  private void prepDmlStmntInsertCompany(final PreparedStatement preparedStatement, final int rowCount) {
    try {
      int i = 2;

      preparedStatement.setObject(i++,
                                  pkLists.get(TABLE_NAME_CITY).get(getRandomIntExcluded(pkListSizes.get(TABLE_NAME_CITY))));
      prepStmntInsertColFlagNY(preparedStatement,
                               i++,
                               rowCount);
      prepStmntInsertColStringOpt(preparedStatement,
                                  i++,
                                  "ADDRESS1_",
                                  rowCount,
                                  autoIncrement);
      prepStmntInsertColStringOpt(preparedStatement,
                                  i++,
                                  "ADDRESS2_",
                                  rowCount,
                                  autoIncrement);
      prepStmntInsertColStringOpt(preparedStatement,
                                  i++,
                                  "ADDRESS3_",
                                  rowCount,
                                  autoIncrement);
      prepStmntInsertColDatetime(preparedStatement,
                                 i++,
                                 rowCount);
      prepStmntInsertColClobOpt(preparedStatement,
                                i++,
                                rowCount);
      prepStmntInsertColStringOpt(preparedStatement,
                                  i++,
                                  "EMAIL_",
                                  rowCount,
                                  autoIncrement);
      prepStmntInsertColStringOpt(preparedStatement,
                                  i++,
                                  "FAX_",
                                  rowCount,
                                  autoIncrement);
      prepStmntInsertColDatetimeOpt(preparedStatement,
                                    i++,
                                    rowCount);
      prepStmntInsertColString(preparedStatement,
                               i++,
                               "NAME_",
                               autoIncrement);
      prepStmntInsertColStringOpt(preparedStatement,
                                  i++,
                                  "PHONE_",
                                  rowCount,
                                  autoIncrement);
      prepStmntInsertColStringOpt(preparedStatement,
                                  i++,
                                  "POSTAL_CODE_",
                                  rowCount,
                                  autoIncrement);
      prepStmntInsertColStringOpt(preparedStatement,
                                  i++,
                                  "URL_",
                                  rowCount,
                                  autoIncrement);
      prepStmntInsertColStringOpt(preparedStatement,
                                  i,
                                  "VAT_ID_NUMBER_",
                                  rowCount,
                                  autoIncrement);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private void prepDmlStmntInsertCountry(final PreparedStatement preparedStatement, final int rowCount) {
    int i = 2;

    prepStmntInsertColBlobOpt(preparedStatement,
                              i++,
                              rowCount);
    prepStmntInsertColDatetime(preparedStatement,
                               i++,
                               rowCount);
    prepStmntInsertColStringOpt(preparedStatement,
                                i++,
                                "ISO3166_",
                                rowCount,
                                autoIncrement);
    prepStmntInsertColDatetimeOpt(preparedStatement,
                                  i++,
                                  rowCount);
    prepStmntInsertColString(preparedStatement,
                             i,
                             "NAME_",
                             autoIncrement);
  }

  private void prepDmlStmntInsertCountryState(final PreparedStatement preparedStatement, final int rowCount) {
    try {
      int i = 2;

      preparedStatement.setObject(i++,
                                  pkLists.get(TABLE_NAME_COUNTRY).get(getRandomIntExcluded(pkListSizes.get(TABLE_NAME_COUNTRY))));
      preparedStatement.setObject(i++,
                                  pkLists.get(TABLE_NAME_TIMEZONE).get(getRandomIntExcluded(pkListSizes.get(TABLE_NAME_TIMEZONE))));
      prepStmntInsertColBlobOpt(preparedStatement,
                                i++,
                                rowCount);
      prepStmntInsertColDatetime(preparedStatement,
                                 i++,
                                 rowCount);
      prepStmntInsertColDatetimeOpt(preparedStatement,
                                    i++,
                                    rowCount);
      prepStmntInsertColString(preparedStatement,
                               i++,
                               "NAME_",
                               autoIncrement);
      prepStmntInsertColStringOpt(preparedStatement,
                                  i,
                                  "SYMBOL_",
                                  rowCount,
                                  autoIncrement);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private void prepDmlStmntInsertTimezone(final PreparedStatement preparedStatement, final int rowCount) {
    int i = 2;

    prepStmntInsertColString(preparedStatement,
                             i++,
                             "ABBREVIATION_",
                             autoIncrement);
    prepStmntInsertColDatetime(preparedStatement,
                               i++,
                               rowCount);
    prepStmntInsertColDatetimeOpt(preparedStatement,
                                  i++,
                                  rowCount);
    prepStmntInsertColString(preparedStatement,
                             i++,
                             "NAME_",
                             autoIncrement);
    prepStmntInsertColStringOpt(preparedStatement,
                                i,
                                "V_TIME_ZONE_",
                                rowCount,
                                autoIncrement);
  }

}
