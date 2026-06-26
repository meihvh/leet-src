/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.mappings.ClassEntry
 *  net.fabricmc.mappings.EntryTriple
 *  net.fabricmc.mappings.FieldEntry
 *  net.fabricmc.mappings.Mappings
 *  net.fabricmc.mappings.MethodEntry
 *  net.fabricmc.mappings.model.V2MappingsProvider
 */
package im.leet.utils.mapper;

import im.leet.utils.LogUtility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import net.fabricmc.mappings.ClassEntry;
import net.fabricmc.mappings.EntryTriple;
import net.fabricmc.mappings.FieldEntry;
import net.fabricmc.mappings.Mappings;
import net.fabricmc.mappings.MethodEntry;
import net.fabricmc.mappings.model.V2MappingsProvider;

public class FabricMapper {
    private static final Mappings mappings = FabricMapper.loadMappings();

    /*
     * Enabled aggressive exception aggregation
     */
    private static Mappings loadMappings() {
        try {
            Class.forName("net.minecraft.client.MinecraftClient");
            LogUtility.debug("mappings is on debug mode");
            return null;
        }
        catch (Exception exception) {
            try (InputStream is = FabricMapper.class.getResourceAsStream("/assets/leet/mappings.tiny");){
                Mappings mappings;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is));){
                    mappings = V2MappingsProvider.readTinyMappings((BufferedReader)reader);
                }
                return mappings;
            }
            catch (IOException | NullPointerException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String remapFieldName(String owner, String intermediaryName) {
        if (mappings == null) {
            return intermediaryName;
        }
        owner = FabricMapper.toSlash(owner);
        List fields = mappings.getFieldEntries().stream().toList();
        for (FieldEntry field : fields) {
            EntryTriple named;
            EntryTriple intermediary = field.get("named");
            if (intermediary == null || !intermediary.getOwner().equals(owner) || !intermediary.getName().equals(intermediaryName) || (named = field.get("intermediary")) == null) continue;
            return FabricMapper.toDot(named.getName());
        }
        return intermediaryName;
    }

    public static String remapFieldNameI2N(String owner, String intermediaryName) {
        if (mappings == null) {
            return intermediaryName;
        }
        owner = FabricMapper.toSlash(owner);
        List fields = mappings.getFieldEntries().stream().toList();
        for (FieldEntry field : fields) {
            EntryTriple named;
            EntryTriple intermediary = field.get("named");
            if (intermediary == null || !intermediary.getOwner().equals(owner) || !intermediary.getName().equals(intermediaryName) || (named = field.get("intermediary")) == null) continue;
            return FabricMapper.toDot(named.getName());
        }
        return intermediaryName;
    }

    public static String remapClassName(String owner) {
        if (mappings == null) {
            return owner;
        }
        owner = FabricMapper.toSlash(owner);
        List fields = mappings.getClassEntries().stream().toList();
        for (ClassEntry field : fields) {
            String named;
            String intermediary = field.get("named");
            if (intermediary == null || !intermediary.equals(owner) || (named = field.get("intermediary")) == null) continue;
            return FabricMapper.toDot(named);
        }
        return owner;
    }

    public static String remapClassNameI2N(String owner) {
        if (mappings == null) {
            return owner;
        }
        owner = FabricMapper.toSlash(owner);
        List fields = mappings.getClassEntries().stream().toList();
        for (ClassEntry field : fields) {
            String named;
            String intermediary = field.get("intermediary");
            if (intermediary == null || !intermediary.equals(owner) || (named = field.get("named")) == null) continue;
            return FabricMapper.toDot(named);
        }
        return owner;
    }

    public static String remapMethodName(String owner, String intermediaryName) {
        if (mappings == null) {
            return intermediaryName;
        }
        owner = FabricMapper.toSlash(owner);
        List fields = mappings.getMethodEntries().stream().toList();
        for (MethodEntry field : fields) {
            EntryTriple named;
            EntryTriple intermediary = field.get("named");
            if (intermediary == null || !intermediary.getOwner().equals(owner) || !intermediary.getName().equals(intermediaryName) || (named = field.get("intermediary")) == null) continue;
            return FabricMapper.toDot(named.getName());
        }
        return intermediaryName;
    }

    public static String remapMethodNameI2N(String owner, String intermediaryName) {
        if (mappings == null) {
            return intermediaryName;
        }
        owner = FabricMapper.toSlash(owner);
        List fields = mappings.getMethodEntries().stream().toList();
        for (MethodEntry field : fields) {
            EntryTriple named;
            EntryTriple intermediary = field.get("intermediary");
            if (intermediary == null || !intermediary.getOwner().equals(owner) || !intermediary.getName().equals(intermediaryName) || (named = field.get("named")) == null) continue;
            return FabricMapper.toDot(named.getName());
        }
        return intermediaryName;
    }

    public static String toDot(String s) {
        return s.replace("/", ".");
    }

    public static String toSlash(String s) {
        return s.replace(".", "/");
    }
}

