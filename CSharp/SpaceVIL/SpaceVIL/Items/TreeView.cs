using System;
using System.Drawing;
using System.Linq;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// TreeView is special container designed to show content as tree view structure.
    /// <para/> Extended from SpaceVIL.ListBox.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class TreeView : ListBox
    {
        /// <summary>
        /// Event that is invoked when tree view need to sort content.
        /// </summary>
        public EventCommonMethod EventSortTree;

        /// <summary>
        /// Disposing TreeView resources if it was removed.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void Release()
        {
            EventSortTree = null;
        }

        internal int MaxWrapperWidth = 0;

        internal TreeItem _root; //nesting level = 0

        /// <summary>
        /// Setting the root (head) SpaceVIl.TreeItem is visible or invisible.
        /// </summary>
        /// <param name="value">True: if you want root SpaceVIl.TreeItem to be visible.
        /// False: if you want root SpaceVIl.TreeItem to be invisible.</param>
        public void SetRootVisible(bool value)
        {
            if (_root == null)
                return;
            _root.SetVisible(value);
            GetWrapper(_root).SetVisible(value);
            //reset all paddings for content
            List<IBaseItem> list = GetListContent();
            if (list != null)
            {
                foreach (var item in list)
                {
                    TreeItem tmp = item as TreeItem;
                    if (tmp != null)
                        tmp.ResetIndents();
                }
            }
            UpdateElements();
        }

        /// <summary>
        /// Returns True if root (head) SpaceVIl.TreeItem is visible otherwise returns False.
        /// </summary>
        /// <returns>True: if root SpaceVIl.TreeItem is visible.
        /// False: if root SpaceVIl.TreeItem is invisible.</returns>
        public bool IsRootVisible()
        {
            if (_root == null)
                return false;
            return _root.IsVisible();
        }

        /// <summary>
        /// Setting text to root (head) SpaceVIl.TreeItem of TreeView.
        /// </summary>
        /// <param name="text">Text for root.</param>
        public void SetRootText(String text)
        {
            if (_root == null)
                return;
            _root.SetText(text);
        }

        /// <summary>
        /// Getting text of root (head) SpaceVIl.TreeItem of TreeView.
        /// </summary>
        /// <returns>Text of root.</returns>
        public String GetRootText()
        {
            if (_root == null)
                return "";
            return _root.GetText();
        }

        /// <summary>
        /// Getting root (head) SpaceVIl.TreeItem of TreeView.
        /// </summary>
        /// <returns>Root as SpaceVIl.TreeItem.</returns>
        public TreeItem GetRootItem()
        {
            return _root;
        }

        /// <summary>
        /// Setting new root (head) SpaceVIl.TreeItem for TreeView.
        /// </summary>
        /// <param name="rootTreeItem">New root as SpaceVIl.TreeItem.</param>
        public void SetRootItem(TreeItem rootTreeItem)
        {
            if (_root != null)
                RemoveItem(_root);
            AddItem(rootTreeItem);
        }

        private static int count = 0;

        /// <summary>
        /// Default TreeView constructor.
        /// </summary>
        public TreeView()
        {
            SetItemName("TreeView_" + count);
            count++;
            _root = new TreeItem(TreeItemType.Branch, "root");
            _root.SetItemName("Root");

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TreeView)));
            EventSortTree += OnSortTree;

            SetHScrollBarPolicy(VisibilityPolicy.AsNeeded);
        }

        /// <summary>
        /// Initializing all elements in the TreeView. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            base.InitElements();
            _root._treeViewContainer = this;
            _root.SetRoot(true);
            _root.GetIndicator().SetToggled(true);
            base.AddItem(_root);
            SetRootVisible(false);
            // _root.ResetIndents();
            MaxWrapperWidth = GetWrapper(_root).GetMinWidth();
        }

        internal void RefreshWrapperWidth()
        {
            foreach (SelectionItem wrp in GetArea()._mapContent.Values)
            {
                wrp.SetMinWidth(MaxWrapperWidth);
                wrp.GetContent().SetMinWidth(MaxWrapperWidth);
            }
        }

        internal void RefreshTree(TreeItem prev, TreeItem item)
        {
            List<IBaseItem> list = GetListContent();
            int index = GetListContent().IndexOf(prev) + 1;
            int nestLev = item._nestingLevel;
            while (index < list.Count)
            {
                if (((TreeItem)list[index])._nestingLevel <= nestLev)
                    break;
                index++;
            }
            InsertItem(item, index);
            item.ResetIndents();
            // item.OnToggleHide(true);
            UpdateElements();
        }

        /// <summary>
        /// Adding all elements in the list area of TreeView from the given list.
        /// </summary>
        /// <param name="content">List of items as IEnumerable&lt;IBaseItem&gt;</param>
        public override void SetListContent(IEnumerable<IBaseItem> content)
        {
            GetArea().Clear();
            foreach (IBaseItem item in content)
            {
                AddItem(item);
            }
        }

        /// <summary>
        /// Updating all TreeView inner items.
        /// </summary>
        public override void UpdateElements()
        {
            MaxWrapperWidth = 0;
            foreach (SelectionItem wrp in GetArea()._mapContent.Values)
            {
                if (!wrp.IsVisible())
                    continue;
                TreeItem item = wrp.GetContent() as TreeItem;
                if (item != null)
                {
                    item.ResetIndents();
                    int actualWrpWidth = GetWidth() - (GetArea().GetPadding().Left + GetArea().GetPadding().Right)
                            - (wrp.GetMargin().Left + wrp.GetMargin().Right);
                    wrp.SetWidth(actualWrpWidth - (VScrollBar.IsDrawable() ? VScrollBar.GetWidth() : 0));
                }
            }
            base.UpdateElements();
        }

        private void OnSortTree()
        {
            if (_root == null)
                return;
            //sorting
            List<TreeItem> outList = new List<TreeItem>();
            outList.Add(_root);
            outList.AddRange(SortHelper(_root));

            SetListContent(outList.Select(_ => _ as IBaseItem).ToList());
        }

        internal SelectionItem GetWrapper(TreeItem item)
        {
            return GetArea()._mapContent[item];
        }

        private List<TreeItem> SortHelper(TreeItem item)
        {
            List<TreeItem> tmpList = item.GetChildren();
            tmpList.Sort(CompareInAlphabet);
            List<TreeItem> outList = new List<TreeItem>();
            foreach (TreeItem ti in tmpList)
            {
                outList.Add(ti);
                if (ti.GetItemType() == TreeItemType.Branch)
                    outList.AddRange(SortHelper(ti));
            }

            return outList;
        }

        internal int CompareInAlphabet(TreeItem ti1, TreeItem ti2)
        {
            if (ti1.GetItemType() != ti2.GetItemType())
            {
                if (ti1.GetItemType() == TreeItemType.Branch) return -1;
                else return 1;
            }

            return ti1.GetText().ToLower().CompareTo(ti2.GetText().ToLower());
        }

        /// <summary>
        /// Adding a node to the TreeView.
        /// </summary>
        /// <param name="item">Node as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            if (_root == null)
            {
                TreeItem tmp = item as TreeItem;
                if (tmp != null)
                {
                    _root = tmp;
                    _root._treeViewContainer = this;
                    _root.SetRoot(true);
                    _root.GetIndicator().SetToggled(true);
                    base.AddItem(_root);
                    SetRootVisible(false);

                    MaxWrapperWidth = GetWrapper(_root).GetMinWidth();
                }
                //exception: ///////
            }
            else
                _root.AddItem(item);
        }

        /// <summary>
        /// Setting style of the TreeView. 
        /// <para/> Inner styles: "area", "vscrollbar", "hscrollbar", "menu".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            //additional
        }

        /// <summary>
        /// Removing all items from the list area of TreeView.
        /// </summary>
        public override void Clear()
        {
            _root.RemoveChildren();
            base.Clear();
            base.AddItem(_root);
            SetRootVisible(IsRootVisible());
            MaxWrapperWidth = GetWrapper(_root).GetMinWidth();

        }

        /// <summary>
        /// Removing the specified item from the list area of TreeView.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public override bool RemoveItem(IBaseItem item)
        {
            if (item == null)
                return false;
            if (item.Equals(_root))
            {
                _root.RemoveChildren();
                _root = null;
            }
            return base.RemoveItem(item);
        }

        /// <summary>
        /// Sorting TreeView nodes in internal list area starting with root (head).
        /// </summary>
        public void SortTree()
        {
            SortBrunch(_root);
        }

        /// <summary>
        /// Sorting part of TreeView content starting with specified branch node.
        /// </summary>
        /// <param name="branch">Branch node as SpaceVIL.TreeItem.</param>
        public void SortBrunch(TreeItem branch)
        {
            if (branch == null)
                return;
            if (branch.GetItemType().Equals(TreeItemType.Leaf))
            {
                return; // Либо сделать, чтобы сортировалась родительская ветвь?
            }

            List<IBaseItem> list = GetArea().GetItems();
            Dictionary<int, List<SelectionItem>> savedMap = new Dictionary<int, List<SelectionItem>>();

            int indFirst = list.IndexOf(GetWrapper(branch)) + 1;
            int nestLev = branch._nestingLevel + 1;
            int indLast = indFirst;
            int maxLev = nestLev;

            while (indLast < list.Count)
            {
                SelectionItem si = ((SelectionItem)list[indLast]);
                int stiLev = ((TreeItem)si.GetContent())._nestingLevel;
                if (stiLev < nestLev)
                    break;

                if (maxLev < stiLev)
                    maxLev = stiLev;

                if (!savedMap.ContainsKey(stiLev))
                {
                    List<SelectionItem> l1 = new List<SelectionItem>();
                    savedMap.Add(stiLev, l1);
                }

                savedMap[stiLev].Add(si);

                list.RemoveAt(indLast);
            }

            for (int i = nestLev; i <= maxLev; i++)
            {
                List<SelectionItem> siList = savedMap[i];
                if (siList == null)
                    continue;

                foreach (SelectionItem selIt in siList)
                {
                    TreeItem curItm = ((TreeItem)selIt.GetContent());
                    TreeItem parItm = curItm.GetParentBranch();
                    int parNum = list.IndexOf(GetWrapper(parItm));

                    int ind = parNum;

                    for (int ii = parNum + 1; ii < list.Count; ii++)
                    {
                        TreeItem tmpItm = (TreeItem)((SelectionItem)list[ii]).GetContent();
                        if (tmpItm._nestingLevel <= parItm._nestingLevel)
                            break;

                        int compRes = CompareInAlphabet(tmpItm, curItm);
                        if (compRes > 0)
                            break;
                        ind = ii;
                    }

                    list.Insert(ind + 1, selIt);
                }
            }

            GetArea().SetContent(list);

            UpdateElements();
        }
    }
}
