package OwnLibs.Owls.Views.Items;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import com.spvessel.spacevil.ButtonToggle;
import com.spvessel.spacevil.Rectangle;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

public class TextMarker extends ButtonToggle {

    private Rectangle _subline = new Rectangle();

    public TextMarker(String text) {
        super(text);
        setBackground(0, 0, 0, 0);
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        setFontSize(16);
        setFontStyle(Font.BOLD);
        setFontStyle(Font.PLAIN);
        setForeground(210, 210, 210);
        setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        removeItemState(ItemStateType.HOVERED);
        removeItemState(ItemStateType.PRESSED);
        addItemState(ItemStateType.TOGGLED, new ItemState(new Color(255, 255, 255, 20)));
    }

    public TextMarker(String text, int width) {
        this(text);
        setWidthPolicy(SizePolicy.FIXED);
        setWidth(width);
    }

    @Override
    public void initElements() {
        super.initElements();

        _subline.setVisible(false);
        _subline.setBackground(0, 162, 232);
        _subline.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        _subline.setHeight(2);
        _subline.setAlignment(ItemAlignment.BOTTOM);

        addItem(_subline);

        eventMouseClick.add((sender, args) -> {
            setToggled(true);
            untoggleOthers();
        });

        eventMouseHover.add((sender, args) -> {
            setFontStyle(Font.BOLD);
            if (isToggled())
                return;
            _subline.setVisible(true);
        });
        eventMouseLeave.add((sender, args) -> {
            setFontStyle(Font.PLAIN);
            if (isToggled())
                return;
            _subline.setVisible(false);
        });
    }

    @Override
    public void setToggled(boolean value) {
        super.setToggled(value);
        if (isToggled())
            _subline.setVisible(true);
        else
            _subline.setVisible(false);

    }

    private void untoggleOthers() {
        List<InterfaceBaseItem> list = getParent().getItems();
        for (InterfaceBaseItem item : list) {
            if (item instanceof TextMarker && !item.equals(this)) {
                TextMarker tm = (TextMarker) item;
                tm.setToggled(false);
            }
        }
    }
}