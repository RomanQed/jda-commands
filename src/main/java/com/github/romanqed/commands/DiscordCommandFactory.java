package com.github.romanqed.commands;

public interface DiscordCommandFactory<T extends DiscordCommand> {
    T create(Class<?> clazz) throws Exception;
}
