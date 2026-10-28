package com.bookstore.gameinfohud.tag;

import java.util.Locale;

import com.bookstore.gameinfohud.tag.registry.TagRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LightLayer;

public abstract class TagPlayerGeneral extends Tag {

    @Override
    public String getCategory() {
        return "playergeneral";
    }

    private static BlockPos pos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    public static class Light extends TagPlayerGeneral {

        @Override
        public String getValue() {
            try {
                BlockPos p = pos(playerPosition.x, playerPosition.y, playerPosition.z);
                return String.valueOf(Math.min(15,
                        world.getBrightness(LightLayer.BLOCK, p) + world.getBrightness(LightLayer.SKY, p)));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    public static class LightFeet extends TagPlayerGeneral {

        @Override
        public String getValue() {
            try {
                BlockPos p = pos(playerPosition.x, (int) Math.round(player.getBoundingBox().minY), playerPosition.z);
                return String.valueOf(Math.min(15,
                        world.getBrightness(LightLayer.BLOCK, p) + world.getBrightness(LightLayer.SKY, p)));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    public static class LightNoSun extends TagPlayerGeneral {

        @Override
        public String getValue() {
            try {
                BlockPos p = pos(playerPosition.x, playerPosition.y, playerPosition.z);
                return String.valueOf(world.getBrightness(LightLayer.BLOCK, p));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    public static class LightNoSunFeet extends TagPlayerGeneral {

        @Override
        public String getValue() {
            try {
                BlockPos p = pos(playerPosition.x, (int) Math.round(player.getBoundingBox().minY), playerPosition.z);
                return String.valueOf(world.getBrightness(LightLayer.BLOCK, p));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    public static class LightSun extends TagPlayerGeneral {

        @Override
        public String getValue() {
            try {
                BlockPos p = pos(playerPosition.x, playerPosition.y, playerPosition.z);
                return String.valueOf(world.getBrightness(LightLayer.SKY, p));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    public static class LightSunFeet extends TagPlayerGeneral {

        @Override
        public String getValue() {
            try {
                BlockPos p = pos(playerPosition.x, (int) Math.round(player.getBoundingBox().minY), playerPosition.z);
                return String.valueOf(world.getBrightness(LightLayer.SKY, p));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    public static class Score extends TagPlayerGeneral {

        @Override
        public String getValue() {
            try {
                return String.valueOf(player.getScore());
            } catch (Exception e) {
                return "0";
            }
        }
    }

    public static class GameMode extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return Component.translatable("selectWorld.gameMode." + minecraft.gameMode.getPlayerMode().getName())
                    .getString();
        }
    }

    public static class GameModeId extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(minecraft.gameMode.getPlayerMode().getId());
        }
    }

    public static class Health extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.2f", player.getHealth());
        }
    }

    public static class MaxHealth extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.2f", player.getMaxHealth());
        }
    }

    public static class Armor extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.getArmorValue());
        }
    }

    public static class FoodLevel extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.getFoodData().getFoodLevel());
        }
    }

    public static class MaxFoodLevel extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(20);
        }
    }

    public static class Saturation extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.2f", player.getFoodData().getSaturationLevel());
        }
    }

