/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.scripts.api.integrate;

import im.leet.api.scripts.Script;
import im.leet.api.scripts.api.integrate.IHook;
import java.util.HashMap;
import java.util.Map;

public class ScriptHook {
    public void createHook(HookValue hook, IHook<?> solution, Script script) {
        hook.processors.put(script, solution);
    }

    public Object solute(HookValue hook, Object val) {
        for (Script s : hook.processors.keySet()) {
            if (!s.getScriptedModule().isEnabled()) continue;
            return hook.processors.get(s).process(val);
        }
        return null;
    }

    public void cleanup() {
        for (HookValue value : HookValue.values()) {
            value.processors.clear();
        }
    }

    public static enum HookValue {
        ELYTRA_RESOLVER,
        AURA_ATTACK,
        AURA_POINT;

        public final Map<Script, IHook> processors = new HashMap<Script, IHook>();
    }
}

