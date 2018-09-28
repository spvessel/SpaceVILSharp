package com.spvessel.Items;

public class ImageItem extends VisualItem {
    public ImageItem() {

    }

    public float[] shape() {
        return new float[] { 0, 0, 0 };
    }

    public byte[] getPixMapImage() {
        return new byte[] { 0, 0, 0, 0 };
    }

    public int getImageWidth() {
        return 0;
    }
    public int getImageHeight() {
        return 0;
    }
}