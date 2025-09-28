package com.crschnick.pdxu.app.installation;

import java.util.Locale;

public class GameLanguage {

    public static GameLanguage ENGLISH = new GameLanguage(Locale.ENGLISH, "l_english", "English");
    public static GameLanguage TRANSLATION_HELPER = new GameLanguage(Locale.ENGLISH, "l_translate", "Translation Helper");

    public static GameLanguage byId(String langId) {
        return ENGLISH;
    }

    private final Locale locale;
    private final String id;
    private final String displayName;

    public GameLanguage(Locale locale, String id, String displayName) {
        this.locale = locale;
        this.id = id;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }
}
