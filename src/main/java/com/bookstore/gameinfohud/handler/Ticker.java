package com.bookstore.gameinfohud.handler;

import com.bookstore.gameinfohud.GameInfoCore;
import com.bookstore.gameinfohud.GameInfoHud;
import com.bookstore.gameinfohud.tag.Tag;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GameInfoHud.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Ticker {

    private static final Minecraft client = Minecraft.getInstance();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (isRunning()) {
                GameInfoCore.INSTANCE.onTickClient();
            }
            Tag.releaseResources();
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (isRunning()) {
            GameInfoCore.INSTANCE.onTickRender(event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        Tag.onClientDisconnect();
    }

    private static boolean isRunning() {
        if (!ConfigurationHandler.showHUD) return false;

        if (ConfigurationHandler.replaceDebug == client.options.renderDebug) {
            if (client.options.hideGui) return false;

            if (!ConfigurationHandler.showOnPlayerList && client.options.keyPlayerList.isDown()) {
                return false;
            }

            if (client.screen == null) return true;

            return ConfigurationHandler.showInChat && client.screen instanceof ChatScreen;
        }

        return false;
    }
}
