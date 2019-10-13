package OwnLibs.Owls.Views.Items;

import java.awt.Color;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

public class HomeTab extends Tab {

    private ImageItem _homeIcon;

    public HomeTab() {
        super();
        _homeIcon = new ImageItem(DefaultsService.getDefaultImage(EmbeddedImage.HOME, EmbeddedImageSize.SIZE_32X32),
                false);

        setDraggable(false);
        setClosable(false);
        setBackground(55, 55, 55);
        setBorderRadius(0);
        setMinWidth(35);
        setMaxWidth(35);
        setMargin(0, 0, 2, 0);
        setPadding(0, 0, 0, 0);
        removeItemState(ItemStateType.HOVERED);
        addItemState(ItemStateType.TOGGLED, new ItemState(new Color(70, 70, 70)));
    }

    @Override
    public void initElements() {
        super.initElements();
        _homeIcon.keepAspectRatio(true);
        _homeIcon.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        _homeIcon.setSize(20, 20);
        _homeIcon.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        addItem(_homeIcon);
    }
}