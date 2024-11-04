package xxl.content;


/** Class representing literals. */
public abstract class Literal extends Content{
    
    /** The value of the literal. */
    private Literal _value;

    public Literal getValue(){
        return _value;
    }

    public void setValue(Literal literal){
        _value = literal;
    }

    /** Gets the integer value associated with the Literal. */
    public int getInt() throws ClassCastException {
        LitInteger integer = null;
        try {
            integer = (LitInteger) _value;
        } catch (ClassCastException e){
            // empty
        }
        return integer.getInt();
    }

    public String getString() throws ClassCastException {
        LitString s = null;
        try {
            s = (LitString) _value;
        } catch (ClassCastException e){
            // empty
        }
        return s.toString();
    }
}