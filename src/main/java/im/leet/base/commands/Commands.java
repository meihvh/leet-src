/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.commands;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventChatMessage;
import im.leet.base.commands.Command;
import im.leet.base.commands.impl.CompileCommand;
import im.leet.base.commands.impl.ConfigCommand;
import im.leet.base.commands.impl.GpsCommand;
import im.leet.base.commands.impl.HelpCommand;
import im.leet.base.commands.impl.HotswapCommand;
import java.util.ArrayList;
import java.util.Arrays;

public class Commands
implements MinecraftHolder {
    public ArrayList<Command> commands = new ArrayList();
    EventBus<Event> events = event -> {
        if (event instanceof EventChatMessage) {
            EventChatMessage ev = (EventChatMessage)event;
            if (ev.message.startsWith(".")) {
                String[] args_raw = ev.message.split(" ");
                String cmd = args_raw[0].replaceAll("^[.]", "");
                for (Command command : this.commands) {
                    if (!command.getName().equalsIgnoreCase(cmd)) continue;
                    if (Client.IS_PANIC) {
                        return;
                    }
                    ev.cancel();
                    command.execute(args_raw);
                }
            }
        }
    };

    public Commands() {
        this.commands.addAll(Arrays.asList(ConfigCommand.INSTANCE, HelpCommand.INSTANCE, HotswapCommand.INSTANCE, CompileCommand.INSTANCE, GpsCommand.INSTANCE));
        Client.EVENTS.register(this);
    }

    public ArrayList<Command> getCommands() {
        return this.commands;
    }

    public EventBus<Event> getEvents() {
        return this.events;
    }
}

