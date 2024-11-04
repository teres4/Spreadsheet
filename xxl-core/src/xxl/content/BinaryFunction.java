package xxl.content;

import xxl.visits.ContentVisitor;

/** Class representing binary operations that work with 2 integers. */

public class BinaryFunction extends Function implements Observer{

    /** Arguments in the function */
    private Content _arg1;

    private Content _arg2;

    /** Boolean flag that indicated if the arguments of the function have changed. */
    private boolean _uptodate = true;

    /**
    * @param name the function's name.
    * @param arg1 the first argument.
    * @param arg2 the second argument.
    */
    public BinaryFunction(String name, Content arg1, Content arg2){
        super(name);
        _arg1 = arg1;
        _arg2 = arg2;
        calculate();
    }

    /**Calculates the result of the function according to its name
     * @return the result
     */
    public void calculate(){
        int int1, int2, result = 0;
        try {
            int1 =_arg1.getInt();
            int2 = _arg2.getInt();

            switch(getName()){
                case "ADD": result = int1 + int2;
                    break;
                case "SUB": result = int1 - int2;
                    break;
                case "MUL": result = int1 * int2;
                    break;
                case "DIV": result = int1 / int2;
                    break; //put exception it cant be 0
            }
            super.setResult(new LitInteger(result));
        } catch (ClassCastException e){
            super.setResult(new LitString("#VALUE"));
        }
    }

    /**
     * Computes the value of the operation.
     * @return result of the operation
     */
    public Literal getResult(){
        calculate();
        return super.getResult();
    }

    public Content getArg1(){
        return _arg1;
    }

    public Content getArg2(){
        return _arg2;
    }

    @Override 
    public void accept(ContentVisitor visitor, boolean start) {
        visitor.visitBinaryFunction(this, start);
    }

    public void update(){
        _uptodate = false;
    }
}