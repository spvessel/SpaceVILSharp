package OwnLibs.Owls.Views.Items;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import com.spvessel.spacevil.ButtonToggle;
import com.spvessel.spacevil.Rectangle;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

public class SelectionMarker extends ButtonToggle {

    private Rectangle _subline = new Rectangle();

    private VerticalStack _layout;

    public SelectionMarker(String text, VerticalStack layout) {
        super(text);
        setBackground(0, 0, 0, 0);
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        setFontSize(16);
        setForeground(180, 180, 180);
        setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        removeItemState(ItemStateType.HOVERED);
        removeItemState(ItemStateType.PRESSED);
        addItemState(ItemStateType.TOGGLED, new ItemState(new Color(255, 255, 255, 20)));
        _layout = layout;
    }

    public SelectionMarker(String text, int width, VerticalStack layout) {
        this(text, layout);
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
            untoggleOthers();
            setToggled(true);
        });

        eventMouseHover.add((sender, args) -> {
            if (isToggled())
                return;
            _subline.setVisible(true);
            setForeground(Color.WHITE);
        });
        eventMouseLeave.add((sender, args) -> {
            if (isToggled())
                return;
            _subline.setVisible(false);
            setForeground(180, 180, 180);
        });
    }

    @Override
    public void setToggled(boolean value) {
        super.setToggled(value);
        if (isToggled()) {
            setForeground(Color.WHITE);
            _subline.setVisible(true);
            _layout.setVisible(true);
            _layout.updateLayout();
        } else {
            _subline.setVisible(false);
            _layout.setVisible(false);
            _layout.updateLayout();
        }
    }

    private void untoggleOthers() {
        List<InterfaceBaseItem> list = getParent().getItems();
        for (InterfaceBaseItem item : list) {
            if (item.equals(this))
                continue;
            if (item instanceof SelectionMarker) {
                SelectionMarker tm = (SelectionMarker) item;
                tm.setToggled(false);
                tm.setForeground(180, 180, 180);
            }
        }
    }
}