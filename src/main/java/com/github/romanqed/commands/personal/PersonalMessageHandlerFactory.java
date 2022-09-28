package com.github.romanqed.commands.personal;

import com.github.romanqed.commands.CommonDiscordCommand;
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

public class PersonalMessageHandlerFactory implements MessageHandlerFactory<CommonDiscordCommand> {
    private final Map<String, CommonDiscordCommand> commands;

    public PersonalMessageHandlerFactory(DefineLoader loader) {
        try {
            LambdaFactory lambdaFactory = new LambdaFactory(loader);
            ArgumentFilterFactory filterFactory = CodecFilterFactory.getInstance();
            PersonalDiscordCommandFactory factory = new PersonalDiscordCommandFactory(lambdaFactory, filterFactory);
            this.commands = Util.findCommands(factory, PersonalCommand.class);
        } catch (Exception e) {
            throw new IllegalStateException("Can't initialize factory due to", e);
        }
    }

    public PersonalMessageHandlerFactory() {
        this(new DefineClassLoader());
    }

    @Override
    public MessageHandler<CommonDiscordCommand> create(Tokenizer tokenizer) {
        return new PersonalMessageHandler(commands, tokenizer);
    }
}
