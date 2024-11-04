package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
// FIXME import classes
import xxl.app.edit.InvalidCellRangeException;
import xxl.app.visitors.RenderContent;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Command for searching content values.
 */
class DoShowValues extends Command<Spreadsheet> {

    DoShowValues(Spreadsheet receiver) {
        super(Label.SEARCH_VALUES, receiver);
        addStringField("value", Prompt.searchValue()); 
    }

    @Override
    protected final void execute() {
        try {
            RenderContent renderer = new RenderContent();
            RenderContent auxrenderer = new RenderContent();
            _receiver.searchValue(stringField("value"), renderer, auxrenderer);
            _display.popup(renderer);
        } catch (UnrecognizedEntryException e){
            // empty
        }
        
        
    }

}
