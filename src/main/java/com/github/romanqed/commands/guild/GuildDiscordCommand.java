package com.github.romanqed.commands.guild;

import com.github.romanqed.commands.CommonDiscordCommand;
import com.github.romanqed.jeflect.lambdas.Lambda;
import com.github.romanqed.util.Action;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GuildDiscordCommand extends CommonDiscordCommand {
    private final Set<Permission> permissions;
    private final Set<String> roles;

    public GuildDiscordCommand(String name,
                               Lambda body,
                               Action<List<String>, Object[]> filter,
                               Set<Permission> permissions, Set<String> roles) {
        super(name, body, filter);
        this.permissions = Collections.unmodifiableSet(permissions);
        this.roles = Collections.unmodifiableSet(roles);
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public boolean canBeExecutedWithRoles(Set<String> roles) {
        return roles.containsAll(this.roles);
    }

    public boolean canBeExecutedWithPermissions(Set<Permission> permissions) {
        return permissions.containsAll(this.permissions);
    }

    public boolean canBeExecutedByMember(Member member) {
        Set<String> roles = member.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return canBeExecutedWithRoles(roles) && member.hasPermission(permissions);
    }
}
