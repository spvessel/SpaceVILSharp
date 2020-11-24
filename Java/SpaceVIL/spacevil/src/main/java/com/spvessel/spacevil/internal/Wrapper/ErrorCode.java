package com.spvessel.spacevil.internal.Wrapper;

public final class ErrorCode {
    private ErrorCode() {
    }

    public static final int NotInitialized = 0x00010001;
    public static final int NoCurrentContext = 0x00010002;
    public static final int InvalidEnum = 0x00010003;
    public static final int InvalidValue = 0x00010004;
    public static final int OutOfMemory = 0x00010005;
    public static final int ApiUnavailable = 0x00010006;
    public static final int VersionUnavailable = 0x00010007;
    public static final int PlatformError = 0x00010008;
    public static final int FormatUnavailable = 0x00010009;
    public static final int NoWindowContext = 0x0001000A;
}
