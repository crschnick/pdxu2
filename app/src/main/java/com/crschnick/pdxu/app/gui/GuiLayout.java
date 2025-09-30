package com.crschnick.pdxu.app.gui;

import com.crschnick.pdxu.app.core.SavegameManagerState;
import com.crschnick.pdxu.app.core.TaskExecutor;
import com.crschnick.pdxu.app.gui.game.GameGuiFactory;
import com.crschnick.pdxu.app.gui.game.GameImage;
import com.crschnick.pdxu.app.installation.Game;
import com.crschnick.pdxu.app.savegame.FileImporter;
import com.crschnick.pdxu.app.util.ThreadHelper;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class GuiLayout {

    private StackPane stack;
    private BorderPane layout;
    private Pane loadingBg;
    private StackPane fileDropOverlay;

    public void setup() {
        layout = new BorderPane();

        JFXSpinner loading = new JFXSpinner();
        loadingBg = new StackPane(loading);
        loadingBg.getStyleClass().add(GuiStyle.CLASS_LOADING);
        loadingBg.setVisible(false);
        TaskExecutor.getInstance().busyProperty().addListener((c, o, n) -> {
            setBusy(n);
        });
        loadingBg.setMinWidth(Pane.USE_COMPUTED_SIZE);
        loadingBg.setPrefHeight(Pane.USE_COMPUTED_SIZE);

        fileDropOverlay = new StackPane(new FontIcon());
        fileDropOverlay.setAlignment(Pos.CENTER);
        fileDropOverlay.getStyleClass().add("file-drag");
        fileDropOverlay.setVisible(false);

        stack = new StackPane(new Pane(), layout, loadingBg, fileDropOverlay);
        stack.setOpacity(0);
    }

    private void fillLayout() {
        layout.setBottom(new GuiStatusBarComp().createRegion());

        var pane = new GuiSavegameEntryListComp().createRegion();
        layout.setCenter(pane);

        layout.setLeft(new GuiSavegameCollectionListComp<>().createRegion());
    }

    private void setGameLookAndFeel(Game g) {
        Platform.runLater(() -> {
            if (g != null) {
                var bg = GameGuiFactory.ALL.get(g).background();
                stack.getChildren().set(0, bg);
            } else {
                stack.getChildren().set(0, new Pane());
            }
        });
    }

    private void setBusy(boolean busy) {
        if (!busy) {
            ThreadHelper.createPlatformThread("loading delay", true, () -> {
                ThreadHelper.sleep(50);
                if (!TaskExecutor.getInstance().isBusy()) {
                    Platform.runLater(() -> loadingBg.setVisible(false));
                }
            }).start();
        } else {
            Platform.runLater(() -> loadingBg.setVisible(true));
        }
    }

    private void setupDragAndDrop() {
        stack.setOnDragOver(event -> {
            if (event.getGestureSource() == null && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        stack.setOnDragEntered(event -> {
            if (event.getGestureSource() == null && event.getDragboard().hasFiles()) {
                fileDropOverlay.setVisible(true);
            }
        });

        stack.setOnDragExited(event -> {
            fileDropOverlay.setVisible(false);
        });

        stack.setOnDragDropped(event -> {
            // Only accept drops from outside the app window
            if (event.getGestureSource() == null && event.getDragboard().hasFiles()) {
                event.setDropCompleted(true);
                Dragboard db = event.getDragboard();
                FileImporter.onFileDrop(db.getFiles());
            }
            event.consume();
        });
    }

    public void finishSetup() {
        SavegameManagerState.get().onGameChange(n -> {
            GameImage.loadGameImages(n);
            setGameLookAndFeel(n);
        });

        Platform.runLater(() -> {
            // Disable focus on startup
            layout.requestFocus();

            fillLayout();
            setupDragAndDrop();

            FadeTransition ft = new FadeTransition(Duration.millis(1500), stack);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        });
    }

    public Region getContent() {
        return stack;
    }
}
