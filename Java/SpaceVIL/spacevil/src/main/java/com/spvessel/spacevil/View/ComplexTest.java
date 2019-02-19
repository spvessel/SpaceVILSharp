package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Flags.*;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.TreeItemType;
import com.spvessel.spacevil.Frame;

import java.awt.*;
import java.util.*;

public class ComplexTest extends ActiveWindow {

    Grid block;

    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout("ComplexTest", "ComplexTest", 1200, 700, true);
        setHandler(Handler);

        Handler.setMinSize(500, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

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
        block = new Grid(2, 2);
        TreeView treeview = new TreeView();
        treeview.setMinWidth(100);

        ButtonCore b1 = getButton("b1", 26, 30, SizePolicy.FIXED);
        InterfaceMouseMethodState b1_click = (sender, args) -> treeview.setRootVisibility(false);
        b1.eventMouseClick.add(b1_click);
        ButtonCore b2 = getButton("b2", 26, 30, SizePolicy.FIXED);
        InterfaceMouseMethodState b2_click = (sender, args) -> treeview.setRootVisibility(true);
        b2.eventMouseClick.add(b2_click);
        ButtonCore b3 = getButton("b3", 26, 30, SizePolicy.FIXED);
        b3.eventMouseClick.add((sender, args) -> treeview.addItem(getTreeBranch()));
        ButtonCore b4 = getButton("b4", 26, 30, SizePolicy.FIXED);
        b4.eventMouseClick.add((sender, args) -> treeview.addItem(getTreeLeaf()));
        ButtonCore b5 = getButton("b5", 26, 30, SizePolicy.FIXED);
        b5.eventMouseClick.add((sender, args) -> block.setFormat(block.getRowCount() + 1, block.getColumnCount() + 1));
        ButtonCore b6 = getButton("b6", 26, 30, SizePolicy.FIXED);
        b6.eventMouseClick.add((sender, args) -> fillBlocks());
        toolbar.addItems(b1, b2, b3, b4, b5, b6);

        // HorizontalSplitArea split_area = new HorizontalSplitArea();
        VerticalSplitArea split_area = new VerticalSplitArea();
        split_area.setMargin(0, 72, 0, 0);
        Handler.addItem(split_area);
        split_area.setSplitHolderPosition(300);

        split_area.assignLeftItem(treeview);

        TabView tabs = new TabView();
        tabs.setMinWidth(300);
        split_area.assignRightItem(tabs);
        tabs.addTab("Blocks");
        tabs.addTab("Keyboard");
        tabs.addTab("Mouse");
        tabs.addTab("Joystick");

        Frame frame_1 = new Frame();
        frame_1.setBackground(0, 255, 0, 30);
        Frame frame_2 = new Frame();
        frame_2.setBackground(0, 0, 255, 30);
        // split_area.assignTopItem(frame_1);
        // split_area.assignBottomItem(frame_2);
        // split_area.assignLeftItem(frame_1);
        // split_area.assignRightItem(frame_2);

        // Grid
        Grid grid = new Grid(3, 7);
        grid.setBackground(new Color(71, 71, 71));

        tabs.addItemToTab("Blocks", block);
        tabs.addItemToTab("Keyboard", grid);
        tabs.addItemToTab("Mouse", getButton("Mouse Tab", 200, 300, SizePolicy.FIXED));
        tabs.addItemToTab("Joystick", getButton("Joystick Tab", 500, 100, SizePolicy.FIXED));

        grid.setSpacing(6, 6);

        grid.insertItem(getButton("Button1", 100, 100, SizePolicy.EXPAND), 0, 0);
        grid.insertItem(getButton("Button2", 100, 100, SizePolicy.EXPAND), 0, 1);
        grid.insertItem(getButton("Button3", 100, 100, SizePolicy.EXPAND), 0, 2);
        grid.insertItem(getButton("Button4", 100, 100, SizePolicy.EXPAND), 0, 3);
        grid.insertItem(getButton("Button5", 100, 100, SizePolicy.EXPAND), 0, 4);
        grid.insertItem(getButton("Button6", 100, 100, SizePolicy.EXPAND), 0, 5);
        grid.insertItem(getButton("Button7", 100, 100, SizePolicy.EXPAND), 0, 6);

        for (int i = 0; i < 4; i++) {
            ButtonCore btn = getButton("Block", 100, 100, SizePolicy.EXPAND);
            btn.eventMouseClick.add((sender, args) -> btn.getParent().removeItem(btn));
            block.addItem(btn);
        }
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
        style.font = new Font("Courier New", Font.PLAIN, 16);
        style.width = w;
        style.minWidth = 30;
        style.maxWidth = 150;
        style.widthPolicy = policy;
        style.height = h;
        style.minHeight = 30;
        style.maxHeight = 100;
        style.heightPolicy = policy;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.HCENTER));
        style.textAlignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.HCENTER));
        style.margin = new Indents(3, 3, 3, 3);
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
//        item.setText(item.getItemName());
        return item;
    }

    private TreeItem getTreeLeaf() {
        TreeItem item = new TreeItem(TreeItemType.LEAF, "name1");
        item.setFontSize(15);
//        item.setText(item.getItemName());
        return item;
    }
}
