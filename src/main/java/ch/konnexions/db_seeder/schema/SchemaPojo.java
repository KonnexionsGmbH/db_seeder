package ch.konnexions.db_seeder.schema;

import java.util.List;
import java.util.Set;

/**
 * Test Data Generator for a Database - Complete schema POJO.
 * <br>
 * @author  walter@konnexions.ch
 * @since   2020-07-15
 */
public class SchemaPojo {

  /*
   *  The globals object.
   */
  public class Globals {

    private int defaultNumberOfRows;

    public int getDefaultNumberOfRows() {
      return defaultNumberOfRows;
    }

    public void setDefaultNumberOfRows(int defaultNumberOfRows) {
      this.defaultNumberOfRows = defaultNumberOfRows;
    }
  }

  /*
   *  The table object.
   */
  @SuppressWarnings("ucd")
  public static class Table {

    /*
     *  The column object.
     */
    @SuppressWarnings("ucd")
    public static class Column {

      /*
       *  The column constraint object.
       */
      public static class ColumnConstraint {

        private String constraintType;

        private String referenceColumn;
        private String referenceTable;

        public String getConstraintType() {
          return constraintType;
        }

        public String getReferenceColumn() {
          return referenceColumn;
        }

        public String getReferenceTable() {
          return referenceTable;
        }

        public void setConstraintType(String constraintType) {
          this.constraintType = constraintType;
        }

        public void setReferenceColumn(String referenceColumn) {
          this.referenceColumn = referenceColumn;
        }

        public void setReferenceTable(String referenceTable) {
          this.referenceTable = referenceTable;
        }
      }

      private List<ColumnConstraint> columnConstraints = null;
      private String                 columnName;

      private String                 dataType;

      private int                    precision;

      private int                    size;

      public List<ColumnConstraint> getColumnConstraints() {
        return columnConstraints;
      }

      public String getColumnName() {
        return columnName;
      }

      public String getDataType() {
        return dataType;
      }

      public int getPrecision() {
        return precision;
      }

      public int getSize() {
        return size;
      }

      public void setColumnName(String columnName) {
        this.columnName = columnName;
      }

      public void setConstraints(List<ColumnConstraint> columnConstraints) {
        this.columnConstraints = columnConstraints;
      }

      public void setDataType(String dataType) {
        this.dataType = dataType;
      }

      @SuppressWarnings("ucd")
      public void setPrecision(int precision) {
        this.precision = precision;
      }

      public void setSize(int size) {
        this.size = size;
      }
    }

    /*
     *  The table constraint object.
     */
    public static class TableConstraint {

      private List<String> columns          = null;
      private String       constraintType;

      private List<String> referenceColumns = null;
      private String       referenceTable;

      public List<String> getColumns() {
        return columns;
      }

      public String getConstraintType() {
        return constraintType;
      }

      public List<String> getReferenceColumns() {
        return referenceColumns;
      }

      public String getReferenceTable() {
        return referenceTable;
      }

      public void setColumns(List<String> columns) {
        this.columns = columns;
      }

      public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
      }

      public void setReferenceColumns(List<String> referenceColumns) {
        this.referenceColumns = referenceColumns;
      }

      public void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
      }
    }

    private List<Column>          columns          = null;
    private int                   numberOfRows;

    private List<TableConstraint> tableConstraints = null;
    private String                tableName;

    public List<Column> getColumns() {
      return columns;
    }

    public int getNumberOfRows() {
      return numberOfRows;
    }

    public List<TableConstraint> getTableConstraints() {
      return tableConstraints;
    }

    public String getTableName() {
      return tableName;
    }

    public void setColumns(List<Column> columns) {
      this.columns = columns;
    }

    public void setNumberOfRows(int numberOfRows) {
      this.numberOfRows = numberOfRows;
    }

    public void setTableConstraints(List<TableConstraint> tableConstraints) {
      this.tableConstraints = tableConstraints;
    }

    public void setTableName(String tableName) {
      this.tableName = tableName;
    }
  }

  private Globals    globals;
  private Set<Table> tables = null;

  public Globals getGlobals() {
    return globals;
  }

  public Set<Table> getTables() {
    return tables;
  }

  public void setGlobals(Globals globals) {
    this.globals = globals;
  }

  public void setTables(Set<Table> tables) {
    this.tables = tables;
  }
}
