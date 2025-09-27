package com.crschnick.pdxu.core;

import com.crschnick.pdxu.core.mode.AppOperationMode;
import com.crschnick.pdxu.util.LocalExec;
import com.crschnick.pdxu.util.OsType;

import java.util.List;

public class AppRestart {

    private static String[] createLaunchCommand(List<String> arguments) {
        var loc = AppProperties.get().isDevelopmentEnvironment()
                ? AppInstallation.ofDefault()
                : AppInstallation.ofCurrent();
        var suffix = (arguments.size() > 0 ? " " + String.join(" ", arguments) : "");
        if (OsType.ofLocal() == OsType.LINUX) {
            return new String[] {
                "sh",
                "-c",
                "nohup \"" + loc.getDaemonExecutablePath() + "\"" + suffix + " </dev/null >/dev/null 2>&1 & disown"
            };
        } else if (OsType.ofLocal() == OsType.MACOS) {
            return new String[] {
                "sh",
                "-c",
                "(sleep 1;open \"" + loc.getBaseInstallationPath() + "\" --args" + suffix
                        + " </dev/null &>/dev/null) & disown"
            };
        } else {
            var exe = loc.getDaemonExecutablePath();
            var base = "\"" + exe + "\"" + suffix;
            return new String[] {"cmd", "/c", "start \"\" " + base};
        }
    }

    public static void restart() {
        AppOperationMode.executeAfterShutdown(() -> {
            LocalExec.executeAsync(createLaunchCommand(List.of()));
        });
    }
}
