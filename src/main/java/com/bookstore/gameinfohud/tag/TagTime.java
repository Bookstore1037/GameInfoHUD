package com.bookstore.gameinfohud.tag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.bookstore.gameinfohud.tag.registry.TagRegistry;
import net.minecraft.network.chat.Component;

public abstract class TagTime extends Tag {

    @Override
    public String getCategory() {
        return "time";
    }

    public static class MinecraftTotal extends TagTime {

        @Override
        public String getValue() {
            long seconds = world.getGameTime() / 20;
            long dd = seconds / 86400;
            long hh = (seconds / 3600) % 24;
            long mm = (seconds / 60) % 60;
            long ss = seconds % 60;

            if (dd >= 1) {
                return String.format("%dd %02d:%02d:%02d", dd, hh, mm, ss);
            } else {
                return String.format("%02d:%02d:%02d", hh, mm, ss);
            }
        }
    }

    public static class MinecraftDay extends TagTime {

        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%d", world.getDayTime() / 24000);
        }
    }

    public static class MinecraftHour extends TagTime {

        @Override
        public String getValue() {
            long hour = (world.getDayTime() / 1000 + 6) % 24;
            return String.format(Locale.ENGLISH, "%02d", hour);
        }
    }

    public static class MinecraftMinute extends TagTime {

        @Override
        public String getValue() {
            long minute = (world.getDayTime() % 1000) * 60 / 1000;
            return String.format(Locale.ENGLISH, "%02d", minute);
        }
    }

    public static class Minecraft24 extends TagTime {

        @Override
        public String getValue() {
            long time = world.getDayTime();
            long hour = (time / 1000 + 6) % 24;
            long minute = (time % 1000) * 60 / 1000;
            return String.format(Locale.ENGLISH, "%02d:%02d", hour, minute);
        }
    }

    public static class Minecraft12 extends TagTime {

        @Override
        public String getValue() {
            long time = world.getDayTime();
            long hour = (time / 1000 + 6) % 24;
            long minute = (time % 1000) * 60 / 1000;
            String ampm = "AM";
            if (hour >= 12) {
                hour -= 12;
                ampm = "PM";
            }
            if (hour == 0) {
                hour += 12;
            }
            return String.format(Locale.ENGLISH, "%02d:%02d %s", hour, minute, ampm);
        }
    }

    public static class Real24 extends TagTime {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        @Override
        public String getValue() {
            String time = this.dateFormat.format(new Date());
            String label = Component.translatable("gameinfohud.realtime.label").getString();
            return label + "§6" + time + "§r";
        }
    }

    public static class Real12 extends TagTime {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        @Override
        public String getValue() {
            return this.dateFormat.format(new Date());
        }
    }

    public static class McDate extends TagTime {
        @Override
        public String getValue() {
            long day = world.getDayTime() / 24000;
            String k = "gameinfohud.time.mcdate";
            String t = Component.translatable(k, "§a" + day + "§r").getString();
            return t.equals(k) ? "Day " + day : t;
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new MinecraftTotal().setName("worldtimetotal"));
        TagRegistry.INSTANCE.register(new MinecraftDay().setName("day"));
        TagRegistry.INSTANCE.register(new MinecraftHour().setName("mctimeh"));
        TagRegistry.INSTANCE.register(new MinecraftMinute().setName("mctimem"));
        TagRegistry.INSTANCE.register(new Minecraft24().setName("mctime24").setAliases("mctime"));
        TagRegistry.INSTANCE.register(new Minecraft12().setName("mctime12"));
        TagRegistry.INSTANCE.register(new Real24().setName("rltime24").setAliases("rltime"));
        TagRegistry.INSTANCE.register(new Real12().setName("rltime12"));
        TagRegistry.INSTANCE.register(new McDate().setName("mcdate"));
    }
}
