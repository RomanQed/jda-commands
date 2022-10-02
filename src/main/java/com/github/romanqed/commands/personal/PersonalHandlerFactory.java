package com.github.romanqed.commands.personal;

import com.github.romanqed.commands.CommonDiscordCommand;
import com.github.romanqed.commands.MessageHandler;
import com.github.romanqed.commands.MessageHandlerFactory;
import com.github.romanqed.commands.Util;
import com.github.romanqed.commands.filters.ArgumentFilterFactory;
import com.github.romanqed.commands.filters.CodecFilterFactory;
import com.github.romanqed.jeflect.lambdas.LambdaFactory;
import com.github.romanqed.util.Tokenizer;

import java.util.Map;

public class PersonalHandlerFactory implements MessageHandlerFactory<CommonDiscordCommand> {
    private static final Map<String, CommonDiscordCommand> COMMANDS;

    static {
        try {
            LambdaFactory lambdaFactory = new LambdaFactory();
            ArgumentFilterFactory filterFactory = CodecFilterFactory.getInstance();
            var factory = new PersonalDiscordCommandFactory(lambdaFactory, filterFactory);
            COMMANDS = Util.findCommands(factory, PersonalCommand.class);
        } catch (Exception e) {
            throw new IllegalStateException("Can't initialize factory due to", e);
        }
    }

    public static Map<String, CommonDiscordCommand> getCommands() {
        return COMMANDS;
    }

    @Override
    public MessageHandler<CommonDiscordCommand> create(Tokenizer tokenizer) {
        return new PersonalMessageHandler(COMMANDS, tokenizer);
    }
}
