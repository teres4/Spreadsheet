package xxl.content;

import xxl.content.Reference;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**Class representing a spreadsheet Cell */
public class Cell implements Serializable, Observable{

    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202308312359L;
    
    /* Content of the cell */
    private Content _content;

    /** List of observers of the cell. */
    private ArrayList<Observer> _observers = new ArrayList<Observer>();

    /**
     * @param content
     */
    public Cell(Content content){
        _content = content;
    }

    public Cell(){};

    public void setContent(Content content){
        
        this.notifyObservers();
        try{
            Reference ref = (Reference) _content;
            ref.getCellFromRef().removeObserver(ref);
        }catch(ClassCastException e){

        }try{
            IntervalFunction f = (IntervalFunction) _content;
            f.deleteObservers();
        }catch(ClassCastException e){}
        _content = content;
    }
    
    
    /** @see java.lang.Object#toString() */
    @Override 
    public String toString() {
        return  _content.toString();
    }

    /**
     * @return Content
     */
    public Content getContent(){
        return _content;
    }


    public void addObserver(Observer observer){
        _observers.add(observer);
    }

    public void removeObserver(Observer observer){
        _observers.remove(observer);
    }
    
    public void notifyObservers(){
        for (Observer observer: _observers) {
            observer.update();
        }
    }


    
}