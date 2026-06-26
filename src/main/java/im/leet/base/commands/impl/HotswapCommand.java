/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.commands.impl;

import im.leet.Client;
import im.leet.base.commands.Command;
import im.leet.utils.client.ChatUtility;

public class HotswapCommand
extends Command {
    public static HotswapCommand INSTANCE = new HotswapCommand();

    public HotswapCommand() {
        super("hotswap", "reloading scripts");
    }

    @Override
    public void execute(String[] args) {
        Client.SCRIPTS.RELOAD();
        ChatUtility.send("Scripts reloaded");
    }
}

