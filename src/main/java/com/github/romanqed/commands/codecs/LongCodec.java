package com.github.romanqed.commands.codecs;

import java.util.function.Function;

@Codec(Long.class)
public class LongCodec implements Function<String, Long> {
    @Override
    public Long apply(String s) {
        return Long.parseLong(s);
    }
}
