package sandbox.View;

import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Flags.*;
import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Flags.VisibilityPolicy;

import java.awt.Color;

public class BlockList extends ResizableItem {
    static int count = 0;

    private ButtonCore _palette;
    private ButtonToggle _lock;
    private TextArea _text;
    private Label _note;
    private ButtonCore _btn_close;
    private ContextMenu _palette_menu;

    public BlockList() {
        setPassEvents(false);
        setMinSize(200, 100);
        setItemName("BlockList_" + count);
        setPadding(4, 4, 4, 4);
        setBorderRadius(4);
        count++;

        _palette = new ButtonCore();
        _palette.setPassEvents(false);
        _lock = new ButtonToggle();
        _text = new TextArea();
        _note = new Label();
    }

    @Override
    public void initElements() {
        _palette.setAlignment(ItemAlignment.Right, ItemAlignment.Top);
        _palette.setMargin(0, 40, 0, 0);
        _palette.setSize(20, 15);
        _palette.setBackground(255, 128, 128);
        _palette.setBorderRadius(0);
        _palette.setBorderRadius(3);
        CustomShape arrow = new CustomShape();
        arrow.setTriangles(GraphicsMathService.getTriangle(30, 30, 0, 0, 180));
        arrow.setBackground(50, 50, 50);
        arrow.setSize(14, 6);
        arrow.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        arrow.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

        _lock.setAlignment(ItemAlignment.Left, ItemAlignment.Top);
        _lock.setSize(15, 15);
        _lock.setBorderRadius(8);
        _lock.eventToggle.add((sender, args) -> {
            isLocked = !isLocked;
            _text.setEditable(!_text.isEditable());
        });

        VerticalScrollBar vs = _text.vScrollBar;
        vs.slider.handler.removeAllItemStates();
        vs.setArrowsVisible(false);
        vs.setBackground(151, 203, 255);
        vs.setPadding(0, 2, 0, 2);
        vs.slider.track.setBackground(new Color(0, 0, 0, 0));
        vs.slider.handler.setBorderRadius(3);
        vs.slider.handler.setBackground(80, 80, 80, 255);
        vs.slider.handler.setMargin(new Indents(5, 0, 5, 0));

        // _text.setPadding(-1, -1, -1, -1);
        _text.setBorderRadius(new CornerRadius(3));
        _text.setHScrollBarPolicy(VisibilityPolicy.Never);
        _text.setHeight(25);
        _text.setAlignment(ItemAlignment.Left, ItemAlignment.Bottom);
        _text.setBackground(151, 203, 255);
        _text.setMargin(0, 60, 0, 0);

        _note.setForeground(180, 180, 180);
        _note.setHeight(25);
        _note.setAlignment(ItemAlignment.Left, ItemAlignment.Top);
        _note.setTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        _note.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        _note.setText("Add a Note:");
        _note.setMargin(0, 30, 0, 0);

        _btn_close = new ButtonCore();
        _btn_close.setBackground(100, 100, 100);
        _btn_close.setItemName("Close_" + getItemName());
        _btn_close.setSize(10, 10);
        _btn_close.setMargin(0, 0, 0, 0);
        _btn_close.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        _btn_close.setAlignment(ItemAlignment.Top, ItemAlignment.Right);
        ItemState hovered = new ItemState(new Color(255, 255, 255, 80));
        _btn_close.addItemState(ItemStateType.Hovered, hovered);
        _btn_close.setCustomFigure(new Figure(false, GraphicsMathService.getCross(10, 10, 3, 45)));
        _btn_close.eventMouseClick.add((sender, args) -> Dispose());

        addItems(_lock, _note, _text, _palette, _btn_close);

        _palette.addItem(arrow);

        _palette_menu = new ContextMenu(getHandler());
        _palette_menu.setBackground(60, 60, 60);
        // _palette_menu.setSize(80, 123);
        MenuItem red = new MenuItem("Red");
        red.setForeground(210, 210, 210);
        red.eventMouseClick.add((sender, args) -> {
            _text.setBackground(255, 196, 196);
            _text.vScrollBar.setBackground(_text.getBackground());
        });
        MenuItem green = new MenuItem("Green");
        green.setForeground(210, 210, 210);
        green.eventMouseClick.add((sender, args) -> {
            _text.setBackground(138, 255, 180);
            _text.vScrollBar.setBackground(_text.getBackground());
        });
        MenuItem blue = new MenuItem("Blue");
        blue.setForeground(210, 210, 210);
        blue.eventMouseClick.add((sender, args) -> {
            _text.setBackground(151, 203, 255);
            _text.vScrollBar.setBackground(_text.getBackground());
        });
        MenuItem yellow = new MenuItem("Yellow");
        yellow.setForeground(210, 210, 210);
        yellow.eventMouseClick.add((sender, args) -> {
            _text.setBackground(234, 232, 162);
            _text.vScrollBar.setBackground(_text.getBackground());
        });
        _palette_menu.addItems(red, green, blue, yellow);
        _palette.eventMouseClick.add((sender, args) -> _palette_menu.show(sender, args));
        _palette_menu.activeButton = MouseButton.ButtonLeft;
    }

    public void Dispose() {
        getParent().removeItem((IBaseItem) this);
    }
}