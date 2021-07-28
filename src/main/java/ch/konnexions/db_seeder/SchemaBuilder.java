package ch.konnexions.db_seeder;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.konnexions.db_seeder.generator.GenerateSchema;
import ch.konnexions.db_seeder.utils.Config;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Data Generator for a Database - Generate schema-related source code.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-07-15
 */
public final class SchemaBuilder {

  private static final Logger logger = LogManager.getLogger(SchemaBuilder.class);

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
                                   "Program abort: command line argument 'DB_SEEDER_RELEASE' missing (null)");
    }

    String fileJsonName = new Config().getFileJsonName();

    File   fileJson     = new File(fileJsonName);

    if (!fileJson.exists()) {
      MessageHandling.abortProgram(logger,
                                   "Program abort: file '" + fileJsonName + "' is not existing");
    }

    if (fileJson.isDirectory()) {
      MessageHandling.abortProgram(logger,
                                   "Program abort: '" + fileJsonName + "' is a directory not a file");
    }

    new GenerateSchema().generateSchema(release,
                                        fileJsonName);

    logger.info("End");

    System.exit(0);
  }

  final boolean isDebug = logger.isDebugEnabled();
}
