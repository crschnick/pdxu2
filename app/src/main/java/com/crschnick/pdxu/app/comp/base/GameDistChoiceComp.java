package com.crschnick.pdxu.app.comp.base;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import com.crschnick.pdxu.app.comp.SimpleComp;
import com.crschnick.pdxu.app.core.AppFontSizes;
import com.crschnick.pdxu.app.core.AppI18n;
import com.crschnick.pdxu.app.core.window.AppMainWindow;
import com.crschnick.pdxu.app.gui.GuiStyle;
import com.crschnick.pdxu.app.gui.GuiTooltips;
import com.crschnick.pdxu.app.installation.Game;
import com.crschnick.pdxu.app.installation.dist.GameDist;
import com.crschnick.pdxu.app.installation.dist.GameDists;
import com.crschnick.pdxu.app.installation.dist.SteamDist;
import com.crschnick.pdxu.app.installation.dist.WindowsStoreDist;
import com.crschnick.pdxu.app.platform.LabelGraphic;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import lombok.AllArgsConstructor;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class GameDistChoiceComp extends SimpleComp {

    private final String nameKey;
    private final Game game;
    private final Property<GameDist> gameDist;

    @Override
    protected Region createSimple() {
        ObjectProperty<GameDist> setDist = new SimpleObjectProperty<>();

        var typeTooltip = new SimpleStringProperty();
        var typeIcon = new SimpleObjectProperty<>(new LabelGraphic.IconGraphic("mdi-help"));
        var typeLabel = new IconButtonComp(typeIcon);
        typeLabel.apply(struc -> struc.get().getStyleClass().remove(Styles.FLAT));
        typeLabel.tooltip(typeTooltip);
        typeLabel.disable(new ReadOnlyBooleanWrapper(true));

        var location = new SimpleStringProperty(Optional.ofNullable(gameDist.getValue()).map(v -> v.getInstallLocation().toString())
                .orElse(""));
        var locationLabel = new TextFieldComp(location);
        locationLabel.hgrow();
        locationLabel.apply(struc -> {
            struc.get().setEditable(false);
            AppFontSizes.sm(struc.get());
        });

        var browse = new IconButtonComp("mdi-magnify", () -> {
            DirectoryChooser dirChooser = new DirectoryChooser();
            if (setDist.get() != null && Files.exists(setDist.get().getInstallLocation())) {
                dirChooser.setInitialDirectory(setDist.get().getInstallLocation().toFile());
            }
            dirChooser.setTitle(AppI18n.get("SELECT_DIR", AppI18n.get(nameKey)));
            File file = dirChooser.showDialog(AppMainWindow.get().getStage());
            if (file != null && file.exists()) {
                var path = file.toPath();
                // Ugly hack for newer installations
                if (path.endsWith("binaries")) {
                    path = path.getParent();
                }
                setDist.set(GameDists.detectDistFromDirectory(game, path));
            }
        });
        browse.tooltipKey("browseDist");
        browse.apply(struc -> struc.get().getStyleClass().remove(Styles.FLAT));

        var xbox = new IconButtonComp("mdi-xbox", () -> {
            var dist = WindowsStoreDist.getDist(game, null).orElse(null);
            if (dist == null) {
                return;
            }

            setDist.set(dist);
        });
        xbox.tooltipKey("xboxDist");
        xbox.apply(struc -> struc.get().getStyleClass().remove(Styles.FLAT));
        if (game.getWindowsStoreName() == null) {
            xbox.disable(new ReadOnlyBooleanWrapper(true));
            xbox.tooltipKey("xboxDistUnavailable");
        }

        var del = new IconButtonComp("mdi2t-trash-can-outline", () -> {
            setDist.set(null);
        });
        del.tooltipKey("delete");
        del.apply(struc -> struc.get().getStyleClass().remove(Styles.FLAT));

        setDist.addListener((c, o, n) -> {
            if (n != null) {
                location.set(n.getInstallLocation().toString());

                if (n instanceof WindowsStoreDist) {
                    typeIcon.setValue(new LabelGraphic.IconGraphic("mdi-xbox"));
                    typeTooltip.set("Windows Store version");
                } else if (n instanceof SteamDist) {
                    typeIcon.setValue(new LabelGraphic.IconGraphic("mdi-steam"));
                    typeTooltip.set("Steam version");
                } else {
                    typeIcon.setValue(new LabelGraphic.IconGraphic("mdi-help"));
                    typeTooltip.set("Other version");
                }
            } else {
                location.set("");
                typeIcon.setValue(new LabelGraphic.IconGraphic("mdi-help"));
                typeTooltip.set(null);
            }
        });
        setDist.setValue(gameDist.getValue());

        var hbox = new InputGroupComp(List.of(typeLabel, locationLabel, browse, xbox, del));
        hbox.styleClass("game-dist-choice-comp");
        hbox.setHeightReference(locationLabel);
        return hbox.createRegion();
    }
}
