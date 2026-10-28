package com.bookstore.gameinfohud.tag;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.bookstore.gameinfohud.reference.Reference;
import com.bookstore.gameinfohud.tag.registry.TagRegistry;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;

public abstract class TagRiding extends Tag {

    private static final int TICKS = 20;
    private static final double CONSTANT = 2.15858575199013618; // playerSpeed / internalPlayerSpeed (0.1)

    @Override
    public String getCategory() {
        return "riding";
    }

    public static class IsHorse extends TagRiding {

        @Override
        public String getValue() {
            return String.valueOf(player.getVehicle() instanceof Horse);
        }
    }

    public static class HorseHealth extends TagRiding {

        @Override
        public String getValue() {
            if (player.getVehicle() instanceof Horse horse) {
                return String.valueOf(horse.getHealth());
            }
            return "-1";
        }
    }

    public static class HorseMaxHealth extends TagRiding {

        @Override
        public String getValue() {
            if (player.getVehicle() instanceof Horse horse) {
                return String.valueOf(horse.getMaxHealth());
            }
            return "-1";
        }
    }

    public static class HorseSpeed extends TagRiding {

        @Override
        public String getValue() {
            if (player.getVehicle() instanceof Horse horse) {
                return String.format(
                        Locale.ENGLISH,
                        "%.3f",
                        TICKS * CONSTANT * horse.getAttributeValue(Attributes.MOVEMENT_SPEED));
            }
            return "-1";
        }
    }

    public static class HorseJump extends TagRiding {

        private final Map<Double, Double> jumpHeightCache = new HashMap<>();

        private double getJumpHeight(final Horse horse) {
            final double jumpStrength = horse.getCustomJump();

            final Double height = this.jumpHeightCache.get(jumpStrength);
            if (height != null) {
                return height;
            }

            double jumpHeight = 0;
            double velocity = jumpStrength;
            while (velocity > 0) {
                jumpHeight += velocity;
                velocity -= 0.08;
                velocity *= 0.98;
            }

            if (this.jumpHeightCache.size() > 16) {
                Reference.logger.trace("Clearing horse jump height cache.");
                this.jumpHeightCache.clear();
            }

            this.jumpHeightCache.put(jumpStrength, jumpHeight);
            return jumpHeight;
        }

        @Override
        public String getValue() {
            if (player.getVehicle() instanceof Horse horse) {
                return String.format(Locale.ENGLISH, "%.3f", getJumpHeight(horse));
            }
            return "-1";
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new IsHorse().setName("ridinghorse"));
        TagRegistry.INSTANCE.register(new HorseHealth().setName("horsehealth"));
        TagRegistry.INSTANCE.register(new HorseMaxHealth().setName("horsemaxhealth"));
        TagRegistry.INSTANCE.register(new HorseSpeed().setName("horsespeed"));
        TagRegistry.INSTANCE.register(new HorseJump().setName("horsejumpstrength").setAliases("horsejumpstr"));
    }
}
