/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 */
package im.leet.base.commands;

import im.leet.MinecraftHolder;
import im.leet.utils.client.ChatUtility;
import java.awt.Color;
import net.minecraft.text.Text;

public abstract class Command
implements MinecraftHolder {
    private String name;
    private String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void execute(String[] var1);

    protected void error(String error) {
        ChatUtility.send((Text)Text.method_43471((String)error).method_54663(new Color(255, 100, 100).getRGB()));
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}

