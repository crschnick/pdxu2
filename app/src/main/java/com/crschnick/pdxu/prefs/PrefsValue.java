package com.crschnick.pdxu.prefs;

public interface PrefsValue {

    default boolean isSelectable() {
        return true;
    }
}
