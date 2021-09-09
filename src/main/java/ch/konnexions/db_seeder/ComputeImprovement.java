package ch.konnexions.db_seeder;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import ch.konnexions.db_seeder.utils.Config;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Data Generator for a Database - Compute runtime improvement with and without constraints.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2021-09-7
 */
public final class ComputeImprovement { // NO_UCD (unused code)

  private static final Logger logger                      = LogManager.getLogger(ComputeImprovement.class);
  private static boolean      isDebug                     = logger.isDebugEnabled();

  private final static int    IMPROVEMENT_POS_CONSTRAINTS = 3;
  private final static int    IMPROVEMENT_POS_DBMS        = 0;
  private final static int    IMPROVEMENT_POS_DB_TYPE     = 2;
  private final static int    IMPROVEMENT_POS_IMPROVEMENT = 4;
  private final static int    IMPROVEMENT_POS_TOTAL_MS    = 2;

  private final static int    STATISTIC_POS_CONSTRAINTS   = 13;
  private final static int    STATISTIC_POS_DBMS          = 1;
  private final static int    STATISTIC_POS_DB_TYPE       = 2;
  private final static int    STATISTIC_POS_IMPROVEMENT   = 14;
  private final static int    STATISTIC_POS_TICKER_SYMBOL = 0;
  private final static int    STATISTIC_POS_TOTAL_MS      = 3;

  /**
   * Calculate the percentage runtime improvement without constraints.
   *@param statisticsFilePath the result file path
   */
  private static void computeImprovement(Config config) {
    if (isDebug) {
      logger.debug("Start computeImprovement()");
    }

    String statisticsFileName = config.getFileStatisticsName();

    Path   statisticsFilePath = Paths.get(statisticsFileName);

    if (Files.notExists(statisticsFilePath)) {
      MessageHandling.abortProgram(logger,
                                   "Program abort: statistics file '" + statisticsFileName + "' is not existing");
    }

    if (Files.isDirectory(statisticsFilePath)) {
      MessageHandling.abortProgram(logger,
                                   "Program abort: '" + statisticsFileName + "' is a directory not a file");
    }

    Map<Pair<String, String>, Triplet<Integer, Integer, Double>> improvementFigures = getImprovmentFigures(config,
                                                                                                           statisticsFileName);

    List<List<String>>                                           statisticsData     = loadCurrentStatisticData(config,
                                                                                                               statisticsFileName,
                                                                                                               improvementFigures);

    saveImprovementData(config,
                        statisticsData);

    if (isDebug) {
      logger.debug("End   computeImprovement()");
    }
  }

