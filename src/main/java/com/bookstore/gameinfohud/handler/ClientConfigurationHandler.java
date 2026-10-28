package com.bookstore.gameinfohud.handler;

import com.bookstore.gameinfohud.GameInfoCore;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class ClientConfigurationHandler implements ResourceManagerReloadListener {

    public static final ClientConfigurationHandler INSTANCE = new ClientConfigurationHandler();

    private ClientConfigurationHandler() {}

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        GameInfoCore.INSTANCE.setConfigFileWithLocale();
        GameInfoCore.INSTANCE.reloadConfig();
    }
}
