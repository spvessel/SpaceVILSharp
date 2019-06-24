package com.spvessel.spacevil.View;

import java.awt.Color;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.Grid;
import com.spvessel.spacevil.HorizontalSlider;
import com.spvessel.spacevil.Tab;
import com.spvessel.spacevil.TabView;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.VerticalScrollBar;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.SizePolicy;

public class Containers extends ActiveWindow {

    TabView tabs;
    Tab tab1;
    Tab tab2;
    Tab tab3;
    TitleBar title;

    @Override
    public void initWindow() {
        setParameters("Containers", "Containers", 700, 500, false);
        this.setMinSize(400, 400);
        this.isCentered = true;

        title = new TitleBar("Containers");
        title.setShadow(5, 0, 3, new Color(0, 0, 0, 150));
        this.addItem(title);

        Frame cc = new Frame();
        cc.setMargin(0, title.getHeight() + 10, 0, 0);
        cc.setBackground(50, 50, 50);
        this.addItem(cc);

        tabs = new TabView();
        tab1 = new Tab("Gridadofigjhopaijgpaijgpoiajhsogijhaosighj1");
        tab1.setToolTip("Gridadofigjhopaijgpaijgpoiajhsogijhaosighj1");
        tab2 = new Tab("List");
        tab3 = new Tab("List");
        cc.addItem(tabs);

        tabs.addTabs(tab1, tab2, tab3);
        tab1.setWidthPolicy(SizePolicy.EXPAND);

        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.MENU)
                for (Tab tab : tabs.getTabs())
                    tab.setClosable(!tab.isClosable());
            if (args.key == KeyCode.DELETE) {
                clearAllTabs();
            }
            if (args.key == KeyCode.ENTER) {
                if (tabs != null) {
                    System.out.println(tabs.getTabs().size());
                }
            }
        });

        Grid grid = new Grid(3, 4);
        grid.setBackground(255, 255, 255, 20);
        grid.setSpacing(2, 2);
        grid.setMargin(20, 30, 20, 30);
        tabs.addItemToTabByName("Gridadofigjhopaijgpaijgpoiajhsogijhaosighj1", grid);
        Grid subgrid = new Grid(1, 2);
        grid.insertItem(subgrid, 1, 1);

        subgrid.addItems(new VerticalScrollBar(), new VerticalScrollBar());
        for (int i = 0; i < 11; i++) {
            ButtonCore h_btn = getButton("Cell" + i, 150, 50, SizePolicy.EXPAND);
            h_btn.setMaxSize(200, 100);
            h_btn.setBackground(121, 223, 152);
            grid.addItem(h_btn);
        }

        tab1.eventOnClose.add(() -> {
            System.out.println("close");
        });
    }

    private void clearAllTabs() {
        // title.removeItem(tabs);
        // tabs.removeAllTabs();
        tabs.getParent().removeItem(tabs);
        tab1 = null;
        tab2 = null;
        tab3 = null;
    }

    private ButtonCore getButton(String name, int w, int h, SizePolicy policy) {
        // Style
        Style style = Style.getButtonCoreStyle();

        style.background = new Color(111, 181, 255);
        style.foreground = Color.black;
        style.borderRadius = new CornerRadius(3);
        style.width = w;
        style.minWidth = 30;
        style.minHeight = 30;
        style.widthPolicy = policy;
        style.height = h;
        style.heightPolicy = policy;
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        ItemState hovered = new ItemState(new Color(255, 255, 255, 60));
        style.addItemState(ItemStateType.HOVERED, hovered);
        ItemState pressed = new ItemState(new Color(0, 0, 60, 30));
        pressed.border.setFill(new Color(255, 255, 255));
        pressed.border.setThickness(1);
        style.addItemState(ItemStateType.PRESSED, pressed);

        ButtonCore btn = new ButtonCore(name);
        btn.setStyle(style);
        btn.setSize(w, h);
        btn.setSizePolicy(policy, policy);
        btn.setItemName(name);
        btn.setShadow(5, 0, 3, new Color(0, 0, 0, 150));

        return btn;
    }
}