    public static class Exhaustion extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.2f", player.getFoodData().getExhaustionLevel());
        }
    }

    public static class AirTicks extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.getAirSupply());
        }
    }

    public static class MaxAirTicks extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.getMaxAirSupply());
        }
    }

    public static class PlayerLevel extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.experienceLevel);
        }
    }

    public static class CurrentExperience extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf((int) Math.ceil(player.experienceProgress * player.getXpNeededForNextLevel()));
        }
    }

    public static class ExperienceUntilNext extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf((int) Math.floor((1.0 - player.experienceProgress) * player.getXpNeededForNextLevel()));
        }
    }

    public static class ExperienceCap extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.getXpNeededForNextLevel());
        }
    }

    public static class Username extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return player.getGameProfile().getName();
        }
    }

    public static class InWater extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.isInWater());
        }
    }

    public static class Wet extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.isInWaterRainOrBubble());
        }
    }

    public static class Alive extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.isAlive());
        }
    }

    public static class Burning extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.isOnFire());
        }
    }

    public static class Riding extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.isPassenger());
        }
    }

    public static class Sneaking extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.isShiftKeyDown());
        }
    }

    public static class Sprinting extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.isSprinting());
        }
    }

    public static class Invisible extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.isInvisible());
        }
    }

    public static class Eating extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.isUsingItem());
        }
    }

    public static class Invulnerable extends TagPlayerGeneral {

        @Override
        public String getValue() {
            return String.valueOf(player.isInvulnerable());
        }
    }

    public static class LightLabel extends TagPlayerGeneral {
        @Override
        public String getValue() {
            String k = "gameinfohud.light.label";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Light" : t;
        }
    }

    public static class SkyLabel extends TagPlayerGeneral {
        @Override
        public String getValue() {
            String k = "gameinfohud.light.sky";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Sky" : t;
        }
    }

    public static class BlockLabel extends TagPlayerGeneral {
        @Override
        public String getValue() {
            String k = "gameinfohud.light.block";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Block" : t;
        }
    }

    public static class FeetLabel extends TagPlayerGeneral {
        @Override
        public String getValue() {
            String k = "gameinfohud.light.feet";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Feet" : t;
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new Light().setName("light"));
        TagRegistry.INSTANCE.register(new LightFeet().setName("lightfeet"));
        TagRegistry.INSTANCE.register(new LightNoSun().setName("lightnosun"));
        TagRegistry.INSTANCE.register(new LightNoSunFeet().setName("lightnosunfeet"));
        TagRegistry.INSTANCE.register(new LightSun().setName("lightsun"));
        TagRegistry.INSTANCE.register(new LightSunFeet().setName("lightsunfeet"));
        TagRegistry.INSTANCE.register(new LightLabel().setName("lightlabel"));
        TagRegistry.INSTANCE.register(new SkyLabel().setName("skylabel"));
        TagRegistry.INSTANCE.register(new BlockLabel().setName("blocklabel"));
        TagRegistry.INSTANCE.register(new FeetLabel().setName("feetlabel"));
        TagRegistry.INSTANCE.register(new Score().setName("score"));
        TagRegistry.INSTANCE.register(new GameMode().setName("gamemode"));
        TagRegistry.INSTANCE.register(new GameModeId().setName("gamemodeid"));
        TagRegistry.INSTANCE.register(new Health().setName("health").setAliases("healthpoints"));
        TagRegistry.INSTANCE.register(new MaxHealth().setName("maxhealth").setAliases("maxhealthpoints"));
        TagRegistry.INSTANCE.register(new Armor().setName("armor").setAliases("armorpoints"));
        TagRegistry.INSTANCE.register(new FoodLevel().setName("foodlevel").setAliases("foodpoints"));
        TagRegistry.INSTANCE.register(new MaxFoodLevel().setName("maxfoodlevel").setAliases("maxfoodpoints"));
        TagRegistry.INSTANCE.register(new Saturation().setName("saturation").setAliases("foodsaturation"));
        TagRegistry.INSTANCE.register(new Exhaustion().setName("exhaustion").setAliases("foodexhaustion"));
        TagRegistry.INSTANCE.register(new AirTicks().setName("airticks"));
        TagRegistry.INSTANCE.register(new MaxAirTicks().setName("maxairticks"));
        TagRegistry.INSTANCE.register(new PlayerLevel().setName("playerlevel"));
        TagRegistry.INSTANCE.register(new CurrentExperience().setName("xpthislevel"));
        TagRegistry.INSTANCE.register(new ExperienceUntilNext().setName("xpuntilnext"));
        TagRegistry.INSTANCE.register(new ExperienceCap().setName("xpcap"));
        TagRegistry.INSTANCE.register(new Username().setName("username"));
        TagRegistry.INSTANCE.register(new InWater().setName("inwater").setAliases("underwater"));
        TagRegistry.INSTANCE.register(new Wet().setName("wet"));
        TagRegistry.INSTANCE.register(new Alive().setName("alive"));
        TagRegistry.INSTANCE.register(new Burning().setName("burning"));
        TagRegistry.INSTANCE.register(new Riding().setName("riding"));
        TagRegistry.INSTANCE.register(new Sneaking().setName("sneaking"));
        TagRegistry.INSTANCE.register(new Sprinting().setName("sprinting"));
        TagRegistry.INSTANCE.register(new Invisible().setName("invisible"));
        TagRegistry.INSTANCE.register(new Eating().setName("eating"));
        TagRegistry.INSTANCE.register(new Invulnerable().setName("invulnerable"));
    }
}
