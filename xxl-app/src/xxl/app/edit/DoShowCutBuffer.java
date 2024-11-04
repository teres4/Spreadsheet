package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
// FIXME import classes
import xxl.app.visitors.RenderContent;

/**
 * Show cut buffer command.
 */
class DoShowCutBuffer extends Command<Spreadsheet> {

    DoShowCutBuffer(Spreadsheet receiver) {
        super(Label.SHOW_CUT_BUFFER, receiver);
    }

    @Override
    protected final void execute() {
        RenderContent renderer = new RenderContent();
        _receiver.acceptCutBuffer(renderer);
        _display.popup(renderer);
    }

}
