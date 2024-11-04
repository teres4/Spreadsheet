package xxl;

import java.util.List;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**  Class representing a user. */
public class User implements Serializable{
    
    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202308312359L;

    /** Name of the user */
    private String _name;
    
    /**  Spreadsheets associated with this user. */
    private List<Spreadsheet> _spreadsheets = new ArrayList<Spreadsheet>();

    /** Constructor.
     * Default user is named "root".
      */
    public User() {
        _name = "root";
    }

    /**
     * Constructor.
     * 
     * @param name the name of the user.
     */
    public User(String name){
        _name = name;
    }

    /**
     * Returns the list of spreadsheets associated with the user.
     * 
     * @return a list of spreadsheets of the user.
     */
    private List<Spreadsheet> getSpreadsheets(){
        return _spreadsheets;
    }

    /**
     * Add a spreadsheet.
     * 
     * @param spreadsheet the spreadsheet to be added.
     */
    private void insertSpreadsheet(Spreadsheet spreadsheet){
        _spreadsheets.add(spreadsheet);
    }
    
}
