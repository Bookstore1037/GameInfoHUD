package com.bookstore.gameinfohud.network;

import com.bookstore.gameinfohud.GameInfoHud;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = GameInfoHud.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerSyncHandler {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        var overworld = player.server.overworld();

        // 种子
        PacketHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                new SyncSeedPacket(overworld.getSeed()));
    }
}
