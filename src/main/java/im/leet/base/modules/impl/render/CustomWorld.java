/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.modules.impl.render;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event3D;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.slider.SliderSetting;
import java.awt.Color;
import net.minecraft.util.math.MathHelper;

public class CustomWorld
extends Module {
    public static final CustomWorld INSTANCE = new CustomWorld();
    final Group timeGroup = this.group("Time setting");
    final EnumSetting<TimeSpec> time = this.timeGroup.enumSetting("Time", TimeSpec.DAY);
    final SliderSetting customTime = (SliderSetting)this.timeGroup.sliderSetting("Custom", 1000.0f, 0.0f, 24000.0f).visible(() -> this.time.is(TimeSpec.CUSTOM));
    final Group weatherGroup = this.group("Weather setting");
    final EnumSetting<WeatherSpec> weather = this.weatherGroup.enumSetting("Weather", WeatherSpec.SUNNY);
    final SliderSetting customRainGradient = (SliderSetting)this.weatherGroup.sliderSetting("Custom rain gradient", 0.0f, 0.0f, 1.0f).increment(0.1f).visible(() -> this.weather.is(WeatherSpec.CUSTOM));
    final SliderSetting customThunderGradient = (SliderSetting)this.weatherGroup.sliderSetting("Custom thunder gradient", 0.0f, 0.0f, 1.0f).increment(0.1f).visible(() -> this.weather.is(WeatherSpec.CUSTOM));
    final Group fogGroup = this.group("Fog setting");
    public CheckBox useFog = this.fogGroup.add(new CheckBox("Fog", true));
    public ColorSetting fogColor = this.fogGroup.add(new ColorSetting("Fog color", Color.PINK));
    public SliderSetting fogDistance = this.fogGroup.add(new SliderSetting("Fog distance", 1.0f, 0.0f, 1.0f).increment(0.01f));
    float animatedTime = 0.0f;
    EventBus<Event> events = event -> {
        if (event instanceof Event3D) {
            Event3D e = (Event3D)event;
            this.animatedTime = MathHelper.method_16439((float)mc.method_61966().method_60637(true), (float)this.animatedTime, (float)(this.animatedTime + ((float)this.time.get().getTime() - this.animatedTime) * 0.01f));
        }
    };

    private CustomWorld() {
        super("CustomWorld", Category.RENDER, "Allows you to customize the world", new Tag[0]);
    }

    public long getTime(long original) {
        if (!this.isEnabled() || this.time.is(TimeSpec.NONE)) {
            return original;
        }
        if (this.time.is(TimeSpec.CUSTOM)) {
            return (long)this.customTime.get();
        }
        return (long)this.animatedTime;
    }

    public float getRainGradient() {
        if (!this.isEnabled() || this.weather.is(WeatherSpec.NONE)) {
            return -1.0f;
        }
        if (this.weather.is(WeatherSpec.CUSTOM)) {
            return this.customRainGradient.get();
        }
        return this.weather.get().getRainGradient();
    }

    public float getThunderGradient() {
        if (!this.isEnabled() || this.weather.is(WeatherSpec.NONE)) {
            return -1.0f;
        }
        if (this.weather.is(WeatherSpec.CUSTOM)) {
            return this.customThunderGradient.get();
        }
        return this.weather.get().getThunderGradient();
    }

    static enum TimeSpec implements EnumChoice
    {
        DAY("\u0414\u0435\u043d\u044c", 1000),
        MIDNIGHT("\u041d\u043e\u0447\u044c", 18000),
        NIGHT("\u041f\u0430\u0441\u043c\u0443\u0440\u043d\u043e", 12300),
        CUSTOM("\u0421\u0432\u043e\u0451", -1),
        NONE("\u041d\u0435 \u043c\u0435\u043d\u044f\u0442\u044c", -1);

        final String renderName;
        final int time;

        private TimeSpec(String renderName, int time) {
            this.renderName = renderName;
            this.time = time;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }

        public int getTime() {
            return this.time;
        }
    }

    static enum WeatherSpec implements EnumChoice
    {
        SUNNY("\u0421\u043e\u043b\u043d\u0435\u0447\u043d\u043e", 0.0f, 0.0f),
        RAINY("\u0414\u043e\u0436\u0434\u044c", 1.0f, 0.0f),
        THUNDER("\u0413\u0440\u043e\u0437\u0430", 1.0f, 1.0f),
        SNOWY("\u0421\u043d\u0435\u0433", 0.9f, 0.0f),
        CUSTOM("\u0421\u0432\u043e\u0451", -1.0f, -1.0f),
        NONE("\u041d\u0435 \u043c\u0435\u043d\u044f\u0442\u044c", -1.0f, -1.0f);

        final String renderName;
        final float rainGradient;
        final float thunderGradient;

        private WeatherSpec(String renderName, float rainGradient, float thunderGradient) {
            this.renderName = renderName;
            this.rainGradient = rainGradient;
            this.thunderGradient = thunderGradient;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }

        public float getRainGradient() {
            return this.rainGradient;
        }

        public float getThunderGradient() {
            return this.thunderGradient;
        }
    }
}

