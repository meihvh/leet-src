/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.commands.impl;

import im.leet.Client;
import im.leet.base.commands.Command;
import im.leet.utils.client.ChatUtility;

public class HelpCommand
extends Command {
    public static HelpCommand INSTANCE = new HelpCommand();

    public HelpCommand() {
        super("help", "shows this list");
    }

    @Override
    public void execute(String[] args) {
        for (Command command : Client.COMMANDS.getCommands()) {
            ChatUtility.send(String.format("%s - %s", command.getName(), command.getDescription()));
        }
    }
}

