package com.crschnick.pdxu.core;

import com.crschnick.pdxu.Main;
import com.crschnick.pdxu.core.mode.AppOperationMode;
import com.crschnick.pdxu.issue.ErrorEventFactory;
import com.crschnick.pdxu.platform.PlatformState;
import com.crschnick.pdxu.util.OsType;

import java.awt.*;
import java.awt.desktop.*;
import java.util.List;
import javax.imageio.ImageIO;

public class AppDesktopIntegration {

    public static void init() {
        try {
            // This will initialize the toolkit on macOS and create the dock icon
            // macOS does not like applications that run fully in the background, so always do it
            if (OsType.ofLocal() == OsType.MACOS && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().setPreferencesHandler(e -> {
                    if (PlatformState.getCurrent() != PlatformState.RUNNING) {
                        return;
                    }

                    if (AppLayoutModel.get() != null) {
                        AppLayoutModel.get().selectFileBrowser();
                    }
                });

                // URL open operations have to be handled in a special way on macOS!
                Desktop.getDesktop().setOpenURIHandler(e -> {
                    AppOpenArguments.handle(List.of(e.getURI().toString()));
                });

                Desktop.getDesktop().addAppEventListener(new AppReopenedListener() {
                    @Override
                    public void appReopened(AppReopenedEvent e) {
                        AppOperationMode.switchToAsync(AppOperationMode.GUI);
                    }
                });
            }
        } catch (Throwable ex) {
            ErrorEventFactory.fromThrowable(ex).term().handle();
        }
    }
}
