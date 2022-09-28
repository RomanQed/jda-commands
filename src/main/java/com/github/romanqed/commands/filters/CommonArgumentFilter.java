package com.github.romanqed.commands.filters;

import com.github.romanqed.commands.exceptions.IllegalCommandArgumentsException;
import com.github.romanqed.util.Action;

import java.util.List;
import java.util.function.Function;

class CommonArgumentFilter implements Action<List<String>, Object[]> {
    private final List<Function<String, Object>> fixed;
    private final List<Function<String, Object>> optional;

    CommonArgumentFilter(List<Function<String, Object>> fixed, List<Function<String, Object>> optional) {
        this.fixed = fixed;
        this.optional = optional;
    }

    @Override
    public Object[] execute(List<String> arguments) throws Throwable {
        Object[] ret = new Object[fixed.size() + optional.size()];
        if (arguments.size() < fixed.size() || arguments.size() > ret.length) {
            throw new IllegalCommandArgumentsException(arguments);
        }
        int index = 0;
        for (Function<String, Object> codec : fixed) {
            ret[index] = codec.apply(arguments.get(index));
            ++index;
        }
        for (Function<String, Object> codec : optional) {
            if (index >= arguments.size()) {
                break;
            }
            ret[index] = codec.apply(arguments.get(index));
            ++index;
        }
        return ret;
    }
}
