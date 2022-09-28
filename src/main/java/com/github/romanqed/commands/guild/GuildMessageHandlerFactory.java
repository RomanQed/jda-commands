package com.github.romanqed.commands.guild;

import com.github.romanqed.commands.MessageHandler;
import com.github.romanqed.commands.MessageHandlerFactory;
import com.github.romanqed.commands.Util;
import com.github.romanqed.commands.filters.ArgumentFilterFactory;
import com.github.romanqed.commands.filters.CodecFilterFactory;
import com.github.romanqed.jeflect.DefineClassLoader;
import com.github.romanqed.jeflect.DefineLoader;
import com.github.romanqed.jeflect.lambdas.LambdaFactory;
import com.github.romanqed.util.Tokenizer;

import java.util.Map;

public class GuildMessageHandlerFactory implements MessageHandlerFactory<GuildDiscordCommand> {
    private final Map<String, GuildDiscordCommand> commands;

    public GuildMessageHandlerFactory(DefineLoader loader) {
        try {
            LambdaFactory lambdaFactory = new LambdaFactory(loader);
            ArgumentFilterFactory filterFactory = CodecFilterFactory.getInstance();
            GuildDiscordCommandFactory factory = new GuildDiscordCommandFactory(lambdaFactory, filterFactory);
            this.commands = Util.findCommands(factory, GuildCommand.class);
        } catch (Exception e) {
            throw new IllegalStateException("Can't initialize factory due to", e);
        }
    }

    public GuildMessageHandlerFactory() {
        this(new DefineClassLoader());
    }

    @Override
    public MessageHandler<GuildDiscordCommand> create(Tokenizer tokenizer) {
        return new GuildMessageHandler(commands, tokenizer);
    }
}
