package xxl;

import xxl.content.Concat;
import xxl.content.LitString;
import xxl.content.BinaryFunction;
import xxl.content.Cell;
import xxl.content.Coalesce;
import xxl.content.Average;
import xxl.content.Product;
import xxl.content.Content;
import xxl.content.Empty;
import xxl.content.Interval;
import xxl.content.LitInteger;
import xxl.content.Reference;
import xxl.content.Function;
import xxl.content.IntervalFunction;


import xxl.exceptions.UnrecognizedEntryException;
import xxl.visits.ContentVisitor;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.Serial;
import java.io.Serializable;
import java.util.regex.Pattern;



/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202308312359L;

    /** Spreadsheet object has been changed. */
    private boolean _changed = false;

    /** The cells of this spreadsheet */
    private SpreadsheetStorage _cells = new SpreadsheetStorage();

    private SpreadsheetStorage _cutBuffer = null;
    
    /** List of users that detain this spreadsheet. */
    private List<User> _users = new ArrayList<User>();

    /* Active user. */
    private User _activeUser = new User();


    /**
     * Constructor.
     * 
    * @param rows    the spreadsheet's rows
    * @param columns the spreadsheet's columns.
    */
    public Spreadsheet(int rows, int columns){
        _cells.setDimensions(rows, columns);   
    }

    /**
    * Set changed.
    */
    public void changed() {
        setChanged(true);
    }

    /**
    * @return changed
    */
    public boolean hasChanged() {
        return _changed;
    }

    /**
    * @param changed
    */
    public void setChanged(boolean changed) {
        _changed = changed;
    }

    public void ensureWithinSpreadsheet(String rangeSpecification) throws UnrecognizedEntryException {
        if (rangeSpecification.contains(":")){
            makeInterval(rangeSpecification);
            return;
        }
        else if (rangeSpecification.contains(";")){
            makeAReference(rangeSpecification);
            return;
        }
        else 
            throw new UnrecognizedEntryException(rangeSpecification);
    }

    /**
     * Insert specified content in specified range.
     *
     * @param rangeSpecification
     * @param contentSpecification
     * @throws UnrecognizedEntryException
     */
    public void insertContents(String rangeSpecification, String contentSpecification) throws UnrecognizedEntryException, UnsupportedOperationException /* FIXME maybe add exceptions */ {

        Content content = filterContent(contentSpecification);

        // se for para inserir conteudos num intervalo
        if (rangeSpecification.contains(":")) {
            Map<String, Cell> cells = makeInterval(rangeSpecification).getCellsInInterval();

            for (Map.Entry<String,Cell> entry : cells.entrySet()){
                entry.getValue().setContent(content);
                _cells.putCell(makeAReference(entry.getKey()), entry.getValue());
            }
        }
        else {
            Reference contentRef = makeAReference(rangeSpecification);
            Cell cell = _cells.getCellFromRef(contentRef);
            cell.setContent(content);
        }
    }       

    /**
     * Create content based on the given information.
     * @param contentSpecification 
     * @return Content
     * @throws UnrecognizedEntryException
     * @throws UnsupportedOperationException
     */
    public Content filterContent(String contentSpecification) throws UnrecognizedEntryException, UnsupportedOperationException {
        // Regular expression pattern to match a string.
        Pattern patString = Pattern.compile("'.+");

        // Regular expression pattern to match a reference.
        Pattern patReference= Pattern.compile("=\\d+;\\d+");

         // Regular expression pattern to match a function.
        Pattern patFunction = Pattern.compile("=\\w+.+");

        // Regular expression pattern to match an integer.
        Pattern patInteger = Pattern.compile("-?\\d+");

        Content content = null;

        if (patString.matcher(contentSpecification).matches()){
            content = new LitString(contentSpecification.substring(1));
        }

        else if (patReference.matcher(contentSpecification).matches()){
            content = makeAReference(contentSpecification.substring(1));
            content.setMemory(_cells);
        }

        else if (patFunction.matcher(contentSpecification).matches()){
            String[] contents = contentSpecification.split("\\(");
            String functionName = contents[0].substring(1);

            String args = contents[1].substring(0, contents[1].length() - 1);

            Function fun = new Function(functionName);
            
            if (fun.isBinaryFunction()) 
                content = makeBinaryFunction(functionName, args);

            else if (fun.isIntervalFunction())  
                content = makeIntervalFunction(functionName, args);
            
            else    // se o nome da funcao for inexistente
                throw new UnsupportedOperationException(functionName);
        }

        else if (patInteger.matcher(contentSpecification).matches()){
            content  = makeALitInt(contentSpecification);
            
        } else {
            throw new UnrecognizedEntryException(contentSpecification);
        }
        return content;
    }


    /**
     * Create a binary function with the given information.
     * @param name name of the function
     * @param argSpecification
     * @return BinaryFunction
     * @throws UnrecognizedEntryException
     */
    public BinaryFunction makeBinaryFunction(String name, String argSpecification) throws UnrecognizedEntryException {
        Content arg1, arg2;
        String[] args = argSpecification.split("\\,");
        
        if (args[0].contains(";")) 
            arg1 = makeAReference(args[0]);
        else
            arg1 = makeALitInt(args[0]);

        if (args[1].contains(";"))
            arg2 = makeAReference(args[1]);
        else
            arg2 = makeALitInt(args[1]);
            
        return new BinaryFunction(name, arg1, arg2);

    }

    /**
     * Create an interval function with the given information.
     * @param name name of the function
     * @param args 
     * @return IntervalFunction
     * @throws UnrecognizedEntryException
     */
    public IntervalFunction makeIntervalFunction(String name, String args) throws UnrecognizedEntryException {
        Interval interval = makeInterval(args);
        IntervalFunction f = null;
        switch(name){
            case "COALESCE": f = new Coalesce(interval);
                break;
            case "AVERAGE": f = new Average(interval);
                break;
            case "CONCAT": f = new Concat(interval);
                break;
            case "PRODUCT": f = new Product(interval);
                break; //put exception it cant be 0
        }
        return f;
    }

    /**
     * Create a reference with the given information.
     * @param contentSpecification contentSpecification contains row and column to create 
     * @return the new reference.
     * @throws UnrecognizedEntryException
     */
    public Reference makeAReference(String contentSpecification) throws UnrecognizedEntryException{

        String[] refStr = contentSpecification.split(";");
        Reference ref = null;

        if (refStr.length >= 2) {
            int refRow = Integer.parseInt(refStr[0]);
            int refColumn = Integer.parseInt(refStr[1]);

            if (_cells.inSpreadsheetBoundaries(refRow, refColumn)){
                ref = new Reference(refRow, refColumn,  _cells);
                ref.setMemory(_cells);
            }
            else 
                throw new UnrecognizedEntryException(contentSpecification);
        }
        else 
            throw new UnrecognizedEntryException(contentSpecification);

        return ref;
    }

    /**
     * Create an integer literal.
     * @param contentSpecification contains integer to create integer literal
     * @return the new integer literal.
     */
    public LitInteger makeALitInt(String contentSpecification){
        LitInteger content = new LitInteger(Integer.parseInt(contentSpecification));
        return content;
    }   

    /**
     * Create an interval.
     * 
     * @param range contains rows and columns to create interval
     * @return the new interval.
     * @throws UnrecognizedEntryException 
     */
    public Interval makeInterval(String range) throws UnrecognizedEntryException {
        Interval interval = null;

        String refs[] = range.split(":");

        if (refs.length != 1) { // se n for so uma celula
            Reference r1 = makeAReference(refs[0]);
            Reference r2 = makeAReference(refs[1]);
            
            if (r1.equals(r2))
                interval = new Interval(r1.getRow(), r1.getColumn(), r1.getRow(), r1.getColumn());
            
            // assumindo que tem smp uma das coordenadas em comum
            if (r1.getRow() < r2.getRow() || r1.getColumn() < r2.getColumn())
                interval = new Interval(r1.getRow(), r1.getColumn(), r2.getRow(), r2.getColumn());

            else if (r1.getRow() > r2.getRow()|| r1.getColumn() > r2.getColumn())
                interval = new Interval(r2.getRow(), r2.getColumn(), r1.getRow(), r1.getColumn());
        }
        else {
            Reference r1 = makeAReference(refs[0]);
            interval = new Interval(r1.getRow(), r1.getColumn(), r1.getRow(), r1.getColumn());
        }
        if (!interval.isValidInterval())
            throw new UnrecognizedEntryException(range);
            
        interval.setSpreadsheetStorage(_cells);
        return interval;
    }

    /**
     * Copy the information in range of cells to the cutBuffer.
     * @param rangeSpecification
     * @throws UnrecognizedEntryException
     */
    public void copyToCutBuffer(String rangeSpecification) throws UnrecognizedEntryException {
        _cutBuffer = new SpreadsheetStorage();
        Interval interval = makeInterval(rangeSpecification);
        List<Reference> refs = interval.listCellsInInterval();

        if (interval.sameRow()) {
            _cutBuffer.setDimensions(1, refs.size());
            for (int i = 1; i < refs.size() + 1 ; i++) {
                Reference ref = refs.get(i - 1);
                Cell cell = new Cell(_cells.getCellFromRef(ref).getContent());
                _cutBuffer.putCell(new Reference(1, i), cell);
            }
        }
        else {
            _cutBuffer.setDimensions(refs.size(), 1);
            for (int i = 1; i < refs.size() + 1 ; i++) {
                Reference ref = refs.get(i - 1);
                Cell cell = new Cell(_cells.getCellFromRef(ref).getContent());
                _cutBuffer.putCell(new Reference(i, 1), cell);
            }
        }
    }


    /**
     * Delete the content in a range of cells.
     * @param rangeSpecification
     * @throws UnrecognizedEntryException
     */
    public void deleteContent(String rangeSpecification) throws UnrecognizedEntryException {
        Interval interval = makeInterval(rangeSpecification);
        List<Reference> refs = interval.listCellsInInterval();

        for (Reference ref : refs){
            Cell cell = _cells.getCellFromRef(ref);
            cell.setContent(new Empty());
        }

    }

    /**
     * Paste the content in the cutBuffer to a range of cells.
     * @param rangeSpecification
     * @throws UnrecognizedEntryException
     */
    public void pasteCutBuffer(String rangeSpecification) throws UnrecognizedEntryException {
        Interval interval = makeInterval(rangeSpecification);
        List<Reference> refs = interval.listCellsInInterval();

        if (_cutBuffer == null) 
            return;
    
        else {
            List<Content> toInsert = _cutBuffer.getListOfContent();
            if (refs.size() == 1) // if the range only has one cell.
                pasteSingleCell(refs.get(0), toInsert);

            else if (refs.size() == _cutBuffer.getSize())
                paste(refs, toInsert); 
        }
    }

    /**
     * Paste the cutBuffer's content 'til it reaches the limits of the spreadsheet.
     * @param ref Reference in which the pasting starts.
     * @param toInsert list containing the content to insert.
     */
    public void pasteSingleCell(Reference ref, List<Content> toInsert){
        int index = 0;

        if (_cutBuffer.getRows() == 1){ // se for uma linha imprime pro lado
            int row = ref.getRow();
            for (int i = ref.getColumn() ; i <= _cells.getColumns() && index < toInsert.size(); i++, index++){
                Cell cell = _cells.getCellFromRef(new Reference(row, i));
                cell.setContent(toInsert.get(index));
            }
        }
        else { // se for uma coluna ent imprime pra baixo
            int column = ref.getColumn();
            for (int i = ref.getRow(); i <= _cells.getRows() && index < toInsert.size(); i++, index++) {
                Cell cell = _cells.getCellFromRef(new Reference(i, column));
                cell.setContent(toInsert.get(index));
            }
        }   
    }

    /**
     * Paste the cutBuffer's content to a specified range of cells.
     * @param refs list of references where the content has to be pasted.
     * @param toInsert list containing the content to insert.
     */
    public void paste(List<Reference> refs, List<Content> toInsert){
        int index = 0;
        for (Reference ref : refs){
            Cell cell = _cells.getCellFromRef(ref);
            cell.setContent(toInsert.get(index));
            index++;
        }
    }


    /**
     * 
     * @param rangeSpecification
     * @param visitor
     * @throws UnrecognizedEntryException
     */
    public void accept(String rangeSpecification, ContentVisitor visitor) throws UnrecognizedEntryException{
        Interval interval = makeInterval(rangeSpecification);
        List<Reference> refs = interval.listCellsInInterval();

        accept(refs, _cells, visitor);
    }


    /**
     * 
     * @param cells
     * @param storage
     * @param visitor
     */
    public void accept(List<Reference> cells,SpreadsheetStorage storage, ContentVisitor visitor) {
        for (Reference ref : cells) {
            ref.accept(visitor, true);
            
            Content content = storage.getCellFromRef(ref).getContent();

            try { // if it doesnt have anything, dont do anything
                Empty empty = (Empty) content;
                continue;
            } catch (ClassCastException e){
            }
            try { // it its a reference, search for its literal value
                Reference reference = (Reference) content;
                searchReferenceInDepth(reference, visitor);
            } catch (ClassCastException e){
            }
            
            if (content != null)
                content.accept(visitor, false);
        }
    }

    /**
     * Searches, recursively, for the literal value given a reference.
     * @param ref
     * @param visitor
     * @return
     */
    public Content searchReferenceInDepth(Reference ref, ContentVisitor visitor){
        Content content = _cells.getCellFromRef(ref).getContent();
        try {
            Reference reference = (Reference) content;
            content = searchReferenceInDepth(reference, visitor);

        } catch (ClassCastException e) { 
            content.accept(visitor, false);
        }
        return content;
    }


    /**
     * 
     * @param visitor
     */
    public void acceptCutBuffer(ContentVisitor visitor) {
        if (_cutBuffer == null)
            return;

        List<Reference> refs = new ArrayList<Reference>();
        for (String str : _cutBuffer.getReferences()){
            try {
                refs.add(makeAReference(str));
            } catch (UnrecognizedEntryException e){
                // empty
            }            
        }
        accept(refs, _cutBuffer, visitor);
    }


    /**
     * Search for the references that are associated with the value we want to find.
     * @param value we want to find
     * @param visitor
     * @return list of references with the value we want
     * @throws UnrecognizedEntryException
     */
    public List<Reference> referencesValue(String value, ContentVisitor visitor ) throws UnrecognizedEntryException{
        Content content = filterContent(value); 
        Collection<String> allReferences = _cells.getReferences();
        List<Reference> matchedRefs = new ArrayList<Reference>();
        
        for (String str : allReferences) {
            Reference ref = makeAReference(str);
            if (_cells.getCellFromRef(ref).getContent().equals(content) || 
            searchReferenceInDepth(ref, visitor).equals(content))  // se for uma ref procura ate encontrar o literal
                matchedRefs.add(ref);
            else {
                try { // se for uma funcao, procura pelo resultado da funcao
                    Function fun = (Function) _cells.getCellFromRef(ref).getContent();
                    if (fun.getResult().equals(content))
                        matchedRefs.add(ref);
                } catch (ClassCastException e){
                }
            }
        }
        return matchedRefs;
    }

    /**
     * 
     * @param value
     * @param visitor
     * @param auxvisitor
     * @throws UnrecognizedEntryException
     */
    public void searchValue(String value, ContentVisitor visitor, ContentVisitor auxvisitor)
    throws UnrecognizedEntryException{
        accept(referencesValue(value, auxvisitor), _cells, visitor);
    }

    
    /**
     * Search for all the references that are associated with function name we want to find.
     * @param functionName
     * @param visitor
     * @throws UnrecognizedEntryException
     */
    public void searchFunction(String functionName, ContentVisitor visitor) throws UnrecognizedEntryException{
        Collection<String> allReferences = _cells.getReferences();
        ArrayList<Reference> matchedRefs = new ArrayList<Reference>();
                
        for (String str : allReferences) {
            Reference ref = makeAReference(str);
            Content content = _cells.getCellFromRef(ref).getContent();
            try {
                Function function = (Function) content;
                if (function.getName().contains(functionName)) // se for uma substring do nome da funcao, aceita a mm
                    this.sortedInsertSearchFunction(matchedRefs, ref, function);
            } catch (ClassCastException e){
            }
        }
        accept(matchedRefs, _cells, visitor);
        
    }
    
    /**
     * Sorts the list of references, according to the name of the function and row and column of the reference.
     * @param list
     * @param currentref
     * @param content
     */
    public void sortedInsertSearchFunction(ArrayList<Reference> list, Reference currentref, Function content){
        boolean foundspot = false;
        int size = list.size();
        for(int j = 0; j < size; j ++){
            Reference ref = list.get(j);
            Function functionite = (Function)_cells.getCellFromRef(ref).getContent();
            if(functionite.getName().compareTo(content.getName()) > 0){
                for(int i = j + 1; i < size; i++){
                    list.add(i, list.get(j));
                }
                list.add(j, currentref);
                foundspot = true;
            }
        }
        if(!foundspot){
            list.add(currentref);
        }
    }
    
}   
