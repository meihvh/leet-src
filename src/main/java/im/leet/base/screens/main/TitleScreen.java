/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
 *  net.minecraft.client.gui.screen.option.OptionsScreen
 *  net.minecraft.client.gui.screen.world.SelectWorldScreen
 *  net.minecraft.text.Text
 */
package im.leet.base.screens.main;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.base.screens.main.widgets.CustomButton;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;
import ru.aloweeed.perfect.Ignored;

@Ignored
public class TitleScreen
extends Screen {
    public static boolean ACCOUNT_SUPPORT = false;
    private ArrayList<CustomButton> buttons = new ArrayList();

    public TitleScreen() {
        super((Text)Text.method_43473());
        try {
            Class.forName("ru.vidtu.ias.screen.AccountScreen");
            ACCOUNT_SUPPORT = true;
        }
        catch (Exception e) {
            ACCOUNT_SUPPORT = false;
        }
    }

    public void method_25394(DrawContext context, int mouseX, int mouseY, float delta) {
        float dx = ((float)this.field_22789 / 2.0f - (float)mouseX) / 100.0f;
        float dy = ((float)this.field_22790 / 2.0f - (float)mouseY) / 100.0f;
        Color c = ColorUtility.injectAlpha(ClientColors.BACK_COLOR, 230.0f);
        for (CustomButton button : this.buttons) {
            button.render(mouseX, mouseY);
        }
        Client.RENDERER.textCentered("hach", (float)this.field_22789 / 2.0f, (float)this.field_22790 / 2.0f - 30.0f, TextureUse.SFMEDIUM, 40.0f, ColorUtility.injectAlpha(ClientColors.DARK_GRAY_COLOR, 23.0f));
        Client.RENDERER.textCentered("t.me/hachclientblog", (float)this.field_22789 / 2.0f, (float)this.field_22790 / 2.0f + 10.0f, TextureUse.SFMEDIUM, 25.0f, ColorUtility.injectAlpha(ClientColors.DARK_GRAY_COLOR, 23.0f));
        if (this.field_22789 >= 642) {
            Client.RENDERER.textCentered("a", 230.0f + dx, (float)this.field_22790 / 2.0f - 48.0f + dy, TextureUse.ICONS, 160.0f, ClientColors.MAIN_COLOR);
        }
    }

    protected void method_25426() {
        super.method_25426();
        int l = this.field_22790 / 2 - 14;
        float off = this.field_22789 < 642 ? (float)this.field_22789 / 2.0f - 75.0f : (float)this.field_22789 * 0.7f;
        float h = 25.0f;
        float margin = h + 2.0f;
        this.buttons.clear();
        this.buttons.addAll(Arrays.asList(CustomButton.CustomButtonBuilder.build(off, l, 150.0f, h, "l", Text.method_43471((String)"menu.singleplayer").getString(), Color.WHITE, Color.WHITE, CustomButton.CustomButtonBuilder.ButtonType.MAIN, () -> this.field_22787.method_1507((Screen)new SelectWorldScreen((Screen)this))), CustomButton.CustomButtonBuilder.build(off, (float)l + margin, 150.0f, h, "m", Text.method_43471((String)"menu.multiplayer").getString(), Color.WHITE, Color.WHITE, CustomButton.CustomButtonBuilder.ButtonType.MAIN, () -> this.field_22787.method_1507((Screen)new MultiplayerScreen((Screen)this))), CustomButton.CustomButtonBuilder.build(off, (float)l + margin * 2.0f, 150.0f, h, "n", "Alt Manager", Color.WHITE, Color.WHITE, CustomButton.CustomButtonBuilder.ButtonType.ALT, () -> {}), CustomButton.CustomButtonBuilder.build(off, (float)l + margin * 3.0f, 150.0f, h, "o", Text.method_43471((String)"menu.options").getString(), Color.WHITE, Color.WHITE, CustomButton.CustomButtonBuilder.ButtonType.MAIN, () -> this.field_22787.method_1507((Screen)new OptionsScreen((Screen)this, this.field_22787.field_1690))), CustomButton.CustomButtonBuilder.build(off + 10.0f, (float)l + margin * 4.0f, 130.0f, h, "p", Text.method_43471((String)"menu.quit").getString(), Color.WHITE, Color.WHITE, CustomButton.CustomButtonBuilder.ButtonType.RED, () -> this.field_22787.close())));
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        for (CustomButton b : this.buttons) {
            b.click((int)mouseX, (int)mouseY, button);
        }
        return super.method_25402(mouseX, mouseY, button);
    }
}

