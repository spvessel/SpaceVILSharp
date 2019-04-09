package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Core.TextInputArgs;
import com.spvessel.spacevil.Flags.InputRestriction;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.MouseButton;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

class TextEditRestricted extends TextEdit {
    TextEditRestricted() {
        //super();
        eventTextInput.clear();
        eventTextInput.add(this::onTextInput);

        //EventKeyMethodState tmp = eventKeyPress;
        //eventKeyPress.clear();
        eventKeyPress.add(this::onKeyPress);
        //eventKeyPress.add(tmp);

        eventMouseDoubleClick.clear();
        eventMouseDoubleClick.add(this::onMouseDoubleClick);

        numbers = new LinkedList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"));

        updateCurrentValue();
        setEditable(false);
    }

    private List<String> numbers;
    private InputRestriction inres = InputRestriction.DOUBLENUMBERS;

    private void onMouseDoubleClick(Object sender, MouseArgs args) {
        if (args.button == MouseButton.BUTTON_LEFT) {
            selectAll();
            setEditable(true);
        }
    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.key == KeyCode.ENTER || args.key == KeyCode.NUMPADENTER) {
            double znc;
            int i1, sgc;
            String t0 = getText();
            if (!t0.equals("-") && t0.length() > 0) {
                String[] txt = t0.split(",|\\.");

                try {
                    znc = Integer.parseInt(txt[0]);
                    if (txt.length > 1 && txt[1].length() > 0) {
                        i1 = Integer.parseInt(txt[1]);
                        sgc = txt[1].length();
                        znc += i1 / Math.pow(10.0, sgc);
//                    if (sgc > signsCount && sgc <= 5) {
//                        signsCount = sgc;
//                        rou = "%." + String.valueOf(signsCount) + "f";
//                    }
                    }
                    currentValue = znc;
                } catch (Exception e) {

                }
            }
            updateCurrentValue();
            setEditable(false);
        }
    }

    private void onTextInput(Object sender, TextInputArgs args) {
        String tmptxt = getText();
        boolean isFirst = (isBeginning() && !tmptxt.contains("-"));
        boolean hasDot = (tmptxt.contains(".") || tmptxt.contains(","));
        byte[] input = ByteBuffer.allocate(4).putInt(args.character).array();
        String str = new String(input, Charset.forName("UTF-32"));
        boolean isValid = false;
        switch (inres) {
            case INTNUMBERS:
                if (numbers.contains(str))
                    isValid = true;
                else if (isFirst && str.equals("-"))
                    isValid = true;
                else isValid = false;
                break;
            case DOUBLENUMBERS:
                if (numbers.contains(str))
                    isValid = true;
                else if (isFirst && str.equals("-"))
                    isValid = true;
                else if (!isFirst && !hasDot && (str.equals(".") || str.equals(",")))
                    isValid = true;
                else isValid = false;
                break;
            default:
                isValid = true;
                break;
        }

        if (isValid) {
            super.cutText();
            super.pasteText(str);
        } else {
            super.pasteText("");
        }


    }

    void setInputRestriction(InputRestriction ir) {
        inres = ir;
    }

    private double minValue = -100;
    private double maxValue = 100;
    private double currentValue = 0;
    private double step = 1;
    private String rou = "%.2f";
    private int signsCount = 2;

    void setParameters(double currentValue, double minValue, double maxValue, double step) {
        this.currentValue = currentValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
        inres = InputRestriction.DOUBLENUMBERS;
        updateCurrentValue();

        String[] splitter = String.valueOf(currentValue).split("\\.");
        int i = 0;
        if (splitter.length > 1)
            i = splitter[1].length();
        splitter = String.valueOf(minValue).split("\\.");
        if (splitter.length > 1 && i < splitter[1].length())
            i = splitter[1].length();
        splitter = String.valueOf(maxValue).split("\\.");
        if (splitter.length > 1 && i < splitter[1].length())
            i = splitter[1].length();
        splitter = String.valueOf(step).split("\\.");
        if (splitter.length > 1 && i < splitter[1].length())
            i = splitter[1].length();
        if (i < 2)
            i = 2;
        else if (i > 5) i = 5;
        signsCount = i;
        rou = "%." + String.valueOf(signsCount) + "f";
    }

    void setParameters(int currentValue, int minValue, int maxValue, int step) {
        this.currentValue = currentValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
        inres = InputRestriction.INTNUMBERS;
        updateCurrentValue();
    }

    public void setValue(int value) {
        currentValue = value;
        updateCurrentValue();
    }

    public void setValue(double value) {
        currentValue = value;
        inres = InputRestriction.DOUBLENUMBERS;
        updateCurrentValue();
    }

    public double getValue()
    {
        return currentValue;
    }

    public void setAccuracy(int accuracy) {
        signsCount = accuracy;
        rou = "%." + String.valueOf(signsCount) + "f";
    }

    private void updateCurrentValue() {
        if (currentValue < minValue)
            currentValue = minValue;
        if (currentValue > maxValue)
            currentValue = maxValue;

        switch (inres) {
            case INTNUMBERS:
                setText(Integer.toString((int) currentValue));
                break;
            default: //case DOUBLENUMBERS:
                setText(String.format(Locale.ROOT, rou, currentValue));
                break;
        }
    }

    void increaseValue() {
        setEditable(false);
        currentValue += step;
        updateCurrentValue();
    }

    void decreaseValue() {
        setEditable(false);
        currentValue -= step;
        updateCurrentValue();
    }
}
