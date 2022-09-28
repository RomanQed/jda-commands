package com.github.romanqed.commands.codecs;

import java.util.function.Function;

@Codec(Integer.class)
public class IntegerCodec implements Function<String, Integer> {
    @Override
    public Integer apply(String s) {
        return Integer.parseInt(s);
    }
}
