package com.robin.multiversal.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DelayedTaskManager {
    private static final List<DelayedBlockRestore> blockRestoreTasks = new ArrayList<>();
    private static final List<DelayedTask> delayedTasks = new ArrayList<>();

    // Existing block restore support
    public static void add(DelayedBlockRestore task) {
        blockRestoreTasks.add(task);
    }

    // New: Add general-purpose delayed task
    public static void add(DelayedTask task) {
        delayedTasks.add(task);
    }

    public static void tickAll() {
        // Tick and remove finished block restore tasks
        blockRestoreTasks.removeIf(DelayedBlockRestore::tick);

        // Tick and remove finished generic delayed tasks
        Iterator<DelayedTask> iterator = delayedTasks.iterator();
        while (iterator.hasNext()) {
            DelayedTask task = iterator.next();
            if (task.tick()) {
                iterator.remove();
            }
        }
    }
}