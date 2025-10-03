package com.crschnick.pdxu.app.gui.dialog;

import com.crschnick.pdxu.app.comp.base.ModalButton;
import com.crschnick.pdxu.app.comp.base.ModalOverlay;
import com.crschnick.pdxu.app.core.AppI18n;
import com.crschnick.pdxu.app.core.window.AppDialog;
import com.crschnick.pdxu.app.core.window.AppSideWindow;
import com.crschnick.pdxu.app.gui.GuiTooltips;
import com.crschnick.pdxu.app.installation.GameLanguage;
import com.crschnick.pdxu.app.installation.GameLocalisationHelper;
import com.crschnick.pdxu.app.issue.ErrorEventFactory;
import com.crschnick.pdxu.app.util.ConverterSupport;
import com.crschnick.pdxu.app.util.DesktopHelper;
import com.crschnick.pdxu.app.util.Hyperlinks;
import com.crschnick.pdxu.io.node.Node;
import com.crschnick.pdxu.io.parser.TextFormatParser;
import com.jfoenix.controls.JFXRadioButton;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import lombok.Value;

import java.util.Map;
import java.util.Optional;

import static com.crschnick.pdxu.app.gui.GuiStyle.CLASS_CONTENT_DIALOG;

@Value
public class GuiConverterConfig {

    ConverterSupport converterSupport;

    public boolean showConfirmConversionDialog() {
        var modal = ModalOverlay.of("converterConfigureTitle", AppDialog.dialogText(
                AppI18n.observable("converterConfigureContent", converterSupport.getFromName(), converterSupport.getToName())));
        var ok = new SimpleBooleanProperty();
        modal.addButton(new ModalButton("converterConfigure", () -> {
            ok.set(true);
        }, true, true));
        modal.showAndWait();
        return ok.get();
    }

    public void showUsageDialog() {
        var modal = ModalOverlay.of("converterUsageTitle", AppDialog.dialogTextKey("converterUsageContent"));
        modal.addButton(new ModalButton("showDownloads", () -> {
            Hyperlinks.open(converterSupport.getDownloadLink());
        }, true, true));
        modal.show();
    }

    public void showConversionSuccessDialog() {
        var modal = ModalOverlay.of("converterSuccessTitle", AppDialog.dialogTextKey("converterSuccessContent"));
        modal.addButton(ModalButton.ok());
        modal.show();
    }

    public void showConversionErrorDialog() {
        var modal = ModalOverlay.of("converterFailedTitle", AppDialog.dialogTextKey("converterFailedContent"));
        modal.addButton(new ModalButton("converterFailedLogs", () -> {
            DesktopHelper.openInDefaultApplication(converterSupport.getWorkingDir().resolve("log.txt"));
        }, true, true));
        modal.show();
    }

    private javafx.scene.Node createOptionNode(
            Node n,
            Map<String, String> translations,
            Map<String, String> values) {
        GridPane grid = new GridPane();
        if (n.getNodeForKeyIfExistent("radioSelector").isEmpty()) {
            return grid;
        }

        var h = GuiTooltips.helpNode(translations.get(n.getNodeForKey("tooltip").getString()));
        grid.add(h, 0, 0);

        var t = new Text(translations.get(n.getNodeForKey("displayName").getString()));
        t.setStyle("-fx-font-weight: bold");
        grid.add(t, 1, 0, 3, 1);

        ToggleGroup tg = new ToggleGroup();
        int row = 1;
        String oName = n.getNodeForKey("name").getString();
        for (var ro : n.getNodeForKey("radioSelector").getNodesForKey("radioOption")) {
            String roValue = ro.getNodeForKey("name").getString();
            var btn = new JFXRadioButton(translations.get(ro.getNodeForKey("displayName").getString()));
            btn.setToggleGroup(tg);
            btn.selectedProperty().addListener((c, o, ne) -> {
                values.put(oName, roValue);
            });
            if (values.get(oName) != null && values.get(oName).equals(roValue)) {
                btn.setSelected(true);
            }
            if (values.get(oName) == null && ro.getNodeForKey("default").getString().equals("true")) {
                btn.setSelected(true);
            }

            grid.add(GuiTooltips.helpNode(translations.get(ro.getNodeForKeyIfExistent("tooltip")
                    .map(Node::getString).orElse(""))), 0, row);
            grid.add(btn, 1, row);
            row++;
        }

        grid.setHgap(10);
        grid.setVgap(10);

        return grid;
    }

    public boolean showConfig(Map<String, String> values) {
        Node configNode;
        Map<String, String> translations;
        try {
            configNode = TextFormatParser.text().parse(converterSupport.getBaseDir()
                            .resolve("Configuration").resolve("fronter-options.txt"));
            translations = GameLocalisationHelper.loadTranslations(converterSupport.getBaseDir()
                    .resolve("Configuration").resolve("options.yml"), GameLanguage.ENGLISH);
        } catch (Exception e) {
            ErrorEventFactory.fromThrowable(e).handle();
            return false;
        }

        Alert alert = AppSideWindow.createEmptyAlert();

        ButtonType open = new ButtonType("Open configs");
        alert.getButtonTypes().add(ButtonType.APPLY);
        alert.getButtonTypes().add(open);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        Button val = (Button) alert.getDialogPane().lookupButton(open);
        val.addEventFilter(
                ActionEvent.ACTION,
                e -> {
                    DesktopHelper.browsePath(converterSupport.getBackendDir().resolve("configurables"));
                    e.consume();
                });

        alert.setTitle("Converter settings");
        alert.initModality(Modality.NONE);
        alert.getDialogPane().setMinWidth(500);
        alert.getDialogPane().getStyleClass().add(CLASS_CONTENT_DIALOG);

        VBox options = new VBox();
        for (var node : configNode.getNodesForKey("option")) {
            options.getChildren().add(createOptionNode(node, translations, values));
            options.getChildren().add(new Separator());
        }
        // Remove last separator
        options.getChildren().remove(options.getChildren().size() - 1);
        options.setSpacing(10);

        var sp = new ScrollPane(options);
        sp.setFitToWidth(true);
        sp.setPrefViewportHeight(500);
        alert.getDialogPane().setContent(sp);

        Optional<ButtonType> r = alert.showAndWait();
        return r.isPresent() && r.get().equals(ButtonType.APPLY);
    }
}
