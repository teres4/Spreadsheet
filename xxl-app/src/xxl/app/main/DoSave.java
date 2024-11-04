package xxl.app.main;

import java.io.FileNotFoundException;
import java.io.IOException;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;

import xxl.Calculator;
import xxl.exceptions.MissingFileAssociationException;

// FIXME import classes

/**
 * Save to file under current name (if unnamed, query for name).
 */
class DoSave extends Command<Calculator> {

    DoSave(Calculator receiver) {
        super(Label.SAVE, receiver, xxl -> xxl.getSpreadsheet() != null);
    }

    @Override
    protected final void execute() {

        try {
            _receiver.save();
        } catch (MissingFileAssociationException e) { // when its not associated w any file

            try { 
                // if the file doesnt have a name yet
                _receiver.saveAs(Form.requestString(Prompt.newSaveAs()));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (MissingFileAssociationException e1){
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // FIXME implement command and create a local Form
    }

}
