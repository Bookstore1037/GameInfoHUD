package com.bookstore.gameinfohud.tag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.bookstore.gameinfohud.tag.registry.TagRegistry;

import net.minecraft.world.entity.player.Player;

public abstract class TagNearbyPlayer extends Tag {

    public static final int MAXIMUM_INDEX = 16;

    private static final Comparator<Player> PLAYER_DISTANCE_COMPARATOR = (playerA, playerB) -> {
        if (Tag.player == null) {
            return 0;
        }

        double distanceA = Tag.player.distanceToSqr(playerA);
        double distanceB = Tag.player.distanceToSqr(playerB);
        if (distanceA > distanceB) {
            return 1;
        } else if (distanceA < distanceB) {
            return -1;
        }
        return 0;
    };
    protected static Player[] nearbyPlayers = null;
    protected final int index;

    public TagNearbyPlayer(int index) {
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
        return "nearbyplayer";
    }

    protected static void updateNearbyPlayers() {
        if (nearbyPlayers == null) {
            List<Player> playerList = new ArrayList<>();
            for (Player player : world.players()) {
                if (player != Tag.player && !player.isShiftKeyDown()) {
                    playerList.add(player);
                }
            }
            playerList.sort(PLAYER_DISTANCE_COMPARATOR);
            nearbyPlayers = playerList.toArray(new Player[0]);
        }
    }

    public static class Name extends TagNearbyPlayer {

        public Name(int index) {
            super(index);
        }

        @Override
        public String getValue() {
            updateNearbyPlayers();
            if (nearbyPlayers.length > this.index) {
                return nearbyPlayers[this.index].getDisplayName().getString();
            }
            return "";
        }
    }

    public static class Distance extends TagNearbyPlayer {

        public Distance(int index) {
            super(index);
        }

        @Override
        public String getValue() {
            updateNearbyPlayers();
            if (nearbyPlayers.length > this.index) {
                return String.format(Locale.ENGLISH, "%.2f", nearbyPlayers[this.index].distanceTo(player));
            }
            return "-1";
        }
    }

    public static void register() {
        for (int i = 0; i < MAXIMUM_INDEX; i++) {
            TagRegistry.INSTANCE.register(new Name(i).setName("nearbyplayername"));
            TagRegistry.INSTANCE.register(new Distance(i).setName("nearbyplayerdistance"));
        }
    }

    public static void releaseResources() {
        nearbyPlayers = null;
    }
}
