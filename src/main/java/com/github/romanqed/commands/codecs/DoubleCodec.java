package com.github.romanqed.commands.codecs;

import java.util.function.Function;

@Codec(Double.class)
public class DoubleCodec implements Function<String, Double> {
    @Override
    public Double apply(String s) {
        return Double.parseDouble(s);
    }
}
