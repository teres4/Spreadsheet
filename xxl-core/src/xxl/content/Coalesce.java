package xxl.content;

import java.util.List;

import xxl.visits.ContentVisitor;

/** Class representing the operation "coalesce" on a range of cells. */
public class Coalesce extends IntervalFunction{
    
    public Coalesce(Interval interval){
        super("COALESCE", interval);
    }

    /**
     * Computes the value of the operation and returns the content of the first cell
     * that contains a string.
     * @return the value of the operation.
     */
    @Override
    public Literal calculateResult(){
        if(super.isUptoDate())
            return super.getResult();

        int i;
        LitString result;
        String resultString = "";
        List<Cell> cells = getInterval().getCells();
        
        /**for(i = 0; i < cells.size(); i++){
            try {
                result = (LitString) cells.get(i).getContent();
                resultString  += result.getString();

                break;
            }
            catch(ClassCastException e){}
        }**/
        for(Cell cell : cells){
            try { 
                Content content = cell.getContent();
                resultString += content.getString();
                break;
            }
            catch (ClassCastException e){
                return new LitString("#VALUE");
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