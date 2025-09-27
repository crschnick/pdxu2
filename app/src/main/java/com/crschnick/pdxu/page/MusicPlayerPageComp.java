package com.crschnick.pdxu.page;

import com.crschnick.pdxu.comp.SimpleComp;

import javafx.scene.layout.Region;

import atlantafx.sampler.page.showcase.musicplayer.MusicPlayerPage;

public class MusicPlayerPageComp extends SimpleComp {

    @Override
    protected Region createSimple() {
        var atlantaMusicPlayer = new MusicPlayerPage();
        return atlantaMusicPlayer;
    }
}
