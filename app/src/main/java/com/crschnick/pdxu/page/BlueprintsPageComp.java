package com.crschnick.pdxu.page;

import com.crschnick.pdxu.comp.SimpleComp;

import javafx.scene.layout.Region;

import atlantafx.sampler.page.showcase.BlueprintsPage;

public class BlueprintsPageComp extends SimpleComp {

    @Override
    protected Region createSimple() {
        var atlantaBlueprints = new BlueprintsPage();
        return atlantaBlueprints;
    }
}
