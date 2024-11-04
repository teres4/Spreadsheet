package xxl.content;

import xxl.SpreadsheetStorage;
import xxl.visits.ContentVisitor;

/** Class representing the location of a cell in a spreadsheet. */
public class Reference extends Content implements Observer {

    /** The row where the cell is located at. */
    private int _row;

    /** The column where the cell is located at. */
    private int _column;

    /*Boolean flag which indicates if the associated cell has suffered any changes. */
    private boolean _uptodate = true;


    /** The spreadsheet storage to which the reference is associated to. */
    private SpreadsheetStorage _memory;
    

    /**
     * Constructor.
     * 
     * @param row
     * @param column
     */
    public Reference(int row, int column){
        _row = row;
        _column = column;
    }

    /**
     * Constructor.
     * 
     * @param row
     * @param column
     * @param memory
     */
    public Reference(int row, int column, SpreadsheetStorage memory){
        _row = row;
        _column = column;
        _memory = memory;
        this.getCellFromRef().addObserver(this); 
    } 

    /**
     * 
     * @return the reference's row.
     */
    public int getRow(){
        return _row;
    }
    
    /**
     * 
     * @return the reference's column.
     */
    public int getColumn(){
        return _column;
    }

    /**
     * @return integer value associated with the reference
     */
    public int getInt() throws ClassCastException {
        LitInteger integer = null;
        try {
            integer = (LitInteger) this.getContent();
        } catch (ClassCastException e){
        }
        if (integer != null)
            return integer.getInt();
        else
            throw new ClassCastException();
    }

    /**public Literal getLiteral() throws ClassCastException {
        LitInteger integer = null;
        try {
            integer = (LitInteger) this.getContent();
        } catch (ClassCastException e){
        }
        if (integer != null)
            return integer.getInt();
        else
            throw new ClassCastException();
    }**/


    public String getString(){
        LitString content = null;
        try {
            content = (LitString) this.getContent();
        } catch (ClassCastException e){
        }
        if (content != null)
            return content.getString();
        else
            throw new ClassCastException();
    }


    @Override
    public void accept(ContentVisitor visitor, boolean start) {
        visitor.visitReference(this, start);
    }

    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o){
        if (o instanceof Reference){
            Reference ref = (Reference) o;
            return _row == ref.getRow() && _column == ref.getColumn();
        }
        return false;
    }


    /**
     * 
     * @return cell associated with the reference.
     */
    public Cell getCellFromRef(){
        Cell cell = _memory.getCellFromRef(this);
        return cell;
    }

    public String toString(){
        return Integer.toString(_row) + ";" + Integer.toString(_column);
    }

    public void update(){
        _uptodate = false;
    }
    

}