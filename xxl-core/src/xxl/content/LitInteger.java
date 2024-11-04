package xxl.content;

import xxl.visits.ContentVisitor;

/**Class representing content of the type integer literal */
public class LitInteger extends Literal {

    /** The integer. */
    private int _int;

    /**
     * 
     * @param integer 
     */
    public LitInteger(int integer){
        _int = integer;
        setValue(this);
    }

    /**
     * 
     * @return integer
     */
    @Override
    public int getInt(){
        return _int;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof LitInteger){
            LitInteger litInt = (LitInteger) o;
            return _int == litInt.getInt();
        }
        return false;
    }

    @Override
    public void accept(ContentVisitor visitor, boolean start) {
        visitor.visitLitInteger(this, start);
    }

    /**
    * 
    * @see java.lang.Object#toString()
    *//** */
    @Override
    public String toString(){
        return Integer.toString(_int);
    }
}