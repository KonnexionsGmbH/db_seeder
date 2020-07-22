/**
 * 
 */
package ch.konnexions.db_seeder;

import java.io.File;

import org.apache.log4j.Logger;

import ch.konnexions.db_seeder.generator.GenerateSchema;
import ch.konnexions.db_seeder.utils.Config;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Test Data Generator for a Database - Application.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-07-15
 */
@SuppressWarnings("ucd")
public class SchemaBuilder {

  @SuppressWarnings("ucd")
  private static final Logger logger = Logger.getLogger(SchemaBuilder.class);

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    logger.info("Start");

    String release = "";

    if (args.length > 0) {
      release = args[0];
    }

    logger.info("args[0]='" + release + "'");

    if (null == release) {
      MessageHandling.abortProgram(logger,
                                   "Command line argument 'DB_SEEDER_RELEASE' missing (null)");
    }

    String fileJsonName = new Config().getFileJsonName();

    File   fileJson     = new File(fileJsonName);

    if (!fileJson.exists()) {
      MessageHandling.abortProgram(logger,
                                   "File '" + fileJsonName + "' is not existing");
    }

    if (fileJson.isDirectory()) {
      MessageHandling.abortProgram(logger,
                                   "'" + fileJsonName + "' is a directory not a file");
    }

    new GenerateSchema().generateSchema(release,
                                        fileJsonName);

    logger.info("End");

    System.exit(0);
  }
}
