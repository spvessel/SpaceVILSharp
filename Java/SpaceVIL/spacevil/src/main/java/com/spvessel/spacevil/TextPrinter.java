package com.spvessel.spacevil;

public class TextPrinter {
    byte[] texture = null;
    int xTextureShift = 0;
    int yTextureShift = 0;
    int widthTexture = 0;
    int heightTexture = 0;

    TextPrinter() {
        texture = null;
    }

    TextPrinter(byte[] bb) {
        texture = bb;
    }
}