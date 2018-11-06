package com.spvessel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FontEngine {
    /*
    static public FontEngine() {

    }
    */
    static String _preloadDefFile = "./somefile.dat";

    static Map<Font, Alphabet> fonts = new HashMap<>();

    public static List<Alphabet.ModifyLetter> getPixMap(String text, Font font) // PixMapData
    {
        //return FontReview.getTextArrays(text, font);
        if (!fonts.containsKey(font)) {
            fonts.put(font, new Alphabet(font));
        }

        return fonts.get(font).makeTextNew(text);// MakeText(text);
    }

    public static int[] getSpacerDims(Font font) {
        //return FontReview.getDims();

        if (!fonts.containsKey(font)) {
            fonts.put(font, new Alphabet(font));
        }
        Alphabet a = fonts.get(font);
        return new int[]{a.lineSpacer, a.alphMinY, a.alphHeight};
    }

    static boolean savePreloadFont(Font font) {
        if (!fonts.containsKey(font)) {
            fonts.put(font, new Alphabet(font));
        }

        fonts.get(font).addMoreLetters(); //Заполнить весь алфавит
        //Сохранить в файл или куда там
        return true;
    }
}