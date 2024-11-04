package xxl.content;

import xxl.SpreadsheetStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/** Class representing an interval of cells. */
public class Interval implements Serializable {

    /** Start row of the interval. */
    private int _startRow;

    /** End row of the interval. */
    private int _endRow;

    /** Start column of the interval. */
    private int _startColumn;

    /** End column of the interval. */
    private int _endColumn;

    /** The spreadsheet storage associated with this interval. */
    private SpreadsheetStorage _storage;
    

    /**
     * Constructor.
     * 
     * @param startRow the start row of the interval
     * @param startColumn the start column of the interval
     * @param endRow the end row of the interval
     * @param endColumn the end column of the interval
     * 
     */
    public Interval(int startRow, int startColumn, int endRow, int endColumn) {
        _startRow = startRow;
        _startColumn = startColumn;
        _endRow = endRow;
        _endColumn = endColumn;
    }

    /**
     * Returns the spreadsheet storage associated with the interval's 
     * spreadsheet.
     * 
     * @return the spreadsheet storage of the spreadsheet.
     */
    public SpreadsheetStorage getSpreadsheetStorage() {
        return _storage;
    }


    /**
     * 
     * @param spreadsheet the new spreadsheet to be set.
     */
    public void setSpreadsheetStorage(SpreadsheetStorage storage){
        _storage = storage;
    }

    /**
     * Checks if the interval is within the same row.
     * 
     * @return true, if the interval is within the same row.
     */
    public boolean sameRow() {
        return _startRow == _endRow;
    }

    /**
     * Checks if the interval is within the same column.
     * 
     * @return true, if the interval is within the same column.
     */
    public boolean sameColumn() {
        return _startColumn == _endColumn;
    }

    /**
     * Checks if the interval is valid.
     * 
     * @return true, if the interval is valid.
     */
    public boolean isValidInterval() {
        return sameRow() || sameColumn();
    }

    /**
     * Gets the Reference where the interval starts.
     * @return start reference of the interval.
     */
    public Reference getStartReference(){
        return new Reference(_startRow, _startColumn);
    }

    /**
     * Gets the Reference where the interval ends.
     * @return end reference of the interval.
     */
    public Reference getEndReference(){
        return new Reference(_endRow, _endColumn);
    }


    /**
     * Gets the cells that are in the interval.
     * @return list of the cells.
     */
    public List<Cell> getCells(){
        ArrayList<Cell> rangeCells = new ArrayList<Cell>(); 
        if (this.sameRow()){ 
            for (int i = _startColumn ; i <= _endColumn; i++){
                Reference ref = new Reference(_startRow, i, _storage);
                rangeCells.add(_storage.getCellFromRef(ref));
            }
        }
        else if (this.sameColumn()) {
            for (int i = _startRow ; i <= _endRow; i++){
                Reference ref = new Reference(i, _startColumn, _storage);
                rangeCells.add(_storage.getCellFromRef(ref));
            }
        }
        return rangeCells;

    }


    /**
     * Gets the references of the cells that are in the interval.
     * @return list of references.
     */
    public List<Reference> listCellsInInterval(){
        List<Reference> cells = new ArrayList<>(); 

        if (this.sameRow()){ 
            for (int i = _startColumn ; i <= _endColumn; i++){
                Reference ref = new Reference(_startRow, i);
                cells.add(ref);
            }
        }
        else if (this.sameColumn()) {
            for (int i = _startRow ; i <= _endRow; i++){
                Reference ref = new Reference(i, _startColumn);
                cells.add(ref);
            }
        }
        return cells;
    }

    /**
     * Gets the string (associated with the reference) and the respective cell.
     * @return map with strings and cells
     */
    public Map<String, Cell> getCellsInInterval() {
        Map<String, Cell> cells = new TreeMap<String, Cell>(); 

        if (this.sameRow()){ 
            for (int i = _startColumn ; i <= _endColumn; i++){
                Reference ref = new Reference(_startRow, i);
                cells.put(ref.toString(), _storage.getCellFromRef(ref));
            }
        }
        else if (this.sameColumn()) {
            for (int i = _startRow ; i <= _endRow; i++){
                Reference ref = new Reference(i, _startColumn, _storage);
                cells.put(ref.toString(), _storage.getCellFromRef(ref));
            }
        }
        return cells;

    }

    @Override
    public String toString(){
        return Integer.toString(_startRow) + ";"  + Integer.toString(_startColumn)
        + ":" + Integer.toString(_endRow) + ";" + Integer.toString(_endColumn);
    }


}