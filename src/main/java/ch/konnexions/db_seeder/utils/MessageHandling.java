package ch.konnexions.db_seeder.utils;

import org.apache.log4j.Logger;

/**
 * Uniform message and program abort handling.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-05-01
 */
public final class MessageHandling {

  private static final int LINE_SIZE = 74;

  /**
   * Abort program.
   *
   * @param logger the logger
   * @param message the message
   */
  public static void abortProgram(Logger logger, String message) {
    logger.info("");
    logger.info("=".repeat(LINE_SIZE));
    logger.info("===> Error: " + message);
    logger.info("");
    logger.info("Program abort");
    logger.info("-".repeat(LINE_SIZE));

    System.exit(1);
  }

  /**
   * Start a new progress message.
   * 
   * @param logger the logger
   * @param message the message
   * 
   */
  public static void startProgress(Logger logger, String message) {
    logger.info("");
    logger.info("=".repeat(74));
    logger.info(message);
    logger.info("");
  }

}
