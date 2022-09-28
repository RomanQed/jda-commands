package com.github.romanqed.commands;

import net.dv8tion.jda.api.entities.Message;

import java.util.Map;

public interface MessageHandler<T extends DiscordCommand> {
    Map<String, T> getCommands();

    void handle(String prefix, Message message) throws Throwable;
}
