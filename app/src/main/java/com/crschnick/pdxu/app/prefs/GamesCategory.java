package com.crschnick.pdxu.app.prefs;

import com.crschnick.pdxu.app.comp.Comp;
import com.crschnick.pdxu.app.comp.base.GameDistChoiceComp;
import com.crschnick.pdxu.app.comp.base.PathChoiceComp;
import com.crschnick.pdxu.app.installation.Game;
import com.crschnick.pdxu.app.installation.GameInstallation;
import com.crschnick.pdxu.app.platform.LabelGraphic;
import com.crschnick.pdxu.app.platform.OptionsBuilder;
import javafx.beans.property.SimpleObjectProperty;

public class GamesCategory extends AppPrefsCategory {

    @Override
    public String getId() {
        return "games";
    }

    @Override
    protected LabelGraphic getIcon() {
        return new LabelGraphic.IconGraphic("mdi2a-animation-play-outline");
    }

    public Comp<?> create() {
        var prefs = AppPrefs.get();
        var builder = new OptionsBuilder();
        var eu4 = new SimpleObjectProperty<>(GameInstallation.ALL.get(Game.EU4).getDist());
        builder.addTitle("paradoxConverters")
                .sub(new OptionsBuilder()
                        .pref(prefs.eu4Directory)
                        .addComp(new GameDistChoiceComp("eu4InstallDir", Game.EU4, eu4).maxWidth(600))
                        .pref(prefs.ck3Directory)
                        .addComp(new GameDistChoiceComp("ck3InstallDir", Game.CK3, eu4).maxWidth(600))
                        .pref(prefs.hoi4Directory)
                        .addComp(new GameDistChoiceComp("hoi4InstallDir", Game.HOI4, eu4).maxWidth(600))
                        .pref(prefs.vic3Directory)
                        .addComp(new GameDistChoiceComp("vic3InstallDir", Game.VIC3, eu4).maxWidth(600))
                        .pref(prefs.stellarisDirectory)
                        .addComp(new GameDistChoiceComp("stellarisInstallDir", Game.STELLARIS, eu4).maxWidth(600))
                        .pref(prefs.ck2Directory)
                        .addComp(new GameDistChoiceComp("ck2InstallDir", Game.CK2, eu4).maxWidth(600))
                        .pref(prefs.vic2Directory)
                        .addComp(new GameDistChoiceComp("vic2InstallDir", Game.VIC2, eu4).maxWidth(600))
                );
        return builder.buildComp();
    }
}
