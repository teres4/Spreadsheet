package xxl.content;

import java.util.List;

import xxl.visits.ContentVisitor;

/** Class which represents the computation of the concatenation of strings on a range of cells. */
public class Concat extends IntervalFunction{

    public Concat(Interval interval){
        super("CONCAT", interval);
    }

    /**
     * Computes the concatenation of the operation on a range of cells.
     * @return the value of the operation
     */
    @Override
    public Literal calculateResult(){
        if(super.isUptoDate())
            return super.getResult();

        int i;
        LitString result;
        String resultString = "";
        List<Cell> cells = getInterval().getCells();

        // FIXME produz valor de entrada
        if (cells.size() == 1){

        }

        for(Cell cell : cells){
            try { 
                Content content = cell.getContent();
                resultString += content.getString();
            }
            catch (ClassCastException e){
            }
        }
        result = new LitString(resultString);
        this.setResult(result);
        return result;
    }

    @Override 
    public void accept(ContentVisitor visitor, boolean litString) {
        visitor.visitIntervalFunction(this, true);
    }
    
}