/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.commands.impl;

import im.leet.Client;
import im.leet.base.commands.Command;
import im.leet.utils.client.ChatUtility;

public class ConfigCommand
extends Command {
    public static ConfigCommand INSTANCE = new ConfigCommand();

    public ConfigCommand() {
        super("cfg", "manage configs");
    }

    @Override
    public void execute(String[] args) {
        String path;
        if (args.length < 2) {
            this.error("usage .cfg <load | save | list>");
            return;
        }
        switch (path = args[1]) {
            case "save": {
                if (args.length == 3) {
                    Client.CONFIG.save(args[2]);
                    ChatUtility.send("config saved");
                    break;
                }
                this.error("usage .cfg <load | save | list>");
                break;
            }
            case "load": {
                if (args.length == 3) {
                    Client.CONFIG.load(args[2]);
                    ChatUtility.send("config loaded");
                    break;
                }
                this.error("usage .cfg <load | save | list>");
                break;
            }
            case "list": {
                ChatUtility.send("config list:");
                for (String cfg : Client.CONFIG.list()) {
                    ChatUtility.send(cfg);
                }
                break;
            }
        }
    }
}

