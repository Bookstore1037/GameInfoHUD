package com.bookstore.gameinfohud.tag;

import java.util.Locale;

import com.bookstore.gameinfohud.network.ClientWorldData;
import com.bookstore.gameinfohud.tag.registry.TagRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public abstract class TagWorld extends Tag {
    @Override
    public String getCategory() {
        return "world";
    }

    private static BlockPos pos() {
        return new BlockPos(playerPosition.x, playerPosition.y, playerPosition.z);
    }

    private static Biome biome() {
        return world.getBiome(pos()).value();
    }

    private static String biomeKey() {
        return world.getBiome(pos()).unwrapKey()
                .map(key -> key.location().toString())
                .orElse("unknown");
    }

    private static boolean isSlimeChunk(int chunkX, int chunkZ) {
        long seed = ClientWorldData.getSeed();          // 改：从缓存读
        RandomSource random = RandomSource.create(seed); // 改：WorldgenRandom.create 不存在
        long i = random.nextLong();
        long j = random.nextLong();
        long k = (long) chunkX * i;
        long l = (long) chunkZ * j;
        random.setSeed(k ^ l ^ seed);
        random.nextInt(10);
        return random.nextInt(10) == 0;
    }

    public static class Name extends TagWorld {
        @Override
        public String getValue() {
            return world.dimension().location().toString();
        }
    }

    public static class Size extends TagWorld {

        @Override
        public String getValue() {
            return "0";
        }
    }

    public static class SizeMB extends TagWorld {

        @Override
        public String getValue() {
            return "0.0";
        }
    }

    public static class Seed extends TagWorld {
        @Override
        public String getValue() {
            return String.valueOf(ClientWorldData.getSeed());
        }
    }

    public static class Difficulty extends TagWorld {

        @Override
        public String getValue() {
            return world.getDifficulty().getDisplayName().getString();
        }
    }

    public static class DifficultyId extends TagWorld {

        @Override
        public String getValue() {
            return String.valueOf(world.getDifficulty().getId());
        }
    }

    public static class Dimension extends TagWorld {
        @Override
        public String getValue() {
            ResourceLocation loc = world.dimension().location();
            String path = loc.getPath();
            String k = "gameinfohud.dimension." + path;
            String t = Component.translatable(k).getString();
            return t.equals(k) ? loc.toString() : t;
        }
    }

    public static class ChunksLabel extends TagWorld {
        @Override
        public String getValue() {
            String k = "gameinfohud.chunks.label";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Loaded Chunks" : t;
        }
    }

    public static class WorldLabel extends TagWorld {
        @Override
        public String getValue() {
            String k = "gameinfohud.world.label";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "World" : t;
        }
    }

    public static class DimensionId extends TagWorld {

        @Override
        public String getValue() {
            return world.dimension().location().toString();
        }
    }

    public static class BiomeName extends TagWorld {
        @Override
        public String getValue() {
            var loc = world.getBiome(pos()).unwrapKey()
                    .map(ResourceKey::location)
                    .orElse(null);
            if (loc == null) return "unknown";
            String key = "biome." + loc.getNamespace() + "." + loc.getPath();
            String t = Component.translatable(key).getString();
            return t.equals(key) ? loc.toString() : t;
        }
    }

    public static class BiomeLabel extends TagWorld {
        @Override
        public String getValue() {
            String k = "gameinfohud.biome.label";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Biome" : t;
        }
    }

    public static class BiomeId extends TagWorld {

        @Override
        public String getValue() {
            return biomeKey();
        }
    }

    public static class Daytime extends TagWorld {
        @Override
        public String getValue() {
            long time = world.getDayTime() % 24000;
            boolean day = time < 12000 || time >= 23850;
            String k = day ? "gameinfohud.time.daynight.day" : "gameinfohud.time.daynight.night";
            String t = Component.translatable(k).getString();
            if (t.equals(k)) return day ? "Day" : "Night";
            String color = day ? "§e" : "§8";
            return color + t + "§r";
        }
    }



    public static class Raining extends TagWorld {
        @Override
        public String getValue() {
            if (!world.isRaining()) return "";
            boolean warm = biome().warmEnoughToRain(pos());
            if (!warm) return "";
            String k = "gameinfohud.weather.rain";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Rain" : t;
        }
    }


    public static class Thundering extends TagWorld {
        @Override
        public String getValue() {
            if (!world.isThundering()) return "";
            String k = "gameinfohud.weather.thunder";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Thunder" : t;
        }
    }

    public static class Snowing extends TagWorld {
        @Override
        public String getValue() {
            if (!world.isRaining()) return "";
            boolean cold = biome().coldEnoughToSnow(pos());
            if (!cold) return "";
            String k = "gameinfohud.weather.snow";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Snow" : t;
        }
    }

    public static class Weather extends TagWorld {
        @Override
        public String getValue() {
            String key;
            if (world.isThundering()) {
                key = "thunder";
            } else if (world.isRaining() && biome().coldEnoughToSnow(pos())) {
                key = "snow";
            } else if (world.isRaining() && biome().warmEnoughToRain(pos())) {
                key = "rain";
            } else {
                key = "clear";
            }
            String k = "gameinfohud.weather." + key;
            String t = Component.translatable(k).getString();
            return t.equals(k) ? key : t;
        }
    }

    public static class WeatherLabel extends TagWorld {
        @Override
        public String getValue() {
            String k = "gameinfohud.weather.label";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Weather" : t;
        }
    }

    public static class Slimes extends TagWorld {

        @Override
        public String getValue() {
            boolean slimeChunk = isSlimeChunk(playerPosition.x >> 4, playerPosition.z >> 4);
            boolean swamp = world.getBiome(pos()).is(Biomes.SWAMP);
            return String.valueOf(slimeChunk || swamp);
        }
    }

    public static class Hardcore extends TagWorld {

        @Override
        public String getValue() {
            return String.valueOf(world.getLevelData().isHardcore());
        }
    }

    public static class TemperatureHumidity extends TagWorld {
        @Override
        public String getValue() {
            double t = biome().getBaseTemperature();
            double c = (t - 0.3) * 30;
            int humidity = (int) Math.round(biome().getModifiedClimateSettings().downfall() * 100);
            String k = "gameinfohud.temp_humidity";
            String cStr = "§a" + String.format("%.0f", c) + "§r";
            String hStr = "§a" + humidity + "§r";
            String formatted = Component.translatable(k, cStr, hStr).getString();
            return formatted.equals(k) ? String.format("%.0f℃ %d%%", c, humidity) : formatted;
        }
    }

    public static class Humidity extends TagWorld {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.0f", biome().getModifiedClimateSettings().downfall() * 100);
        }
    }

    public static class MoonPhase extends TagWorld {
        private static final String[] KEYS = {
                "full_moon",       // 0
                "waning_gibbous",  // 1
                "third_quarter",   // 2
                "waning_crescent", // 3
                "new_moon",        // 4
                "waxing_crescent", // 5
                "first_quarter",   // 6
                "waxing_gibbous"   // 7
        };

        @Override
        public String getValue() {
            int phase = world.getMoonPhase() & 7;
            String k = "gameinfohud.moonphase." + KEYS[phase];
            String t = Component.translatable(k).getString();
            return t.equals(k) ? KEYS[phase] : t;
        }
    }

    public static class MoonPhaseLabel extends TagWorld {
        @Override
        public String getValue() {
            String k = "gameinfohud.moonphase.label";
            String t = Component.translatable(k).getString();
            return t.equals(k) ? "Moon" : t;
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new Name().setName("worldname"));
        TagRegistry.INSTANCE.register(new Size().setName("worldsize"));
        TagRegistry.INSTANCE.register(new SizeMB().setName("worldsizemb"));
        TagRegistry.INSTANCE.register(new Seed().setName("seed"));
        TagRegistry.INSTANCE.register(new Difficulty().setName("difficulty"));
        TagRegistry.INSTANCE.register(new DifficultyId().setName("difficultyid"));
        TagRegistry.INSTANCE.register(new ChunksLabel().setName("chunkslabel"));
        TagRegistry.INSTANCE.register(new WorldLabel().setName("worldlabel"));
        TagRegistry.INSTANCE.register(new Dimension().setName("dimension"));
        TagRegistry.INSTANCE.register(new DimensionId().setName("dimensionid"));
        TagRegistry.INSTANCE.register(new BiomeName().setName("biome"));
        TagRegistry.INSTANCE.register(new BiomeId().setName("biomeid"));
        TagRegistry.INSTANCE.register(new BiomeLabel().setName("biomelabel"));
        TagRegistry.INSTANCE.register(new Daytime().setName("daytime"));
        TagRegistry.INSTANCE.register(new Raining().setName("raining"));
        TagRegistry.INSTANCE.register(new Thundering().setName("thundering"));
        TagRegistry.INSTANCE.register(new Snowing().setName("snowing"));
        TagRegistry.INSTANCE.register(new Weather().setName("weather"));
        TagRegistry.INSTANCE.register(new WeatherLabel().setName("weatherlabel"));
        TagRegistry.INSTANCE.register(new Slimes().setName("slimes"));
        TagRegistry.INSTANCE.register(new Hardcore().setName("hardcore"));
        TagRegistry.INSTANCE.register(new TemperatureHumidity().setName("temphumidity"));
        TagRegistry.INSTANCE.register(new Humidity().setName("humidity"));
        TagRegistry.INSTANCE.register(new MoonPhase().setName("moonphase"));
        TagRegistry.INSTANCE.register(new MoonPhaseLabel().setName("moonphaselabel"));
    }
}
