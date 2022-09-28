package com.github.romanqed.commands.personal;

import com.github.romanqed.commands.AbstractDiscordCommandFactory;
import com.github.romanqed.commands.CommonDiscordCommand;
import com.github.romanqed.commands.filters.ArgumentFilterFactory;
import com.github.romanqed.jeflect.lambdas.Lambda;
import com.github.romanqed.jeflect.lambdas.LambdaFactory;
import com.github.romanqed.util.Action;

import java.lang.reflect.Method;
import java.util.List;

public class PersonalDiscordCommandFactory extends AbstractDiscordCommandFactory<CommonDiscordCommand> {
    protected PersonalDiscordCommandFactory(LambdaFactory lambdaFactory, ArgumentFilterFactory argumentFilterFactory) {
        super(lambdaFactory, argumentFilterFactory);
    }

    @Override
    public CommonDiscordCommand create(Class<?> clazz) throws Exception {
        // Check if class contains PersonalCommand annotation
        PersonalCommand command = clazz.getAnnotation(PersonalCommand.class);
        if (command == null) {
            throw new IllegalArgumentException("Class does not contain GuildCommand annotation");
        }
        // Try to locate command method
        Method found = findMethod(clazz.getDeclaredMethods());
        // Create filter
        Action<List<String>, Object[]> filter = createFilter(found);
        // Pack command method to lambda
        Lambda packed = packMethod(found);
        return new CommonDiscordCommand(command.value(), packed, filter);
    }
}
