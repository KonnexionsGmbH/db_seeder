/**
 *
 */
package ch.konnexions.db_seeder.jdbc.cratedb;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a CrateDB DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class CratedbSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(CratedbSeeder.class);

  /**
   * Instantiates a new CrateDB seeder.
   */
  public CratedbSeeder() {
    super();

    String methodName = new Object() {
    }.getClass().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start Constructor");

    dbms               = Dbms.CRATEDB;

    tableNameDelimiter = "";

    urlBase            = config.getCratedbConnectionPrefix() + config.getJdbcConnectionHost() + ":" + config.getCratedbConnectionPort() + "/?strict=true&user=";
    url                = urlBase + config.getCratedbUser() + "&password=" + config.getCratedbPassword();
    urlSetup           = urlBase + config.getCratedbUserSys();

    dropTableStmnt     = "SELECT table_name, 'DROP TABLE \"' || table_name || '\"' FROM information_schema.tables WHERE table_name = ? AND table_schema = 'doc'";

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End   Constructor");
  }

  @SuppressWarnings("preview")
  @Override
  protected final String createDdlStmnt(final String tableName) {
    switch (tableName) {
    case TABLE_NAME_CITY:
      return """
             CREATE TABLE CITY (
                 PK_CITY_ID          BIGINT    NOT NULL PRIMARY KEY,
                 FK_COUNTRY_STATE_ID BIGINT,
                 CITY_MAP            OBJECT,
                 CREATED             TIMESTAMP NOT NULL,
                 MODIFIED            TIMESTAMP,
                 NAME                TEXT      NOT NULL
              )""";
    case TABLE_NAME_COMPANY:
      return """
             CREATE TABLE COMPANY (
                 PK_COMPANY_ID BIGINT    NOT NULL PRIMARY KEY,
                 FK_CITY_ID    BIGINT    NOT NULL,
                 ACTIVE        TEXT      NOT NULL,
                 ADDRESS1      TEXT,
                 ADDRESS2      TEXT,
                 ADDRESS3      TEXT,
                 CREATED       TIMESTAMP NOT NULL,
                 DIRECTIONS    TEXT,
                 EMAIL         TEXT,
                 FAX           TEXT,
                 MODIFIED      TIMESTAMP,
                 NAME          TEXT      NOT NULL,
                 PHONE         TEXT,
                 POSTAL_CODE   TEXT,
                 URL           TEXT,
                 VAT_ID_NUMBER TEXT
             )""";
    case TABLE_NAME_COUNTRY:
      return """
             CREATE TABLE COUNTRY (
                PK_COUNTRY_ID BIGINT    NOT NULL PRIMARY KEY,
                COUNTRY_MAP   OBJECT,
                CREATED       TIMESTAMP NOT NULL,
                ISO3166       TEXT,
                MODIFIED      TIMESTAMP,
                NAME          TEXT      NOT NULL
             )""";
    case TABLE_NAME_COUNTRY_STATE:
      return """
             CREATE TABLE COUNTRY_STATE (
                PK_COUNTRY_STATE_ID BIGINT    NOT NULL PRIMARY KEY,
                FK_COUNTRY_ID       BIGINT    NOT NULL,
                FK_TIMEZONE_ID      BIGINT    NOT NULL,
                COUNTRY_STATE_MAP   OBJECT,
                CREATED             TIMESTAMP NOT NULL,
                MODIFIED            TIMESTAMP,
                NAME                TEXT      NOT NULL,
                SYMBOL              TEXT
             )""";
    case TABLE_NAME_TIMEZONE:
      return """
             CREATE TABLE TIMEZONE (
                PK_TIMEZONE_ID BIGINT     NOT NULL PRIMARY KEY,
                ABBREVIATION   TEXT       NOT NULL,
                CREATED        TIMESTAMP  NOT NULL,
                MODIFIED       TIMESTAMP,
                NAME           TEXT       NOT NULL,
                V_TIME_ZONE    TEXT
             )""";
    default:
      throw new RuntimeException("Not yet implemented - database table : " + String.format(FORMAT_TABLE_NAME, tableName));
    }
  }

  private final void dropAllTables() throws SQLException {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    for (String tableName : TABLE_NAMES_DROP) {
      String sqlStmntLocal = "DROP TABLE IF EXISTS " + tableName;
      logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- sqlStmnt='" + sqlStmntLocal + "'");
      statement.execute(sqlStmntLocal);
    }

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }

  @Override
  protected final void setupDatabase() {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- Start");

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSetup, true);

    // -----------------------------------------------------------------------
    // Drop the database user and tables if already existing.
    // -----------------------------------------------------------------------
    // java.sql.ParameterMetaData as returned by e.g. java.sql.PreparedStatement
    // DataSource is not implemented
    // -----------------------------------------------------------------------

    String cratedbUser = config.getCratedbUser();

    try {
      statement = connection.createStatement();

      statement.execute("DROP USER IF EXISTS " + cratedbUser);

      dropAllTables();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Create the database user and grant the necessary rights.
    // -----------------------------------------------------------------------

    try {
      statement.execute("CREATE USER " + cratedbUser + " WITH (PASSWORD = '" + config.getCratedbPassword() + "')");

      statement.execute("GRANT ALL PRIVILEGES TO " + cratedbUser);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(url, true);

    logger.debug(String.format(FORMAT_METHOD_NAME, methodName) + "- End");
  }
}
