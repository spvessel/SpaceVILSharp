package com.spvessel.Cores;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class TextPrinter {
    /*
    public byte[] _letterTexture;
    public int xshift = 0;
    public int yshift = 0;
    public int letWidth = 0;
    public int letHeight = 0;
    public int yWinShift = 0;
*/
    public TextPrinter() {
        //this(new byte[]{});
        texture = null;
    }
/*
    public TextPrinter(byte[] list) {
        _letterTexture = list;
    }
    */

    public TextPrinter(ByteBuffer bb) {
        texture = bb;
    }

    public ByteBuffer texture = null;
    public int xTextureShift = 0;
    public int yTextureShift = 0;
}