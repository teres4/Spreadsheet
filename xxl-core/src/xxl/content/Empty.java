package xxl.content;

import xxl.visits.ContentVisitor;

/**Class representing the Content of an empty cell*/
public class Empty extends Content{

    public int getInt() throws ClassCastException {
        throw new ClassCastException();
    }

    public String getString(){
        return "";
    }

    @Override
    public void accept(ContentVisitor visitor, boolean start) {
        visitor.visitEmpty(this, start);
    }

}