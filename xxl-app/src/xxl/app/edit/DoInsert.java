package xxl.app.edit;

import xxl.app.edit.UnknownFunctionException;
import xxl.exceptions.UnrecognizedEntryException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

// FIXME import classes

/**
 * Class for inserting data.
 */
class DoInsert extends Command<Spreadsheet> {

    DoInsert(Spreadsheet receiver) {
        super(Label.INSERT, receiver);
        addStringField("range", Prompt.address());
        addStringField("content", Prompt.content());
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.insertContents(stringField("range"), stringField("content"));
            _receiver.changed();
            
        } catch (UnsupportedOperationException e){
            throw new UnknownFunctionException(stringField("content"));
        } catch (UnrecognizedEntryException e) {
            throw new InvalidCellRangeException(stringField("range")); 
        // } catch (ArrayIndexOutOfBoundsException e){
        //     throw new InvalidCellRangeException(stringField("range")); 
        }
    }
}

