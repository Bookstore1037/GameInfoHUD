package com.bookstore.gameinfohud;

import com.bookstore.gameinfohud.handler.KeyInputHandler;
import com.bookstore.gameinfohud.handler.ClientConfigurationHandler;
import com.bookstore.gameinfohud.handler.ConfigurationHandler;
import com.bookstore.gameinfohud.network.PacketHandler;
import com.bookstore.gameinfohud.reference.Names;
import com.bookstore.gameinfohud.tag.registry.TagRegistry;
import com.bookstore.gameinfohud.value.registry.ValueRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Mod(GameInfoHud.MODID)
public class GameInfoHud {

    public static final String MODID = "gameinfohud";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public GameInfoHud(FMLJavaModLoadingContext context) {
        // 1.20.1：用构造器注入的 context，不用 ModLoadingContext.get()
        context.registerConfig(ModConfig.Type.CLIENT, ConfigurationHandler.SPEC);
        PacketHandler.register();

        // TODO: Cloth Config 可选配置界面，主干跑通后再接
        // if (ModList.get().isLoaded("cloth_config")) {
        //     context.registerExtensionPoint(
        //             ConfigScreenHandler.ConfigScreenFactory.class,
        //             () -> new ConfigScreenHandler.ConfigScreenFactory(
        //                     (mc, screen) -> HudConfigScreen.create(screen)));
        // }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                File configDir = FMLPaths.CONFIGDIR.get().resolve(MODID).toFile();
                if (!configDir.exists() && !configDir.mkdirs()) {
                    LOGGER.warn("Failed to create config dir: {}", configDir.getAbsolutePath());
                }

                ValueRegistry.INSTANCE.init();
                TagRegistry.INSTANCE.init();

                GameInfoCore core = GameInfoCore.INSTANCE;

                File configXml = new File(configDir, Names.Files.FILE_XML);
                if (!configXml.exists()) {
                    Minecraft mc = Minecraft.getInstance();  // ← 加这行
                    try (InputStream in = mc.getResourceManager()
                            .getResourceOrThrow(ResourceLocation.fromNamespaceAndPath(MODID, Names.Files.FILE_XML)).open();
                         OutputStream out = new FileOutputStream(configXml)) {
                        in.transferTo(out);
                    } catch (Exception e) {
                        LOGGER.error("Failed to copy default config", e);
                    }
                }

                core.setConfigFile(Names.Files.FILE_XML);
                core.reloadConfig();
                core.setConfigDirectory(configDir);
                core.setConfigFileWithLocale(ConfigurationHandler.configName);
                core.reloadConfig();

            });
        }

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KeyInputHandler.KEY_BINDING_TOGGLE);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onAddReloadListeners(AddReloadListenerEvent event) {
            event.addListener(ClientConfigurationHandler.INSTANCE);
        }
    }
}