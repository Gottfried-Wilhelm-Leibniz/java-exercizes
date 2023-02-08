package tvmprog.vm;
import tvmprog.OpEnum;
import tvmprog.operations.Operations;
import tvmprog.actions.*;
import tvmprog.opcode.*;
import tvmprog.printer.Printer;
import tvmprog.stack.Stack;
import java.nio.file.Path;
import java.util.EnumMap;

public class Tvm implements Vm {
    private static final EnumMap<OpEnum, OpAction> actions = new EnumMap<>(OpEnum.class);
    private final Stack stack;
    private final Stack callStack;
    private final Operations operations;
    private final OpCode opCode;
    private final Printer printer;
    static {
        actions.put(OpEnum.ADD, new AddAction());
        actions.put(OpEnum.SUB, new SubAction());
        actions.put(OpEnum.MUL, new MulAction());
        actions.put(OpEnum.DIV, new DivAction());
        actions.put(OpEnum.POP, new PopAction());
        actions.put(OpEnum.PUSH, new PushAction());
        actions.put(OpEnum.DUP, new DupAction());
        actions.put(OpEnum.SWAP, new SwapAction());
        actions.put(OpEnum.PRINT, new PrintAction());
        actions.put(OpEnum.PRINTC, new PrintCAction());
        actions.put(OpEnum.NOP, new NopAction());
        actions.put(OpEnum.HALT, new HaltAction());
        actions.put(OpEnum.INC, new IncAction());
        actions.put(OpEnum.DEC, new DecAction());
        actions.put(OpEnum.JMP, new JmpAction());
        actions.put(OpEnum.JNZ, new JnzAction());
        actions.put(OpEnum.JZ, new JzAction());
        actions.put(OpEnum.CALL, new CallAction());
        actions.put(OpEnum.RET, new RetAction());
    }

    public Tvm(Stack stack, Stack callStack, OpCode opCode, Printer printer) {
        this.stack = stack;
        this.callStack = callStack;
        this.opCode = opCode;
        this.printer = printer;
        this.operations =  new Operations(this::print, this::setAddress, this::next, this::getAddress, stack, callStack);
    }

    public void run(Path path) {
        opCode.load(path);
        while (opCode.hasNext()) {
            var word = next();
            var action = OpEnum.values()[word];
            if (actions.containsKey(action)) {
                actions.get(action).act(operations);
            }
        }
    }

    private void setAddress(int inti) {
        opCode.setIndex(inti);
    }
    private int getAddress() {
        return opCode.getIndex();
    }
    private int next() {
        return opCode.getNextInstructions();
    }
    private void print(char toPrint) {
        printer.print(toPrint);
    }

}
