package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.Flags.TreeItemType;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class TreeView extends ListBox {
    public EventCommonMethod eventSortTree = new EventCommonMethod();
    // private ContextMenu _menu;
    TreeItem _root; // nesting level = 0

    /**
     * Is root item visible
     */
    public void setRootVisibility(boolean visible) {
        _root.setVisible(visible);
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

    private static int count = 0;

    /**
     * Constructs a TreeView
     */
    public TreeView() {
        setItemName("TreeView_" + count);
        count++;
        _root = new TreeItem(TreeItemType.BRANCH, "root");

        // setStyle(DefaultsService.getDefaultStyle(typeof(SpaceVIL.TreeView)));
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
        // setSelectionVisibility(true);
        _root._parent = this;
        _root.isRoot = true;
        super.addItem(_root);
        setRootVisibility(false);

        // _menu = new ContextMenu(getHandler());
        // _menu.setBackground(40, 40, 40);
        // _menu.setPassEvents(false);

        // MenuItem paste = new MenuItem("Paste");
        // paste.setForeground(new Color(210, 210, 210));

        // MenuItem new_leaf = new MenuItem("New Leaf");
        // new_leaf.setForeground(new Color(210, 210, 210));
        // InterfaceMouseMethodState leaf_click = (sender, args) -> {
        //     this.addItem(getTreeLeaf());
        // };
        // new_leaf.eventMouseClick.add(leaf_click);

        // MenuItem new_branch = new MenuItem("New Branch");
        // new_branch.setForeground(new Color(210, 210, 210));
        // InterfaceMouseMethodState branch_click = (sender, args) -> {
        //     this.addItem(getTreeBranch());
        // };
        // new_branch.eventMouseClick.add(branch_click);

        // InterfaceMouseMethodState menu_click = (sender, args) -> _menu.show(sender, args);
        // eventMouseClick.add(menu_click);

        // _menu.addItems(new_branch, new_leaf, paste);
    }

    // public TreeItem getTreeBranch(String name) {
    //     TreeItem item = new TreeItem(TreeItemType.BRANCH);
    //     item.setText(item.getItemName());
    //     return item;
    // }

    // public TreeItem getTreeLeaf(String name) {
    //     TreeItem item = new TreeItem(TreeItemType.LEAF);
    //     item.setText(item.getItemName());
    //     return item;
    // }

    void refreshTree(TreeItem item) {
        super.addItem(item);
        onSortTree();
        updateElements();
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

    private List<TreeItem> sortHelper(TreeItem item) {
        List<TreeItem> tmpList = item.getTreeItems();
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
}