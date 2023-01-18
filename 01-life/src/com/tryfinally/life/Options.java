package com.tryfinally.life;

public class Options {
    private final String fileName;
    private final int iters;
    private final int threads;
    private final int width;
    private final int height;
    public Options(String args[]) {
        if (args.length != 5) {
            this.fileName = "life";
            this.iters = 100;
            this.threads = 4;
            this.width = 100;
            this.height = 100;
        }
        else {
            this.fileName = args[0];
            this.iters = parseInt(args[1], 20);
            this.threads = parseInt(args[2], 1);
            this.width = parseInt(args[3], 100);
            this.height = parseInt(args[4], 100);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public int getIters() {
        return iters;
    }

    public int getThreads() {
        return threads;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private int parseInt(String s, int num) {
        try {
            num = Integer.valueOf(s);
        } catch (NumberFormatException x) {
        }
        return num;
    }
}
