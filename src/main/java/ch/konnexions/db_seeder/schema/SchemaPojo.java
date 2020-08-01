package ch.konnexions.db_seeder.schema;

import java.util.ArrayList;
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
  public static final class Globals {

    private Integer defaultNumberOfRows;
    private boolean encodingISO_8859_1;

    private boolean encodingUTF_8;

    private Integer nullFactor;

    public final Integer getDefaultNumberOfRows() {
      return defaultNumberOfRows;
    }

    public Integer getNullFactor() {
      return nullFactor;
    }

    public boolean isEncodingISO_8859_1() {
      return encodingISO_8859_1;
    }

    public boolean isEncodingUTF_8() {
      return encodingUTF_8;
    }

    public final void setDefaultNumberOfRows(Integer defaultNumberOfRows) {
      this.defaultNumberOfRows = defaultNumberOfRows;
    }

    public void setEncodingISO_8859_1(boolean encodingISO_8859_1) {
      this.encodingISO_8859_1 = encodingISO_8859_1;
    }

    public void setEncodingUTF_8(boolean encodingUTF_8) {
      this.encodingUTF_8 = encodingUTF_8;
    }

    public void setNullFactor(Integer nullFactor) {
      this.nullFactor = nullFactor;
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
      public static class References {

        private String referenceColumn;
        private String referenceTable;

        public final String getReferenceColumn() {
          return referenceColumn;
        }

        public final String getReferenceTable() {
          return referenceTable;
        }

        public final void setReferenceColumn(String referenceColumn) {
          this.referenceColumn = referenceColumn;
        }

        public final void setReferenceTable(String referenceTable) {
          this.referenceTable = referenceTable;
        }
      }

      private String                columnName;

      private String                dataType;
      private Integer               defaultValueInteger;
      private String                defaultValueString;

      private Integer               lowerRangeInteger;
      private String                lowerRangeString;

      private boolean               notNull;

      private Integer               precision;
      private boolean               primaryKey;

      private ArrayList<References> references = null;

      private Integer               size;

      private boolean               unique;
      private Integer               upperRangeInteger;
      private String                upperRangeString;

      private ArrayList<Integer>    validValuesInteger;

      private ArrayList<String>     validValuesString;

      public final String getColumnName() {
        return columnName;
      }

      public final String getDataType() {
        return dataType;
      }

      public final Integer getDefaultValueInteger() {
        return defaultValueInteger;
      }

      public final String getDefaultValueString() {
        return defaultValueString;
      }

      public final Integer getLowerRangeInteger() {
        return lowerRangeInteger;
      }

      public final String getLowerRangeString() {
        return lowerRangeString;
      }

      public final Integer getPrecision() {
        return precision;
      }

      public final ArrayList<References> getReferences() {
        return references;
      }

      public final Integer getSize() {
        return size;
      }

      public final Integer getUpperRangeInteger() {
        return upperRangeInteger;
      }

      public final String getUpperRangeString() {
        return upperRangeString;
      }

      public final ArrayList<Integer> getValidValuesInteger() {
        return validValuesInteger;
      }

      public final ArrayList<String> getValidValuesString() {
        return validValuesString;
      }

      public boolean isNotNull() {
        return notNull;
      }

      public boolean isPrimaryKey() {
        return primaryKey;
      }

      public boolean isUnique() {
        return unique;
      }

      public final void setColumnName(String columnName) {
        this.columnName = columnName;
      }

      public final void setDataType(String dataType) {
        this.dataType = dataType;
      }

      public final void setDefaultValueInteger(Integer defaultValueInteger) {
        this.defaultValueInteger = defaultValueInteger;
      }

      public final void setDefaultValueString(String defaultValueString) {
        this.defaultValueString = defaultValueString;
      }

      public final void setLowerRangeInteger(Integer lowerRangeInteger) {
        this.lowerRangeInteger = lowerRangeInteger;
      }

      public final void setLowerRangeString(String lowerRangeString) {
        this.lowerRangeString = lowerRangeString;
      }

      public void setNotNull(boolean notNull) {
        this.notNull = notNull;
      }

      @SuppressWarnings("ucd")
      public final void setPrecision(Integer precision) {
        this.precision = precision;
      }

      public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
      }

      public final void setReferences(ArrayList<References> references) {
        this.references = references;
      }

      public final void setSize(Integer size) {
        this.size = size;
      }

      public void setUnique(boolean unique) {
        this.unique = unique;
      }

      public final void setUpperRangeInteger(Integer upperRangeInteger) {
        this.upperRangeInteger = upperRangeInteger;
      }

      public final void setUpperRangeString(String upperRangeString) {
        this.upperRangeString = upperRangeString;
      }

      public final void setValidValuesInteger(ArrayList<Integer> validValuesInteger) {
        this.validValuesInteger = validValuesInteger;
      }

      public final void setValidValuesString(ArrayList<String> validValuesString) {
        this.validValuesString = validValuesString;
      }
    }

    /*
     *  The table constraint object.
     */
    public static class TableConstraint {

      private ArrayList<String> columns          = null;
      private String            constraintType;

      private ArrayList<String> referenceColumns = null;
      private String            referenceTable;

      public final ArrayList<String> getColumns() {
        return columns;
      }

      public final String getConstraintType() {
        return constraintType;
      }

      public final ArrayList<String> getReferenceColumns() {
        return referenceColumns;
      }

      public final String getReferenceTable() {
        return referenceTable;
      }

      public final void setColumns(ArrayList<String> columns) {
        this.columns = columns;
      }

      public final void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
      }

      public final void setReferenceColumns(ArrayList<String> referenceColumns) {
        this.referenceColumns = referenceColumns;
      }

      public final void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
      }
    }

    private ArrayList<Column>          columns          = null;
    private Integer                    numberOfRows;

    private ArrayList<TableConstraint> tableConstraints = null;
    private String                     tableName;

    public final ArrayList<Column> getColumns() {
      return columns;
    }

    public final Integer getNumberOfRows() {
      return numberOfRows;
    }

    public final ArrayList<TableConstraint> getTableConstraints() {
      return tableConstraints;
    }

    public final String getTableName() {
      return tableName;
    }

    public final void setColumns(ArrayList<Column> columns) {
      this.columns = columns;
    }

    public final void setNumberOfRows(Integer numberOfRows) {
      this.numberOfRows = numberOfRows;
    }

    public final void setTableConstraints(ArrayList<TableConstraint> tableConstraints) {
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
