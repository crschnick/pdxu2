package io.abc_def.kickstart_fx;

import io.abc_def.kickstart_fx.core.AppInit;
import io.abc_def.kickstart_fx.core.AppProperties;

public class Main {

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("version")) {
            AppProperties.init(args);
            System.out.println(AppProperties.get().getVersion());
            return;
        }

        AppInit.init(args);
    }
}
