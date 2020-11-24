package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.VisibilityPolicy;
import com.spvessel.spacevil.Flags.TreeItemType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * TreeView is special container designed to show content as tree view
 * structure.
 * <p>
 * Extended from com.spvessel.spacevil.ListBox.
 * <p>
 * Supports all events except drag and drop.
 */
public class TreeView extends ListBox {
    public EventCommonMethod eventSortTree = new EventCommonMethod();

    /**
     * Disposing TreeView resources if it was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void release() {
        eventSortTree.clear();
    }

    TreeItem _root; // nesting level = 0

    int maxWrapperWidth = 0;

    /**
     * Setting the root (head) com.spvessel.spacevil.TreeItem is visible or
     * invisible.
     * 
     * @param visible True: if you want root com.spvessel.spacevil.TreeItem to be
     *                visible. False: if you want root
     *                com.spvessel.spacevil.TreeItem to be invisible.
     */
    public void setRootVisible(boolean visible) {
        if (_root == null)
            return;
        _root.setVisible(visible);
        getWrapper(_root).setVisible(visible);
        // reset all paddings for content
        List<IBaseItem> list = getListContent();
        if (list != null) {
            for (IBaseItem item : list) {
                if (item instanceof TreeItem) {
                    if (item.isVisible()) {
                        TreeItem tmp = (TreeItem) item;
                        tmp.resetIndents();
                    }
                }
            }
        }
        updateElements();
    }

    /**
     * Returns True if root (head) com.spvessel.spacevil.TreeItem is visible
     * otherwise returns False.
     * 
     * @return True: if root com.spvessel.spacevil.TreeItem is visible. False: if
     *         root com.spvessel.spacevil.TreeItem is invisible.
     */
    public boolean isRootVisible() {
        if (_root == null)
            return false;
        return _root.isVisible();
    }

    /**
     * Setting text to root (head) com.spvessel.spacevil.TreeItem of TreeView.
     * 
     * @param text Text for root.
     */
    public void setRootText(String text) {
        if (_root == null)
            return;
        _root.setText(text);
    }

    /**
     * Getting text of root (head) com.spvessel.spacevil.TreeItem of TreeView.
     * 
     * @return Text of root.
     */
    public String getRootText() {
        if (_root == null)
            return "";
        return _root.getText();
    }

    /**
     * Getting root (head) com.spvessel.spacevil.TreeItem of TreeView.
     * 
     * @return Root as com.spvessel.spacevil.TreeItem.
     */
    public TreeItem getRootItem() {
        return _root;
    }

    /**
     * Setting new root (head) com.spvessel.spacevil.TreeItem for TreeView.
     * 
     * @param rootTreeItem New root as com.spvessel.spacevil.TreeItem.
     */
    public void setRootItem(TreeItem rootTreeItem) {
        if (_root != null)
            removeItem(_root);
        addItem(rootTreeItem);
    }

    private static int count = 0;

    /**
     * Default TreeView constructor.
     */
    public TreeView() {
        setItemName("TreeView_" + count);
        count++;
        _root = new TreeItem(TreeItemType.Branch, "root");
        _root.setItemName("root");

        setStyle(DefaultsService.getDefaultStyle(TreeView.class));
        eventSortTree.add(this::onSortTree);

        setHScrollBarPolicy(VisibilityPolicy.AsNeeded);
    }

    /**
     * Initializing all elements in the TreeView.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        super.initElements();
        _root._treeViewContainer = this;
        _root.setRoot(true);
        _root.getIndicator().setToggled(true);
        super.addItem(_root);
        setRootVisible(false);

        // _root.resetIndents();
        maxWrapperWidth = getWrapper(_root).getMinWidth();
    }

    void refreshWrapperWidth() {
        for (SelectionItem wrp : getArea()._mapContent.values()) {
            wrp.setMinWidth(maxWrapperWidth);
            wrp.getContent().setMinWidth(maxWrapperWidth);
        }
    }

    void refreshTree(TreeItem prev, TreeItem item) {
        List<IBaseItem> list = getListContent();
        int index = getListContent().indexOf(prev) + 1;
        int nestLev = item._nestingLevel;
        while (index < list.size()) {
            if (((TreeItem) list.get(index))._nestingLevel <= nestLev)
                break;
            index++;
        }
        insertItem(item, index);
        item.resetIndents();
        // item.onToggleHide(true);
        updateElements();
    }

    /**
     * Adding all elements in the list area of TreeView from the given list.
     * 
     * @param content List of items as
     *                List&lt;com.spvessel.spacevil.Core.IBaseItem&gt;
     */
    @Override
    public void setListContent(List<IBaseItem> content) {
        getArea().clear();

        for (IBaseItem item : content) {
            addItem(item);
        }
    }

