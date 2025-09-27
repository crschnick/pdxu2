package com.crschnick.pdxu.comp.base;

import com.crschnick.pdxu.comp.SimpleComp;

import lombok.Getter;

@Getter
public abstract class ModalOverlayContentComp extends SimpleComp {

    protected ModalOverlay modalOverlay;

    void setModalOverlay(ModalOverlay modalOverlay) {
        this.modalOverlay = modalOverlay;
    }
}
