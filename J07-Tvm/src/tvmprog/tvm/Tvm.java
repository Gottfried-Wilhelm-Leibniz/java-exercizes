package tvmprog.tvm;

import tvmprog.tvm.Translator.Translator;
import tvmprog.tvm.actions.*;
import tvmprog.tvm.opcode.*;
import tvmprog.tvm.stack.Stack;

import java.util.EnumMap;

public class Tvm implements Vm {
    private final Translator translator;
    private static final EnumMap<OpCodeEnums, OpAction> actions = new EnumMap<>(OpCodeEnums.class);
    private final tvmprog.tvm.stack.Stack stack;
    private final Printer printer;
    static {
        actions.put(OpCodeEnums.ADD, new AddAction());
        actions.put(OpCodeEnums.SUB, new SubAction());
        actions.put(OpCodeEnums.MUL, new MulAction());
        actions.put(OpCodeEnums.DIV, new DivAction());
        actions.put(OpCodeEnums.POP, new PopAction());
        actions.put(OpCodeEnums.PUSH, new PushAction());
        actions.put(OpCodeEnums.DUP, new DupAction());
        actions.put(OpCodeEnums.SWAP, new SwapAction());
        actions.put(OpCodeEnums.PRINT, new PrintAction());
        actions.put(OpCodeEnums.PRINTC, new PrintCAction());
        actions.put(OpCodeEnums.NOP, new NopAction());
        actions.put(OpCodeEnums.HALT, new HaltAction());
        actions.put(OpCodeEnums.INC, new IncAction());
        actions.put(OpCodeEnums.DEC, new DecAction());
    }

    public Tvm(Translator translator, Stack stack, Printer printer) {
        this.translator = translator;
        this.stack = stack;
        this.printer = printer;
    }

    public void run(OpCode opCode, int startIdx) {
        opCode.setStartIdx(startIdx);
        while (opCode.hasNext()) {
            var word = opCode.getNextInstructions();
            var decimal = translator.binaryToDecimal(word.bitSet());
            var action = OpCodeEnums.values()[decimal];
            if (actions.containsKey(action)) {
                var data = 0;
                if (action.equals(OpCodeEnums.PUSH)) {
                    word = opCode.getNextInstructions();
                    data = translator.binaryToDecimal(word.bitSet());
                }
                actions.get(action).act(stack, data, printer);
            }

        }
    }
}
