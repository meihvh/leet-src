/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonObject
 *  org.java_websocket.client.WebSocketClient
 *  org.java_websocket.handshake.ServerHandshake
 */
package im.leet.api.cloud.system;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import im.leet.api.cloud.Cloud;
import im.leet.api.cloud.system.CloudHandler;
import im.leet.utils.LogUtility;
import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class CloudClient
extends WebSocketClient {
    private final Cloud cloud;
    private final CloudHandler handler;

    public CloudClient(URI serverUri, Cloud cloud) {
        super(serverUri);
        this.cloud = cloud;
        this.handler = new CloudHandler();
    }

    public void onOpen(ServerHandshake serverHandshake) {
        this.cloud.setConnected(true);
        LogUtility.debug("[cloud] connected.");
    }

    public void onMessage(String s) {
        try {
            JsonObject object = (JsonObject)new Gson().fromJson(s, JsonObject.class);
            this.handler.handle(object);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClose(int i, String s, boolean b) {
        this.cloud.setConnected(false);
    }

    public void onError(Exception e) {
    }
}

