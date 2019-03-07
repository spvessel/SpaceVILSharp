package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.Flags.TreeItemType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

public class TreeView extends ListBox {
    public EventCommonMethod eventSortTree = new EventCommonMethod();

    TreeItem _root; // nesting level = 0

    int _maxWrapperWidth = 0;

    /**
     * Is root item visible
     */
    public void setRootVisibility(boolean visible) {
        _root.setVisible(visible);
        getWrapper(_root).setVisible(visible);
        // reset all paddings for content
        List<InterfaceBaseItem> list = getListContent();
        if (list != null) {
            for (InterfaceBaseItem item : list) {
                if (item instanceof TreeItem) {
                    TreeItem tmp = (TreeItem) item;
                    tmp.resetIndents();
                }
            }
        }
        updateElements();
    }

    public boolean getRootVisibility() {
        return _root.isVisible();
    }

    public void setRootText(String text) {
        _root.setText(text);
    }

    public String getRootText() {
        return _root.getText();
    }

    private static int count = 0;

    /**
     * Constructs a TreeView
     */
    public TreeView() {
        setItemName("TreeView_" + count);
        count++;
        _root = new TreeItem(TreeItemType.BRANCH, "root");

        setStyle(DefaultsService.getDefaultStyle(TreeView.class));
        eventSortTree.add(this::onSortTree);

        setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
    }

    /**
     * Initialization and adding of all elements in the TreeView
     */
    @Override
    public void initElements() {
        super.initElements();
        _root._treeViewContainer = this;
        _root.isRoot = true;
        super.addItem(_root);
        setRootVisibility(false);

        // _root.resetIndents();
        _maxWrapperWidth = getWrapper(_root).getMinWidth();
    }

    void refreshWrapperWidth() {
        for (SelectionItem wrp : getArea()._mapContent.values()) {
            wrp.setMinWidth(_maxWrapperWidth);
            wrp.getContent().setMinWidth(_maxWrapperWidth);
        }
    }

    void refreshTree(TreeItem prev, TreeItem item) {
        List<InterfaceBaseItem> list = getListContent();
        int index = getListContent().indexOf(prev) + 1;
        int nestLev = item._nesting_level;
        while (index < list.size()) {
            if (((TreeItem) list.get(index))._nesting_level <= nestLev)
                break;
            index++;
        }
        insertItem(item, index);
        item.resetIndents();
        // item.onToggleHide(true);
        updateElements();
    }

    @Override
    public void setListContent(List<InterfaceBaseItem> content) {
        getArea().removeAllItems();

        for (InterfaceBaseItem item : content) {
            addItem(item);
        }
    }

    private void onSortTree() {
        // sorting
        List<TreeItem> outList = new LinkedList<>();
        outList.add(_root);
        outList.addAll(sortHelper(_root));

        List<InterfaceBaseItem> list = new LinkedList<>();
        for (TreeItem var : outList) {
            list.add(var);
        }
        setListContent(list);
    }

    SelectionItem getWrapper(TreeItem item) {
        return getArea()._mapContent.get(item);
    }

    private List<TreeItem> sortHelper(TreeItem item) {
        List<TreeItem> tmpList = item.getChildren();

        Collections.sort(tmpList, new CompareInAlphabet());

        List<TreeItem> outList = new LinkedList<>();
        for (TreeItem ti : tmpList) {
            outList.add(ti);
            if (ti.getItemType() == TreeItemType.BRANCH)
                outList.addAll(sortHelper(ti));
        }

        return outList;
    }

    class CompareInAlphabet implements Comparator<TreeItem> {
        @Override
        public int compare(TreeItem ti1, TreeItem ti2) {
            if (ti1.getItemType() != ti2.getItemType()) {
                if (ti1.getItemType() == TreeItemType.BRANCH)
                    return -1;
                else
                    return 1;
            }
            return ti1.getText().compareTo(ti2.getText());
        }
    }

    Comparator<TreeItem> getComparator() {
        return new CompareInAlphabet();
    }

    /**
     * Add item to the TreeView
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        _root.addItem(item);
    }

    /**
     * Set style of the TreeView
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        // additional

    }

    @Override
    public void clear() {
        _root.removeChildren();
        super.clear();
        super.addItem(_root);
        setRootVisibility(getRootVisibility());
        // _root.resetIndents();
        _maxWrapperWidth = getWrapper(_root).getMinWidth();
    }

    @Override
    public void removeItem(InterfaceBaseItem item) {
        if (item.equals(_root))
            return;
        super.removeItem(item);
    }
}