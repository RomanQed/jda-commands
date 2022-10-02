package com.github.romanqed.commands.guild;

import com.github.romanqed.commands.MessageHandler;
import com.github.romanqed.commands.MessageHandlerFactory;
import com.github.romanqed.commands.Util;
import com.github.romanqed.commands.filters.ArgumentFilterFactory;
import com.github.romanqed.commands.filters.CodecFilterFactory;
import com.github.romanqed.jeflect.lambdas.LambdaFactory;
import com.github.romanqed.util.Tokenizer;

import java.util.Map;

public class GuildHandlerFactory implements MessageHandlerFactory<GuildDiscordCommand> {
    private static final Map<String, GuildDiscordCommand> COMMANDS;

    static {
        try {
            LambdaFactory lambdaFactory = new LambdaFactory();
            ArgumentFilterFactory filterFactory = CodecFilterFactory.getInstance();
            var factory = new GuildDiscordCommandFactory(lambdaFactory, filterFactory);
            COMMANDS = Util.findCommands(factory, GuildCommand.class);
        } catch (Exception e) {
            throw new IllegalStateException("Can't initialize factory due to", e);
        }
    }

    public static Map<String, GuildDiscordCommand> getCommands() {
        return COMMANDS;
    }

    @Override
    public MessageHandler<GuildDiscordCommand> create(Tokenizer tokenizer) {
        return new GuildMessageHandler(COMMANDS, tokenizer);
    }
}
