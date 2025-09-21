package io.abc_def.kickstart_fx.util;

public class Hyperlinks {

    // TODO: Change the links
    public static final String GITHUB = "https://github.com/xpipe-io/kickstartfx";
    public static final String DOCS = "https://kickstartfx.xpipe.io";
    public static final String TRANSLATE = "https://github.com/xpipe-io/kickstartfx/tree/main/lang";

    public static void open(String uri) {
        DesktopHelper.openUrlInBrowser(uri);
    }
}
