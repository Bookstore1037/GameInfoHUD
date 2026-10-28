package com.bookstore.gameinfohud.tag;

import com.bookstore.gameinfohud.client.gui.Info;
import com.bookstore.gameinfohud.client.gui.InfoText;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3i;

public abstract class Tag {

    protected static final Minecraft minecraft = Minecraft.getInstance();
    protected static final Vector3i playerPosition = new Vector3i();
    protected static final Vector3f playerMotion = new Vector3f();
    protected static Level world;
    protected static LocalPlayer player;

    private String name = null;
    private String[] aliases = new String[0];

    public Tag setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Tag setAliases(String... aliases) {
        this.aliases = aliases;
        return this;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public abstract String getCategory();

    public abstract String getValue();

    public @NotNull String getValue(@NotNull InfoText caller) {
        return "";
    }

    public static void update() {
        world = minecraft.level;
        player = minecraft.player;

        if (player != null) {
            playerPosition.set(
                    (int) Math.floor(player.getX()),
                    (int) Math.floor(player.getY()),
                    (int) Math.floor(player.getZ()));
            playerMotion.set(
                    (float) (player.getX() - player.xOld),
                    (float) (player.getY() - player.yOld),
                    (float) (player.getZ() - player.zOld));
        }
    }

    public static void releaseResources() {
        TagNearbyPlayer.releaseResources();
        TagPlayerPotion.releaseResources();
    }

    public static void onClientDisconnect() {
        world = null;
        player = null;
    }

    public static String getIconTag(Info info) {
        return String.format("{ICON|%s}", info.getIconSpacing());
    }
}
