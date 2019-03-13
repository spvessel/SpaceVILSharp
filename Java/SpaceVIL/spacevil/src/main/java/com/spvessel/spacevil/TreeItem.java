package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Flags.TreeItemType;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Comparator;
import java.util.LinkedList;

public class TreeItem extends Prototype {
    private List<TreeItem> _list_inners;

    /**
     * @return list of the TreeItem items
     */
    public List<TreeItem> getChildren() {
        return _list_inners;
    }

    public void removeChild(TreeItem child) {
        if (_list_inners.contains(child)) {
            _list_inners.remove(child);
            child.removeAllChildren();
            _treeViewContainer.removeItem(child);
        }
    }

    void removeAllChildren() {
        for (TreeItem item : _list_inners) {
            item.removeAllChildren();
            _treeViewContainer.removeItem(item);
        }
        _list_inners.clear();
    }

    private TreeItem _parentBranch;

    public TreeItem getParentBranch() {
        return _parentBranch;
    }

    TreeView _treeViewContainer;
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

    /**
     * Constructs a TreeItem type of TreeItemType (LEAF or BRANCH)
     */
    public TreeItem(TreeItemType type) {
        _item_type = type;
        setItemName(type.toString().toLowerCase() + "_v" + count);
        count++;

        _list_inners = new LinkedList<>();
        _indicator = new ButtonToggle();
        _indicator.setItemName("Indicator_" + count);
        _text_object = new Label();
        _text_object.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        _icon_shape = new CustomShape();

        setStyle(DefaultsService.getDefaultStyle(TreeItem.class));
        eventKeyPress.add(this::onKeyPress);
    }

    /**
     * Constructs a TreeItem
     * 
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
        // else if (args.key == KeyCode.SPACE)
        //     addItem(new TreeItem(TreeItemType.BRANCH, "new branch " + count));
        // else if (args.key == KeyCode.EQUAL)
        //     addItem(new TreeItem(TreeItemType.LEAF, "new leaf " + count));
        // else if (args.key == KeyCode.DELETE)
        //     getParentBranch().removeChild(this);
    }

    void resetIndents() {
        int level = _nesting_level;
        if (!_treeViewContainer._root.isVisible())
            level--;
        setPadding(2 + _indent_size * level, 0, 0, 0);
        int width = getPadding().left + 10;
        for (InterfaceBaseItem item : getItems()) {
            width += item.getWidth() + item.getMargin().left + item.getMargin().right + getSpacing().horizontal;
        }

        int newMinWidth = width - getSpacing().horizontal;
        if (newMinWidth > _treeViewContainer._maxWrapperWidth) {
            _treeViewContainer._maxWrapperWidth = newMinWidth;
            _treeViewContainer.refreshWrapperWidth();
        } else {
            newMinWidth = _treeViewContainer._maxWrapperWidth;
        }

        setMinWidth(newMinWidth);
        _treeViewContainer.getWrapper(this).setMinWidth(newMinWidth);
    }

    /**
     * Initialization and adding of all elements in the TreItem
     */
    @Override
    public void initElements() {
        switch (_item_type) {
        case LEAF:
            _icon_shape.setMargin(2, 0, 0, 0);
            super.addItem(_icon_shape);
            super.addItem(_text_object);
            break;

        case BRANCH:
            _indicator.eventToggle.add((sender, args) -> onToggleHide(_indicator.isToggled()));
            _indicator.isFocusable = false;
            eventMouseDoubleClick.add((sender, args) -> {
                if (args.button == MouseButton.BUTTON_LEFT)
                    _indicator.eventToggle.execute(sender, args);
            });
            super.addItem(_indicator);
            super.addItem(_icon_shape);
            super.addItem(_text_object);
            break;

        default:
            super.addItem(_text_object);
            break;
        }
        _text_object.isFocusable = false;
    }

    void onToggleHide(boolean value) // refactor
    {
        for (TreeItem item : _list_inners) {
            if (value) {
                if (_indicator.isToggled()) {
                    _treeViewContainer.getWrapper(item).setVisible(true);
                    item.setVisible(true);
                    item.onToggleHide(value);
                }
            } else {
                _treeViewContainer.getWrapper(item).setVisible(false);
                item.setVisible(false);
                item.onToggleHide(value);
            }
        }
        // update layout
        _treeViewContainer.updateElements();
    }

    void addTreeItem(TreeItem item) {
        item._parentBranch = this;
        item._treeViewContainer = _treeViewContainer;
        item._nesting_level = _nesting_level + 1;
//        _indicator.setToggled(true);

        Comparator<TreeItem> comp = _treeViewContainer.getComparator();

        List<TreeItem> neighbors = getChildren();
        int ind = -1;

        for (int i = 0; i < neighbors.size(); i++) {
            int out = comp.compare(neighbors.get(i), item);
            if (out == 1)
                break;
            ind = i;
        }

        _list_inners.add(ind + 1, item);

        if (ind == -1)
            _treeViewContainer.refreshTree(this, item);
        else
            _treeViewContainer.refreshTree(_list_inners.get(ind), item);
        onToggleHide(true);

        List<TreeItem> children = item.getChildren();
        if (children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                item.addItem(children.get(i));
            }
        }
    }

    /**
     * Add item to the TreeItem
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        if (item instanceof TreeItem)
            addTreeItem((TreeItem) item);
        else
            super.addItem(item);
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
        _text_object.setWidth(_text_object.getTextWidth());
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
        // _text_object.setWidth(_text_object.getTextWidth());
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

    public void setExpanded(boolean value) {
        if (_item_type.equals(TreeItemType.BRANCH)) {
            _indicator.setToggled(value);
            onToggleHide(value);
        }
    }
}