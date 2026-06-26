/*
 * Decompiled with CFR 0.152.
 */
package ru.aloweeed.perfect;

public class UserData {
    private final String name;
    private final int uid;
    private final int role;

    public String getName() {
        return this.name;
    }

    public int getUid() {
        return this.uid;
    }

    public UserData(String name, int uid, int role) {
        this.name = name;
        this.uid = uid;
        this.role = role;
    }
}

