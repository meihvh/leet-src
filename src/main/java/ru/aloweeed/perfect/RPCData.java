/*
 * Decompiled with CFR 0.152.
 */
package ru.aloweeed.perfect;

public class RPCData {
    private final String name;
    private final String avatar;
    private final long id;

    public RPCData(String name, long id, String avatar) {
        this.name = name;
        this.avatar = avatar;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public long getId() {
        return this.id;
    }
}

