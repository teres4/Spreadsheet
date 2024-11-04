package xxl.content;

import java.util.List;

import xxl.visits.ContentVisitor;


/** Class which represents the computation of the operation "product" on a range of cells. */
public class Product extends IntervalFunction{

    public Product(Interval interval){
        super("PRODUCT", interval);
    }

    // public void setResult(Literal result){
    //     super.setResult(result);
    // }

    /**
     * Computes the value of the operation.
     * @return result of the operation.
     */
    @Override
    public Literal calculateResult(){
        if (super.isUptoDate())
            return super.getResult();

        int intresult = 1; 
        List<Cell> cells = getInterval().getCells();

        for(Cell cell : cells){
            try {
                Content content = cell.getContent();
                LitInteger res = (LitInteger) content;
                intresult *= res.getInt();
            }
            catch(ClassCastException e){
                return new LitString("#VALUE");
            }
            
        }
            return new LitInteger(intresult);

    }

    @Override 
    public void accept(ContentVisitor visitor, boolean litString) {
        visitor.visitIntervalFunction(this, false);
    }
    
}