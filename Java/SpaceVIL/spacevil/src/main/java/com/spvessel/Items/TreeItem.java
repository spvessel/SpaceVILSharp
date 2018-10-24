package com.spvessel.Items;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.LinkedList;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.InterfaceCommonMethodState;
import com.spvessel.Cores.InterfaceMouseMethodState;
import com.spvessel.Decorations.*;
import com.spvessel.Engine.GraphicsMathService;
import com.spvessel.Flags.*;

public class TreeItem extends VisualItem {
    private List<TreeItem> _list_inners;

    public List<TreeItem> getTreeItems() {
        return _list_inners;
    }

    private TreeItem _branch;
    protected TreeView _parent;
    protected int _nesting_level = 0;
    private int _indent_size = 20;

    public int getIndentSize() {
        return _indent_size;
    }

    public void setIndentSize(int size) {
        _indent_size = size;
        resetIndents();
    }

    public boolean isRoot = false;
    protected long index = 0;
    private TreeItemType _item_type;

    public TreeItemType getItemType() {
        return _item_type;
    }

    static int count = 0;
    private Label _text_object;
    private ButtonToggle _indicator;

    public ButtonToggle getIndicator() {
        return _indicator;
    }

    private CustomShape _icon_shape;

    private void setCustomIconShape(CustomShape icon_shape) {
        // set shape
    }

    private ImageItem _icon_image;

    private void setCustomIconImage(BufferedImage icon_image) {
        // set image
    }

    private ContextMenu _menu;

