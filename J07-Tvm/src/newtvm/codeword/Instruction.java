package newtvm.codeword;

import newtvm.Counter;
import newtvm.actions.*;
import newtvm.context.Context;

import java.util.HashMap;
import java.util.Map;

public enum Instruction implements CodeWord {
    ADD(new AddAction()),
    SUB(new SubAction()),
    MUL(new MulAction()),
    DIV(new DivAction()),
    POP(new PopAction()),
    PUSH(new PushAction()),
    DUP(new DupAction()),
    SWAP(new SwapAction()),
    PRINT(new PrintAction()),
    PRINTC(new PrintCAction()),
    NOP(new NopAction()),
    HALT(new HaltAction()),
    INC(new IncAction()),
    DEC(new DecAction()),
    JMP(new JmpAction()),
    JZ(new JzAction()),
    JNZ(new JnzAction()),
    CALL(new CallAction()),
    RET(new RetAction()),
    ;
    private static final Map<Integer, Instruction> instructionList;
    private final int code;
    private final OpAction action;

    static {
        var cap = (int)(Instruction.values().length * 1.5);
        instructionList = new HashMap<>(cap);
        var arr = Instruction.values();
        for (int i = 0; i < arr.length; i++) {
            instructionList.put(arr[i].code, arr[i]);
        }
    }

    Instruction(OpAction action) {
        this.code = Counter.value++;
        this.action = action;
    }
    public static Map<Integer, Instruction> getInstructionMap() {
        return Map.copyOf(instructionList);
    }

    @Override
    public void execute(Context context) {
       action.act(context);
    }

    @Override
    public int data() {
        return code;
    }
}
