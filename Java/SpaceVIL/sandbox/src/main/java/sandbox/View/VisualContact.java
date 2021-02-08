package sandbox.View;

import java.awt.Color;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Core.IMouseMethodState;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

public class VisualContact extends Prototype {
    public static int _count = 0;

    // private Integer[] _array;

    public VisualContact() {
        // self view attr
        setItemName("VC_" + _count);
        setBackground(80, 80, 80);
        setMinSize(250, 60);
        setSize(300, 60);
        setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        setAlignment(ItemAlignment.Top, ItemAlignment.Left);
        setBorderRadius(15);
        ItemState hover = new ItemState();
        hover.background = new Color(255, 255, 255, 15);
        addItemState(ItemStateType.Hovered, hover);
        setPadding(10, 0, 5, 0);
        setMargin(1, 1, 1, 1);
        effects().add(new Shadow(10, new Position(3, 3), new Color(0, 0, 0, 160)));
        _count++;
    }

    @Override
    public void initElements() {
        // contact image border
        Ellipse border = new Ellipse();
        border.setBackground(255, 180, 100, 100);
        border.setHeight(45);
        border.setHeightPolicy(SizePolicy.Fixed);
        border.setWidth(45);
        border.setWidthPolicy(SizePolicy.Fixed);
        border.setAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        border.quality = 32;
        // border.setTriangles(GraphicsMathService.getRoundSquare(50, 50, 25, 0, 0));
        // border.setTriangles(GraphicsMathService.getEllipse(50, 50, 0, 0, 32));

        // contact name
        Label name = new Label(getItemName() + " contact");
        // name.isHover = false;
        // Label name = new Label();
        name.setTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        name.setBackground(255, 255, 255, 32);
        name.setForeground(210, 210, 210);
        name.setHeight(30);
        name.setHeightPolicy(SizePolicy.Fixed);
        name.setWidthPolicy(SizePolicy.Expand);
        // name.setMargin(60, 0, 30, 5);
        name.setPadding(20, 0, 0, 0);
        name.setAlignment(ItemAlignment.Bottom, ItemAlignment.Left);
        name.setBorderRadius(10);
        name.addItemState(ItemStateType.Hovered, new ItemState(new Color(10, 162, 232)));

        // contact close
        ButtonCore close = new ButtonCore();
        close.setBackground(40, 40, 40);
        close.setWidth(14);
        close.setWidthPolicy(SizePolicy.Fixed);
        close.setHeight(14);
        close.setHeightPolicy(SizePolicy.Fixed);
        close.setAlignment(ItemAlignment.Top, ItemAlignment.Right);
        // close.setMargin(0, 5, 0, 0);
        close.setCustomFigure(new Figure(false, GraphicsMathService.getCross(14, 14, 5, 45)));
        ItemState hover = new ItemState();
        hover.background = new Color(255, 255, 255, 125);
        close.addItemState(ItemStateType.Hovered, hover);
        IMouseMethodState click = (sender, args) -> disposeSelf();
        close.eventMouseClick.add(click);

        // adding
        addItem(border);
        addItem(name);
        addItem(close);

        // _array = new Integer[1024 * 1024 * 128];
        // for (int i = 0; i < _array.length; i++) {
        // _array[i] = 12;
        // }
    }

    public void disposeSelf() {
        getParent().removeItem(this);
    }
}