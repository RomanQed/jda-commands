package com.github.romanqed.commands;

import org.atteo.classindex.ClassIndex;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static <T extends DiscordCommand> Map<String, T> findCommands(
            DiscordCommandFactory<T> factory,
            Class<? extends Annotation> annotation) throws Exception {
        Map<String, T> ret = new HashMap<>();
        Iterable<Class<?>> found = ClassIndex.getAnnotated(annotation);
        for (Class<?> clazz : found) {
            T command = factory.create(clazz);
            ret.put(command.getName(), command);
        }
        return Collections.unmodifiableMap(ret);
    }
}
