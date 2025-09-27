package com.crschnick.pdxu.util;

@FunctionalInterface
public interface FailableRunnable<E extends Throwable> {

    void run() throws E;
}
