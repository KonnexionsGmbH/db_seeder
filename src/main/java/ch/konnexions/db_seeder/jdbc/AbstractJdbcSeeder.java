package ch.konnexions.db_seeder.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.AbstractDbmsSeeder;
import ch.konnexions.db_seeder.utils.Config;
import ch.konnexions.db_seeder.utils.MessageHandling;
import ch.konnexions.db_seeder.utils.Statistics;

/**
 * Data Generator for a Database - Abstract JDBC Seeder.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public abstract class AbstractJdbcSeeder extends AbstractJdbcSchema {

  private static final int    ENCODING_MAX     = 3;

  private static final Logger logger           = LogManager.getLogger(AbstractJdbcSeeder.class);

  private static final int    XLOB_OMNISCI_MAX = 32767 / 2;

  /**
   * Gets the catalog name.
   *
   * @param tickerSymbolIntern the internal DBMS ticker symbol
   * @return the catalog name
   */
  public static String getCatalogName(String tickerSymbolIntern) {
    return "db_seeder_" + tickerSymbolIntern;
  }

  /**
   * Gets the trino URL string.
   *
   * @param tickerSymbolIntern the internal DBMS ticker symbol
   * @param connectionHost    the connection host name
   * @param connectionPort    the connection port
   * @param databaseSchema    the database schema
   * @return the trino URL string
   */
  public static String getUrlTrino(String tickerSymbolIntern, String connectionHost, int connectionPort, String databaseSchema) {
    return "jdbc:trino://" + connectionHost + ":" + connectionPort + "/" + getCatalogName(tickerSymbolIntern) + "/" + databaseSchema + "?user=trino";
  }

  private final String            BLOB_FILE              = Paths.get("src",
                                                                     "main",
                                                                     "resources").toAbsolutePath() + File.separator + "blob.png";
  private final byte[]            BLOB_DATA_BYTES        = readBlobFile2Bytes();
  private final String            BLOB_DATA_BYTES_STRING = new String(readBlobFile2Bytes(), StandardCharsets.UTF_8);

  private final String            CLOB_FILE              = Paths.get("src",
                                                                     "main",
                                                                     "resources").toAbsolutePath() + File.separator + "clob.md";
  private final String            CLOB_DATA              = readClobFile();

  protected Connection            connection             = null;
  private Map<String, Constraint> constraints            = new LinkedHashMap<>();

  protected String                driver                 = "";
  protected final String          driver_trino           = "io.trino.jdbc.TrinoDriver";
  protected String                dropTableStmnt         = "";

  protected Properties            encodedColumnNames     = new Properties();

  private Map<String, String>     informixConstraintNames;
  protected final boolean         isClient;
  private final boolean           isDebug                = logger.isDebugEnabled();
  protected final boolean         isTrino;

  protected int                   nullFactor;

  private final Random            randomInt              = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
  protected ResultSet             resultSet              = null;

  protected Statement             statement              = null;

  private final String            tickerSymbolExtern;

  protected String                urlSys                 = "";
  protected String                urlTrino               = "";
  protected String                urlUser                = "";

  /**
   * Initialises a new abstract JDBC seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public AbstractJdbcSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    this.tickerSymbolExtern = tickerSymbolExtern;
    logger.info("tickerSymbolExtern =" + tickerSymbolExtern);

    config          = new Config();

    batchSize       = config.getBatchSize();
    dropConstraints = config.getDropConstraints();

    boolean isEmbedded;

    if ("embedded".equals(dbmsOption)) {
      isClient   = false;
      isEmbedded = true;
      isTrino    = false;
    } else if ("trino".equals(dbmsOption)) {
      isClient   = false;
      isEmbedded = false;
      isTrino    = true;
    } else {
      isClient   = true;
      isEmbedded = false;
      isTrino    = false;
    }

    if (isDebug) {
      logger.debug("client  =" + isClient);
      logger.debug("embedded=" + isEmbedded);
      logger.debug("trino  =" + isTrino);

      logger.debug("End   Constructor");
    }
  }

  /**
   * Commit the previous DDL operations.
   *
   * @param connection the database connection
   */
  private void commitDDL(Connection connection) {
    if (isDebug) {
      logger.debug("Start");
    }

    if (!("cratedb".equals(tickerSymbolIntern) || "firebird".equals(tickerSymbolIntern) || "oracle".equals(tickerSymbolIntern))) {
      try {
        if (!(connection.getAutoCommit())) {
          connection.commit();
        }
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Commit the previous DML operations.
   *
   * @param connection the database connection
   */
  private void commitDML(Connection connection) {
    if (isDebug) {
      logger.debug("Start");
    }

    if (!("cratedb".equals(tickerSymbolIntern))) {
      try {
        if (!(connection.getAutoCommit())) {
          connection.commit();
        }
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create a database connection.
   *
   * @param url the URL
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
   * @param url        the URL
   * @param autoCommit the auto commit option
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
   * @param url    the URL
   * @param driver the database driver
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
   * @param url        the URL
   * @param driver     the database driver
   * @param autoCommit the auto commit option
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
   * @param url      the URL
   * @param driver   the database driver
   * @param user     the user name
   * @param password the password
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
   * @param urlIn      the URL
   * @param driver     the database driver
   * @param user       the user name
   * @param password   the password
   * @param autoCommit the auto commit option
   * @return the database connection
   */
  protected final Connection connect(String urlIn, String driver, String user, String password, boolean autoCommit) {
    if (isDebug) {
      logger.debug("Start");
    }

    if (driver != null) {
      try {
        if (isDebug) {
          logger.debug("driver='" + driver + "'");
        }

        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    Connection connection = null;

    String     url        = urlIn.replace("\"",
                                          "");

    try {
      if (isDebug) {
        logger.debug("url='" + url + "'");
      }

      if (user == null && password == null) {
        connection = DriverManager.getConnection(url);
      } else {
        if (isDebug) {
          logger.debug("user='" + user + "' password='" + password + "'");
        }
        connection = DriverManager.getConnection(url,
                                                 user,
                                                 password);
      }

      connection.setAutoCommit(autoCommit);

      if (isDebug) {
        logger.debug("auto=" + connection.getAutoCommit());
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End   [" + connection + "]");
    }

    return connection;
  }

  private int countData(String tableName) {
    if (isDebug) {
      logger.debug("Start");
    }

    int count = 0;

    try {
      statement = connection.createStatement();

      String sqlStmnt = "SELECT COUNT(*) FROM " + identifierDelimiter + tableName + identifierDelimiter;

      executeSQLStmnt(sqlStmnt);

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
      logger.debug("End");
    }

    return count;
  }

  /**
   * Create the column names with encoding variations.
   *
   * @param isEncodingIso_8859_1 the is encoding ISO_8859_1 8859 1
   * @param isEncodingUtf_8      the is encoding UTF_8 required
   */
  protected abstract void createColumnNames(boolean isEncodingIso_8859_1, boolean isEncodingUtf_8);

  /**
   * Create the data for all database valTableNames.
   */
  public final void createData() {
    if (isDebug) {
      logger.debug("Start");
    }

    Statistics statistics = new Statistics(config, tickerSymbolExtern, dbmsDetails);

    setupDatabase();

    // Drop the constraints of type FOREIGN KEY, PRIMARY KEY and UNIQUE KEY
    if ("yes".equals(dropConstraints)) {
      LocalDateTime startDateTime = LocalDateTime.now();

      constraints = new LinkedHashMap<>();

      if ("cockroach".equals(tickerSymbolIntern)) {
        try {
          connection.commit();
        } catch (SQLException e) {
          e.printStackTrace();
          System.exit(1);
        }
      }

      dropTableConstraints(connection);

      long duration = Duration.between(startDateTime,
                                       LocalDateTime.now()).toMillis();

      statistics.setDurationDDLConstraintsDrop(duration);

      logger.info(String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                duration) + " ms - total DDL constraints (FK, PK, UK) dropped");
    }

    statistics.setStartDateTimeDML();

    // Perform the DML statements
    for (String tableName : TABLE_NAMES_CREATE) {
      LocalDateTime startDateTime = LocalDateTime.now();

      createData(tableName);

      long duration = Duration.between(startDateTime,
                                       LocalDateTime.now()).toMillis();

      logger.info(String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                duration) + " ms - total DML database table " + tableName);
    }

    statistics.setDurationDML();

    // Restore the constraints of type FOREIGN KEY, PRIMARY KEY and UNIQUE KEY
    if ("yes".equals(dropConstraints)) {
      LocalDateTime startDateTime = LocalDateTime.now();

      if ("cockroach".equals(tickerSymbolIntern)) {
        try {
          connection.commit();
        } catch (SQLException e) {
          e.printStackTrace();
          System.exit(1);
        }
      }

      restoreTableConstraints(connection);

      long duration = Duration.between(startDateTime,
                                       LocalDateTime.now()).toMillis();

      statistics.setDurationDDLConstraintsAdd(duration);

      logger.info(String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                duration) + " ms - total DDL constraints (FK, PK, UK) restored and enabled");
    }

    disconnectDML(connection);

    statistics.createMeasuringEntry();

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void createData(String tableName) {
    int rowMaxSize = getMaxRowSize(tableName);

    if (isDebug) {
      logger.debug("Start - database table " + String.format(FORMAT_TABLE_NAME,
                                                             tableName) + " - " + String.format(FORMAT_ROW_NO,
                                                                                                rowMaxSize) + " rows to be created");
    }

    String    editedTableName = setCaseIdentifier(tableName);

    final int countExisting   = countData(editedTableName);

    if (countExisting != 0) {
      if (isDebug) {
        logger.debug("found existing data in database table " + String.format(FORMAT_TABLE_NAME,
                                                                              tableName));
      }

      return;
    }

    ArrayList<Object> pkList = new ArrayList<>();

    createDataInsert(tableName,
                     rowMaxSize,
                     pkList);

    commitDML(connection);

    pkLists.put(tableName,
                pkList);
    pkListSizes.put(tableName,
                    pkList.size());

    validateNumberRows(editedTableName,
                       rowMaxSize);

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void createDataInsert(String tableName, int rowMaxSize, ArrayList<Object> pkList) {
    if (isDebug) {
      logger.debug("Start");
    }

    String       editedTableName   = setCaseIdentifier(tableName);
    int          msgDivisor        = rowMaxSize > 500 * 10
        ? rowMaxSize / 10
        : 500;

    String       insertTemplate    = dmlStatements.get(tableName);
    int          insertTemplateFix = insertTemplate.indexOf(") VALUES (");

    final String sqlStmnt          = "INSERT INTO " + identifierDelimiter + editedTableName + identifierDelimiter + " (" + setCaseIdentifier(insertTemplate
        .substring(0,
                   insertTemplateFix)) + insertTemplate.substring(insertTemplateFix) + ")";

    if (isDebug) {
      logger.debug("sql='" + sqlStmnt + "'");
    }

    if (dbmsEnum == DbmsEnum.MONETDB) {
      try {
        statement = connection.createStatement();

        executeSQLStmnts(statement,
                         "SET sys.optimizer='minimal_pipe';");

        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(sqlStmnt);

    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    boolean isToBeExecuted = false;

    for (long rowNo = 1; rowNo <= rowMaxSize; rowNo++) {

      if (rowNo % msgDivisor == 0) {
        logger.info("database table " + String.format(FORMAT_TABLE_NAME,
                                                      tableName.toUpperCase()) + " - " + String.format(FORMAT_ROW_NO + " rows so far",
                                                                                                       rowNo));
      }

      insertTable(preparedStatement,
                  tableName,
                  rowNo);

      try {
        preparedStatement.addBatch();

        if ((batchSize > 0) && (rowNo % batchSize == 0)) {
          preparedStatement.executeBatch();

          isToBeExecuted = false;
          logger.info("Batch processing finished - number of SQL statements: " + batchSize);
        } else {
          isToBeExecuted = true;
        }

        pkList.add(rowNo);
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      if (isToBeExecuted) {
        preparedStatement.executeBatch();
      }

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (dbmsEnum == DbmsEnum.CRATEDB) {
      try {
        statement = connection.createStatement();

        executeSQLStmnts(statement,
                         "REFRESH TABLE " + editedTableName);

        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create the DDL statement: CREATE TABLE.
   *
   * @param tableName the database table name
   * @return the 'CREATE TABLE' statement
   */
  protected abstract String createDdlStmnt(String tableName);

  /**
   * Create the all database tables.
   *
   * @param connection the database connection
   */
  protected final void createSchema(Connection connection) {
    if (isDebug) {
      logger.debug("Start");
    }

    for (String tableName : TABLE_NAMES_CREATE) {
      try {
        statement = connection.createStatement();

        String sqlStmnt = createDdlStmnt(tableName);

        if (isDebug) {
          logger.debug("next SQL statement=" + sqlStmnt);
        }

        executeSQLStmnts(statement,
                         sqlStmnt);

        statement.close();

        commitDDL(connection);
      } catch (SQLException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Close the database connection after DDL operations.
   *
   * @param connection the database connection
   */
  protected final void disconnectDDL(Connection connection) {
    if (isDebug) {
      logger.debug("Start [" + connection.toString() + "]");
    }

    try {
      if (!(connection.getAutoCommit())) {
        commitDDL(connection);
      }

      connection.close();

    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Close the database connection after DML operations.
   *
   * @param connection the database connection
   */
  private void disconnectDML(Connection connection) {
    if (isDebug) {
      logger.debug("Start [" + connection.toString() + "]");
    }

    try {
      if (!(connection.getAutoCommit())) {
        commitDML(connection);
      }

      connection.close();

    } catch (SQLException ec) {
      ec.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Drop all valTableNames based on q metadata query.
   *
   * @param sqlStmnt the SQL statement
   */
  protected final void dropAllTables(String sqlStmnt) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      for (String tableName : TABLE_NAMES_DROP) {
        String queryStmnt = sqlStmnt.replace("?",
                                             dbmsEnum == DbmsEnum.CRATEDB || dbmsEnum == DbmsEnum.OMNISCI

                                                 ? tableName.toLowerCase()
                                                 : tableName.toUpperCase());

        executeSQLStmnt(queryStmnt);

        if (resultSet.next()) {
          String dropStmnt = resultSet.getString(1);

          if (isDebug) {
            logger.debug("next SQL statement=" + dropStmnt);
          }

          executeSQLStmnts(statement,
                           dropStmnt);
        }

        resultSet.close();
      }

    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Drop all existing valTableNames.
   */
  protected final void dropAllTablesIfExists() {
    if (isDebug) {
      logger.debug("Start");
    }

    for (String tableName : TABLE_NAMES_DROP) {
      String sqlStmnt = "DROP TABLE" + (dbmsEnum == DbmsEnum.VOLTDB
          ? " " + identifierDelimiter + tableName + identifierDelimiter + " "
          : " ") + "IF EXISTS" + (dbmsEnum == DbmsEnum.VOLTDB
              ? ""
              : " " + identifierDelimiter + tableName + identifierDelimiter);

      if (isDebug) {
        logger.debug("next SQL statement=" + sqlStmnt);
      }

      executeSQLStmnts(statement,
                       sqlStmnt);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Drop the database.
   *
   * @param databaseName    the database name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName       the table name
   * @param columnName      the column name
   */
  protected final void dropDatabase(String databaseName, String cascadeRestrict, String tableName, String columnName) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      int    count    = 0;

      String sqlStmnt = "SELECT count(*) FROM " + tableName + " WHERE " + columnName + " = '" + databaseName + "'";

      executeSQLStmnt(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        sqlStmnt = "DROP " + (dbmsEnum == DbmsEnum.MIMER
            ? "DATABANK"
            : "DATABASE") + " " + databaseName + (cascadeRestrict != null
                ? " " + cascadeRestrict
                : "");

        if (isDebug) {
          logger.debug("next SQL statement=" + sqlStmnt);
        }

        executeSQLStmnts(statement,
                         sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Drop the database schema.
   *
   * @param schemaName      the schema name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName       the table name
   * @param columnName      the column name
   */
  protected final void dropSchema(String schemaName, String cascadeRestrict, String tableName, String columnName) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      int    count    = 0;

      String sqlStmnt = "SELECT count(*) FROM " + tableName + " WHERE " + columnName + " = '" + schemaName + "'";

      executeSQLStmnt(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        sqlStmnt = "DROP SCHEMA " + schemaName + (cascadeRestrict != null
            ? " " + cascadeRestrict
            : "");

        if (isDebug) {
          logger.debug("next SQL statement=" + sqlStmnt);
        }

        executeSQLStmnts(statement,
                         sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Drops the following constraint types in a database table: FOREIGN KEY, PRIMARY KEY and UNIQUE KEY.
   *
   * @param connection the database connection
   */
  private void dropTableConstraints(Connection connection) {
    if (isDebug) {
      logger.debug("Start");
    }

    String                   catalog      = null;
    TreeMap<Integer, String> columns      = new TreeMap<>();
    String                   constraintName;
    String                   constraintNamePrev;

    DatabaseMetaData         dbMetaData   = null;

    TreeMap<Integer, String> refColumns   = new TreeMap<>();
    String                   refTableName = "";

    String                   schema       = null;
    String                   table;

    switch (tickerSymbolIntern) {
    case "agens":
    case "postgresql":
    case "sqlserver":
    case "timescale":
    case "yugabyte":
      catalog = config.getDatabase();
      schema = config.getSchema();
      break;
    case "cockroach":
    case "cubrid":
    case "firebird":
    case "mariadb":
    case "mimer":
    case "mysql":
      catalog = config.getDatabase();
      break;
    case "hsqldb":
      if ("hsqldb".equals(tickerSymbolExtern)) {
        schema = config.getSchema().toUpperCase();
      }

      break;
    case "ibmdb2":
      schema = config.getSchema().toUpperCase();
      break;
    case "informix":
      getInformixConstraintNames();
      catalog = config.getDatabase();
      break;
    case "monetdb":
      schema = config.getSchema();
      break;
    case "percona":
      schema = config.getDatabase();
      break;
    default:
    }

    try {
      dbMetaData = connection.getMetaData();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // =========================================================================
    // FOREIGN KEY constraints
    // =========================================================================

    try {
      for (String tableName : TABLE_NAMES_CREATE) {
        table = setCaseIdentifierMetData(tableName);

        if (isDebug) {
          logger.debug("getImportedKeys(" + catalog + "," + schema + "," + table + ")");
        }

        resultSet          = dbMetaData.getImportedKeys(catalog,
                                                        schema,
                                                        table);

        constraintNamePrev = "";

        while (resultSet.next()) {
          if (isDebug) {
            logger.debug("getImportedKeys(" + catalog + "," + schema + "," + table + ")");
            logger.debug("PKTABLE_CAT     =" + resultSet.getString("PKTABLE_CAT"));
            logger.debug("PKTABLE_SCHEM   =" + resultSet.getString("PKTABLE_SCHEM"));
            logger.debug("PKTABLE_NAME    =" + resultSet.getString("PKTABLE_NAME"));
            logger.debug("PKCOLUMN_NAME   =" + resultSet.getString("PKCOLUMN_NAME"));
            logger.debug("FKTABLE_CAT     =" + resultSet.getString("FKTABLE_CAT"));
            logger.debug("FKTABLE_SCHEM   =" + resultSet.getString("FKTABLE_SCHEM"));
            logger.debug("FKTABLE_NAME    =" + resultSet.getString("FKTABLE_NAME"));
            logger.debug("FKCOLUMN_NAME   =" + resultSet.getString("FKCOLUMN_NAME"));
            logger.debug("KEY_SEQ         =" + resultSet.getInt("KEY_SEQ"));
            logger.debug("UPDATE_RULE     =" + resultSet.getInt("UPDATE_RULE"));
            logger.debug("DELETE_RULE     =" + resultSet.getInt("DELETE_RULE"));
            logger.debug("FK_NAME         =" + resultSet.getString("FK_NAME"));
            logger.debug("PK_NAME         =" + resultSet.getString("PK_NAME"));
            logger.debug("DEFERRABILITY   =" + resultSet.getInt("DEFERRABILITY"));
          }

          constraintName = resultSet.getString("FK_NAME");

          // First foreign key
          if ("".equals(constraintNamePrev)) {
            columns            = new TreeMap<>();
            constraintNamePrev = constraintName;
            refColumns         = new TreeMap<>();
            refTableName       = resultSet.getString("PKTABLE_NAME");
            // New foreign key
          } else if (!(constraintNamePrev.equals(constraintName))) {
            storeConstraint(schema,
                            table,
                            constraintNamePrev,
                            "R",
                            columns,
                            refTableName,
                            refColumns);

            columns            = new TreeMap<>();
            constraintNamePrev = constraintName;
            refColumns         = new TreeMap<>();
            refTableName       = resultSet.getString("PKTABLE_NAME");
          }

          columns.put(resultSet.getInt("KEY_SEQ"),
                      resultSet.getString("FKCOLUMN_NAME"));
          refColumns.put(resultSet.getInt("KEY_SEQ"),
                         resultSet.getString("PKCOLUMN_NAME"));
        }

        // Last foreign key
        if (!("".equals(constraintNamePrev))) {
          storeConstraint(schema,
                          table,
                          constraintNamePrev,
                          "R",
                          columns,
                          refTableName,
                          refColumns);
        }
      }

      resultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // =========================================================================
    // PRIMARY KEY constraints
    // =========================================================================

    try {
      for (String tableName : TABLE_NAMES_CREATE) {
        table = setCaseIdentifierMetData(tableName);

        if (isDebug) {
          logger.debug("getPrimaryKeys(" + catalog + "," + schema + "," + table + ")");
        }

        resultSet      = dbMetaData.getPrimaryKeys(catalog,
                                                   schema,
                                                   table);

        constraintName = "";

        while (resultSet.next()) {
          if (isDebug) {
            logger.debug("getPrimaryKeys(" + catalog + "," + schema + "," + table + ")");
            logger.debug("TABLE_CAT       =" + resultSet.getString("TABLE_CAT"));
            logger.debug("TABLE_SCHEM     =" + resultSet.getString("TABLE_SCHEM"));
            logger.debug("TABLE_NAME      =" + resultSet.getString("TABLE_NAME"));
            logger.debug("COLUMN_NAME     =" + resultSet.getString("COLUMN_NAME"));
            logger.debug("KEY_SEQ         =" + resultSet.getInt("KEY_SEQ"));
            logger.debug("PK_NAME         =" + resultSet.getString("PK_NAME"));
          }

          // First primary key column
          if ("".equals(constraintName)) {
            columns        = new TreeMap<>();
            constraintName = resultSet.getString("PK_NAME");
          }

          columns.put(resultSet.getInt("KEY_SEQ"),
                      resultSet.getString("COLUMN_NAME"));
        }

        if (!("".equals(constraintName))) {
          storeConstraint(schema,
                          table,
                          constraintName,
                          "P",
                          columns,
                          null,
                          null);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // =========================================================================
    // UNIQUE KEY constraints
    // =========================================================================

    try {
      for (String tableName : TABLE_NAMES_CREATE) {
        table = setCaseIdentifierMetData(tableName);

        if (isDebug) {
          logger.debug("getIndexInfo(" + catalog + "," + schema + "," + table + ",false,true)");
        }

        resultSet          = dbMetaData.getIndexInfo(catalog,
                                                     schema,
                                                     table,
                                                     false,
                                                     true);

        constraintNamePrev = "";

        while (resultSet.next()) {
          String columnName = resultSet.getString("COLUMN_NAME");
          constraintName = resultSet.getString("INDEX_NAME");

          if (isDebug) {
            logger.debug("getIndexInfo(" + catalog + "," + schema + "," + table + ",false,true)");
            logger.debug("TABLE_CAT       =" + resultSet.getString("TABLE_CAT"));
            logger.debug("TABLE_SCHEM     =" + resultSet.getString("TABLE_SCHEM"));
            logger.debug("TABLE_NAME      =" + resultSet.getString("TABLE_NAME"));
            logger.debug("NON_UNIQUE      =" + resultSet.getBoolean("NON_UNIQUE"));
            logger.debug("INDEX_QUALIFIER =" + resultSet.getString("INDEX_QUALIFIER"));
            logger.debug("INDEX_NAME      =" + constraintName);
            logger.debug("TYPE            =" + resultSet.getInt("TYPE"));
            logger.debug("ORDINAL_POSITION=" + resultSet.getInt("ORDINAL_POSITION"));
            logger.debug("COLUMN_NAME     =" + columnName);
            logger.debug("ASC_OR_DESC     =" + resultSet.getString("ASC_OR_DESC"));
            logger.debug("CARDINALITY     =" + resultSet.getInt("CARDINALITY"));
            logger.debug("PAGES           =" + resultSet.getInt("PAGES"));
            logger.debug("FILTER_CONDITION=" + resultSet.getString("FILTER_CONDITION"));
          }

          // Irrelevant entries
          if (constraintName == null) {
            continue;
          }

          if (!("informix".equals(tickerSymbolIntern))) {
            if (!(constraintName.contains("KXN_"))) {
              continue;
            }
          }

          // First unique key
          if ("".equals(constraintNamePrev)) {
            columns            = new TreeMap<>();
            constraintNamePrev = constraintName;
            // New unique key
          } else if (!(constraintNamePrev.equals(constraintName))) {
            storeConstraint(schema,
                            table,
                            constraintNamePrev,
                            "U",
                            columns,
                            null,
                            null);

            columns            = new TreeMap<>();
            constraintNamePrev = constraintName;
          }

          columns.put(resultSet.getInt("ORDINAL_POSITION"),
                      columnName);
        }

        // Last unique key
        if (!("".equals(constraintNamePrev))) {
          storeConstraint(schema,
                          table,
                          constraintNamePrev,
                          "U",
                          columns,
                          null,
                          null);
        }
      }

      resultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // =========================================================================
    // Drop the constraints found 
    // =========================================================================

    if (constraints.isEmpty()) {
      MessageHandling.abortProgram(logger,
                                   "Program abort: no constraints found to be dropped");
    }

    try {
      if ("mimer".equals(tickerSymbolIntern)) {
        commitDML(connection);
      }

      statement = connection.createStatement();

      for (Constraint constraint : constraints.values()) {
        String dropConstraint = constraint.getDropConstraintStatement();
        if (!("NONE".equals(dropConstraint))) {
          executeSQLStmnts(statement,
                           dropConstraint);
        }
      }

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Drop the database user.
   *
   * @param userName        the user name
   * @param cascadeRestrict "CASCADE" or "RESTRICT"
   * @param tableName       the table name
   * @param columnName      the column name
   */
  protected final void dropUser(String userName, String cascadeRestrict, String tableName, String columnName) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      int    count    = 0;

      String sqlStmnt = "SELECT count(*) FROM " + tableName + " WHERE " + columnName + " = '" + userName + "'";

      executeSQLStmnt(sqlStmnt);

      while (resultSet.next()) {
        count = resultSet.getInt(1);
      }

      resultSet.close();

      if (count > 0) {
        sqlStmnt = "DROP " + (dbmsEnum == DbmsEnum.MIMER
            ? "IDENT"
            : "USER") + "  " + userName + (cascadeRestrict != null
                ? " " + cascadeRestrict
                : "");

        if (isDebug) {
          logger.debug("next SQL statement=" + sqlStmnt);
        }

        executeSQLStmnts(statement,
                         sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void executeSQLStmnt(String sqlStmnt) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      if (isDebug) {
        logger.debug("next SQL statement=" + sqlStmnt);
      }

      resultSet = statement.executeQuery(sqlStmnt);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Execute SQL statement optional.
   *
   * @param statement          the statement object
   * @param sqlStmnt           the SQL statement
   */
  protected final void executeSQLStmntOptional(Statement statement, String sqlStmnt) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      if (isDebug) {
        logger.debug("next SQL statement=" + sqlStmnt);
      }

      statement.execute(sqlStmnt);

    } catch (SQLException e) {
      logger.info("SQL statement='" + sqlStmnt + "' not executed");
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Execute SQL statements.
   *
   * @param statement          the statement object
   * @param firstSQLStmnt      the first SQL statement
   * @param remainingSQLStmnts the remaining SQL statements
   */
  protected final void executeSQLStmnts(Statement statement, String firstSQLStmnt, String... remainingSQLStmnts) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      if (isDebug) {
        logger.debug("next SQL statement=" + firstSQLStmnt);
      }

      statement.execute(firstSQLStmnt);

      for (String sqlStmnt : remainingSQLStmnts) {

        if (isDebug) {
          logger.debug("next SQL statement=" + sqlStmnt);
        }

        statement.execute(sqlStmnt);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  protected long getContentBigint(String tableName,
                                  String columnName,
                                  long rowNo,
                                  Integer defaultValue,
                                  Integer lowerRange,
                                  Integer upperRange,
                                  List<Integer> validValues) {
    if (validValues != null) {
      return validValues.get(randomInt.nextInt(validValues.size()));
    } else if (lowerRange != null) {
      return randomInt.nextInt(upperRange - lowerRange + 1) + lowerRange - 1;
    }

    return rowNo;
  }

  protected byte[] getContentBlob(String tableName, String columnName, long rowNo) {

    return BLOB_DATA_BYTES;
  }

  private String getContentBlobString(String tableName, String columnName, long rowNo) {

    return BLOB_DATA_BYTES_STRING;
  }

  protected String getContentClob(String tableName, String columnName, long rowNo) {

    return CLOB_DATA;
  }

  private Object getContentFk(String tableName, String columnName, long rowNo, ArrayList<Object> fkList) {
    return fkList.get(randomInt.nextInt(fkList.size()));
  }

  private int getContentFkInt(String tableName, String columnName, long rowNo, ArrayList<Object> fkList) {
    return randomInt.nextInt(fkList.size());
  }

  protected Timestamp getContentTimestamp(String tableName, String columnName, long rowNo) {

    return new java.sql.Timestamp(System.currentTimeMillis() + randomInt.nextInt(2147483647));
  }

  protected String getContentVarchar(String tableName,
                                     String columnName,
                                     long rowNo,
                                     int size,
                                     String defaultValue,
                                     String lowerRange,
                                     String upperRange,
                                     List<String> validValues) {
    if (isDebug) {
      logger.debug("Start");
    }

    String columnValue;

    if (validValues != null) {
      return validValues.get(randomInt.nextInt(validValues.size())).stripTrailing();
    } else if (lowerRange != null) {
      columnValue = RandomStringUtils.randomGraph(1,
                                                  size + 1);

      if (columnValue.compareTo(lowerRange) < 0) {
        return lowerRange;
      }

      if (columnValue.compareTo(upperRange) > 0) {
        return upperRange;
      }
    } else {
      columnValue = (columnName + "_" + encodedColumnNames.getProperty(columnName + "_" + rowNo % ENCODING_MAX) + String.format(FORMAT_IDENTIFIER,
                                                                                                                                rowNo)).stripTrailing();
      int columnValueSize = getLengthUTF_8(columnValue);
      if (columnValueSize > size)
        return columnValue.substring(columnValueSize - size + 1);
      else
        return columnValue;
    }

    if (isDebug) {
      logger.debug("End");
    }

    return columnValue;
  }

  private void getInformixConstraintNames() {
    if (isDebug) {
      logger.debug("Start");
    }

    informixConstraintNames = new LinkedHashMap<>();

    try {
      statement = connection.createStatement();

      String sqlStmnt = "SELECT idxname, constrname FROM  sysconstraints WHERE idxname IS NOT NULL";

      executeSQLStmnt(sqlStmnt);

      while (resultSet.next()) {
        informixConstraintNames.put(resultSet.getString(1).substring(1),
                                    resultSet.getString(2));
        if (isDebug) {
          logger.debug("Informix: IDXNAME=" + resultSet.getString(1).substring(1) + " CONSTRNAME=" + resultSet.getString(2));
        }
      }

      resultSet.close();

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private int getLengthUTF_8(String stringUTF_8) {
    int count = 0;

    for (int i = 0, len = stringUTF_8.length(); i < len; i++) {
      char ch = stringUTF_8.charAt(i);
      if (ch <= 0x7F) {
        count++;
      } else if (ch <= 0x7FF) {
        count += 2;
      } else if (Character.isHighSurrogate(ch)) {
        count += 4;
        ++i;
      } else {
        count += 3;
      }
    }

    return count;
  }

  private int getMaxRowSize(String tableName) {
    int maxRowSize   = maxRowSizes.get(tableName);

    int MAX_ROW_SIZE = Integer.MAX_VALUE;

    return Math.min(maxRowSize,
                    MAX_ROW_SIZE);
  }

  protected abstract void insertTable(PreparedStatement preparedStatement, String tableName, long rowNo);

  /**
   * Sets the designated column to a BIGINT value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   * @param defaultValue      the default value
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  protected void prepStmntColBigint(PreparedStatement preparedStatement,
                                    String tableName,
                                    String columnName,
                                    int columnPos,
                                    long rowNo,
                                    Integer defaultValue,
                                    Integer lowerRange,
                                    Integer upperRange,
                                    List<Integer> validValues) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      preparedStatement.setLong(columnPos,
                                getContentBigint(tableName,
                                                 columnName,
                                                 rowNo,
                                                 defaultValue,
                                                 lowerRange,
                                                 upperRange,
                                                 validValues));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  // ToDo 
  //  /**
  //   * Sets the designated optional column to a BIGINT value or to NULL.
  //   *
  //   * @param preparedStatement the prepared statement
  //   * @param tableName         the table name
  //   * @param columnName        the column name
  //   * @param columnPos         the column position
  //   * @param rowNo             the current row number
  //   * @param defaultValue      the lower value
  //   * @param lowerRange        the lower range
  //   * @param upperRange        the upper range
  //   * @param validValues       the valid values
  //   */
  //  protected final void prepStmntColBigintOpt(PreparedStatement preparedStatement,
  //                                             String tableName,
  //                                             String columnName,
  //                                             int columnPos,
  //                                             long rowNo,
  //                                             Integer defaultValue,
  //                                             Integer lowerRange,
  //                                             Integer upperRange,
  //                                             List<Integer> validValues) {
  //    try {
  //      if (rowNo % nullFactor == 0) {
  //        if (defaultValue == null) {
  //          preparedStatement.setNull(columnPos,
  //                                    java.sql.Types.INTEGER);
  //        } else {
  //          preparedStatement.setLong(columnPos,
  //                                    defaultValue);
  //        }
  //        return;
  //      }
  //
  //      prepStmntColBigint(preparedStatement,
  //                         tableName,
  //                         columnName,
  //                         columnPos,
  //                         rowNo,
  //                         defaultValue,
  //                         lowerRange,
  //                         upperRange,
  //                         validValues);
  //    } catch (SQLException e) {
  //      e.printStackTrace();
  //      System.exit(1);
  //    }
  //  }

  /**
   * Sets the designated column to a BLOB value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  private void prepStmntColBlob(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
    try {
      if (dbmsEnum == DbmsEnum.EXASOL) {
        preparedStatement.setString(columnPos,
                                    getContentBlobString(tableName,
                                                         columnName,
                                                         rowNo));
      } else if (dbmsEnum == DbmsEnum.OMNISCI) {
        String dataValue = getContentBlobString(tableName,
                                                columnName,
                                                rowNo);
        preparedStatement.setString(columnPos,
                                    dataValue.substring(0,
                                                        Math.min(dataValue.length(),
                                                                 XLOB_OMNISCI_MAX)));
      } else {
        preparedStatement.setBytes(columnPos,
                                   getContentBlob(tableName,
                                                  columnName,
                                                  rowNo));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional column to a BLOB value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  protected final void prepStmntColBlobOpt(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
    try {
      if (dbmsEnum == DbmsEnum.CRATEDB) {
        preparedStatement.setNull(columnPos,
                                  Types.NULL);
        return;
      }

      if (rowNo % nullFactor == 0) {
        if (dbmsEnum == DbmsEnum.EXASOL
            || dbmsEnum == DbmsEnum.POSTGRESQL
            || dbmsEnum == DbmsEnum.TIMESCALE
            || dbmsEnum == DbmsEnum.VOLTDB
            || dbmsEnum == DbmsEnum.YUGABYTE) {
          preparedStatement.setNull(columnPos,
                                    Types.NULL);
        } else {
          preparedStatement.setNull(columnPos,
                                    Types.BLOB);
        }
        return;
      }

      prepStmntColBlob(preparedStatement,
                       tableName,
                       columnName,
                       columnPos,
                       rowNo);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated column to a CLOB value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  private void prepStmntColClob(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
    try {
      if (dbmsEnum == DbmsEnum.OMNISCI) {
        String dataValue = getContentClob(tableName,
                                          columnName,
                                          rowNo);
        preparedStatement.setString(columnPos,
                                    dataValue.substring(0,
                                                        Math.min(dataValue.length(),
                                                                 XLOB_OMNISCI_MAX)));
      } else {
        preparedStatement.setString(columnPos,
                                    getContentClob(tableName,
                                                   columnName,
                                                   rowNo));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional column to a CLOB value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  protected final void prepStmntColClobOpt(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
    try {
      if (rowNo % nullFactor == 0) {
        if (dbmsEnum == DbmsEnum.CRATEDB || dbmsEnum == DbmsEnum.EXASOL || dbmsEnum == DbmsEnum.VOLTDB) {
          preparedStatement.setNull(columnPos,
                                    Types.VARCHAR);
        } else {
          preparedStatement.setNull(columnPos,
                                    java.sql.Types.CLOB);
        }
        return;
      }

      prepStmntColClob(preparedStatement,
                       tableName,
                       columnName,
                       columnPos,
                       rowNo);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated column to an existing foreign key value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   * @param fkList            the existing foreign keys
   */
  protected final void prepStmntColFk(PreparedStatement preparedStatement,
                                      String tableName,
                                      String columnName,
                                      int columnPos,
                                      long rowNo,
                                      ArrayList<Object> fkList) {

    try {
      if (dbmsEnum == DbmsEnum.OMNISCI) {
        preparedStatement.setLong(columnPos,
                                  getContentFkInt(tableName,
                                                  columnName,
                                                  rowNo,
                                                  fkList));
        return;
      }

      preparedStatement.setObject(columnPos,
                                  getContentFk(tableName,
                                               columnName,
                                               rowNo,
                                               fkList));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional column to an existing foreign key value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   * @param fkList            the existing foreign keys
   */
  protected final void prepStmntColFkOpt(PreparedStatement preparedStatement,
                                         String tableName,
                                         String columnName,
                                         int columnPos,
                                         long rowNo,
                                         ArrayList<Object> fkList) {
    try {
      if (rowNo % nullFactor == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.INTEGER);
        return;
      }

      prepStmntColFk(preparedStatement,
                     tableName,
                     columnName,
                     columnPos,
                     rowNo,
                     fkList);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated column to a TIMESTAMP value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  protected final void prepStmntColTimestamp(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
    try {
      preparedStatement.setTimestamp(columnPos,
                                     getContentTimestamp(tableName,
                                                         columnName,
                                                         rowNo));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated optional column to a TIMESTAMP value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   */
  protected final void prepStmntColTimestampOpt(PreparedStatement preparedStatement, String tableName, String columnName, int columnPos, long rowNo) {
    try {
      if (rowNo % nullFactor == 0) {
        preparedStatement.setNull(columnPos,
                                  java.sql.Types.TIMESTAMP);
        return;
      }

      prepStmntColTimestamp(preparedStatement,
                            tableName,
                            columnName,
                            columnPos,
                            rowNo);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the designated column to a VARCHAR value.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   * @param size              the column size
   * @param defaultValue      the default value
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  protected final void prepStmntColVarchar(PreparedStatement preparedStatement,
                                           String tableName,
                                           String columnName,
                                           int columnPos,
                                           long rowNo,
                                           int size,
                                           String defaultValue,
                                           String lowerRange,
                                           String upperRange,
                                           List<String> validValues) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      if (dbmsEnum == DbmsEnum.FIREBIRD || dbmsEnum == DbmsEnum.MARIADB || dbmsEnum == DbmsEnum.SQLSERVER || dbmsEnum == DbmsEnum.ORACLE) {
        preparedStatement.setNString(columnPos,
                                     getContentVarchar(tableName,
                                                       columnName,
                                                       rowNo,
                                                       size,
                                                       defaultValue,
                                                       lowerRange,
                                                       upperRange,
                                                       validValues));
        return;
      }

      preparedStatement.setString(columnPos,
                                  getContentVarchar(tableName,
                                                    columnName,
                                                    rowNo,
                                                    size,
                                                    defaultValue,
                                                    lowerRange,
                                                    upperRange,
                                                    validValues));
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Sets the designated optional column to a VARCHAR value or to NULL.
   *
   * @param preparedStatement the prepared statement
   * @param tableName         the table name
   * @param columnName        the column name
   * @param columnPos         the column position
   * @param rowNo             the current row number
   * @param size              the column size
   * @param defaultValue      the default value
   * @param lowerRange        the lower range
   * @param upperRange        the upper range
   * @param validValues       the valid values
   */
  protected final void prepStmntColVarcharOpt(PreparedStatement preparedStatement,
                                              String tableName,
                                              String columnName,
                                              int columnPos,
                                              long rowNo,
                                              int size,
                                              String defaultValue,
                                              String lowerRange,
                                              String upperRange,
                                              List<String> validValues) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      if (rowNo % nullFactor == 0) {
        if (defaultValue == null) {
          preparedStatement.setNull(columnPos,
                                    java.sql.Types.VARCHAR);
        } else {
          if (dbmsEnum == DbmsEnum.FIREBIRD || dbmsEnum == DbmsEnum.MARIADB || dbmsEnum == DbmsEnum.SQLSERVER || dbmsEnum == DbmsEnum.ORACLE) {
            preparedStatement.setNString(columnPos,
                                         defaultValue);
          } else {
            preparedStatement.setString(columnPos,
                                        defaultValue);
          }
        }
      } else {
        prepStmntColVarchar(preparedStatement,
                            tableName,
                            columnName,
                            columnPos,
                            rowNo,
                            size,
                            defaultValue,
                            lowerRange,
                            upperRange,
                            validValues);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private byte[] readBlobFile2Bytes() {
    if (isDebug) {
      logger.debug("Start");

      logger.debug("BLOB_FILE ='" + BLOB_FILE + "'");
    }

    File      file       = new File(BLOB_FILE);

    final int fileLength = (int) file.length();

    if (isDebug) {
      logger.debug("fileLength=" + fileLength);
    }

    byte[]          blobDataBytesArray = new byte[(int) file.length()];

    FileInputStream fileInputStream;

    try {
      fileInputStream = new FileInputStream(file);

      int size = fileInputStream.read(blobDataBytesArray);

      if (size == 0) {
        MessageHandling.abortProgram(logger,
                                     "Program abort: no BLOB data found");
      }

      fileInputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("byteLength=" + blobDataBytesArray.length);

      logger.debug("End");
    }

    return blobDataBytesArray;
  }

  private String readClobFile() {
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader(CLOB_FILE));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    StringBuilder clobData = new StringBuilder();
    String        nextLine;

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

  /**
   * Restores the previously dropped constraints for a database table..
   *
   * @param connection the database connection
   */
  private void restoreTableConstraints(Connection connection) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      if ("mimer".equals(tickerSymbolIntern)) {
        commitDML(connection);
      }

      statement = connection.createStatement();

      for (String tableName : TABLE_NAMES_CREATE) {
        for (Constraint constraint : constraints.values()) {
          if (tableName.equals(constraint.getTableName())) {
            String addConstraint = constraint.getAddConstraintStatement();
            if (!("NONE".equals(addConstraint))) {
              executeSQLStmnts(statement,
                               addConstraint);
            }
          }
        }
      }

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private String setCaseIdentifierMetData(String identifier) {
    return switch (tickerSymbolIntern) {
    case "cockroach", "cratedb", "monetdb", "postgresql", "timescale", "yugabyte" -> identifier.toLowerCase();
    case "exasol", "ibmdb2", "oracle", "percona" -> identifier.toUpperCase();
    default -> setCaseIdentifier(identifier);
    };
  }

  /**
   * Delete any existing relevant database schema objects (database, user,
   * schema or valTableNames)and initialise the database for a new run.
   */
  protected abstract void setupDatabase();

  /**
   * Delete any existing relevant database schema objects (database, user,
   * schema or valTableNames)and initialise the database for a new run.
   *
   * @param driver  the database driver
   * @param urlSys  the database URL for privileged access
   * @param urlUser the database URL for non-privileged access
   * @return the database connection
   */
  public Connection setupMysql(String driver, String urlSys, String urlUser) {
    if (isDebug) {
      logger.debug("Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSys,
                         driver);

    String databaseName = config.getDatabase();
    String userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeSQLStmnts(statement,
                       "DROP DATABASE IF EXISTS `" + databaseName + "`",
                       "DROP USER IF EXISTS `" + userName + "`");
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeSQLStmnts(statement,
                       "CREATE DATABASE `" + databaseName + "`",
                       "USE `" + databaseName + "`",
                       "CREATE USER `" + userName + "` IDENTIFIED BY '" + config.getPassword() + "'",
                       "GRANT ALL ON " + databaseName + ".* TO `" + userName + "`");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    disconnectDDL(connection);

    connection = connect(urlUser);

    try {
      statement = connection.createStatement();

      createSchema(connection);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }

    return connection;
  }

  /**
   * Delete any existing relevant database schema objects (database, user,
   * schema or valTableNames)and initialise the database for a new run.
   *
   * @param driver  the database driver
   * @param urlSys  the database URL for privileged access
   * @param urlUser the database URL for non-privileged access
   * @return the database connection
   */
  public Connection setupPostgresql(String driver, String urlSys, String urlUser) {
    if (isDebug) {
      logger.debug("Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    Connection connection   = connect(urlSys,
                                      true);

    String     databaseName = config.getDatabase();
    String     schemaName   = config.getSchema();
    String     userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeSQLStmnts(statement,
                       "DROP SCHEMA IF EXISTS " + schemaName + " CASCADE",
                       "DROP DATABASE IF EXISTS " + databaseName,
                       "DROP USER IF EXISTS " + userName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeSQLStmnts(statement,
                       "CREATE USER " + userName + " WITH ENCRYPTED PASSWORD '" + config.getPassword() + "'",
                       "CREATE DATABASE " + databaseName + " WITH OWNER " + userName,
                       "GRANT ALL PRIVILEGES ON DATABASE " + databaseName + " TO " + userName);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    disconnectDDL(connection);

    connection = connect(urlUser);

    try {
      statement = connection.createStatement();

      executeSQLStmnts(statement,
                       "CREATE SCHEMA " + schemaName,
                       "SET search_path = " + schemaName);

      createSchema(connection);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }

    return connection;
  }

  private void storeConstraint(String schemaName,
                               String tableName,
                               String constraintName,
                               String constraintType,
                               TreeMap<Integer, String> columnNames,
                               String refTableName,
                               TreeMap<Integer, String> refColumnNames) {

    Constraint                  constraint = new Constraint(tickerSymbolIntern, schemaName, tableName, constraintType, refTableName);

    Set<Entry<Integer, String>> columnsSet = columnNames.entrySet();

    for (Entry<Integer, String> mapEntry : columnsSet) {
      constraint.setColumnName(mapEntry.getValue());
    }

    if ("R".equals(constraintType)) {
      Set<Entry<Integer, String>> refColumnsSet = refColumnNames.entrySet();

      for (Entry<Integer, String> mapEntry : refColumnsSet) {
        constraint.setRefColumnName(mapEntry.getValue());
      }
    }

    if ("U".equals(constraintType)) {
      String constName = constraintName;
      if ("informix".equals(tickerSymbolIntern)) {
        if (informixConstraintNames.containsKey(constraintName)) {
          constName = informixConstraintNames.get(constraintName);
          if (!("u".equals(constName.substring(0,
                                               1)))) {
            return;
          }
          if (constraints.containsKey(constName)) {
            Constraint primaryCandidat = constraints.get(constName);
            if ("P".equals(primaryCandidat.getConstraintType())) {
              return;
            }
          }
        }
      }
      constraintName = constName;
    }

    constraint.setConstraintName(constraintName);

    constraints.put(constraintName,
                    constraint);
  }

  private void validateNumberRows(String tableName, int expectedRows) {
    if (isDebug) {
      logger.debug("Start");
    }

    int count = 0;

    try {
      statement = connection.createStatement();

      String sqlStmnt = "SELECT COUNT(*) FROM " + identifierDelimiter + tableName + identifierDelimiter;

      executeSQLStmnt(sqlStmnt);

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
      logger.info("database table " + String.format(FORMAT_TABLE_NAME,
                                                    tableName.toUpperCase()) + " - " + String.format(FORMAT_ROW_NO,
                                                                                                     count) + " rows in total");
    } else {
      logger.fatal("database table " + String.format(FORMAT_TABLE_NAME,
                                                     tableName.toUpperCase()) + " is incomplete - expected" + String.format(FORMAT_ROW_NO,
                                                                                                                            expectedRows) + " rows - found "
          + String.format(FORMAT_ROW_NO,
                          count) + " rows");
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }
}
