package com.github.romanqed.commands.guild;

import com.github.romanqed.commands.CommandMethod;
import com.github.romanqed.commands.DiscordCommandFactory;
import com.github.romanqed.commands.filters.ArgumentFilterFactory;
import com.github.romanqed.jeflect.ReflectUtil;
import com.github.romanqed.jeflect.lambdas.Lambda;
import com.github.romanqed.jeflect.lambdas.LambdaFactory;
import com.github.romanqed.util.Action;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuildDiscordCommandFactory implements DiscordCommandFactory<GuildDiscordCommand> {
    private final LambdaFactory lambdaFactory;
    private final ArgumentFilterFactory argumentFilterFactory;

    public GuildDiscordCommandFactory(LambdaFactory lambdaFactory, ArgumentFilterFactory argumentFilterFactory) {
        this.lambdaFactory = lambdaFactory;
        this.argumentFilterFactory = argumentFilterFactory;
    }

    private void validate(Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length < 1) {
            throw new IllegalArgumentException("Invalid command method: " + method);
        }
        Class<?> clazz = parameters[0].getType();
        if (clazz != Message.class) {
            throw new IllegalArgumentException("Invalid command method parameter type: " + clazz);
        }
    }

    private Method findMethod(Method[] methods) {
        if (methods.length == 0) {
            return null;
        }
        if (methods.length == 1) {
            validate(methods[0]);
            return methods[0];
        }
        for (Method method : methods) {
            if (method.isAnnotationPresent(CommandMethod.class)) {
                validate(method);
                return method;
            }
        }
        return null;
    }

    private <T> Set<T> extractData(Annotation annotation) throws NoSuchMethodException {
        Set<T> ret = new HashSet<>();
        if (annotation == null) {
            return ret;
        }
        T[] data = ReflectUtil.extractAnnotationValue(annotation);
        ret.addAll(Arrays.asList(data));
        return ret;
    }

    @Override
    public GuildDiscordCommand create(Class<?> clazz) throws Exception {
        // Check if class contains GuildCommand annotation
        GuildCommand command = clazz.getAnnotation(GuildCommand.class);
        if (command == null) {
            throw new IllegalArgumentException("Class does not contain GuildCommand annotation");
        }
        // Try to locate command method
        Method found = findMethod(clazz.getDeclaredMethods());
        if (found == null) {
            throw new IllegalStateException("Command method not found");
        }
        // Create filter
        Action<List<String>, Object[]> filter = argumentFilterFactory.create(found);
        // Extract permissions and roles
        Set<Permission> permissions = extractData(clazz.getAnnotation(GuildPermission.class));
        Set<String> roles = extractData(clazz.getAnnotation(GuildRole.class));
        // Pack command method to lambda
        Lambda packed = lambdaFactory.packMethod(found, clazz.getDeclaredConstructor().newInstance());
        return new GuildDiscordCommand(command.value(), packed, filter, permissions, roles);
    }
}
