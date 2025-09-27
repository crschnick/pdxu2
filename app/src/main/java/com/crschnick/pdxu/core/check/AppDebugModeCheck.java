package com.crschnick.pdxu.core.check;

import com.crschnick.pdxu.core.AppLogs;
import com.crschnick.pdxu.core.AppProperties;
import com.crschnick.pdxu.util.ThreadHelper;

public class AppDebugModeCheck {

    public static void printIfNeeded() {
        if (!AppProperties.get().isImage() || !AppLogs.get().getLogLevel().equals("trace")) {
            return;
        }

        var out = AppLogs.get().getOriginalSysOut();
        var msg =
                """

                  ****************************************
                  * You are running in debug mode!       *
                  * The debug console output can contain *
                  * sensitive information and secrets.   *
                  * Don't share this output via an       *
                  * untrusted website or service.        *
                  ****************************************
                  """;
        out.println(msg);
        ThreadHelper.sleep(1000);
    }
}
