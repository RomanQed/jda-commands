package com.github.romanqed.commands.filters;

import com.github.romanqed.commands.Optional;
import com.github.romanqed.commands.codecs.Codec;
import com.github.romanqed.util.Action;
import org.atteo.classindex.ClassIndex;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

public class CodecFilterFactory implements ArgumentFilterFactory {
    private final Map<Class<?>, Function<String, Object>> codecs;

    private CodecFilterFactory(Map<Class<?>, Function<String, Object>> codecs) {
        this.codecs = codecs;
    }

    @SuppressWarnings("unchecked")
    public static CodecFilterFactory getInstance() throws Exception {
        Map<Class<?>, Function<String, Object>> ret = new HashMap<>();
        Iterable<Class<?>> found = ClassIndex.getAnnotated(Codec.class);
        for (Class<?> clazz : found) {
            Codec codec = clazz.getAnnotation(Codec.class);
            Function<String, Object> toAdd = (Function<String, Object>) clazz.getDeclaredConstructor().newInstance();
            ret.put(codec.value(), toAdd);
        }
        return new CodecFilterFactory(Collections.unmodifiableMap(ret));
    }

    private Function<String, Object> findCodec(Class<?> type) {
        var ret = codecs.get(type);
        if (ret == null) {
            throw new IllegalArgumentException("Codec for type " + type + " not found");
        }
        return ret;
    }

    @Override
    public Action<List<String>, Object[]> create(Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length < 2) {
            return new EmptyArgumentFilter();
        }
        List<Function<String, Object>> fixed = new ArrayList<>();
        int index = 1;
        for (; index < parameters.length; ++index) {
            Parameter parameter = parameters[index];
            if (parameter.isAnnotationPresent(Optional.class)) {
                break;
            }
            fixed.add(findCodec(parameter.getType()));
        }
        List<Function<String, Object>> optional = new ArrayList<>();
        for (; index < parameters.length; ++index) {
            Parameter parameter = parameters[index];
            if (!parameter.isAnnotationPresent(Optional.class)) {
                throw new IllegalArgumentException("Non optional argument after optional");
            }
            optional.add(findCodec(parameter.getType()));
        }
        return new CommonArgumentFilter(fixed, optional);
    }
}
