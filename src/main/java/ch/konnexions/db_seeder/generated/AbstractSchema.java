package ch.konnexions.db_seeder.generated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.konnexions.db_seeder.AbstractDatabaseSeeder;

public abstract class AbstractSchema extends AbstractDatabaseSeeder {

  @SuppressWarnings("serial")
  protected static final Map<String, String>          dmlStatements            = new HashMap<String, String>() {
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

  @SuppressWarnings("serial")
  protected static final Map<String, Integer>         maxRowSizes              = new HashMap<String, Integer>() {
                                                                                 {
                                                                                   put(TABLE_NAME_CITY,
                                                                                       Integer.valueOf(1800));
                                                                                   put(TABLE_NAME_COMPANY,
                                                                                       Integer.valueOf(5400));
                                                                                   put(TABLE_NAME_COUNTRY,
                                                                                       Integer.valueOf(200));
                                                                                   put(TABLE_NAME_COUNTRY_STATE,
                                                                                       Integer.valueOf(600));
                                                                                   put(TABLE_NAME_TIMEZONE,
                                                                                       Integer.valueOf(11));
                                                                                 }
                                                                               };

  protected static HashMap<String, ArrayList<Object>> pkLists                  = new HashMap<String, ArrayList<Object>>();

  protected static HashMap<String, Integer>           pkListSizes              = new HashMap<String, Integer>();

  protected static final String                       TABLE_NAME_CITY          = "CITY";
  protected static final String                       TABLE_NAME_COMPANY       = "COMPANY";

  protected static final String                       TABLE_NAME_COUNTRY       = "COUNTRY";
  protected static final String                       TABLE_NAME_COUNTRY_STATE = "COUNTRY_STATE";
  protected static final String                       TABLE_NAME_TIMEZONE      = "TIMEZONE";
  protected static final List<String>                 TABLE_NAMES_CREATE       = Arrays.asList(TABLE_NAME_COUNTRY,
                                                                                               TABLE_NAME_TIMEZONE,
                                                                                               TABLE_NAME_COUNTRY_STATE,
                                                                                               TABLE_NAME_CITY,
                                                                                               TABLE_NAME_COMPANY);
  protected static final List<String>                 TABLE_NAMES_DROP         = Arrays.asList(TABLE_NAME_COMPANY,
                                                                                               TABLE_NAME_CITY,
                                                                                               TABLE_NAME_COUNTRY_STATE,
                                                                                               TABLE_NAME_COUNTRY,
                                                                                               TABLE_NAME_TIMEZONE);

  public AbstractSchema() {
    super();
  }

  public AbstractSchema(boolean isClient) {
    super();
  }
}
