package xxl.content;

import java.util.List;

import xxl.visits.ContentVisitor;

/** Class which represents the computation of the operation "average" on a range of cells. */
public class Average extends IntervalFunction{
    
    public Average(Interval interval){
        super("AVERAGE", interval);
    }

    /**
     * Computes the average value of a range of cells with valid content (integers).
     * @return the value of the operation.
     */
    @Override
    public Literal calculateResult(){

        
        //if(super.isUptoDate()) 
        //    return super.getResult();

        int add = 0;

        List<Cell> cells = getInterval().getCells();

        for(Cell cell : cells){
            try { 
                Content content = cell.getContent();
                add += content.getInt();
            }
            catch (ClassCastException e){
                return new LitString("#VALUE");
            }
        }
        int size = cells.size();
        return new LitInteger(add/size);
    }

    @Override 
    public void accept(ContentVisitor visitor, boolean litString) {
        visitor.visitIntervalFunction(this, false);
    }
    
}