package com.robin.multiversal.util;

public class DelayedTask {
    private final int delayTicks;
    private final Runnable task;
    private int ticksWaited = 0;

    public DelayedTask(int delayTicks, Runnable task) {
        this.delayTicks = delayTicks;
        this.task = task;
    }

    public boolean tick() {
        ticksWaited++;
        if (ticksWaited >= delayTicks) {
            task.run();
            return true;
        }
        return false;
    }
}