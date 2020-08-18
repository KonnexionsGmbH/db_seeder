package ch.konnexions.db_seeder.samples.monetdb;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Random;

/**
 * Demonstration program for Issue 72.
 * 
 * https://github.com/prestosql/presto/issues/4812
 * 
 * @author  walter@konnexions.ch
 * @since   2020-08-18
 */
public class I72 {
  private static final int    ROW_MAX_CITY          = 1800;
  private static final int    ROW_MAX_COUNTRY       = 200;
  private static final int    ROW_MAX_COUNTRY_STATE = 600;
  private static final int    ROW_MAX_TIMEZONE      = 11;
  
  private final static Logger logger                = Logger.getLogger(I72.class);

  @SuppressWarnings("preview")
  private final static void insertCities(Connection connection, Statement statement) throws Exception {
    statement.execute("""
                      CREATE TABLE CITY (
                          PK_CITY_ID                       BIGINT                    NOT NULL
                                                                                     PRIMARY KEY,
                          FK_COUNTRY_STATE_ID              BIGINT                    REFERENCES COUNTRY_STATE                    (PK_COUNTRY_STATE_ID),
                          CITY_MAP                         BLOB,
                          CREATED                          TIMESTAMP                 NOT NULL,
                          MODIFIED                         TIMESTAMP,
                          NAME                             VARCHAR(100)              NOT NULL
                      )
                         """);

    PreparedStatement preparedStatement = connection.prepareStatement(
                                                                      "INSERT INTO CITY (PK_CITY_ID,FK_COUNTRY_STATE_ID,CITY_MAP,CREATED,MODIFIED,NAME) VALUES (?,?,?,?,?,?)");

    boolean           isToBeExecuted    = false;

    for (int i = 1; i <= ROW_MAX_CITY; i++) {
      int columnNo = 1;
      preparedStatement.setInt(columnNo++,
                               i);
      preparedStatement.setInt(columnNo++,
                               new Random().nextInt(ROW_MAX_COUNTRY_STATE) + 1);
      preparedStatement.setNull(columnNo++,
                                Types.BLOB);
      preparedStatement.setTimestamp(columnNo++,
                                     new Timestamp(System.currentTimeMillis()));
      preparedStatement.setTimestamp(columnNo++,
                                     new Timestamp(System.currentTimeMillis()));
      preparedStatement.setString(columnNo++,
                                  "name_" + i);

      preparedStatement.addBatch();

      if (i % 1000 == 0) {
        logger.info("Table CITY          : " + String.format("%1$6d" + " row(s) inserted so far",
                                                             i));
        preparedStatement.executeBatch();
        isToBeExecuted = false;
      } else {
        isToBeExecuted = true;
      }
    }

    if (isToBeExecuted) {
      logger.info("Table CITY          : " + String.format("%1$6d" + " row(s) inserted in total",
                                                           ROW_MAX_CITY));
      preparedStatement.executeBatch();
    }
  }

  @SuppressWarnings("preview")
  private final static void insertCountries(Connection connection, Statement statement) throws Exception {
    statement.execute("""
                      CREATE TABLE COUNTRY (
                          PK_COUNTRY_ID                    BIGINT                    NOT NULL
                                                                                     PRIMARY KEY,
                          COUNTRY_MAP                      BLOB,
                          CREATED                          TIMESTAMP                 NOT NULL,
                          ISO3166                          VARCHAR(50),
                          MODIFIED                         TIMESTAMP,
                          NAME                             VARCHAR(100)              NOT NULL
                                                                                     UNIQUE
                      )
                      """);

    PreparedStatement preparedStatement = connection.prepareStatement(
                                                                      "INSERT INTO COUNTRY (PK_COUNTRY_ID,COUNTRY_MAP,CREATED,ISO3166,MODIFIED,NAME) VALUES (?,?,?,?,?,?)");

    boolean           isToBeExecuted    = false;

    for (int i = 1; i <= ROW_MAX_COUNTRY; i++) {
      int columnNo = 1;
      preparedStatement.setInt(columnNo++,
                               i);
      preparedStatement.setNull(columnNo++,
                                Types.BLOB);
      preparedStatement.setTimestamp(columnNo++,
                                     new Timestamp(System.currentTimeMillis()));
      preparedStatement.setString(columnNo++,
                                  "iso3166_" + i);
      preparedStatement.setTimestamp(columnNo++,
                                     new Timestamp(System.currentTimeMillis()));
      preparedStatement.setString(columnNo++,
                                  "name_" + i);

      preparedStatement.addBatch();

      if (i % 1000 == 0) {
        logger.info("Table COUNTRY       : " + String.format("%1$6d" + " row(s) inserted so far",
                                                             i));
        preparedStatement.executeBatch();
        isToBeExecuted = false;
      } else {
        isToBeExecuted = true;
      }
    }

    if (isToBeExecuted) {
      logger.info("Table COUNTRY       : " + String.format("%1$6d" + " row(s) inserted in total",
                                                           ROW_MAX_COUNTRY));
      preparedStatement.executeBatch();
    }
  }

