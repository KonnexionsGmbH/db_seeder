/**
 *
 */
package ch.konnexions.db_seeder.jdbc.postgresql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder;

/**
 * Test Data Generator for a PostgreSQL DBMS.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public class PostgresqlSeeder extends AbstractJdbcSeeder {

  private static Logger logger = Logger.getLogger(PostgresqlSeeder.class);

  /**
   * Instantiates a new PostgreSQL Database seeder.
   * 
   * @param dbmsTickerSymbol 
   */
  public PostgresqlSeeder(String dbmsTickerSymbol) {
    super();

    if (isDebug) {
      logger.debug("Start Constructor");
    }

    dbms                  = Dbms.POSTGRESQL;
    this.dbmsTickerSymbol = dbmsTickerSymbol;

    tableNameDelimiter    = "";

    urlBase               = config.getConnectionPrefix() + config.getConnectionHost() + ":" + config.getConnectionPort() + "/";
    url                   = urlBase + config.getDatabase() + "?user=" + config.getUser() + "&password=" + config.getPassword();
    urlSetup              = urlBase + config.getDatabaseSys() + "?user=" + config.getUserSys() + "&password=" + config.getPasswordSys();

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

  /**
   * Create the DDL statement: CREATE TABLE.
   *
   * @param tableName the database table name
   *
   * @return the 'CREATE TABLE' statement
   */
  @Override
  protected final String createDdlStmnt(final String tableName) {
    return PostgresqlSchema.createTableStmnts.get(tableName);
  }

  @Override
  protected final void prepStmntInsertColBlob(PreparedStatement preparedStatement, final int columnPos, int rowCount) {
    FileInputStream blobData = null;

    try {
      blobData = new FileInputStream(new File(Paths.get("src",
                                                        "main",
                                                        "resources").toAbsolutePath().toString() + File.separator + "blob.png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      preparedStatement.setBinaryStream(columnPos,
                                        blobData);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      blobData.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  protected final void setupDatabase() {
    if (isDebug) {
      logger.debug("Start");
    }

    // -----------------------------------------------------------------------
    // Connect.
    // -----------------------------------------------------------------------

    connection = connect(urlSetup,
                         true);

    String databaseName = config.getDatabase();
    String userName     = config.getUser();

    // -----------------------------------------------------------------------
    // Tear down an existing schema.
    // -----------------------------------------------------------------------

    try {
      statement = connection.createStatement();

      executeDdlStmnts("DROP DATABASE IF EXISTS " + databaseName,
                       "DROP USER IF EXISTS " + userName);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Setup the database.
    // -----------------------------------------------------------------------

    try {
      executeDdlStmnts("CREATE DATABASE " + databaseName,
                       "CREATE USER " + userName + " WITH ENCRYPTED PASSWORD '" + config.getPassword() + "'",
                       "GRANT ALL PRIVILEGES ON DATABASE " + databaseName + " TO " + userName);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // -----------------------------------------------------------------------
    // Disconnect and reconnect.
    // -----------------------------------------------------------------------

    disconnect(connection);

    connection = connect(url);

    if (isDebug) {
      logger.debug("End");
    }
  }

}
