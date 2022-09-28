package com.github.romanqed.commands.guild;

import com.github.romanqed.commands.exceptions.CommandAccessException;
import com.github.romanqed.commands.exceptions.CommandNotFoundException;
import com.github.romanqed.commands.MessageHandler;
import com.github.romanqed.util.QuoteTokenizer;
import com.github.romanqed.util.Tokenizer;
import com.github.romanqed.util.concurrent.TaskFactory;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GuildMessageHandler implements MessageHandler<GuildDiscordCommand> {
    private final Map<String, GuildDiscordCommand> commands;
    private final Tokenizer tokenizer;
    private final TaskFactory factory;

    public GuildMessageHandler(Map<String, GuildDiscordCommand> commands, Tokenizer tokenizer, TaskFactory factory) {
        this.commands = Collections.unmodifiableMap(commands);
        this.tokenizer = tokenizer;
        this.factory = factory;
    }

    public GuildMessageHandler(Map<String, GuildDiscordCommand> commands, Tokenizer tokenizer) {
        this(commands, tokenizer, null);
    }

    public GuildMessageHandler(Map<String, GuildDiscordCommand> commands) {
        this(commands, new QuoteTokenizer(), null);
    }

    @Override
    public TaskFactory getTaskFactory() {
        return factory;
    }

    @Override
    public Map<String, GuildDiscordCommand> getCommands() {
        return commands;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(String prefix, Message message) throws Throwable {
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
        GuildDiscordCommand command = commands.get(tokenized.get(0));
        if (command == null) {
            throw new CommandNotFoundException(tokenized.get(0));
        }
        // Try to get member
        Member member = message.getMember();
        if (member == null) {
            throw new IllegalStateException("Passed message do not has member data");
        }
        // Check if member has perms to execute command
        if (!command.canBeExecutedByMember(member)) {
            throw new CommandAccessException(command.getName(), member);
        }
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
