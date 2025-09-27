package com.crschnick.pdxu.core;

import com.crschnick.pdxu.core.mode.AppOperationMode;
import com.crschnick.pdxu.util.OsType;
import com.crschnick.pdxu.util.ThreadHelper;

public class AppAotTrain {

    public static void runTrainingMode() throws Throwable {
        // Linux runners don't support graphics
        if (OsType.ofLocal() == OsType.LINUX) {
            return;
        }

        AppOperationMode.switchToSyncOrThrow(AppOperationMode.GUI);

        for (AppLayoutModel.Entry entry : AppLayoutModel.get().getEntries()) {
            AppLayoutModel.get().getSelected().setValue(entry);
            ThreadHelper.sleep(1000);
        }
    }
}
