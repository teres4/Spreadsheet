package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import xxl.Spreadsheet;
import xxl.app.visitors.RenderContent;
import xxl.exceptions.UnrecognizedEntryException;

// FIXME import classes

/**
 * Class for searching functions.
 */
class DoShow extends Command<Spreadsheet> {

    DoShow(Spreadsheet receiver) {
        super(Label.SHOW, receiver);
        addStringField("range", Prompt.address());        
        // FIXME add fields
        
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.ensureWithinSpreadsheet(stringField("range"));

            RenderContent renderer = new RenderContent();
            _receiver.accept(stringField("range"), renderer);
            _display.popup(renderer);

        } catch (UnrecognizedEntryException e) {
            throw new InvalidCellRangeException(stringField("range"));
        };
    }

}
