package com.github.romanqed.commands.filters;

import com.github.romanqed.commands.exceptions.IllegalCommandArgumentsException;
import com.github.romanqed.util.Action;

import java.util.List;

final class EmptyArgumentFilter implements Action<List<String>, Object[]> {
    private static final Object[] EMPTY_ARRAY = new Object[0];

    @Override
    public Object[] execute(List<String> arguments) {
        if (!arguments.isEmpty()) {
            throw new IllegalCommandArgumentsException(arguments);
        }
        return EMPTY_ARRAY;
    }
}
