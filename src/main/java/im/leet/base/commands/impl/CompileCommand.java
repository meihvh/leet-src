/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.commands.impl;

import im.leet.Client;
import im.leet.api.render.system.sys2d.Shader;
import im.leet.base.commands.Command;
import im.leet.utils.client.ChatUtility;

public class CompileCommand
extends Command {
    public static CompileCommand INSTANCE = new CompileCommand();

    public CompileCommand() {
        super("compile", "recompile client shader");
    }

    @Override
    public void execute(String[] args) {
        Client.RENDERER.getCrenderSystem().setShader(new Shader());
        ChatUtility.send("compiled!");
    }
}

