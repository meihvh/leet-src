/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package im.leet.api.cloud;

import com.google.gson.JsonObject;
import im.leet.Client;
import im.leet.api.cloud.RequestBuilder;
import im.leet.api.cloud.system.CloudClient;
import im.leet.base.modules.Module;
import im.leet.utils.LogUtility;
import im.leet.utils.client.ClientSettings;
import java.net.URI;

public class Cloud {
    private CloudClient client = new CloudClient(URI.create("ws://localhost:3000/cloud"), this);
    private boolean isConnected = false;

    public Cloud() {
        this.client.connect();
        new Thread(() -> {
            while (ClientSettings.INSTANCE.cloud.get()) {
                try {
                    Thread.sleep(5000L);
                    if (this.isConnected) continue;
                    LogUtility.debug("[cloud] cloud is not connected, reconnecting...");
                    this.client.reconnect();
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void send(String text) {
        if (this.isConnected) {
            this.client.send(text);
        } else {
            LogUtility.debug("[cloud] cannot send data due not connected to cloud");
        }
    }

    public void loadConfig(String name) {
        this.send(new RequestBuilder().param("action", "config").param("subaction", "load").param("name", name).build());
    }

    public void saveConfig(String name) {
        JsonObject json = new JsonObject();
        for (Module module : Client.MODULES.getModules()) {
            module.save(json);
        }
        this.send(new RequestBuilder().param("action", "config").param("subaction", "save").param("name", name).param("ctx", json.toString()).build());
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
}

