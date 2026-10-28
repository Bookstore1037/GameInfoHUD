package com.bookstore.gameinfohud.reference;

@SuppressWarnings("HardCodedStringLiteral")
public final class Names {

    public static final class Config {

        public static final String FILENAME_DESC = "The configuration that should be loaded on startup.";
        public static final String SHOW_HUD_DESC = "If this is true, it will render the info overlay";
        public static final String REPLACE_DEBUG_DESC = "Replace the debug overlay (F3) with the InGameInfoXML overlay.";
        public static final String SHOW_IN_CHAT_DESC = "Display the overlay in chat.";
        public static final String SHOW_ON_PLAYER_LIST_DESC = "Display the overlay on the player list.";
        public static final String SCALE_DESCRIPTION = "The overlay will be scaled by this amount.(use this one, the other is deprecated)";
        public static final String FILE_INTERVAL_DESC = "The interval between file reads for the 'file' tag (in seconds).";

        public static final String ALIGNMENT_DESC = "Offsets for %s (X<space>Y).";
    }

    public static final class Files {

        public static final String FILE_XML = "ingameinfo.xml";

        public static final String EXT_XML = ".xml";
        public static final String EXT_JSON = ".json";
        public static final String EXT_TXT = ".txt";
    }

    public static final class Keys {

        public static final String CATEGORY = "ingameinfoxml.key.category";
        public static final String TOGGLE = "ingameinfoxml.key.toggle";
    }
}
