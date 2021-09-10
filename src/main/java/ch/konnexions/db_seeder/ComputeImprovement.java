package ch.konnexions.db_seeder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
@SuppressWarnings("ucd")
public final class ComputeImprovement { // NO_UCD (unused code)

  private static final Logger  logger                      = LogManager.getLogger(ComputeImprovement.class);
  private static final boolean isDebug                     = logger.isDebugEnabled();

  private final static int     STATISTIC_POS_CONSTRAINTS   = 13;
  private final static int     STATISTIC_POS_DBMS          = 1;
  private final static int     STATISTIC_POS_DB_TYPE       = 2;
  private final static int     STATISTIC_POS_TICKER_SYMBOL = 0;
  private final static int     STATISTIC_POS_TOTAL_MS      = 3;

  /**
   * Calculate the percentage runtime improvement without constraints.
   *
   * @param config the configuration parameters
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

    Map<Pair<String, String>, Triplet<Integer, Integer, Double>> improvementFigures = getImprovementFigures(config,
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
   * @param statisticsFileName the file name of the statistics file
   * @return the improvement figures
   */
  @SuppressWarnings({
      "unchecked",
      "rawtypes" })
  private static Map<Pair<String, String>, Triplet<Integer, Integer, Double>> getImprovementFigures(Config config, String statisticsFileName) {
    if (isDebug) {
      logger.debug("Start getImprovementFigures()");
    }

    Map<Pair<String, String>, Triplet<Integer, Integer, Double>> improvementFigures = new HashMap<>();

    BufferedReader                                               statisticsFile     = null;
    try {
      statisticsFile = new BufferedReader(new FileReader(statisticsFileName));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    String recordString;

    try {
      while ((recordString = statisticsFile.readLine()) != null) {
        String[] record      = recordString.split(config.getFileStatisticsDelimiter());

        String   constraints = record[STATISTIC_POS_CONSTRAINTS];

        if (!("active".equals(constraints) || ("inactive".equals(constraints)))) {
          continue;
        }

        Pair<String, String> key = new Pair<>(record[STATISTIC_POS_TICKER_SYMBOL], record[STATISTIC_POS_DB_TYPE]);

        int                  ms  = Integer.parseInt(record[STATISTIC_POS_TOTAL_MS]);

        if (improvementFigures.containsKey(key)) {
          int msActive = improvementFigures.get(key).getValue0();
          improvementFigures.put(key,
                                 new Triplet<>(msActive, ms, ((msActive - ms) * 100.) / msActive));

        } else {
          improvementFigures.put(key,
                                 new Triplet<>(ms, null, null));
        }
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      statisticsFile.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    improvementFigures.entrySet().removeIf(pairTripletEntry -> ((Triplet<Integer, Integer, Double>) ((Entry) pairTripletEntry).getValue()).getValue2() == null);

    if (isDebug) {
      logger.debug("End   getImprovementFigures()");
    }

    return improvementFigures;
  }

  /**
   * Load current statistic data from the file.
   *
   * @param config the configuration parameters
   * @param statisticsFileName the file name of the statistic file
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

    BufferedReader     statisticsFile        = null;
    try {
      statisticsFile = new BufferedReader(new FileReader(statisticsFileName));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    boolean isHeader = true;
    String  recordString;

    try {
      while ((recordString = statisticsFile.readLine()) != null) {
        if (isHeader) {
          isHeader = false;
          continue;
        }

        String[]           record      = recordString.split(config.getFileStatisticsDelimiter());

        String             constraints = record[STATISTIC_POS_CONSTRAINTS];

        LinkedList<String> recordList  = new LinkedList<>();

        recordList.add(record[STATISTIC_POS_DBMS]);
        recordList.add(record[STATISTIC_POS_DB_TYPE]);
        recordList.add(record[STATISTIC_POS_TOTAL_MS]);
        recordList.add(constraints);

        if (!("active".equals(constraints) || ("inactive".equals(constraints)))) {
          recordList.add("");
          currentStatisticsData.add(recordList);
          continue;
        }

        Pair<String, String> key = new Pair<>(record[STATISTIC_POS_TICKER_SYMBOL], record[STATISTIC_POS_DB_TYPE]);

        if (!(improvementFigures.containsKey(key))) {
          recordList.add("");
          currentStatisticsData.add(recordList);
          continue;
        }

        Triplet<Integer, Integer, Double> value          = improvementFigures.get(key);

        double                            improvementRaw = value.getValue2();

        if ((improvementRaw >= 0 && "active".equals(constraints)) || (improvementRaw < 0 && "inactive".equals(constraints))) {
          continue;
        }

        DecimalFormat df = new DecimalFormat("9.9");
        df.setRoundingMode(RoundingMode.HALF_EVEN);

        recordList.add(df.format(improvementRaw));

        currentStatisticsData.add(recordList);
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
      System.exit(1);
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

    if (args.length > 0) {
      MessageHandling.abortProgram(logger,
                                   "Program abort: unexpected command line argument(s)");
    }

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

    BufferedWriter improvemenstFile    = null;
    try {
      improvemenstFile = new BufferedWriter(new FileWriter(improvementFileName, false));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      improvemenstFile.write(config.getFileImprovementHeader().replace(";",
                                                                       statisticsDelimiter));
      improvemenstFile.newLine();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    ListIterator<List<String>> listIterator = statisticsData.listIterator();

    while (listIterator.hasNext()) {
      try {
        improvemenstFile.write(String.join(statisticsDelimiter,
                                           listIterator.next()));
        improvemenstFile.newLine();
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      improvemenstFile.close();
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
