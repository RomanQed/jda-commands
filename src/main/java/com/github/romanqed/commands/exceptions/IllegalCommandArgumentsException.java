package com.github.romanqed.commands.exceptions;

import java.util.List;

public class IllegalCommandArgumentsException extends IllegalArgumentException {
    private final List<String> arguments;

    public IllegalCommandArgumentsException(List<String> arguments) {
        super("Invalid command arguments are received");
        this.arguments = arguments;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
