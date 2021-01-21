package OwnLibs.Owls.Views.Items;

import OwnLibs.Owls.ElementsFactory;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.ImageItem;
import com.spvessel.spacevil.Prototype;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagedButton extends Prototype {
    private ImageItem _image = null;
    private boolean isToggleable;
    private boolean isToggled;

    public ImagedButton(BufferedImage image, String tooltip) {
        this(image, false, tooltip);
    }

    public ImagedButton(BufferedImage image, boolean isToggleable, String tooltip) {
        this(new ImageItem(image), isToggleable, tooltip);
    }

    public ImagedButton(ImageItem _image, boolean isToggleable, String tooltip) {
        this._image = _image;
        this.isToggleable = isToggleable;
        isToggled = false;
        setStyle(ElementsFactory.getFunctionalButtonStyle());
        setToolTip(tooltip);
    }

    @Override
    public void initElements()
    {
        super.initElements();

        if (isToggleable) {
            eventMouseClick.add(this::onMouseClick);
        }

        _image.setMargin(4, 4, 4, 4);
        _image.keepAspectRatio(true);
        addItem(_image);

        if (isToggleable) {
            _image.setColorOverlay(new Color(209, 108, 108));
            setShadow(5, 0, 0, Color.black);
            setBackground(55, 55, 55);
            setShadowDrop(false);
        }
    }

    private void onMouseClick(Object sender, MouseArgs args) {
        setToggled(!isToggled);
    }

    public boolean isToggled() {
        if (!isToggleable) {
            return false;
        }
        return isToggled;
    }

    public void setToggled(boolean value) {
        if (!isToggleable) {
            return;
        }
        isToggled = value;
        if (value) {
            setState(ItemStateType.TOGGLED);
        } else {
            setState(ItemStateType.BASE);
        }
        setShadowDrop(value);
    }
}
