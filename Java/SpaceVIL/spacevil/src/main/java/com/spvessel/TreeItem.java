package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.InterfaceBaseItem;
import com.spvessel.Core.InterfaceItem;
import com.spvessel.Core.KeyArgs;
import com.spvessel.Core.MouseArgs;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.InputEventType;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.KeyCode;
import com.spvessel.Flags.MouseButton;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Flags.TreeItemType;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.LinkedList;

public class TreeItem extends Prototype {
    private List<TreeItem> _list_inners;

    /**
     * @return list of the TreeItem items
     */
    public List<TreeItem> getTreeItems() {
        return _list_inners;
    }

    private TreeItem _branch;
    TreeView _parent;
    int _nesting_level = 0;
    private int _indent_size = 20;

    /**
     * Indent size of the TreeItem
     */
    public int getIndentSize() {
        return _indent_size;
    }
    public void setIndentSize(int size) {
        _indent_size = size;
        resetIndents();
    }

    /**
     * Is the TreeItem root
     */
    public boolean isRoot = false;
    long index = 0;
    private TreeItemType _item_type;

    /**
     * @return TreeItem type (LEAF, BRANCH)
     */
    public TreeItemType getItemType() {
        return _item_type;
    }

    static int count = 0;
    private Label _text_object;
    private ButtonToggle _indicator;

    /**
     * @return TreeItem indicator ButtonToggle type for styling
     */
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

    /**
     * Constructs a TreeItem type of TreeItemType (LEAF or BRANCH)
     */
    public TreeItem(TreeItemType type) {
        _item_type = type;
        setItemName(type.toString().toLowerCase() + "_v" + count);
        count++;

        _list_inners = new LinkedList<TreeItem>();
        _indicator = new ButtonToggle();
        _indicator.setItemName("Indicator_" + count);
        _text_object = new Label();
        _text_object.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        _icon_shape = new CustomShape();

        setStyle(DefaultsService.getDefaultStyle(TreeItem.class));
        setPassEvents(false, InputEventType.MOUSE_PRESS, InputEventType.MOUSE_RELEASE);

        eventKeyPress.add(this::onKeyPress);
    }

    /**
     * Constructs a TreeItem
     * @param type item type (LEAF or BRUNCH)
     * @param text item text
     */
    public TreeItem(TreeItemType type, String text) {
        this(type);
        setText(text);
    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.key == KeyCode.ENTER)
            _indicator.eventToggle.execute(sender, new MouseArgs());
        if (args.key == KeyCode.MENU) {
            MouseArgs margs = new MouseArgs();
            margs.button = MouseButton.BUTTON_RIGHT;
            margs.position.setPosition(getX() + _parent.getWidth() / 2, getY() + getHeight());
            if (_menu != null)
                _menu.show(this, margs);
        }
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

    void resetIndents() {
        int level = _nesting_level;
        if (!_parent._root.isVisible())
            level--;
        setPadding(2 + _indent_size * level, 0, 0, 0);
        int width = getPadding().left + 10;
        for (InterfaceBaseItem item : getItems()) {
            width += item.getWidth() + item.getMargin().left + item.getMargin().right + getSpacing().horizontal;
        }
        setMinWidth(width - getSpacing().horizontal);
    }

    /**
     * Initialization and adding of all elements in the TreItem
     */
    @Override
    public void initElements() {
        _menu = new ContextMenu(getHandler());
        _menu.setBackground(40, 40, 40);
        _menu.setPassEvents(false);

        MenuItem remove = new MenuItem("Remove");
        remove.setForeground(new Color(210, 210, 210));
        remove.eventMouseClick.add((sender, args) -> {
            getParent().removeItem(this);
        });

        MenuItem rename = new MenuItem("Rename");
        rename.setForeground(new Color(210, 210, 210));

        MenuItem copy = new MenuItem("Copy");
        copy.setForeground(new Color(210, 210, 210));

        MenuItem paste = new MenuItem("Paste");
        paste.setForeground(new Color(210, 210, 210));

        MenuItem new_leaf = new MenuItem("New Leaf");
        new_leaf.setForeground(new Color(210, 210, 210));
        new_leaf.eventMouseClick.add((sender, args) -> {
            this.addItem(getTreeLeaf());
        });

        MenuItem new_branch = new MenuItem("New Branch");
        new_branch.setForeground(new Color(210, 210, 210));
        new_branch.eventMouseClick.add((sender, args) -> {
            this.addItem(getTreeBranch());
        });
        _menu.returnFocus = _parent.getArea();
        eventMouseClick.add((sender, args) -> {
            _menu.show(sender, args);
            _parent.getArea().eventMouseClick.execute(_parent, args);
        });

        switch (_item_type) {
        case LEAF:
            _icon_shape.setMargin(2, 0, 0, 0);
            super.addItem(_icon_shape);
            super.addItem(_text_object);
            _menu.addItems(rename, remove, copy);
            break;

        case BRANCH:
            _indicator.eventToggle.add((sender, args) -> onToggleHide(_indicator.isToggled()));
            _indicator.setPassEvents(false, InputEventType.MOUSE_PRESS, InputEventType.MOUSE_RELEASE);
            _indicator.isFocusable = false;
            eventMouseDoubleClick.add((sender, args) -> {
                if (args.button == MouseButton.BUTTON_LEFT)
                    _indicator.eventToggle.execute(sender, args);
            });

            super.addItem(_indicator);
            super.addItem(_icon_shape);
            super.addItem(_text_object);
            _menu.addItems(new_branch, new_leaf, rename, paste);
            break;
        default:
            super.addItem(_text_object);
            break;
        }
        _text_object.isFocusable = false;
        resetIndents();
    }

    void onToggleHide(boolean value) // refactor
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

    /**
     * Add item to the TreeItem
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        if (item instanceof TreeItem) {
            TreeItem tmp = (TreeItem) item;
            addTreeItem(tmp);
        }
    }

    /**
     * Set TreeItem width
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    /**
     * Set TreeItem X position
     */
    @Override
    public void setX(int _x) {
        super.setX(_x);
        updateLayout();
    }

    /**
     * Update TreeItem states
     */
    public void updateLayout() {
        // update self width
        int offset = 0;
        int startX = getX() + getPadding().left;

        for (InterfaceBaseItem child : getItems()) {
            child.setX(startX + offset + child.getMargin().left);
            if (child.getWidthPolicy() == SizePolicy.EXPAND) {
                child.setWidth(getWidth() - offset);
            }
            offset += child.getWidth() + getSpacing().horizontal;
        }
    }

    // text init
    /**
     * Text alignment in the TreeItem
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }
    public void setTextAlignment(ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }

    /**
     * Text margin in the TreeItem
     */
    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    /**
     * Text font parameters in the TreeItem
     */
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

    /**
     * TreeItem text
     */
    public void setText(String text) {
        _text_object.setText(text);
        _text_object.setWidth(_text_object.getTextWidth());
        updateLayout();
    }
    public String getText() {
        return _text_object.getText();
    }

    /**
     * @return TreeItem's text width
     */
    public int getTextWidth() {
        return _text_object.getWidth();
    }

    /**
     * Text color in the TreeItem
     */
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

    /**
     * Set style of the TreeItem
     */
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