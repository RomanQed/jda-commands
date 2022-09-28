package com.github.romanqed.commands.personal;

import com.github.romanqed.commands.AbstractMessageHandler;
import com.github.romanqed.commands.CommonDiscordCommand;
import com.github.romanqed.util.QuoteTokenizer;
import com.github.romanqed.util.Tokenizer;
import net.dv8tion.jda.api.entities.Message;

import java.util.Map;

public class PersonalMessageHandler extends AbstractMessageHandler<CommonDiscordCommand> {
    public PersonalMessageHandler(Map<String, CommonDiscordCommand> commands, Tokenizer tokenizer) {
        super(commands, tokenizer);
    }

    public PersonalMessageHandler(Map<String, CommonDiscordCommand> commands) {
        super(commands, new QuoteTokenizer());
    }

    @Override
    public void handle(String prefix, Message message) throws Throwable {
        super.handle(prefix, message, command -> {
        });
    }
}
