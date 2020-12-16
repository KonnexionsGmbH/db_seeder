package ch.konnexions.db_seeder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.utils.Config;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Test Data Generator for a Database - Create a summary file from the benchmark data.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-07-15
 */
@SuppressWarnings("ucd")
public final class CreateSummaryFile {

  private static BufferedWriter bufferedWriter;

  private final static Config   config                 = new Config();

  private static String         fileStatisticsDelimiter;
  private static String[]       fileStatisticsHeaderTokens;
  private static String         fileStatisticsSummaryNameExt;
  private static String         fileStatisticsSummaryNameNoPath;

  private static final Logger   logger                 = Logger.getLogger(CreateSummaryFile.class);
  private final static boolean  isDebug                = logger.isDebugEnabled();

  private static final int      MAX_FILE_NAME_TOKENS   = 7;

  private static int            numberErrors           = 0;
  private static int            numberProcessedFiles   = 0;
  private static int            numberProcessedRecords = 0;

  private static CSVPrinter     statisticsSummaryFile;

  private static void createHeader() {
    if (isDebug) {
      logger.debug("Start");
    }

    insertRecord(fileStatisticsHeaderTokens,
                 StringUtils.splitByWholeSeparatorPreserveAllTokens(";;creator;db_type;schema;version;file_name",
                                                                    ";"));

    if (isDebug) {
      logger.debug("End");
    }
  }

  private static void insertRecord(String[] fromFileContent, String[] fromFileName) {

    try {
      statisticsSummaryFile.printRecord(fromFileContent[0], // ticker_symbol
                                        fromFileContent[1], // dbms
                                        fromFileName[5], // version
                                        fromFileName[2], // creator
                                        fromFileContent[2], // db_type
                                        fromFileName[4], // schema
                                        fromFileContent[3], // run_time
                                        fromFileContent[4], // start_time
                                        fromFileContent[5], // end_time
                                        fromFileContent[6], // host_name
                                        fromFileContent[7], // no_cores
                                        fromFileContent[8], // operating_system
                                        fromFileName[6]); // file_name
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

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

    fileStatisticsDelimiter    = config.getFileStatisticsDelimiter();
    fileStatisticsHeaderTokens = StringUtils.split(config.getFileStatisticsHeader(),
                                                   ";");
    String fileStatisticsSummaryName = config.getFileStatisticsSummaryName();
    fileStatisticsSummaryNameExt    = FilenameUtils.getExtension(fileStatisticsSummaryName);
    fileStatisticsSummaryNameNoPath = FilenameUtils.getName(fileStatisticsSummaryName);

    openStatisticsSummaryFile(fileStatisticsSummaryName);

    createHeader();

    processDirectories(statisticsSummaryFile);

    try {
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    logger.info("Number total errors           : " + String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                                                   numberErrors));
    logger.info("Number total processed files  : " + String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                                                   numberProcessedFiles));
    logger.info("Number total processed records: " + String.format(AbstractDbmsSeeder.FORMAT_ROW_NO,
                                                                   numberProcessedRecords));

    logger.info("End");

    System.exit(0);
  }

  private static void openStatisticsSummaryFile(String fileName) {
    if (isDebug) {
      logger.debug("Start");
    }

    File fileStatisticsSummary = new File(fileName);

    if (fileStatisticsSummary.isDirectory()) {
      MessageHandling.abortProgram(logger,
                                   "Program abort: '" + fileName + "' is a directory not a file");
    }

    try {
      bufferedWriter        = new BufferedWriter(new FileWriter(fileName, false));
      statisticsSummaryFile = new CSVPrinter(bufferedWriter, CSVFormat.EXCEL.withDelimiter(config.getFileStatisticsDelimiter().charAt(0)));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private static void processDirectories(CSVPrinter statisticsSummaryFile2) {
    if (isDebug) {
      logger.debug("Start");
    }

    String[] directories = StringUtils.split(config.getFileStatisticsSummarySource(),
                                             ";");

    for (String directory : directories) {
      processDirectory(directory);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private static void processDirectory(String directory) {
    if (isDebug) {
      logger.debug("Start directory=" + directory);
    }

    Set<String> fileNames = Stream.of(new File(directory).listFiles()).filter(file -> !file.isDirectory()).map(File::getName).collect(Collectors.toSet());

    for (String fileName : fileNames) {
      if (fileName.equals(fileStatisticsSummaryNameNoPath)) {
        logger.info("File ignored: " + fileName);
        continue;
      }

      if (!(fileStatisticsSummaryNameExt.equals(FilenameUtils.getExtension(fileName)))) {
        logger.error("'defaultNumberOfRows' missing (null)");
        numberErrors++;
      }

      String   fileNameBase      = FilenameUtils.getBaseName(fileName);

      String[] fileNameTokensNet = StringUtils.splitByWholeSeparatorPreserveAllTokens(fileNameBase,
                                                                                      "_",
                                                                                      MAX_FILE_NAME_TOKENS);

      String[] fileNameTokens    = new String[MAX_FILE_NAME_TOKENS];

      System.arraycopy(fileNameTokensNet,
                       0,
                       fileNameTokens,
                       0,
                       fileNameTokensNet.length);

      if (fileNameTokens[2] == null || (!(fileNameTokens[2].equals("bash") || fileNameTokens[2].equals("cmd") || fileNameTokens[2].equals("compose")))) {
        fileNameTokens[2] = "unknown";
      }

      if (fileNameTokens[3] == null || (!(fileNameTokens[3].equals("client") || fileNameTokens[3].equals("embedded") || fileNameTokens[3].equals("presto")))) {
        fileNameTokens[3] = "unknown";
      }

      if (fileNameTokens[4] == null || (!(fileNameTokens[4].equals("company") || fileNameTokens[4].equals("syntax")))) {
        fileNameTokens[4] = "unknown";
      }

      if (fileNameTokens[5] == null || (!(fileNameTokens[5].compareTo("0.0.0") >= 0 && fileNameTokens[5].compareTo("9.9.9") <= 0))) {
        fileNameTokens[5] = "unknown";
      } else {
        fileNameTokens[5] = "v" + fileNameTokens[5];
      }

      fileNameTokens[6] = fileNameBase;

      processFile(directory,
                  fileName,
                  fileNameTokens);

      numberProcessedFiles++;
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  private static void processFile(String directory, String fileName, String[] fileNameTokens) {
    if (isDebug) {
      logger.debug("Start directory=" + directory + " fileName=" + fileName);
    }

    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader(directory + File.separator + fileName));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    String   nextLine;
    String[] nextLineTokens;

    try {
      while ((nextLine = bufferedReader.readLine()) != null) {
        nextLineTokens = StringUtils.split(nextLine,
                                           fileStatisticsDelimiter);

        if (fileStatisticsHeaderTokens[0].equals(nextLineTokens[0])) {
          continue;
        }

        insertRecord(nextLineTokens,
                     fileNameTokens);

        numberProcessedRecords++;
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    try {
      bufferedReader.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (isDebug) {
      logger.debug("End");
    }
  }
}
