package com.github.romanqed.commands.codecs;

import java.util.function.Function;

@Codec(Character.class)
public class CharCodec implements Function<String, Character> {
    @Override
    public Character apply(String s) {
        return s.charAt(0);
    }
}
