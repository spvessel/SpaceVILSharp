package CommonVesselGUI;

import CommonVesselGUI.Cores.PixMapData;
import CommonVesselGUI.Items.Frame;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class TextLabel extends ItemText {
    private static int count = 0;

    private Frame _frame;

    private int minLineSpacer;
    private int minFontY;
    private int maxFontY;
    //private boolean oneLine;
    private List<List<Float>> coordArray;
    private float[] lineWidth;

    TextLabel(WindowLayout wnd_handler, String text, Font font) {
        super(wnd_handler, text, font, "TextLabel" + count);
        count++;
        int[] out = FontEngine.getSpacerDims(font);
        minLineSpacer = out[0];
        minFontY = out[1];
        maxFontY = out[2];

        lineSpacer = minLineSpacer;

        //oneLine = !(text.contains("\n"));
    }

    @Override
    void InitElements() {
        _frame = new Frame(wnd);
        addItem(_frame);
    }

    private void createText() {
        String text = getItemText();
        //oneLine = !(text.contains("\n"));
        Font font = getFont();
        String[] line = text.split("\n");
        PixMapData obj;
        coordArray = new LinkedList<>();
        lineWidth = new float[line.length];
        List<Float> alphas = new LinkedList<>();

        int inc = 0;
        for (String textLine : line) {
            obj = FontEngine.getPixMap(textLine, font);
            coordArray.add(inc, obj.getPixels());
            alphas.addAll(obj.getColors());
            lineWidth[inc] = obj.getAlpha();
            inc++;
        }

        setAlphas(alphas);

        addAllShifts();
    }

    private void addAllShifts() {
        List<Float> outRealCoords = new LinkedList<>();
        //System.out.println("font y sizes " + maxFontY + " " + minFontY);
        List<ItemAlignment> alignments = getTextAlignment();
        float alignShiftX = 1;
        float alignShiftY = 0;

        //outRealCoords.addAll(coordArray.get(0));

        //if (!oneLine) {
        float bigSpacer = Math.abs(maxFontY - minFontY) + lineSpacer;

        float addSpace = Math.abs(minFontY); //bigSpacer + alignShiftY; //Math.abs(minFontY) + Math.abs(maxFontY) + lineSpacer;

        for (int i = 0; i < coordArray.size(); i++) {
            if (alignments.contains(ItemAlignment.Right))
                alignShiftX = wnd.getWidth() - lineWidth[i];
            else if (alignments.contains(ItemAlignment.HCenter))
                alignShiftX = (wnd.getWidth() - lineWidth[i]) / 2f;

            if (alignments.contains(ItemAlignment.Bottom))
                alignShiftY = wnd.getHeight() - bigSpacer * lineWidth.length + lineSpacer; //Заезжающий низ здесь уже учтен
            else if (alignments.contains(ItemAlignment.VCenter))
                alignShiftY = (wnd.getHeight() - bigSpacer * lineWidth.length) / 2f;

            for (int j = 0; j < coordArray.get(i).size() / 3; j++) {
                outRealCoords.add(coordArray.get(i).get(j * 3) + alignShiftX);
                outRealCoords.add(coordArray.get(i).get(j * 3 + 1) + addSpace + alignShiftY);
                outRealCoords.add(coordArray.get(i).get(j * 3 + 2));
            }
            addSpace += bigSpacer;
        }
        //}

        setRealCoords(outRealCoords);
    }

    private int lineSpacer;

    void setLineSpacer(int lineSpacer) {
        if ((lineSpacer != this.lineSpacer) && (lineSpacer >= minLineSpacer)) {
            setCoordsFlag(true);
            this.lineSpacer = lineSpacer;
        } //TODO some exception or warning when (lineSpacer < minLineSpacer)
    }

    public int getLineSpacer() {
        return lineSpacer;
    }

    @Override
    void updateData(UpdateType updateType) {
        switch (updateType) {
            case Critical:
                createText();
                break;
            case CoordsOnly:
                addAllShifts();
                break;
        }
    }
}
