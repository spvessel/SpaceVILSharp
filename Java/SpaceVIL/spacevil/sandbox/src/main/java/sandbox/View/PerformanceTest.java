package sandbox.View;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.BlankItem;
import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.Grid;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.ItemsLayoutBox;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.WrapGrid;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.Size;
import com.spvessel.spacevil.Decorations.Effects;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.Orientation;
import com.spvessel.spacevil.Flags.RedrawFrequency;
import com.spvessel.spacevil.Flags.SizePolicy;

public class PerformanceTest extends ActiveWindow {
    Label _infoOutput;

    @Override
    public void initWindow() {
        setParameters("PerformanceTest", "Performance JVM", 800, 800, true);
        isCentered = true;
        setBackground(24, 24, 24);
        setPadding(2, 30, 2, 2);
        _infoOutput = new Label("No button pressed");
        _infoOutput.setHeight(25);
        _infoOutput.setHeightPolicy(SizePolicy.Fixed);
        _infoOutput.setBackground(80, 80, 80);
        _infoOutput.setForeground(210, 210, 210);
        _infoOutput.setFontSize(18);
        _infoOutput.setFontStyle(Font.BOLD);
        _infoOutput.setAlignment(ItemAlignment.Bottom);
        _infoOutput.setTextAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
        _infoOutput.setPadding(0, 0, 0, 3);
        addItem(_infoOutput);
        eventKeyRelease.add((sender, args) -> {
            _infoOutput.setText(ItemsLayoutBox.getListOfItemsNames(this).length + " items");
        });

        eventOnStart.add(() -> {
            // wrapTest();
            // wrapWrapTest();
            // stackTest();
            // gridTest();
            shadowTest();
        });
    }

    private IBaseItem getButton(int index) {
        ButtonCore btn = new ButtonCore();
        btn.setItemName("Button_" + index);
        btn.setBackground(Color.WHITE);
        btn.setSize(8, 8);
        btn.addItemState(ItemStateType.Hovered, new ItemState(Color.RED));
        // btn.setBorderRadius(4);
        btn.setMargin(1, 1, 1, 1);
        btn.eventMouseClick.add((sender, args) -> {
            _infoOutput.setText(btn.getItemName() + " was pressed");
        });
        // btn.setCustomFigure(new CustomFigure(false, GraphicsMathService.getRectangle(8, 8, 0, 0)));

        return btn;
    }

    private void addManyItems(WrapGrid container, int count) {
        // long startTime = System.nanoTime();
        // for (int i = 0; i < count; i++) {
        //     container.addItem(getButton(i + 1));
        // }
        // System.out.println("Fuction run " + ((System.nanoTime() - startTime) / 1000000) + " ms");

        List<IBaseItem> content = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            content.add(getButton(i + 1));
        }
        long startTime = System.nanoTime();
        container.setListContent(content);
        System.out.println("Fuction run " + ((System.nanoTime() - startTime) / 1000000) + " ms");
    }

    private void wrapTest() {
        WrapGrid wrapGrid = new WrapGrid(9, 9, Orientation.Horizontal);
        wrapGrid.getArea().setSpacing(0, 0);
        wrapGrid.setBackground(new Color(0, 0, 0, 0));
        addItems(wrapGrid);
        addManyItems(wrapGrid, 1000);
    }

    private void wrapWrapTest() {
        WrapGrid wrapGrid = new WrapGrid(250, 250, Orientation.Horizontal);
        wrapGrid.getArea().setSpacing(0, 0);
        wrapGrid.setMargin(0, 0, 0, 30);
        // Grid wrapGrid = new Grid(2, 2);
        // ListBox wrapGrid = new ListBox();
        addItems(wrapGrid);
        for (int i = 0; i < 8; i++) {
            WrapGrid w = new WrapGrid(10, 10, Orientation.Horizontal);
            w.getArea().setSpacing(0, 0);
            // w.setHeightPolicy(SizePolicy.FIXED);
            // w.setWidthPolicy(SizePolicy.FIXED);
            // w.setHeight(300);
            // w.setWidth(300);
            w.setMargin(2, 2, 2, 2);
            w.setBackground(Color.black);
            wrapGrid.addItem(w);
            addManyItems(w, 1000);
            // w.getArea().itemListChanged.execute();
        }
    }

    private void stackTest() {
        setBackground(200, 200, 200);
        VerticalStack vStack = new VerticalStack();
        vStack.setSpacing(0, 0);
        addItems(vStack);
        int index = 0;
        for (int i = 0; i < 1; i++) {
            HorizontalStack h = new HorizontalStack();
            h.setHeightPolicy(SizePolicy.Fixed);
            h.setHeight(22);
            h.setSpacing(6, 0);
            h.setContentAlignment(ItemAlignment.HCenter);
            vStack.addItem(h);
            for (int j = 0; j < 1; j++) {
                IBaseItem btn = getButton(++index);
                btn.setSize(14, 14);
                // ((ButtonCore) btn).setBorderRadius(3);
                Effects.addEffect(btn, new Shadow(5, new Position(3, 2), new Size(10, 10), Color.black));
                h.addItem(btn);
            }
        }
    }

    private void shadowTest() {
        setBackground(200, 200, 200);
        VerticalStack v = v = new VerticalStack();
        v.setSpacing(0, 0);
        addItems(v);
        int index = 0;
        for (int i = 0; i < 32; i++) {
            HorizontalStack h = new HorizontalStack();
            h.setHeightPolicy(SizePolicy.Fixed);
            h.setHeight(22);
            h.setSpacing(6, 0);
            h.setContentAlignment(ItemAlignment.HCenter);
            v.addItem(h);
            for (int j = 0; j < 32; j++) {
                IBaseItem btn = getButton(++index);

                //set size & shadow
                btn.setSize(14, 14);
                Effects.addEffect(btn, new Shadow(5, new Position(3, 2), new Size(10, 10), Color.black));

                h.addItem(btn);
            }
        }
    }

    private void gridTest() {
        Grid grid = new Grid(20, 50);
        grid.setSpacing(2, 2);
        addItems(grid);
        int index = 0;
        for (int i = 0; i < 1000; i++) {
            grid.insertItem(getButton(index++), i);
        }
    }
}