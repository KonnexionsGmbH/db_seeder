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
import org.apache.log4j.Logger;

/**
 * This class is used to record the statisticss of the db_seeder runs.
 */
public final class Statistics {
  private static final Logger         logger    = Logger.getLogger(Statistics.class);
  private final boolean               isDebug   = logger.isDebugEnabled();

  private final Config                config;

  private final String                dbmsTickerSymbol;
  private final Map<String, String[]> dbmsValues;

  private final DateTimeFormatter     formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnn");

  private final LocalDateTime         startDateTime;
  private CSVPrinter                  statisticsFile;

  /**
   * Constructs a Statistics object using the given {@link Config} object.
   * @param config the {@link Config} object
   * @param dbmsTickerSymbol DBMS ticker symbol the DBMS ticker symbol
   * @param dbmsValues the DBMS related values DBMS name and client / embedded remark
   */
  public Statistics(Config config, String dbmsTickerSymbol, Map<String, String[]> dbmsValues) {
    this.config           = config;
    this.dbmsTickerSymbol = dbmsTickerSymbol;
    this.dbmsValues       = dbmsValues;
    this.startDateTime    = LocalDateTime.now();

    createStatisticsFile();

    openStatisticsFile();
  }

  public final void createMeasuringEntry() {
    if (isDebug) {
      logger.debug("Start");
    }

    try {
      LocalDateTime endDateTime = LocalDateTime.now();

      statisticsFile.printRecord(dbmsTickerSymbol,
                                 dbmsValues.get(dbmsTickerSymbol)[0],
                                 dbmsValues.get(dbmsTickerSymbol)[1],
                                 Duration.between(startDateTime,
                                                  endDateTime).toSeconds(),
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
   * Creates a new statistics file if none exists yet.
   */
  @SuppressWarnings("resource")
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
                                     "Statistics file \"" + statisticsName + "\" is missing");
      }

      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(statisticsName, true));
      statisticsFile = new CSVPrinter(bufferedWriter, CSVFormat.EXCEL.withDelimiter(statisticsDelimiter.charAt(0)));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
