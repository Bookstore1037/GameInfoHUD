package com.bookstore.gameinfohud.tag;

import com.bookstore.gameinfohud.client.gui.Info;
import com.bookstore.gameinfohud.client.gui.InfoItem;
import com.bookstore.gameinfohud.client.gui.InfoText;
import com.bookstore.gameinfohud.tag.registry.TagRegistry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.NotNull;

public abstract class TagPlayerEquipment extends Tag {

    public static final String[] TYPES = new String[] { "equipped", "helmet", "chestplate", "leggings", "boots" };
    public static final int[] SLOTS = new int[] { -1, 3, 2, 1, 0 };
    protected final int slot;

    public TagPlayerEquipment(int slot) {
        this.slot = slot;
    }

    @Override
    public String getCategory() {
        return "playerequipment";
    }

    protected ItemStack getItemStack(int slot) {
        if (slot == -1) {
            return player.getMainHandItem();
        }
        return player.getInventory().armor.get(slot);
    }

    private static int getItemCount(Item item) {
        int count = 0;
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static class Name extends TagPlayerEquipment {

        public Name(int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            ItemStack itemStack = getItemStack(this.slot);
            String arrows = itemStack != null && itemStack.is(Items.BOW)
                    ? " (" + getItemCount(Items.ARROW) + ")"
                    : "";
            return itemStack != null ? itemStack.getDisplayName().getString() + arrows : "";
        }
    }

    public static class UniqueName extends TagPlayerEquipment {

        public UniqueName(int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            ItemStack itemStack = getItemStack(this.slot);
            Item item = itemStack != null ? itemStack.getItem() : null;
            return item != null ? String.valueOf(BuiltInRegistries.ITEM.getKey(item)) : "";
        }
    }

    public static class Damage extends TagPlayerEquipment {

        public Damage(int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            ItemStack itemStack = getItemStack(this.slot);
            return String.valueOf(
                    itemStack != null && itemStack.isDamageableItem() ? itemStack.getDamageValue() : 0);
        }
    }

    public static class MaximumDamage extends TagPlayerEquipment {

        public MaximumDamage(int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            ItemStack itemStack = getItemStack(this.slot);
            return String.valueOf(itemStack != null && itemStack.isDamageableItem() ? itemStack.getMaxDamage() + 1 : 0);
        }
    }

    public static class DamageLeft extends TagPlayerEquipment {

        public DamageLeft(int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            ItemStack itemStack = getItemStack(this.slot);
            return String.valueOf(
                    itemStack != null && itemStack.isDamageableItem()
                            ? itemStack.getMaxDamage() + 1 - itemStack.getDamageValue()
                            : 0);
        }
    }

    public static class ItemMeta extends TagPlayerEquipment {

        public ItemMeta(int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            ItemStack itemStack = getItemStack(this.slot);
            return itemStack != null ? String.valueOf(itemStack.getDamageValue()) : "";
        }
    }

    public static class ItemId extends TagPlayerEquipment {

        public ItemId(int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            ItemStack itemStack = getItemStack(this.slot);
            Item item = itemStack != null ? itemStack.getItem() : null;
            return item != null ? String.valueOf(BuiltInRegistries.ITEM.getId(item)) : "0";
        }
    }

    public static class Quantity extends TagPlayerEquipment {

        public Quantity(int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            ItemStack itemStack = getItemStack(this.slot);
            return String.valueOf(itemStack != null ? getItemCount(itemStack.getItem()) : 0);
        }
    }

    public static class Icon extends TagPlayerEquipment {

        private final boolean large;

        public Icon(int slot, boolean large) {
            super(slot);
            this.large = large;
        }

        @Override
        public @NotNull String getValue(@NotNull InfoText caller) {
            ItemStack itemStack = getItemStack(slot);
            if (itemStack == null) {
                caller.removeAttachedValue(getName());
                return "";
            }

            Info value = caller.getAttachedValue(getName());

            if (value != null) {
                value.setValue(itemStack);
                return getIconTag(value);
            }

            InfoItem item = new InfoItem(itemStack, this.large);
            item.setIdentifier(itemStack.getDisplayName().getString());
            caller.attachValue(getName(), item);
            return getIconTag(item);
        }

        @Override
        public String getValue() {
            return "";
        }
    }

    public static void register() {
        for (int i = 0; i < TYPES.length; i++) {
            TagRegistry.INSTANCE.register(new Name(SLOTS[i]).setName(TYPES[i] + "name"));
            TagRegistry.INSTANCE.register(new UniqueName(SLOTS[i]).setName(TYPES[i] + "uniquename"));
            TagRegistry.INSTANCE.register(new Damage(SLOTS[i]).setName(TYPES[i] + "damage"));
            TagRegistry.INSTANCE.register(new MaximumDamage(SLOTS[i]).setName(TYPES[i] + "maxdamage"));
            TagRegistry.INSTANCE.register(new DamageLeft(SLOTS[i]).setName(TYPES[i] + "damageleft"));
            TagRegistry.INSTANCE.register(new ItemMeta(SLOTS[i]).setName(TYPES[i] + "itemmeta"));
            TagRegistry.INSTANCE.register(new ItemId(SLOTS[i]).setName(TYPES[i] + "itemid"));
            TagRegistry.INSTANCE.register(new Quantity(SLOTS[i]).setName(TYPES[i] + "quantity"));
            TagRegistry.INSTANCE.register(new Icon(SLOTS[i], false).setName(TYPES[i] + "icon"));
            TagRegistry.INSTANCE.register(new Icon(SLOTS[i], true).setName(TYPES[i] + "largeicon"));
        }
    }
}
