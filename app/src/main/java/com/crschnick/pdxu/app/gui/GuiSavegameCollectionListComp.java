package com.crschnick.pdxu.app.gui;

import atlantafx.base.theme.Styles;
import com.crschnick.pdxu.app.comp.Comp;
import com.crschnick.pdxu.app.comp.SimpleComp;
import com.crschnick.pdxu.app.comp.base.IconButtonComp;
import com.crschnick.pdxu.app.core.SavegameManagerState;
import com.crschnick.pdxu.app.gui.dialog.GuiImporter;
import com.crschnick.pdxu.app.info.SavegameInfo;
import com.crschnick.pdxu.app.util.ThreadHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

import static com.crschnick.pdxu.app.gui.GuiStyle.CLASS_CAMPAIGN_LIST;

public class GuiSavegameCollectionListComp<T, I extends SavegameInfo<T>> extends SimpleComp {

    private Comp<?> createImportButton() {
        var b = new IconButtonComp("mdi2l-layers", () -> {
            GuiImporter.createImporterDialog();
        });
        b.styleClass("import-button");
        b.apply(struc -> {
            struc.get().getStyleClass().remove(Styles.FLAT);
        });
        return b;
    }

    private Region createTopBar() {
        var filterProperty = new SimpleStringProperty();
        filterProperty.addListener((observable, oldValue, newValue) -> {
            ThreadHelper.runAsync(() -> {
                SavegameManagerState.get().getFilter().filterProperty().set(newValue);
            });
        });
        var filter = new GuiFilterComp(filterProperty).createRegion();
        var importButton = createImportButton().createRegion();
        var hbox = new HBox(filter, importButton);
        importButton.minHeightProperty().bind(filter.heightProperty());
        importButton.prefHeightProperty().bind(filter.heightProperty());
        importButton.maxHeightProperty().bind(filter.heightProperty());
        importButton.minWidthProperty().bind(filter.heightProperty());
        importButton.prefWidthProperty().bind(filter.heightProperty());
        importButton.maxWidthProperty().bind(filter.heightProperty());
        hbox.setSpacing(8);
        hbox.setPadding(new Insets(8, 8, 0, 8));
        hbox.setAlignment(Pos.CENTER);
        HBox.setHgrow(filter, Priority.ALWAYS);
        return hbox;
    }

    @Override
    protected Region createSimple() {
        Region list = new GuiListViewComp<>(
                SavegameManagerState.<T, I>get().getShownCollections(),
                c -> new GuiSavegameCampaignComp<>(c),
                false)
                .createRegion();
        list.getStyleClass().add(CLASS_CAMPAIGN_LIST);
        list.setAccessibleText("Campaign list");
        list.setPrefWidth(300);

        var top = createTopBar();
        var box = new VBox(top, list);
        top.prefWidthProperty().bind(box.widthProperty());
        VBox.setVgrow(list, Priority.ALWAYS);
        return box;
    }
}
