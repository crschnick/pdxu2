package com.crschnick.pdxu.app.prefs;

import com.crschnick.pdxu.app.comp.Comp;
import com.crschnick.pdxu.app.comp.base.GameDistChoiceComp;
import com.crschnick.pdxu.app.comp.base.PathChoiceComp;
import com.crschnick.pdxu.app.installation.Game;
import com.crschnick.pdxu.app.installation.GameInstallation;
import com.crschnick.pdxu.app.installation.dist.GameDist;
import com.crschnick.pdxu.app.platform.LabelGraphic;
import com.crschnick.pdxu.app.platform.OptionsBuilder;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import java.nio.file.Path;

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
        builder.addTitle("gameInstallations")
                .sub(new OptionsBuilder()
                        .nameAndDescription("gameDirs")
                        .addComp(Comp.empty())
                        .name("eu4")
                        .description("installationDirectory")
                        .addComp(new GameDistChoiceComp("eu4InstallDir", Game.EU4, wrap(Game.EU4, prefs.eu4Directory)).maxWidth(600))
                        .name("ck3")
                        .description("installationDirectory")
                        .addComp(new GameDistChoiceComp("ck3InstallDir", Game.CK3, wrap(Game.CK3, prefs.ck3Directory)).maxWidth(600))
                        .name("hoi4")
                        .description("installationDirectory")
                        .addComp(new GameDistChoiceComp("hoi4InstallDir", Game.HOI4, wrap(Game.HOI4, prefs.hoi4Directory)).maxWidth(600))
                        .name("vic3")
                        .description("installationDirectory")
                        .addComp(new GameDistChoiceComp("vic3InstallDir", Game.VIC3, wrap(Game.VIC3, prefs.vic3Directory)).maxWidth(600))
                        .name("stellaris")
                        .description("installationDirectory")
                        .addComp(new GameDistChoiceComp("stellarisInstallDir", Game.STELLARIS, wrap(Game.STELLARIS, prefs.stellarisDirectory)).maxWidth(600))
                        .name("ck2")
                        .description("installationDirectory")
                        .addComp(new GameDistChoiceComp("ck2InstallDir", Game.CK2, wrap(Game.CK2, prefs.ck2Directory)).maxWidth(600))
                        .name("vic2")
                        .description("installationDirectory")
                        .addComp(new GameDistChoiceComp("vic2InstallDir", Game.VIC2, wrap(Game.VIC2, prefs.vic2Directory)).maxWidth(600))
                );
        return builder.buildComp();
    }

    private Property<GameDist> wrap(Game game, Property<Path> p) {
        var initial = GameInstallation.ALL.get(game);
        var prop = new SimpleObjectProperty<>(initial != null ? initial.getDist() : null);
        prop.subscribe(gameDist -> {
           p.setValue(gameDist != null ? gameDist.getInstallLocation() : null);
        });
        return prop;
    }
}
