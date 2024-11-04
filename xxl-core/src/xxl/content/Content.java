package xxl.content;

import java.io.Serial;
import java.io.Serializable;

import xxl.visits.ContentVisitor;

/** Class representing the content of a cell */

public abstract class Content implements Serializable{


    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202308312359L;



    public abstract String getString();
    public abstract int getInt();
    /**
     * 
     * @return the string of an invalid value
     */
    public String InvalidValue(){
        return "#VALUE";
    }

    public abstract void accept(ContentVisitor visitor, boolean start);

}