/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.graalvm.polyglot.Value
 */
package im.leet.api.scripts.api.bindings.event;

import im.leet.api.scripts.Script;
import im.leet.api.scripts.api.bindings.event.IEventProvider;
import org.graalvm.polyglot.Value;

public class EventProvider {
    private final Script script;

    public EventProvider(Script script) {
        this.script = script;
    }

    public EventProvider set(Value provider) {
        this.script.setEventProvider((IEventProvider)provider.as(IEventProvider.class));
        return this;
    }
}

