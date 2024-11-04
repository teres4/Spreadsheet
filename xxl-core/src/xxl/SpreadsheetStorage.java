package xxl;

import java.util.Map;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import xxl.content.Reference;
import xxl.content.Cell;
import xxl.content.Empty;
import xxl.content.Content;


/**Class holding and managing the storage of cells in the spreadsheet */
public class SpreadsheetStorage implements Serializable {
    
	/** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202308312359L;

    /**Cells in the Spreadsheet */
    private Map<String, Cell> _memory;

    /** Spreadsheet's number of rows. */
    private int _rows;

    /** Spreadsheet's number of columns. */
    private int _columns;

    /**
     * Constructor
     */
    public SpreadsheetStorage(){
        _memory = new HashMap<String, Cell>();
    }   

    /**
     * Sets the dimensions of the spreadsheet.
     * @param rows number of rows of the spreadsheet.
     * @param columns number of columns of the spreadsheet.
     */
    public void setDimensions(int rows, int columns){
        _rows = rows;
        _columns = columns;
    }

    /**
     * Gets the number of cells with content in the spreadsheet.
     * @return number of cells with content.
     */
    public int getSize(){
        return _memory.size();
    }
    
    public int getRows(){
        return _rows;
    }

    public int getColumns(){
        return _columns;
    }

    /**
     * Gets the strings associated with the references of the spreadsheet.
     * @return a Collection that contains all the references' strings.
     */
    public Collection<String> getReferences(){
        return _memory.keySet();
    }
    
    /**
     * Gets all the cells in the spreadsheet.
     * @return a Collection that contains all the spreadsheet's cells.
     */
    public Collection<Cell> getCells(){
        return _memory.values();
    }

    /**
     * Gets all the content associated with each cell from the spreadsheet.
     * @return a list with the content from all the cells in the spreadsheet.
     */
    public List<Content> getListOfContent(){
        List<Content> listContent = new ArrayList<Content>();
        for (Cell cell : this.getCells()){
            listContent.add(cell.getContent());
        }
        return listContent;
    }

    /**
     * Checks if given row and column are within the spreadsheet's dimensions.
     * 
     * @param row  the given row to check
     * @param column the given column to check
     * @return true, if they are within the spreadsheet's dimensions.
     */
    public boolean inSpreadsheetBoundaries(int row, int column){
        if (row > 0 && row <= _rows && column > 0 && column <= _columns)
            return true;
        return false;
    }

    /**
     * 
     * @param ref
     * @param cell
     */
    public void putCell(Reference ref, Cell cell){
        _memory.put(ref.toString(), cell);
    }

    /**
     * 
     * @param ref
     * @return Cell associated with the reference
     */
    public Cell getCellFromRef(Reference ref){
        Cell cell =  _memory.get(ref.toString());
        if (cell == null){
            cell = new Cell(new Empty());
            this.putCell(ref, cell);
        }
        return cell;
    }
}