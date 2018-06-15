package CommonVesselGUI.Cores;

import java.util.List;

public class PixMapData {
    private List<Float> pixels;
    private List<Float> colors;
    private float alpha;

    public PixMapData(List<Float> pixels, List<Float> colors, float alpha) {
        this.pixels = pixels;
        this.colors = colors;
        this.alpha = alpha;
    }

    public List<Float> getPixels() {
        return pixels;
    }

    public List<Float> getColors() {
        return colors;
    }

    public float getAlpha() {
        return alpha;
    }
}
