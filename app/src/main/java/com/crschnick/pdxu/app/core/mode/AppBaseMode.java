package com.crschnick.pdxu.app.core.mode;

import com.crschnick.pdxu.app.core.*;
import com.crschnick.pdxu.app.core.beacon.AppBeacon;
import com.crschnick.pdxu.app.core.check.*;
import com.crschnick.pdxu.app.core.window.AppDialog;
import com.crschnick.pdxu.app.core.window.AppMainWindow;
import com.crschnick.pdxu.app.core.window.AppWindowTitle;
import com.crschnick.pdxu.app.installation.GameAppManager;
import com.crschnick.pdxu.app.installation.GameCacheManager;
import com.crschnick.pdxu.app.installation.GameInstallation;
import com.crschnick.pdxu.app.issue.ErrorEventFactory;
import com.crschnick.pdxu.app.issue.TrackEvent;
import com.crschnick.pdxu.app.platform.PlatformInit;
import com.crschnick.pdxu.app.platform.PlatformState;
import com.crschnick.pdxu.app.prefs.AppPrefs;
import com.crschnick.pdxu.app.savegame.SavegameStorage;
import com.crschnick.pdxu.app.savegame.SavegameWatcher;
import com.crschnick.pdxu.app.update.UpdateAvailableDialog;
import com.crschnick.pdxu.app.util.EditorProvider;
import com.crschnick.pdxu.app.util.GlobalTimer;
import com.crschnick.pdxu.app.util.ThreadHelper;
import com.crschnick.pdxu.app.util.WindowsRegistry;
import com.github.kwhat.jnativehook.GlobalScreen;
import org.apache.commons.lang3.SystemUtils;

public class AppBaseMode extends AppOperationMode {

    private boolean initialized;

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public String getId() {
        return "background";
    }

    @Override
    public void onSwitchTo() {
        if (initialized) {
            return;
        }

        // For debugging error handling
        // if (true) throw new IllegalStateException();

        TrackEvent.info("Initializing base mode components ...");
        AppMainWindow.loadingText("initializingApp");
        AppWindowTitle.init();
        AppPathCorruptCheck.check();
        AppWindowsTempCheck.check();
        AppDirectoryPermissionsCheck.checkDirectory(AppSystemInfo.ofCurrent().getTemp());
        WindowsRegistry.init();
        AppJavaOptionsCheck.check();
        AppLayoutModel.init();

        // If we downloaded an update, and decided to no longer automatically update, don't remind us!
        // You can still update manually in the about tab
        if (AppPrefs.get().automaticallyUpdate().get()) {
            UpdateAvailableDialog.showIfNeeded(true);
        }

        AppRosettaCheck.check();
        AppWindowsArmCheck.check();
        AppMainWindow.loadingText("loadingUserInterface");
        PlatformInit.init(true);
        AppImages.init();

        TaskExecutor.getInstance().start();
        AppPrefs.get().determineDefaults();
        GameCacheManager.init();
        GameInstallation.init();
        SavegameStorage.init();
        SavegameManagerState.init();
        AppFileWatcher.init();
        SavegameWatcher.init();
        GameAppManager.init();
        // EditorProvider.get().init();
        registerNativeHook();

        AppMainWindow.initContent();

        TrackEvent.info("Waiting for startup dialogs to close");
        AppDialog.waitForAllDialogsClose();

        AppConfigurationDialog.showIfNeeded();

        TrackEvent.info("Finished base components initialization");
        initialized = true;
    }

    private void registerNativeHook() {
        try {
            if (AppProperties.get().isNativeHookEnabled() && !SystemUtils.IS_OS_MAC) {
                GlobalScreen.registerNativeHook();
            }
        } catch (Throwable ex) {
            ErrorEventFactory.fromThrowable("Unable to register native hook.\n" +
                    "This might be a permissions issue with your system. " +
                    "In-game keyboard shortcuts will be unavailable!" +
                    "\nRestart the Pdx-Unlimiter once the permission issues are fixed to enable in-game shortcuts.", ex).handle();
        }
    }

    private void unregisterNativeHook() {
        try {
            if (AppProperties.get().isNativeHookEnabled() && !SystemUtils.IS_OS_MAC) {
                GlobalScreen.unregisterNativeHook();
            }
        } catch (Throwable ex) {
            ErrorEventFactory.fromThrowable("Unable to unregister native hook.\n" +
                    "This might be a permissions issue with your system.", ex).handle();
        }
    }


    @Override
    public void onSwitchFrom() {}

    @Override
    public void finalTeardown() {
        AppFileWatcher.reset();
        SavegameManagerState.reset();
        GameAppManager.reset();
        SavegameWatcher.reset();
        SavegameStorage.reset();
        GameInstallation.reset();
        unregisterNativeHook();

        TrackEvent.withInfo("Base mode shutdown started").build();
        AppPrefs.reset();
        AppBeacon.reset();
        AppLayoutModel.reset();
        AppTheme.reset();
        PlatformState.reset();
        AppResources.reset();
        AppDataLock.reset();
        GlobalTimer.reset();
        TrackEvent.info("Base mode shutdown finished");
    }
}
