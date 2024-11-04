package xxl.app.visitors;

import xxl.content.LitString;
import xxl.content.LitInteger;
import xxl.content.Reference;
import xxl.content.IntervalFunction;
import xxl.content.BinaryFunction;
import xxl.content.Empty;
import xxl.visits.ContentVisitor;



public class RenderContent implements ContentVisitor {

    /** Rendered content. */
    private String _rendering = "";

    public void visitLitString(LitString str, boolean start) {
        _rendering += "'" + str.getString();
    }

    public void visitLitInteger(LitInteger litInt, boolean start){
        _rendering += "" + litInt.getInt();
    }

    public void visitReference(Reference ref, boolean start){
        if (_rendering != "")
            _rendering += (start ? "\n" : "=");
        _rendering += Integer.toString(ref.getRow()) + ';' + Integer.toString(ref.getColumn()) + (start ? "|" : ""); 
        // se for o inicio de uma linha ent tem de mostrar o conteudo
    }
    
    public void visitEmpty(Empty empty, boolean start) {
        _rendering += empty.getString();
    }

    public void visitBinaryFunction(BinaryFunction binaryFunction, boolean start){
        _rendering += binaryFunction.getResult() + "=" + binaryFunction.getName() + "(" + 
        binaryFunction.getArg1().toString() + "," + binaryFunction.getArg2().toString() + ")";
    }

    public void visitIntervalFunction(IntervalFunction intervalFunction, boolean litString){
        _rendering += (litString ? "'" : "");
        _rendering += intervalFunction.getResult() + "=" + intervalFunction.getName() + "(" + intervalFunction.getStartReference().toString()
        + ":" + intervalFunction.getEndReference().toString() + ")";
    }

    @Override
    public String toString(){
        return _rendering;
    }

    public void reset(){
        _rendering = "";
    }
}