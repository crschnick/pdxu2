package com.crschnick.pdxu.prefs;

import com.crschnick.pdxu.comp.Comp;
import com.crschnick.pdxu.platform.LabelGraphic;
import com.crschnick.pdxu.platform.OptionsBuilder;

public class UpdatesCategory extends AppPrefsCategory {

    @Override
    public String getId() {
        return "updates";
    }

    @Override
    protected LabelGraphic getIcon() {
        return new LabelGraphic.IconGraphic("mdi2d-download-box-outline");
    }

    public Comp<?> create() {
        var prefs = AppPrefs.get();
        var builder = new OptionsBuilder();
        builder.addTitle("updates")
                .sub(new OptionsBuilder()
                        .pref(prefs.automaticallyCheckForUpdates)
                        .addToggle(prefs.automaticallyCheckForUpdates));
        return builder.buildComp();
    }
}
