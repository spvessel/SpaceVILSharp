using System;
using System.Drawing;
using System.Linq;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class TreeView : ListBox
    {
        public EventCommonMethod EventSortTree;

        internal int _maxWrapperWidth = 0;

        internal TreeItem _root; //nesting level = 0

        public void SetRootVisibility(bool visible)
        {
            _root.SetVisible(visible);
            GetWrapper(_root).SetVisible(visible);
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
        public bool GetRootVisibility()
        {
            return _root.IsVisible();
        }
        public void SetRootText(String text)
        {
            _root.SetText(text);
        }
        public String GetRootText()
        {
            return _root.GetText();
        }

        private static int count = 0;

        public TreeView()
        {
            SetItemName("TreeView_" + count);
            count++;
            _root = new TreeItem(TreeItemType.Branch, "root");

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TreeView)));
            EventSortTree += OnSortTree;

            SetHScrollBarVisible(ScrollBarVisibility.AsNeeded);
        }
        public override void InitElements()
        {
            base.InitElements();
            _root._treeViewContainer = this;
            _root.IsRoot = true;
            base.AddItem(_root);
            SetRootVisibility(false);
            _root.ResetIndents();
            _maxWrapperWidth = GetWrapper(_root).GetMinWidth();
        }

        internal void RefreshWrapperWidth()
        {
            foreach (SelectionItem wrp in GetArea()._mapContent.Values)
            {
                wrp.SetMinWidth(_maxWrapperWidth);
                wrp.GetContent().SetMinWidth(_maxWrapperWidth);
            }
        }

        internal void RefreshTree(TreeItem prev, TreeItem item)
        {
            List<IBaseItem> list = GetListContent();
            int index = GetListContent().IndexOf(prev) + 1;
            int nestLev = item._nesting_level;
            while (index < list.Count)
            {
                if (((TreeItem)list[index])._nesting_level <= nestLev)
                    break;
                index++;
            }
            InsertItem(item, index);
            item.ResetIndents();
            // item.OnToggleHide(true);
            UpdateElements();
        }

        public override void SetListContent(List<IBaseItem> content)
        {
            GetArea().RemoveAllItems();
            foreach (IBaseItem item in content)
            {
                AddItem(item);
            }
        }

        void OnSortTree()
        {
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

            return ti1.GetText().CompareTo(ti2.GetText());
        }

        public override void AddItem(IBaseItem item)
        {
            _root.AddItem(item);
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            //additional
        }

        public override void Clear()
        {
            base.Clear();
            _root.RemoveAllChildren();
            base.AddItem(_root);
            _root.ResetIndents();
            _maxWrapperWidth = GetWrapper(_root).GetMinWidth();

        }
        public override void RemoveItem(IBaseItem item)
        {
            if (item.Equals(_root))
                return;
            base.RemoveItem(item);
        }
    }
}
