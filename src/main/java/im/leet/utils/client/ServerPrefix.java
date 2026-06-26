/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.client;

public final class ServerPrefix {
    static final String[] RW_MAP = new String[]{"\ua500\ua501\ua502\ua503", "\ua504\ua505\ua506\ua507", "\ua508\ua509\ua510\ua511", "\ua512\ua513\ua514\ua515", "\ua516\ua517\ua518\ua519", "\ua520\ua521\ua522\ua523", "\ua524\ua525\ua526\ua527", "\ua528\ua529\ua530\ua531", "\ua532\ua533\ua534\ua535", "\ua536\ua537\ua538\ua539", "\ua540\ua541\ua542\ua543", "\ua544\ua545\ua546\ua547", "\ua548\ua549\ua550\ua551", "\ua552\ua553\ua554\ua555", "\ua556\ua557\ua558\ua559", "\ua560\ua561\ua562\ua563"};

    public static int[] rwFrom(String c) {
        int x = -1;
        int y = -1;
        boolean found = false;
        for (y = 0; y < RW_MAP.length; ++y) {
            boolean stop = false;
            for (x = 0; x < RW_MAP[y].length(); ++x) {
                if (RW_MAP[y].charAt(x) != c.charAt(0)) continue;
                stop = true;
                found = true;
                break;
            }
            if (stop) break;
        }
        if (!found) {
            x = -1;
            y = -1;
        }
        return new int[]{x, y};
    }

    private ServerPrefix() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

