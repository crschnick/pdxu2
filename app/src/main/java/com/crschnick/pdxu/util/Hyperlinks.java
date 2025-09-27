package com.crschnick.pdxu.util;

public class Hyperlinks {

    public static final String GITHUB = "https://github.com/crschnick/pdx_unlimiter";
    public static final String DOCS = "https://github.com/crschnick/pdx_unlimiter/wiki";
    public static final String TRANSLATE = "https://github.com/crschnick/pdx_unlimiter/tree/main/lang";

    public static void open(String uri) {
        DesktopHelper.openUrlInBrowser(uri);
    }
}
