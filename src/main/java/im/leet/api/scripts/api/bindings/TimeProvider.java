/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.graalvm.polyglot.HostAccess$Export
 */
package im.leet.api.scripts.api.bindings;

import im.leet.api.scripts.api.bindings.support.TimerScripted;
import org.graalvm.polyglot.HostAccess;

public class TimeProvider {
    @HostAccess.Export
    public TimerScripted create() {
        return new TimerScripted();
    }
}

