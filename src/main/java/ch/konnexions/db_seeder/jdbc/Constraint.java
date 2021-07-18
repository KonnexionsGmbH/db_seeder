package ch.konnexions.db_seeder.jdbc;

import java.util.LinkedHashSet;

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

  public String getDropStatement() {
    return "ALTER TABLE " + tableName + " DROP CONSTRAINT " + constraintName + " CASCADE";
  }

  public String getRestoreStatement() {
    String restoreStatement = "ALTER TABLE " + tableName + " ADD CONSTRAINT " + constraintName;

    switch (constraintType) {
    case "P":
      restoreStatement = restoreStatement + " PRIMARY KEY (";
      break;
    case "R":
      restoreStatement = restoreStatement + " FOREIGN KEY (";
      break;
    case "U":
      restoreStatement = restoreStatement + " UNIQUE (";
    }

    restoreStatement = restoreStatement + String.join(",",
                                                      columnNames) + ")";

    if ("R".equals(constraintType)) {
      restoreStatement = restoreStatement + " REFERENCES " + refTableName + " (" + String.join(",",
                                                                                               refColumnNames) + ")";
    }

    return restoreStatement + " ENABLE";
  }

  public String getTableName() {
    return tableName;
  }

  public void setColumnName(String column) {
    this.columnNames.add(column);
  }

  public void setConstraintName(String constraintName) {
    this.constraintName = constraintName;
  }

  public void setConstraintType(String constraintType) {
    this.constraintType = constraintType;
  }

  public void setRefColumnName(String refColumnName) {
    this.refColumnNames.add(refColumnName);
  }

  public void setRefTableName(String refTableName) {
    this.refTableName = refTableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;

  }
}
