package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceImageItem;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.LinkedList;
import java.nio.ByteBuffer;

public class ImageItem extends Prototype implements InterfaceImageItem {

    private static int count = 0;
    private BufferedImage _image;
    private byte[] _bitmap;
    private String _url;

    private float _angleRotation = 0.0f;

    public void setRotationAngle(float angle) {
        _angleRotation = angle * (float) Math.PI / 180.0f;
    }

    public float getRotationAngle() {
        return _angleRotation;
    }

    public boolean isHover = true;

    @Override
    public boolean getHoverVerification(float xpos, float ypos) {
        if (isHover)
            return super.getHoverVerification(xpos, ypos);
        return false;
    }

    /**
     * Constructs an ImageItem
     */
    public ImageItem() {
        setItemName("Image_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(ImageItem.class));
    }

    /**
     * Constructs an ImageItem with an image
     */
    public ImageItem(BufferedImage picture) {
        this();
        if (picture == null)
            return;
        _image = picture;
        _bitmap = createByteImage();
    }

    /**
     * Constructs an ImageItem with an image and hover attribute
     */
    public ImageItem(BufferedImage picture, boolean hover) {
        this(picture);
        isHover = hover;
    }

    /**
     * Returns the image as byte array
     */
    public byte[] getPixMapImage() {
        if (_image == null)
            return null;

        if (_bitmap == null) {
            _bitmap = createByteImage();
        }
        return _bitmap;
    }

    private byte[] createByteImage() {
        List<Byte> bmp = new LinkedList<Byte>();
        for (int j = _image.getHeight() - 1; j >= 0; j--) {
            for (int i = 0; i < _image.getWidth(); i++) {
                byte[] bytes = ByteBuffer.allocate(4).putInt(_image.getRGB(i, j)).array();
                bmp.add(bytes[1]);
                bmp.add(bytes[2]);
                bmp.add(bytes[3]);
                bmp.add(bytes[0]);
            }
        }
        byte[] result = new byte[bmp.size()];
        int index = 0;
        for (Byte var : bmp) {
            result[index] = var;
            index++;
        }
        return result;
    }

    /**
     * @return width of the image in the ImageItem
     */
    public int getImageWidth() {
        if (_image == null)
            return -1;
        return _image.getWidth();
    }

    /**
     * @return height of the image in the ImageItem
     */
    public int getImageHeight() {
        if (_image == null)
            return -1;
        return _image.getHeight();
    }

    /**
     * @return BufferedImage of the ImageItem
     */
    public BufferedImage getImage() {
        return _image;
    }

    /**
     * Set an image into the ImageItem
     */
    public void setImage(BufferedImage image) {
        if (image == null)
            return;
        _image = image;
        _bitmap = createByteImage();
    }

    /**
     * Image location
     */
    public String getImageUrl() {
        return _url;
    }

    public void setImageUrl(String url) {
        _url = url;
    }

    private Color _colorOverlay;

    public Color getColorOverlay() {
        return _colorOverlay;
    }

    public void setColorOverlay(Color color) {
        _colorOverlay = color;
    }

    public boolean isColorOverLay() {
        if (_colorOverlay != null)
            return true;
        return false;
    }
}