    /**
     * Updating all TreeView inner items.
     */
    @Override
    public void updateElements() {
        maxWrapperWidth = 0;
        for (SelectionItem wrp : getArea()._mapContent.values()) {
            if (!wrp.isVisible())
                continue;

            if (wrp.getContent() instanceof TreeItem) {
                ((TreeItem) wrp.getContent()).resetIndents();
                int actualWrpWidth = getWidth() - (getArea().getPadding().left + getArea().getPadding().right)
                        - (wrp.getMargin().left + wrp.getMargin().right);
                wrp.setWidth(actualWrpWidth - (vScrollBar.isDrawable() ? vScrollBar.getWidth() : 0));
            }
        }
        super.updateElements();
    }

    private void onSortTree() {
        if (_root == null)
            return;
        // sorting
        List<TreeItem> outList = new LinkedList<>();
        outList.add(_root);
        outList.addAll(sortHelper(_root));

        List<IBaseItem> list = new LinkedList<>();
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
            if (ti.getItemType() == TreeItemType.Branch)
                outList.addAll(sortHelper(ti));
        }

        return outList;
    }

    class CompareInAlphabet implements Comparator<TreeItem> {
        @Override
        public int compare(TreeItem ti1, TreeItem ti2) {
            if (ti1.getItemType() != ti2.getItemType()) {
                if (ti1.getItemType() == TreeItemType.Branch)
                    return -1;
                else
                    return 1;
            }
            return ti1.getText().compareToIgnoreCase(ti2.getText());
        }
    }

    Comparator<TreeItem> getComparator() {
        return new CompareInAlphabet();
    }

    /**
     * Adding a node to the TreeView.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     */
    @Override
    public void addItem(IBaseItem item) {
        if (_root == null) {
            if (item instanceof TreeItem) {
                _root = (TreeItem) item;
                _root._treeViewContainer = this;
                _root.setRoot(true);
                _root.getIndicator().setToggled(true);
                super.addItem(_root);
                setRootVisible(false);

                maxWrapperWidth = getWrapper(_root).getMinWidth();
            }
            // exception: ///////
        } else
            _root.addItem(item);
    }

    /**
     * Setting style of the TreeView.
     * <p>
     * Inner styles: "area", "vscrollbar", "hscrollbar", "menu".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        // additional

    }

    /**
     * Removing all items from the list area of TreeView.
     */
    @Override
    public void clear() {
        _root.removeChildren();
        super.clear();
        super.addItem(_root);
        setRootVisible(isRootVisible());
        maxWrapperWidth = getWrapper(_root).getMinWidth();
    }

    /**
     * Removing the specified item from the list area of TreeView.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(IBaseItem item) {
        if (item == null)
            return false;
        if (item.equals(_root)) {
            _root.removeChildren();
            _root = null;
        }
        return super.removeItem(item);
    }

    /**
     * Sorting TreeView nodes in internal list area starting with root (head).
     */
    public void sortTree() {
        sortBrunch(_root);
    }

    /**
     * Sorting part of TreeView content starting with specified branch node.
     * 
     * @param branch Branch node as com.spvessel.spacevil.TreeItem.
     */
    public void sortBrunch(TreeItem branch) {
        if (branch == null)
            return;
        if (branch.getItemType().equals(TreeItemType.Leaf)) {
            return; // Либо сделать, чтобы сортировалась родительская ветвь?
        }

        List<IBaseItem> list = getArea().getItems();
        Map<Integer, List<SelectionItem>> savedMap = new HashMap<>();

        int indFirst = list.indexOf(getWrapper(branch)) + 1;
        int nestLev = branch._nestingLevel + 1;
        int indLast = indFirst;
        int maxLev = nestLev;

        while (indLast < list.size()) {
            SelectionItem si = ((SelectionItem) list.get(indLast));
            int stiLev = ((TreeItem) si.getContent())._nestingLevel;
            if (stiLev < nestLev)
                break;

            if (maxLev < stiLev)
                maxLev = stiLev;

            if (!savedMap.containsKey(stiLev)) {
                List<SelectionItem> l1 = new LinkedList<>();
                savedMap.put(stiLev, l1);
            }

            savedMap.get(stiLev).add(si);

            list.remove(indLast);
        }

        Comparator<TreeItem> comp = getComparator();

        for (int i = nestLev; i <= maxLev; i++) {
            List<SelectionItem> siList = savedMap.get(i);
            if (siList == null)
                continue;

            for (SelectionItem selIt : siList) {
                TreeItem curItm = ((TreeItem) selIt.getContent());
                TreeItem parItm = curItm.getParentBranch();
                int parNum = list.indexOf(getWrapper(parItm));

                int ind = parNum;

                for (int ii = parNum + 1; ii < list.size(); ii++) {
                    TreeItem tmpItm = (TreeItem) ((SelectionItem) list.get(ii)).getContent();
                    if (tmpItm._nestingLevel <= parItm._nestingLevel)
                        break;

                    int out = comp.compare(tmpItm, curItm);
                    if (out > 0)
                        break;
                    ind = ii;
                }

                list.add(ind + 1, selIt);
            }
        }

        getArea().setContent(list);

        updateElements();
    }
}