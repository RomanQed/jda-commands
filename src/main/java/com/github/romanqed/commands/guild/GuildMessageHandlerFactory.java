package com.github.romanqed.commands.guild;

import com.github.romanqed.commands.MessageHandler;
import com.github.romanqed.commands.MessageHandlerFactory;
import com.github.romanqed.commands.filters.ArgumentFilterFactory;
import com.github.romanqed.commands.filters.CodecFilterFactory;
import com.github.romanqed.jeflect.DefineLoader;
import com.github.romanqed.jeflect.lambdas.LambdaFactory;
import com.github.romanqed.util.Tokenizer;
import com.github.romanqed.util.concurrent.TaskFactory;
import org.atteo.classindex.ClassIndex;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GuildMessageHandlerFactory implements MessageHandlerFactory<GuildDiscordCommand> {
    private final Map<String, GuildDiscordCommand> commands;

    public GuildMessageHandlerFactory(DefineLoader loader) throws Exception {
        LambdaFactory lambdaFactory = new LambdaFactory(loader);
        ArgumentFilterFactory filterFactory = CodecFilterFactory.getInstance();
        GuildDiscordCommandFactory factory = new GuildDiscordCommandFactory(lambdaFactory, filterFactory);
        this.commands = findCommands(factory);
    }

    private static Map<String, GuildDiscordCommand> findCommands(GuildDiscordCommandFactory factory) throws Exception {
        Map<String, GuildDiscordCommand> ret = new HashMap<>();
        Iterable<Class<?>> found = ClassIndex.getAnnotated(GuildCommand.class);
        for (Class<?> clazz : found) {
            GuildDiscordCommand command = factory.create(clazz);
            ret.put(command.getName(), command);
        }
        return Collections.unmodifiableMap(ret);
    }

    @Override
    public MessageHandler<GuildDiscordCommand> create(Tokenizer tokenizer, TaskFactory factory) {
        return new GuildMessageHandler(commands, tokenizer, factory);
    }
}
