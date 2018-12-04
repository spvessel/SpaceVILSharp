package com.spvessel;

import java.nio.ByteBuffer;

public class TextPrinter {
    ByteBuffer texture = null;
    int xTextureShift = 0;
    int yTextureShift = 0;
    int widthTexture = 0;
    int heightTexture = 0;

    TextPrinter() {
        texture = null;
    }

    TextPrinter(ByteBuffer bb) {
        texture = bb;
    }
}