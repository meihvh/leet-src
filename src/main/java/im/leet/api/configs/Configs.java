/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonObject
 */
package im.leet.api.configs;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.base.modules.Module;
import im.leet.utils.LogUtility;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.secure.CryptUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Configs
implements MinecraftHolder {
    private File dir = Client.CLIENT_DIR.resolve("configs").toFile();
    private ArrayList<String> configs = new ArrayList();
    public static boolean IS_LOADING = false;

    public Configs() {
        this.dir.mkdirs();
        this.updateFolder();
    }

    public void updateFolder() {
        this.configs.clear();
        if (this.dir.exists() && this.dir.isDirectory() && this.dir.listFiles() != null) {
            for (File f : this.dir.listFiles()) {
                if (!f.getName().endsWith(".leet")) continue;
                this.configs.add(f.getName());
            }
        }
    }

    public void onChange() {
    }

    public void save(String name) {
        JsonObject json = new JsonObject();
        ClientSettings.INSTANCE.save(json);
        for (Module module : Client.MODULES.getModules()) {
            module.save(json);
        }
        Client.HUD.save(json);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(this.dir, name + ".leet"));
            fileOutputStream.write(CryptUtility.proccessXOR(json.toString().getBytes()));
            fileOutputStream.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void load(String name) {
        IS_LOADING = true;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(this.dir, name + ".leet"));
            byte[] bytes = CryptUtility.proccessXOR(fileInputStream.readAllBytes());
            fileInputStream.close();
            JsonObject gson = (JsonObject)new Gson().fromJson(new String(bytes), JsonObject.class);
            try {
                ClientSettings.INSTANCE.load(gson);
            }
            catch (Exception e) {
                LogUtility.error(e, "loading clientsettings config");
            }
            for (Module module : Client.MODULES.getModules()) {
                try {
                    module.load(gson);
                }
                catch (Exception e) {
                    LogUtility.error(e, "loading module " + module.getName() + " config");
                }
            }
            try {
                Client.HUD.load(gson);
            }
            catch (Exception e) {
                LogUtility.error(e, "loading hud config");
            }
        }
        catch (Exception e) {
            LogUtility.error(e, "loading config");
        }
        IS_LOADING = false;
    }

    public String[] list() {
        if (this.dir.exists() && this.dir.isDirectory()) {
            File[] files = this.dir.listFiles();
            ArrayList<String> fileNames = new ArrayList<String>();
            if (files != null) {
                for (File file : files) {
                    if (!file.isFile() || !file.getName().endsWith(".leet")) continue;
                    String fileName = file.getName().replace(".leet", "");
                    fileNames.add(fileName);
                }
            }
            return fileNames.toArray(new String[0]);
        }
        return null;
    }
}

