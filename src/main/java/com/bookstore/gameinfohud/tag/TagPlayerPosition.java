package com.bookstore.gameinfohud.tag;

import java.util.Locale;

import com.bookstore.gameinfohud.tag.registry.TagRegistry;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public abstract class TagPlayerPosition extends Tag {

    private static final int TICKS = 20;

    protected static final String[] ROUGHDIRECTION = { "South", "West", "North", "East" };
    protected static final String[] FINEDIRECTION = { "South", "South West", "West", "North West", "North",
            "North East", "East", "South East" };
    protected static final String[] ABRROUGHDIRECTION = { "S", "W", "N", "E" };
    protected static final String[] ABRFINEDIRECTION = { "S", "SW", "W", "NW", "N", "NE", "E", "SE" };

    private static final String[] ROUGHDIRECTION_KEYS = { "south", "west", "north", "east" };
    private static final String[] FINEDIRECTION_KEYS = { "south", "southwest", "west", "northwest", "north",
            "northeast", "east", "southeast" };
    private static final String[] ABRROUGHDIRECTION_KEYS = { "s", "w", "n", "e" };
    private static final String[] ABRFINEDIRECTION_KEYS = { "s", "sw", "w", "nw", "n", "ne", "e", "se" };

    private static String translateDirection(String key, String fallback) {
        String translated = Component.translatable(key).getString();
        return translated.equals(key) ? fallback : translated;
    }

    private static String abbrFine(int idx) {
        return translateDirection("gameinfohud.direction.short." + ABRFINEDIRECTION_KEYS[idx],
                ABRFINEDIRECTION[idx]);
    }

    @Override
    public String getCategory() {
        return "playerposition";
    }

    public static class ChunkX extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.valueOf(playerPosition.x >> 4);
        }
    }

    public static class ChunkZ extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.valueOf(playerPosition.z >> 4);
        }
    }

    public static class ChunkOffsetX extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.valueOf(playerPosition.x & 0x0F);
        }
    }

    public static class ChunkOffsetZ extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.valueOf(playerPosition.z & 0x0F);
        }
    }

    public static class X extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.2f", player.getX());
        }
    }

    public static class Y extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.2f", player.getY());
        }
    }

    public static class YFeet extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.2f", player.getBoundingBox().minY);
        }
    }

    public static class Z extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.2f", player.getZ());
        }
    }

    public static class Xi extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.valueOf(playerPosition.x);
        }
    }

    public static class Yi extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.valueOf(playerPosition.y);
        }
    }

    public static class YFeeti extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.valueOf((int) Math.floor(player.getBoundingBox().minY));
        }
    }

    public static class Zi extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.valueOf(playerPosition.z);
        }
    }

    public static class Speed extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.format(
                    Locale.ENGLISH,
                    "%.3f",
                    TICKS * Math.sqrt(
                            playerMotion.x * playerMotion.x + playerMotion.y * playerMotion.y
                                    + playerMotion.z * playerMotion.z));
        }
    }

    public static class SpeedX extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.3f", TICKS * Math.abs(playerMotion.x));
        }
    }

    public static class SpeedY extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.3f", TICKS * Math.abs(playerMotion.y));
        }
    }

    public static class SpeedZ extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.3f", TICKS * Math.abs(playerMotion.z));
        }
    }

    public static class SpeedXZ extends TagPlayerPosition {

        @Override
        public String getValue() {
            return String.format(
                    Locale.ENGLISH,
                    "%.3f",
                    TICKS * Math.sqrt(playerMotion.x * playerMotion.x + playerMotion.z * playerMotion.z));
        }
    }

    public static class Direction extends TagPlayerPosition {

        @Override
        public String getValue() {
            float direction = player.getYRot() % 360;
            if (direction >= 180) {
                direction -= 360;
            } else if (direction < -180) {
                direction += 360;
            }
            return String.format(Locale.ENGLISH, "%.2f", direction);
        }
    }

    public static class RoughDirection extends TagPlayerPosition {
        @Override
        public String getValue() {
            int idx = Mth.floor(player.getYRot() * 4.0 / 360.0 + 0.5) & 3;
            return translateDirection("gameinfohud.direction." + ROUGHDIRECTION_KEYS[idx], ROUGHDIRECTION[idx]);
        }
    }

    public static class FineDirection extends TagPlayerPosition {

        @Override
        public String getValue() {
            int idx = Mth.floor(player.getYRot() * 8.0 / 360.0 + 0.5) & 7;
            return translateDirection("gameinfohud.direction." + FINEDIRECTION_KEYS[idx], FINEDIRECTION[idx]);
        }
    }

    public static class AbbreviatedRoughDirection extends TagPlayerPosition {

        @Override
        public String getValue() {
            int idx = Mth.floor(player.getYRot() * 4.0 / 360.0 + 0.5) & 3;
            return translateDirection("gameinfohud.direction.short." + ABRROUGHDIRECTION_KEYS[idx],
                    ABRROUGHDIRECTION[idx]);
        }
    }

    public static class AbbreviatedFineDirection extends TagPlayerPosition {

        @Override
        public String getValue() {
            int idx = Mth.floor(player.getYRot() * 8.0 / 360.0 + 0.5) & 7;
            return abbrFine(idx);
        }
    }

    public static class DirectionHud extends TagPlayerPosition {

        @Override
        public String getValue() {
            int direction = Mth.floor(player.getYRot() * 16.0f / 360.0f + 0.5) & 15;
            int base = direction / 2;
            int len = ABRFINEDIRECTION.length;
            String left = abbrFine((base + len - 1) % len);
            String center = abbrFine((base + len) % len);
            String right = abbrFine((base + len + 1) % len);
            if (direction % 2 == 0) {
                return String.format("§r%s   §c%s§r   %s", left, center, right);
            }
            return String.format("§r   %2s   %2s   ", center, right);
        }
    }

    public static class ChunkLabel extends TagPlayerPosition {
        @Override
        public String getValue() {
            String k = "gameinfohud.chunk.label";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Chunk" : t;
        }
    }

    public static class OffsetLabel extends TagPlayerPosition {
        @Override
        public String getValue() {
            String k = "gameinfohud.chunk.label.offset";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Offset" : t;
        }
    }

    public static class FacingLabel extends TagPlayerPosition {
        @Override
        public String getValue() {
            String k = "gameinfohud.facing.label";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Facing" : t;
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new ChunkX().setName("chunkx"));
        TagRegistry.INSTANCE.register(new ChunkZ().setName("chunkz"));
        TagRegistry.INSTANCE.register(new ChunkOffsetX().setName("chunkoffsetx"));
        TagRegistry.INSTANCE.register(new ChunkOffsetZ().setName("chunkoffsetz"));
        TagRegistry.INSTANCE.register(new X().setName("x").setAliases("xr"));
        TagRegistry.INSTANCE.register(new Y().setName("y").setAliases("yr"));
        TagRegistry.INSTANCE.register(new YFeet().setName("yfeet").setAliases("yfeetr"));
        TagRegistry.INSTANCE.register(new Z().setName("z").setAliases("zr"));
        TagRegistry.INSTANCE.register(new Xi().setName("xi"));
        TagRegistry.INSTANCE.register(new Yi().setName("yi"));
        TagRegistry.INSTANCE.register(new YFeeti().setName("yfeeti"));
        TagRegistry.INSTANCE.register(new Zi().setName("zi"));
        TagRegistry.INSTANCE.register(new Speed().setName("speed"));
        TagRegistry.INSTANCE.register(new SpeedX().setName("speedx"));
        TagRegistry.INSTANCE.register(new SpeedY().setName("speedy"));
        TagRegistry.INSTANCE.register(new SpeedZ().setName("speedz"));
        TagRegistry.INSTANCE.register(new SpeedXZ().setName("speedxz"));
        TagRegistry.INSTANCE.register(new Direction().setName("direction"));
        TagRegistry.INSTANCE.register(new RoughDirection().setName("roughdirection"));
        TagRegistry.INSTANCE.register(new FineDirection().setName("finedirection"));
        TagRegistry.INSTANCE.register(new AbbreviatedRoughDirection().setName("abrroughdirection"));
        TagRegistry.INSTANCE.register(new AbbreviatedFineDirection().setName("abrfinedirection"));
        TagRegistry.INSTANCE.register(new DirectionHud().setName("directionhud"));
        TagRegistry.INSTANCE.register(new ChunkLabel().setName("chunklabel"));
        TagRegistry.INSTANCE.register(new OffsetLabel().setName("offsetlabel"));
        TagRegistry.INSTANCE.register(new FacingLabel().setName("facinglabel"));
    }
}
