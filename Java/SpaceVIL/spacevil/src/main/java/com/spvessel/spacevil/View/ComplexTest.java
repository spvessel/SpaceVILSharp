package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Flags.*;
import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.TreeItemType;
import com.spvessel.spacevil.Common.DefaultsService;

import java.awt.*;
import java.util.*;

public class ComplexTest extends ActiveWindow {

    TreeView treeview;
    Grid block;
    WrapGrid wrap;
    int count = 0;

    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout("ComplexTest", "ComplexTest", 950, 700, true);
        setHandler(Handler);

        Handler.setMinSize(500, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        treeview = new TreeView();
        block = new Grid(4, 4);
        wrap = new WrapGrid(100, 100, Orientation.VERTICAL);

        TitleBar title = new TitleBar("ComplexTest");
        Handler.addItem(title);

        HorizontalStack toolbar = new HorizontalStack();
        toolbar.setBackground(51, 51, 51);
        toolbar.setItemName("toolbar");
        toolbar.setSpacing(6, 0);
        toolbar.setMargin(0, 30, 0, 0);
        toolbar.setHeight(40);
        toolbar.setPadding(10, 0, 0, 0);
        toolbar.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        Handler.addItem(toolbar);
        TreeView treeview = new TreeView();
        treeview.setItemName("TreeView Fucker!");
        treeview.setMinWidth(100);

        ButtonCore b1 = getButton("b1", 26, 30, SizePolicy.FIXED);
        b1.eventMouseClick.add((sender, args) -> {
            treeview.setRootVisibility(!treeview.getRootVisibility());
        });
        ButtonCore b2 = getButton("b2", 26, 30, SizePolicy.FIXED);
        b2.eventMouseClick.add((sender, args) -> {
            treeview.addItem(getTreeBranch());
        });
        ButtonCore b3 = getButton("b3", 26, 30, SizePolicy.FIXED);
        b3.eventMouseClick.add((sender, args) -> {
            treeview.addItem(getTreeLeaf());
        });
        ButtonCore b4 = getButton("b4", 26, 30, SizePolicy.FIXED);
        b4.eventMouseClick.add((sender, args) -> {
            treeview.clear();
        });
        ButtonCore b5 = getButton("b5", 26, 30, SizePolicy.FIXED);
        b5.eventMouseClick.add((sender, args) -> {
            block.setFormat(block.getRowCount() + 1, block.getColumnCount() + 1);
        });
        ButtonCore b6 = getButton("b6", 26, 30, SizePolicy.FIXED);
        b6.eventMouseClick.add((sender, args) -> {
            // fillBlocks();
            wrap.addItem(getButton("Wrap" + count++, 100, 100, SizePolicy.EXPAND));
        });
        toolbar.addItems(b1, b2, b3, b4, b5, b6);

        Style btn_style = Style.getButtonCoreStyle();
        btn_style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        btn_style.setStyle(b1, b2, b5, b6);

        // HorizontalSplitArea split_area = new HorizontalSplitArea();
        VerticalSplitArea split_area = new VerticalSplitArea();
        split_area.setMargin(0, 72, 0, 0);
        Handler.addItem(split_area);
        split_area.setSplitHolderPosition(300);

        split_area.assignLeftItem(treeview);

        TabView tabs = new TabView();
        tabs.setMinWidth(300);
        split_area.assignRightItem(tabs);
        tabs.addTab("WrapArea");
        tabs.addTab("Grid");

        tabs.addItemToTab("WrapArea", wrap);
        tabs.addItemToTab("Grid", block);

        block.insertItem(getButton("Button1", 100, 100, SizePolicy.EXPAND), 0, 0);
        block.insertItem(getButton("Button2", 100, 100, SizePolicy.EXPAND), 0, 1);
        block.insertItem(getButton("Button3", 100, 100, SizePolicy.EXPAND), 0, 2);
        block.insertItem(getButton("Button4", 100, 100, SizePolicy.EXPAND), 0, 3);
        block.insertItem(getButton("Button5", 100, 100, SizePolicy.EXPAND), 0, 4);
        block.insertItem(getButton("Button6", 100, 100, SizePolicy.EXPAND), 0, 5);
        block.insertItem(getButton("Button7", 100, 100, SizePolicy.EXPAND), 0, 6);
        block.insertItem(getButton("Button8", 100, 100, SizePolicy.EXPAND), 0, 7);

        // Handler.getWindow().eventKeyPress.add((sender, args) -> {
        // System.out.println("FocusedItem: " +
        // getHandler().getFocusedItem().getItemName());
        // });
    }

    private void fillBlocks() {
        for (int i = 0; i < 4; i++) {
            ButtonCore btn = getButton("Block", 100, 100, SizePolicy.EXPAND);
            btn.eventMouseClick.add((sender, args) -> btn.getParent().removeItem(btn));
            block.addItem(btn);
        }
    }

    private ButtonCore getButton(String name, int w, int h, SizePolicy policy) {
        // Style
        Style style = new Style();
        style.background = new Color(255, 181, 111);
        style.foreground = new Color(0, 0, 0);
        style.borderRadius = new CornerRadius(6);
        style.font = DefaultsService.getDefaultFont();//new Font("Courier New", Font.PLAIN, 16);
        style.width = w;
        style.minWidth = 30;
        style.maxWidth = 100;
        style.widthPolicy = policy;
        style.height = h;
        style.minHeight = 30;
        style.maxHeight = 100;
        style.heightPolicy = policy;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.HCENTER));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.HCENTER));
        style.margin = new Indents(5, 5, 5, 5);
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 30);
        style.addItemState(ItemStateType.HOVERED, hovered);

        ItemState pressed = new ItemState(new Color(30, 0, 0, 60));
        pressed.border.setFill(new Color(255, 255, 255));
        pressed.border.setThickness(1);
        style.addItemState(ItemStateType.PRESSED, pressed);

        ButtonCore btn = new ButtonCore(name);
        btn.setSize(w, h);
        btn.setSizePolicy(policy, policy);
        btn.setItemName(name);
        btn.setStyle(style);
        btn.setShadow(10, 3, 3, new Color(0, 0, 0, 160));
        return btn;
    }

    private TreeItem getTreeBranch() {
        TreeItem item = new TreeItem(TreeItemType.BRANCH, "name2");
        item.setFontSize(15);
        // item.setText(item.getItemName());
        return item;
    }

    private TreeItem getTreeLeaf() {
        TreeItem item = new TreeItem(TreeItemType.LEAF, "name1");
        item.setFontSize(15);
        // item.setText(item.getItemName());
        return item;
    }
}
