/**
 * 
 */
package ch.konnexions.db_seeder.generator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import ch.konnexions.db_seeder.schema.SchemaPojo;
import ch.konnexions.db_seeder.schema.SchemaPojo.Table;
import ch.konnexions.db_seeder.schema.SchemaPojo.Table.Column;
import ch.konnexions.db_seeder.schema.SchemaPojo.Table.Column.ColumnConstraint;
import ch.konnexions.db_seeder.schema.SchemaPojo.Table.TableConstraint;
import ch.konnexions.db_seeder.utils.AbstractDatabaseSeeder.DbmsEnum;
import ch.konnexions.db_seeder.utils.MessageHandling;

/**
 * Test Data Generator for a Database - Transform JSON to POJO.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-07-15
 */
public class GenerateSchema {

  private static final Logger              logger  = Logger.getLogger(GenerateSchema.class);

  private int                              errors  = 0;

  private final boolean                    isDebug = logger.isDebugEnabled();

  private HashSet<String>                  valColumnNames;
  private List<Table>                      valTables;
  private HashMap<String, HashSet<String>> valTablesColumns;

  private void generateClassBaseSchema(String release, SchemaPojo schemaPojo) {
  }

  private void generateClassDbmsSchema(String release, SchemaPojo schemaPojo, DbmsEnum ticketSymbol) {
  }

  /**
   * Generate the Java classes.
   *
   * @param release the current release number
   * @param schemaPojo the schema POJO
   */
  private void generateClasses(String release, SchemaPojo schemaPojo) {

    generateClassBaseSchema(release,
                            schemaPojo);

    for (DbmsEnum ticketSymbol : DbmsEnum.values()) {
      generateClassDbmsSchema(release,
                              schemaPojo,
                              ticketSymbol);
    }

  }

