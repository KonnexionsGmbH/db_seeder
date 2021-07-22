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

  private String                      tableName;

  /**
   * Get the ADD CONSTRAINT statement.
   *
   * @param tickerSymbol the external ticker symbol
   * @return the ADD CONSTRAINT statement
   */
  public String getAddConstraintStatement(String tickerSymbol) {
    String addConstraintName = quoteConstraintName(tickerSymbol);

    String restoreStatement  = "ALTER TABLE " + tableName + " ADD CONSTRAINT " + addConstraintName;

    switch (constraintType) {
    case "P":
      restoreStatement += " PRIMARY KEY (";
      break;
    case "R":
      restoreStatement += " FOREIGN KEY (";
      break;
    case "U":
      restoreStatement += " UNIQUE (";
    }

    restoreStatement += String.join(",",
                                    columnNames) + ")";

    if ("R".equals(constraintType)) {
      restoreStatement += " REFERENCES " + refTableName + " (" + String.join(",",
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
      restoreStatement += " ENABLE";
      break;
    default:
    }

    return restoreStatement;
  }

  /**
   * Get the DROP CONSTRAINT statement.
   *
   * @param tickerSymbol the external ticker symbol
   * @return the DROP CONSTRAINT statement
   */
  public String getDropConstraintStatement(String tickerSymbol) {
    switch (tickerSymbol) {
    case "mysql":
      String dropStatement = "ALTER TABLE " + tableName + " DROP ";
      switch (constraintType) {
      case "F":
        return dropStatement + "FOREIGN KEY " + quoteConstraintName(tickerSymbol);
      case "P":
        return dropStatement + "PRIMARY KEY";
      default:
        return dropStatement + "INDEX " + quoteConstraintName(tickerSymbol);
      }
    default:
      return "ALTER TABLE " + tableName + " DROP CONSTRAINT " + quoteConstraintName(tickerSymbol);
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

  private String quoteConstraintName(String tickerSymbol) {
    String addConstraintName;

    switch (tickerSymbol) {
    case "cockroach":
    case "derby":
    case "derby_emb":
      addConstraintName = "\"" + constraintName + "\"";
      break;
    default:
      addConstraintName = constraintName;
    }
    return addConstraintName;
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
   * Sets the constraint type.
   *
   * @param constraintType the new constraint type
   */
  public void setConstraintType(String constraintType) {
    this.constraintType = constraintType;
  }

  /**
   * Sets the reference column name.
   *
   * @param refColumnName the new ref column name
   */
  public void setRefColumnName(String refColumnName) {
    this.refColumnNames.add(refColumnName);
  }

  /**
   * Sets the reference table name.
   *
   * @param refTableName the new ref table name
   */
  public void setRefTableName(String refTableName) {
    this.refTableName = refTableName;
  }

  /**
   * Sets the table name.
   *
   * @param tableName the new table name
   */
  public void setTableName(String tableName) {
    this.tableName = tableName;

  }
}
