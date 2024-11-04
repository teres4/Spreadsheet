package xxl.content;

import xxl.visits.ContentVisitor;

/**Class representing content of the type String literal */
public class LitString extends Literal {

    /** The string */
    private String _str;
    
    /**
     * 
     * @param str
     */
    public LitString(String str){
        _str = str;
        setValue(this);
    }



    @Override
    public boolean equals(Object o){
        if (o instanceof LitString){
            LitString str = (LitString) o;
            return _str.equals(str.toString());
        }
        return false;
    }

    @Override
    public void accept(ContentVisitor visitor, boolean start) {
        visitor.visitLitString(this, start);
    }

    @Override
    public String toString(){
        return _str;
    }

    public String getString(){
        return _str;
    }

}