/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.text.MutableText
 *  net.minecraft.text.Text
 */
package im.leet.base.modules;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.leet.Client;
import im.leet.api.render.system.IconUse;
import im.leet.base.modules.Category;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.player.ClientSounds;
import im.leet.base.settings.impl.group.Group;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.DebugUtility;
import im.leet.utils.client.SoundUtility;
import im.leet.utils.client.TextUtility;
import im.leet.utils.lang.LangUtility;
import im.leet.utils.stupidity.MySoundEvents;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public abstract class Module
extends Group {
    protected static final MinecraftClient mc = MinecraftClient.method_1551();
    public String description;
    private Category category;
    private boolean enabled;
    private int key = -1;
    private final Set<Tag> tags;
    private boolean activatable = true;
    private BindType bindType = BindType.PRESS;
    public SmoothStepAnimation keybindAnimation = new SmoothStepAnimation(200, 1.0);

    @Override
    public String getDesc() {
        return LangUtility.get(this.getLangKey() + "._description", this.description);
    }

    @Override
    public boolean isEnabled() {
        return this.enabled && !Client.IS_PANIC;
    }

    @Override
    public String getLangKey() {
        return "leet.module." + this.name;
    }

    public static boolean nullCheck() {
        return Module.mc.field_1724 == null || Module.mc.field_1687 == null || mc.method_1562() == null;
    }

    public Module(String name, Category category, String description, Tag ... tags) {
        super(name.trim());
        this.category = category;
        this.description = description;
        this._desc = description;
        this.tags = new HashSet<Tag>(Set.of(tags));
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove((Object)tag);
    }

    protected void nonActivatable() {
        this.activatable = false;
        this.superEnable();
    }

    public String getKeyText() {
        return TextUtility.keyToString(this.getKey());
    }

    public void toggle() {
        this.setEnabled(!this.enabled);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.setEnabled(enabled, this.getBindType() == BindType.PRESS);
        }
    }

    public void setEnabled(boolean enabled, boolean notification) {
        if (!this.activatable || this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            this.superEnable();
        } else {
            this.superDisable();
        }
        if (notification) {
            if (ClientSounds.INSTANCE.isEnabled() && ClientSounds.INSTANCE.toggleFunction.get()) {
                SoundUtility.playSound(enabled ? MySoundEvents.moduleOn : MySoundEvents.moduleOff, ClientSounds.INSTANCE.volume.get());
            }
            MutableText module = Text.method_30163((String)this.name).method_27661().method_54663(ClientColors.MAIN_COLOR.getRGB());
            MutableText enableOrdisable = Text.method_30163((String)(enabled ? " \u0432\u043a\u043b\u044e\u0447\u0435\u043d!" : " \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d!")).method_27661().method_54663(enabled ? new Color(0, 200, 0, 255).getRGB() : new Color(200, 0, 0, 255).getRGB());
            MutableText mainText = Text.method_30163((String)"\u041c\u043e\u0434\u0443\u043b\u044c ").method_27661().method_10852((Text)module).method_27661().method_10852((Text)enableOrdisable);
            Client.NOTIFIES.add((Text)mainText, IconUse.INFO, 2000L);
        }
    }

    private void superEnable() {
        if (Module.mc.field_1724 != null) {
            this.onEnable();
        }
        Client.EVENTS.register(this);
    }

    private void superDisable() {
        if (Module.mc.field_1724 != null) {
            this.onDisable();
        }
        Client.EVENTS.unregister(this);
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    @Override
    public void save(JsonObject json) {
        JsonObject me = new JsonObject();
        me.addProperty("state", Boolean.valueOf(this.enabled));
        me.addProperty("bind", (Number)this.key);
        me.addProperty("bindType", (Number)this.bindType.ordinal());
        JsonObject s = new JsonObject();
        me.add("settings", (JsonElement)s);
        super.save(s);
        json.add(this.name, (JsonElement)me);
    }

    @Override
    public void load(JsonObject json) {
        if (json.has(this.name)) {
            JsonObject me = json.get(this.name).getAsJsonObject();
            this.setEnabled(me.get("state").getAsBoolean(), false);
            this.key = me.get("bind").getAsInt();
            JsonObject s = me.get("settings").getAsJsonObject();
            if (json.has("bindType")) {
                this.bindType = BindType.values()[me.get("bindType").getAsInt()];
            }
            super.load(s);
        }
    }

    protected void debug(String section, Object o) {
        DebugUtility.trace(this.name + " " + section, o);
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public boolean isActivatable() {
        return this.activatable;
    }

    public BindType getBindType() {
        return this.bindType;
    }

    public void setBindType(BindType bindType) {
        this.bindType = bindType;
    }

    public static enum BindType {
        PRESS,
        HOLD;

    }
}

