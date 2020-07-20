/**
 * 
 */
package ch.konnexions.db_seeder;

import java.io.File;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.utils.Config;

/**
 * Test Data Generator for a Database - Application.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-07-15
 */
public class SchemaGenerator {

  private final static Config config       = new Config();

  private final static String fileJsonName = config.getFileJsonName();

  private final boolean       isDebug      = logger.isDebugEnabled();

  private final static Logger logger       = Logger.getLogger(SchemaGenerator.class);

  private static File         fileJson;

  private static String       release      = "";

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    logger.info("Start");

    if (args.length > 0) {
      release = args[0];
    }

    logger.info("args[0]='" + release + "'");

    if (null == release) {
      logger.error("Abort: Command line argument 'DB_SEEDER_RELEASE' missing (null)");
      System.exit(1);
    }

    fileJson = new File(fileJsonName);

    if (!fileJson.exists()) {
      logger.error("Abort: File '" + fileJsonName + "' is not existing");
      System.exit(1);
    }

    if (fileJson.isDirectory()) {
      logger.error("Abort: '" + fileJsonName + "' is a directory not a file");
      System.exit(1);
    }

    SchemaGenerator schemaGenerator = new SchemaGenerator();

    schemaGenerator.generateSchema();

    logger.info("End");

    System.exit(0);
  }

  /**
   * Generate schema.
   */
  private void generateSchema() {
    if (isDebug) {
      logger.debug("Start Constructor");
    }

    if (isDebug) {
      logger.debug("End   Constructor");
    }
  }

}
