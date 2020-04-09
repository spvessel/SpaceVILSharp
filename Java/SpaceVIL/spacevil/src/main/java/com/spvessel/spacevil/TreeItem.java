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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * TreeItem is designed to be a node for com.spvessel.spacevil.TreeView
 * (branch-leaf type of container).
 * <p>
 * Can be as leaf node or as branch node. Branch node can contains another
 * branches and leafs. Leaf node cannot contains any nodes.
 * <p>
 * Contains text, icon, indicator (branch only).
 * <p>
 * Supports all events except drag and drop.
 */
public class TreeItem extends Prototype {
    private List<TreeItem> _list_inners;

    /**
     * Getting all contained nodes in this TreeItem.
     * 
     * @return Contained nodes as List&lt;com.spvessel.spacevil.TreeItem&gt;
     */
    public List<TreeItem> getChildren() {
        return _list_inners;
    }

    /**
     * Removing the specified node from TreeItem.
     * 
     * @param child Node as com.spvessel.spacevil.TreeItem.
     */
    public void removeChild(TreeItem child) {
        if (_list_inners.contains(child)) {
            _list_inners.remove(child);
            child.removeChildren();
            _treeViewContainer.removeItem(child);
        }
    }

    private Lock locker = new ReentrantLock();

    /**
     * Removing all contained nodes in this TreeItem.
     */
    public void removeChildren() {
        locker.lock();
        try {
            for (TreeItem item : _list_inners) {
                item.removeChildren();
                _treeViewContainer.removeItem(item);
            }
            _list_inners.clear();
        } catch (Exception ex) {
            System.out.println("Method - RemoveChildren");
            ex.printStackTrace();
        } finally {
            locker.unlock();
        }
    }

    private TreeItem _parentBranch;

    /**
     * Getting the parent branch node that contains this TreeItem.
     * 
     * @return Parent branch node as com.spvessel.spacevil.TreeItem.
     */
    public TreeItem getParentBranch() {
        return _parentBranch;
    }

    TreeView _treeViewContainer;

    int _nestingLevel = 0;

    private int _indentSize = 20;

    /**
     * Getting indent size (X axis) of the current TreeItem relative to its parent
     * branch.
     * 
     * @return Indent size (X axis).
     */
    public int getIndentSize() {
        return _indentSize;
    }

    /**
     * Setting indent size (X axis) for the current TreeItem relative to its parent
     * branch.
     * 
     * @param size Indent size (X axis).
     */
    public void setIndentSize(int size) {
        _indentSize = size;
        resetIndents();
    }

    private boolean _isRoot = false;

    /**
     * Returns True if this TreeItem is root (head) otherwise returns False.
     * 
     * @return True: if this TreeItem is root (head). False: if this TreeItem is not
     *         root (head).
     */
    public boolean isRoot() {
        return _isRoot;
    }

    void setRoot(boolean value) {
        _isRoot = value;
    }

    long index = 0;

    private TreeItemType _nodeType;

    /**
     * Getting node type.
     * <p>
     * Can be TreeItemType.LEAF or TreeItemType.BRANCH.
     * 
     * @return Node type as com.spvessel.spacevil.Flags.TreeItemType.
     */
    public TreeItemType getItemType() {
        return _nodeType;
    }

    static int count = 0;

    private Label _textLabel;

    private ButtonToggle _indicator;

    /**
     * Getting the branch node indicator of TreeItem.
     * 
     * @return Branch node indicator.
     */
    public ButtonToggle getIndicator() {
        return _indicator;
    }

    private CustomShape _iconShape;

    /**
     * Constructs TreeItem with specified type of node.
     * 
     * @param type Node type as com.spvessel.spacevil.Flags.TreeItemType.
     */
    public TreeItem(TreeItemType type) {
        if (type == null)
            type = TreeItemType.LEAF;
        _nodeType = type;
        setItemName(type.toString().toLowerCase() + "_v" + count);
        count++;

        _list_inners = new LinkedList<>();
        _indicator = new ButtonToggle();
        _indicator.setItemName("Indicator_" + count);
        _textLabel = new Label();
        _textLabel.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        _iconShape = new CustomShape();

        setStyle(DefaultsService.getDefaultStyle(TreeItem.class));
        eventKeyPress.add(this::onKeyPress);
    }

