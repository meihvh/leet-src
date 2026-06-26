/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelPipeline
 *  io.netty.handler.proxy.Socks4ProxyHandler
 *  io.netty.handler.proxy.Socks5ProxyHandler
 */
package im.leet.utils.network;

import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.text.TextSetting;
import im.leet.utils.LogUtility;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ProxyUtility
extends Group {
    private final TextSetting address = this.text("Address", "localhost:9150");
    private final EnumSetting<Type> type = this.enumSetting("Type", Type.SOCKS5);
    private final TextSetting username = (TextSetting)this.text("Username", "").desc("Enter username");
    private final TextSetting password = (TextSetting)((TextSetting)this.text("Password", "").desc("Enter password")).setHideMask("*").visible(() -> this.type.is(Type.SOCKS5));

    public ProxyUtility() {
        super("Proxy", false);
    }

    public void fire(ChannelPipeline pipeline) {
        if (this.isEnabled()) {
            String v = this.address.getText();
            int colon = v.indexOf(58);
            String host = v.substring(0, colon);
            int port = Integer.parseInt(v.substring(colon + 1));
            LogUtility.debug(String.format("proxy address resolved! %s:%d", host, port));
            InetSocketAddress resolvedAddress = new InetSocketAddress(host, port);
            switch (this.type.get().ordinal()) {
                case 1: {
                    pipeline.addFirst("leet-socks4", (ChannelHandler)new Socks4ProxyHandler((SocketAddress)resolvedAddress, this.username.getText()));
                    break;
                }
                case 0: {
                    pipeline.addFirst("leet-socks5", (ChannelHandler)new Socks5ProxyHandler((SocketAddress)resolvedAddress, this.username.getText(), this.password.getText()));
                }
            }
        }
    }

    public static enum Type {
        SOCKS5,
        SOCKS4;

    }
}

