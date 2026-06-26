/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ModInitializer
 *  net.minecraft.SharedConstants
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.util.Icons
 *  net.minecraft.resource.ResourcePack
 *  net.minecraft.util.Util
 *  net.minecraft.util.Util$OperatingSystem
 */
package im.leet;

import im.leet.api.cloud.Cloud;
import im.leet.api.configs.Configs;
import im.leet.api.drags.Drags;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.Events;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.friends.Friends;
import im.leet.api.macros.Macros;
import im.leet.api.render.ClientRenderer;
import im.leet.api.render.msdf.Fonts;
import im.leet.api.scripts.Scripts;
import im.leet.api.sound.SoundMixFilter;
import im.leet.base.commands.Commands;
import im.leet.base.hud.notification.Notifies;
import im.leet.base.hud.ui.Hud;
import im.leet.base.modules.Module;
import im.leet.base.modules.Modules;
import im.leet.base.rotations.RotationHandler;
import im.leet.base.screens.ingame.ClickGui;
import im.leet.utils.Scheduler;
import im.leet.utils.block.ChunkScanner;
import im.leet.utils.client.ChatUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.stupidity.MySoundEvents;
import java.nio.file.Path;
import net.fabricmc.api.ModInitializer;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Icons;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Util;

public class Client
implements ModInitializer {
    public static ClientRenderer RENDERER;
    public static Fonts FONTS;
    public static Events EVENTS;
    public static Commands COMMANDS;
    public static Modules MODULES;
    public static Drags DRAGS;
    public static ClickGui CLICKGUI;
    public static RotationHandler ROTATION;
    public static Configs CONFIG;
    public static Hud HUD;
    public static Scripts SCRIPTS;
    public static Scheduler SCHEDULER;
    public static Notifies NOTIFIES;
    public static Friends FRIENDS;
    public static Cloud CLOUD;
    public static SoundMixFilter RTX_ENGINE;
    public static Macros MACROS;
    public static ChunkScanner ChunkScanner;
    public static float TIMER;
    public static boolean INITIALIZED;
    public static boolean IS_DEBUG;
    public static boolean IS_PANIC;
    public static final Path CLIENT_DIR;
    TimeUtility autoSave = new TimeUtility();
    EventBus<Event> tick = event -> {
        if (event instanceof EventGameTick && this.autoSave.reached(60000L, true)) {
            Client.saveAll();
        }
    };

    public void onInitialize() {
        MySoundEvents.init();
    }

    private static Path getClientDir() {
        return switch (Util.method_668()) {
            case Util.OperatingSystem.field_1133 -> Path.of("c:/LEET", new String[0]);
            default -> Path.of(System.getProperty("user.home"), ".leet");
        };
    }

    public void init() {
        CONFIG = new Configs();
        RENDERER = new ClientRenderer();
        FONTS = new Fonts();
        EVENTS = new Events();
        HUD = new Hud();
        FRIENDS = new Friends();
        COMMANDS = new Commands();
        MACROS = new Macros();
        MODULES = new Modules();
        DRAGS = new Drags();
        CLICKGUI = new ClickGui();
        ROTATION = new RotationHandler();
        SCRIPTS = new Scripts();
        SCHEDULER = new Scheduler();
        NOTIFIES = new Notifies();
        CLOUD = new Cloud();
        ChunkScanner = new ChunkScanner();
        FRIENDS.load();
        SCRIPTS.RELOAD();
        if (CLIENT_DIR.resolve("configs/default.leet").toFile().exists()) {
            CONFIG.load("default");
        }
        INITIALIZED = true;
        EVENTS.register(this);
        EVENTS.register(ChunkScanner);
    }

    public static void saveAll() {
        CONFIG.save("default");
        FRIENDS.save();
    }

    public static void startPanic() {
        Client.saveAll();
        IS_PANIC = true;
        CLICKGUI.setOpened(false);
        CLICKGUI.panicStarted();
        for (Module module : MODULES.getModules()) {
            module.setEnabled(false, false);
        }
        try {
            MinecraftClient.method_1551().method_22683().method_4491((ResourcePack)MinecraftClient.method_1551().method_45573(), SharedConstants.method_16673().comp_4031() ? Icons.field_44650 : Icons.field_44651);
        }
        catch (Exception exception) {
            // empty catch block
        }
        MinecraftClient.method_1551().method_24288();
    }

    public static void stopPanic() {
        IS_PANIC = false;
        CONFIG.load("default");
        ChatUtility.send("client attached!");
        try {
            MinecraftClient.method_1551().method_22683().method_4491((ResourcePack)MinecraftClient.method_1551().method_45573(), SharedConstants.method_16673().comp_4031() ? Icons.field_44650 : Icons.field_44651);
        }
        catch (Exception exception) {
            // empty catch block
        }
        MinecraftClient.method_1551().method_24288();
    }

    static {
        TIMER = 1.0f;
        INITIALIZED = false;
        IS_DEBUG = true;
        IS_PANIC = false;
        CLIENT_DIR = Client.getClientDir();
    }
}

