package newtvm.codeword;

import newtvm.Counter;
import newtvm.actions.*;
import newtvm.context.Context;

import java.util.HashMap;
import java.util.Map;

public enum Instruction implements CodeWord {
    ADD(100, new AddAction()),
    SUB(101, new SubAction()),
    MUL(102, new MulAction()),
    DIV(103, new DivAction()),
    POP(104, new PopAction()),
    PUSH(105, new PushAction()),
    DUP(106, new DupAction()),
    SWAP(107, new SwapAction()),
    PRINT(108, new PrintAction()),
    PRINTC(109, new PrintCAction()),
    NOP(110, new NopAction()),
    HALT(111, new HaltAction()),
    INC(112, new IncAction()),
    DEC(113, new DecAction()),
    JMP(114, new JmpAction()),
    JZ(115, new JzAction()),
    JNZ(116, new JnzAction()),
    CALL(117, new CallAction()),
    RET(118, new RetAction()),
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

    Instruction(int code, OpAction action) {
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
