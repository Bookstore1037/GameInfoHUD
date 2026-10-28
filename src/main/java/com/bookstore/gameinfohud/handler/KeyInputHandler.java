package com.bookstore.gameinfohud.handler;

import com.bookstore.gameinfohud.GameInfoCore;
import com.bookstore.gameinfohud.GameInfoHud;
import com.bookstore.gameinfohud.reference.Names;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = GameInfoHud.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyInputHandler {

    public static final KeyMapping KEY_BINDING_TOGGLE = new KeyMapping(
            Names.Keys.TOGGLE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            Names.Keys.CATEGORY);

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KEY_BINDING_TOGGLE.consumeClick()) {
            if (Minecraft.getInstance().screen == null) {
                toggleHud();
            }
        }
    }

    private static void toggleHud() {
        String primaryConfig = ConfigurationHandler.configName;
        String secondaryConfig = getSecondaryConfigName(primaryConfig);
        GameInfoCore core = GameInfoCore.INSTANCE;
        boolean hasSecondary = core.hasConfigFileWithLocale(secondaryConfig);

        if (!hasSecondary) {
            ConfigurationHandler.showHUD = !ConfigurationHandler.showHUD;
            ConfigurationHandler.saveHUDsettingToFile();
            return;
        }

        if (!ConfigurationHandler.showHUD) {
            ConfigurationHandler.showHUD = true;
            ConfigurationHandler.saveHUDsettingToFile();
            core.setConfigFileWithLocale(primaryConfig);
            core.reloadConfig();
            return;
        }

        String currentConfig = core.getBaseConfigFileName();
        if (currentConfig != null && secondaryConfig.equalsIgnoreCase(currentConfig)) {
            ConfigurationHandler.showHUD = false;
            ConfigurationHandler.saveHUDsettingToFile();
            return;
        }

        core.setConfigFileWithLocale(secondaryConfig);
        core.reloadConfig();
    }

    private static String getSecondaryConfigName(String primaryConfig) {
        if (primaryConfig == null || primaryConfig.isEmpty()) {
            return "InGameInfo2.xml";
        }

        int dotIndex = primaryConfig.lastIndexOf('.');
        if (dotIndex < 0) {
            return primaryConfig + "2";
        }

        return primaryConfig.substring(0, dotIndex) + "2" + primaryConfig.substring(dotIndex);
    }
}
