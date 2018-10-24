package com.spvessel.Items;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.*;
import com.spvessel.Decorations.*;
import com.spvessel.Flags.*;

public class TreeView extends ListBox {
    public EventCommonMethod eventSortTree = new EventCommonMethod();
    private ContextMenu _menu;
    protected TreeItem _root; // nesting level = 0

    public void setRootVisibility(boolean visible) {
        _root.setVisible(visible);
        // reset all paddings for content
        List<BaseItem> list = getListContent();
        if (list != null) {
            for (BaseItem item : list) {
                if (item instanceof TreeItem) {
                    TreeItem tmp = (TreeItem) item;
                    tmp.resetIndents();
                }
            }
        }
        updateElements();
    }

    static int count = 0;

    public TreeView() {
        setItemName("TreeView_" + count);
        count++;
        _root = new TreeItem(TreeItemType.BRANCH, "root");

        // setStyle(DefaultsService.getDefaultStyle(typeof(SpaceVIL.TreeView)));
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.TreeView.class));
        InterfaceCommonMethod onSort = () -> onSortTree();
        eventSortTree.add(onSort);
    }

    @Override
    public void initElements() {
        super.initElements();
        setSelectionVisibility(true);
        _root._parent = this;
        _root.isRoot = true;
        super.addItem(_root);
        setRootVisibility(false);

        _menu = new ContextMenu(getHandler());
        _menu.setBackground(40, 40, 40);
        _menu.setPassEvents(false);

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);

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

        _menu.setSize(100, 4 + 30 * 3 - 5);
        _menu.addItems(new_branch, new_leaf, paste);
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

    protected void refreshTree(TreeItem item) {
        super.addItem(item);
        onSortTree();
        updateElements();
    }

    protected void onSortTree() {
        // sorting
        List<TreeItem> outList = new LinkedList<TreeItem>();
        outList.add(_root);
        outList.addAll(sortHelper(_root));

        List<BaseItem> list = new LinkedList<>();
        for (TreeItem var : outList) {
            list.add(var);
        }
        setListContent(list);
    }

    private List<TreeItem> sortHelper(TreeItem item) {
        List<TreeItem> tmpList = item.getTreeItems();
        Collections.sort(tmpList, new CompareInAlphabet());
        List<TreeItem> outList = new LinkedList<TreeItem>();
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

    @Override
    public void addItem(BaseItem item) {
        _root.addItem(item);
    }

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        // additional

    }
}