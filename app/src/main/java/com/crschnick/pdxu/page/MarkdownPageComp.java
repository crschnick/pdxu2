package com.crschnick.pdxu.page;

import com.crschnick.pdxu.comp.SimpleComp;
import com.crschnick.pdxu.comp.base.MarkdownComp;
import com.crschnick.pdxu.core.AppResources;
import com.crschnick.pdxu.issue.ErrorEventFactory;

import javafx.scene.layout.Region;

import java.nio.file.Files;
import java.util.function.UnaryOperator;

public class MarkdownPageComp extends SimpleComp {

    @Override
    protected Region createSimple() {
        try {
            var md = AppResources.getResourcePath(AppResources.MAIN_MODULE, "misc/markdown.md")
                    .orElseThrow();
            var string = Files.readString(md);
            return new MarkdownComp(string, UnaryOperator.identity(), true).createRegion();
        } catch (Exception e) {
            ErrorEventFactory.fromThrowable(e).handle();
            return null;
        }
    }
}
