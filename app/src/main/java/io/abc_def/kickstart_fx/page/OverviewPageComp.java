package io.abc_def.kickstart_fx.page;

import io.abc_def.kickstart_fx.comp.SimpleComp;

import javafx.scene.layout.Region;

import atlantafx.sampler.page.showcase.OverviewPage;

public class OverviewPageComp extends SimpleComp {

    @Override
    protected Region createSimple() {
        var atlantaOverview = new OverviewPage();
        return atlantaOverview;
    }
}
