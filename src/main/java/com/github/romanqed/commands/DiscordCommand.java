package com.github.romanqed.commands;

import com.github.romanqed.jeflect.lambdas.Lambda;
import com.github.romanqed.util.Action;

import java.util.List;

public interface DiscordCommand {
    String getName();

    String getDescription();

    String getHelp();

    Lambda getBody();

    Action<List<String>, Object[]> getArgumentFilter();
}
