/**
 *
 */
package ch.konnexions.db_seeder.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
//import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.AbstractDatabaseSeeder;
import ch.konnexions.db_seeder.utils.Config;
import ch.konnexions.db_seeder.utils.Statistics;

/**
 * Test Data Generator for a Database.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public abstract class AbstractJdbcSeeder extends AbstractDatabaseSeeder implements JdbcSchema {

  private static final int   ENCODING_MAX       = 3;
  private static Logger      logger             = Logger.getLogger(AbstractJdbcSeeder.class);

  private final String       BLOB_FILE          = Paths.get("src",
                                                            "main",
                                                            "resources").toAbsolutePath().toString() + File.separator + "blob.png";
  private final byte[]       BLOB_DATA_BYTES    = readBlobFile2Bytes();
  private final String       CLOB_FILE          = Paths.get("src",
                                                            "main",
                                                            "resources").toAbsolutePath().toString() + File.separator + "clob.md";
  private final String       CLOB_DATA          = readClobFile();

  private final Properties   COLUMN_NAME;
  protected Connection       connection         = null;

  protected String           driver             = "";

  protected String           dropTableStmnt     = "";
  protected boolean          isClient           = true;

  protected boolean          isEmbedded         = !(isClient);

  private final int          MAX_ROW_SIZE       = Integer.MAX_VALUE;

  private ArrayList<Object>  pkListCity         = new ArrayList<Object>();
  private ArrayList<Object>  pkListCountry      = new ArrayList<Object>();
  private ArrayList<Object>  pkListCountryState = new ArrayList<Object>();
  private ArrayList<Object>  pkListTimezone     = new ArrayList<Object>();
  private PreparedStatement  preparedStatement  = null;

  private final int          RANDOM_NUMBER      = 4;
  private Random             randomInt          = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
  private ResultSet          resultSet          = null;

  protected Statement        statement          = null;

  private final List<String> TABLE_NAMES_CREATE = Arrays.asList(TABLE_NAME_COUNTRY,
                                                                TABLE_NAME_TIMEZONE,
                                                                TABLE_NAME_COUNTRY_STATE,
                                                                TABLE_NAME_CITY,
                                                                TABLE_NAME_COMPANY);

  private final List<String> TABLE_NAMES_DROP   = Arrays.asList(TABLE_NAME_COMPANY,
                                                                TABLE_NAME_CITY,
                                                                TABLE_NAME_COUNTRY_STATE,
                                                                TABLE_NAME_COUNTRY,
                                                                TABLE_NAME_TIMEZONE);

  protected String           url                = "";
  protected String           urlBase            = "";
  protected String           urlSetup           = "";

  /**
   * Instantiates a new abstract JDBC seeder.
   */
  public AbstractJdbcSeeder() {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    config      = new Config();

    COLUMN_NAME = createColumnNames();

    isClient    = true;
    isEmbedded  = !(this.isClient);

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- client  =" + isClient);
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- embedded=" + isEmbedded);

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  /**
   * Instantiates a new abstract JDBC seeder.
   *
   * @param isClient is a client database version ?
   */
  public AbstractJdbcSeeder(boolean isClient) {
    super(isClient);

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    config        = new Config();

    COLUMN_NAME   = createColumnNames();

    this.isClient = isClient;
    isEmbedded    = !(this.isClient);

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- client  =" + isClient);
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- embedded=" + isEmbedded);

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  private final void addOptionalFk(final String tableName, final ArrayList<Object> fKList) {
    switch (tableName) {
    case TABLE_NAME_COUNTRY_STATE:
      if (pkListCity == null) {
        recreatePkList(TABLE_NAME_CITY);
      }

      addOptionalFk(TABLE_NAME_CITY,
                    "PK_CITY_ID",
                    pkListCity,
                    "FK_COUNTRY_STATE_ID",
                    fKList);
      break;
    }
  }

  private final void addOptionalFk(final String tableName,
                                   final String pkcolumnName,
                                   final ArrayList<Object> pkList,
                                   final String fkColumnName,
                                   final ArrayList<Object> fkList) {
    int pkListSize = pkList.size();
    if (pkListSize == 0) {
      return;
    }

    int fkListSize = fkList.size();
    if (fkListSize == 0) {
      return;
    }

    pkListSize++;

    try {
      preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET " + fkColumnName + " = ? WHERE " + pkcolumnName + " = ?");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    for (Object pkIndex : pkList) {
      if (getRandomIntExcluded(pkListSize) % RANDOM_NUMBER == 0) {
        continue;
      }

      try {
        preparedStatement.setObject(1,
                                    fkList.get(getRandomIntExcluded(fkListSize)));
        preparedStatement.setObject(2,
                                    pkIndex);

        preparedStatement.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * 
   * @return the database connection
   */
  protected final Connection connect(String url) {
    return connect(url,
                   null,
                   null,
                   null,
                   false);
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @param autoCommit the auto commit option
   * 
   * @return the database connection
   */
  protected final Connection connect(String url, boolean autoCommit) {
    return connect(url,
                   null,
                   null,
                   null,
                   autoCommit);
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @param driver the database driver
   * 
   * @return the database connection
   */
  protected final Connection connect(String url, String driver) {
    return connect(url,
                   driver,
                   null,
                   null,
                   false);
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @param driver the database driver
   * @param autoCommit the auto commit option
   * 
   * @return the database connection
   */
  protected final Connection connect(String url, String driver, boolean autoCommit) {
    return connect(url,
                   driver,
                   null,
                   null,
                   autoCommit);
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @param driver the database driver
   * @param user the user name
   * @param password the password
   *   
   * @return the database connection
   */
  protected final Connection connect(String url, String driver, String user, String password) {
    return connect(url,
                   driver,
                   user,
                   password,
                   false);
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
   * @param driver the database driver
   * @param user the user name
   * @param password the password
   * @param autoCommit the auto commit option
   * 
   * @return the database connection
   */
  protected final Connection connect(String url, String driver, String user, String password, boolean autoCommit) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    if (driver != null) {
      try {
        logger.debug(String.format(FORMAT_METHOD_NAME,
                                   methodName) + "- driver='" + driver + "'");
        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    Connection connection = null;

    try {
      if (isDebug) {
        logger.debug(String.format(FORMAT_METHOD_NAME,
                                   methodName) + "- url   ='" + url + "'");
      }
      if (user == null && password == null) {
        connection = DriverManager.getConnection(url);
      } else {
        if (isDebug) {
          logger.debug(String.format(FORMAT_METHOD_NAME,
                                     methodName) + "- user  ='" + user + "' password='" + password + "'");
        }
        connection = DriverManager.getConnection(url,
                                                 user,
                                                 password);
      }

      connection.setAutoCommit(autoCommit);

      if (isDebug) {
        logger.debug(String.format(FORMAT_METHOD_NAME,
                                   methodName) + "- auto  =" + connection.getAutoCommit());
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   [" + connection.toString() + "]");
    }

    return connection;
  }

  private final int countData(final String tableName) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    int count = 0;

    try {
      statement = connection.createStatement();

      String sqlStmnt = "SELECT COUNT(*) FROM " + tableNameDelimiter + tableName + tableNameDelimiter;

      if (isDebug) {
        logger.debug(String.format(FORMAT_METHOD_NAME,
                                   methodName) + "- sql='" + sqlStmnt + "'");
      }

      resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }

    return count;
  }

  private final Properties createColumnNames() {
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
   * Create the test data for all database tables.
   */
  public final void createData() {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    Statistics statistics = new Statistics(config, dbmsTickerSymbol, dbmsValues);

    setupDatabase();

    for (String tableName : TABLE_NAMES_CREATE) {
      createData(tableName,
                 getMaxRowSize(tableName));
    }

    disconnect(connection);

    statistics.createMeasuringEntry();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  private final void createData(String tableName, int rowCount) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start - database table " + String.format(FORMAT_TABLE_NAME,
                                                                                           tableName) + " - " + String.format(FORMAT_ROW_NO,
                                                                                                                              rowCount)
          + " rows to be created");
    }

    tableName = tableName.toUpperCase();

    if (rowCount < 1) {
      rowCount = getDefaultRowSize(tableName);
    } else if (rowCount > MAX_ROW_SIZE) {
      rowCount = MAX_ROW_SIZE;
    }

    try {
      statement = connection.createStatement();

      String sqlStmnt = createDdlStmnt(tableName);

      if (isDebug) {
        logger.debug(String.format(FORMAT_METHOD_NAME,
                                   methodName) + "- next SQL statement=" + sqlStmnt);
      }

      statement.execute(sqlStmnt);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    final int countExisting = countData(tableName);

    if (countExisting != 0) {
      if (isDebug) {
        logger.debug(String.format(FORMAT_METHOD_NAME,
                                   methodName) + " - found existing test data in database table " + String.format(FORMAT_TABLE_NAME,
                                                                                                                  tableName));
      }

      return;
    }

    ArrayList<Object> pkList = new ArrayList<Object>();

    retrieveFkList(tableName);
    createFkList(tableName);

    autoIncrement = 0;

    createDataInsert(tableName,
                     rowCount,
                     pkList);

    addOptionalFk(tableName,
                  pkList);

    if (!(dbms == Dbms.CRATEDB || dbms == Dbms.FIREBIRD)) {
      try {
        connection.commit();
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    savePkList(tableName,
               pkList);

    validateNumberRows(tableName,
                       rowCount);

    validateEncoding(tableName,
                     "NAME",
                     rowCount);

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  private final void createDataInsert(String tableName, int rowCount, ArrayList<Object> pkList) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    final String sqlStmnt = "INSERT INTO " + tableNameDelimiter + tableName + tableNameDelimiter + " (" + createDmlStmnt(tableName) + ")";

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- sql='" + sqlStmnt + "'");
    }

    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(sqlStmnt);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    for (int rowNo = 1; rowNo <= rowCount; rowNo++) {
      try {
        preparedStatement.setInt(1,
                                 autoIncrement);
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }

      insertTable(preparedStatement,
                  tableName,
                  rowCount,
                  rowNo,
                  pkList);

      try {
        preparedStatement.executeUpdate();

        pkList.add(autoIncrement);

        autoIncrement++;
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (dbms == Dbms.CRATEDB) {
      try {
        statement = connection.createStatement();

        statement.execute("REFRESH TABLE " + tableName);

        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  /**
   * Create the DDL statement: CREATE TABLE.
   *
   * @param tableName the database table name
   *
   * @return the 'CREATE TABLE' statement
   */
  protected abstract String createDdlStmnt(String tableName);

  private final String createDmlStmnt(final String tableName) {
    return "pk_" + tableName.toLowerCase() + "_id," + switch (tableName) {
    case TABLE_NAME_CITY -> "fk_country_state_id,city_map,created,modified,name) VALUES (?,?,?,?,?,?";
    case TABLE_NAME_COMPANY -> "fk_city_id,active,address1,address2,address3,created,directions,email,fax,modified,name,phone,postal_code,url,vat_id_number) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
    case TABLE_NAME_COUNTRY -> "country_map,created,iso3166,modified,name) VALUES (?,?,?,?,?,?";
    case TABLE_NAME_COUNTRY_STATE -> "fk_country_id,fk_timezone_id,country_state_map,created,modified,name,symbol) VALUES (?,?,?,?,?,?,?,?";
    case TABLE_NAME_TIMEZONE -> "abbreviation,created,modified,name,v_time_zone) VALUES (?,?,?,?,?,?";
    default -> throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME,
                                                                                                    tableName));
    };
  }

  private final void createFkList(final String tableName) {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start [" + connection.toString() + "]");
    }

    switch (tableName) {
    case TABLE_NAME_COMPANY:
      if (pkListCity.size() == 0) {
        createData(TABLE_NAME_CITY,
                   config.getMaxRowCity());
      }

      break;
    case TABLE_NAME_COUNTRY_STATE:
      if (pkListCountry.size() == 0) {
        createData(TABLE_NAME_COUNTRY,
                   config.getMaxRowCountry());
      }

      if (pkListTimezone.size() == 0) {
        createData(TABLE_NAME_TIMEZONE,
                   config.getMaxRowTimezone());
      }

      break;
    }

    if (TABLE_NAME_COMPANY.equals(tableName)) {
      if (pkListCity.size() == 0) {
        if (countData(TABLE_NAME_CITY) == 0) {
          createData(TABLE_NAME_CITY,
                     config.getMaxRowCity());
        } else {
          recreatePkList(TABLE_NAME_CITY);
        }
      }
    }

    if (TABLE_NAME_COUNTRY_STATE.equals(tableName)) {
      if (pkListCountry.size() == 0) {
        if (countData(TABLE_NAME_COUNTRY) == 0) {
          createData(TABLE_NAME_COUNTRY,
                     config.getMaxRowCountry());
        } else {
          recreatePkList(TABLE_NAME_COUNTRY);
        }
      }

      if (pkListTimezone.size() == 0) {
        if (countData(TABLE_NAME_TIMEZONE) == 0) {
          createData(TABLE_NAME_TIMEZONE,
                     config.getMaxRowTimezone());
        } else {
          recreatePkList(TABLE_NAME_TIMEZONE);
        }
      }
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  /**
   * Close the database connection.
   *
   * @param connection the database connection
   */
  protected final void disconnect(Connection connection) {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start [" + connection.toString() + "]");
    }

    try {
      if (!(connection.getAutoCommit())) {
        connection.commit();
      }

      connection.close();

      connection = null;
    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  /**
   * Drop all tables based on q metadata query.
   *
   * @param sqlStmnt the SQL statement
   */
  protected final void dropAllTables(String sqlStmnt) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    ;

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    try {
      for (String tableName : TABLE_NAMES_DROP) {
        String queryStmnt = sqlStmnt.replace("?",
                                             dbms == Dbms.CRATEDB ? tableName.toLowerCase() : tableName.toUpperCase());

        if (isDebug) {
          logger.debug(String.format(FORMAT_METHOD_NAME,
                                     methodName) + "- next SQL statement=" + queryStmnt);
        }

        resultSet = statement.executeQuery(queryStmnt);

        if (resultSet.next()) {
          String dropStmnt = resultSet.getString(1);

          if (isDebug) {
            logger.debug(String.format(FORMAT_METHOD_NAME,
                                       methodName) + "- next SQL statement=" + dropStmnt);
          }

          statement.execute(dropStmnt);
        }

        resultSet.close();
      }

    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  /**
   * Drop all existing tables.
   */
  protected final void dropAllTablesIfExists() {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    try {
      for (String tableName : TABLE_NAMES_DROP) {
        String sqlStmnt = "DROP TABLE IF EXISTS \"" + tableName + "\"";

        if (isDebug) {
          logger.debug(String.format(FORMAT_METHOD_NAME,
                                     methodName) + "- next SQL statement=" + sqlStmnt);
        }

        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  /**
   * Drop the database.
   *
   * @param databaseName the database name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName the table name
   * @param columnName the column name
   */
  protected final void dropDatabase(String databaseName, String cascadeRestrict, String tableName, String columnName) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    try {
      int    count    = 0;

      String sqlStmnt = "SELECT count(*) FROM " + tableName + " WHERE " + columnName + " = '" + databaseName + "'";

      if (isDebug) {
        logger.debug(String.format(FORMAT_METHOD_NAME,
                                   methodName) + "- next SQL statement=" + sqlStmnt);
      }

      resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        sqlStmnt = "DROP " + (dbms == Dbms.MIMER ? "DATABANK" : "DATABASE") + " " + databaseName + (cascadeRestrict != null ? " " + cascadeRestrict : "");

        if (isDebug) {
          logger.debug(String.format(FORMAT_METHOD_NAME,
                                     methodName) + "- next SQL statement=" + sqlStmnt);
        }

        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  /**
   * Drop the database schema.
   *
   * @param schemaName the schema name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName the table name
   * @param columnName the column name
   */
  protected final void dropSchema(String schemaName, String cascadeRestrict, String tableName, String columnName) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    try {
      int    count    = 0;

      String sqlStmnt = "SELECT count(*) FROM " + tableName + " WHERE " + columnName + " = '" + schemaName + "'";

      if (isDebug) {
        logger.debug(String.format(FORMAT_METHOD_NAME,
                                   methodName) + "- next SQL statement=" + sqlStmnt);
      }

      resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        sqlStmnt = "DROP SCHEMA " + schemaName + (cascadeRestrict != null ? " " + cascadeRestrict : "");

        if (isDebug) {
          logger.debug(String.format(FORMAT_METHOD_NAME,
                                     methodName) + "- next SQL statement=" + sqlStmnt);
        }

        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  /**
   * Drop the database user.
   *
   * @param userName the user name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName the table name
   * @param columnName the column name
   */
  protected final void dropUser(String userName, String cascadeRestrict, String tableName, String columnName) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    try {
      int    count    = 0;

      String sqlStmnt = "SELECT count(*) FROM " + tableName + " WHERE " + columnName + " = '" + userName + "'";

      if (isDebug) {
        logger.debug(String.format(FORMAT_METHOD_NAME,
                                   methodName) + "- next SQL statement=" + sqlStmnt);
      }

      resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        sqlStmnt = "DROP " + (dbms == Dbms.MIMER ? "IDENT" : "USER") + "  " + userName + (cascadeRestrict != null ? " " + cascadeRestrict : "");

        if (isDebug) {
          logger.debug(String.format(FORMAT_METHOD_NAME,
                                     methodName) + "- next SQL statement=" + sqlStmnt);
        }

        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  /**
   * Execute DDL statements.
   *
   * @param firstDdlStmnt the first DDL statement
   * @param remainingDdlStmnts the remaining DDL statements
   */
  protected final void executeDdlStmnts(String firstDdlStmnt, String... remainingDdlStmnts) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    try {
      if (isDebug) {
        logger.debug(String.format(FORMAT_METHOD_NAME,
                                   methodName) + "- next SQL statement=" + firstDdlStmnt);
      }

      statement.execute(firstDdlStmnt);

      for (String sqlStmnt : remainingDdlStmnts) {

        if (isDebug) {
          logger.debug(String.format(FORMAT_METHOD_NAME,
                                     methodName) + "- next SQL statement=" + sqlStmnt);
        }

        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  private final String getColumnContent(String columnName, int rowNo) {
    return columnName + COLUMN_NAME.getProperty(columnName + rowNo % ENCODING_MAX) + String.format(FORMAT_IDENTIFIER,
                                                                                                   rowNo);
  }

  private final int getDefaultRowSize(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return 1200;
    case TABLE_NAME_COMPANY:
      return 6000;
    case TABLE_NAME_COUNTRY:
      return 100;
    case TABLE_NAME_COUNTRY_STATE:
      return 400;
    case TABLE_NAME_TIMEZONE:
      return 11;
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME,
                                                                                           tableName));
    }
  }

  private final int getMaxRowSize(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return config.getMaxRowCity();
    case TABLE_NAME_COMPANY:
      return config.getMaxRowCompany();
    case TABLE_NAME_COUNTRY:
      return config.getMaxRowCountry();
    case TABLE_NAME_COUNTRY_STATE:
      return config.getMaxRowCountryState();
    case TABLE_NAME_TIMEZONE:
      return config.getMaxRowTimezone();
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME,
                                                                                           tableName));
    }
  }

  private final int getRandomIntExcluded(final int upperLimit) {
    return randomInt.nextInt(upperLimit);
  }

  private final int getRandomIntIncluded(final int upperLimit) {
    return randomInt.nextInt(upperLimit) + 1;
  }

  //  private final double getRandomDouble(final double lowerLimit, final double upperLimit) {
  //    return ThreadLocalRandom.current().nextDouble(lowerLimit, upperLimit);
  //  }

  private final Timestamp getRandomTimestamp() {

    return new java.sql.Timestamp(System.currentTimeMillis() + randomInt.nextInt(2147483647));
  }

  private final void insertTable(final PreparedStatement preparedStatement,
                                 final String tableName,
                                 final int rowCount,
                                 final int rowNo,
                                 final ArrayList<Object> pkList) {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    switch (tableName) {
    case TABLE_NAME_CITY:
      prepDmlStmntInsertCity(preparedStatement,
                             rowCount);
      break;
    case TABLE_NAME_COMPANY:
      prepDmlStmntInsertCompany(preparedStatement,
                                rowCount);
      break;
    case TABLE_NAME_COUNTRY:
      prepDmlStmntInsertCountry(preparedStatement,
                                rowCount);
      break;
    case TABLE_NAME_COUNTRY_STATE:
      prepDmlStmntInsertCountryState(preparedStatement,
                                     rowCount);
      break;
    case TABLE_NAME_TIMEZONE:
      prepDmlStmntInsertTimezone(preparedStatement,
                                 rowCount);
      break;
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME,
                                                                                           tableName));
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  private final void prepDmlStmntInsertCity(final PreparedStatement preparedStatement, final int rowCount) {
    int i = 2;

    prepStmntInsertColFKOpt(i++,
                            preparedStatement,
                            pkListCountryState,
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

  private final void prepDmlStmntInsertCompany(final PreparedStatement preparedStatement, final int rowCount) {
    try {
      int i = 2;

      preparedStatement.setObject(i++,
                                  pkListCity.get(getRandomIntExcluded(pkListCity.size())));
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

  private final void prepDmlStmntInsertCountry(final PreparedStatement preparedStatement, final int rowCount) {
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

  private final void prepDmlStmntInsertCountryState(final PreparedStatement preparedStatement, final int rowCount) {
    try {
      int i = 2;

      preparedStatement.setObject(i++,
                                  pkListCountry.get(getRandomIntExcluded(pkListCountry.size())));
      preparedStatement.setObject(i++,
                                  pkListTimezone.get(getRandomIntExcluded(pkListTimezone.size())));
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

  private final void prepDmlStmntInsertTimezone(final PreparedStatement preparedStatement, final int rowCount) {
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

  /**
   * Sets the designated optional parameter to a BLOB value.
   * 
   * @param preparedStatement the prepared statement
   * @param columnPos         the column position
   * @param rowCount          the row count
   */
  protected void prepStmntInsertColBlob(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      preparedStatement.setBytes(columnPos,
                                 BLOB_DATA_BYTES);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColBlobOpt(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      if (dbms == Dbms.CRATEDB) {
        preparedStatement.setNull(columnPos,
                                  Types.NULL);
      } else if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        if (dbms == Dbms.POSTGRESQL) {
          preparedStatement.setNull(columnPos,
                                    Types.NULL);
        } else {
          preparedStatement.setNull(columnPos,
                                    Types.BLOB);
        }
      } else {
        prepStmntInsertColBlob(preparedStatement,
                               columnPos,
                               rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColClob(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      preparedStatement.setString(columnPos,
                                  CLOB_DATA);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColClobOpt(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        if (dbms == Dbms.CRATEDB) {
          preparedStatement.setNull(columnPos,
                                    Types.VARCHAR);
        } else {
          preparedStatement.setNull(columnPos,
                                    java.sql.Types.CLOB);
        }
      } else {
        prepStmntInsertColClob(preparedStatement,
                               columnPos,
                               rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColDatetime(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      preparedStatement.setTimestamp(columnPos,
                                     getRandomTimestamp());
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColDatetimeOpt(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.TIMESTAMP);
      } else {
        prepStmntInsertColDatetime(preparedStatement,
                                   columnPos,
                                   rowCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColFKOpt(final int columnPos, PreparedStatement preparedStatement, final ArrayList<Object> fkList, final int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.INTEGER);
      } else {
        preparedStatement.setObject(columnPos,
                                    fkList.get(getRandomIntExcluded(fkList.size())));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColFlagNY(PreparedStatement preparedStatement, final int columnPos, final int rowCount) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setString(columnPos,
                                    "N");
      } else {
        preparedStatement.setString(columnPos,
                                    "Y");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final void prepStmntInsertColString(final PreparedStatement preparedStatement, final int columnPos, final String columnName, final int rowNo) {
    try {
      if (dbms == Dbms.FIREBIRD || dbms == Dbms.MARIADB || dbms == Dbms.MSSQLSERVER || dbms == Dbms.ORACLE) {
        preparedStatement.setNString(columnPos,
                                     getColumnContent(columnName,
                                                      rowNo));
      } else {
        preparedStatement.setString(columnPos,
                                    getColumnContent(columnName,
                                                     rowNo));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  //  private final void
  //            prepStmntInsertColDoubleOpt(PreparedStatement preparedStatement, final int columnPos, final int rowCount, double lowerLimit, double upperLimit) {
  //    try {
  //      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
  //        preparedStatement.setNull(columnPos, java.sql.Types.DECIMAL);
  //      } else {
  //        preparedStatement.setBigDecimal(columnPos, BigDecimal.valueOf(getRandomDouble(lowerLimit, upperLimit)));
  //      }
  //    } catch (SQLException e) {
  //      e.printStackTrace();
  //      System.exit(1);
  //    }
  //  }

  private final void prepStmntInsertColStringOpt(final PreparedStatement preparedStatement,
                                                 final int columnPos,
                                                 final String columnName,
                                                 final int rowCount,
                                                 final int rowNo) {
    try {
      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.VARCHAR);
      } else {
        prepStmntInsertColString(preparedStatement,
                                 columnPos,
                                 columnName,
                                 rowNo);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private final byte[] readBlobFile2Bytes() {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- BLOB_FILE ='" + BLOB_FILE + "'");
    }

    File      file       = new File(BLOB_FILE);

    final int fileLength = (int) file.length();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- fileLength=" + fileLength);
    }

    byte[]          blobDataBytesArray = new byte[(int) file.length()];

    FileInputStream fileInputStream    = null;

    try {
      fileInputStream = new FileInputStream(file);

      fileInputStream.read(blobDataBytesArray);

      fileInputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- byteLength=" + blobDataBytesArray.length);

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }

    return blobDataBytesArray;
  }

  private final String readClobFile() {
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader(CLOB_FILE));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
      System.exit(1);
    }

    StringBuffer clobData = new StringBuffer();
    String       nextLine = "";

    try {
      while ((nextLine = bufferedReader.readLine()) != null) {
        clobData.append(nextLine).append(System.lineSeparator());
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      bufferedReader.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    return clobData.toString();
  }

  //  private final void prepStmntInsertColFlagNYOpt(PreparedStatement preparedStatement, final int columnPos, final int rowCount) {
  //    try {
  //      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
  //        preparedStatement.setNull(columnPos, java.sql.Types.VARCHAR);
  //      } else {
  //        prepStmntInsertColFlagNY(preparedStatement, columnPos, rowCount);
  //      }
  //    } catch (SQLException e) {
  //      e.printStackTrace();
  //      System.exit(1);
  //    }
  //  }

  //  private final void prepStmntInsertColIntOpt(PreparedStatement preparedStatement, final int columnPos, final int rowCount, final int upperLimit) {
  //    try {
  //      if (getRandomIntIncluded(rowCount) % RANDOM_NUMBER == 0) {
  //        preparedStatement.setNull(columnPos, java.sql.Types.INTEGER);
  //      } else {
  //        preparedStatement.setInt(columnPos, getRandomIntIncluded(upperLimit));
  //      }
  //    } catch (SQLException e) {
  //      e.printStackTrace();
  //      System.exit(1);
  //    }
  //  }

  private final void recreatePkList(final String tableName) {
    ArrayList<Object> pkList    = new ArrayList<Object>();

    Statement         statement = null;

    try {
      statement = connection.createStatement();

      resultSet = statement.executeQuery("SELECT PK_" + tableName + "_ID FROM " + tableNameDelimiter + tableName + tableNameDelimiter);

      while (resultSet.next()) {
        pkList.add(resultSet.getInt(1));
      }

      resultSet.close();

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    savePkList(tableName,
               pkList);
  }

  private final void retrieveFkList(final String tableName) {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    switch (tableName) {
    case TABLE_NAME_CITY:
      if (pkListCountryState.size() == 0) {
        recreatePkList(TABLE_NAME_COUNTRY_STATE);
      }

      break;
    case TABLE_NAME_COMPANY:
      if (pkListCity.size() == 0) {
        recreatePkList(TABLE_NAME_CITY);
      }

      break;
    case TABLE_NAME_COUNTRY_STATE:
      if (pkListCountry.size() == 0) {
        recreatePkList(TABLE_NAME_COUNTRY);
      }

      if (pkListTimezone.size() == 0) {
        recreatePkList(TABLE_NAME_TIMEZONE);
      }
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  private final void savePkList(final String tableName, final ArrayList<Object> pkList) {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    switch (tableName) {
    case TABLE_NAME_CITY:
      pkListCity = pkList;
      break;
    case TABLE_NAME_COMPANY:
      break;
    case TABLE_NAME_COUNTRY:
      pkListCountry = pkList;
      break;
    case TABLE_NAME_COUNTRY_STATE:
      pkListCountryState = pkList;
      break;
    case TABLE_NAME_TIMEZONE:
      pkListTimezone = pkList;
      break;
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME,
                                                                                           tableName));
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  /**
   * Delete any existing relevant database schema objects (database, user, 
   * schema or tables)and initialise the database for a new run.
   */
  protected abstract void setupDatabase();

  private void validateEncoding(String tableName, String columnName, int rowCount) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    switch (rowCount) {
    case 0:
      logger.info(String.format(FORMAT_METHOD_NAME,
                                methodName) + "- database table " + String.format(FORMAT_TABLE_NAME,
                                                                                  tableName) + " - no rows generated");
      break;
    case 1:
      validateEncodingType(tableName,
                           columnName,
                           0);
      break;
    case 2:
      validateEncodingType(tableName,
                           columnName,
                           0);
      validateEncodingType(tableName,
                           columnName,
                           1);
      break;
    default:
      validateEncodingType(tableName,
                           columnName,
                           0);
      validateEncodingType(tableName,
                           columnName,
                           1);
      validateEncodingType(tableName,
                           columnName,
                           2);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  private void validateEncodingType(String tableName, String columnName, int rowNo) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    int    count        = 0;

    String encodingType = "";

    switch (rowNo) {
    case 0:
      encodingType = "ASCII";
      break;
    case 1:
      encodingType = "ISO_8859_1";
      break;
    case 2:
      encodingType = "UTF_8";
      break;
    default:
      logger.error(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- database table " + String.format(FORMAT_TABLE_NAME,
                                                                                   tableName) + " - wrong encoding key : " + rowNo);
      System.exit(1);
    }

    try {
      preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM " + tableNameDelimiter + tableName + tableNameDelimiter + " WHERE " + columnName
          + " = ?");
      preparedStatement.setString(1,
                                  getColumnContent(columnName + "_",
                                                   rowNo));

      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      switch (count) {
      case 0:
        logger.error(String.format(FORMAT_METHOD_NAME,
                                   methodName) + "- database table " + String.format(FORMAT_TABLE_NAME,
                                                                                     tableName) + " - no rows generated - comparison value='"
            + getColumnContent(columnName + "_",
                               rowNo) + "'");
        System.exit(1);
      case 1:
        if (isDebug) {
          logger.debug(String.format(FORMAT_METHOD_NAME,
                                     methodName) + "- database table " + String.format(FORMAT_TABLE_NAME,
                                                                                       tableName) + " - encoding " + encodingType + " ok");
        }
        break;
      default:
        logger.error(String.format(FORMAT_METHOD_NAME,
                                   methodName) + "- database table " + String.format(FORMAT_TABLE_NAME,
                                                                                     tableName) + " - too many hits: " + count);
        System.exit(1);
      }

      resultSet.close();

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  private final void validateNumberRows(String tableName, int expectedRows) {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    int count = 0;

    try {
      statement = connection.createStatement();

      resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + tableNameDelimiter + tableName + tableNameDelimiter);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (expectedRows == count) {
      logger.info(String.format(FORMAT_METHOD_NAME,
                                methodName) + "- database table " + String.format(FORMAT_TABLE_NAME,
                                                                                  tableName) + " - " + String.format(FORMAT_ROW_NO,
                                                                                                                     count) + " rows created");
    } else {
      logger.fatal(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- database table " + String.format(FORMAT_TABLE_NAME,
                                                                                   tableName) + " is incomplete - expected" + String.format(FORMAT_ROW_NO,
                                                                                                                                            expectedRows)
          + " rows - found " + String.format(FORMAT_ROW_NO,
                                             count) + " rows");
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }
}
