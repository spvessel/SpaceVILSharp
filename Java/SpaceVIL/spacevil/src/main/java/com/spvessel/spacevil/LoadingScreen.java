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
import com.spvessel.spacevil.Flags.RedrawFrequency;

public class LoadingScreen extends Prototype {
    static int count = 0;
    private ImageItem _loadIcon;

    public void setImage(BufferedImage image) {
        _loadIcon = new ImageItem(image);
    }

    private Label _text_object;
    private WindowLayout _handler = null;

    public LoadingScreen() {
        setItemName("LoadingScreen_" + count++);
        setPassEvents(false);
        _loadIcon = new ImageItem();
        if (_loadIcon.getImage() == null)
            _loadIcon
                    .setImage(DefaultsService.getDefaultImage(EmbeddedImage.LOAD_CIRCLE, EmbeddedImageSize.SIZE_64X64));
        _text_object = new Label("0%");
        setStyle(DefaultsService.getDefaultStyle(LoadingScreen.class));

        eventKeyPress.add((s, a) -> {
            if (a.key == KeyCode.NUMPADADD)
                setValue(getValue() + 1);
            else if (a.key == KeyCode.NUMPADSUBTRACT)
                setValue(getValue() - 1);
            else if (a.key == KeyCode.ESCAPE)
                setToClose();
        });
    }

    @Override
    public void initElements() {
        _loadIcon.isHover = false;
        super.addItems(_loadIcon, _text_object);
    }

    private int _percent = 0;

    public void setValue(int value) {
        if (value == _percent)
            return;
        _percent = value;
        if (_percent > 100)
            _percent = 100;
        if (_percent < 0)
            _percent = 0;
        _text_object.setText(Integer.toString(_percent) + "%");
    }

    public int getValue() {
        return _percent;
    }

    private Lock _locker = new ReentrantLock();
    private boolean _isShouldClose = false;

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

    private boolean IsOnClose() {
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

    public void Show(WindowLayout handler) {
        _handler = handler;
        _handler.addItem(this);
        _handler.setFocusedItem(this);
        RedrawFrequency tmp = _handler.getRenderFrequency();
        if (tmp != RedrawFrequency.HIGH)
            _handler.setRenderFrequency(RedrawFrequency.HIGH);
        Thread thread = new Thread(() -> {
            int alpha = 360;
            while (!IsOnClose()) {
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

    private void Close() {
        _handler.getWindow().removeItem(this);
    }

    /**
     * Set text in the LoadingScreen
     */
    public void setFont(Font font) {
        _text_object.setFont(font);
    }

    public void setFontSize(int size) {
        _text_object.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _text_object.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _text_object.setFontFamily(font_family);
    }

    public Font getFont() {
        return _text_object.getFont();
    }

    /**
     * Text color in the LoadingScreen
     */
    public void setForeground(Color color) {
        _text_object.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _text_object.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _text_object.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _text_object.getForeground();
    }

    /**
     * Set style of the LoadingScreen
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
        _text_object.setStyle(style.getInnerStyle("text"));
        _loadIcon.setStyle(style.getInnerStyle("image"));
    }
}