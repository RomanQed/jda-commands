package com.github.romanqed.commands;

import com.github.romanqed.util.QuoteTokenizer;
import com.github.romanqed.util.Tokenizer;
import com.github.romanqed.util.concurrent.TaskFactory;

public interface MessageHandlerFactory<T extends DiscordCommand> {
    MessageHandler<T> create(Tokenizer tokenizer, TaskFactory factory);

    default MessageHandler<T> create(Tokenizer tokenizer) {
        return create(tokenizer, null);
    }

    default MessageHandler<T> create() {
        return create(new QuoteTokenizer(), null);
    }
}
