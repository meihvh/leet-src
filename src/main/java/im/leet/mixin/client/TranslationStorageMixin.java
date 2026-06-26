/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  com.typesafe.config.Config
 *  com.typesafe.config.ConfigFactory
 *  com.typesafe.config.ConfigObject
 *  com.typesafe.config.ConfigValue
 *  com.typesafe.config.ConfigValueType
 *  net.minecraft.client.resource.language.TranslationStorage
 *  net.minecraft.resource.Resource
 *  net.minecraft.resource.ResourceManager
 *  net.minecraft.util.Identifier
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;
import im.leet.utils.LogUtility;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={TranslationStorage.class})
public class TranslationStorageMixin {
    @Inject(method={"load(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;Z)Lnet/minecraft/client/resource/language/TranslationStorage;"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/resource/language/TranslationStorage;load(Ljava/lang/String;Ljava/util/List;Ljava/util/Map;)V")})
    private static void leet$load(ResourceManager manager, List<Identifier> definitions, boolean rtl, CallbackInfoReturnable<?> cir, @Local Map<String, String> translations, @Local(ordinal=0) String currentLangCode, @Local(ordinal=2) String mod) {
        for (Resource resource : manager.method_14489(Identifier.method_60655((String)mod, (String)("lang/" + currentLangCode + ".conf")))) {
            try {
                InputStream is = resource.method_14482();
                try {
                    Config config = ConfigFactory.parseReader((Reader)new InputStreamReader(is));
                    ConfigObject root = config.root();
                    TranslationStorageMixin.leet$unwrap("", root, translations);
                }
                finally {
                    if (is == null) continue;
                    is.close();
                }
            }
            catch (IOException e) {
                LogUtility.LOGGER.error("couldn't read strings from {}/lang/{}.conf :(", (Object)mod, (Object)currentLangCode);
            }
        }
    }

    @Unique
    private static void leet$unwrap(String prefix, ConfigObject object, Map<String, String> translations) {
        for (Map.Entry entry : object.entrySet()) {
            String newPrefix;
            String key = (String)entry.getKey();
            ConfigValue value = (ConfigValue)entry.getValue();
            String string = newPrefix = prefix.isEmpty() ? key : prefix + "." + key;
            if (value.valueType() == ConfigValueType.OBJECT) {
                TranslationStorageMixin.leet$unwrap(newPrefix, (ConfigObject)value, translations);
                continue;
            }
            if (value.valueType() != ConfigValueType.STRING) continue;
            translations.put(newPrefix, (String)value.unwrapped());
        }
    }
}

