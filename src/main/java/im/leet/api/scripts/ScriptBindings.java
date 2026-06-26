/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.graalvm.polyglot.Context
 *  org.graalvm.polyglot.Value
 *  org.joml.Vector2f
 *  org.joml.Vector3f
 */
package im.leet.api.scripts;

import im.leet.api.events.list.Event2D;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventAttack;
import im.leet.api.events.list.EventBoost;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventMove;
import im.leet.api.scripts.Script;
import im.leet.api.scripts.api.bindings.ClientProvider;
import im.leet.api.scripts.api.bindings.DragProvider;
import im.leet.api.scripts.api.bindings.FontProvider;
import im.leet.api.scripts.api.bindings.GameProvider;
import im.leet.api.scripts.api.bindings.HookProvider;
import im.leet.api.scripts.api.bindings.ReflectProvider;
import im.leet.api.scripts.api.bindings.RenderProvider;
import im.leet.api.scripts.api.bindings.TimeProvider;
import im.leet.api.scripts.api.bindings.event.EventProvider;
import im.leet.api.scripts.api.bindings.support.AngleScripted;
import im.leet.api.scripts.api.integrate.ScriptHook;
import im.leet.base.modules.Category;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import java.util.HashMap;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class ScriptBindings {
    public static void bind(Context ctx, Script script) {
        Value bindings = ctx.getBindings("js");
        bindings.putMember("register", script::register);
        bindings.putMember("Client", (Object)new ClientProvider(script));
        bindings.putMember("Events", (Object)new EventProvider(script));
        bindings.putMember("Reflect", (Object)new ReflectProvider());
        bindings.putMember("Render", (Object)new RenderProvider());
        bindings.putMember("Timer", (Object)new TimeProvider());
        bindings.putMember("Hook", (Object)new HookProvider(script));
        bindings.putMember("Game", (Object)new GameProvider());
        bindings.putMember("Drag", (Object)new DragProvider());
        HashMap<String, String> fonts = new HashMap<String, String>();
        for (FontProvider provider : FontProvider.values()) {
            fonts.put(provider.name(), provider.name());
        }
        bindings.putMember("Font", fonts);
        HashMap<String, String> categories = new HashMap<String, String>();
        for (Category category : Category.values()) {
            categories.put(category.name(), category.name());
        }
        bindings.putMember("Category", categories);
        HashMap<String, String> hooks = new HashMap<String, String>();
        for (Enum enum_ : ScriptHook.HookValue.values()) {
            hooks.put(enum_.name(), enum_.name());
        }
        bindings.putMember("Hooks", hooks);
        bindings.putMember("Render2D", Event2D.class);
        bindings.putMember("GameTick", EventGameTick.class);
        bindings.putMember("ServerMove", EventMove.class);
        bindings.putMember("FireworkBoost", EventBoost.class);
        bindings.putMember("Attack", EventAttack.class);
        bindings.putMember("Render3D", Event3D.class);
        bindings.putMember("Slider", SliderSetting.class);
        bindings.putMember("CheckBox", CheckBox.class);
        bindings.putMember("Enums", EnumSetting.class);
        bindings.putMember("Vec3", Vector3f.class);
        bindings.putMember("Vec2f", Vector2f.class);
        bindings.putMember("Angle", AngleScripted.class);
    }
}

