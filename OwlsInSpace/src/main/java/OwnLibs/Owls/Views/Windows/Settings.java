package OwnLibs.Owls.Views.Windows;

import java.awt.image.BufferedImage;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;

public class Settings extends ActiveWindow {
    TitleBar titleBar;

    @Override
    public void initWindow() {
        setParameters("Settings", "Settings", 800, 800, false);
        setMinSize(400, 400);
        setPadding(2, 2, 2, 2);

        BufferedImage iBig = DefaultsService.getDefaultImage(EmbeddedImage.GEAR, EmbeddedImageSize.SIZE_64X64);
        BufferedImage iSmall = DefaultsService.getDefaultImage(EmbeddedImage.GEAR, EmbeddedImageSize.SIZE_32X32);
        setIcon(iBig, iSmall);

        titleBar = new TitleBar("Settings");
        titleBar.setIcon(iSmall, 24, 24);
        titleBar.setTextMargin(new Indents(10, 0, 0, 0));

        VerticalStack layout = new VerticalStack();
        layout.setMargin(0, 30, 0, 0);
        layout.setBackground(64, 64, 64);

        addItems(titleBar, layout);
    }
}