/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.render.customyaica;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;

public class CustomModel
extends Module {
    public static final CustomModel INSTANCE = new CustomModel();
    public EnumSetting<Model> model = this.enumSetting("Model", Model.CrazyRabbit);
    public CheckBox showOnFriends = this.checkbox("Show on friends", true);

    private CustomModel() {
        super("CustomModel", Category.PLAYER, "\u0421\u0442\u0430\u0432\u0438\u0442 \u043a\u0430\u043a\u0443\u044e \u0442\u043e \u0445\u0435\u0440\u043e\u0442\u0435\u043d\u044c \u0432\u043c\u0435\u0441\u0442\u043e \u043c\u043e\u0434\u0435\u043b\u044c\u043a\u0438 \u0438\u0433\u0440\u043e\u043a\u0430", new Tag[0]);
    }

    public static enum Model {
        CrazyRabbit;

    }
}

