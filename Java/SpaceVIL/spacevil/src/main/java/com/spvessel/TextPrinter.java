package com.spvessel;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class TextPrinter {
    public ByteBuffer texture = null;
    public int xTextureShift = 0;
    public int yTextureShift = 0;
    public int widthTexture = 0;
    public int heightTexture = 0;

    public TextPrinter() {
        texture = null;
    }

    public TextPrinter(ByteBuffer bb) {
        texture = bb;
    }
}