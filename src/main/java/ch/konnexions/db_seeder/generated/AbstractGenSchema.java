package ch.konnexions.db_seeder.generated;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a Database - Abstract Generated Schema.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
abstract class AbstractGenSchema extends AbstractJdbcSeeder {

  protected static final String TABLE_NAME_CITY          = "CITY";
  protected static final String TABLE_NAME_COMPANY       = "COMPANY";
  protected static final String TABLE_NAME_COUNTRY       = "COUNTRY";
  protected static final String TABLE_NAME_COUNTRY_STATE = "COUNTRY_STATE";
  protected static final String TABLE_NAME_TIMEZONE      = "TIMEZONE";

  private static final Logger   logger                   = Logger.getLogger(AbstractGenSchema.class);

  /**
   * Initialises a new abstract generated schema object.
   */
  public AbstractGenSchema() {
    super();

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    initConstants();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  protected final Properties createColumnNames() {
    Properties columnName = new Properties();

    // Encoding ASCII
    columnName.setProperty("ABBREVIATION_0",
                           "");
    columnName.setProperty("ADDRESS1_0",
                           "");
    columnName.setProperty("ADDRESS2_0",
                           "");
    columnName.setProperty("ADDRESS3_0",
                           "");
    columnName.setProperty("EMAIL_0",
                           "");
    columnName.setProperty("FAX_0",
                           "");
    columnName.setProperty("ISO3166_0",
                           "");
    columnName.setProperty("NAME_0",
                           "");
    columnName.setProperty("PHONE_0",
                           "");
    columnName.setProperty("POSTAL CODE_0",
                           "");
    columnName.setProperty("SYMBOL_0",
                           "");
    columnName.setProperty("URL_0",
                           "");
    columnName.setProperty("VAT_ID_NUMBER_0",
                           "");
    columnName.setProperty("V_TIME_ZONE_0",
                           "");

    // Encoding ISO_8859_1
    boolean isIso_8859_1 = config.getEncodingIso_8859_1();

    columnName.setProperty("ABBREVIATION_1",
                           isIso_8859_1 ? "ABRÉVIATION_" : "NO_ISO_8859_1_");
    columnName.setProperty("ADDRESS1_1",
                           isIso_8859_1 ? "DIRECCIÓN1_" : "NO_ISO_8859_1_");
    columnName.setProperty("ADDRESS2_1",
                           isIso_8859_1 ? "DIRECCIÓN2_" : "NO_ISO_8859_1_");
    columnName.setProperty("ADDRESS3_1",
                           isIso_8859_1 ? "DIRECCIÓN3_" : "NO_ISO_8859_1_");
    columnName.setProperty("EMAIL_1",
                           isIso_8859_1 ? "CORREO_ELECTRÓNICO_" : "NO_ISO_8859_1_");
    columnName.setProperty("FAX_1",
                           isIso_8859_1 ? "TÉLÉCOPIE_" : "NO_ISO_8859_1_");
    columnName.setProperty("ISO3166_1",
                           isIso_8859_1 ? "CÓDIGO 3166_" : "NO_ISO_8859_1_");
    columnName.setProperty("NAME_1",
                           isIso_8859_1 ? "COMPAÑÍA_" : "NO_ISO_8859_1_");
    columnName.setProperty("PHONE_1",
                           isIso_8859_1 ? "TÉLÉPHONE_" : "NO_ISO_8859_1_");
    columnName.setProperty("POSTAL CODE_1",
                           isIso_8859_1 ? "CÓDIGO_POSTAL_" : "NO_ISO_8859_1_");
    columnName.setProperty("SYMBOL_1",
                           isIso_8859_1 ? "SÍMBOLO_" : "NO_ISO_8859_1_");
    columnName.setProperty("URL_1",
                           isIso_8859_1 ? "ENDEREÇO_" : "NO_ISO_8859_1_");
    columnName.setProperty("VAT_ID_NUMBER_1",
                           isIso_8859_1 ? "NUMÉRO_D'IDENTIFICATION_DE_LA_TVA_" : "NO_ISO_8859_1_");
    columnName.setProperty("V_TIME_ZONE_1",
                           isIso_8859_1 ? "FUSO_HORÁRIO_" : "NO_ISO_8859_1_");

    // Encoding UTF_8
    boolean isUtf_8 = config.getEncodingUtf_8();

    columnName.setProperty("ABBREVIATION_2",
                           isUtf_8 ? "缩略语_" : "NO_UTF_8_");
    columnName.setProperty("ADDRESS1_2",
                           isUtf_8 ? "地址1_" : "NO_UTF_8_");
    columnName.setProperty("ADDRESS2_2",
                           isUtf_8 ? "地址2_" : "NO_UTF_8_");
    columnName.setProperty("ADDRESS3_2",
                           isUtf_8 ? "地址3_" : "NO_UTF_8_");
    columnName.setProperty("EMAIL_2",
                           isUtf_8 ? "电子邮件_" : "NO_UTF_8_");
    columnName.setProperty("FAX_2",
                           isUtf_8 ? "传真_" : "NO_UTF_8_");
    columnName.setProperty("ISO3166_2",
                           isUtf_8 ? "ISO 3166标准_" : "NO_UTF_8_");
    columnName.setProperty("NAME_2",
                           isUtf_8 ? "名称_" : "NO_UTF_8_");
    columnName.setProperty("PHONE_2",
                           isUtf_8 ? "电话_" : "NO_UTF_8_");
    columnName.setProperty("POSTAL CODE_2",
                           isUtf_8 ? "邮政编码_" : "NO_UTF_8_");
    columnName.setProperty("SYMBOL_2",
                           isUtf_8 ? "符号_" : "NO_UTF_8_");
    columnName.setProperty("URL_2",
                           isUtf_8 ? "网址_" : "NO_UTF_8_");
    columnName.setProperty("VAT_ID_NUMBER_2",
                           isUtf_8 ? "增值税号_" : "NO_UTF_8_");
    columnName.setProperty("V_TIME_ZONE_2",
                           isUtf_8 ? "时区_" : "NO_UTF_8_");

    return columnName;
  }

  /**
   * Initialising constants.
   */
  @SuppressWarnings("serial")
  private void initConstants() {
    dmlStatements      = new HashMap<>() {
                         {
                           put(TABLE_NAME_CITY,
                               "fk_country_state_id,city_map,created,modified,name) VALUES (?,?,?,?,?,?");
                           put(TABLE_NAME_COMPANY,
                               "fk_city_id,active,address1,address2,address3,created,directions,email,fax,modified,name,phone,postal_code,url,vat_id_number) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
                           put(TABLE_NAME_COUNTRY,
                               "country_map,created,iso3166,modified,name) VALUES (?,?,?,?,?,?");
                           put(TABLE_NAME_COUNTRY_STATE,
                               "fk_country_id,fk_timezone_id,country_state_map,created,modified,name,symbol) VALUES (?,?,?,?,?,?,?,?");
                           put(TABLE_NAME_TIMEZONE,
                               "abbreviation,created,modified,name,v_time_zone) VALUES (?,?,?,?,?,?");
                         }
                       };

    maxRowSizes        = new HashMap<>() {
                         {
                           put(TABLE_NAME_CITY,
                               1800);
                           put(TABLE_NAME_COMPANY,
                               5400);
                           put(TABLE_NAME_COUNTRY,
                               200);
                           put(TABLE_NAME_COUNTRY_STATE,
                               600);
                           put(TABLE_NAME_TIMEZONE,
                               11);
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
                                       TABLE_NAME_COUNTRY,
                                       TABLE_NAME_TIMEZONE);

  }

}
