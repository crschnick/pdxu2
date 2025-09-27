package com.crschnick.pdxu.update;

import com.crschnick.pdxu.comp.Comp;
import com.crschnick.pdxu.comp.base.MarkdownComp;
import com.crschnick.pdxu.comp.base.ModalOverlay;
import com.crschnick.pdxu.issue.TrackEvent;

public class UpdateAvailableDialog {

    public static void showIfNeeded(boolean wait) {
        UpdateHandler uh = AppDistributionType.get().getUpdateHandler();
        if (uh.getLastUpdateCheckResult().getValue() == null) {
            return;
        }

        // Check whether we still have the latest version prepared
        uh.refreshUpdateCheckSilent();
        if (uh.getLastUpdateCheckResult().getValue() == null) {
            return;
        }

        TrackEvent.withInfo("Showing update alert ...")
                .tag("version", uh.getLastUpdateCheckResult().getValue().getVersion())
                .handle();
        var u = uh.getLastUpdateCheckResult().getValue();

        var comp = Comp.of(() -> {
            var markdown = new MarkdownComp(u.getBody() != null ? u.getBody() : "", s -> s, false).createRegion();
            return markdown;
        });
        var modal = ModalOverlay.of("updateReadyAlertTitle", comp.prefWidth(600), null);
        for (var action : uh.createActions()) {
            modal.addButton(action);
        }

        if (wait) {
            modal.showAndWait();
        } else {
            modal.show();
        }
    }
}