  /**
   * Gets the improvement figures.
   *
   * @param config the configuration parameters
   * @param statisticsFileName the file name of the statistics CSV file
   * @return the improvement figures
   */
  @SuppressWarnings({
      "unchecked",
      "rawtypes" })
  private static Map<Pair<String, String>, Triplet<Integer, Integer, Double>> getImprovmentFigures(Config config, String statisticsFileName) {
    if (isDebug) {
      logger.debug("Start getImprovmentFigures()");
    }

    Map<Pair<String, String>, Triplet<Integer, Integer, Double>> improvementFigures = new HashMap<>();

    Reader                                                       statisticsFile     = null;
    try {
      statisticsFile = new FileReader(statisticsFileName);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    Iterable<CSVRecord> records = null;
    try {
      records = CSVFormat.EXCEL.builder().setDelimiter(config.getFileStatisticsDelimiter().charAt(0)).build().parse(statisticsFile);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    for (CSVRecord record : records) {
      String constraints = record.get(STATISTIC_POS_CONSTRAINTS);

      if (!("active".equals(constraints) || ("inactive".equals(constraints)))) {
        continue;
      }

      Pair<String, String> key = new Pair<>(record.get(STATISTIC_POS_TICKER_SYMBOL), record.get(STATISTIC_POS_DB_TYPE));

      int                  ms  = Integer.parseInt(record.get(STATISTIC_POS_TOTAL_MS));

      if (improvementFigures.containsKey(key)) {
        int msActive = improvementFigures.get(key).getValue0();
        improvementFigures.put(key,
                               new Triplet<>(msActive, ms, ((msActive - ms) * 100.) / msActive));

      } else {
        improvementFigures.put(key,
                               new Triplet<>(ms, null, null));
      }
    }

    try {
      statisticsFile.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    improvementFigures.entrySet().removeIf(pairTripletEntry -> ((Triplet<Integer, Integer, Double>) ((Entry) pairTripletEntry).getValue()).getValue2() == null);

    if (isDebug) {
      logger.debug("End   getImprovmentFigures()");
    }

    return improvementFigures;
  }

  /**
   * Load current statistic data from CSV file.
   *
   * @param config the configuration parameters
   * @param statisticsFileName the file name of the CSV statistic file
   * @param improvementFigures the improvement figures
   * @return the list of the statistic data
   */
  private static List<List<String>> loadCurrentStatisticData(Config config,
                                                             String statisticsFileName,
                                                             Map<Pair<String, String>, Triplet<Integer, Integer, Double>> improvementFigures) {
    if (isDebug) {
      logger.debug("Start loadCurrentStatisticData()");
    }

    List<List<String>> currentStatisticsData = new LinkedList<>();

    Reader             statisticsFile        = null;
    try {
      statisticsFile = new FileReader(statisticsFileName);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    Iterable<CSVRecord> records = null;
    try {
      records = CSVFormat.EXCEL.builder().setDelimiter(config.getFileStatisticsDelimiter().charAt(0)).build().parse(statisticsFile);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    boolean isHeader = true;

    for (CSVRecord record : records) {
      if (isHeader) {
        isHeader = false;
        continue;
      }

      String             constraints = record.get(STATISTIC_POS_CONSTRAINTS);

      LinkedList<String> recordList  = new LinkedList<String>();

      recordList.add(record.get(STATISTIC_POS_DBMS));
      recordList.add(record.get(STATISTIC_POS_DB_TYPE));
      recordList.add(record.get(STATISTIC_POS_TOTAL_MS));
      recordList.add(constraints);

      if (!("active".equals(constraints) || ("inactive".equals(constraints)))) {
        recordList.add("");
        currentStatisticsData.add(recordList);
        continue;
      }

      Pair<String, String> key = new Pair<>(record.get(STATISTIC_POS_TICKER_SYMBOL), record.get(STATISTIC_POS_DB_TYPE));

      if (!(improvementFigures.containsKey(key))) {
        recordList.add("");
        currentStatisticsData.add(recordList);
        continue;
      }

      Triplet<Integer, Integer, Double> value         = improvementFigures.get(key);

      double                            improvmentRaw = value.getValue2();

      if ((improvmentRaw >= 0 && "active".equals(constraints)) || (improvmentRaw < 0 && "inactive".equals(constraints))) {
        continue;
      }

      DecimalFormat df = new DecimalFormat("0.0");
      df.setRoundingMode(RoundingMode.HALF_EVEN);

      recordList.add(df.format(improvmentRaw));

      currentStatisticsData.add(recordList);
    }

    try {
      statisticsFile.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End   loadCurrentStatisticData()");
    }

    return currentStatisticsData;
  }

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    logger.info("Start");

    computeImprovement(new Config());

    logger.info("End");

    System.exit(0);
  }

  /**
   * Save the computed improvement data in a new file.
   *
   * @param config the configuration parameters
   * @param statisticsData the current statistics data
   */
  private static void saveImprovementData(Config config, List<List<String>> statisticsData) {
    if (isDebug) {
      logger.debug("Start saveImprovementData()");
    }

    String         statisticsDelimiter = config.getFileStatisticsDelimiter();
    String         improvementFileName = config.getFileImprovementName();

    BufferedWriter bufferedWriter      = null;
    try {
      bufferedWriter = new BufferedWriter(new FileWriter(improvementFileName, false));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      bufferedWriter.write(config.getFileImprovementHeader().replace(";",
                                                                     statisticsDelimiter));
      bufferedWriter.newLine();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    ListIterator<List<String>> listIterator = statisticsData.listIterator();

    while (listIterator.hasNext()) {
      try {
        bufferedWriter.write(String.join(statisticsDelimiter,
                                         listIterator.next()));
        bufferedWriter.newLine();
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    logger.info("Improvement file created: file name=" + improvementFileName);

    if (isDebug) {
      logger.debug("End   saveImprovementData()");
    }
  }
}
