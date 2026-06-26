/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.util.Identifier
 */
package im.leet.api.render.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public final class ResourceProvider {
    private static final Gson GSON = new Gson();

    public static Identifier getShaderIdentifier(String name) {
        return Identifier.method_60655((String)"leet", (String)("core/" + name));
    }

    public static JsonObject toJson(Identifier identifier) {
        return JsonParser.parseString((String)ResourceProvider.toString(identifier)).getAsJsonObject();
    }

    public static <T> T fromJsonToInstance(Identifier identifier, Class<T> clazz) {
        return (T)GSON.fromJson(ResourceProvider.toString(identifier), clazz);
    }

    public static String toString(Identifier identifier) {
        return ResourceProvider.toString(identifier, "\n");
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public static String toString(Identifier identifier, String delimiter) {
        try (InputStream inputStream = MinecraftClient.method_1551().method_1478().open(identifier);){
            String string;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));){
                string = reader.lines().collect(Collectors.joining(delimiter));
            }
            return string;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

