/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 */
package im.leet.api.friends;

import im.leet.Client;
import im.leet.utils.LogUtility;
import im.leet.utils.secure.CryptUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.player.PlayerEntity;

public class Friends {
    private ArrayList<String> friendList = new ArrayList();
    private final File file = Client.CLIENT_DIR.resolve("friends.leet").toFile();

    public Friends() {
        if (!this.file.exists()) {
            try {
                new FileOutputStream(this.file).write("".getBytes(StandardCharsets.UTF_8));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addFriend(PlayerEntity player) {
        if (!this.friendList.contains(player.method_7334().getName())) {
            this.friendList.add(player.method_7334().getName());
        }
    }

    public void addFriend(String playerName) {
        if (!this.friendList.contains(playerName)) {
            this.friendList.add(playerName);
        }
    }

    public void removeFriend(PlayerEntity player) {
        this.friendList.remove(player.method_7334().getName());
    }

    public void removeFriend(String playerName) {
        this.friendList.remove(playerName);
    }

    public boolean isFriend(PlayerEntity player) {
        return this.friendList.contains(player.method_7334().getName());
    }

    public boolean isFriend(String playerName) {
        return this.friendList.contains(playerName);
    }

    public void save() {
        try {
            if (!this.file.exists()) {
                try {
                    this.file.createNewFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream out = new FileOutputStream(this.file);
            StringBuilder builder = new StringBuilder();
            for (String friend : this.friendList) {
                builder.append(friend).append("\n");
            }
            out.write(CryptUtility.proccessXOR(builder.toString().getBytes(StandardCharsets.UTF_8)));
            LogUtility.debug(String.format("saved friends", new Object[0]));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            FileInputStream in = new FileInputStream(this.file);
            String s = new String(CryptUtility.proccessXOR(in.readAllBytes()), StandardCharsets.UTF_8);
            Object[] friends = s.split("\n");
            this.friendList.clear();
            if (!s.isBlank()) {
                this.friendList.addAll(Arrays.asList(friends));
            }
            LogUtility.debug(String.format("loaded friends: %s", Arrays.toString(friends)));
        }
        catch (Exception e) {
            LogUtility.error(e, "loading friends");
        }
    }

    public ArrayList<String> getFriendList() {
        return this.friendList;
    }
}

