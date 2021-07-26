package ch.konnexions.db_seeder.jdbc;

import java.util.LinkedHashSet;

// TODO: Auto-generated Javadoc
/**
 * Data Generator for a Database - Managing constraints.
 * <br>
 *
 * @author walter@konnexions.ch
 * @since 2021-07-18
 */
class Constraint {

  private final LinkedHashSet<String> columnNames    = new LinkedHashSet<String>();

  private String                      constraintName;
  private String                      constraintType;
  private final LinkedHashSet<String> refColumnNames = new LinkedHashSet<String>();

  private String                      refTableName;
  private String                      schemaName;

  private String                      tableName;

  private String                      tickerSymbol;

  /**
   * Instantiates a new constraint.
   *
   * @param tickerSymbol the ticker symbol
   * @param schemaName the schema name
   * @param tableName the table name
   * @param constraintType the constraint type
   * @param refTableName the ref table name
   */
  public Constraint(String tickerSymbol, String schemaName, String tableName, String constraintType, String refTableName) {
    super();
    this.tickerSymbol   = tickerSymbol;
    this.schemaName     = schemaName;
    this.tableName      = tableName;
    this.constraintName = null;
    this.constraintType = constraintType;
    this.refTableName   = refTableName;
  }

  /**
   * Get the ADD CONSTRAINT statement.
   *
   * @return the ADD CONSTRAINT statement
   */
  public String getAddConstraintStatement() {
    String addConstraintName = quoteConstraintName();

    String restoreStatement  = "ALTER TABLE " + quoteTableName(tableName) + " ADD CONSTRAINT ";

    switch (tickerSymbol) {
    case "informix":
    case "mariadb":
    case "mysql":
    case "mysql_trino":
    case "percona":
      break;
    default:
      restoreStatement += addConstraintName + " ";
    }

    switch (constraintType) {
    case "P":
      restoreStatement += "PRIMARY KEY (";
      break;
    case "R":
      restoreStatement += "FOREIGN KEY (";
      break;
    case "U":
      restoreStatement += "UNIQUE (";
    }

    restoreStatement += String.join(",",
                                    columnNames) + ")";

    if ("R".equals(constraintType)) {
      restoreStatement += " REFERENCES " + quoteTableName(refTableName) + " (" + String.join(",",
                                                                                             refColumnNames) + ")";
    }

    switch (tickerSymbol) {
    case "cockroach":
    case "postgresql":
    case "postgresql_trino":
      if ("R".equals(constraintType)) {
        restoreStatement += ", VALIDATE CONSTRAINT " + addConstraintName;
      }
      break;
    case "oracle":
    case "oracle_trino":
      restoreStatement += " ENABLE";
      break;
    default:
    }

    return restoreStatement;
  }

  /**
   * @return the columnNames
   */
  public LinkedHashSet<String> getColumnNames() {
    return columnNames;
  }

  /**
   * @return the constraintType
   */
  public String getConstraintType() {
    return constraintType;
  }

  /**
   * Get the DROP CONSTRAINT statement.
   *
   * @return the DROP CONSTRAINT statement
   */
  public String getDropConstraintStatement() {
    String dropStatement;

    switch (tickerSymbol) {
    case "cockroach":
      switch (constraintType) {
      case "U":
        return "DROP INDEX " + quoteConstraintName() + " CASCADE";
      default:
        return "ALTER TABLE " + quoteTableName(tableName) + " DROP CONSTRAINT " + quoteConstraintName();
      }
    case "ibmdb2":
      dropStatement = "ALTER TABLE " + schemaName + "." + quoteTableName(tableName) + " DROP ";
      switch (constraintType) {
      case "R":
        return dropStatement + "FOREIGN KEY " + quoteConstraintName();
      case "P":
        return dropStatement + "PRIMARY KEY";
      default:
        return dropStatement + "UNIQUE " + quoteConstraintName();
      }
    case "mariadb":
    case "mysql":
    case "mysql_trino":
    case "percona":
      dropStatement = "ALTER TABLE " + quoteTableName(tableName) + " DROP ";
      switch (constraintType) {
      case "R":
        return dropStatement + "FOREIGN KEY " + quoteConstraintName();
      case "P":
        return dropStatement + "PRIMARY KEY";
      default:
        return dropStatement + "INDEX " + quoteConstraintName();
      }
    default:
      return "ALTER TABLE " + quoteTableName(tableName) + " DROP CONSTRAINT " + quoteConstraintName();
    }
  }

  /**
   * Gets the table name.
   *
   * @return the table name
   */
  public String getTableName() {
    return tableName;
  }

  private String quoteConstraintName() {
    switch (tickerSymbol) {
    case "cockroach":
    case "derby":
    case "derby_emb":
      return "\"" + constraintName + "\"";
    default:
      return constraintName;
    }
  }

  private String quoteTableName(String tableName) {
    switch (tickerSymbol) {
    case "cubrid":
      return "\"" + tableName + "\"";
    default:
      return tableName;
    }
  }

  /**
   * Sets the column name.
   *
   * @param column the new column name
   */
  public void setColumnName(String column) {
    this.columnNames.add(column);
  }

  /**
   * Sets the constraint name.
   *
   * @param constraintName the new constraint name
   */
  public void setConstraintName(String constraintName) {
    this.constraintName = constraintName;

  }

  /**
   * Sets the reference column name.
   *
   * @param refColumnName the new reference column name
   */
  public void setRefColumnName(String refColumnName) {
    this.refColumnNames.add(refColumnName);
  }
}
