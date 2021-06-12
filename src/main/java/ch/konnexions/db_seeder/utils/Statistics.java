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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.AbstractDbmsSeeder;

/**
 * This class is used to record the statisticss of the db_seeder runs.
 */
public final class Statistics {
  private static final Logger         logger    = LogManager.getLogger(Statistics.class);

  private final boolean               isDebug   = logger.isDebugEnabled();
  private final Config                config;

  private final Map<String, String[]> dbmsValues;

  private final DateTimeFormatter     formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnn");

  private final LocalDateTime         startDateTime;

  private LocalDateTime               startDateTimeDML;
  private CSVPrinter                  statisticsFile;
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
    this.startDateTime      = LocalDateTime.now();
    this.tickerSymbolExtern = tickerSymbolExtern;

    createStatisticsFile();

    openStatisticsFile();
  }

  public final void createMeasuringEntry() {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      LocalDateTime endDateTime   = LocalDateTime.now();

      long          durationTotal = Duration.between(startDateTime,
                                                     endDateTime).toMillis();

      logger.info(String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                Duration.between(startDateTime,
                                                 startDateTimeDML).toMillis()) + " ms - total DDL");
      logger.info(String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                Duration.between(startDateTimeDML,
                                                 endDateTime).toMillis()) + " ms - total DML");
      logger.info(String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                durationTotal) + " ms - total");

      statisticsFile.printRecord(tickerSymbolExtern,
                                 dbmsValues.get(tickerSymbolExtern)[AbstractDbmsSeeder.DBMS_DETAILS_NAME_CHOICE],
                                 dbmsValues.get(tickerSymbolExtern)[AbstractDbmsSeeder.DBMS_DETAILS_CLIENT_EMBEDDED],
                                 durationTotal,
                                 startDateTime.format(formatter),
                                 endDateTime.format(formatter),
                                 InetAddress.getLocalHost().getHostName(),
                                 Integer.toString(Runtime.getRuntime().availableProcessors()),
                                 System.getProperty("os.arch") + " / " + System.getProperty("os.name") + " / " + System.getProperty("os.version"));

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
      Path statisticsPath = Paths.get(statisticsName);

      Files.createDirectories(statisticsPath.getParent());

      boolean isFileExisting = Files.exists(Paths.get(statisticsName));

      if (!(isFileExisting)) {
        Files.createFile(statisticsPath);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(statisticsName, false));

        new CSVPrinter(bufferedWriter, CSVFormat.EXCEL.withDelimiter(statisticsDelimiter.charAt(0)).withHeader(config.getFileStatisticsHeader().replace(";",
                                                                                                                                                        statisticsDelimiter)
            .split(statisticsDelimiter)));

        bufferedWriter.close();

        logger.info("missing statistics file created: file name=" + config.getFileStatisticsName());
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
    String statisticsDelimiter = config.getFileStatisticsDelimiter();
    String statisticsName      = config.getFileStatisticsName();

    try {
      boolean isFileExisting = Files.exists(Paths.get(statisticsName));

      if (!(isFileExisting)) {
        MessageHandling.abortProgram(logger,
                                     "Program abort: statistics file \"" + statisticsName + "\" is missing");
      }

      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(statisticsName, true));
      statisticsFile = new CSVPrinter(bufferedWriter, CSVFormat.EXCEL.withDelimiter(statisticsDelimiter.charAt(0)));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Sets the start date time of DML opeations.
   */
  public void setStartDateTimeDML() {
    startDateTimeDML = LocalDateTime.now();
  }
}
