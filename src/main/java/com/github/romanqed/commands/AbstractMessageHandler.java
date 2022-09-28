package com.github.romanqed.commands;

import com.github.romanqed.commands.exceptions.CommandNotFoundException;
import com.github.romanqed.util.Handler;
import com.github.romanqed.util.Tokenizer;
import net.dv8tion.jda.api.entities.Message;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractMessageHandler<T extends DiscordCommand> implements MessageHandler<T> {
    private final Map<String, T> commands;
    private final Tokenizer tokenizer;

    protected AbstractMessageHandler(Map<String, T> commands, Tokenizer tokenizer) {
        this.commands = Collections.unmodifiableMap(commands);
        this.tokenizer = tokenizer;
    }

    @Override
    public Map<String, T> getCommands() {
        return commands;
    }

    @SuppressWarnings("unchecked")
    protected void handle(String prefix, Message message, Handler<T> handler) throws Throwable {
        // Check if command starts with prefix
        String content = message.getContentRaw();
        if (!content.startsWith(prefix)) {
            return;
        }
        // Try to tokenize command
        List<String> tokenized = tokenizer.tokenize(content.replace(prefix, ""));
        if (tokenized.isEmpty()) {
            return;
        }
        // Try to find command
        T command = commands.get(tokenized.get(0));
        if (command == null) {
            throw new CommandNotFoundException(tokenized.get(0));
        }
        handler.handle(command);
        // Deserialize command arguments
        int size = tokenized.size();
        Object[] data = command
                .getArgumentFilter()
                .execute(size > 1 ? tokenized.subList(1, size) : Collections.EMPTY_LIST);
        // Copy arguments to array with message object
        Object[] arguments = new Object[data.length + 1];
        arguments[0] = message;
        System.arraycopy(data, 0, arguments, 1, data.length);
        // Execute command
        command.getBody().call(arguments);
    }
}
