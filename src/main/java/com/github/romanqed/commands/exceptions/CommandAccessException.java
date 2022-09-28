package com.github.romanqed.commands.exceptions;

import net.dv8tion.jda.api.entities.Member;

public class CommandAccessException extends IllegalAccessException {
    private final Member member;
    private final String command;

    public CommandAccessException(String command, Member member) {
        super("Member " + member.getIdLong() + " can't execute " + command + " command");
        this.command = command;
        this.member = member;
    }

    public String getCommand() {
        return command;
    }

    public Member getMember() {
        return member;
    }
}
