package com.spacevil;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class FontEngine {
    /*
    static public FontEngine() {

    }
    */
    static String _preloadDefFile = "./somefile.dat";

    private static Map<Font, Alphabet> fonts = new HashMap<>();

    private static Lock fontLock = new ReentrantLock();

    static List<Alphabet.ModifyLetter> getModifyLetters(String text, Font font) // PixMapData
    {
        //return FontReview.getTextArrays(text, font);
        if (!fonts.containsKey(font)) {
            fontLock.lock();
            try {
                if (!fonts.containsKey(font))
                    fonts.put(font, new Alphabet(font));
            } finally {
                fontLock.unlock();
            }
        }

        return fonts.get(font).makeTextNew(text);// MakeText(text);
    }

    static int[] getSpacerDims(Font font) {
        //return FontReview.getDims();

        if (!fonts.containsKey(font)) {
            fontLock.lock();
            try {
                if (!fonts.containsKey(font))
                    fonts.put(font, new Alphabet(font));
            } finally {
                fontLock.unlock();
            }
        }
        Alphabet a = fonts.get(font);
        return new int[]{a.lineSpacer, a.alphMinY, a.alphHeight};
    }

    static boolean savePreloadFont(Font font) {
        if (!fonts.containsKey(font)) {
            fontLock.lock();
            try {
                if (!fonts.containsKey(font))
                    fonts.put(font, new Alphabet(font));
            } finally {
                fontLock.unlock();
            }
        }

        fonts.get(font).addMoreLetters(); //Заполнить весь алфавит
        //Сохранить в файл или куда там
        return true;
    }
}