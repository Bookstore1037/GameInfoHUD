package com.bookstore.gameinfohud.network;

import com.bookstore.gameinfohud.GameInfoHud;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(GameInfoHud.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private PacketHandler() {}

    /** 在 mod 构造器里调用一次，注册所有包 */
    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, SyncSeedPacket.class,
                SyncSeedPacket::encode, SyncSeedPacket::decode, SyncSeedPacket::handle);
    }
}
