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

import ch.konnexions.db_seeder.AbstractDatabaseSeeder;

/**
 * This class is used to record the statisticss of the db_seeder runs.
 */
public class Statistics {
  private static Logger               logger    = Logger.getLogger(Statistics.class);

  private final Config                config;

  private final String                dbmsTickerSymbol;
  private final Map<String, String[]> dbmsValues;

  private final DateTimeFormatter     formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnn");

  private final boolean               isDebug   = logger.isDebugEnabled();

  private final LocalDateTime         startDateTime;
  private CSVPrinter                  statisticsFile;

  /**
   * Constructs a Statistics object using the given {@link Config} object.
   * @param config the {@link Config} object
   * @param dbmsTickerSymbol the DBMS ticker symbol
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
    String methodName = null;

    if (isDebug) {
      methodName = new Object() {
      }.getClass().getEnclosingMethod().getName();

      logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
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
      logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  /**
   * Creates a new statistics file if none exists yet.
   */
  @SuppressWarnings("resource")
  private final void createStatisticsFile() {
    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    if (isDebug) {
      logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME,
                                 methodName) + "- Start");
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

        logger.info(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME,
                                  methodName) + "  missing statistics file created: file name=" + config.getFileStatisticsName());
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME,
                                 methodName) + "- End");
    }
  }

  private final void openStatisticsFile() {
    String statisticsDelimiter = config.getFileStatisticsDelimiter();
    String statisticsName      = config.getFileStatisticsName();

    try {
      boolean isFileExisting = Files.exists(Paths.get(statisticsName));

      if (!(isFileExisting)) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        logger.error(String.format(AbstractDatabaseSeeder.FORMAT_METHOD_NAME,
                                   methodName) + "  fatal error: program abort =====> statistics file \"" + statisticsName + "\" is missing <=====");
        System.exit(1);
      }

      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(statisticsName, true));
      statisticsFile = new CSVPrinter(bufferedWriter, CSVFormat.EXCEL.withDelimiter(statisticsDelimiter.charAt(0)));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
