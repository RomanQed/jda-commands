package com.github.romanqed.commands.guild;

import com.github.romanqed.commands.AbstractMessageHandler;
import com.github.romanqed.commands.exceptions.CommandAccessException;
import com.github.romanqed.util.QuoteTokenizer;
import com.github.romanqed.util.Tokenizer;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.Map;

public class GuildMessageHandler extends AbstractMessageHandler<GuildDiscordCommand> {
    public GuildMessageHandler(Map<String, GuildDiscordCommand> commands, Tokenizer tokenizer) {
        super(commands, tokenizer);
    }

    public GuildMessageHandler(Map<String, GuildDiscordCommand> commands) {
        this(commands, new QuoteTokenizer());
    }

    @Override
    public void handle(String prefix, Message message) throws Throwable {
        super.handle(prefix, message, command -> {
            // Try to get member
            Member member = message.getMember();
            if (member == null) {
                throw new IllegalStateException("Passed message do not has member data");
            }
            // Check if member has perms to execute command
            if (!command.canBeExecutedByMember(member)) {
                throw new CommandAccessException(command.getName(), member);
            }
        });
    }
}
