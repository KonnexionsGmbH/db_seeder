package ch.konnexions.db_seeder.jdbc.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.WeakHashMap;

import ch.konnexions.db_seeder.AbstractDbmsSeeder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generated.AbstractGenOracleSchema;
import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Data Generator for an Oracle DBMS.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2020-05-01
 */
public final class OracleSeeder extends AbstractGenOracleSchema {

  private class Constraint {
    private LinkedHashSet<String> columnNames    = new LinkedHashSet<String>();
    private String                constraintName;
    private String                constraintType;
    private LinkedHashSet<String> refColumnNames = new LinkedHashSet<String>();
    private String                refTableName;
    private String                tableName;

    public String getDropStatement() {
      return "ALTER TABLE " + tableName + " DROP CONSTRAINT " + constraintName + " CASCADE";
    }

    public String getRestoreStatement() {
      String restoreStatement = "ALTER TABLE " + tableName + " ADD CONSTRAINT " + constraintName;

      switch (constraintType) {
      case "P":
        restoreStatement = restoreStatement + " PRIMARY KEY (";
        break;
      case "R":
        restoreStatement = restoreStatement + " FOREIGN KEY (";
        break;
      case "U":
        restoreStatement = restoreStatement + " UNIQUE (";
      }

      restoreStatement = restoreStatement + String.join(",",
                                                        columnNames) + ")";

      if ("R".equals(constraintType)) {
        restoreStatement = restoreStatement + " REFERENCES " + refTableName + " (" + String.join(",",
                                                                                                 refColumnNames) + ")";
      }

      return restoreStatement + " ENABLE";
    }

    public void setColumnName(String column) {
      this.columnNames.add(column);
    }

    public void setConstraintName(String constraintName) {
      this.constraintName = constraintName;
    }

    public void setConstraintType(String constraintType) {
      this.constraintType = constraintType;
    }

    public void setRefColumnName(String refColumnName) {
      this.refColumnNames.add(refColumnName);
    }

    public void setRefTableName(String refTableName) {
      this.refTableName = refTableName;
    }

    public void setTableName(String tableName) {
      this.tableName = tableName;

    }
  }

  private static final Logger logger = LogManager.getLogger(OracleSeeder.class);

