package com.bookstore.gameinfohud.tag;

import com.bookstore.gameinfohud.tag.registry.TagRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public abstract class TagMisc extends Tag {

    @Override
    public String getCategory() {
        return "misc";
    }

    public static class MemoryMaximum extends TagMisc {

        @Override
        public String getValue() {
            return String.valueOf(Runtime.getRuntime().maxMemory());
        }
    }

    public static class MemoryTotal extends TagMisc {

        @Override
        public String getValue() {
            return String.valueOf(Runtime.getRuntime().totalMemory());
        }
    }

    public static class MemoryFree extends TagMisc {

        @Override
        public String getValue() {
            return String.valueOf(Runtime.getRuntime().freeMemory());
        }
    }

    public static class MemoryUsed extends TagMisc {

        @Override
        public String getValue() {
            return String.valueOf(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        }
    }

    public static class FPS extends TagMisc {

        @Override
        public String getValue() {
            return String.valueOf(minecraft.getFps());
        }
    }

    public static class ResourcePack extends TagMisc {

        @Override
        public String getValue() {
            return "";
        }
    }

    public static class EntitiesLine extends TagMisc {
        @Override
        public String getValue() {
            int total = countEntities();
            int rendered = countRendered();
            if (rendered < 0) {
                rendered = total;  // 反射失败，已渲染 = 总数
            }
            String k = "gameinfohud.entities";
            String t = Component.translatable(k, "§b" + rendered + "§r", "§b" + total + "§r").getString();
            return t.equals(k) ? rendered + " / " + total : t;
        }
    }

        public static class LoadedChunks extends TagMisc {

            @Override
            public String getValue() {
                return String.valueOf(world.getChunkSource().getLoadedChunksCount());
            }
        }

        public static class Server extends TagMisc {

            @Override
            public String getValue() {
                ServerData data = minecraft.getCurrentServer();
                if (data == null) return "localhost";
                String port = port(data);
                return host(data) + ("25565".equals(port) ? "" : ":" + port);
            }
        }

        public static class ServerName extends TagMisc {

            @Override
            public String getValue() {
                return host(minecraft.getCurrentServer());
            }
        }

        public static class ServerIP extends TagMisc {

            @Override
            public String getValue() {
                return host(minecraft.getCurrentServer());
            }
        }

        public static class ServerPort extends TagMisc {

            @Override
            public String getValue() {
                return port(minecraft.getCurrentServer());
            }
        }

        public static class Ping extends TagMisc {

            @Override
            public String getValue() {
                if (minecraft.getConnection() == null) return "-1";
                PlayerInfo info = minecraft.getConnection().getPlayerInfo(player.getUUID());
                return info != null ? String.valueOf(info.getLatency()) : "-1";
            }
        }

        public static class PingIcon extends TagMisc {

            @Override
            public String getValue() {
                return "";
            }
        }

        private static String host(ServerData data) {
            if (data == null) return "localhost";
            String ip = data.ip;
            int i = ip.indexOf(':');
            return i < 0 ? ip : ip.substring(0, i);
        }

        private static String port(ServerData data) {
            if (data == null) return "25565";
            String ip = data.ip;
            int i = ip.indexOf(':');
            return i < 0 ? "25565" : ip.substring(i + 1);
        }

        private static int countEntities() {
            if (minecraft.level == null) return 0;
            int count = 0;
            for (Entity ignored : minecraft.level.entitiesForRendering()) {
                count++;
            }
            return count;
        }

        private static int countRendered() {
            try {
                var lr = Minecraft.getInstance().levelRenderer;
                var f = LevelRenderer.class.getDeclaredField("renderedEntities");
                f.setAccessible(true);
                return f.getInt(lr);
            } catch (Exception e) {
                return -1;
            }
        }


        public static void register() {
            TagRegistry.INSTANCE.register(new MemoryMaximum().setName("memmax"));
            TagRegistry.INSTANCE.register(new MemoryTotal().setName("memtotal"));
            TagRegistry.INSTANCE.register(new MemoryFree().setName("memfree"));
            TagRegistry.INSTANCE.register(new MemoryUsed().setName("memused"));
            TagRegistry.INSTANCE.register(new FPS().setName("fps"));
            TagRegistry.INSTANCE.register(new ResourcePack().setName("resourcepack"));
            TagRegistry.INSTANCE.register(new LoadedChunks().setName("loadedchunks"));
            TagRegistry.INSTANCE.register(new EntitiesLine().setName("entitiesline"));
            TagRegistry.INSTANCE.register(new Server().setName("server"));
            TagRegistry.INSTANCE.register(new ServerName().setName("servername"));
            TagRegistry.INSTANCE.register(new ServerIP().setName("serverip"));
            TagRegistry.INSTANCE.register(new ServerPort().setName("serverport"));
            TagRegistry.INSTANCE.register(new Ping().setName("ping"));
            TagRegistry.INSTANCE.register(new PingIcon().setName("pingicon"));
        }

}
