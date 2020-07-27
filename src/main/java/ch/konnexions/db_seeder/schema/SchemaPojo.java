package ch.konnexions.db_seeder.schema;

import java.util.ArrayList;
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
  @SuppressWarnings("ucd")
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

      private int                    defaultValueInteger;

      private String                 defaultValueString;

      private int                    lowerRangeInteger;

      private String                 lowerRangeString;

      private int                    precision;

      private int                    size;

      private int                    upperRangeInteger;

      private String                 upperRangeString;

      private List<Integer>          validValuesInteger;

      private List<String>           validValuesString;

      public List<ColumnConstraint> getColumnConstraints() {
        return columnConstraints;
      }

      public String getColumnName() {
        return columnName;
      }

      public String getDataType() {
        return dataType;
      }

      public int getDefaultValueInteger() {
        return defaultValueInteger;
      }

      public String getDefaultValueString() {
        return defaultValueString;
      }

      public int getLowerRangeInteger() {
        return lowerRangeInteger;
      }

      public String getLowerRangeString() {
        return lowerRangeString;
      }

      public int getPrecision() {
        return precision;
      }

      public int getSize() {
        return size;
      }

      public int getUpperRangeInteger() {
        return upperRangeInteger;
      }

      public String getUpperRangeString() {
        return upperRangeString;
      }

      public List<Integer> getValidValuesInteger() {
        return validValuesInteger;
      }

      public List<String> getValidValuesString() {
        return validValuesString;
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

      public void setDefaultValueInteger(int defaultValueInteger) {
        this.defaultValueInteger = defaultValueInteger;
      }

      public void setDefaultValueString(String defaultValueString) {
        this.defaultValueString = defaultValueString;
      }

      public void setLowerRangeInteger(int lowerRangeInteger) {
        this.lowerRangeInteger = lowerRangeInteger;
      }

      public void setLowerRangeString(String lowerRangeString) {
        this.lowerRangeString = lowerRangeString;
      }

      @SuppressWarnings("ucd")
      public void setPrecision(int precision) {
        this.precision = precision;
      }

      public void setSize(int size) {
        this.size = size;
      }

      public void setUpperRangeInteger(int upperRangeInteger) {
        this.upperRangeInteger = upperRangeInteger;
      }

      public void setUpperRangeString(String upperRangeString) {
        this.upperRangeString = upperRangeString;
      }

      public void setValidValuesInteger(List<Integer> validValuesInteger) {
        this.validValuesInteger = validValuesInteger;
      }

      public void setValidValuesString(List<String> validValuesString) {
        this.validValuesString = validValuesString;
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

    private ArrayList<Column>     columns          = null;
    private int                   numberOfRows;

    private List<TableConstraint> tableConstraints = null;
    private String                tableName;

    public ArrayList<Column> getColumns() {
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

    public void setColumns(ArrayList<Column> columns) {
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
