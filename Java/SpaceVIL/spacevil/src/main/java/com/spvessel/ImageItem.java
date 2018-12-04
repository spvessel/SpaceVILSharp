package com.spvessel;

import com.spvessel.Core.InterfaceImageItem;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.LinkedList;
import java.nio.ByteBuffer;

public class ImageItem extends Prototype implements InterfaceImageItem {

    private static int count = 0;
    private BufferedImage _image;
    private byte[] _bitmap;
    private String _url;

    /**
     * Constructs an ImageItem
     */
    public ImageItem() {
        setItemName("Image_" + count);
        count++;
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
}
