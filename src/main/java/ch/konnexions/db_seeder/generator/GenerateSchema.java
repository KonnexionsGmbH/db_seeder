package ch.konnexions.db_seeder.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import ch.konnexions.db_seeder.AbstractDbmsSeeder.DbmsEnum;
import ch.konnexions.db_seeder.schema.SchemaPojo;
import ch.konnexions.db_seeder.schema.SchemaPojo.Table;
import ch.konnexions.db_seeder.schema.SchemaPojo.Table.Column;
import ch.konnexions.db_seeder.schema.SchemaPojo.Table.Column.ColumnConstraint;
import ch.konnexions.db_seeder.schema.SchemaPojo.Table.TableConstraint;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Test Data Generator for a Database - Transform JSON to POJO.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-07-15
 */
public class GenerateSchema {

  private static final Logger                logger                  = Logger.getLogger(GenerateSchema.class);

  private int                                errors                  = 0;

  private Map<String, ArrayList<String>>     genTableNameColumnNames = new HashMap<>();
  private List<String>                       genTableHierarchy       = new ArrayList<>();
  private HashMap<String, ArrayList<Column>> genTablesColumns        = new HashMap<>();
  private List<String>                       genTableNames           = new ArrayList<>();
  private Map<String, Integer>               genTableNumberOfRows    = new HashMap<>();
  private Set<String>                        genVarcharColumnNames   = new HashSet<>();

  private final boolean                      isDebug                 = logger.isDebugEnabled();

  private String                             printDate;
  private final String                       printDatePattern        = "yyyy-MM-dd";

  private int                                valDefaultNumberOfRows;
  private Set<Table>                         valTables;
  private HashMap<String, HashSet<String>>   valTablesColumns;
  private HashMap<String, HashSet<String>>   valTableNameForeignKeys = new HashMap<>();

  /**
   * Instantiates a new GenerateSchema object.
   */
  public GenerateSchema() {
    super();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(printDatePattern);

    printDate = simpleDateFormat.format(new Date());
  }

  private void generateClassDbmsSchema(String release, SchemaPojo schemaPojo, DbmsEnum tickerSymbol) {
  }

