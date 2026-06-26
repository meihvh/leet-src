/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.network.PlayerListEntry
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.MathHelper
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.api.ui.widgets.other.pages;

import com.mojang.authlib.GameProfile;
import im.leet.Client;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIPage;
import im.leet.api.ui.parts.impl.UITextField;
import im.leet.api.ui.widgets.impl.TextFieldWidget;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.ElasticAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.Rectangle;
import im.leet.utils.network.NetworkUtility;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class FriendPage
extends UIPage {
    private UITextField textField = new UITextField();
    private TextFieldWidget friendInput = new TextFieldWidget(this.textField);
    private Rectangle friendAdd = new Rectangle();
    private Map<String, FriendObject> friendObjects = new HashMap<String, FriendObject>();
    private Map<String, Rectangle> playerHitbox = new HashMap<String, Rectangle>();
    private float scroll;
    private float scrollAnim;

    public FriendPage() {
        this.textField.setDesc("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0438\u043c\u044f \u0434\u0440\u0443\u0433\u0430");
        this.textField.setLimit(16);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        this.friendInput.bound(this.x, this.y + 35.0f, 120.0f, 15.0f).render(mouseX, mouseY);
        this.friendAdd.bound(this.x + 120.0f, this.y + 35.0f, 15.0f, 15.0f);
        Client.RENDERER.rect(this.friendAdd, new Vector4f(8.0f), 1.0f, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        Client.RENDERER.text(IconUse.ADD, this.friendAdd.getX() + 2.8f, this.friendAdd.getY() + 3.0f, TextureUse.ICONS, 8.0f, ClientColors.DARK_GRAY_COLOR);
        if (FriendPage.mc.field_1687 != null) {
            float off = 0.0f;
            this.playerHitbox.clear();
            for (PlayerListEntry entry : mc.method_1562().method_2880()) {
                String profile = entry.method_2966().getName();
                if (off > this.width - 180.0f || Client.FRIENDS.isFriend(profile) || !profile.toLowerCase().startsWith(this.textField.getText().toLowerCase())) {
                    this.playerHitbox.remove(entry.method_2966().getName());
                    continue;
                }
                if (!this.playerHitbox.containsKey(profile)) {
                    this.playerHitbox.put(profile, new Rectangle());
                }
                Rectangle rect = this.playerHitbox.get(profile);
                float wd = Client.RENDERER.textWidth(profile, TextureUse.SFMEDIUM, 8.0f);
                float hg = Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 8.0f);
                rect.bound(this.x + 150.0f + off - 4.0f, this.y + 42.5f - hg / 2.0f - 4.0f, wd + 8.0f, hg + 8.0f);
                Color bgColor = ClientColors.GUI_BACKGROUND;
                Color thumbBackColor = ClientColors.GUI_STROKE;
                Client.RENDERER.rect(rect, new Vector4f(8.0f), 1.0f, bgColor, bgColor, bgColor, bgColor);
                Client.RENDERER.outline(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), 0.0f, new Vector4f(8.0f), new Vector2f(1.0f), thumbBackColor, thumbBackColor, thumbBackColor, thumbBackColor);
                int ln = Math.min(this.textField.getText().length(), profile.length());
                Client.RENDERER.text(profile.substring(0, ln), rect.getX() - 1.0f + rect.getWidth() / 2.0f - wd / 2.0f, rect.getY() + 4.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
                Client.RENDERER.text(profile.substring(ln), rect.getX() - 1.0f + rect.getWidth() / 2.0f - wd / 2.0f + Client.RENDERER.textWidth(profile.substring(0, ln), TextureUse.SFMEDIUM, 8.0f), rect.getY() + 4.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.DARK_GRAY_COLOR);
                off += wd + 10.0f;
            }
        }
        float offset = 0.0f;
        float offX = 0.0f;
        FriendObject last = null;
        system.push(this.x, this.y + 60.0f, this.width, this.height - 60.0f);
        for (String friend2 : Client.FRIENDS.getFriendList()) {
            boolean removing;
            if (!this.friendObjects.containsKey(friend2)) {
                this.friendObjects.put(friend2, new FriendObject(friend2, this));
            }
            FriendObject rect = this.friendObjects.get(friend2);
            rect.prev = last;
            boolean bl = removing = rect.animation.getDirection() == Direction.BACKWARDS && (double)rect.animation.getOutput() < 0.5;
            if (offX > this.width - 8.0f && !removing) {
                offX = 0.0f;
                offset += rect.getHeight() + 4.0f;
            }
            rect.render(mouseX, mouseY);
            if (removing) continue;
            rect.bound(this.x + offX, this.y + 60.0f + offset + this.scrollAnim, this.width / 3.0f - 4.0f, 25.0f);
            last = rect;
            offX += rect.getWidth() + 4.0f;
        }
        system.pop();
        Iterator<FriendObject> iter = this.friendObjects.values().iterator();
        while (iter.hasNext()) {
            FriendObject obj = iter.next();
            if (!obj.shouldRemove()) continue;
            Client.FRIENDS.removeFriend(obj.friend);
            iter.remove();
        }
        if (this.friendObjects.isEmpty()) {
            Client.RENDERER.textCentered("No friends added", this.x + this.width / 2.0f - 1.0f, this.y + this.height / 2.0f - Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 8.0f) / 2.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.DARK_GRAY_COLOR);
        }
        float scrollHeight = offset;
        this.scroll = MathHelper.method_15363((float)this.scroll, (float)(-scrollHeight), (float)1.0f);
        this.scrollAnim = MathUtility.linearFps(this.scrollAnim, this.scroll, 10.0f);
        this.friendObjects.keySet().removeIf(friend -> !Client.FRIENDS.getFriendList().contains(friend));
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.friendInput.click(mouseX, mouseY, button)) {
            return true;
        }
        if (this.friendAdd.hovered(mouseX, mouseY) && button == 0) {
            Client.FRIENDS.addFriend(this.textField.getText());
            this.textField.setText("");
        }
        for (FriendObject friendObject : this.friendObjects.values()) {
            if (!friendObject.click(mouseX, mouseY, button)) continue;
            return true;
        }
        for (Map.Entry entry : this.playerHitbox.entrySet()) {
            if (!((Rectangle)entry.getValue()).hovered(mouseX, mouseY) || button != 0) continue;
            Client.FRIENDS.addFriend((String)entry.getKey());
        }
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void release(int button) {
        for (FriendObject object : this.friendObjects.values()) {
            object.release(button);
        }
        this.friendInput.release(button);
        super.release(button);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        this.friendInput.keyPressed(keyCode, scanCode, modifiers);
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        this.friendInput.chartyped(ch, keyCode);
        super.chartyped(ch, keyCode);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.scroll += (float)(verticalAmount * 10.0);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public static class FriendObject
    extends RendererObject {
        String friend;
        ElasticAnimation animation;
        float lerpedX = -999.0f;
        float lerpedY = -999.0f;
        public float lerpProgress = 0.0f;
        public FriendObject prev = null;
        private FriendPage page;

        public FriendObject(String friend, FriendPage parent) {
            this.friend = friend;
            this.animation = new ElasticAnimation(1000, 1.0, 5.0f, 2.0f, false);
            this.animation.reset();
            this.page = parent;
        }

        public boolean shouldRemove() {
            return this.animation.getDirection() == Direction.BACKWARDS && (double)this.animation.getOutput() < 0.1;
        }

        @Override
        public void render(int mouseX, int mouseY) {
            if (this.lerpedX == -999.0f) {
                this.lerpedX = this.x - this.page.x;
            }
            if (this.lerpedY == -999.0f) {
                this.lerpedY = this.y - this.page.y;
            }
            this.lerpedX = MathUtility.linearFps(this.lerpedX, this.x - this.page.x, 15.0f);
            this.lerpedY = MathUtility.linearFps(this.lerpedY, this.y - this.page.y, 15.0f);
            float x_ = this.page.x + this.lerpedX;
            float y_ = this.page.y + this.lerpedY;
            this.lerpProgress = (float)Math.sqrt(this.lerpedX * this.lerpedX + this.lerpedY * this.lerpedY);
            MatrixStack matrixStack = Client.RENDERER.getStack();
            matrixStack.method_22903();
            MathUtility.scale(matrixStack, this.x + this.width / 2.0f, this.y + this.height / 2.0f, this.animation.getOutput());
            Client.RENDERER.outlined(x_, y_, this.width, this.height);
            Identifier identifier = mc.method_1582().method_52862(new GameProfile(NetworkUtility.offlineUUID(this.friend), this.friend)).comp_1626();
            Client.RENDERER.texture(identifier, x_ + 4.0f, y_ + 4.0f, 16.0f, 16.0f, 1.0f, new Vector4f(0.04f, 0.038f, 0.345f, 0.5f), new Vector4f(4.0f), Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
            Client.RENDERER.text(this.friend, x_ + 24.0f, y_ + this.height / 2.0f - Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 8.0f) / 2.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
            Client.RENDERER.text(IconUse.CROSS, x_ + this.width - 16.0f, y_ + this.height / 2.0f - Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 8.0f) / 2.0f, TextureUse.ICONS, 8.0f, ClientColors.UI_RED);
            matrixStack.method_22909();
        }

        @Override
        public boolean click(int mouseX, int mouseY, int button) {
            if (MathUtility.mouseIn(this.x + this.width - 16.0f, this.y + this.height / 2.0f - Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 8.0f) / 2.0f, 16.0f, 16.0f, mouseX, mouseY)) {
                this.animation.setDirection(Direction.BACKWARDS);
            }
            return super.click(mouseX, mouseY, button);
        }

        @Override
        public void release(int button) {
            super.release(button);
        }
    }
}

