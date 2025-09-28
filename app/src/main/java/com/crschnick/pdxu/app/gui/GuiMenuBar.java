package com.crschnick.pdxu.app.gui;

import com.crschnick.pdxu.app.core.AppI18n;
import com.crschnick.pdxu.app.core.SavegameManagerState;
import com.crschnick.pdxu.app.gui.dialog.*;
import com.crschnick.pdxu.app.installation.GameInstallation;
import com.crschnick.pdxu.app.installation.dist.GameDistLauncher;
import com.crschnick.pdxu.app.savegame.SavegameStorageIO;
import com.crschnick.pdxu.app.util.EditorProvider;
import com.crschnick.pdxu.app.util.Hyperlinks;
import com.crschnick.pdxu.app.util.ThreadHelper;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.nio.file.Path;
import java.util.Optional;

public class GuiMenuBar {

    private static MenuBar createMenuBar() {

        Menu pdxu = new Menu("Pdx-Unlimiter");

        MenuItem ed = new MenuItem(AppI18n.get("OPEN_EDITOR"));
        ed.setOnAction((a) -> {
            EditorProvider.get().browseExternalFile();
        });
        pdxu.getItems().add(ed);

        MenuItem export = new MenuItem(AppI18n.get("EXPORT_STORAGE"));
        export.setOnAction((a) -> {
            Optional<Path> path = GuiSavegameIO.showExportDialog();
            path.ifPresent(p -> {
                SavegameStorageIO.exportSavegameStorage(p);
            });
        });
        pdxu.getItems().add(export);


        Menu about = new Menu(AppI18n.get("ABOUT"));

        MenuItem other = new MenuItem(AppI18n.get("OTHER_PROJECTS"));
        other.setOnAction((a) -> {
            Hyperlinks.open(Hyperlinks.XPIPE);
        });

        MenuItem tri = new MenuItem(AppI18n.get("TRANSLATE"));
        tri.setOnAction((a) -> {
            GuiTranslate.showTranslatationAlert();
        });

        MenuItem src = new MenuItem(AppI18n.get("CONTRIBUTE"));
        src.setOnAction((a) -> {
            Hyperlinks.open(Hyperlinks.GITHUB);
        });

        about.getItems().add(other);
        about.getItems().add(tri);
        about.getItems().add(src);


        Menu help = new Menu(AppI18n.get("HELP"));

        MenuItem guide = new MenuItem(AppI18n.get("USER_GUIDE"));
        guide.setOnAction((a) -> {
            Hyperlinks.open(Hyperlinks.DOCS);
        });
        help.getItems().add(guide);

        MenuItem discord = new MenuItem(AppI18n.get("DISCORD"));
        discord.setOnAction((a) -> {
            Hyperlinks.open(Hyperlinks.DISCORD);
        });
        help.getItems().add(discord);

        MenuBar menuBar = new MenuBar();
        menuBar.setUseSystemMenuBar(false);
        menuBar.getMenus().add(pdxu);
        menuBar.getMenus().add(about);
        menuBar.getMenus().add(help);
        return menuBar;
    }

    private static Node createRightBar() {
        JFXButton m = new JFXButton(AppI18n.get("SWITCH_GAME"));
        m.setGraphic(new FontIcon());
        m.getStyleClass().add(GuiStyle.CLASS_SWTICH_GAME);
        m.setOnAction(a -> GuiGameSwitcher.showGameSwitchDialog());
        m.setDisable(SavegameManagerState.get().current() == null);
        SavegameManagerState.get().onGameChange(n -> {
            Platform.runLater(() -> m.setDisable(n == null));
        });

        JFXButton importB = new JFXButton(AppI18n.get("IMPORT"));
        importB.setOnAction(e -> {
            GuiImporter.createImporterDialog();
            e.consume();
        });
        importB.setGraphic(new FontIcon());
        importB.getStyleClass().add(GuiStyle.CLASS_IMPORT);
        importB.setDisable(SavegameManagerState.get().current() == null);
        SavegameManagerState.get().onGameChange(n -> {
            Platform.runLater(() -> importB.setDisable(n == null));
        });

        JFXButton launch = new JFXButton(AppI18n.get("LAUNCH"));
        launch.setOnAction(e -> {
            GameDistLauncher.startLauncher();
            e.consume();
        });
        launch.setGraphic(new FontIcon());
        launch.getStyleClass().add(GuiStyle.CLASS_LAUNCH);
        launch.setDisable(SavegameManagerState.get().current() == null);
        SavegameManagerState.get().onGameChange(n -> {
            Platform.runLater(() -> {
                boolean disable = n == null || !GameInstallation.ALL.get(n).getDist().supportsLauncher();
                launch.setDisable(disable);
            });
        });

        var box = new HBox(m, importB, launch);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    public static Node createMenu() {
        MenuBar leftBar = createMenuBar();

        StackPane spacer = new StackPane();
        Label game = new Label();
        SavegameManagerState.get().onGameChange(n -> {
            String name = n != null ? n.getTranslatedFullName() : AppI18n.get("NO_GAME");
            Platform.runLater(() -> game.setText(name));
        });
        spacer.getChildren().add(game);
        spacer.setAlignment(Pos.CENTER);

        spacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        StackPane s = new StackPane();
        s.getStyleClass().add("menu-bar");
        s.getChildren().add(createRightBar());

        return new HBox(leftBar, spacer, s);
    }
}
