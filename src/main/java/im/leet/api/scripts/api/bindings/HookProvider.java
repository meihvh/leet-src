/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.graalvm.polyglot.HostAccess$Export
 */
package im.leet.api.scripts.api.bindings;

import im.leet.Client;
import im.leet.api.scripts.Script;
import im.leet.api.scripts.api.integrate.IHook;
import im.leet.api.scripts.api.integrate.ScriptHook;
import org.graalvm.polyglot.HostAccess;

public class HookProvider {
    Script script;

    public HookProvider(Script script) {
        this.script = script;
    }

    @HostAccess.Export
    public void hook(String enum_, IHook<?> solution) {
        Client.SCRIPTS.getScriptHook().createHook(ScriptHook.HookValue.valueOf(enum_), solution, this.script);
    }
}

