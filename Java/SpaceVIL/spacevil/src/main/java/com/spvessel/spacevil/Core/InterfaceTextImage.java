package com.spvessel.spacevil.Core;

/**
 * An interface that describes text image.
 */
public interface InterfaceTextImage {
    /**
     * Method for getting bitmap image as byte array.
     * @return Bitmap image as byte array.
     */
    public byte[] getBytes();

    /**
     * Method for getting empty status.
     * @return True: implementation of the interface does not contain an image.
     * False: implementation of the interface contains image.
     */
    public boolean isEmpty();

    /**
     * Method for getting width of the image.
     * @return Image width.
     */
    public int getWidth();

    /**
     * Method for getting height of the image.
     * @return Image height.
     */
    public int getHeight();

    /**
     * Method for getting image offset by X axis.
     * @return X axis offset.
     */
    public int getXOffset();

    /**
     * Method for getting image offset by Y axis.
     * @return Y axis offset.
     */
    public int getYOffset();
}
