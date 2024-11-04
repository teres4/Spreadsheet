package xxl.content;

import xxl.visits.ContentVisitor;


/** Class representing functions. */
public class Function extends Content {
    
    /** Name of the function */
    private String _name;

    /** Literal containing the value after the computation of the operation. */
    private Literal _result;

    /**
    * @param name the function's name.
    */
    public Function(String name){
        _name = name;
    }

    public Literal getResult(){
        return _result;
    }

    public void setResult(Literal result){
        _result = result;
    }

    public String getName(){
        return _name;
    }

    /**
     * Checks if the function is a binary function, based on its name.
     * @return true, if it's a binary function.
     */
    public boolean isBinaryFunction(){
        return _name.equals("ADD") || _name.equals("SUB") || _name.equals("MUL") || _name.equals("DIV");
    }

    /**
     * Checks if the function is an interval function, based on its name.
     * @return true, it it's an interval function.
     */
    public boolean isIntervalFunction(){
        return _name.equals("AVERAGE") || _name.equals("PRODUCT") || _name.equals("CONCAT") || _name.equals("COALESCE");
    }

    /**
     * Checks if the function has a valid function name.
     * @return true, it it's a valid function name.
     */
    public boolean isFunction(){
        return isBinaryFunction() || isIntervalFunction();
    }

    /**
     * Checks if the functions have the same name.
     * @param fun
     * @return  true, if they have the same name.
     */
    public boolean functionSameName(Function fun){
        return _name.equals(fun.getName());
    }


    /**
     * Gets the integer value associated with the function's value after computation.
     * @return int
     */
    public int getInt() throws ClassCastException {
        LitInteger integer = null;
        try {
            integer = (LitInteger) _result;
        } catch (ClassCastException e){
        }
        return integer.getInt();
    }
    
    public String getString() throws ClassCastException {
        LitString s = null;
        try {
            s = (LitString) _result;
            return s.toString();
        } catch (ClassCastException e){
            // empty
        }
        return "";
    }


    @Override
    public void accept(ContentVisitor visitor, boolean start) {
        // empty
    }


}