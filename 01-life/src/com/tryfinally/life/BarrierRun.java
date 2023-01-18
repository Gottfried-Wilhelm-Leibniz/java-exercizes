package com.tryfinally.life;

import java.io.IOException;
import java.util.function.Supplier;

public class BarrierRun implements Runnable {
    private final String gameName;
    private final VoidApply swapAction;
    private final FileSaver fileSaver;
    private final Supplier<Grid<Boolean>> gridSupplier;

    public BarrierRun(VoidApply swapAction, Supplier<Grid<Boolean>> gridSupplier, String gameName, FileSaver fileSaver) {
        this.swapAction = swapAction;
        this.gameName = gameName;
        this.fileSaver = fileSaver;
        this.gridSupplier = gridSupplier;
    }
    @Override
    public void run() {
        try {
            fileSaver.save(gridSupplier.get(), (e) -> e ? 1 : 0, gameName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        swapAction.apply();
    }
}
