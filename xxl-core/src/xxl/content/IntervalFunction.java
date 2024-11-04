package xxl.content;


import xxl.visits.ContentVisitor;
import java.util.List;
import java.util.Observer;

/** Class representing operations that work with a range of cells. */
public abstract class IntervalFunction extends Function implements Observer{

    /** The interval which the function is associated to. */
    private Interval _interval;
    
    /** Boolean flag that signals if any of the cells in the interval have been changed. */
    private boolean _uptodate = false;
    private Literal _result;

    /**
     * 
     * @param name the function's name.
     * @param interval the interval associated with the operation.
     */
    public IntervalFunction(String name, Interval interval){
        super(name);
        _interval = interval;
        List<Cell> cells = interval.getCells();
        if(cells.size() == 0){
            _result = new LitString("poopoocaca");
            _uptodate = false;
        }
        for(Cell cell: cells){
            cell.addObserver(this);
        }
    }

    public Interval getInterval(){
        return _interval;
    }

    /**
     * Gets the Reference where the interval starts.
     * @return start reference of the interval
     */
    public Reference getStartReference(){
        return _interval.getStartReference();
    }

    /**
     * Gets the Reference where the interval ends.
     * @return end reference of the interval.
     */
    public Reference getEndReference(){
        return _interval.getEndReference();
    }

    public abstract Literal calculateResult();

    /** Computes the value of the operation. 
     * @return result of the operation.
    */
    public Literal getResult(){
        _interval.setSpreadsheetStorage(getMemory());
        if(_uptodate){
            return _result;
        }
        Literal result = calculateResult();
        _uptodate = true;
        _result = result;
        super.setResult(result);
        return result;
    }
    
    /**
     * Visit the function.
     * 
     * @param visitor
     * @param litString (is it a interval function that returns a string or not?)
     * 
     */
    public void accept(ContentVisitor visitor, boolean litString) {
        visitor.visitIntervalFunction(this, litString);
    }

    public void update(){
        _uptodate = false;
    }

    public boolean isUptoDate(){
        return _uptodate;
    }

    public void deleteObservers(){
        List<Cell> cells = _interval.getCells();
        for(Cell cell: cells){
            cell.removeObserver(this);;
        }
    }

}