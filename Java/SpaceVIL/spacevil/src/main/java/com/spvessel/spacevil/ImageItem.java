package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.Geometry;
import com.spvessel.spacevil.Core.InterfaceImageItem;
import com.spvessel.spacevil.Core.InterfacePosition;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.LinkedList;
import java.nio.ByteBuffer;

public class ImageItem extends Prototype implements InterfaceImageItem {
    class ImageBounds extends Geometry implements InterfacePosition {
        private int _x, _y;

        public void setX(int x) {
            _x = x;
        }

        public void setY(int y) {
            _y = y;
        }

        public int getX() {
            return _x;
        }

        public int getY() {
            return _y;
        }
    }

    ImageBounds area = new ImageBounds();

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
     * set an image into the ImageItem
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

    private boolean _isKeepAspectRatio = false;

    public void keepAspectRatio(boolean value) {
        _isKeepAspectRatio = value;
    }

    public boolean isAspectRatio() {
        return _isKeepAspectRatio;
    }

    @Override
    public void setSize(int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        area.setHeight(height);
        if (_isKeepAspectRatio && _image != null)
            applyAspectRatio();
        UpdateLayout();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        area.setWidth(width);
        if (_isKeepAspectRatio && _image != null)
            applyAspectRatio();
        UpdateLayout();
    }

    @Override
    public void setX(int _x) {
        super.setX(_x);
        UpdateLayout();
    }

    @Override
    public void setY(int _y) {
        super.setY(_y);
        UpdateLayout();
    }

    private void applyAspectRatio() {
        int w, h;
        float ratioW = (float) _image.getWidth() / (float) _image.getHeight();
        float ratioH = (float) _image.getHeight() / (float) _image.getWidth();
        if (getWidth() > getHeight()) {
            h = getHeight();
            w = (int) ((float) h * ratioW);
            area.setWidth(w);
            area.setHeight(h);
        } else {
            w = getWidth();
            h = (int) ((float) w * ratioH);
            area.setWidth(w);
            area.setHeight(h);
        }
    }

    // self update
    public void UpdateLayout() {
        UpdateVerticalPosition();
        UpdateHorizontalPosition();
    }

    private void UpdateHorizontalPosition() {
        if (getAlignment().contains(ItemAlignment.LEFT)) {
            area.setX(getX());
        } else if (getAlignment().contains(ItemAlignment.RIGHT)) {
            area.setX(getX() + getWidth() - area.getWidth());
        } else if (getAlignment().contains(ItemAlignment.HCENTER)) {
            int x = getX();
            int w = area.getWidth();
            area.setX((getWidth() - w) / 2 + x);
        }
    }

    private void UpdateVerticalPosition() {
        if (getAlignment().contains(ItemAlignment.TOP)) {
            area.setY(getY());
        } else if (getAlignment().contains(ItemAlignment.BOTTOM)) {
            area.setY(getY() + getHeight() - area.getHeight());
        } else if (getAlignment().contains(ItemAlignment.VCENTER)) {
            int y = getY();
            int h = area.getHeight();
            area.setY((getHeight() - h) / 2 + y);
        }
    }
}
