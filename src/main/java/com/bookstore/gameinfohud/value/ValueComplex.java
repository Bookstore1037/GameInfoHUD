package com.bookstore.gameinfohud.value;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bookstore.gameinfohud.GameInfoCore;
import com.bookstore.gameinfohud.GameInfoHud;
import com.bookstore.gameinfohud.client.gui.Info;
import com.bookstore.gameinfohud.client.gui.InfoIcon;
import com.bookstore.gameinfohud.client.gui.InfoItem;
import com.bookstore.gameinfohud.handler.ConfigurationHandler;
import com.bookstore.gameinfohud.reference.Reference;
import com.bookstore.gameinfohud.tag.Tag;
import com.bookstore.gameinfohud.value.registry.ValueRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public abstract class ValueComplex extends Value {

    @Override
    public boolean isSimple() {
        return false;
    }

    public static class ValueOperation extends ValueComplex {

        @Override
        public boolean isValidSize() {
            return this.values.size() > 1;
        }

        @Override
        public String getValue() {
            try {
                Operation operation = Operation.fromString(getValue(0));
                return operation.getValue(this);
            } catch (Exception e) {
                return "";
            }
        }
    }

    public static class ValueConcat extends ValueComplex {

        @Override
        public boolean isValidSize() {
            return this.values.size() > 1;
        }

        @Override
        public String getValue() {
            StringBuilder str = new StringBuilder();
            for (Value val : this.values) {
                str.append(replaceVariables(val.getValue()));
            }
            return str.toString();
        }
    }

    public static class ValueMin extends ValueComplex {

        @Override
        public boolean isValidSize() {
            return this.values.size() == 2 || this.values.size() == 4;
        }

        @Override
        public String getValue() {
            try {
                double arg0 = getDoubleValue(0);
                double arg1 = getDoubleValue(1);
                int shift = this.values.size() - 2;
                return arg0 < arg1 ? getValue(shift) : getValue(1 + shift);
            } catch (Exception e) {
                return "0";
            }
        }
    }

    public static class ValueMax extends ValueComplex {

        @Override
        public boolean isValidSize() {
            return this.values.size() == 2 || this.values.size() == 4;
        }

        @Override
        public String getValue() {
            try {
                double arg0 = getDoubleValue(0);
                double arg1 = getDoubleValue(1);
                int shift = this.values.size() - 2;
                return arg0 > arg1 ? getValue(shift) : getValue(1 + shift);
            } catch (Exception e) {
                return "0";
            }
        }
    }

    public static class ValueItemQuantity extends ValueComplex {

        @Override
        public boolean isValidSize() {
            return this.values.size() == 1 || this.values.size() == 2;
        }

        @Override
        public String getValue() {
            try {
                Item item;
                try {
                    item = BuiltInRegistries.ITEM.get(new ResourceLocation(getValue(0)));
                } catch (Exception e3) {
                    item = Item.byId(getIntValue(0));
                }
                return String.valueOf(getItemCountInInventory(item));
            } catch (Exception e2) {
                return "0";
            }
        }
    }

    public static class ValueTranslate extends ValueComplex {

        @Override
        public boolean isValidSize() {
            return this.values.size() > 0;
        }

        @Override
        public String getValue() {
            try {
                String format = getValue(0);
                String[] args = new String[this.values.size() - 1];
                for (int i = 0; i < args.length; i++) {
                    args[i] = getValue(i + 1);
                }
                return Component.translatable(format, (Object[]) args).getString();
            } catch (Exception e) {
                return "?";
            }
        }
    }

    public static class ValueFormattedTime extends ValueComplex {

        @Override
        public boolean isValidSize() {
            return this.values.size() == 1;
        }

        @Override
        public String getValue() {
            try {
                String format = getValue(0);
                return new SimpleDateFormat(format).format(new Date());
            } catch (Exception e) {
                return "?";
            }
        }
    }

    public static class ValueFile extends ValueComplex {

        private static int ticks = 0;

        private final Map<String, String> cache = new HashMap<>();

        @Override
        public boolean isValidSize() {
            return this.values.size() == 1;
        }

        @Override
        public String getValue() {
            final String filename = getValue(0);

            if (ticks == 0) {
                if (this.cache.size() > 16) {
                    Reference.logger.trace("Clearing file cache...");
                    this.cache.clear();
                }

                final File file = new File(GameInfoCore.INSTANCE.getConfigDirectory(), filename);
                if (file.exists()) {
                    this.cache.put(filename, getLine(file));
                }
            }

            final String line = this.cache.get(filename);
            if (line != null) {
                return line;
            }

            return "";
        }

        private String getLine(File file) {
            try {
                final FileReader fileReader = new FileReader(file);
                final BufferedReader reader = new BufferedReader(fileReader);

                final String line = reader.readLine();

                reader.close();

                if (line.startsWith("﻿")) {
                    return line.substring(1);
                }

                return line;
            } catch (Exception e) {
                Reference.logger.error("", e);
            }

            return "";
        }

        public static void tick() {
            ticks = (ticks + 1) % (ConfigurationHandler.fileInterval * 20);
        }
    }

    public static class ValueIcon extends ValueComplex {

        private static int lastId;

        public ValueIcon() {
            this.setName("icon" + lastId++);
        }

        @Override
        public boolean isValidSize() {
            return (this.values.size() == 1 || this.values.size() == 2
                    || this.values.size() == 5
                    || this.values.size() == 7
                    || this.values.size() == 11);
        }

        @Override
        public String getValue() {
            try {
                int size = this.values.size();
                String what = getValue(0);

                Info value = parent.getAttachedValue(getName());
                if (value != null && value.getIdentifier().equals(what)) {
                    return value.getIconSpacing();
                }

                if ((size == 1 || size == 2) && !what.endsWith(".png")) {
                    Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(what));
                    ItemStack itemStack = item != null ? new ItemStack(item) : ItemStack.EMPTY;
                    if (itemStack.isEmpty()) {
                        Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(what));
                        itemStack = block != null ? new ItemStack(block) : ItemStack.EMPTY;
                    }

                    if (itemStack.isEmpty()) return "";

                    if (value == null) {
                        InfoItem item1 = new InfoItem(itemStack);
                        item1.setIdentifier(what);
                        parent.attachValue(getName(), item1);
                        return Tag.getIconTag(item1);
                    } else {
                        value.setIdentifier(what);
                        value.setValue(itemStack);
                        return Tag.getIconTag(value);
                    }
                }

                InfoIcon icon;
                if (value == null) {
                    icon = new InfoIcon(what);
                } else {
                    icon = (InfoIcon) value;
                }

                icon.setIdentifier(what);
                int index = 0;

                if (size == 5 || size == 11) {
                    int displayX = getIntValue(++index);
                    int displayY = getIntValue(++index);
                    int displayWidth = getIntValue(++index);
                    int displayHeight = getIntValue(++index);
                    icon.setDisplayDimensions(displayX, displayY, displayWidth, displayHeight);
                }

                if (size == 7 || size == 11) {
                    int iconX = getIntValue(++index);
                    int iconY = getIntValue(++index);
                    int iconWidth = getIntValue(++index);
                    int iconHeight = getIntValue(++index);
                    int textureWidth = getIntValue(++index);
                    int textureHeight = getIntValue(++index);
                    icon.setTextureData(iconX, iconY, iconWidth, iconHeight, textureWidth, textureHeight);
                }

                if (value == null) {
                    parent.attachValue(getName(), icon);
                    return Tag.getIconTag(icon);
                } else {
                    value.setValue(what);
                    return Tag.getIconTag(value);
                }
            } catch (Exception e) {
                GameInfoHud.LOGGER.error("countRendered failed", e);
                return "-1";
            }
        }
    }

    private static int getItemCountInInventory(Item item) {
        if (Minecraft.getInstance().player == null || item == null) {
            return 0;
        }
        Inventory inventory = Minecraft.getInstance().player.getInventory();
        int count = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static void register() {
        ValueRegistry.INSTANCE.register(new ValueOperation().setName("op").setAliases("operation"));
        ValueRegistry.INSTANCE.register(new ValueConcat().setName("concat"));
        ValueRegistry.INSTANCE.register(new ValueMax().setName("max").setAliases("maximum"));
        ValueRegistry.INSTANCE.register(new ValueMin().setName("min").setAliases("minimum"));
        ValueRegistry.INSTANCE.register(new ValueItemQuantity().setName("itemquantity"));
        ValueRegistry.INSTANCE.register(new ValueTranslate().setName("trans").setAliases("translate"));
        ValueRegistry.INSTANCE.register(new ValueFormattedTime().setName("formattedtime").setAliases("rltimef"));
        ValueRegistry.INSTANCE.register(new ValueIcon().setName("icon").setAliases("img", "image"));
        ValueRegistry.INSTANCE.register(new ValueFile().setName("file"));
    }
}
