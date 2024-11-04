package xxl.visits;

import xxl.content.BinaryFunction;
import xxl.content.Empty;
import xxl.content.LitInteger;
import xxl.content.LitString;
import xxl.content.Reference;
import xxl.content.IntervalFunction;


public interface ContentVisitor {
    
    void visitLitString(LitString str, boolean start);

    void visitLitInteger(LitInteger litInt, boolean start);

    void visitReference(Reference ref, boolean start);

    void visitEmpty(Empty empty, boolean start);

    void visitBinaryFunction(BinaryFunction binaryFunction, boolean start);

    void visitIntervalFunction(IntervalFunction intervalFunction, boolean litString);

    void reset();

}
