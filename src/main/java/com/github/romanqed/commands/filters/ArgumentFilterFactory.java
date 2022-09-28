package com.github.romanqed.commands.filters;

import com.github.romanqed.util.Action;

import java.lang.reflect.Method;
import java.util.List;

public interface ArgumentFilterFactory {
    Action<List<String>, Object[]> create(Method method);
}
