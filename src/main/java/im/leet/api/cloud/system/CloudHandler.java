/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonObject
 */
package im.leet.api.cloud.system;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import im.leet.Client;
import im.leet.base.modules.Module;
import im.leet.utils.LogUtility;

public class CloudHandler {
    public void handle(JsonObject json) {
        String action;
        switch (action = json.get("action").getAsString()) {
            case "config": {
                this.handleConfig(json);
            }
        }
    }

    private void handleConfig(JsonObject json) {
        String subaction;
        System.out.println(json);
        switch (subaction = json.get("subaction").getAsString()) {
            case "load": {
                String ctx = json.get("ctx").getAsString();
                JsonObject obj = (JsonObject)new Gson().fromJson(ctx, JsonObject.class);
                for (Module module : Client.MODULES.getModules()) {
                    module.load(obj);
                }
                LogUtility.debug(String.format("[cloud] loading config: %s", ctx));
                break;
            }
            case "save": {
                int code = json.get("code").getAsInt();
                LogUtility.debug(String.format("[cloud] saving config: %s", code));
            }
        }
    }
}

