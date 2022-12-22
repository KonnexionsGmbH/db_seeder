/*
 *
 */

package ch.konnexions.db_seeder.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.AbstractDbmsSeeder;

/**
 * This class is used to record the statistics of the db_seeder runs.
 */
public final class Statistics {

  private static final Logger         logger                     = LogManager.getLogger(Statistics.class);

  private final Config                config;

  private final Map<String, String[]> dbmsValues;

  private long                        durationDDLConstraintsAdd  = 0;

  private long                        durationDDLConstraintsDrop = 0;

  private long                        durationDML;
  private final DateTimeFormatter     formatter                  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnn");
  private final boolean               isDebug                    = logger.isDebugEnabled();

  private LocalDateTime               startDateTimeDML;

  private final LocalDateTime         startDateTimeTotal;

  private BufferedWriter              statisticsFile             = null;

  private final String                tickerSymbolExtern;

  /**
   * Constructs a Statistics object using the given {@link Config} object.
   *
   * @param config             the {@link Config} object
   * @param dbmsValues         the DBMS related values DBMS name and db type remark
   * @param tickerSymbolExtern the external DBMS ticker symbol
   */
  public Statistics(Config config, String tickerSymbolExtern, Map<String, String[]> dbmsValues) {
    this.config             = config;
    this.dbmsValues         = dbmsValues;
    this.startDateTimeTotal = LocalDateTime.now();
    this.tickerSymbolExtern = tickerSymbolExtern;

    createStatisticsFile();

    openStatisticsFile();
  }

  /**
   * Creates the measuring entry.
   */
  public void createMeasuringEntry() {
    if (isDebug) {
      logger.debug("Start");
    }

    LocalDateTime endDateTimeTotal = LocalDateTime.now();
    try {
      long durationTotal = Duration.between(startDateTimeTotal,
                                            endDateTimeTotal).toMillis();

      logger.info(String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                durationTotal - durationDML) + " ms - total DDL");
      logger.info(String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                durationDML) + " ms - total DML");
      logger.info(String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                durationTotal) + " ms - total");

      String dbType = dbmsValues.get(tickerSymbolExtern)[AbstractDbmsSeeder.DBMS_DETAILS_CLIENT_EMBEDDED];

      String constraints;
      if ("yes".equals(System.getenv("DB_SEEDER_DROP_CONSTRAINTS"))) {
        constraints = "inactive";
      } else if ("cockroach".equals(tickerSymbolExtern)
          || "cratedb".equals(tickerSymbolExtern)
          || "h2".equals(tickerSymbolExtern)
          || "h2_emb".equals(tickerSymbolExtern)
          || "heavy".equals(tickerSymbolExtern)
          || "sqlite".equals(tickerSymbolExtern)
          || "trino".equals(dbType)) {
        constraints = "active - no choice";
      } else {
        constraints = "active";
      }

      statisticsFile.write(String.join(config.getFileStatisticsDelimiter(),
                                       tickerSymbolExtern,
                                       dbmsValues.get(tickerSymbolExtern)[AbstractDbmsSeeder.DBMS_DETAILS_NAME_CHOICE],
                                       dbType,
                                       Long.toString(durationTotal),
                                       startDateTimeTotal.format(formatter),
                                       endDateTimeTotal.format(formatter),
                                       InetAddress.getLocalHost().getHostName(),
                                       Integer.toString(Runtime.getRuntime().availableProcessors()),
                                       System.getProperty("os.arch") + " / " + System.getProperty("os.name") + " / " + System.getProperty("os.version"),
                                       Long.toString(durationTotal - durationDML),
                                       Long.toString(durationDDLConstraintsAdd),
                                       Long.toString(durationDDLConstraintsDrop),
                                       Long.toString(durationDML),
                                       constraints));
      statisticsFile.newLine();

      statisticsFile.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Create a new statistics file if none exists yet.
   */
  private void createStatisticsFile() {
    if (isDebug) {
      logger.debug("Start");
    }

    String statisticsDelimiter = config.getFileStatisticsDelimiter();
    String statisticsName      = config.getFileStatisticsName();

    try {
      Path statisticsPath       = Paths.get(statisticsName);
      Path statisticsPathParent = statisticsPath.getParent();

      if (statisticsPathParent == null) {
        MessageHandling.abortProgram(logger,
                                     "Program abort: The directory path for the statistics file is missing (null)");
        System.exit(1);
      }

      Files.createDirectories(statisticsPathParent);

      boolean isFileExisting = Files.exists(Paths.get(statisticsName));

      if (!(isFileExisting)) {
        Files.createFile(statisticsPath);

        BufferedWriter statisticsFile = new BufferedWriter(new FileWriter(statisticsName, false));

        try {
          statisticsFile.write(config.getFileStatisticsHeader().replace(";",
                                                                        statisticsDelimiter));
          statisticsFile.newLine();
        } catch (IOException e) {
          e.printStackTrace();
          System.exit(1);
        }

        statisticsFile.close();

        logger.info("missing statistics file created: file name=" + statisticsName);
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private void openStatisticsFile() {
    String statisticsName = config.getFileStatisticsName();

    try {
      boolean isFileExisting = Files.exists(Paths.get(statisticsName));

      if (!(isFileExisting)) {
        MessageHandling.abortProgram(logger,
                                     "Program abort: statistics file \"" + statisticsName + "\" is missing");
      }

      statisticsFile = new BufferedWriter(new FileWriter(statisticsName, true));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the duration in ms of all DDL operations to restore the constraints.
   */
  public void setDurationDDLConstraintsAdd(long duration) {
    durationDDLConstraintsAdd = duration;
  }

  /**
   * Sets the duration in ms of all DDL operations to drop the constraints.
   */
  public void setDurationDDLConstraintsDrop(long duration) {
    durationDDLConstraintsDrop = duration;
  }

  /**
   * Sets the duration in ms of all DML operations.
   */
  public void setDurationDML() {
    durationDML = Duration.between(startDateTimeDML,
                                   LocalDateTime.now()).toMillis();
  }

  /**
   * Sets the start date time of DML operations.
   */
  public void setStartDateTimeDML() {
    startDateTimeDML = LocalDateTime.now();
  }
}
