package com.crschnick.pdxu.app.gui.dialog;

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
        Alert alert = AppSideWindow.createEmptyAlert();

        var config = new ButtonType("Configure");
        alert.getButtonTypes().add(config);

        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm conversion");
        alert.setHeaderText(String.format("""
                                                  Do you want to convert the selected %s savegame to an %s mod?
                                                  """, converterSupport.getFromName(), converterSupport.getToName()));
        alert.setContentText("""
                This conversion may take a while.
                """);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get().equals(config);
    }

    public void showUsageDialog() {
        Alert alert = AppSideWindow.createEmptyAlert();

        var download = new ButtonType("Show downloads");
        alert.getButtonTypes().add(download);
        Button val = (Button) alert.getDialogPane().lookupButton(download);
        val.setOnAction(e -> {
            Hyperlinks.open(converterSupport.getDownloadLink());
        });

        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle(converterSupport.getFromName() + " to " + converterSupport.getToName() + " converter");
        alert.setHeaderText("""
                To use the converter functionality, you first have to download the converter,
                extract it, and then set the path to the extracted directory in the settings menu.
                """);
        alert.showAndWait();
    }

    public void showAlreadyExistsDialog(String name) {
        AppSideWindow.showBlockingAlert(alert -> {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("Mod already exists");
            alert.setHeaderText("A converted mod with the name " + name + " already exists in your mod directory.");
        });
    }

    public void showConversionSuccessDialog() {
        Alert alert = AppSideWindow.createEmptyAlert();
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Conversion succeeded");
        alert.setHeaderText("""
                The Converter has finished successfully.
                """);
        alert.setContentText("""
                The created mod has been added to your mod directory.
                To play it, simply enable the mod your the Paradox launcher.
                """);
        alert.showAndWait();
    }

    public void showConversionErrorDialog() {
        Alert alert = AppSideWindow.createEmptyAlert();
        var openLog = new ButtonType("Open log");
        alert.getButtonTypes().add(openLog);
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle("Conversion failed");
        alert.setHeaderText("""
                The converter returned an error.
                """);
        alert.setContentText("""
                If you want to diagnose the error, you can take a look at the log file of the converter.
                """);
        Button val = (Button) alert.getDialogPane().lookupButton(openLog);
        val.setOnAction(e -> {
            DesktopHelper.openInDefaultApplication(converterSupport.getWorkingDir().resolve("log.txt"));
        });
        alert.showAndWait();
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
