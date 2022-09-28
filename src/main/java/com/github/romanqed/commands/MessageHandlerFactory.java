package com.github.romanqed.commands;

import com.github.romanqed.util.QuoteTokenizer;
import com.github.romanqed.util.Tokenizer;

public interface MessageHandlerFactory<T extends DiscordCommand> {
    MessageHandler<T> create(Tokenizer tokenizer);

    default MessageHandler<T> create() {
        return create(new QuoteTokenizer());
    }
}
