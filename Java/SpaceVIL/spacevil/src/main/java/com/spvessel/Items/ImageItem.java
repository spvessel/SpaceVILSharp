package com.spvessel.Items;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.LinkedList;
import java.nio.ByteBuffer;

import com.spvessel.Cores.InterfaceImageItem;

public class ImageItem extends VisualItem implements InterfaceImageItem {

    static int count = 0;
    private BufferedImage _image;
    private byte[] _bitmap;
    private String _url;

    public ImageItem() {
        setItemName("Image_" + count);
        count++;
    }

    public ImageItem(BufferedImage picture) {
        this();
        if (picture == null)
            return;
        _image = picture;
        _bitmap = createByteImage();
    }

    public byte[] getPixMapImage() {
        if (_image == null)
            return null;

        if (_bitmap == null) {
            _bitmap = createByteImage();
        }
        return _bitmap;
    }

    private byte[] createByteImage() {
        List<Byte> bmp = new LinkedList<>();
        for (int i = 0; i < _image.getWidth(); i++) {
            for (int j = 0; j < _image.getHeight(); j++) {
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

    public int getImageWidth() {
        if (_image == null)
            return -1;
        return _image.getWidth();
    }

    public int getImageHeight() {
        if (_image == null)
            return -1;
        return _image.getHeight();
    }

    public BufferedImage getImage() {
        return _image;
    }

    public void setImage(BufferedImage image) {
        _image = image;
    }

    public String getImageUrl() {
        return _url;
    }

    public void setImageUrl(String url) {
        _url = url;
    }
}