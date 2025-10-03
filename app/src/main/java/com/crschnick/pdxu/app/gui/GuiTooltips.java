package com.crschnick.pdxu.app.gui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Window;
import javafx.util.Duration;

public class GuiTooltips {

    public static Tooltip createTooltip(String text) {
        var tt = new FocusTooltip(text);
        tt.styleProperty().setValue("-fx-font-size: 1em; -fx-background-color: #333333FF;");
        tt.setWrapText(true);
        return tt;
    }

    public static Tooltip createTooltip(Node node) {
        var tt = new FocusTooltip();
        tt.setGraphic(node);
        tt.styleProperty().setValue("-fx-background-color: #333333FF;");
        return tt;
    }

    public static void install(Node node, String text) {
        Platform.runLater(() -> {
            var tt = GuiTooltips.createTooltip(text);
            tt.setShowDelay(Duration.millis(350));
            tt.setShowDuration(Duration.INDEFINITE);
            Tooltip.install(node, tt);
        });
    }

    private static class FocusTooltip extends Tooltip {

        public FocusTooltip() {
        }

        public FocusTooltip(String string) {
            super(string);
        }

        @Override
        protected void show() {
            Window owner = getOwnerWindow();
            if (owner.isFocused())
                super.show();
        }
    }
}
