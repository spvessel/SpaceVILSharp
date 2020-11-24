package com.spvessel.spacevil.internal.Wrapper;

public final class ContextRobustness {
    private ContextRobustness() {
    }

    public static final int None = 0;
    public static final int NoResetNotification = 0x00031001;
    public static final int LoseContextOnReset = 0x00031002;
}
