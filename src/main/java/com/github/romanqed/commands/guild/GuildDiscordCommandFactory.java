package com.github.romanqed.commands.guild;

import com.github.romanqed.commands.AbstractDiscordCommandFactory;
import com.github.romanqed.commands.filters.ArgumentFilterFactory;
import com.github.romanqed.jeflect.ReflectUtil;
import com.github.romanqed.jeflect.lambdas.Lambda;
import com.github.romanqed.jeflect.lambdas.LambdaFactory;
import com.github.romanqed.util.Action;
import net.dv8tion.jda.api.Permission;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuildDiscordCommandFactory extends AbstractDiscordCommandFactory<GuildDiscordCommand> {
    public GuildDiscordCommandFactory(LambdaFactory lambdaFactory, ArgumentFilterFactory argumentFilterFactory) {
        super(lambdaFactory, argumentFilterFactory);
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
        // Create filter
        Action<List<String>, Object[]> filter = createFilter(found);
        // Extract permissions and roles
        Set<Permission> permissions = extractData(clazz.getAnnotation(GuildPermission.class));
        Set<String> roles = extractData(clazz.getAnnotation(GuildRole.class));
        // Pack command method to lambda
        Lambda packed = packMethod(found);
        return new GuildDiscordCommand(command.value(), packed, filter, permissions, roles);
    }
}
