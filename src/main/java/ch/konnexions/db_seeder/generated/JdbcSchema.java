package ch.konnexions.db_seeder.generated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public interface JdbcSchema {

  static HashMap<String, ArrayList<Object>> pkLists                  = new HashMap<String, ArrayList<Object>>();
  static HashMap<String, Integer>           pkListSizes              = new HashMap<String, Integer>();

  static final String                       TABLE_NAME_CITY          = "CITY";
  static final String                       TABLE_NAME_COMPANY       = "COMPANY";
  static final String                       TABLE_NAME_COUNTRY       = "COUNTRY";
  static final String                       TABLE_NAME_COUNTRY_STATE = "COUNTRY_STATE";
  static final String                       TABLE_NAME_TIMEZONE      = "TIMEZONE";

  static final List<String>                 TABLE_NAMES_CREATE       = Arrays.asList(TABLE_NAME_COUNTRY,
                                                                                     TABLE_NAME_TIMEZONE,
                                                                                     TABLE_NAME_COUNTRY_STATE,
                                                                                     TABLE_NAME_CITY,
                                                                                     TABLE_NAME_COMPANY);

  static final List<String>                 TABLE_NAMES_DROP         = Arrays.asList(TABLE_NAME_COMPANY,
                                                                                     TABLE_NAME_CITY,
                                                                                     TABLE_NAME_COUNTRY_STATE,
                                                                                     TABLE_NAME_COUNTRY,
                                                                                     TABLE_NAME_TIMEZONE);

}