    public TreeItem(TreeItemType type) {
        _item_type = type;
        setItemName(type.toString().toLowerCase() + "_v" + count);
        count++;

        _list_inners = new LinkedList<TreeItem>();
        _indicator = new ButtonToggle();
        _indicator.setItemName("Indicator_" + count);
        _text_object = new Label();
        _icon_shape = new CustomShape();

        // setStyle(DefaultsService.getDefaultStyle(typeof(SpaceVIL.TreeBranch)));
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.TreeItem.class));
        setPassEvents(false);
    }

    public TreeItem(TreeItemType type, String text) {
        this(type);
        setText(text);
    }

    private TreeItem getTreeBranch() {
        TreeItem item = new TreeItem(TreeItemType.BRANCH);
        item.setText(item.getItemName());
        return item;
    }

    private TreeItem getTreeLeaf() {
        TreeItem item = new TreeItem(TreeItemType.LEAF);
        item.setText(item.getItemName());
        return item;
    }

    protected void resetIndents() {
        int level = _nesting_level;
        if (!_parent._root.getVisible())
            level--;
        setPadding(2 + _indent_size * level, 0, 0, 0);
    }

    @Override
    public void initElements() {
        resetIndents();

        _menu = new ContextMenu(getHandler());
        _menu.setBackground(40, 40, 40);
        _menu.setPassEvents(false);

        MenuItem remove = new MenuItem("Remove");
        remove.setForeground(new Color(210, 210, 210));
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        remove.addItemState(ItemStateType.HOVERED, hovered);
        InterfaceMouseMethodState remove_click = (sender, args) -> {
            getParent().removeItem(this);
        };
        remove.eventMouseClick.add(remove_click);

        MenuItem rename = new MenuItem("Rename");
        rename.setForeground(new Color(210, 210, 210));
        rename.addItemState(ItemStateType.HOVERED, hovered);

        MenuItem copy = new MenuItem("Copy");
        copy.setForeground(new Color(210, 210, 210));
        copy.addItemState(ItemStateType.HOVERED, hovered);

        MenuItem paste = new MenuItem("Paste");
        paste.setForeground(new Color(210, 210, 210));
        paste.addItemState(ItemStateType.HOVERED, hovered);

        MenuItem new_leaf = new MenuItem("New Leaf");
        new_leaf.setForeground(new Color(210, 210, 210));
        new_leaf.addItemState(ItemStateType.HOVERED, hovered);
        InterfaceMouseMethodState leaf_click = (sender, args) -> {
            this.addItem(getTreeLeaf());
        };
        new_leaf.eventMouseClick.add(leaf_click);

        MenuItem new_branch = new MenuItem("New Branch");
        new_branch.setForeground(new Color(210, 210, 210));
        new_branch.addItemState(ItemStateType.HOVERED, hovered);
        InterfaceMouseMethodState branch_click = (sender, args) -> {
            this.addItem(getTreeBranch());
        };
        new_branch.eventMouseClick.add(branch_click);

        InterfaceMouseMethodState menu_click = (sender, args) -> _menu.show(sender, args);
        eventMouseClick.add(menu_click);

        switch (_item_type) {
        case LEAF:
            _icon_shape.setMargin(2, 0, 0, 0);
            super.addItem(_icon_shape);
            super.addItem(_text_object);

            _menu.setSize(100, 4 + 30 * 2 - 5);
            _menu.addItems(rename, remove, copy);
            break;

        case BRANCH:
            InterfaceMouseMethodState e_toggle = (sender, args) -> onToggleHide(_indicator.isToggled());
            _indicator.eventToggle.add(e_toggle);

            super.addItem(_indicator);
            super.addItem(_icon_shape);
            super.addItem(_text_object);

            _menu.setSize(100, 4 + 30 * 3 - 5);
            _menu.addItems(new_branch, new_leaf, rename, paste);
            break;
        default:
            super.addItem(_text_object);
            break;
        }
    }

    protected void onToggleHide(boolean value) // refactor
    {
        for (TreeItem item : _list_inners) {
            if (value) {
                if (_indicator.isToggled()) {
                    item.setVisible(true);
                    item.onToggleHide(value);
                }
            } else {
                item.setVisible(false);
                item.onToggleHide(value);
            }
        }
        // update layout
        _parent.updateElements();
    }

    private void addTreeItem(TreeItem item) {
        _indicator.setToggled(true);
        _list_inners.add(item);
        item._branch = this;
        item._parent = _parent;
        item._nesting_level = _nesting_level + 1;
        _parent.refreshTree(item);
    }

    @Override
    public void addItem(BaseItem item) {
        if (item instanceof TreeItem) {
            TreeItem tmp = (TreeItem) item;
            addTreeItem(tmp);
        }
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    @Override
    public void setX(int _x) {
        super.setX(_x);
        updateLayout();
    }

    public void updateLayout() {
        // update self width
        int offset = 0;
        int startX = getX() + getPadding().left;

        for (BaseItem child : getItems()) {
            child.setX(startX + offset + child.getMargin().left);
            if (child.getWidthPolicy() == SizePolicy.EXPAND) {
                child.setWidth(getWidth() - offset);
            }
            offset += child.getWidth() + getSpacing().horizontal;
        }
    }

    // text init
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    public void setFont(Font font) {
        _text_object.setFont(font);
    }

    public void setFontSize(int size) {
        _text_object.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _text_object.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _text_object.setFontFamily(font_family);
    }

    public Font getFont() {
        return _text_object.getFont();
    }

    public void setText(String text) {
        _text_object.setText(text);
        updateLayout();
    }

    public String getText() {
        return _text_object.getText();
    }

    public int getTextWidth() {
        return _text_object.getWidth();
    }

    public void setForeground(Color color) {
        _text_object.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _text_object.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _text_object.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _text_object.getForeground();
    }

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);

        // additional
        Style inner_style = style.getInnerStyle("indicator");
        if (inner_style != null) {
            _indicator.setStyle(inner_style);
        }
        if (_item_type == TreeItemType.BRANCH)
            inner_style = style.getInnerStyle("branchicon");
        else
            inner_style = style.getInnerStyle("leaficon");

        if (inner_style != null) {
            _icon_shape.setStyle(inner_style);
        }
    }
}