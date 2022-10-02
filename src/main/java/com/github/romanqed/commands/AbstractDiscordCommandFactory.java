package com.github.romanqed.commands;

import com.github.romanqed.commands.filters.ArgumentFilterFactory;
import com.github.romanqed.jeflect.lambdas.Lambda;
import com.github.romanqed.jeflect.lambdas.LambdaFactory;
import com.github.romanqed.util.Action;
import net.dv8tion.jda.api.entities.Message;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public abstract class AbstractDiscordCommandFactory<T extends DiscordCommand> implements DiscordCommandFactory<T> {
    private final LambdaFactory lambdaFactory;
    private final ArgumentFilterFactory argumentFilterFactory;

    protected AbstractDiscordCommandFactory(LambdaFactory lambdaFactory, ArgumentFilterFactory argumentFilterFactory) {
        this.lambdaFactory = lambdaFactory;
        this.argumentFilterFactory = argumentFilterFactory;
    }

    protected void validate(Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length < 1) {
            throw new IllegalArgumentException("Invalid command method: " + method);
        }
        Class<?> clazz = parameters[0].getType();
        if (clazz != Message.class) {
            throw new IllegalArgumentException("Invalid command method parameter type: " + clazz);
        }
    }

    protected String extractDescription(Class<?> clazz) {
        Description description = clazz.getAnnotation(Description.class);
        if (description == null) {
            return "";
        }
        return description.value();
    }

    protected String extractHelp(Class<?> clazz) {
        Help help = clazz.getAnnotation(Help.class);
        if (help == null) {
            return "";
        }
        return help.value();
    }

    protected Method findMethod(Method[] methods) {
        if (methods.length == 1) {
            validate(methods[0]);
            return methods[0];
        }
        if (methods.length > 1) {
            for (Method method : methods) {
                if (method.isAnnotationPresent(CommandMethod.class)) {
                    validate(method);
                    return method;
                }
            }
        }
        throw new IllegalStateException("Command method not found");
    }

    protected Lambda packMethod(Method method) throws Exception {
        return lambdaFactory.packMethod(method, method.getDeclaringClass().getDeclaredConstructor().newInstance());
    }

    protected Action<List<String>, Object[]> createFilter(Method method) {
        return argumentFilterFactory.create(method);
    }
}
