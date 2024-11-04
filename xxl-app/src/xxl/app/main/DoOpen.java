package xxl.app.main;

import java.io.IOException;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import xxl.Calculator;
import xxl.exceptions.UnavailableFileException;

// FIXME import classes



/**
 * Open existing file.
 */
class DoOpen extends Command<Calculator> {

    DoOpen(Calculator receiver) {
        super(Label.OPEN, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            // if there were changes to the spreadsheet and we want to save them 
            if (_receiver.changed() && Form.confirm(Prompt.saveBeforeExit())) {
                DoSave save = new DoSave(_receiver);
                save.execute();
            }
            _receiver.load(Form.requestString(Prompt.openFile()));
        } catch (UnavailableFileException e) {
            throw new FileOpenFailedException(e);
        } catch (ClassNotFoundException e) {
             throw new FileOpenFailedException(e);
        } catch (IOException e) {
             throw new FileOpenFailedException(e);
        }
    }

}
