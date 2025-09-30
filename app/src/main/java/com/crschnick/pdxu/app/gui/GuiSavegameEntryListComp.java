package com.crschnick.pdxu.app.gui;

import com.crschnick.pdxu.app.comp.SimpleComp;
import com.crschnick.pdxu.app.core.AppI18n;
import com.crschnick.pdxu.app.core.SavegameManagerState;
import com.crschnick.pdxu.app.gui.dialog.GuiImporter;
import com.crschnick.pdxu.app.util.Hyperlinks;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.javafx.FontIcon;

public class GuiSavegameEntryListComp extends SimpleComp {

    private static Region createNoCampaignNode() {
        VBox v = new VBox();
        Label text = new Label();
        v.getChildren().add(text);
        SavegameManagerState.get().onGameChange(n -> {
            if (n != null) {
                Platform.runLater(() -> {
                    text.setText(AppI18n.get("NO_SAVEGAMES", n.getTranslatedFullName()) + "\n");
                });
            }
        });

        Button importB = new Button(AppI18n.get("IMPORT_SAVEGAMES"));
        importB.setOnAction(e -> {
            GuiImporter.createImporterDialog();
            e.consume();
        });
        importB.setGraphic(new FontIcon());
        importB.getStyleClass().add(GuiStyle.CLASS_IMPORT);
        v.getChildren().add(importB);

        v.getChildren().add(new Label());
        Label text2 = new Label(AppI18n.get("FAMILIARIZE"));
        text2.setWrapText(true);
        text2.setTextAlignment(TextAlignment.CENTER);
        v.getChildren().add(text2);

        Button guide = new Button(AppI18n.get("USER_GUIDE"));
        guide.setOnAction((a) -> {
            Hyperlinks.open(Hyperlinks.DOCS);
        });
        v.getChildren().add(guide);

        v.getStyleClass().add(GuiStyle.CLASS_NO_CAMPAIGN);
        v.setFillWidth(true);
        v.setSpacing(10);
        v.setAlignment(Pos.CENTER);
        return v;
    }

    @Override
    protected Region createSimple() {
        Region grid = new GuiListViewComp<>(
                SavegameManagerState.get().getShownEntries(),
                GuiSavegameEntryComp::new,
                true)
                .createRegion();
        grid.setOpacity(0.9);
        grid.getStyleClass().add(GuiStyle.CLASS_ENTRY_LIST);

        var ncn = createNoCampaignNode();
        StackPane pane = new StackPane(ncn, grid);
        ncn.prefWidthProperty().bind(pane.widthProperty());
        ncn.prefHeightProperty().bind(pane.heightProperty());
        grid.prefWidthProperty().bind(pane.widthProperty());
        grid.prefHeightProperty().bind(pane.heightProperty());

        grid.setVisible(!SavegameManagerState.get().isStorageEmpty());
        ncn.setVisible(SavegameManagerState.get().isStorageEmpty());
        SavegameManagerState.get().storageEmptyProperty().addListener((c, o, n) -> {
            grid.setVisible(!n);
            ncn.setVisible(n);
        });
        grid.setAccessibleText("Campaign savegames");
        return pane;
    }
}
