package OwnLibs.Owls.Views.Items;

import OwnLibs.Owls.ElementsFactory;
import OwnLibs.Owls.Views.Windows.MainWindow;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.ImageItem;
import com.spvessel.spacevil.MenuItem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImagedMenuItem extends MenuItem {
    private ImageItem _image = null;
    private Color _overlay = null;

    public ImagedMenuItem(String text, BufferedImage image) {
        this(text, image, null);
    }

    public ImagedMenuItem(String text, ImageItem image) {
        this(text, image, null);
    }

    public ImagedMenuItem(String text, String resPath) {
        this(text, resPath, null);
    }

    public ImagedMenuItem(String text, String resPath, Color overlay) {
        super(text);

        BufferedImage image = null;
        try {
            image = ImageIO.read(ImagedMenuItem.class.getResourceAsStream(resPath));
        } catch (Exception e) {
            System.out.println("load icon for " + text + " failed");
        }

        this._image = new ImageItem(image);
        this._overlay = overlay;
        setStyle(ElementsFactory.getMenuStyle());
        addItem(_image);
    }

    public ImagedMenuItem(String text, BufferedImage image, Color overlay) {
        this(text, new ImageItem(image, false), overlay);
    }

    public ImagedMenuItem(String text, ImageItem image, Color overlay) {
        super(text);
        this._image = image;
        this._overlay = overlay;
        setStyle(ElementsFactory.getMenuStyle());
        addItem(_image);
    }

    @Override
    public void initElements() {
        super.initElements();
//        setBackground(new Color(0,0,0,0));

        _image.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        _image.setSize(16, 16);
        _image.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        _image.keepAspectRatio(true);
        if (_overlay != null) {
            _image.setColorOverlay(_overlay);
        }

//        addItem(_image);
    }
}
