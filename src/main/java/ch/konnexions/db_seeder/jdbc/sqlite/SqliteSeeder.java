/**
 *
 */
package ch.konnexions.db_seeder.jdbc.sqlite;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for am SQLite DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class SqliteSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(SqliteSeeder.class);

  /**
   * Instantiates a new SQLite seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public SqliteSeeder(String dbmsTickerSymbol) {
    super();

    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start Constructor");
    }

    dbms                  = Dbms.SQLITE;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    tableNameDelimiter    = "";

    url                   = config.getConnectionPrefix() + config.getDatabase();

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End   Constructor");
    }
  }

  @Override protected final String createDdlStmnt(final String tableName) {
    return SqliteSchema.databaseTables.get(tableName);
  }

  @Override protected void setupDatabase() {
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(url);

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();
      dropAllTablesIfExists();
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    if (isDebug) {
      logger.debug(String.format(FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }
}
