package com.crschnick.pdxu.app.core;

import com.crschnick.pdxu.app.comp.Comp;
import com.crschnick.pdxu.app.gui.GuiLayout;
import com.crschnick.pdxu.app.page.*;
import com.crschnick.pdxu.app.page.PrefsPageComp;
import com.crschnick.pdxu.app.platform.LabelGraphic;
import com.crschnick.pdxu.app.platform.PlatformThread;
import com.crschnick.pdxu.app.util.GlobalTimer;
import com.crschnick.pdxu.app.util.Hyperlinks;
import com.crschnick.pdxu.app.util.ThreadHelper;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import lombok.*;
import lombok.experimental.NonFinal;
import lombok.extern.jackson.Jacksonized;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AppLayoutModel {

    private static AppLayoutModel INSTANCE;

    private final SavedState savedState;

    private final List<Entry> entries;

    private final Property<Entry> selected;

    private final ObservableList<QueueEntry> queueEntries;

    private final BooleanProperty ptbAvailable = new SimpleBooleanProperty();

    public AppLayoutModel(SavedState savedState) {
        this.savedState = savedState;
        this.entries = createEntryList();
        this.selected = new SimpleObjectProperty<>(entries.getFirst());
        this.queueEntries = FXCollections.observableArrayList();
    }

    public static AppLayoutModel get() {
        return INSTANCE;
    }

    public static void init() {
        var state = AppCache.getNonNull("layoutState", SavedState.class, () -> new SavedState(270, 300));
        INSTANCE = new AppLayoutModel(state);
    }

    public static void reset() {
        if (INSTANCE == null) {
            return;
        }

        AppCache.update("layoutState", INSTANCE.savedState);
        INSTANCE = null;
    }

    public synchronized void showQueueEntry(QueueEntry entry, Duration duration, boolean allowDuplicates) {
        if (!allowDuplicates && queueEntries.contains(entry)) {
            return;
        }

        queueEntries.add(entry);
        if (duration != null) {
            GlobalTimer.delay(
                    () -> {
                        synchronized (this) {
                            queueEntries.remove(entry);
                        }
                    },
                    duration);
        }
    }

    public void selectBlueprints() {
        PlatformThread.runLaterIfNeeded(() -> {
            selected.setValue(entries.getFirst());
        });
    }

    public void selectMusicPlayer() {
        PlatformThread.runLaterIfNeeded(() -> {
            selected.setValue(entries.get(1));
        });
    }

    public void selectFileBrowser() {
        PlatformThread.runLaterIfNeeded(() -> {
            selected.setValue(entries.get(2));
        });
    }

    public void selectOverview() {
        PlatformThread.runLaterIfNeeded(() -> {
            selected.setValue(entries.get(3));
        });
    }

    public void selectMarkdown() {
        PlatformThread.runLaterIfNeeded(() -> {
            selected.setValue(entries.get(4));
        });
    }

    public void selectDeveloper() {
        PlatformThread.runLaterIfNeeded(() -> {
            selected.setValue(entries.get(6));
        });
    }

    public void selectSettings() {
        PlatformThread.runLaterIfNeeded(() -> {
            selected.setValue(entries.get(7));
        });
    }

    private List<Entry> createEntryList() {
        var l = new ArrayList<>(List.of(
                new Entry(
                        AppI18n.observable("settings"),
                        new LabelGraphic.IconGraphic("mdsmz-miscellaneous_services"),
                        Comp.of(() -> {
                            var gl = new GuiLayout();
                            gl.setup();
                            gl.finishSetup();
                            return gl.getContent();
                        }),
                        null),
                new Entry(
                        AppI18n.observable("settings"),
                        new LabelGraphic.IconGraphic("mdsmz-miscellaneous_services"),
                        new PrefsPageComp(),
                        null),
                new Entry(
                        AppI18n.observable("docs"),
                        new LabelGraphic.IconGraphic("mdi2b-book-open-variant"),
                        null,
                        () -> Hyperlinks.open(Hyperlinks.DOCS)),
                new Entry(
                        AppI18n.observable("visitGithubRepository"),
                        new LabelGraphic.IconGraphic("mdi2g-github"),
                        null,
                        () -> Hyperlinks.open(Hyperlinks.GITHUB))));
        return l;
    }

    @Data
    @Builder
    @Jacksonized
    public static class SavedState {

        double sidebarWidth;
        double browserConnectionsWidth;
    }

    public record Entry(ObservableValue<String> name, LabelGraphic icon, Comp<?> comp, Runnable action) {}

    @Value
    @NonFinal
    public static class QueueEntry {

        ObservableValue<String> name;
        LabelGraphic icon;
        Runnable action;

        public void show() {
            ThreadHelper.runAsync(() -> {
                try {
                    getAction().run();
                } finally {
                    AppLayoutModel.get().getQueueEntries().remove(this);
                }
            });
        }
    }
}
