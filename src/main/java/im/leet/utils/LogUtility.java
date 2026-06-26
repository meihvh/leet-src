/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package im.leet.utils;

import im.leet.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtility {
    public static final Logger LOGGER = LoggerFactory.getLogger((String)"leet");

    public static void debug(Object object) {
        if (Client.IS_DEBUG) {
            System.out.println("[debug] " + String.valueOf(object));
        }
    }

    public static void error(Exception ex, String message) {
        LOGGER.error(message, (Throwable)ex);
    }
}

