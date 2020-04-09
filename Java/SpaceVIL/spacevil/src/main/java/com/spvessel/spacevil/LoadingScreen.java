package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.RedrawFrequency;

/**
 * LoadingScreen is designed to lock the entire window to prevent all input
 * events during the execution of any long time task.
 * <p>
 * Contains image and text.
 * <p>
 * Supports all events except drag and drop.
 */
public class LoadingScreen extends Prototype {
    static int count = 0;
    private ImageItem _loadIcon;

    /**
     * Setting an image that should let the user know that another task is not yet
     * complete, and the user must wait.
     * 
     * @param image Image as java.awt.image.BufferedImage.
     */
    public void setImage(BufferedImage image) {
        _loadIcon = new ImageItem(image);
    }

    private Label _textObject;

    /**
     * Setting the text that represents the progress of the unfinished task, visible
     * or invisible.
     * 
     * @param value True: if text should be visible. False: if text should be
     *              invisible.
     */
    public void setValueVisible(boolean value) {
        _textObject.setVisible(value);
    }

    /**
     * Returns True if text that represents the progress of the unfinished task is
     * visible, otherwise returns False.
     * 
     * @return True: if text is visible. False: if text is invisible.
     */
    public boolean isValueVisible() {
        return _textObject.isVisible();
    }

    private CoreWindow _handler = null;

    /**
     * Default LoadingScreen constructor.
     */
    public LoadingScreen() {
        setItemName("LoadingScreen_" + count++);
        setPassEvents(false);
        _loadIcon = new ImageItem();
        if (_loadIcon.getImage() == null)
            _loadIcon
                    .setImage(DefaultsService.getDefaultImage(EmbeddedImage.LOAD_CIRCLE, EmbeddedImageSize.SIZE_64X64));
        _textObject = new Label("0%");
        setStyle(DefaultsService.getDefaultStyle(LoadingScreen.class));

        eventKeyPress.add((s, a) -> {
            if (a.key == KeyCode.ESCAPE && a.mods.contains(KeyMods.SHIFT))
                setToClose();
        });
    }

    /**
     * Initializing all elements in the LoadingScreen.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        _loadIcon.isHover = false;
        super.addItems(_loadIcon, _textObject);
    }

    private int _percent = 0;

    /**
     * Setting the progress value of the unfinished task.
     * 
     * @param value Progress value of the unfinished task.
     */
    public void setValue(int value) {
        if (value == _percent)
            return;
        _percent = value;
        if (_percent > 100)
            _percent = 100;
        if (_percent < 0)
            _percent = 0;
        _textObject.setText(Integer.toString(_percent) + "%");
    }

    /**
     * Getting the progress value of the unfinished task.
     * 
     * @return Progress value of the unfinished task.
     */
    public int getValue() {
        return _percent;
    }

    private Lock _locker = new ReentrantLock();
    private boolean _isShouldClose = false;

    /**
     * Informing of LoadingScreen to closes.
     */
    public void setToClose() {
        _locker.lock();
        try {
            _isShouldClose = true;
        } catch (Exception ex) {
            System.out.println("Method - setToClose");
            ex.printStackTrace();
            _isShouldClose = true;
        } finally {
            _locker.unlock();
        }
    }

    private boolean isOnClose() {
        _locker.lock();
        try {
            return _isShouldClose;
        } catch (Exception ex) {
            System.out.println("Method - IsOnClose");
            ex.printStackTrace();
            return true;
        } finally {
            _locker.unlock();
        }
    }

    /**
     * Shows LoadingScreen and attaches it to the specified window (see
     * com.spvessel.spacevil.CoreWindow, com.spvessel.spacevil.ActiveWindow,
     * com.spvessel.spacevil.DialogWindow).
     * 
     * @param handler Window for attaching LoadingScreen.
     */
    public void show(CoreWindow handler) {
        _handler = handler;
        _handler.addItem(this);
        _handler.setFocusedItem(this);
        RedrawFrequency tmp = _handler.getRenderFrequency();
        if (tmp != RedrawFrequency.HIGH)
            _handler.setRenderFrequency(RedrawFrequency.HIGH);
        Thread thread = new Thread(() -> {
            int alpha = 360;
            while (!isOnClose()) {
                _loadIcon.setRotationAngle(alpha);
                alpha--;
                if (alpha == 0)
                    alpha = 360;
                try {
                    Thread.sleep(2);
                } catch (Exception e) {
                }
            }
            Close();
            if (tmp != RedrawFrequency.HIGH)
                _handler.setRenderFrequency(tmp);
        });
        thread.start();
    }

    /**
     * Closes LoadingScreen.
     */
    private void Close() {
        _handler.removeItem(this);
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        _textObject.setFont(font);
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        _textObject.setFontSize(size);
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        _textObject.setFontStyle(style);
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        _textObject.setFontFamily(fontFamily);
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _textObject.getFont();
    }

    /**
     * Setting text color of a LoadingScreen.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    /**
     * Setting text color of a LoadingScreen in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        _textObject.setForeground(r, g, b);
    }

    /**
     * Setting text color of an item in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b, int a) {
        _textObject.setForeground(r, g, b, a);
    }

    /**
     * Setting text color of a LoadingScreen in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    /**
     * Setting text color of a LoadingScreen in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        _textObject.setForeground(r, g, b, a);
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return _textObject.getForeground();
    }

    /**
     * Setting style of the ButtonCore.
     * <p>
     * Inner styles: "text", "image".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        // common
        setForeground(style.foreground);
        setFont(style.font);
        // parts
        _textObject.setStyle(style.getInnerStyle("text"));
        _loadIcon.setStyle(style.getInnerStyle("image"));
    }
}