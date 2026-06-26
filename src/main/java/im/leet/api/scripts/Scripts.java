/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 */
package im.leet.api.scripts;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event3D;
import im.leet.api.scripts.Script;
import im.leet.api.scripts.api.integrate.ScriptHook;
import im.leet.utils.client.ChatUtility;
import java.io.File;
import java.util.ArrayList;
import net.minecraft.client.MinecraftClient;

public class Scripts {
    private final File dir = Client.CLIENT_DIR.resolve("scripts").toFile();
    private final ArrayList<Script> scripts = new ArrayList();
    private final ScriptHook scriptHook = new ScriptHook();
    EventBus<Event> events = event -> {
        for (Script script : this.scripts) {
            if (script.getEventProvider() == null || !script.getScriptedModule().isEnabled()) continue;
            MinecraftClient.method_1551().execute(() -> {
                try {
                    if (event instanceof Event3D) {
                        Event3D e = (Event3D)event;
                        e.stack.method_22903();
                        Client.RENDERER.toCamera(e.stack);
                        script.getEventProvider().call(event);
                        e.stack.method_22909();
                    } else {
                        script.getEventProvider().call(event);
                    }
                }
                catch (Exception e) {
                    ChatUtility.send(String.format("Script error %s: %s", script.getName(), e.getMessage()));
                }
            });
        }
    };

    public Scripts() {
        this.dir.mkdirs();
        Client.EVENTS.register(this);
        this.updateFolder();
    }

    public void updateFolder() {
        this.scripts.clear();
        if (this.dir.exists() && this.dir.isDirectory() && this.dir.listFiles() != null) {
            for (File f : this.dir.listFiles()) {
                if (!f.getName().endsWith(".js")) continue;
                this.scripts.add(new Script(f.getName(), f));
            }
        }
    }

    public String[] list() {
        if (this.dir.exists() && this.dir.isDirectory()) {
            File[] files = this.dir.listFiles();
            ArrayList<String> fileNames = new ArrayList<String>();
            if (files != null) {
                for (File file : files) {
                    if (!file.isFile() || !file.getName().endsWith(".js")) continue;
                    String fileName = file.getName().replace(".js", "");
                    fileNames.add(fileName);
                }
            }
            return fileNames.toArray(new String[0]);
        }
        return null;
    }

    public void RELOAD() {
        for (Script script : Client.SCRIPTS.getScripts()) {
            script.init();
        }
        Client.CLICKGUI.RELOAD();
    }

    public ArrayList<Script> getScripts() {
        return this.scripts;
    }

    public ScriptHook getScriptHook() {
        return this.scriptHook;
    }
}

