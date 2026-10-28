package com.bookstore.gameinfohud.tag;

import java.util.Collection;

import com.bookstore.gameinfohud.tag.registry.TagRegistry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

public abstract class TagPlayerPotion extends Tag {

    public static final int MAXIMUM_INDEX = 32;

    protected static MobEffectInstance[] potionEffects = null;
    protected final int index;

    public TagPlayerPotion(int index) {
        this.index = index;
    }

    @Override
    public String getName() {
        return super.getName() + this.index;
    }

    @Override
    public String[] getAliases() {
        String[] aliases = super.getAliases();
        String[] aliasesIndexed = new String[aliases.length];
        for (int i = 0; i < aliases.length; i++) {
            aliasesIndexed[i] = aliases[i] + this.index;
        }
        return aliasesIndexed;
    }

    @Override
    public String getCategory() {
        return "playerpotion";
    }

    protected void updatePotionEffects() {
        if (potionEffects == null) {
            Collection<MobEffectInstance> collection = player.getActiveEffects();
            potionEffects = collection.toArray(new MobEffectInstance[0]);
        }
    }

    public static class Effect extends TagPlayerPotion {

        public Effect(int index) {
            super(index);
        }

        @Override
        public String getValue() {
            updatePotionEffects();
            if (potionEffects.length > this.index) {
                MobEffectInstance effect = potionEffects[this.index];
                String str = Component.translatable(effect.getEffect().getDescriptionId()).getString();
                switch (effect.getAmplifier()) {
                    case 1:
                        str += " II";
                        break;
                    case 2:
                        str += " III";
                        break;
                    case 3:
                        str += " IV";
                        break;
                    default:
                        break;
                }
                return str;
            }
            return "";
        }
    }

    public static class Duration extends TagPlayerPotion {

        public Duration(int index) {
            super(index);
        }

        @Override
        public String getValue() {
            updatePotionEffects();
            if (potionEffects.length > this.index) {
                return MobEffectUtil.formatDuration(potionEffects[this.index], 1.0F).getString();
            }
            return "0:00";
        }
    }

    public static class DurationTicks extends TagPlayerPotion {

        public DurationTicks(int index) {
            super(index);
        }

        @Override
        public String getValue() {
            updatePotionEffects();
            if (potionEffects.length > this.index) {
                return String.valueOf(potionEffects[this.index].getDuration());
            }
            return "0";
        }
    }

    public static class Negative extends TagPlayerPotion {

        public Negative(int index) {
            super(index);
        }

        @Override
        public String getValue() {
            updatePotionEffects();
            if (potionEffects.length > this.index) {
                return String.valueOf(!potionEffects[this.index].getEffect().isBeneficial());
            }
            return "false";
        }
    }

    public static class Icon extends TagPlayerPotion {

        public Icon(int index, boolean large) {
            super(index);
        }

        @Override
        public String getValue() {
            return "";
        }
    }

    public static void register() {
        for (int i = 0; i < MAXIMUM_INDEX; i++) {
            TagRegistry.INSTANCE.register(new Effect(i).setName("potioneffect"));
            TagRegistry.INSTANCE.register(new Duration(i).setName("potionduration"));
            TagRegistry.INSTANCE.register(new DurationTicks(i).setName("potiondurationticks"));
            TagRegistry.INSTANCE.register(new Negative(i).setName("potionnegative"));
            TagRegistry.INSTANCE.register(new Icon(i, false).setName("potionicon"));
            TagRegistry.INSTANCE.register(new Icon(i, true).setName("potionlargeicon"));
        }
    }

    public static void releaseResources() {
        potionEffects = null;
    }
}
