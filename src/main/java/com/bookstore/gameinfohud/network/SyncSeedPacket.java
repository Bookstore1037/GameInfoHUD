package com.bookstore.gameinfohud.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SyncSeedPacket {

    private final long seed;

    public SyncSeedPacket(long seed) {
        this.seed = seed;
    }

    // ---------- 编解码 ----------

    public static void encode(SyncSeedPacket packet, FriendlyByteBuf buf) {
        buf.writeLong(packet.seed);
    }

    public static SyncSeedPacket decode(FriendlyByteBuf buf) {
        return new SyncSeedPacket(buf.readLong());
    }

    // ---------- 处理 ----------

    public static void handle(SyncSeedPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // 这里只在客户端执行（服务端收到也无害，但正常不会发到服务端）
            ClientWorldData.setSeed(packet.seed);
        });
        context.setPacketHandled(true);
    }
}