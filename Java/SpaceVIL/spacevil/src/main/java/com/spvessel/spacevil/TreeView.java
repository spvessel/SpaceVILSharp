package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.VisibilityPolicy;
import com.spvessel.spacevil.Flags.TreeItemType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class TreeView extends ListBox {
    public EventCommonMethod eventSortTree = new EventCommonMethod();

    @Override
    public void release() {
        eventSortTree.clear();
    }

    TreeItem _root; // nesting level = 0

    int maxWrapperWidth = 0;

    /**
     * Is root item visible
     */
    public void setRootVisible(boolean visible) {
        if (_root == null)
            return;
        _root.setVisible(visible);
        getWrapper(_root).setVisible(visible);
        // reset all paddings for content
        List<InterfaceBaseItem> list = getListContent();
        if (list != null) {
            for (InterfaceBaseItem item : list) {
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

    public boolean isRootVisible() {
        if (_root == null)
            return false;
        return _root.isVisible();
    }

    public void setRootText(String text) {
        if (_root == null)
            return;
        _root.setText(text);
    }

    public String getRootText() {
        if (_root == null)
            return "";
        return _root.getText();
    }

    public TreeItem getRootItem() {
        return _root;
    }

    public void setRootItem(TreeItem rootTreeItem) {
        if (_root != null)
            removeItem(_root);
        addItem(rootTreeItem);
    }

    private static int count = 0;

    /**
     * Constructs a TreeView
     */
    public TreeView() {
        setItemName("TreeView_" + count);
        count++;
        _root = new TreeItem(TreeItemType.BRANCH, "root");
        _root.setItemName("root");

        setStyle(DefaultsService.getDefaultStyle(TreeView.class));
        eventSortTree.add(this::onSortTree);

        setHScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
    }

    /**
     * Initialization and adding of all elements in the TreeView
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
        getArea().clear();

        for (InterfaceBaseItem item : content) {
            addItem(item);
        }
    }

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
            return ti1.getText().compareToIgnoreCase(ti2.getText());
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
        setRootVisible(isRootVisible());
        maxWrapperWidth = getWrapper(_root).getMinWidth();
    }

    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        if (item == null)
            return false;
        if (item.equals(_root)) {
            _root.removeChildren();
            _root = null;
        }
        return super.removeItem(item);
    }

    public void sortTree() {
        sortBrunch(_root);
    }

    public void sortBrunch(TreeItem branch) {
        if (branch == null)
            return;
        if (branch.getItemType().equals(TreeItemType.LEAF)) {
            return; // Либо сделать, чтобы сортировалась родительская ветвь?
        }

        List<InterfaceBaseItem> list = getArea().getItems();
        Map<Integer, List<SelectionItem>> savedMap = new HashMap<>();

        int indFirst = list.indexOf(getWrapper(branch)) + 1;
        int nestLev = branch._nesting_level + 1;
        int indLast = indFirst;
        int maxLev = nestLev;

        while (indLast < list.size()) {
            SelectionItem si = ((SelectionItem) list.get(indLast));
            int stiLev = ((TreeItem) si.getContent())._nesting_level;
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
                    if (tmpItm._nesting_level <= parItm._nesting_level)
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