package com.github.romanqed.commands.codecs;

import java.util.function.Function;

@Codec(String.class)
public class StringCodec implements Function<String, Object> {
    @Override
    public Object apply(String s) {
        return s;
    }
}
