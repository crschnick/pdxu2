package com.crschnick.pdxu.app.prefs;

import atlantafx.base.controls.ProgressSliderSkin;
import atlantafx.base.theme.Styles;
import com.crschnick.pdxu.app.comp.Comp;
import com.crschnick.pdxu.app.comp.base.IntFieldComp;
import com.crschnick.pdxu.app.comp.base.PathChoiceComp;
import com.crschnick.pdxu.app.comp.base.TextFieldComp;
import com.crschnick.pdxu.app.platform.LabelGraphic;
import com.crschnick.pdxu.app.platform.OptionsBuilder;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.control.Slider;

public class ImportsCategory extends AppPrefsCategory {

    @Override
    public String getId() {
        return "imports";
    }

    @Override
    protected LabelGraphic getIcon() {
        return new LabelGraphic.IconGraphic("mdi2d-download-box-outline");
    }

    public Comp<?> create() {
        var prefs = AppPrefs.get();
        var builder = new OptionsBuilder();
        builder.addTitle("paradoxConverters")
                .sub(new OptionsBuilder()
                        .pref(prefs.playSoundOnBackgroundImport)
                        .addToggle(prefs.playSoundOnBackgroundImport)
                        .pref(prefs.enabledTimedImports)
                        .addToggle(prefs.enabledTimedImports)
                        .pref(prefs.timedImportsInterval)
                        .addComp(
                                Comp.of(() -> {
                                            var s = new Slider(1, 60, prefs.timedImportsInterval.getValue());
                                            s.getStyleClass().add(Styles.SMALL);
                                            s.valueProperty().addListener((ov, oldv, newv) -> {
                                                prefs.timedImportsInterval.setValue(newv.intValue());
                                            });
                                            s.setSkin(new ProgressSliderSkin(s));
                                            return s;
                                        })
                                        .maxWidth(600))
                        .addComp(new IntFieldComp(prefs.timedImportsInterval).disable(new ReadOnlyBooleanWrapper(true)).maxWidth(50))
                        .pref(prefs.importOnGameNormalExit)
                        .addToggle(prefs.importOnGameNormalExit)
                        .pref(prefs.deleteOnImport)
                        .addToggle(prefs.deleteOnImport)
                );
        return builder.buildComp();
    }
}