  /**
   * Gets the connection URL.
   *
   * @param connectionHost    the connection host name
   * @param connectionPort    the connection port number
   * @param connectionPrefix  the connection prefix
   * @param connectionService the connection service
   * @return the connection URL
   */
  private static String getUrl(String connectionHost, int connectionPort, String connectionPrefix, String connectionService) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + connectionService + "?oracle.net.disableOob=true";
  }

  /**
   * Gets the connection URL for trino (used by TrinoEnvironment).
   *
   * @param connectionHost    the connection host name
   * @param connectionPort    the connection port number
   * @param connectionPrefix  the connection prefix
   * @param connectionService the connection service
   * @return the connection URL for non-privileged access
   */
  public static String getUrlTrino(String connectionHost, int connectionPort, String connectionPrefix, String connectionService) {
    return connectionPrefix + connectionHost + ":" + connectionPort + "/" + connectionService + "?oracle.net.disableOob=true";
  }

  private WeakHashMap<String, Constraint> constraints;

  private final boolean                   isDebug = logger.isDebugEnabled();

  /**
   * Instantiates a new Oracle seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   */
  public OracleSeeder(String tickerSymbolExtern) {
    this(tickerSymbolExtern, "client");
  }

  /**
   * Instantiates a new Oracle seeder object.
   *
   * @param tickerSymbolExtern the external DBMS ticker symbol
   * @param dbmsOption         client, embedded or trino
   */
  public OracleSeeder(String tickerSymbolExtern, String dbmsOption) {
    super(tickerSymbolExtern, dbmsOption);

    if (isDebug) {
      logger.debug("Start Constructor - tickerSymbolExtern=" + tickerSymbolExtern + " - dbmsOption=" + dbmsOption);
    }

    dbmsEnum = DbmsEnum.ORACLE;

    if (isTrino) {
      urlTrino = AbstractJdbcSeeder.getUrlTrino(tickerSymbolLower,
                                                config.getConnectionHostTrino(),
                                                config.getConnectionPortTrino(),
                                                config.getUser());
    }

    urlUser = getUrl(config.getConnectionHost(),
                     config.getConnectionPort(),
                     config.getConnectionPrefix(),
                     config.getConnectionService());

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Create the DDL statement: CREATE TABLE.
   *
   * @param tableName the database table name
   * @return the 'CREATE TABLE' statement
   */
  @Override
  protected final String createDdlStmnt(String tableName) {
    return AbstractGenOracleSchema.createTableStmnts.get(tableName);
  }

  @Override
  protected void dropTableConstraints(Connection connection) {
    if (isDebug) {
      logger.debug("Start");
    }

    LocalDateTime startDateTime       = LocalDateTime.now();

    final int     POS_TABLE_NAME      = 1;
    final int     POS_CONSTRAINT_NAME = 2;
    final int     POS_CONSTRAINT_TYPE = 3;
    final int     POS_COLUMN_NAME     = 4;
    final int     POS_POSITION        = 5;
    final int     POS_REF_TABLE_NAME  = 6;
    final int     POS_REF_COLUMN_NAME = 7;

    try {
      statement = connection.createStatement();

      String sqlStmnt = """
                        SELECT ac.table_name,
                               ac.constraint_name,
                               ac.constraint_type,
                               acc.column_name,
                               acc.POSITION,
                               ac_r.TABLE_NAME,
                               acc_r.COLUMN_NAME
                          FROM                 all_constraints ac
                               LEFT OUTER JOIN all_cons_columns acc   ON ac.constraint_name = acc.constraint_name
                               LEFT OUTER JOIN all_constraints  ac_r  ON ac.R_CONSTRAINT_NAME = ac_r.constraint_name
                               LEFT OUTER JOIN all_cons_columns acc_r ON ac.r_constraint_name = acc_r.constraint_name
                         WHERE ac.constraint_type IN ('F', 'P', 'U')
                           AND ac.table_name IN ('tableNames')
                           AND ac.owner = 'user'
                         ORDER BY ac.constraint_type DESC,
                                  ac.constraint_name,
                                  acc.position
                        """.replace("tableNames",
                                    String.join("','",
                                                TABLE_NAMES_CREATE)).replace("user",
                                                                             config.getUser().toUpperCase());

      if (isDebug) {
        logger.debug("sqlStmnt='" + sqlStmnt + "'");
      }

      resultSet = statement.executeQuery(sqlStmnt);

      while (resultSet.next()) {
        String     constraintName = resultSet.getString(POS_CONSTRAINT_NAME);
        String     columnName     = resultSet.getString(POS_COLUMN_NAME);
        int        position       = resultSet.getInt(POS_POSITION);
        String     refColumnName  = resultSet.getString(POS_REF_COLUMN_NAME);

        Constraint constraint;

        if (position == 1) {
          constraint = new Constraint();

          constraint.setTableName(resultSet.getString(POS_TABLE_NAME));
          constraint.setConstraintName(constraintName);
          constraint.setConstraintType(resultSet.getString(POS_CONSTRAINT_TYPE));
          constraint.setColumnName(columnName);
          constraint.setRefTableName(resultSet.getString(POS_REF_TABLE_NAME));
          constraint.setRefColumnName(refColumnName);
        } else {
          constraint = constraints.get(constraintName);

          constraint.setColumnName(columnName);
          constraint.setRefColumnName(refColumnName);
        }

        constraints.put(constraintName,
                        constraint);
      }

      resultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    logger.info("      " + String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                         Duration.between(startDateTime,
                                                          LocalDateTime.now()).toMillis()) + " ms - total DDL constraints (FK, PK, UK) retrieved");

    for (String tableName : TABLE_NAMES_DROP) {
      for (Constraint constraint : constraints.values()) {
        if (tableName.equals(constraint.tableName)) {
          executeDdlStmnts(statement,
                           constraint.getDropStatement());
        }
      }
    }

    try {
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  @Override
  protected void restoreTableConstraints(Connection connection) {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      statement = connection.createStatement();

      for (String tableName : TABLE_NAMES_CREATE) {
        for (Constraint constraint : constraints.values()) {
          if (tableName.equals(constraint.tableName)) {
            executeDdlStmnts(statement,
                             constraint.getRestoreStatement());
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

  /**
   * Delete any existing relevant database schema objects (database, user,
   * schema or valTableNames)and initialise the database for a new run.
   */
  @Override
  protected final void setupDatabase() {
    if (isDebug) {
      logger.debug("Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlUser,
                         null,
                         config.getUserSys().toUpperCase(),
                         config.getPasswordSys());

    String userName = config.getUser().toUpperCase();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    dropUser(userName,
             "CASCADE",
             "ALL_USERS",
             "username");

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts(statement,
                       "CREATE USER " + userName + " IDENTIFIED BY \"" + config.getPassword() + "\"",
                       "ALTER USER " + userName + " QUOTA UNLIMITED ON users",
                       "GRANT CREATE SESSION TO " + userName,
                       "GRANT CREATE TABLE TO " + userName,
                       "GRANT UNLIMITED TABLESPACE TO " + userName);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create database schema.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(urlUser,
                         null,
                         userName,
                         config.getPassword());

    try {
      statement = connection.createStatement();

      createSchema(connection);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect - trino.
    // -----------------------------------------------------------------------

    if (isTrino) {
      disconnect(connection);

      connection = connect(urlTrino,
                           driver_trino,
                           true);
    }

    constraints = new WeakHashMap<String, Constraint>();

    if (isDebug) {
      logger.debug("End");
    }
  }
}
