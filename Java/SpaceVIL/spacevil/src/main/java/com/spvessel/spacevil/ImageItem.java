package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceImageItem;
import com.spvessel.spacevil.Core.RectangleBounds;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;
import java.nio.ByteBuffer;

public class ImageItem extends Prototype implements InterfaceImageItem {

    private RectangleBounds area = new RectangleBounds();

    public RectangleBounds getRectangleBounds() {
        return area;
    }

    private static int count = 0;
    private byte[] _bitmap;

    private float _angleRotation = 0.0f;

    public void setRotationAngle(float angle) {
        _angleRotation = angle * (float) Math.PI / 180.0f;
    }

    public float getRotationAngle() {
        return _angleRotation;
    }

    public boolean isHover = false;

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
        _bitmap = createByteImage(picture);
        picture.flush();
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
        return _bitmap;
    }

    private byte[] createByteImage(BufferedImage picture) {
        try {
            _imageWidth = picture.getWidth();
            _imageHeight = picture.getHeight();
            List<Byte> bmp = new LinkedList<Byte>();
            for (int j = picture.getHeight() - 1; j >= 0; j--) {
                for (int i = 0; i < picture.getWidth(); i++) {
                    byte[] bytes = ByteBuffer.allocate(4).putInt(picture.getRGB(i, j)).array();
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
            setNew(true);
            return result;
        } catch (Exception ex) {
            System.out.println("Create byte image");
            ex.printStackTrace();
            return null;
        }
    }

    private int _imageWidth;
    private int _imageHeight;

    /**
     * @return width of the image in the ImageItem
     */
    public int getImageWidth() {
        return _imageWidth;
    }

    /**
     * @return height of the image in the ImageItem
     */
    public int getImageHeight() {
        return _imageHeight;
    }

    /**
     * set an image into the ImageItem
     */
    public void setImage(BufferedImage image) {
        if (image == null)
            return;
        _bitmap = createByteImage(image);
        image.flush();
        if (_isKeepAspectRatio && _bitmap != null)
            applyAspectRatio();
        UpdateLayout();
    }

    private boolean _isOverlay = false;
    private Color _colorOverlay;

    public Color getColorOverlay() {
        return _colorOverlay;
    }

    public void setColorOverlay(Color color) {
        _colorOverlay = color;
        _isOverlay = true;
    }

    public void setColorOverlay(Color color, boolean overlay) {
        _colorOverlay = color;
        _isOverlay = overlay;
    }
    
    public void setColorOverlay(boolean overlay) {
        _isOverlay = overlay;
    }

    public boolean isColorOverlay() {
        return _isOverlay;
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
        if (_isKeepAspectRatio && _bitmap != null)
            applyAspectRatio();
        UpdateLayout();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        area.setWidth(width);
        if (_isKeepAspectRatio && _bitmap != null)
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
        float boundW = getWidth();
        float boundH = getHeight();

        float ratioX = (boundW / _imageWidth);
        float ratioY = (boundH / _imageHeight);
        float ratio = ratioX < ratioY ? ratioX : ratioY;

        int resH = (int) (_imageHeight * ratio);
        int resW = (int) (_imageWidth * ratio);
        area.setWidth(resW);
        area.setHeight(resH);
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

    @Override
    public void release() {
        VRAMStorage.addToDelete(this);
    }

    private Lock _lock = new ReentrantLock();
    private boolean _isNew = true;

    boolean isNew() {
        _lock.lock();
        try {
            return _isNew;
        } finally {
            _lock.unlock();
        }
    }

    void setNew(boolean value) {
        _lock.lock();
        try {
            _isNew = value;
        } finally {
            _lock.unlock();
        }
    }
}
