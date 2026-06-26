/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
 */
package im.leet.base.modules;

import im.leet.Client;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventKey;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.impl.combat.AimBot;
import im.leet.base.modules.impl.combat.AuraModule;
import im.leet.base.modules.impl.combat.AutoSoup;
import im.leet.base.modules.impl.combat.AutoTotem;
import im.leet.base.modules.impl.combat.Backtrack;
import im.leet.base.modules.impl.combat.Criticals;
import im.leet.base.modules.impl.combat.CrystalAura;
import im.leet.base.modules.impl.combat.ElytraAura;
import im.leet.base.modules.impl.combat.Hitbox;
import im.leet.base.modules.impl.combat.HugTarget;
import im.leet.base.modules.impl.combat.MaceTarget;
import im.leet.base.modules.impl.combat.Resolver;
import im.leet.base.modules.impl.combat.ShieldUtility;
import im.leet.base.modules.impl.combat.SuperBow;
import im.leet.base.modules.impl.combat.TargetStrafe;
import im.leet.base.modules.impl.combat.TriggerBot;
import im.leet.base.modules.impl.combat.VelocityModule;
import im.leet.base.modules.impl.combat.gunaura.GunAura;
import im.leet.base.modules.impl.movement.AntiBounce;
import im.leet.base.modules.impl.movement.AntiPush;
import im.leet.base.modules.impl.movement.ElytraRecast;
import im.leet.base.modules.impl.movement.Flight;
import im.leet.base.modules.impl.movement.HighJump;
import im.leet.base.modules.impl.movement.Jesus;
import im.leet.base.modules.impl.movement.LevitationControl;
import im.leet.base.modules.impl.movement.LongJump;
import im.leet.base.modules.impl.movement.NoFall;
import im.leet.base.modules.impl.movement.NoSlowDown;
import im.leet.base.modules.impl.movement.SlimeJump;
import im.leet.base.modules.impl.movement.Speed;
import im.leet.base.modules.impl.movement.Strafe;
import im.leet.base.modules.impl.movement.SuperFirework;
import im.leet.base.modules.impl.movement.WaterSpeed;
import im.leet.base.modules.impl.movement.elytrafly.ElytraFly;
import im.leet.base.modules.impl.movement.spider.Spider;
import im.leet.base.modules.impl.other.Obosralipsis;
import im.leet.base.modules.impl.player.AirStuck;
import im.leet.base.modules.impl.player.AssistModule;
import im.leet.base.modules.impl.player.AutoArmor;
import im.leet.base.modules.impl.player.AutoEZ;
import im.leet.base.modules.impl.player.BedwarsShop;
import im.leet.base.modules.impl.player.Blink;
import im.leet.base.modules.impl.player.Breaker;
import im.leet.base.modules.impl.player.CancelAction;
import im.leet.base.modules.impl.player.ChorusExploit;
import im.leet.base.modules.impl.player.ClientSounds;
import im.leet.base.modules.impl.player.DeathCoords;
import im.leet.base.modules.impl.player.Disabler;
import im.leet.base.modules.impl.player.ElytraHelper;
import im.leet.base.modules.impl.player.EventTracker;
import im.leet.base.modules.impl.player.FakeLag;
import im.leet.base.modules.impl.player.FakePing;
import im.leet.base.modules.impl.player.FakeRot;
import im.leet.base.modules.impl.player.GuiWalk;
import im.leet.base.modules.impl.player.InstantRespawn;
import im.leet.base.modules.impl.player.MinecraftBetter;
import im.leet.base.modules.impl.player.PacketLogger;
import im.leet.base.modules.impl.player.Phase;
import im.leet.base.modules.impl.player.Scaffold;
import im.leet.base.modules.impl.player.Spamer;
import im.leet.base.modules.impl.player.Sprint;
import im.leet.base.modules.impl.player.Timer;
import im.leet.base.modules.impl.player.XCarry;
import im.leet.base.modules.impl.render.Animations;
import im.leet.base.modules.impl.render.AspectRatio;
import im.leet.base.modules.impl.render.BedESP;
import im.leet.base.modules.impl.render.Chams;
import im.leet.base.modules.impl.render.Cosmetics;
import im.leet.base.modules.impl.render.CustomWorld;
import im.leet.base.modules.impl.render.ESP;
import im.leet.base.modules.impl.render.FullBright;
import im.leet.base.modules.impl.render.Interface;
import im.leet.base.modules.impl.render.JumpCircle;
import im.leet.base.modules.impl.render.Kagune;
import im.leet.base.modules.impl.render.Particles;
import im.leet.base.modules.impl.render.Removals;
import im.leet.base.modules.impl.render.Tracers;
import im.leet.base.modules.impl.render.Zoom;
import im.leet.base.settings.Setting;
import im.leet.base.settings.impl.group.Group;
import im.leet.utils.LogUtility;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Modules {
    private final Map<Class<? extends Module>, Module> byClass = new Object2ObjectLinkedOpenHashMap();
    private final List<Module> modules = new ArrayList<Module>();
    EventBus<EventKey> onKey = event -> {
        if (Module.mc.field_1755 != null || Client.CLICKGUI.WRITING) {
            return;
        }
        this.getModules().forEach(module -> {
            if (module.getKey() != -1 && module.getKey() == event.getKey() && (event.getAction() == 1 && module.getBindType() == Module.BindType.PRESS || event.getAction() <= 1 && module.getBindType() == Module.BindType.HOLD)) {
                module.toggle();
            }
        });
    };

    public Modules() {
        this.add(TriggerBot.INSTANCE, Criticals.INSTANCE, AuraModule.INSTANCE, ElytraAura.INSTANCE, VelocityModule.INSTANCE, AutoTotem.INSTANCE, AutoSoup.INSTANCE, CrystalAura.INSTANCE, MaceTarget.INSTANCE, SuperBow.INSTANCE, Resolver.INSTANCE, Hitbox.INSTANCE, ShieldUtility.INSTANCE, GunAura.INSTANCE, Backtrack.INSTANCE, AimBot.INSTANCE, HugTarget.INSTANCE, TargetStrafe.INSTANCE, Sprint.INSTANCE, AutoArmor.INSTANCE, CancelAction.INSTANCE, AirStuck.INSTANCE, ElytraHelper.INSTANCE, Phase.INSTANCE, ClientSounds.INSTANCE, DeathCoords.INSTANCE, MinecraftBetter.INSTANCE, EventTracker.INSTANCE, ChorusExploit.INSTANCE, AssistModule.INSTANCE, FakeLag.INSTANCE, GuiWalk.INSTANCE, Timer.INSTANCE, InstantRespawn.INSTANCE, PacketLogger.INSTANCE, FakePing.INSTANCE, XCarry.INSTANCE, Breaker.INSTANCE, Scaffold.INSTANCE, BedwarsShop.INSTANCE, Blink.INSTANCE, AutoEZ.INSTANCE, Spamer.INSTANCE, Interface.INSTANCE, AspectRatio.INSTANCE, Removals.INSTANCE, CustomWorld.INSTANCE, FullBright.INSTANCE, Animations.INSTANCE, JumpCircle.INSTANCE, Particles.INSTANCE, Chams.INSTANCE, ESP.INSTANCE, Tracers.INSTANCE, Obosralipsis.INSTANCE, Zoom.INSTANCE, BedESP.INSTANCE, Cosmetics.INSTANCE, Kagune.INSTANCE, Speed.INSTANCE, Jesus.INSTANCE, NoSlowDown.INSTANCE, ElytraFly.INSTANCE, SuperFirework.INSTANCE, AntiPush.INSTANCE, WaterSpeed.INSTANCE, HighJump.INSTANCE, LevitationControl.INSTANCE, Flight.INSTANCE, Disabler.INSTANCE, ElytraRecast.INSTANCE, LongJump.INSTANCE, NoFall.INSTANCE, AntiBounce.INSTANCE, Strafe.INSTANCE, SlimeJump.INSTANCE, Spider.INSTANCE);
        if (Client.IS_DEBUG) {
            this.add(FakeRot.INSTANCE);
        }
        Client.EVENTS.register(this);
        this.printLang();
    }

    private void printLang() {
        try (FileOutputStream fos = new FileOutputStream("lang.conf");){
            PrintStream ps = new PrintStream(fos);
            for (Module m : this.modules) {
                this.printLangSetting(m, ps);
            }
            ps.flush();
        }
        catch (Exception e) {
            LogUtility.error(e, "printLang");
        }
    }

    private void printLangSetting(Setting<?> setting, PrintStream ps) {
        Group g;
        Object name;
        Object object = name = setting.name.contains("(") ? "\"" + setting.name + "\"" : setting.name;
        if (setting instanceof Group && !(g = (Group)setting).isEmpty()) {
            ps.println((String)name + "{");
            ps.println("_description = \"" + g._desc + "\"");
            for (Setting<?> child : g.getSettings()) {
                if (g.enabled != null && child == g.enabled) continue;
                this.printLangSetting(child, ps);
            }
            ps.println("}");
        } else {
            ps.println((String)name + "._description = \"" + setting._desc + "\"");
        }
    }

    public void add(Module ... modules) {
        for (Module m : modules) {
            this.addModule(m);
        }
        this.sync();
    }

    private void addModule(Module module) {
        this.byClass.put(module.getClass(), module);
        this.modules.add(module);
    }

    public void unregister(Module ... modules) {
        for (Module m : modules) {
            this.unregisterModule(m);
        }
        this.sync();
    }

    private void unregisterModule(Module module) {
        this.byClass.remove(module.getClass());
        this.modules.remove(module);
    }

    private void sync() {
        this.modules.sort(Comparator.comparing(Setting::getName));
    }

    public <T extends Module> T get(String name) {
        for (Module m : this.modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return (T)m;
        }
        return null;
    }

    public <T extends Module> T get(Class<T> clazz) {
        return (T)this.byClass.get(clazz);
    }

    public List<Module> get(Category category) {
        return this.modules.stream().filter(module -> module.getCategory() == category).collect(Collectors.toList());
    }

    public List<Module> getModules() {
        return this.modules;
    }
}

