package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Calculator;
import xxl.Spreadsheet;

/**
 * Open a new file.
 */
class DoNew extends Command<Calculator> {

    DoNew(Calculator receiver) {
        super(Label.NEW, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        // saving the changes before opening a new spreadsheet
        if (_receiver.changed() && Form.confirm(Prompt.saveBeforeExit())) {
            DoSave save = new DoSave(_receiver);
            save.execute();
        }     
        _receiver.reset(Form.requestInteger(Prompt.lines()), Form.requestInteger(Prompt.columns()));
    }

}