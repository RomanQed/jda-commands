package com.github.romanqed.commands;

import com.github.romanqed.util.concurrent.Task;
import com.github.romanqed.util.concurrent.TaskFactory;
import net.dv8tion.jda.api.entities.Message;

import java.util.Map;

public interface MessageHandler<T extends DiscordCommand> {
    TaskFactory getTaskFactory();

    default Task<Void> getTask(String prefix, Message message) {
        TaskFactory factory = getTaskFactory();
        if (factory == null) {
            throw new IllegalStateException("This handler does not support task creating");
        }
        return factory.createTask(() -> {
            try {
                handle(prefix, message);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    Map<String, T> getCommands();

    void handle(String prefix, Message message) throws Throwable;
}
