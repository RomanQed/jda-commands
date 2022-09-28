package com.github.romanqed.commands;

import com.github.romanqed.commands.exceptions.CommandAccessException;
import com.github.romanqed.commands.exceptions.CommandNotFoundException;
import com.github.romanqed.commands.exceptions.IllegalCommandArgumentsException;
import com.github.romanqed.commands.guild.GuildDiscordCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public abstract class JDAListenerAdapter extends ListenerAdapter {
    private final MessageHandler<GuildDiscordCommand> guild;
    private final MessageHandler<CommonDiscordCommand> personal;

    public JDAListenerAdapter(MessageHandler<GuildDiscordCommand> guild,
                              MessageHandler<CommonDiscordCommand> personal) {
        this.guild = Objects.requireNonNull(guild);
        this.personal = Objects.requireNonNull(personal);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        if (message.getAuthor().equals(event.getJDA().getSelfUser())) {
            return;
        }
        MessageHandler<?> handler = message.isFromGuild() ? guild : personal;
        try {
            handler.handle(getPrefix(message), message);
        } catch (CommandNotFoundException e) {
            onCommandNotFound(message, e.getCommand());
        } catch (IllegalCommandArgumentsException e) {
            onIllegalCommandArguments(message, e.getArguments());
        } catch (CommandAccessException e) {
            onCommandAccessException(message, e.getMember(), e.getCommand());
        } catch (Throwable e) {
            onException(message, e);
        }
    }

    protected abstract String getPrefix(Message message);

    protected abstract void onCommandNotFound(Message message, String command);

    protected abstract void onIllegalCommandArguments(Message message, List<String> arguments);

    protected abstract void onCommandAccessException(Message message, Member member, String command);

    protected abstract void onException(Message message, Throwable e);
}
