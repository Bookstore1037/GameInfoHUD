package com.bookstore.gameinfohud.network;

public class ClientWorldData {

    // ---------- 种子（登录时同步一次） ----------
    private static long seed = 0L;

    public static void setSeed(long s) {
        seed = s;
    }

    public static long getSeed() {
        return seed;
    }

}
