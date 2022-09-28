package com.github.romanqed.commands;

import com.github.romanqed.jeflect.lambdas.Lambda;
import com.github.romanqed.util.Action;

import java.util.List;

public class CommonDiscordCommand implements DiscordCommand {
    private final String name;
    private final Lambda body;
    private final Action<List<String>, Object[]> filter;
    private String help;
    private String description;

    protected CommonDiscordCommand(String name, Lambda body, Action<List<String>, Object[]> filter) {
        this.name = name;
        this.body = body;
        this.filter = filter;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHelp() {
        return help;
    }

    @Override
    public Lambda getBody() {
        return body;
    }

    @Override
    public Action<List<String>, Object[]> getArgumentFilter() {
        return filter;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommonDiscordCommand)) return false;
        CommonDiscordCommand that = (CommonDiscordCommand) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
