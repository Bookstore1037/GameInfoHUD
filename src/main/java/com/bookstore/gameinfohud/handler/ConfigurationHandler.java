package com.bookstore.gameinfohud.handler;

import java.util.HashMap;
import java.util.Map;

import com.bookstore.gameinfohud.Alignment;
import com.bookstore.gameinfohud.GameInfoHud;
import com.bookstore.gameinfohud.reference.Names;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = GameInfoHud.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigurationHandler {

    public static final String CONFIG_NAME_DEFAULT = Names.Files.FILE_XML;
    public static final boolean SHOW_HUD_DEFAULT = true;
    public static final boolean REPLACE_DEBUG_DEFAULT = false;
    public static final boolean SHOW_IN_CHAT_DEFAULT = true;
    public static final boolean SHOW_ON_PLAYER_LIST_DEFAULT = true;
    public static final int DEFAULT_SCALE = 10;
    public static final int FILE_INTERVAL_DEFAULT = 5;

    public static String configName = CONFIG_NAME_DEFAULT;
    public static boolean showHUD = SHOW_HUD_DEFAULT;
    public static boolean replaceDebug = REPLACE_DEBUG_DEFAULT;
    public static boolean showInChat = SHOW_IN_CHAT_DEFAULT;
    public static boolean showOnPlayerList = SHOW_ON_PLAYER_LIST_DEFAULT;
    public static float scale = DEFAULT_SCALE;
    public static int fileInterval = FILE_INTERVAL_DEFAULT;

    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.ConfigValue<String> CONFIG_NAME;
    private static final ForgeConfigSpec.BooleanValue SHOW_HUD;
    private static final ForgeConfigSpec.BooleanValue REPLACE_DEBUG;
    private static final ForgeConfigSpec.BooleanValue SHOW_IN_CHAT;
    private static final ForgeConfigSpec.BooleanValue SHOW_ON_PLAYER_LIST;
    private static final ForgeConfigSpec.IntValue SCALE;
    private static final ForgeConfigSpec.IntValue FILE_INTERVAL;
    private static final Map<Alignment, ForgeConfigSpec.ConfigValue<String>> ALIGNMENTS = new HashMap<>();

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("general");
        CONFIG_NAME = builder.comment(Names.Config.FILENAME_DESC).define("filename", CONFIG_NAME_DEFAULT);
        SHOW_HUD = builder.comment(Names.Config.SHOW_HUD_DESC).define("showHUD", SHOW_HUD_DEFAULT);
        REPLACE_DEBUG = builder.comment(Names.Config.REPLACE_DEBUG_DESC).define("replaceDebug", REPLACE_DEBUG_DEFAULT);
        SHOW_IN_CHAT = builder.comment(Names.Config.SHOW_IN_CHAT_DESC).define("showInChat", SHOW_IN_CHAT_DEFAULT);
        SHOW_ON_PLAYER_LIST = builder.comment(Names.Config.SHOW_ON_PLAYER_LIST_DESC)
                .define("showOnPlayerList", SHOW_ON_PLAYER_LIST_DEFAULT);
        SCALE = builder.comment(Names.Config.SCALE_DESCRIPTION).defineInRange("scale", DEFAULT_SCALE, 1, 20);
        FILE_INTERVAL = builder.comment(Names.Config.FILE_INTERVAL_DESC).defineInRange("fileInterval", FILE_INTERVAL_DEFAULT, 1, 60);
        builder.pop();

        builder.push("alignment");
        for (Alignment alignment : Alignment.values()) {
            ALIGNMENTS.put(alignment, builder
                    .comment(String.format(Names.Config.ALIGNMENT_DESC, alignment))
                    .define(alignment.name().toLowerCase(), alignment.getDefaultXY()));
        }
        builder.pop();

        SPEC = builder.build();
    }

    public static void load() {
        configName = CONFIG_NAME.get();
        showHUD = SHOW_HUD.get();
        replaceDebug = REPLACE_DEBUG.get();
        showInChat = SHOW_IN_CHAT.get();
        showOnPlayerList = SHOW_ON_PLAYER_LIST.get();
        scale = SCALE.get();
        fileInterval = FILE_INTERVAL.get();

        for (Map.Entry<Alignment, ForgeConfigSpec.ConfigValue<String>> entry : ALIGNMENTS.entrySet()) {
            entry.getKey().setXY(entry.getValue().get());
        }
    }

    public static void saveHUDsettingToFile() {
        SHOW_HUD.set(showHUD);
        SPEC.save();
    }

    @SubscribeEvent
    public static void onConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getModId().equals(GameInfoHud.MODID)) {
            load();
        }
    }
}
