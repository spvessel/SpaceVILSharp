package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceImageItem;
import com.spvessel.spacevil.Core.Area;
import com.spvessel.spacevil.Flags.ImageQuality;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * ImageItem is class for rendering loaded images.
 * <p>
 * Supports all events except drag and drop.
 */
public class ImageItem extends Prototype implements InterfaceImageItem {

    private Area area = new Area();

    /**
     * Getting bounds for an image (for example: to keep aspect ratio).
     * 
     * @return Bounds as com.spvessel.spacevil.Core.Area.
     */

    public Area getAreaBounds() {
        return area;
    }

    private static int count = 0;
    private BufferedImage _image = null;

    /**
     * Getting a bitmap image in the form as java.awt.image.BufferedImage.
     * 
     * @return Image as java.awt.image.BufferedImage.
     */
    public BufferedImage getImage() {
        return _image;
    }

    private float _angleRotation = 0.0f;

    /**
     * Setting rotation angle for image.
     * <p>
     * Default: 0.
     * 
     * @param angle Rotation angle.
     */
    public void setRotationAngle(float angle) {
        _angleRotation = angle * (float) Math.PI / 180.0f;
    }

    /**
     * Getting rotation angle for image.
     * 
     * @return Rotation angle.
     */
    public float getRotationAngle() {
        return _angleRotation;
    }

