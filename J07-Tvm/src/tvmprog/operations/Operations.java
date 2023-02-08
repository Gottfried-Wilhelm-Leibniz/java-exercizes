package tvmprog.operations;

import tvmprog.stack.Stack;

public class Operations {
        private final Print print;
        private final SetAddress setAddress;
        private final GetData getData;
        private final GetAddress getAddress;
        private final Stack stack;
        private final Stack callStack;

    public Stack getStack() {
        return stack;
    }

    public Stack getCallStack() {
        return callStack;
    }

    public Operations(Print print, SetAddress setAddress, GetData getData, GetAddress getAddress, Stack stack, Stack callStack) {
        this.print = print;
        this.setAddress = setAddress;
        this.getData = getData;
        this.getAddress = getAddress;
        this.stack = stack;
        this.callStack = callStack;
    }

    public void print(Character toPrint) {
        print.printIt(toPrint);
    }
    public void setAddress(int newIndex) {
        setAddress.setIt(newIndex);
    }
    public int getData() {
        return getData.getIt();
    }
    public int getAddress() {
        return getAddress.getIt();
    }
}