  /**
  * Test Data Generator for a Database - Generate a complete schema.
   *
   * @param release the release identification
   * @param fileJsonName the name of the db_seeder schema definition file
   */
  public void generateSchema(String release, String fileJsonName) {
    if (isDebug) {
      logger.debug("Start");
    }

    String     fileSchemaName = "src/main/resources/db_seeder_schema.json";

    JSONObject jsonSchema     = null;

    try {
      jsonSchema = new JSONObject(new JSONTokener(new FileInputStream(fileSchemaName)));
    } catch (JSONException e) {
      e.printStackTrace();
      System.exit(1);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    JSONObject jsonSubject = null;

    try {
      jsonSubject = new JSONObject(new JSONTokener(new FileInputStream(fileJsonName)));
    } catch (JSONException e) {
      e.printStackTrace();
      System.exit(1);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    validateJson(jsonSchema,
                 jsonSubject);

    SchemaPojo schemaPojo = transformJson2Pojo(fileJsonName);

    validateSchema(schemaPojo);

    generateClasses(release,
                    schemaPojo);

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Transform the schema in JSON format to POJO.
   *
   * @param fileJsonName the file name of the JSON schema file
   * @return the schema POJO
   */
  private SchemaPojo transformJson2Pojo(String fileJsonName) {
    if (isDebug) {
      logger.debug("Start");
    }

    MessageHandling.startProgress(logger,
                                  "Start transforming JSON to a POJO");

    Gson       gson       = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    SchemaPojo schemaPojo = null;

    try {
      schemaPojo = gson.fromJson(Files.newBufferedReader(Paths.get(fileJsonName)),
                                 SchemaPojo.class);
    } catch (JsonSyntaxException e) {
      MessageHandling.abortProgram(logger,
                                   e.getMessage());
    } catch (JsonIOException e) {
      MessageHandling.abortProgram(logger,
                                   e.getMessage());
    } catch (IOException e) {
      MessageHandling.abortProgram(logger,
                                   e.getMessage());
    }

    logger.info("===> The JSON to POJO transformation was successful.");
    logger.info("-".repeat(74));

    if (isDebug) {
      logger.debug("End");
    }

    return schemaPojo;
  }

  /**
   * Validate the JSON format of the schema definition.
   * 
   * @param jsonSchema the JSON schema definition
   * @param jsonSubject the db_seeder schema definition
   */
  private void validateJson(JSONObject jsonSchema, JSONObject jsonSubject) {
    if (isDebug) {
      logger.debug("Start");
    }

    MessageHandling.startProgress(logger,
                                  "Start JSON validation");

    Schema schema = SchemaLoader.load(jsonSchema);

    try {
      schema.validate(jsonSubject);
    } catch (ValidationException e) {
      MessageHandling.abortProgram(logger,
                                   e.getMessage());
    }

    logger.info("===> The JSON validation was successful.");
    logger.info("-".repeat(74));

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Validate the POJO format of the schema definition.
   *
   * @param schemaPojo the schema POJO
   */
  private void validateSchema(SchemaPojo schemaPojo) {
    if (isDebug) {
      logger.debug("Start");
    }

    MessageHandling.startProgress(logger,
                                  "Start validating the schema definition");

    valTables        = schemaPojo.getTables();
    valTablesColumns = new HashMap<String, HashSet<String>>();

    if (valTables == null || valTables.size() == 0) {
      logger.error("No definitions found for database valTableNames");
      errors++;
    } else {

      String tableName;

      for (Table table : valTables) {
        tableName = table.getTableName().toUpperCase();

        if (valTablesColumns.containsKey(tableName)) {
          logger.error("Database table: '" + tableName + "' - This database table was defined more than once");
          errors++;
          continue;
        }

        List<Column> columns = table.getColumns();

        if (columns == null || columns.size() == 0) {
          logger.error("Database table: '" + tableName + "' - No definitions found for table columns");
          errors++;
          continue;
        }

        valColumnNames = validateSchemaColumns(tableName,
                                               columns);

        if (errors == 0) {
          valTablesColumns.put(tableName,
                               valColumnNames);
        }
      }

      if (errors == 0) {
        validateSchemaTableConstraints();

        validateSchemaColumnConstraints();
      }
    }

    if (errors > 0) {
      MessageHandling.abortProgram(logger,
                                   "Program abort: " + errors + " error(s) occurred during the schema validation.");
    }

    logger.info("===> The schema validation was successful.");
    logger.info("-".repeat(74));

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Validate the column constraint definitions.
   */
  private void validateSchemaColumnConstraints() {
    if (isDebug) {
      logger.debug("Start");
    }

    List<Column>           columns;
    List<ColumnConstraint> columnConstraints;
    String                 columnName;
    String                 constraintType;
    String                 referenceColumn;
    String                 referenceTable;
    String                 tableName;

    HashSet<String>        valRefrenceColumnNames;

    for (Table table : valTables) {
      tableName = table.getTableName().toUpperCase();

      columns   = table.getColumns();

      for (Column column : columns) {
        columnConstraints = column.getColumnConstraints();

        if (columnConstraints == null || columnConstraints.size() == 0) {
          continue;
        }

        columnName = column.getColumnName();

        for (ColumnConstraint columnConstraint : columnConstraints) {
          constraintType = columnConstraint.getConstraintType().toUpperCase();

          if (constraintType == null) {
            logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Constraint type is missing (null)");
            errors++;
            continue;
          }

          referenceColumn = columnConstraint.getReferenceColumn();
          referenceTable  = columnConstraint.getReferenceTable();

          switch (constraintType) {
          case "FOREIGN":
            if (referenceTable == null) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Constraint type '" + constraintType
                  + "' requires a reference table");
              errors++;
            }

            if (tableName.equals(referenceTable)) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' constraint type: '" + constraintType
                  + "' - Reference table must be a different database table");
              errors++;
            }

            if (!(valTablesColumns.containsKey(referenceTable))) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' constraint type: '" + constraintType + "' - Reference table '"
                  + referenceTable + "' is not defined");
              errors++;
            }

            if (referenceColumn == null) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Constraint type '" + constraintType
                  + "' requires a reference column");
              errors++;
            }

            valRefrenceColumnNames = valTablesColumns.get(referenceTable);

            if (!valRefrenceColumnNames.contains(referenceColumn.toUpperCase())) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' constraint type: '" + constraintType + "' - Reference column '"
                  + referenceColumn.toUpperCase() + "' is not defined in database table '" + referenceTable + "'");
              errors++;
            }

