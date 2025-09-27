package com.crschnick.pdxu.prefs;

import com.crschnick.pdxu.comp.Comp;
import com.crschnick.pdxu.platform.LabelGraphic;

public abstract class AppPrefsCategory {

    public boolean show() {
        return true;
    }

    public abstract String getId();

    protected abstract LabelGraphic getIcon();

    public abstract Comp<?> create();
}