  /**
   * Generate the Java classes.
   *
   * @param release the current release number
   * @param schemaPojo the schema POJO
   */
  private void generateClasses(String release, SchemaPojo schemaPojo) {
    if (isDebug) {
      logger.debug("Start");
    }

    MessageHandling.startProgress(logger,
                                  "Start generating Java classes");

    generateClassSchema(release,
                        schemaPojo);

    logger.info("===> Generated class: AbstractGenSchema");

    generateClassSeeder(release,
                        schemaPojo);

    logger.info("===> Generated class: AbstractGenSeeder");

    for (DbmsEnum tickerSymbol : DbmsEnum.values()) {
      String tickerSymbolLower  = tickerSymbol.getTicketSymbol().toLowerCase();
      String tickerSymbolPascal = tickerSymbolLower.substring(0,
                                                              1).toUpperCase() + tickerSymbolLower.substring(1);

      generateClassDbmsSchema(release,
                              schemaPojo,
                              tickerSymbol);

      logger.info("===> Generated class: Abstract" + tickerSymbolPascal + "GenSchema");
    }

    logger.info("");
    logger.info("===> The Java class generation was successful.");
    logger.info("-".repeat(74));

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Generate the Java class AbstractGenSchema.
   *
   * @param release the current release number
   * @param schemaPojo the schema POJO
   */
  private void generateClassSchema(String release, SchemaPojo schemaPojo) {
    if (isDebug) {
      logger.debug("Start");
    }

    BufferedWriter bufferedWriter = null;
    try {
      bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(
          "src/main/java/ch/konnexions/db_seeder/generated/AbstractGenSchema.java"), false), StandardCharsets.UTF_8));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      generateCodeSchema(bufferedWriter,
                         release,
                         schemaPojo);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Generate the Java class AbstractGenSeeder.
   *
   * @param release the current release number
   * @param schemaPojo the schema POJO
   */
  private void generateClassSeeder(String release, SchemaPojo schemaPojo) {
    if (isDebug) {
      logger.debug("Start");
    }

    BufferedWriter bufferedWriter = null;
    try {
      bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(
          "src/main/java/ch/konnexions/db_seeder/generated/AbstractGenSeeder.java"), false), StandardCharsets.UTF_8));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      generateCodeSeeder(bufferedWriter,
                         release,
                         schemaPojo);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Generate the code of the Java class Abstract<db_ticker>Schema.
   *
   * @param bw the buffered writer
   * @param release the current release number
   * @param schemaPojo the schema POJO
   */
  @SuppressWarnings("unused")
  private void generateCodeDbmsSchema(BufferedWriter bw, String release, SchemaPojo schemaPojo) throws IOException {
    try {
      bw.append("package ch.konnexions.db_seeder.generated;");
      bw.newLine();
      bw.newLine();
      bw.append("import java.util.HashMap;");
      bw.newLine();
      bw.newLine();
      bw.append("import org.apache.log4j.Logger;");
      bw.newLine();
      bw.newLine();
      bw.append("/**");
      bw.newLine();
      bw.append(" * CREATE TABLE statements for a ##DBMS_NAME## DBMS.");
      bw.newLine();
      bw.append(" * <br>");
      bw.newLine();
      bw.append(" * @author  GenerateSchema.class");
      bw.newLine();
      bw.append(" * @version " + release);
      bw.newLine();
      bw.append(" * @since   " + printDate);
      bw.newLine();
      bw.append(" */");
      bw.newLine();
      bw.append("public abstract class AbstractGen##DBMS_NAME_PASCAL##Schema extends AbstractGenSeeder {");
      bw.newLine();
      bw.newLine();
      bw.append("  public static final HashMap<String, String> createTableStmnts = createTableStmnts();");
      bw.newLine();
      bw.newLine();
      bw.append("  private static final Logger                 logger            = Logger.getLogger(AbstractGen##DBMS_NAME_PASCAL##Schema.class);");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Creates the CREATE TABLE statements.");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  @SuppressWarnings(\"preview\")");
      bw.newLine();
      bw.append("  private static HashMap<String, String> createTableStmnts() {");
      bw.newLine();
      bw.append("    HashMap<String, String> statements = new HashMap<>();");
      bw.newLine();
      bw.newLine();
      //
      // <=== TBD ===>
      //
      bw.append("    statements.put(TABLE_NAME_CITY,");
      bw.newLine();
      bw.append("                   \"\"\"");
      bw.newLine();
      bw.append("                   CREATE TABLE `CITY` (");
      bw.newLine();
      bw.append("                      `PK_CITY_ID`          BIGINT       NOT NULL PRIMARY KEY,");
      bw.newLine();
      bw.append("                      `FK_COUNTRY_STATE_ID` BIGINT,");
      bw.newLine();
      bw.append("                      `CITY_MAP`            LONGBLOB,");
      bw.newLine();
      bw.append("                      `CREATED`             DATETIME     NOT NULL,");
      bw.newLine();
      bw.append("                      `MODIFIED`            DATETIME,");
      bw.newLine();
      bw.append("                      `NAME`                VARCHAR(100) NOT NULL,");
      bw.newLine();
      bw.append("                       CONSTRAINT `FK_CITY_COUNTRY_STATE` FOREIGN KEY `FK_CITY_COUNTRY_STATE` (`FK_COUNTRY_STATE_ID`) REFERENCES `COUNTRY_STATE` (`PK_COUNTRY_STATE_ID`)");
      bw.newLine();
      bw.append("                   )");
      bw.newLine();
      bw.append("                   \"\"\");");
      bw.newLine();
      bw.newLine();
      bw.append("    statements.put(TABLE_NAME_COMPANY,");
      bw.newLine();
      bw.append("                   \"\"\"");
      bw.newLine();
      bw.append("                   CREATE TABLE `COMPANY` (");
      bw.newLine();
      bw.append("                      `PK_COMPANY_ID` BIGINT       NOT NULL PRIMARY KEY,");
      bw.newLine();
      bw.append("                      `FK_CITY_ID`    BIGINT       NOT NULL,");
      bw.newLine();
      bw.append("                      `ACTIVE`        VARCHAR(1)   NOT NULL,");
      bw.newLine();
      bw.append("                      `ADDRESS1`      VARCHAR(50),");
      bw.newLine();
      bw.append("                      `ADDRESS2`      VARCHAR(50),");
      bw.newLine();
      bw.append("                      `ADDRESS3`      VARCHAR(50),");
      bw.newLine();
      bw.append("                      `CREATED`       DATETIME     NOT NULL,");
      bw.newLine();
      bw.append("                      `DIRECTIONS`    LONGTEXT,");
      bw.newLine();
      bw.append("                      `EMAIL`         VARCHAR(100),");
      bw.newLine();
      bw.append("                      `FAX`           VARCHAR(50),");
      bw.newLine();
      bw.append("                      `MODIFIED`      DATETIME,");
      bw.newLine();
      bw.append("                      `NAME`          VARCHAR(250) NOT NULL UNIQUE,");
      bw.newLine();
      bw.append("                      `PHONE`         VARCHAR(50),");
      bw.newLine();
      bw.append("                      `POSTAL_CODE`   VARCHAR(50),");
      bw.newLine();
      bw.append("                      `URL`           VARCHAR(250),");
      bw.newLine();
      bw.append("                      `VAT_ID_NUMBER` VARCHAR(100),");
      bw.newLine();
      bw.append("                       CONSTRAINT `FK_COMPANY_CITY` FOREIGN KEY `FK_COMPANY_CITY` (`FK_CITY_ID`) REFERENCES `CITY` (`PK_CITY_ID`)");
      bw.newLine();
      bw.append("                   )");
      bw.newLine();
      bw.append("                   \"\"\");");
      bw.newLine();
      bw.newLine();
      bw.append("    statements.put(TABLE_NAME_COUNTRY,");
      bw.newLine();
      bw.append("                   \"\"\"");
      bw.newLine();
      bw.append("                   CREATE TABLE `COUNTRY` (");
      bw.newLine();
      bw.append("                      `PK_COUNTRY_ID` BIGINT       NOT NULL PRIMARY KEY,");
      bw.newLine();
      bw.append("                      `COUNTRY_MAP`   LONGBLOB,");
      bw.newLine();
      bw.append("                      `CREATED`       DATETIME     NOT NULL,");
      bw.newLine();
      bw.append("                      `ISO3166`       VARCHAR(50),");
      bw.newLine();
      bw.append("                      `MODIFIED`      DATETIME,");
      bw.newLine();
      bw.append("                      `NAME`          VARCHAR(100) NOT NULL UNIQUE");
      bw.newLine();
      bw.append("                   )");
      bw.newLine();
      bw.append("                   \"\"\");");
      bw.newLine();
      bw.newLine();
      bw.append("    statements.put(TABLE_NAME_COUNTRY_STATE,");
      bw.newLine();
      bw.append("                   \"\"\"");
      bw.newLine();
      bw.append("                   CREATE TABLE `COUNTRY_STATE` (");
      bw.newLine();
      bw.append("                      `PK_COUNTRY_STATE_ID` BIGINT       NOT NULL PRIMARY KEY,");
      bw.newLine();
      bw.append("                      `FK_COUNTRY_ID`       BIGINT       NOT NULL,");
      bw.newLine();
      bw.append("                      `FK_TIMEZONE_ID`      BIGINT       NOT NULL,");
      bw.newLine();
      bw.append("                      `COUNTRY_STATE_MAP`   LONGBLOB,");
      bw.newLine();
      bw.append("                      `CREATED`             DATETIME     NOT NULL,");
      bw.newLine();
      bw.append("                      `MODIFIED`            DATETIME,");
      bw.newLine();
      bw.append("                      `NAME`                VARCHAR(100) NOT NULL,");
      bw.newLine();
      bw.append("                      `SYMBOL`              VARCHAR(50),");
      bw.newLine();
      bw.append("                       CONSTRAINT `FK_COUNTRY_STATE_COUNTRY`  FOREIGN KEY `FK_COUNTRY_STATE_COUNTRY`  (`FK_COUNTRY_ID`)  REFERENCES `COUNTRY`  (`PK_COUNTRY_ID`),");
      bw.newLine();
      bw.append("                       CONSTRAINT `FK_COUNTRY_STATE_TIMEZONE` FOREIGN KEY `FK_COUNTRY_STATE_TIMEZONE` (`FK_TIMEZONE_ID`) REFERENCES `TIMEZONE` (`PK_TIMEZONE_ID`),");
      bw.newLine();
      bw.append("                       CONSTRAINT `UQ_COUNTRY_STATE`          UNIQUE (`FK_COUNTRY_ID`,`NAME`)");
      bw.newLine();
      bw.append("                   )");
      bw.newLine();
      bw.append("                   \"\"\");");
      bw.newLine();
      bw.newLine();
      bw.append("    statements.put(TABLE_NAME_TIMEZONE,");
      bw.newLine();
      bw.append("                   \"\"\"");
      bw.newLine();
      bw.append("                   CREATE TABLE `TIMEZONE` (");
      bw.newLine();
      bw.append("                      `PK_TIMEZONE_ID` BIGINT        NOT NULL PRIMARY KEY,");
      bw.newLine();
      bw.append("                      `ABBREVIATION`   VARCHAR(50)   NOT NULL,");
      bw.newLine();
      bw.append("                      `CREATED`        DATETIME      NOT NULL,");
      bw.newLine();
      bw.append("                      `MODIFIED`       DATETIME,");
      bw.newLine();
      bw.append("                      `NAME`           VARCHAR(100)  NOT NULL UNIQUE,");
      bw.newLine();
      bw.append("                      `V_TIME_ZONE`    VARCHAR(4000)");
      bw.newLine();
      bw.append("                   )");
      bw.newLine();
      bw.append("                   \"\"\");");
      bw.newLine();
      bw.newLine();
      bw.append("    return statements;");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Instantiates a new abstract ##DBMS_NAME## schema object.");
      bw.newLine();
      bw.append("   *");
      bw.newLine();
      bw.append("   * @param dbmsTickerSymbol DBMS ticker symbol ");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  public AbstractGen##DBMS_NAME_PASCAL##Schema(String dbmsTickerSymbol) {");
      bw.newLine();
      bw.append("    super(dbmsTickerSymbol);");
      bw.newLine();
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"Start Constructor - dbmsTickerSymbol=\" + dbmsTickerSymbol);");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"End   Constructor\");");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.append("}");
      bw.newLine();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Generate the code of the Java class AbstractGenSchema.
   *
   * @param bw the buffered writer
   * @param release the current release number
   * @param schemaPojo the schema POJO
   */
  private void generateCodeSchema(BufferedWriter bw, String release, SchemaPojo schemaPojo) throws IOException {
    try {
      bw.append("package ch.konnexions.db_seeder.generated;");
      bw.newLine();
      bw.newLine();
      bw.append("import java.util.Arrays;");
      bw.newLine();
      bw.append("import java.util.HashMap;");
      bw.newLine();
      bw.append("import java.util.Properties;");
      bw.newLine();
      bw.newLine();
      bw.append("import org.apache.log4j.Logger;");
      bw.newLine();
      bw.newLine();
      bw.append("import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;");
      bw.newLine();
      bw.newLine();
      bw.append("/**");
      bw.newLine();
      bw.append(" * Test Data Generator for a Database - Abstract Generated Schema.");
      bw.newLine();
      bw.append(" * <br>");
      bw.newLine();
      bw.append(" * @author  GenerateSchema.class");
      bw.newLine();
      bw.append(" * @version " + release);
      bw.newLine();
      bw.append(" * @since   " + printDate);
      bw.newLine();
      bw.append(" */");
      bw.newLine();
      bw.append("abstract class AbstractGenSchema extends AbstractJdbcSeeder {");
      bw.newLine();
      bw.newLine();

      for (String tableName : genTableNames) {
        bw.append("  protected static final String TABLE_NAME_" + String.format("%-30s",
                                                                                tableName) + " = \"" + tableName + "\";");
        bw.newLine();
      }

      bw.newLine();
      bw.append("  private static final Logger   logger                   = Logger.getLogger(AbstractGenSchema.class);");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Initialises a new abstract generated schema object.");
      bw.newLine();
      bw.append("   *");
      bw.newLine();
      bw.append("   * @param dbmsTickerSymbol DBMS ticker symbol ");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  public AbstractGenSchema(String dbmsTickerSymbol) {");
      bw.newLine();
      bw.append("    super(dbmsTickerSymbol);");
      bw.newLine();
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"Start Constructor - dbmsTickerSymbol=\" + dbmsTickerSymbol);");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.newLine();
      bw.append("    initConstants();");
      bw.newLine();
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"End   Constructor\");");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Initialises a new abstract generated schema object.");
      bw.newLine();
      bw.append("   *");
      bw.newLine();
      bw.append("   * @param dbmsTickerSymbol DBMS ticker symbol ");
      bw.newLine();
      bw.append("   * @param isClient client database version");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  public AbstractGenSchema(String dbmsTickerSymbol, boolean isClient) {");
      bw.newLine();
      bw.append("    super(dbmsTickerSymbol, isClient);");
      bw.newLine();
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"Start Constructor - dbmsTickerSymbol=\" + dbmsTickerSymbol + \" - isClient=\" + isClient);");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.newLine();
      bw.append("    initConstants();");
      bw.newLine();
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"End   Constructor\");");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  protected final Properties createColumnNames() {");
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"Start\");");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.newLine();
      bw.append("    Properties columnName = new Properties();");
      bw.newLine();
      bw.newLine();
      bw.append("    // Encoding ASCII");
      bw.newLine();

      Set<String> genVarcharColumnNamesSorted = new TreeSet<String>(genVarcharColumnNames);

      for (String columnName : genVarcharColumnNamesSorted) {
        bw.append("    columnName.setProperty(\"" + columnName + "_0\",");
        bw.newLine();
        bw.append("                           \"\");");
        bw.newLine();
      }

      bw.newLine();
      bw.append("    // Encoding ISO_8859_1");
      bw.newLine();
      bw.append("    boolean isIso_8859_1 = config.getEncodingIso_8859_1();");
      bw.newLine();
      bw.newLine();

      for (String columnName : genVarcharColumnNamesSorted) {
        bw.append("    columnName.setProperty(\"" + columnName + "_1\",");
        bw.newLine();
        bw.append("                           isIso_8859_1 ? \"ÁÇÉÍÑÓ_\" : \"NO_ISO_8859_1_\");");
        bw.newLine();
      }

      bw.newLine();
      bw.append("    // Encoding UTF_8");
      bw.newLine();
      bw.append("    boolean isUtf_8 = config.getEncodingUtf_8();");
      bw.newLine();
      bw.newLine();

      for (String columnName : genVarcharColumnNamesSorted) {
        bw.append("    columnName.setProperty(\"" + columnName + "_2\",");
        bw.newLine();
        bw.append("                           isUtf_8 ? \"缩略语地址电子邮件传真_\" : \"NO_UTF_8_\");");
        bw.newLine();
      }

      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"End\");");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.newLine();
      bw.append("    return columnName;");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Initialising constants.");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  @SuppressWarnings(\"serial\")");
      bw.newLine();
      bw.append("  private void initConstants() {");
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"Start\");");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.newLine();
      bw.append("    dmlStatements      = new HashMap<>() {");
      bw.newLine();
      bw.append("                         {");
      bw.newLine();

      for (String tableName : genTableNames) {
        List<String> columnNames          = genTableNameColumnNames.get(tableName);

        String       columnnamesWithComma = String.join(",",
                                                        columnNames);
        String       questionMarks        = "";

        for (int i = 0; i < columnNames.size(); i++) {
          questionMarks = questionMarks + (i == 0 ? "?" : ",?");
        }

        bw.append("                           put(TABLE_NAME_" + tableName + ",");
        bw.newLine();
        bw.append("                               \"" + columnnamesWithComma + ") VALUES (" + questionMarks + "\");");
        bw.newLine();
      }

      bw.append("                         }");
      bw.newLine();
      bw.append("                       };");
      bw.newLine();
      bw.newLine();
      bw.append("    maxRowSizes        = new HashMap<>() {");
      bw.newLine();
      bw.append("                         {");
      bw.newLine();

      for (String tableName : genTableNames) {
        bw.append("                           put(TABLE_NAME_" + tableName + ",");
        bw.newLine();
        bw.append("                               " + genTableNumberOfRows.get(tableName).toString() + ");");
        bw.newLine();
      }

      bw.append("                         }");
      bw.newLine();
      bw.append("                       };");
      bw.newLine();
      bw.newLine();

      int count = 0;

      for (String tableName : genTableHierarchy) {
        if (count == 0) {
          bw.append("    TABLE_NAMES_CREATE = Arrays.asList(TABLE_NAME_");
        } else {
          bw.append("                                       TABLE_NAME_");
        }

        bw.append(tableName);

        count++;

        if (genTableHierarchy.size() == count) {
          bw.append(");");
        } else {
          bw.append(",");
        }

        bw.newLine();
      }

      bw.newLine();

      Collections.reverse(genTableHierarchy);

      count = 0;

      for (String tableName : genTableHierarchy) {
        if (count == 0) {
          bw.append("    TABLE_NAMES_DROP   = Arrays.asList(TABLE_NAME_");
        } else {
          bw.append("                                       TABLE_NAME_");
        }

        bw.append(tableName);

        count++;

        if (genTableHierarchy.size() == count) {
          bw.append(");");
        } else {
          bw.append(",");
        }

        bw.newLine();
      }

      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"End\");");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.append("}");
      bw.newLine();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Generate the code of the Java class AbstractGenSeeder.
   *
   * @param bw the buffered writer
   * @param release the current release number
   * @param schemaPojo the schema POJO
   */
  private void generateCodeSeeder(BufferedWriter bw, String release, SchemaPojo schemaPojo) throws IOException {
    try {
      bw.append("package ch.konnexions.db_seeder.generated;");
      bw.newLine();
      bw.newLine();
      bw.append("import java.sql.PreparedStatement;");
      bw.newLine();
      bw.append("import java.sql.Timestamp;");
      bw.newLine();
      bw.append("import java.util.ArrayList;");
      bw.newLine();
      bw.append("import java.util.Arrays;");
      bw.newLine();
      bw.append("import java.util.List;");
      bw.newLine();
      bw.newLine();
      bw.append("import org.apache.log4j.Logger;");
      bw.newLine();
      bw.newLine();
      bw.append("/**");
      bw.newLine();
      bw.append(" * Test Data Generator for a Database - Abstract Generated Seeder.");
      bw.newLine();
      bw.append(" * <br>");
      bw.newLine();
      bw.append(" * @author  GenerateSchema.class");
      bw.newLine();
      bw.append(" * @version " + release);
      bw.newLine();
      bw.append(" * @since   " + printDate);
      bw.newLine();
      bw.append(" */");
      bw.newLine();
      bw.append("abstract class AbstractGenSeeder extends AbstractGenSchema {");
      bw.newLine();
      bw.newLine();
      bw.append("  private static final Logger logger = Logger.getLogger(AbstractGenSeeder.class);");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Initialises a new abstract generated seeder object.");
      bw.newLine();
      bw.append("   *");
      bw.newLine();
      bw.append("   * @param dbmsTickerSymbol DBMS ticker symbol ");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  public AbstractGenSeeder(String dbmsTickerSymbol) {");
      bw.newLine();
      bw.append("    super(dbmsTickerSymbol);");
      bw.newLine();
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"Start Constructor - dbmsTickerSymbol=\" + dbmsTickerSymbol);");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"End   Constructor\");");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Initialises a new abstract generated seeder object.");
      bw.newLine();
      bw.append("   *");
      bw.newLine();
      bw.append("   * @param dbmsTickerSymbol DBMS ticker symbol ");
      bw.newLine();
      bw.append("   * @param isClient client database version");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  public AbstractGenSeeder(String dbmsTickerSymbol, boolean isClient) {");
      bw.newLine();
      bw.append("    super(dbmsTickerSymbol, isClient);");
      bw.newLine();
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"Start Constructor - dbmsTickerSymbol=\" + dbmsTickerSymbol + \" - isClient=\" + isClient);");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"End   Constructor\");");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Creates a content value of type BIGINT.");
      bw.newLine();
      bw.append("   *");
      bw.newLine();
      bw.append("   * @param preparedStatement the prepared statement");
      bw.newLine();
      bw.append("   * @param tableName         the table name");
      bw.newLine();
      bw.append("   * @param columnName        the column name");
      bw.newLine();
      bw.append("   * @param rowNo             the current row number");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  @Override");
      bw.newLine();
      bw.append("  protected final long getContentBigint(String tableName, String columnName, long rowNo) {");
      bw.newLine();
      bw.append("    long result = super.getContentBigint(tableName,");
      bw.newLine();
      bw.append("                                         columnName,");
      bw.newLine();
      bw.append("                                         rowNo);");
      bw.newLine();
      bw.newLine();
      bw.append("    return result;");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Creates a content value of type BLOB.");
      bw.newLine();
      bw.append("   *");
      bw.newLine();
      bw.append("   * @param preparedStatement the prepared statement");
      bw.newLine();
      bw.append("   * @param tableName         the table name");
      bw.newLine();
      bw.append("   * @param columnName        the column name");
      bw.newLine();
      bw.append("   * @param rowNo             the current row number");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  @Override");
      bw.newLine();
      bw.append("  protected byte[] getContentBlob(String tableName, String columnName, int rowNo) {");
      bw.newLine();
      bw.append("    byte[] result = super.getContentBlob(tableName,");
      bw.newLine();
      bw.append("                                         columnName,");
      bw.newLine();
      bw.append("                                         rowNo);");
      bw.newLine();
      bw.newLine();
      bw.append("    return result;");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Creates a content value of type CLOB.");
      bw.newLine();
      bw.append("   *");
      bw.newLine();
      bw.append("   * @param preparedStatement the prepared statement");
      bw.newLine();
      bw.append("   * @param tableName         the table name");
      bw.newLine();
      bw.append("   * @param columnName        the column name");
      bw.newLine();
      bw.append("   * @param rowNo             the current row number");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  @Override");
      bw.newLine();
      bw.append("  protected String getContentClob(String tableName, String columnName, int rowNo) {");
      bw.newLine();
      bw.append("    String result = super.getContentClob(tableName,");
      bw.newLine();
      bw.append("                                         columnName,");
      bw.newLine();
      bw.append("                                         rowNo);");
      bw.newLine();
      bw.newLine();
      bw.append("    return result;");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Creates a content value of type foreign key value.");
      bw.newLine();
      bw.append("   *");
      bw.newLine();
      bw.append("   * @param preparedStatement the prepared statement");
      bw.newLine();
      bw.append("   * @param tableName         the table name");
      bw.newLine();
      bw.append("   * @param columnName        the column name");
      bw.newLine();
      bw.append("   * @param rowNo             the current row number");
      bw.newLine();
      bw.append("   * @param fkList            the existing foreign keys");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  @Override");
      bw.newLine();
      bw.append("  protected Object getContentFk(String tableName, String columnName, int rowNo, ArrayList<Object> fkList) {");
      bw.newLine();
      bw.append("    Object result = super.getContentFk(tableName,");
      bw.newLine();
      bw.append("                                       columnName,");
      bw.newLine();
      bw.append("                                       rowNo,");
      bw.newLine();
      bw.append("                                       fkList);");
      bw.newLine();
      bw.newLine();
      bw.append("    return result;");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Creates a content value of type TIMESTAMP.");
      bw.newLine();
      bw.append("   *");
      bw.newLine();
      bw.append("   * @param preparedStatement the prepared statement");
      bw.newLine();
      bw.append("   * @param tableName         the table name");
      bw.newLine();
      bw.append("   * @param columnName        the column name");
      bw.newLine();
      bw.append("   * @param rowNo             the current row number");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  @Override");
      bw.newLine();
      bw.append("  protected Timestamp getContentTimestamp(String tableName, String columnName, int rowNo) {");
      bw.newLine();
      bw.append("    Timestamp result = super.getContentTimestamp(tableName,");
      bw.newLine();
      bw.append("                                                 columnName,");
      bw.newLine();
      bw.append("                                                 rowNo);");
      bw.newLine();
      bw.newLine();
      bw.append("    return result;");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  /**");
      bw.newLine();
      bw.append("   * Creates a content value of type VARCHAR.");
      bw.newLine();
      bw.append("   *");
      bw.newLine();
      bw.append("   * @param preparedStatement the prepared statement");
      bw.newLine();
      bw.append("   * @param tableName         the table name");
      bw.newLine();
      bw.append("   * @param columnName        the column name");
      bw.newLine();
      bw.append("   * @param rowNo             the current row number");
      bw.newLine();
      bw.append("   * @param size              the column size");
      bw.newLine();
      bw.append("   * @param lowerRange        the lower range");
      bw.newLine();
      bw.append("   * @param upperRange        the upper range");
      bw.newLine();
      bw.append("   * @param validValues       the valid values");
      bw.newLine();
      bw.append("   */");
      bw.newLine();
      bw.append("  @Override");
      bw.newLine();
      bw.append("  protected String getContentVarchar(String tableName, String columnName, int rowNo, int size, String lowerRange, String upperRange, List<String> validValues) {");
      bw.newLine();
      bw.append("    String result = super.getContentVarchar(tableName,");
      bw.newLine();
      bw.append("                                            columnName,");
      bw.newLine();
      bw.append("                                            rowNo,");
      bw.newLine();
      bw.append("                                            size,");
      bw.newLine();
      bw.append("                                            lowerRange,");
      bw.newLine();
      bw.append("                                            upperRange,");
      bw.newLine();
      bw.append("                                            validValues);");
      bw.newLine();
      bw.newLine();
      bw.append("    return result;");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();
      bw.append("  protected final void insertTable(PreparedStatement preparedStatement,");
      bw.newLine();
      bw.append("                                   final String tableName,");
      bw.newLine();
      bw.append("                                   final int rowNo,");
      bw.newLine();
      bw.append("                                   final ArrayList<Object> pkList) {");
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"Start\");");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.newLine();
      bw.append("    switch (tableName) {");
      bw.newLine();

      for (String tableName : genTableNames) {
        bw.append("    case TABLE_NAME_" + tableName + " -> prepDmlStmntInsert" + getTableNamePascalCase(tableName) + "(preparedStatement,");
        bw.newLine();
        bw.append("                                                   rowNo);");
        bw.newLine();
      }

      bw.append("    default -> throw new RuntimeException(\"Not yet implemented - database table : \" + String.format(FORMAT_TABLE_NAME,");
      bw.newLine();
      bw.append("                                                                                                    tableName));");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.newLine();
      bw.append("    if (isDebug) {");
      bw.newLine();
      bw.append("      logger.debug(\"End\");");
      bw.newLine();
      bw.append("    }");
      bw.newLine();
      bw.append("  }");
      bw.newLine();
      bw.newLine();

      for (String tableName : genTableNames) {
        bw.append("  private void prepDmlStmntInsert" + getTableNamePascalCase(tableName) + "(PreparedStatement preparedStatement, int rowNo) {");
        bw.newLine();
        bw.append("    if (isDebug) {");
        bw.newLine();
        bw.append("      logger.debug(\"Start\");");
        bw.newLine();
        bw.append("    }");
        bw.newLine();
        bw.newLine();
        bw.append("    int i = 0;");
        bw.newLine();
        bw.newLine();

        List<Column> columns = genTablesColumns.get(tableName);

        for (Column column : columns) {
          String                 columnName        = column.getColumnName().toUpperCase();
          String                 dataType          = column.getDataType().toUpperCase();

          List<ColumnConstraint> columnConstraints = column.getColumnConstraints();

          String                 referenceTable    = getColumnConstraintForeign(columnConstraints);
          boolean                isNotNull         = getColumnConstraintNotNull(columnConstraints);
          boolean                isPrimaryKey      = getColumnConstraintPrimary(columnConstraints);

          if (isPrimaryKey) {
            isNotNull = true;
          }

          switch (dataType) {
          case "BIGINT":
            if ("".equals(referenceTable)) {
              bw.append("    prepStmntColBigint" + (isNotNull ? "" : "Opt") + "(preparedStatement,");
              bw.newLine();
              bw.append("                              \"" + tableName + "\",");
              bw.newLine();
              bw.append("                              \"" + columnName + "\",");
              bw.newLine();
              bw.append("                              ++i,");
              bw.newLine();
              bw.append("                              rowNo);");
              bw.newLine();
            } else {
              bw.append("    prepStmntColFk" + (isNotNull ? "" : "Opt") + "(preparedStatement,");
              bw.newLine();
              bw.append("                            \"" + tableName + "\",");
              bw.newLine();
              bw.append("                            \"" + columnName + "\",");
              bw.newLine();
              bw.append("                            ++i,");
              bw.newLine();
              bw.append("                            rowNo,");
              bw.newLine();
              bw.append("                            pkLists.get(TABLE_NAME_" + referenceTable + "));");
              bw.newLine();
            }

            break;
          case "BLOB":
            bw.append("    prepStmntColBlob" + (isNotNull ? "" : "Opt") + "(preparedStatement,");
            bw.newLine();
            bw.append("                              \"" + tableName + "\",");
            bw.newLine();
            bw.append("                              \"" + columnName + "\",");
            bw.newLine();
            bw.append("                              ++i,");
            bw.newLine();
            bw.append("                              rowNo);");
            bw.newLine();

            break;
          case "CLOB":
            bw.append("      prepStmntColClob" + (isNotNull ? "" : "Opt") + "(preparedStatement,");
            bw.newLine();
            bw.append("                                \"" + tableName + "\",");
            bw.newLine();
            bw.append("                                \"" + columnName + "\",");
            bw.newLine();
            bw.append("                                ++i,");
            bw.newLine();
            bw.append("                                rowNo);");
            bw.newLine();

            break;
          case "TIMESTAMP":
            bw.append("    prepStmntColTimestamp" + (isNotNull ? "" : "Opt") + "(preparedStatement,");
            bw.newLine();
            bw.append("                               \"" + tableName + "\",");
            bw.newLine();
            bw.append("                               \"" + columnName + "\",");
            bw.newLine();
            bw.append("                               ++i,");
            bw.newLine();
            bw.append("                               rowNo);");
            bw.newLine();
            break;
          case "VARCHAR":
            bw.append("    prepStmntColVarchar" + (isNotNull ? "" : "Opt") + "(preparedStatement,");
            bw.newLine();
            bw.append("                             \"" + tableName + "\",");
            bw.newLine();
            bw.append("                             \"" + columnName + "\",");
            bw.newLine();
            bw.append("                             ++i,");
            bw.newLine();
            bw.append("                             rowNo,");
            bw.newLine();
            bw.append("                             " + column.getSize() + ",");
            bw.newLine();

            if (column.getLowerRangeString() == null && column.getUpperRangeString() == null) {
              bw.append("                             null,");
              bw.newLine();
              bw.append("                             null,");
              bw.newLine();
            } else {
              bw.append("                             \"" + column.getLowerRangeString() + "\",");
              bw.newLine();
              bw.append("                             \"" + column.getUpperRangeString() + "\",");
              bw.newLine();
            }

            if (column.getValidValuesString() == null) {
              bw.append("                             null);");
              bw.newLine();
            } else {
              bw.append("                             Arrays.asList(\"" + String.join(",",
                                                                                      column.getValidValuesString()).replace(",",
                                                                                                                             "\",\"") + "\"));");
              bw.newLine();
            }

            break;
          default:
            MessageHandling.abortProgram(logger,
                                         "Database table: '" + tableName + "' column: '" + columnName + "' - Unknown data type '" + dataType + "'");
          }
        }

        bw.append("    if (isDebug) {");
        bw.newLine();
        bw.append("      logger.debug(\"End\");");
        bw.newLine();
        bw.append("    }");
        bw.newLine();
        bw.append("  }");
        bw.newLine();
        bw.newLine();
      }

      bw.append("}");
      bw.newLine();
      bw.newLine();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
  * Test Data Generator for a Database - Generate a complete schema.
   *
   * @param release the release identification
   * @param fileJsonName the name of the db_seeder schema definition file
   */
  public void generateSchema(String release, String fileJsonName) {
    if (isDebug) {
      logger.debug("Start");
    }

    String     fileSchemaName = "src/main/resources/db_seeder_schema.schema.json";

    JSONObject jsonSchema     = null;

    try {
      jsonSchema = new JSONObject(new JSONTokener(new FileInputStream(fileSchemaName)));
    } catch (JSONException | FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    JSONObject jsonSubject = null;

    try {
      jsonSubject = new JSONObject(new JSONTokener(new FileInputStream(fileJsonName)));
    } catch (JSONException | FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    validateJson(jsonSchema,
                 jsonSubject);

    SchemaPojo schemaPojo = transformJson2Pojo(fileJsonName);

    validateSchema(schemaPojo);

    generateClasses(release,
                    schemaPojo);

    if (isDebug) {
      logger.debug("End");
    }
  }

  private String getColumnConstraintForeign(List<ColumnConstraint> columnConstraints) {
    if (columnConstraints != null) {
      for (ColumnConstraint columnConstraint : columnConstraints) {
        if ("FOREIGN".equals(columnConstraint.getConstraintType().toUpperCase())) {
          return columnConstraint.getReferenceTable().toUpperCase();
        }
      }
    }

    return "";
  }

  private boolean getColumnConstraintNotNull(List<ColumnConstraint> columnConstraints) {
    if (columnConstraints != null) {
      for (ColumnConstraint columnConstraint : columnConstraints) {
        if ("NOT NULL".equals(columnConstraint.getConstraintType().toUpperCase())) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean getColumnConstraintPrimary(List<ColumnConstraint> columnConstraints) {
    if (columnConstraints != null) {
      for (ColumnConstraint columnConstraint : columnConstraints) {
        if ("PRIMARY".equals(columnConstraint.getConstraintType().toUpperCase())) {
          return true;
        }
      }
    }

    return false;
  }

  private String getTableNamePascalCase(String tableName) {
    StringBuilder tableNamePascalCase = new StringBuilder();

    boolean       isUnderline         = true;

    for (String ch : tableName.split("")) {
      if ("_".equals(ch)) {
        isUnderline = true;
        continue;
      }

      if (isUnderline) {
        tableNamePascalCase.append(ch);
        isUnderline = false;
      } else {
        tableNamePascalCase.append(ch.toLowerCase());
      }
    }

    return tableNamePascalCase.toString();
  }

  /**
   * Transform the schema in JSON format to POJO.
   *
   * @param fileJsonName the file name of the JSON schema file
   * @return the schema POJO
   */
  private SchemaPojo transformJson2Pojo(String fileJsonName) {
    if (isDebug) {
      logger.debug("Start");
    }

    MessageHandling.startProgress(logger,
                                  "Start transforming JSON to a POJO");

    Gson       gson       = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    SchemaPojo schemaPojo = null;

    try {
      schemaPojo = gson.fromJson(Files.newBufferedReader(Paths.get(fileJsonName)),
                                 SchemaPojo.class);
    } catch (JsonSyntaxException | IOException | JsonIOException e) {
      MessageHandling.abortProgram(logger,
                                   e.getMessage());
    }

    logger.info("===> The JSON to POJO transformation was successful.");
    logger.info("-".repeat(74));

    if (isDebug) {
      logger.debug("End");
    }

    return schemaPojo;
  }

  /**
   * Validate the JSON format of the schema definition.
   * 
   * @param jsonSchema the JSON schema definition
   * @param jsonSubject the db_seeder schema definition
   */
  private void validateJson(JSONObject jsonSchema, JSONObject jsonSubject) {
    if (isDebug) {
      logger.debug("Start");
    }

    MessageHandling.startProgress(logger,
                                  "Start JSON validation");

    Schema schema = SchemaLoader.load(jsonSchema);

    try {
      schema.validate(jsonSubject);
    } catch (ValidationException e) {
      MessageHandling.abortProgram(logger,
                                   e.getMessage());
    }

    logger.info("===> The JSON validation was successful.");
    logger.info("-".repeat(74));

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Validate the POJO format of the schema definition.
   *
   * @param schemaPojo the schema POJO
   */
  private void validateSchema(SchemaPojo schemaPojo) {
    if (isDebug) {
      logger.debug("Start");
    }

    MessageHandling.startProgress(logger,
                                  "Start validating the schema definition");

    valDefaultNumberOfRows = schemaPojo.getGlobals().getDefaultNumberOfRows();

    if (valDefaultNumberOfRows <= 0) {
      logger.error("Default number of rows must be greater than zero");
      errors++;
    }

    valTables        = schemaPojo.getTables();
    valTablesColumns = new HashMap<>();

    if (valTables == null || valTables.size() == 0) {
      logger.error("No definitions found for database valTableNames");
      errors++;
    } else {

      int    numberOfRows;
      String tableName;

      for (Table table : valTables) {
        tableName = table.getTableName();

        if (tableName != null) {
          tableName = tableName.toUpperCase();
        }

        if (valTablesColumns.containsKey(tableName)) {
          logger.error("Database table: '" + tableName + "' - This database table was defined more than once");
          errors++;
          continue;
        }

        numberOfRows = table.getNumberOfRows();

        if (numberOfRows < 0) {
          logger.error("Number of rows must not be negative");
          errors++;
        } else if (numberOfRows == 0) {
          numberOfRows = valDefaultNumberOfRows;
        }

        ArrayList<Column> columns = table.getColumns();

        if (columns == null || columns.size() == 0) {
          logger.error("Database table: '" + tableName + "' - No definitions found for table columns");
          errors++;
          continue;
        }

        HashSet<String> valColumnNames = validateSchemaColumns(tableName,
                                                               columns);

        if (errors == 0) {
          genTablesColumns.put(tableName,
                               columns);
          genTableNames.add(tableName);
          genTableNumberOfRows.put(tableName,
                                   numberOfRows);

          valTablesColumns.put(tableName,
                               valColumnNames);
        }
      }

      if (errors == 0) {
        Collections.sort(genTableNames);

        for (String tableNameSorted : genTableNames) {
          valTableNameForeignKeys.put(tableNameSorted,
                                      new HashSet<String>());
        }

        validateSchemaTableConstraints();

        validateSchemaColumnConstraints();

        if (errors == 0) {
          validateSchemaForeignKeys();
        }
      }
    }

    if (errors > 0) {
      MessageHandling.abortProgram(logger,
                                   "Program abort: " + errors + " error(s) occurred during the schema validation.");
    }

    logger.info("===> The schema validation was successful.");
    logger.info("-".repeat(74));

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Validate the column constraint definitions.
   */
  private void validateSchemaColumnConstraints() {
    if (isDebug) {
      logger.debug("Start");
    }

    List<Column>           columns;
    List<ColumnConstraint> columnConstraints;
    String                 columnName;
    String                 constraintType;
    String                 referenceColumn;
    String                 referenceTable;
    String                 tableName;

    HashSet<String>        valRefrenceColumnNames;

    for (Table table : valTables) {
      tableName = table.getTableName().toUpperCase();

      columns   = table.getColumns();

      for (Column column : columns) {
        columnConstraints = column.getColumnConstraints();

        if (columnConstraints == null || columnConstraints.size() == 0) {
          continue;
        }

        columnName = column.getColumnName();

        for (ColumnConstraint columnConstraint : columnConstraints) {
          constraintType  = columnConstraint.getConstraintType().toUpperCase();

          referenceColumn = columnConstraint.getReferenceColumn();

          if (referenceColumn != null) {
            referenceColumn = referenceColumn.toUpperCase();
          }

          referenceTable = columnConstraint.getReferenceTable();

          if (referenceTable != null) {
            referenceTable = referenceTable.toUpperCase();
          }

          switch (constraintType) {
          case "FOREIGN" -> {
            if (referenceTable == null) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Constraint type '" + constraintType
                  + "' requires a reference table");
              errors++;
            }

            if (tableName.equals(referenceTable)) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' constraint type: '" + constraintType
                  + "' - Reference table must be a different database table");
              errors++;
            }

            if (!(valTablesColumns.containsKey(referenceTable))) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' constraint type: '" + constraintType + "' - Reference table '"
                  + referenceTable + "' is not defined");
              errors++;
            }

            if (referenceColumn == null) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Constraint type '" + constraintType
                  + "' requires a reference column");
              errors++;
              continue;
            }

            valRefrenceColumnNames = valTablesColumns.get(referenceTable);

            if (!valRefrenceColumnNames.contains(referenceColumn)) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' constraint type: '" + constraintType + "' - Reference column '"
                  + referenceColumn + "' is not defined in database table '" + referenceTable + "'");
              errors++;
            }

            if (errors == 0) {
              HashSet<String> referenceTables = valTableNameForeignKeys.get(tableName);
              referenceTables.add(referenceTable);
              valTableNameForeignKeys.put(tableName,
                                          referenceTables);
            }
          }
          case "NOT NULL" -> {
            if (referenceTable != null) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Constraint type '" + constraintType
                  + "' does not allow a reference table");
              errors++;
            }
            if (referenceColumn != null) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Constraint type '" + constraintType
                  + "' does not allow a reference column");
              errors++;
            }
          }
          case "PRIMARY", "UNIQUE" -> {
            if (referenceTable != null) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Constraint type '" + constraintType
                  + "' does not allow a reference table");
              errors++;
            }
            if (referenceColumn != null) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Constraint type '" + constraintType
                  + "' does not allow a reference column");
              errors++;
            }
          }
          default -> {
            logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Unknown constraint type '" + constraintType + "'");
            errors++;
          }
          }
        }
      }
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Validate the column definitions of a specific database table.
   *
   * @param tableName the name of the database table
   * @param columns the database table columns columns
   * 
   * @return the hash set containing the column names
   */
  private HashSet<String> validateSchemaColumns(String tableName, ArrayList<Column> columns) {
    if (isDebug) {
      logger.debug("Start");
    }

    HashSet<String>   columnNames      = new HashSet<>();

    String            columnName;
    ArrayList<String> tableColumnNames = new ArrayList<>();
    String            dataType;
    int               precision;
    int               size;

    for (Column column : columns) {
      columnName = column.getColumnName();

      if (columnName != null) {
        columnName = columnName.toUpperCase();
      }

      if (!columnNames.add(columnName)) {
        logger.error("Database table: '" + tableName + "' column: '" + columnName
            + "' - This database column was defined more than once in this database table");
        errors++;
        continue;
      }

      if (column.getDataType() == null) {
        logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Data type is missing (null)");
        errors++;
        continue;
      }

      dataType  = column.getDataType().toUpperCase();
      size      = column.getSize();
      precision = column.getPrecision();

      switch (dataType) {
      case "BIGINT", "BLOB", "CLOB", "TIMESTAMP" -> {
        if (size > 0) {
          logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Data type '" + dataType + "' does not allow a value for size ("
              + size + ")");
          errors++;
        }
        if (precision > 0) {
          logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Data type '" + dataType + "' does not allow a value for precision ("
              + precision + ")");
          errors++;
        }
      }
      case "VARCHAR" -> {
        if (size == 0) {
          logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Size is missing (null)");
          errors++;
          continue;
        }
        if (size < 1 || size > 4000) {
          logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Size must have a value between 1 and 4000");
          errors++;
        }
        if (precision != 0) {
          logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Data type '" + dataType + "' does not allow a value for precision ("
              + precision + ")");
          errors++;
        }
      }
      default -> {
        logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Unknown data type '" + dataType + "'");
        errors++;
      }
      }

      if ("VARCHAR".equals(dataType)) {
        genVarcharColumnNames.add(columnName);
      }

      tableColumnNames.add(columnName);
    }

    genTableNameColumnNames.put(tableName,
                                tableColumnNames);

    if (isDebug) {
      logger.debug("End");
    }

    return columnNames;
  }

  /**
   * Validate foreign key hierarchy.
   */
  private void validateSchemaForeignKeys() {
    if (isDebug) {
      logger.debug("Start");
    }

    while (valTableNameForeignKeys.size() > 0) {
      List<String> currentLevel = new ArrayList<>();

      for (String tableName : genTableNames) {
        if (valTableNameForeignKeys.containsKey(tableName)) {
          if (valTableNameForeignKeys.get(tableName).size() == 0) {
            currentLevel.add(tableName);
            genTableHierarchy.add(tableName);
            valTableNameForeignKeys.remove(tableName);
          }
        }
      }

      if (currentLevel.size() == 0) {
        logger.error("Foreign key hierarchy is wrong");
        errors++;
        break;
      }

      for (String referenceTable : currentLevel) {
        for (String tableName : genTableNames) {
          HashSet<String> referenceTables = valTableNameForeignKeys.get(tableName);
          if (referenceTables != null) {
            if (referenceTables.contains(referenceTable)) {
              referenceTables.remove(referenceTable);
              valTableNameForeignKeys.put(tableName,
                                          referenceTables);
            }
          }
        }
      }
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Validate table constraint definitions.
   */
  private void validateSchemaTableConstraints() {
    if (isDebug) {
      logger.debug("Start");
    }

    List<String>          columns;
    String                constraintType;

    List<String>          referenceColumns;
    String                referenceTable;
    HashSet<String>       valRefrenceColumnNames;

    HashSet<String>       valColumnNames;
    List<TableConstraint> tableConstraints;
    String                tableName;

    for (Table table : valTables) {
      tableName        = table.getTableName().toUpperCase();

      tableConstraints = table.getTableConstraints();

      if (tableConstraints == null || tableConstraints.size() == 0) {
        continue;
      }

      for (TableConstraint tableConstraint : tableConstraints) {
        if (tableConstraint.getConstraintType() == null) {
          logger.error("Database table: '" + tableName + "' - Data type is missing (null)");
          errors++;
          continue;
        }

        constraintType = tableConstraint.getConstraintType();

        if (constraintType != null) {
          constraintType = constraintType.toUpperCase();
        }

        columns = tableConstraint.getColumns();

        if (columns == null || columns.size() == 0) {
          logger.error("Database table: '" + tableName + "' - Columns missing (null)");
          errors++;
          continue;
        }

        valColumnNames = valTablesColumns.get(tableName);

        for (String column : columns) {
          if (!valColumnNames.contains(column.toUpperCase())) {
            logger.error("Database table: '" + tableName + "' constraint type: '" + constraintType + "' - Column '" + column.toUpperCase()
                + "' is not defined in database table '" + tableName + "'");
            errors++;
          }
        }

        referenceColumns = tableConstraint.getReferenceColumns();
        referenceTable   = tableConstraint.getReferenceTable();

        if (referenceTable != null) {
          referenceTable = referenceTable.toUpperCase();
        }

        switch (constraintType) {
        case "FOREIGN" -> {
          if (columns.size() == 1) {
            logger.error("Database table: '" + tableName + "' constraint type '" + constraintType
                + "' - With a single rcolumn, a constraint must be defined as a column constraint");
            errors++;
          }

          if (referenceTable == null) {
            logger.error("Database table: '" + tableName + "' - Constraint type '" + constraintType + "' requires a reference table");
            errors++;
            continue;
          }

          if (tableName.equals(referenceTable)) {
            logger.error("Database table: '" + tableName + "' constraint type: '" + constraintType + "' - Reference table must be a different database table");
            errors++;
          }

          if (!(valTablesColumns.containsKey(referenceTable))) {
            logger.error("Database table: '" + tableName + "' constraint type: '" + constraintType + "' - Reference table '" + referenceTable
                + "' is not defined");
            errors++;
          }

          if (referenceColumns == null || referenceColumns.size() == 0) {
            logger.error("Database table: '" + tableName + "' - Constraint type '" + constraintType + "' requires reference column(s)");
            errors++;
            continue;
          }

          valRefrenceColumnNames = valTablesColumns.get(referenceTable);

          for (String referenceColumn : referenceColumns) {
            if (!valRefrenceColumnNames.contains(referenceColumn.toUpperCase())) {
              logger.error("Database table: '" + tableName + "' constraint type: '" + constraintType + "' - Reference column '" + referenceColumn.toUpperCase()
                  + "' is not defined in database table '" + referenceTable + "'");
              errors++;
            }
          }

          if (errors == 0) {
            HashSet<String> referenceTables = valTableNameForeignKeys.get(tableName);
            referenceTables.add(referenceTable);
            valTableNameForeignKeys.put(tableName,
                                        referenceTables);
          }
        }
        case "PRIMARY", "UNIQUE" -> {
          if (columns.size() == 1) {
            logger.error("Database table: '" + tableName + "' constraint type '" + constraintType
                + "' - With a single rcolumn, a constraint must be defined as a column constraint");
            errors++;
          }

          if (referenceTable != null) {
            logger.error("Database table: '" + tableName + "' - Constraint type '" + constraintType + "' does not allow a reference table");
            errors++;
          }

          if (referenceColumns != null && referenceColumns.size() > 0) {
            logger.error("Database table: '" + tableName + "' - Constraint type '" + constraintType + "' does not allow reference column(s)");
            errors++;
            continue;
          }
        }
        default -> {
          logger.error("Database table: '" + tableName + "' - Unknown constraint type '" + constraintType + "'");
          errors++;
        }
        }
      }
    }

    if (isDebug) {
      logger.debug("End");
    }
  }
}