    /**
     * Property to enable or disable mouse events (hover, click, press, scroll).
     * <p>
     * True: ImageItem can receive mouse events. False: cannot receive mouse events.
     * <p>
     * Default: False.
     */
    public boolean isHover = false;

    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        if (isHover)
            return super.getHoverVerification(xpos, ypos);
        return false;
    }

    /**
     * Default ImageItem constructor. Does not contains any image.
     */
    public ImageItem() {
        setItemName("Image_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(ImageItem.class));
    }

    /**
     * Constructs an ImageItem with an bitmap image.
     * 
     * @param picture Bitmap image as java.awt.image.BufferedImage.
     */
    public ImageItem(BufferedImage picture) {
        this();
        if (picture == null)
            return;
        setImage(picture);
    }

    /**
     * Constructs an ImageItem with an bitmap image with the ability to enable or
     * disable mouse events.
     * 
     * @param picture Bitmap image as java.awt.image.BufferedImage.
     * @param hover   True: ImageItem can receive mouse events. False: cannot
     *                receive mouse events.
     */
    public ImageItem(BufferedImage picture, boolean hover) {
        this(picture);
        isHover = hover;
    }

    // public byte[] getPixMapImage() {
    // return _bitmap;
    // }

    // protected byte[] createByteImage(BufferedImage picture) {
    // try {
    // _imageWidth = picture.getWidth();
    // _imageHeight = picture.getHeight();
    // List<Byte> bmp = new LinkedList<Byte>();
    // for (int j = picture.getHeight() - 1; j >= 0; j--) {
    // for (int i = 0; i < picture.getWidth(); i++) {
    // byte[] bytes = ByteBuffer.allocate(4).putInt(picture.getRGB(i, j)).array();
    // bmp.add(bytes[1]);
    // bmp.add(bytes[2]);
    // bmp.add(bytes[3]);
    // bmp.add(bytes[0]);
    // }
    // }
    // byte[] result = new byte[bmp.size()];
    // int index = 0;
    // for (Byte var : bmp) {
    // result[index] = var;
    // index++;
    // }
    // return result;
    // } catch (Exception ex) {
    // System.out.println("Create byte image");
    // ex.printStackTrace();
    // return null;
    // }
    // }

    private int _imageWidth;
    private int _imageHeight;

    /**
     * Getting an image width.
     * 
     * @return Image width.
     */
    public int getImageWidth() {
        return _imageWidth;
    }

    /**
     * Getting an image height.
     * 
     * @return Image height.
     */
    public int getImageHeight() {
        return _imageHeight;
    }

    /**
     * Setting new bitmap image of ImageItem.
     * 
     * @param image New bitmap image as java.awt.image.BufferedImage.
     */
    public void setImage(BufferedImage image) {
        if (image == null)
            return;
        _image = ImageItem.cloneImage(image);
        _imageWidth = image.getWidth();
        _imageHeight = image.getHeight();
        if (_isKeepAspectRatio && _image != null)
            applyAspectRatio();
        UpdateLayout();
    }

    private boolean _isOverlay = false;
    private Color _colorOverlay;

    /**
     * Getting color overlay (useful in images that have alpha channel).
     * 
     * @return Color overlay as java.awt.Color.
     */
    public Color getColorOverlay() {
        return _colorOverlay;
    }

    /**
     * Setting color overlay (useful in images that have alpha channel).
     * 
     * @param color Color overlay as java.awt.Color.
     */
    public void setColorOverlay(Color color) {
        _colorOverlay = color;
        _isOverlay = true;
    }

    /**
     * Setting color overlay (useful in images that have alpha channel) with ability
     * to specify overlay status.
     * 
     * @param color   Color overlay as java.awt.Color.
     * @param overlay True: if color overlay is using. False: if color overlay is
     *                not using.
     */
    public void setColorOverlay(Color color, boolean overlay) {
        _colorOverlay = color;
        _isOverlay = overlay;
    }

    /**
     * Setting color overlay status.
     * 
     * @param overlay True: if color overlay is using. False: if color overlay is
     *                not using.
     */
    public void setColorOverlay(boolean overlay) {
        _isOverlay = overlay;
    }

    /**
     * Getting color overlay status.
     * 
     * @return True: if color overlay is using. False: if color overlay is not
     *         using.
     */
    public boolean isColorOverlay() {
        return _isOverlay;
    }

    private boolean _isKeepAspectRatio = false;

    /**
     * Setting value to keep or not to keep еру aspect ratio of the image.
     * 
     * @param value True: to keep aspect ratio of the image. False: to not keep
     *              aspect ratio of the image.
     */
    public void keepAspectRatio(boolean value) {
        _isKeepAspectRatio = value;
    }

    /**
     * Returns True if aspect ratio of the image is kept otherwise returns False.
     * 
     * @return True: if aspect ratio of the image is kept. False: if aspect ratio of
     *         the image is not kept.
     */
    public boolean isAspectRatio() {
        return _isKeepAspectRatio;
    }

    /**
     * Setting ImageItem size (width and height).
     * 
     * @param width  Width of the ImageItem.
     * @param height Height of the ImageItem.
     */
    @Override
    public void setSize(int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    /**
     * Setting ImageItem height. If the value is greater/less than the
     * maximum/minimum value of the height, then the height becomes equal to the
     * maximum/minimum value.
     * 
     * @param height Height of the ImageItem.
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        area.setHeight(height);

        if (_isKeepAspectRatio && _image != null)
            applyAspectRatio();
        UpdateLayout();
    }

    /**
     * Setting ImageItem width. If the value is greater/less than the
     * maximum/minimum value of the width, then the width becomes equal to the
     * maximum/minimum value.
     * 
     * @param width Width of the ImageItem.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        area.setWidth(width);

        if (_isKeepAspectRatio && _image != null)
            applyAspectRatio();
        UpdateLayout();
    }

    /**
     * Setting X coordinate of the left-top corner of the ImageItem.
     * 
     * @param x X position of the left-top corner.
     */
    @Override
    public void setX(int x) {
        super.setX(x);
        UpdateLayout();
    }

    /**
     * Setting Y coordinate of the left-top corner of the ImageItem.
     * 
     * @param y Y position of the left-top corner.
     */
    @Override
    public void setY(int y) {
        super.setY(y);
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
    void UpdateLayout() {
        UpdateVerticalPosition();
        UpdateHorizontalPosition();
        ItemsRefreshManager.setRefreshImage(this);
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

    /**
     * Disposing bitmap resources if the item was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void release() {
        if (_image != null)
            _image.flush();
    }

    static private BufferedImage cloneImage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private ImageQuality _filter = ImageQuality.SMOOTH;

    /**
     * Getting an image filtering quality.
     * 
     * @return Image filtering quality as com.spvessel.spacevil.Flags.ImageQuality.
     */
    @Override
    public ImageQuality getImageQuality() {
        return _filter;
    }

    /**
     * Setting an image filtering quality.
     * 
     * @param quality Image filtering quality as
     *                com.spvessel.spacevil.Flags.ImageQuality.
     */
    public void setImageQuality(ImageQuality quality) {
        _filter = quality;
    }
}