    /**
     * Constructs TreeItem with specified type of node and text.
     *
     * @param type Node type as com.spvessel.spacevil.Flags.TreeItemType.
     * @param text Text of TreeItem.
     */
    public TreeItem(TreeItemType type, String text) {
        this(type);
        setText(text);
    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.key == KeyCode.ENTER)
            _indicator.eventToggle.execute(sender, new MouseArgs());
        // else if (args.key == KeyCode.SPACE)
        // addItem(new TreeItem(TreeItemType.BRANCH, "new branch " + count));
        // else if (args.key == KeyCode.EQUAL)
        // addItem(new TreeItem(TreeItemType.LEAF, "new leaf " + count));
        // else if (args.key == KeyCode.DELETE)
        // getParentBranch().removeChild(this);
    }

    void resetIndents() {
        int level = _nestingLevel;
        if (!_treeViewContainer._root.isVisible())
            level--;
        setPadding(2 + _indentSize * level, 0, 0, 0);
        int width = getPadding().left;
        for (InterfaceBaseItem item : getItems()) {
            width += item.getWidth() + item.getMargin().left + item.getMargin().right + getSpacing().horizontal;
        }

        int newMinWidth = width - getSpacing().horizontal;
        if (newMinWidth > _treeViewContainer.maxWrapperWidth) {
            _treeViewContainer.maxWrapperWidth = newMinWidth;
            _treeViewContainer.refreshWrapperWidth();
        } else {
            newMinWidth = _treeViewContainer.maxWrapperWidth;
        }

        setMinWidth(newMinWidth);
        _treeViewContainer.getWrapper(this).setMinWidth(newMinWidth);
    }

    /**
     * Initializing all elements in the TreeItem.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        switch (_nodeType) {
            case LEAF:
                _iconShape.setMargin(2, 0, 0, 0);
                super.addItem(_iconShape);
                super.addItem(_textLabel);
                break;

            case BRANCH:
                super.addItem(_indicator);
                super.addItem(_iconShape);
                super.addItem(_textLabel);

                _indicator.eventToggle.add((sender, args) -> onToggleHide(_indicator.isToggled()));
                _indicator.isFocusable = false;
                eventMouseDoubleClick.add((sender, args) -> {
                    if (args.button == MouseButton.BUTTON_LEFT)
                        _indicator.eventToggle.execute(sender, args);
                });
                break;

            default:
                super.addItem(_textLabel);
                break;
        }
        _textLabel.isFocusable = false;
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
        item._nestingLevel = _nestingLevel + 1;
        // _indicator.setToggled(true);
        if (!_indicator.isToggled())
            item.setVisible(false);

        Comparator<TreeItem> comp = _treeViewContainer.getComparator();

        List<TreeItem> neighbors = getChildren();
        int ind = -1;

        for (int i = 0; i < neighbors.size(); i++) {
            int out = comp.compare(neighbors.get(i), item);
            if (out > 0)
                break;
            ind = i;
        }

        _list_inners.add(ind + 1, item);

        if (ind == -1)
            _treeViewContainer.refreshTree(this, item);
        else
            _treeViewContainer.refreshTree(_list_inners.get(ind), item);

        // onToggleHide(true);

        // List<TreeItem> children = item.getChildren();
        // if (children.size() > 0) {
        // recursiveAddChildren(item, children);
        // }
    }

    // private void recursiveAddChildren(TreeItem item, List<TreeItem> chiList) {
    // for (int i = 0; i < chiList.size(); i++) {
    // // item.addItem(chiList.get(i));
    // chiList.get(i).resetIndents();
    // }
    // }

    /**
     * Adding item into the TreeItem.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        if (item instanceof TreeItem)
            addTreeItem((TreeItem) item);
        else
            super.addItem(item);
    }

    /**
     * Setting item width. If the value is greater/less than the maximum/minimum
     * value of the width, then the width becomes equal to the maximum/minimum
     * value.
     * 
     * @param width Width of the item.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    /**
     * Setting X coordinate of the left-top corner of the item.
     * 
     * @param x X position of the left-top corner.
     */
    @Override
    public void setX(int x) {
        super.setX(x);
        updateLayout();
    }

    protected void updateLayout() {
        _textLabel.setWidth(_textLabel.getTextWidth() + 5);
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

    /**
     * Setting alignment of a TreeItem text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textLabel.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a TreeItem text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textLabel.setTextAlignment(alignment);
    }

    /**
     * Setting indents for the text to offset text relative to TreeItem.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textLabel.setMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to TreeItem.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        _textLabel.setMargin(left, top, right, bottom);
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return _textLabel.getMargin();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        _textLabel.setFont(font);
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        _textLabel.setFontSize(size);
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        _textLabel.setFontStyle(style);
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        _textLabel.setFontFamily(fontFamily);
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _textLabel.getFont();
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.String.
     */
    public void setText(String text) {
        _textLabel.setText(text);
    }

    /**
     * Getting the current text of the TreeItem.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return _textLabel.getText();
    }

    /**
     * Getting the text width (useful when you need resize TreeItem by text width).
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return _textLabel.getWidth();
    }

    /**
     * Getting the text height (useful when you need resize TreeItem by text
     * height).
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return _textLabel.getHeight();
    }

    /**
     * Setting text color of a TreeItem.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textLabel.setForeground(color);
    }

    /**
     * Setting text color of a TreeItem in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        _textLabel.setForeground(r, g, b);
    }

    /**
     * Setting background color of an item in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b, int a) {
        _textLabel.setForeground(r, g, b, a);
    }

    /**
     * Setting text color of a TreeItem in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        _textLabel.setForeground(r, g, b);
    }

    /**
     * Setting text color of a TreeItem in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        _textLabel.setForeground(r, g, b, a);
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return _textLabel.getForeground();
    }

    /**
     * Setting style of the TreeItem.
     * <p>
     * Inner styles: "indicator", "branchicon", "leaficon".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
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
        Style innerStyle = style.getInnerStyle("indicator");
        if (innerStyle != null) {
            _indicator.setStyle(innerStyle);
        }
        if (_nodeType == TreeItemType.BRANCH)
            innerStyle = style.getInnerStyle("branchicon");
        else
            innerStyle = style.getInnerStyle("leaficon");

        if (innerStyle != null) {
            _iconShape.setStyle(innerStyle);
        }
    }

    /**
     * Shows or hides content (contained nodes) of this TreeItem.
     * 
     * @param value True: if you want to show content. False: if you want to hide
     *              content.
     */
    public void setExpanded(boolean value) {
        if (_nodeType.equals(TreeItemType.BRANCH)) {
            _indicator.setToggled(value);
            onToggleHide(value);
        }
    }
}