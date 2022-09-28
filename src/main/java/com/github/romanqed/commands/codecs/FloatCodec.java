package com.github.romanqed.commands.codecs;

import java.util.function.Function;

@Codec(Float.class)
public class FloatCodec implements Function<String, Float> {
    @Override
    public Float apply(String s) {
        return Float.parseFloat(s);
    }
}
