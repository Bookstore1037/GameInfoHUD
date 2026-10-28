package com.bookstore.gameinfohud.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public class InfoItem extends Info {

    private ItemStack itemStack;
    private final boolean large;
    private final int size;

    public InfoItem(ItemStack itemStack) {
        this(itemStack, false);
    }

    public InfoItem(ItemStack itemStack, boolean large) {
        this(itemStack, large, 0, 0);
    }

    public InfoItem(ItemStack itemStack, boolean large, int x, int y) {
        super(x, y);
        this.itemStack = itemStack;
        this.large = large;
        this.size = large ? 16 : 8;
        if (large) {
            this.y = -4;
        }
    }

    @Override
    public void drawInfo(GuiGraphics guiGraphics) {
        if (itemStack == null || itemStack.isEmpty()) return;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(getX(), getY(), 0);
        if (!large) {
            guiGraphics.pose().scale(0.5f, 0.5f, 1.0f);
        }
        guiGraphics.renderItem(itemStack, 0, 0);
        guiGraphics.pose().popPose();
    }

    @Override
    public void setValue(@NotNull Object value) {
        if (!(value instanceof ItemStack stack)) return;
        this.itemStack = stack;
    }

    @Override
    public int getWidth() {
        return itemStack != null && !itemStack.isEmpty() ? size : 0;
    }

    @Override
    public int getHeight() {
        return itemStack != null && !itemStack.isEmpty() ? size : 0;
    }

    @Override
    public String toString() {
        return String.format(
                "InfoItem{itemStack: %s, x: %d, y: %d, offsetX: %d, offsetY: %d}",
                this.itemStack,
                this.x,
                this.y,
                this.offsetX,
                this.offsetY);
    }
}