  @SuppressWarnings("preview")
  private final static void insertCountryStates(Connection connection, Statement statement) throws Exception {
    statement.execute("""
                      CREATE TABLE COUNTRY_STATE (
                          PK_COUNTRY_STATE_ID              BIGINT                    NOT NULL
                                                                                     PRIMARY KEY,
                          FK_COUNTRY_ID                    BIGINT                    NOT NULL
                                                                                     REFERENCES COUNTRY                          (PK_COUNTRY_ID),
                          FK_TIMEZONE_ID                   BIGINT                    NOT NULL
                                                                                     REFERENCES TIMEZONE                         (PK_TIMEZONE_ID),
                          COUNTRY_STATE_MAP                BLOB,
                          CREATED                          TIMESTAMP                 NOT NULL,
                          MODIFIED                         TIMESTAMP,
                          NAME                             VARCHAR(100)              NOT NULL,
                          SYMBOL                           VARCHAR(50),
                          CONSTRAINT CONSTRAINT_9        UNIQUE      (fk_country_id, name)
                      )
                         """);

    PreparedStatement preparedStatement = connection.prepareStatement(
                                                                      "INSERT INTO COUNTRY_STATE (PK_COUNTRY_STATE_ID,FK_COUNTRY_ID,FK_TIMEZONE_ID,COUNTRY_STATE_MAP,CREATED,MODIFIED,NAME,SYMBOL) VALUES (?,?,?,?,?,?,?,?)");

    boolean           isToBeExecuted    = false;

    for (int i = 1; i <= ROW_MAX_COUNTRY_STATE; i++) {
      int columnNo = 1;
      preparedStatement.setInt(columnNo++,
                               i);
      preparedStatement.setInt(columnNo++,
                               new Random().nextInt(ROW_MAX_COUNTRY) + 1);
      preparedStatement.setInt(columnNo++,
                               new Random().nextInt(ROW_MAX_TIMEZONE) + 1);
      preparedStatement.setNull(columnNo++,
                                Types.BLOB);
      preparedStatement.setTimestamp(columnNo++,
                                     new Timestamp(System.currentTimeMillis()));
      preparedStatement.setTimestamp(columnNo++,
                                     new Timestamp(System.currentTimeMillis()));
      preparedStatement.setString(columnNo++,
                                  "name_" + i);
      preparedStatement.setString(columnNo++,
                                  "symbol_" + i);

      preparedStatement.addBatch();

      if (i % 1000 == 0) {
        logger.info("Table COUNTRY_STATE : " + String.format("%1$6d" + " row(s) inserted so far",
                                                             i));
        preparedStatement.executeBatch();
        isToBeExecuted = false;
      } else {
        isToBeExecuted = true;
      }
    }

    if (isToBeExecuted) {
      logger.info("Table COUNTRY_STATE : " + String.format("%1$6d" + " row(s) inserted in total",
                                                           ROW_MAX_COUNTRY_STATE));
      preparedStatement.executeBatch();
    }
  }

  @SuppressWarnings("preview")
  private final static void insertTimezones(Connection connection, Statement statement) throws Exception {
    statement.execute("""
                      CREATE TABLE TIMEZONE (
                          PK_TIMEZONE_ID                   BIGINT                    NOT NULL
                                                                                     PRIMARY KEY,
                          ABBREVIATION                     VARCHAR(50)               NOT NULL,
                          CREATED                          TIMESTAMP                 NOT NULL,
                          MODIFIED                         TIMESTAMP,
                          NAME                             VARCHAR(100)              NOT NULL
                                                                                     UNIQUE,
                          V_TIME_ZONE                      VARCHAR(4000)
                      )
                         """);

    PreparedStatement preparedStatement = connection.prepareStatement(
                                                                      "INSERT INTO TIMEZONE (PK_TIMEZONE_ID,ABBREVIATION,CREATED,MODIFIED,NAME,V_TIME_ZONE) VALUES (?,?,?,?,?,?)");

    boolean           isToBeExecuted    = false;

    for (int i = 1; i <= ROW_MAX_TIMEZONE; i++) {
      int columnNo = 1;
      preparedStatement.setInt(columnNo++,
                               i);
      preparedStatement.setString(columnNo++,
                                  "abbreviation" + i);
      preparedStatement.setTimestamp(columnNo++,
                                     new Timestamp(System.currentTimeMillis()));
      preparedStatement.setTimestamp(columnNo++,
                                     new Timestamp(System.currentTimeMillis()));
      preparedStatement.setString(columnNo++,
                                  "name_" + i);
      preparedStatement.setString(columnNo++,
                                  "v_time_zone_" + i);

      preparedStatement.addBatch();

      if (i % 1000 == 0) {
        logger.info("Table TIMEZONE      : " + String.format("%1$6d" + " row(s) inserted so far",
                                                             i));
        preparedStatement.executeBatch();
        isToBeExecuted = false;
      } else {
        isToBeExecuted = true;
      }
    }

    if (isToBeExecuted) {
      logger.info("Table TIMEZONE      : " + String.format("%1$6d" + " row(s) inserted in total",
                                                           ROW_MAX_TIMEZONE));
      preparedStatement.executeBatch();
    }
  }

  public static void main(String[] args) throws Exception {
    Class.forName("nl.cwi.monetdb.jdbc.MonetDriver");
    String     url        = "jdbc:monetdb://localhost:50000/demo";
    Connection connection = DriverManager.getConnection(url,
                                                        "monetdb",
                                                        "monetdb");
    Statement  statement  = connection.createStatement();

    statement.execute("CREATE USER kxn_user WITH UNENCRYPTED PASSWORD 'monetdb' NAME 'Dbseeder User' SCHEMA sys");
    statement.execute("CREATE SCHEMA kxn_schema AUTHORIZATION kxn_user");
    statement.execute("ALTER USER kxn_user SET SCHEMA kxn_schema");

    statement.close();
    connection.close();

    connection = DriverManager.getConnection(url,
                                             "kxn_user",
                                             "monetdb");
    statement  = connection.createStatement();

    insertCountries(connection,
                    statement);

    insertTimezones(connection,
                    statement);

    insertCountryStates(connection,
                        statement);

    insertCities(connection,
                 statement);

    statement.close();
    connection.close();
  }

}
