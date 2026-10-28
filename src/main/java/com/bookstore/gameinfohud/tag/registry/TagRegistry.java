package com.bookstore.gameinfohud.tag.registry;

import java.util.HashMap;
import java.util.Map;

import com.bookstore.gameinfohud.reference.Reference;
import com.bookstore.gameinfohud.tag.Tag;
import com.bookstore.gameinfohud.tag.TagFormatting;
import com.bookstore.gameinfohud.tag.TagMisc;
import com.bookstore.gameinfohud.tag.TagMouseOver;
import com.bookstore.gameinfohud.tag.TagNearbyPlayer;
import com.bookstore.gameinfohud.tag.TagPlayerEquipment;
import com.bookstore.gameinfohud.tag.TagPlayerGeneral;
import com.bookstore.gameinfohud.tag.TagPlayerPosition;
import com.bookstore.gameinfohud.tag.TagPlayerPotion;
import com.bookstore.gameinfohud.tag.TagRiding;
import com.bookstore.gameinfohud.tag.TagTime;
import com.bookstore.gameinfohud.tag.TagWorld;

public class TagRegistry {

    public static final TagRegistry INSTANCE = new TagRegistry();

    private final Map<String, Tag> stringTagMap = new HashMap<>();

    private void register(String name, Tag tag) {
        if (this.stringTagMap.containsKey(name)) {
            Reference.logger.error("Duplicate tag key '{}'!", name);
            return;
        }

        if (name == null) {
            Reference.logger.error("Tag name cannot be null!");
            return;
        }

        this.stringTagMap.put(name.toLowerCase(), tag);
    }

    public void register(Tag tag) {
        register(tag.getName(), tag);

        for (String name : tag.getAliases()) {
            register(name, tag);
        }
    }

    public Tag getTag(String name) {
        return stringTagMap.get(name.toLowerCase());
    }

    public void init() {
        TagFormatting.register();
        TagMisc.register();
        TagMouseOver.register();
        TagNearbyPlayer.register();
        TagPlayerEquipment.register();
        TagPlayerGeneral.register();
        TagPlayerPosition.register();
        TagPlayerPotion.register();
        TagRiding.register();
        TagTime.register();
        TagWorld.register();

        Reference.logger.info("Registered {} tags.", stringTagMap.size());
    }
}
