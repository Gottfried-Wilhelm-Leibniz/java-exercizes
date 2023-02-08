package newtvm.vm;
import newtvm.program.Program;
import newtvm.context.Context;
import newtvm.actions.*;
import newtvm.printer.Printer;
import newtvm.stack.Stack;
public class Tvm implements Vm {
    private final Stack stack;
    private final Stack callStack;
    private final Context context;
    private final Printer printer;
    private Program programRun;

    public Tvm(Stack stack, Stack callStack, Printer printer) {
        this.stack = stack;
        this.callStack = callStack;
        this.printer = printer;
        this.context =  new Context(this::print, this::setAddress, this::next, this::getAddress, stack, callStack);
    }
    @Override
    public void run(Program program) {
        programRun = program;
        while (programRun.hasNext()) {
            var opCode = programRun.next();
            opCode.execute(context);
        }
        clearStacks();
    }

    private void clearStacks() {
        stack.clear();
        callStack.clear();
    }

    private void setAddress(int inti) {
        programRun.setIndex(inti);
    }
    private int getAddress() {
        return programRun.getIndex();
    }
    private int next() {
        return programRun.next().data();
    }
    private void print(char toPrint) {
        printer.print(toPrint);
    }

}
