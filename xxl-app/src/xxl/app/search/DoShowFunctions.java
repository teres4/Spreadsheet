package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
// FIXME import classes
import xxl.app.visitors.RenderContent;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Command for searching function names.
 */
class DoShowFunctions extends Command<Spreadsheet> {

    DoShowFunctions(Spreadsheet receiver) {
        super(Label.SEARCH_FUNCTIONS, receiver);
        addStringField("value", Prompt.searchFunction()); 
    }

    @Override
    protected final void execute() {
        try {
            RenderContent renderer = new RenderContent();
            _receiver.searchFunction(stringField("value"), renderer);
            _display.popup(renderer);
        } catch (UnrecognizedEntryException e){
            // empty
        }

    }

}