            break;
          case "PRIMARY":
          case "UNIQUE":
            if (referenceTable != null) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Constraint type '" + constraintType
                  + "' does not allow a reference table");
              errors++;
            }

            if (referenceColumn != null) {
              logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Constraint type '" + constraintType
                  + "' does not allow a reference column");
              errors++;
            }

            break;
          default:
            logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Unknown constraint type '" + constraintType + "'");
            errors++;
          }
        }
      }
    }

    if (isDebug) {
      logger.debug("End");
    }
  }

  /**
   * Validate the column definitions of a specific database table.
   *
   * @param tableName the name of the database table
   * @param columns the database table columns columns
   * 
   * @return the hash set containing the column names
   */
  private HashSet<String> validateSchemaColumns(String tableName, List<Column> columns) {
    if (isDebug) {
      logger.debug("Start");
    }

    HashSet<String> columnNames = new HashSet<String>();

    String          columnName;
    String          dataType;
    Integer         precision;
    Integer         size;

    for (Column column : columns) {
      columnName = column.getColumnName().toUpperCase();

      if (!columnNames.add(columnName)) {
        logger.error("Database table: '" + tableName + "' column: '" + columnName
            + "' - This database column was defined more than once in this database table");
        errors++;
        continue;
      }

      dataType  = column.getDataType().toUpperCase();
      size      = column.getSize();
      precision = column.getPrecision();

      if (dataType == null) {
        logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Data type is missing (null)");
        errors++;
        continue;
      }

      switch (dataType) {
      case "BIGINT":
      case "BLOB":
      case "CLOB":
      case "TIMESTAMP":
        if (size != null) {
          logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Data type '" + dataType + "' does not allow a value for size ("
              + size + ")");
          errors++;
        }
        if (precision != null) {
          logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Data type '" + dataType + "' does not allow a value for precision ("
              + precision + ")");
          errors++;
        }
        break;
      case "VARCHAR":
        if (size == null) {
          logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Size is missing (null)");
          errors++;
        }
        if (size.intValue() < 1 || size.intValue() > 4000) {
          logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Size must have a value between 1 and 4000");
          errors++;
        }
        if (precision != null) {
          logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Data type '" + dataType + "' does not allow a value for precision ("
              + precision + ")");
          errors++;
        }
        break;
      default:
        logger.error("Database table: '" + tableName + "' column: '" + columnName + "' - Unknown data type '" + dataType + "'");
        errors++;
      }
    }

    if (isDebug) {
      logger.debug("End");
    }

    return columnNames;
  }

  /**
   * Validate table constraint definitions.
   */
  private void validateSchemaTableConstraints() {
    if (isDebug) {
      logger.debug("Start");
    }

    List<String>          columns;
    String                constraintType;

    List<String>          referenceColumns;
    String                referenceTable;
    String                referenceTableUpper;
    HashSet<String>       valRefrenceColumnNames;

    HashSet<String>       valColumnNames;
    List<TableConstraint> tableConstraints;
    String                tableName;

    for (Table table : valTables) {
      tableName        = table.getTableName().toUpperCase();

      tableConstraints = table.getTableConstraints();

      if (tableConstraints == null || tableConstraints.size() == 0) {
        continue;
      }

      for (TableConstraint tableConstraint : tableConstraints) {
        constraintType = tableConstraint.getConstraintType().toUpperCase();

        if (constraintType == null) {
          logger.error("Database table: '" + tableName + "' - Data type is missing (null)");
          errors++;
          continue;
        }

        columns = tableConstraint.getColumns();

        if (columns == null || columns.size() == 0) {
          logger.error("Database table: '" + tableName + "' - Columns missing (null)");
          errors++;
        }

        valColumnNames = valTablesColumns.get(tableName);

        for (String column : columns) {
          if (!valColumnNames.contains(column.toUpperCase())) {
            logger.error("Database table: '" + tableName + "' constraint type: '" + constraintType + "' - Column '" + column.toUpperCase()
                + "' is not defined in database table '" + tableName + "'");
            errors++;
          }
        }

        referenceColumns = tableConstraint.getReferenceColumns();
        referenceTable   = tableConstraint.getReferenceTable();

        switch (constraintType) {
        case "FOREIGN":
          if (referenceTable == null) {
            logger.error("Database table: '" + tableName + "' - Constraint type '" + constraintType + "' requires a reference table");
            errors++;
          }

          referenceTableUpper = referenceTable.toUpperCase();

          if (tableName.equals(referenceTableUpper.toUpperCase())) {
            logger.error("Database table: '" + tableName + "' constraint type: '" + constraintType + "' - Reference table must be a different database table");
            errors++;
          }

          if (!(valTablesColumns.containsKey(referenceTableUpper))) {
            logger.error("Database table: '" + tableName + "' constraint type: '" + constraintType + "' - Reference table '" + referenceTableUpper
                + "' is not defined");
            errors++;
          }

          if (referenceColumns == null || referenceColumns.size() == 0) {
            logger.error("Database table: '" + tableName + "' - Constraint type '" + constraintType + "' requires reference column(s)");
            errors++;
          }

          valRefrenceColumnNames = valTablesColumns.get(referenceTableUpper);

          for (String referenceColumn : referenceColumns) {
            if (!valRefrenceColumnNames.contains(referenceColumn.toUpperCase())) {
              logger.error("Database table: '" + tableName + "' constraint type: '" + constraintType + "' - Reference column '" + referenceColumn.toUpperCase()
                  + "' is not defined in database table '" + referenceTableUpper + "'");
              errors++;
            }
          }

          break;
        case "PRIMARY":
        case "UNIQUE":
          if (referenceTable != null) {
            logger.error("Database table: '" + tableName + "' - Constraint type '" + constraintType + "' does not allow a reference table");
            errors++;
          }

          if (referenceColumns != null && referenceColumns.size() > 0) {
            logger.error("Database table: '" + tableName + "' - Constraint type '" + constraintType + "' does not allow reference column(s)");
            errors++;
          }

          break;
        default:
          logger.error("Database table: '" + tableName + "' - Unknown constraint type '" + constraintType + "'");
          errors++;
        }
      }
    }

    if (isDebug) {
      logger.debug("End");
    }
  }
}
