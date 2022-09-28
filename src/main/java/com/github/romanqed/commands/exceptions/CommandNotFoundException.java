package com.github.romanqed.commands.exceptions;

public class CommandNotFoundException extends IllegalArgumentException {
    private final String command;

    public CommandNotFoundException(String command) {
        super("Command " + command + " not found");
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
