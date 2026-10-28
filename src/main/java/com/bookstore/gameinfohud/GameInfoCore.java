package com.bookstore.gameinfohud;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bookstore.gameinfohud.client.gui.InfoText;
import com.bookstore.gameinfohud.handler.ConfigurationHandler;
import com.bookstore.gameinfohud.parser.IParser;
import com.bookstore.gameinfohud.parser.json.JsonParser;
import com.bookstore.gameinfohud.parser.text.TextParser;
import com.bookstore.gameinfohud.parser.xml.XmlParser;
import com.bookstore.gameinfohud.reference.Names;
import com.bookstore.gameinfohud.tag.Tag;
import com.bookstore.gameinfohud.value.Value;
import com.bookstore.gameinfohud.value.ValueComplex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

public class GameInfoCore {

    public static final GameInfoCore INSTANCE = new GameInfoCore();

    private IParser parser;

    private final Minecraft minecraft = Minecraft.getInstance();
    private File configDirectory = null;
    private File configFile = null;
    /** Config file name without locale code suffix. */
    private String baseConfigFileName;
    private final Map<Alignment, List<List<Value>>> format = new HashMap<>();
    public int scaledWidth;
    public int scaledHeight;
    private boolean needsRefresh = true;

    private GameInfoCore() {}

    public void setConfigDirectory(File directory) {
        configDirectory = directory;
    }

    public File getConfigDirectory() {
        return configDirectory;
    }

    public void setConfigFileWithLocale() {
        if (baseConfigFileName != null) {
            setConfigFileWithLocale(baseConfigFileName);
        }
    }

    public String getBaseConfigFileName() {
        return baseConfigFileName;
    }

    public boolean hasConfigFileWithLocale(String filename) {
        if (configDirectory == null || filename == null) {
            return false;
        }
        String localeAwareFileName = getLocaleAwareFilename(filename);
        if (new File(configDirectory, localeAwareFileName).isFile()) {
            return true;
        }
        return new File(configDirectory, filename).isFile();
    }

    public void setConfigFileWithLocale(String filename) {
        String localeAwareFileName = getLocaleAwareFilename(filename);
        if (new File(configDirectory, localeAwareFileName).isFile()) {
            setConfigFile(localeAwareFileName, filename);
        } else {
            setConfigFile(filename);
        }
    }

    public String getLocaleAwareFilename(String filename) {
        String userLang = minecraft.getLanguageManager().getSelected();
        String[] parts = filename.split("\\.", 2);
        String baseName = parts[0];
        String extension = parts.length > 1 ? "." + parts[1] : "";
        return baseName + "_" + userLang + extension;
    }

    public boolean setConfigFile(String filename) {
        return setConfigFile(filename, filename);
    }

    public boolean setConfigFile(String filename, String baseName) {
        File file = new File(configDirectory, filename);
        if (file.exists()) {
            configFile = file;
            baseConfigFileName = baseName;
            if (filename.endsWith(Names.Files.EXT_XML)) {
                parser = new XmlParser();
            } else if (filename.endsWith(Names.Files.EXT_JSON)) {
                parser = new JsonParser();
            } else if (filename.endsWith(Names.Files.EXT_TXT)) {
                parser = new TextParser();
            }
            return true;
        }

        configFile = null;
        parser = new XmlParser();
        baseConfigFileName = null;
        return false;
    }

    public void onTickClient() {
        float scale = ConfigurationHandler.scale / 10.0f;
        scaledWidth = (int) (minecraft.getWindow().getGuiScaledWidth() / scale);
        scaledHeight = (int) (minecraft.getWindow().getGuiScaledHeight() / scale);
        Tag.update();

        if (needsRefresh) {
            refreshInfoTexts();
            needsRefresh = false;
        }

        for (Alignment alignment : Alignment.VALUES) {
            int lastActiveIndex = 0;
            for (InfoText infoText : alignment.getLines()) {
                infoText.update(lastActiveIndex);
                if (infoText.isActive()) {
                    lastActiveIndex++;
                }
            }
        }

        ValueComplex.ValueFile.tick();
    }

    public void onTickRender(GuiGraphics guiGraphics) {
        float scale = ConfigurationHandler.scale / 10.0f;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0f);
        for (Alignment alignment : Alignment.VALUES) {
            for (InfoText info : alignment.getLines()) {
                info.draw(guiGraphics);
            }
        }
        guiGraphics.pose().popPose();
    }

    public boolean reloadConfig() {
        needsRefresh = true;
        format.clear();

        if (parser == null) {
            return false;
        }

        try (InputStream inputStream = getInputStream()) {
            if (inputStream == null) {
                return false;
            }
            if (parser.load(inputStream) && parser.parse(format)) {
                return true;
            }
        } catch (IOException e) {
            GameInfoHud.LOGGER.error("Failed to load config", e);
        }

        format.clear();
        return false;
    }

    private InputStream getInputStream() {
        try {
            if (configFile != null && configFile.exists()) {
                GameInfoHud.LOGGER.debug("Loading file config...");
                return new FileInputStream(configFile);
            } else {
                GameInfoHud.LOGGER.debug("Loading default config...");
                ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(
                        GameInfoHud.MODID, Names.Files.FILE_XML);
                Resource resource = minecraft.getResourceManager().getResourceOrThrow(resourceLocation);
                return resource.open();
            }
        } catch (Exception e) {
            GameInfoHud.LOGGER.error("", e);
        }

        return null;
    }

    public void refreshInfoTexts() {
        for (Alignment alignment : Alignment.VALUES) {
            List<List<Value>> lines = format.get(alignment);
            alignment.getLines().clear();

            if (lines == null) {
                continue;
            }

            for (List<Value> line : lines) {
                alignment.getLines().add(new InfoText(alignment, line));
            }
        }
    }

}
