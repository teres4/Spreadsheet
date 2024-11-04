package xxl;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;

// FIXME import classes

/**
 * Class representing a spreadsheet application.
 */
public class Calculator {

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet = null;


    /** File name. */
    private String _filename = "";

    /**
     * 
     * @return spreadsheet
     */
    public Spreadsheet getSpreadsheet(){
            return _spreadsheet;
    }

    /**
     * 
     * @param spreadsheet the new spreadsheet to be set.
     */
    public void setSpreadsheet(Spreadsheet spreadsheet){
        _spreadsheet = spreadsheet;
    }

    /**
     * 
     * @return the associated file name.
     */
    public String getFilename() {
        return _filename;
    }

    public void setFilename(String filename) {
        _filename = filename;
    }


    /**
     * Checks if the spreadsheet has suffered any changes.
     * 
     * @return true, if the spreadsheet has been changed.
     */
    public boolean changed() {
        if (_spreadsheet == null)  
            return false;
        return _spreadsheet.hasChanged();
    }
    
    /**
     * 
     * @return true, if the spreadsheet is null.
     */
    public boolean nullSpreadsheet() {
        return _spreadsheet == null;
    }
    
    
    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {                
        if (_filename == null || _filename.equals(""))
            throw new MissingFileAssociationException();

        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)))) {
            oos.writeObject(_spreadsheet);
            _spreadsheet.setChanged(false);
        }

    }

    /**
     * Saves the serialized application's state into the specified file. The current network is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        _filename = filename;
        save();
    }

    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException, ClassNotFoundException, IOException {                
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            _spreadsheet = (Spreadsheet) ois.readObject();
            _spreadsheet.setChanged(false);
        }
    }

    /**
     * Read text input file and create domain entities..
     *
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    public void importFile(String filename) throws ImportFileException {
        int rows = 0, columns = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            // getting info to create a new spreadsheet
            while ((rows == 0 || columns == 0) && (line = reader.readLine()) != null){
                String s[] = line.split("=");
                if (line.contains("linhas"))
                    rows = Integer.parseInt(s[1]);
                else if (line.contains("colunas"))
                    columns = Integer.parseInt(s[1]);                      
            }
            // creates new spreadsheet
            setSpreadsheet(new Spreadsheet(rows, columns));
            
            while ((line = reader.readLine()) != null) {

                String[] fields = line.split("\\|");
                
                // if its just a cell (without content) skip over
                if (fields.length == 1)
                    continue;

                _spreadsheet.insertContents(fields[0], fields[1]);
                _spreadsheet.changed();
            }
            reader.close();

        // ....
        } catch (IOException | UnrecognizedEntryException /* FIXME maybe other exceptions */ e) {
            throw new ImportFileException(filename, e);
        }
    }
    

    /** 
     * Reset calculator.
     */
    public void reset(int rows, int columns) {
        _filename = null; 
        _spreadsheet = new Spreadsheet(rows, columns);
    }


}


    
