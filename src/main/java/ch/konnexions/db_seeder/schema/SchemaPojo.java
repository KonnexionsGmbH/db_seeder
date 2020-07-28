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
public final class SchemaPojo {

  /*
   *  The globals object.
   */
  @SuppressWarnings("ucd")
  public final class Globals {

    private int defaultNumberOfRows;

    public final int getDefaultNumberOfRows() {
      return defaultNumberOfRows;
    }

    public final void setDefaultNumberOfRows(int defaultNumberOfRows) {
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

        public final String getConstraintType() {
          return constraintType;
        }

        public final String getReferenceColumn() {
          return referenceColumn;
        }

        public final String getReferenceTable() {
          return referenceTable;
        }

        public final void setConstraintType(String constraintType) {
          this.constraintType = constraintType;
        }

        public final void setReferenceColumn(String referenceColumn) {
          this.referenceColumn = referenceColumn;
        }

        public final void setReferenceTable(String referenceTable) {
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

      public final List<ColumnConstraint> getColumnConstraints() {
        return columnConstraints;
      }

      public final String getColumnName() {
        return columnName;
      }

      public final String getDataType() {
        return dataType;
      }

      public final int getDefaultValueInteger() {
        return defaultValueInteger;
      }

      public final String getDefaultValueString() {
        return defaultValueString;
      }

      public final int getLowerRangeInteger() {
        return lowerRangeInteger;
      }

      public final String getLowerRangeString() {
        return lowerRangeString;
      }

      public final int getPrecision() {
        return precision;
      }

      public final int getSize() {
        return size;
      }

      public final int getUpperRangeInteger() {
        return upperRangeInteger;
      }

      public final String getUpperRangeString() {
        return upperRangeString;
      }

      public final List<Integer> getValidValuesInteger() {
        return validValuesInteger;
      }

      public final List<String> getValidValuesString() {
        return validValuesString;
      }

      public final void setColumnName(String columnName) {
        this.columnName = columnName;
      }

      public final void setConstraints(List<ColumnConstraint> columnConstraints) {
        this.columnConstraints = columnConstraints;
      }

      public final void setDataType(String dataType) {
        this.dataType = dataType;
      }

      public final void setDefaultValueInteger(int defaultValueInteger) {
        this.defaultValueInteger = defaultValueInteger;
      }

      public final void setDefaultValueString(String defaultValueString) {
        this.defaultValueString = defaultValueString;
      }

      public final void setLowerRangeInteger(int lowerRangeInteger) {
        this.lowerRangeInteger = lowerRangeInteger;
      }

      public final void setLowerRangeString(String lowerRangeString) {
        this.lowerRangeString = lowerRangeString;
      }

      @SuppressWarnings("ucd")
      public final void setPrecision(int precision) {
        this.precision = precision;
      }

      public final void setSize(int size) {
        this.size = size;
      }

      public final void setUpperRangeInteger(int upperRangeInteger) {
        this.upperRangeInteger = upperRangeInteger;
      }

      public final void setUpperRangeString(String upperRangeString) {
        this.upperRangeString = upperRangeString;
      }

      public final void setValidValuesInteger(List<Integer> validValuesInteger) {
        this.validValuesInteger = validValuesInteger;
      }

      public final void setValidValuesString(List<String> validValuesString) {
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

      public final List<String> getColumns() {
        return columns;
      }

      public final String getConstraintType() {
        return constraintType;
      }

      public final List<String> getReferenceColumns() {
        return referenceColumns;
      }

      public final String getReferenceTable() {
        return referenceTable;
      }

      public final void setColumns(List<String> columns) {
        this.columns = columns;
      }

      public final void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
      }

      public final void setReferenceColumns(List<String> referenceColumns) {
        this.referenceColumns = referenceColumns;
      }

      public final void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
      }
    }

    private ArrayList<Column>     columns          = null;
    private int                   numberOfRows;

    private List<TableConstraint> tableConstraints = null;
    private String                tableName;

    public final ArrayList<Column> getColumns() {
      return columns;
    }

    public final int getNumberOfRows() {
      return numberOfRows;
    }

    public final List<TableConstraint> getTableConstraints() {
      return tableConstraints;
    }

    public final String getTableName() {
      return tableName;
    }

    public final void setColumns(ArrayList<Column> columns) {
      this.columns = columns;
    }

    public final void setNumberOfRows(int numberOfRows) {
      this.numberOfRows = numberOfRows;
    }

    public final void setTableConstraints(List<TableConstraint> tableConstraints) {
      this.tableConstraints = tableConstraints;
    }

    public final void setTableName(String tableName) {
      this.tableName = tableName;
    }
  }

  private Globals    globals;
  private Set<Table> tables = null;

  public final Globals getGlobals() {
    return globals;
  }

  public final Set<Table> getTables() {
    return tables;
  }

  public final void setGlobals(Globals globals) {
    this.globals = globals;
  }

  public final void setTables(Set<Table> tables) {
    this.tables = tables;
  }
}
