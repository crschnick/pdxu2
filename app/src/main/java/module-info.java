import com.crschnick.pdxu.core.AppLogs;
import com.crschnick.pdxu.util.AppJacksonModule;
import com.crschnick.pdxu.util.ModuleLayerLoader;

import com.fasterxml.jackson.databind.Module;
import org.slf4j.spi.SLF4JServiceProvider;

open module com.crschnick.pdxu {
    exports com.crschnick.pdxu.core;
    exports com.crschnick.pdxu.util;
    exports com.crschnick.pdxu;
    exports com.crschnick.pdxu.issue;
    exports com.crschnick.pdxu.comp.base;
    exports com.crschnick.pdxu.core.mode;
    exports com.crschnick.pdxu.prefs;
    exports com.crschnick.pdxu.update;
    exports com.crschnick.pdxu.core.check;
    exports com.crschnick.pdxu.core.window;
    exports com.crschnick.pdxu.comp;
    exports com.crschnick.pdxu.platform;
    exports com.crschnick.pdxu.page;
    exports com.crschnick.pdxu.core.beacon;

    requires static lombok;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires org.slf4j;
    requires org.slf4j.jdk.platform.logging;
    requires atlantafx.base;
    requires com.vladsch.flexmark;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires io.xpipe.modulefs;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.web;
    requires javafx.graphics;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires io.sentry;
    requires info.picocli;
    requires java.instrument;
    requires java.management;
    requires jdk.management;
    requires jdk.management.agent;
    requires java.net.http;
    requires org.jetbrains.annotations;
    requires org.kohsuke.github;

    // Required runtime modules
    requires jdk.charsets;
    requires jdk.crypto.cryptoki;
    requires jdk.localedata;
    requires jdk.accessibility;
    requires org.kordamp.ikonli.material2;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.kordamp.ikonli.bootstrapicons;
    requires org.kordamp.ikonli.feather;
    requires jdk.zipfs;

    // AtlantaFX Sampler modules
    requires atlantafx.sampler;
    requires datafaker;
    requires javafx.fxml;

    // Monkey tester stuff
    requires monkey_tester;
    requires javafx.swing;

    uses ModuleLayerLoader;
    uses Module;

    provides Module with
            AppJacksonModule;
    provides SLF4JServiceProvider with
            AppLogs.Slf4jProvider;
}
