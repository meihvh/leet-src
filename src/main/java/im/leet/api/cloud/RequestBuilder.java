/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package im.leet.api.cloud;

import com.google.gson.JsonObject;

public class RequestBuilder {
    private final JsonObject obj = new JsonObject();

    public RequestBuilder param(String key, String value) {
        this.obj.addProperty(key, value);
        return this;
    }

    public String build() {
        return this.obj.toString();
    }
}

