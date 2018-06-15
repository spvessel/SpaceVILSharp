package CommonVesselGUI;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ItemText extends Primitive {
    //private static int count = 0;
    //private List<Float> realCoords;
    private List<Float> alphas;
    private float[] coordinates;
    private float[] colors;
    private FontEngine fontEngine;
    private String itemText;

    private Font font;

    private boolean criticalFlag = true;
    private boolean coordsFlag = true;
    private boolean colorFlag = true;

    private double alignShiftX = 0;
    private double alignShiftY = 0;

    private float textWidth;
    private float textMinY;
    private float textMaxY;

    ItemText(WindowLayout wnd_handler, String text, Font font, String name) {
        super(wnd_handler, name); //"Text" + count);
        //count++;
        //default
        itemText = text;
        this.font = font;

        //super.setXPosition(100);
        //super.setYPosition(300); //TODO это нижняя левая точка, но не совсем. См. l0.0188.0.

        //setBackground(new Color(255, 255, 255, 255));

        setWidthPolicy(SizePolicy.Fixed);
        setHeightPolicy(SizePolicy.Fixed);
        //----------------------------------------------------------------------------------------

        //updateMainArrays();
    }

    protected void setRealCoords(List<Float> realCoords) {
        coordinates = toGL(realCoords);
    }

    protected void setAlphas(List<Float> alphas) {
        this.alphas = alphas;
        colors = setColor(alphas);
    }

    String getItemText() { return itemText; }
    void setItemText(String itemText) {   //TODO update
        if (!this.itemText.equals(itemText)) {
            this.itemText = itemText;
            criticalFlag = true;
        }
    }

    Font getFont() { return font; }
    void setFont(Font font) {   //TODO update
        if (!this.font.equals(font)) {
            this.font = font;
            criticalFlag = true;
        }
    }

    @Override
    protected void invokePoolEvents() {

    }

    abstract void updateData(UpdateType updateType);

    float[] getCoordinates() {
        if (criticalFlag) {
            updateData(UpdateType.Critical);
            criticalFlag = false;
            coordsFlag = false;
            colorFlag = false;
        }
        else if (coordsFlag) {
            updateData(UpdateType.CoordsOnly);
            coordsFlag = false;
            colorFlag = false;
        }
        else if (colorFlag) {
            setColor(alphas);
            colorFlag = false;
        }
        return coordinates;
    }

    float[] getColors() {
        if (criticalFlag) {
            updateData(UpdateType.Critical);
            criticalFlag = false;
            coordsFlag = false;
            colorFlag = false;
        }
        else if (coordsFlag) {
            updateData(UpdateType.CoordsOnly);
            coordsFlag = false;
            colorFlag = false;
        }
        else if (colorFlag) {
            setColor(alphas);
            colorFlag = false;
        }
        return colors;
    }

    private float[] toGL (List<Float> coord) {
        float[] outCoord = new float[coord.size()];
        float f;
        float x0 = getXPosition();
        float y0 = getYPosition();
        float windowH = getParent().getHeight() / 2f; //wnd.getHeight() / 2;
        float windowW = getParent().getWidth() / 2f; //wnd.getWidth() / 2;
        float xmin = coord.get(0);
        float xmax = coord.get(0);
        float ymin = coord.get(1);
        float ymax = coord.get(1);

        for (int i = 0; i < coord.size(); i += 3) {
            f = coord.get(i) ;
            xmin = (xmin > f) ? f : xmin;
            xmax = (xmax < f) ? f : xmax;
            f += x0;
            f = f / windowW - 1.0f;
            outCoord[i] = f;

            f = coord.get(i + 1);
            ymin = (ymin > f) ? f : ymin;
            ymax = (ymax < f) ? f : ymax;
            f += y0;
            f = -( f / windowH - 1.0f);
            outCoord[i + 1] = f;

            f = coord.get(i + 2);
            outCoord[i + 2] = f;
        }

        setWidth((int)Math.abs(xmax - xmin)); //???
        setHeight((int)Math.abs(ymax - ymin));

        return outCoord;
    }

    private Color foreground = new Color(255, 255, 255, 255); //default
    public Color getForeground() { return foreground; }
    public void setForeground(Color foreground) {
        if (!this.foreground.equals(foreground)) {
            this.foreground = foreground;
            colorFlag = true;
        }
    }

    private float[] setColor(List<Float> alphas) {
        float[] out = new float[alphas.size() * 4];

        Color col = getForeground();
        float r = col.getRed() * 1f / 255f;
        float g = col.getGreen() * 1f / 255f;
        float b = col.getBlue() * 1f / 255f;

        int inc = 0;
        for (float f : alphas) {
            out[inc] = r; inc++;
            out[inc] = g; inc++;
            out[inc] = b; inc++;
            out[inc] = f; inc++;
        }

        return out;
    }

    private List<ItemAlignment> textAlignment = new LinkedList<>();
    public List<ItemAlignment> getTextAlignment() { return textAlignment; }
    public void setTextAlignment(ItemAlignment... value) {
        List<ItemAlignment> list = Arrays.stream(value).collect(Collectors.toList());
        if (!textAlignment.equals(list)) {
            textAlignment = list;
            coordsFlag = true;
        }
    }

    protected void setCoordsFlag(boolean flag) { coordsFlag = flag; }

    enum UpdateType {
        Critical,
        CoordsOnly
    }
}
