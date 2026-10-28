package com.bookstore.gameinfohud.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import org.jetbrains.annotations.NotNull;

public abstract class Info {

    protected static final Font font = Minecraft.getInstance().font;
    public int x;
    public int y;
    public int offsetX;
    public int offsetY;
    private String identifier = "";
    private String iconSpacing = "";
    public boolean hasPosition = false;
    private int oldWidth;

    protected Info(int x, int y) {
        this.x = x;
        this.y = y;
        this.offsetX = 0;
        this.offsetY = 0;
    }

    public void draw(GuiGraphics guiGraphics) {
        drawInfo(guiGraphics);
    }

    public abstract void drawInfo(GuiGraphics guiGraphics);

    public int getX() {
        return this.x + this.offsetX;
    }

    public int getY() {
        return this.y + this.offsetY;
    }

    public String getIconSpacing() {
        if (oldWidth != getWidth()) {
            setIconSpacing();
            oldWidth = getWidth();
        }
        return iconSpacing;
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    public void setIdentifier(@NotNull String identifier) {
        this.identifier = identifier;
    }

    public void setValue(@NotNull Object value) {}

    public String getIdentifier() {
        return this.identifier;
    }

    private void setIconSpacing() {
        String str = "";
        for (int i = 0; i < getWidth() && font.width(str) < getWidth(); i++) {
            str += " ";
        }
        iconSpacing = str;
    }

    @Override
    public String toString() {
        return "Info";
    }
}
