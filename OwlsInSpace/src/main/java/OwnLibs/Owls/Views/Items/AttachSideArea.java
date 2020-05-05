package OwnLibs.Owls.Views.Items;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.*;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

public class AttachSideArea extends SideArea {
    private boolean _isInit = false;
    private ListBox _contentList = new ListBox();
    private ButtonCore _addContentButton = new ButtonCore();
    private ButtonCore _close;

    private List<Label> _list = new LinkedList<>();

    public AttachSideArea(CoreWindow handler, Side attachSide) {
        super(handler, attachSide);
        setBackground(0, 0, 0, 0);
        window.setBackground(64, 64, 64);
        window.setMargin(0, 135, 0, 0);
        window.setPadding(new Indents());
    }

    @Override
    public void initElements() {
        super.initElements();
        if (_isInit)
            return;

        Label _name = new Label();
        _name.setBackground(50, 50, 50);
        _name.setFont(DefaultsService.getDefaultFont(16));
        _name.setText("Attached content:");
        _name.setMargin(0, 0, 0, 0);
        _name.setPadding(10, 10, 10, 0);
        _name.setHeightPolicy(SizePolicy.FIXED);
        _name.setHeight(50);

        _contentList.setBackground(64, 64, 64);
        _contentList.setSelectionVisible(false);
        _contentList.setVScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        _contentList.setHScrollBarPolicy(VisibilityPolicy.NEVER);
        _contentList.setMargin(10, 100, 10, 50);
        _contentList.vScrollBar.setStyle(Style.getSimpleVerticalScrollBarStyle());

        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = Color.BLACK;
        style.font = DefaultsService.getDefaultFont();
        style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        style.setSize(30, 30);
        style.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        style.setTextAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        style.setPadding(4, 4, 4, 4);
        style.setMargin(0, 0, 10, 10);
        style.setSpacing(0, 0);
        style.setBorder(new Border(new Color(0, 0, 0, 0), new CornerRadius(15), 0));
        style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 20)));
        style.addItemState(ItemStateType.PRESSED, new ItemState(new Color(0, 0, 0, 30)));
        _addContentButton.setStyle(style);

        CustomShape plus = new CustomShape(GraphicsMathService.getCross(30, 30, 3, 0));
        plus.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        plus.setBackground(120, 120, 120);
        plus.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        for (InterfaceBaseItem item : window.getItems()) {
            if (item instanceof ButtonCore) {
                _close = (ButtonCore) item;
                break;
            }
        }
        _close.setMargin(0, 5, 10, 0);

        insertItem(_name, 0);
        addItems(_contentList, _addContentButton);

        _addContentButton.addItem(plus);
        _addContentButton.eventMouseClick.add((sender, args) -> {

        });

        if (_list.size() > 0)
            for (Label item : _list) {
                _contentList.addItem(item);
            }

        _isInit = true;
    }

    public void show() {
        super.show();
    }

    public void show(InterfaceItem item, MouseArgs args) {
        super.show(item, args);
    }
}