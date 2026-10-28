package com.bookstore.gameinfohud.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

public class InfoIcon extends Info {

    private ResourceLocation resourceLocation;
    private int displayX;
    private int displayY;
    private int displayWidth;
    private int displayHeight;
    private int iconX;
    private int iconY;
    private int iconWidth;
    private int iconHeight;
    private int textureWidth;
    private int textureHeight;

    public InfoIcon(String location) {
        this(location, 0, 0, 8, 8, 0, 0, 8, 8, 8, 8, 0, 0);
    }

    public InfoIcon(String location, int displayX, int displayY, int displayWidth, int displayHeight, int iconX,
            int iconY, int iconWidth, int iconHeight, int textureWidth, int textureHeight, int x, int y) {
        super(x, y);
        this.resourceLocation = ResourceLocation.parse(location);
        setDisplayDimensions(displayX, displayY, displayWidth, displayHeight);
        setTextureData(iconX, iconY, iconWidth, iconHeight, textureWidth, textureHeight);
    }

    public void setDisplayDimensions(int displayX, int displayY, int displayWidth, int displayHeight) {
        this.displayX = displayX;
        this.displayY = displayY;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
    }

    public void setTextureData(int iconX, int iconY, int iconWidth, int iconHeight, int textureWidth,
            int textureHeight) {
        this.iconX = iconX;
        this.iconY = iconY;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void drawInfo(GuiGraphics guiGraphics) {
        guiGraphics.blit(
                resourceLocation,
                getX() + displayX,
                getY() + displayY,
                displayWidth,
                displayHeight,
                iconX,
                iconY,
                iconWidth,
                iconHeight,
                textureWidth,
                textureHeight);
    }

    @Override
    public void setValue(@NotNull Object value) {
        this.resourceLocation = ResourceLocation.parse(value.toString());
    }

    @Override
    public int getWidth() {
        return this.displayWidth;
    }

    @Override
    public int getHeight() {
        return this.displayHeight;
    }

    @Override
    public String toString() {
        return String.format(
                "InfoIcon{resource: %s, x: %d, y: %d, offsetX: %d, offsetY: %d}",
                this.resourceLocation,
                this.x,
                this.y,
                this.offsetX,
                this.offsetY);
    }
}
