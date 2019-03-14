using System;
using System.Drawing;
using System.Linq;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;
using System.Threading;

namespace SpaceVIL
{
    public class TreeItem : Prototype
    {
        private Object Locker = new Object();

        private List<TreeItem> _list_inners;
        public List<TreeItem> GetChildren()
        {
            return _list_inners;
        }

        public void RemoveChild(TreeItem child)
        {
            if (_list_inners.Contains(child))
            {
                _list_inners.Remove(child);
                child.RemoveChildren();
                _treeViewContainer.RemoveItem(child);
            }
        }

        public void RemoveChildren()
        {
            Monitor.Enter(Locker);
            try
            {
                foreach (var item in _list_inners)
                {
                    item.RemoveChildren();
                    _treeViewContainer.RemoveItem(item);
                }
                _list_inners.Clear();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - RemoveChildren");
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(Locker);
            }
        }

        private TreeItem _parentBranch;

        public TreeItem GetParentBranch()
        {
            return _parentBranch;
        }

        internal TreeView _treeViewContainer;
        internal int _nesting_level = 0;
        private int _indent_size = 20;
        public int GetIndentSize()
        {
            return _indent_size;
        }
        public void SetIndentSize(int size)
        {
            _indent_size = size;
            ResetIndents();
        }
        public bool IsRoot = false;
        internal UInt32 Index = 0;
        private TreeItemType _item_type;
        public TreeItemType GetItemType()
        {
            return _item_type;
        }
        static int count = 0;
        private Label _text_object;
        private ButtonToggle _indicator;
        public ButtonToggle GetIndicator()
        {
            return _indicator;
        }
        private CustomShape _icon_shape;

        public TreeItem(TreeItemType type)
        {
            _item_type = type;
            SetItemName(type.ToString().ToLower() + "_v" + count);
            count++;

            _list_inners = new List<TreeItem>();
            _indicator = new ButtonToggle();
            _indicator.SetItemName("Indicator_" + count);
            _text_object = new Label();
            _text_object.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            _icon_shape = new CustomShape();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TreeItem)));
            EventKeyPress += OnKeyPress;
        }

        public TreeItem(TreeItemType type, String text = "") : this(type)
        {
            SetText(text);
        }

        void OnKeyPress(IItem sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter)
                _indicator.EventToggle.Invoke(sender, new MouseArgs());

            // else if (args.Key == KeyCode.Space)
            //     AddItem(new TreeItem(TreeItemType.Branch, "new branch " + count));
            // else if (args.Key == KeyCode.Equal)
            // {
            //     TreeItem tr = new TreeItem(TreeItemType.Leaf, "new leaf " + count);
            //     tr.EventMouseDoubleClick += (s, a)=>{
            //         Console.WriteLine(tr.GetItemName() + " click");
            //     };
            //     AddItem(tr);
            // }

            // else if (args.Key == KeyCode.Delete)
            //     GetParentBranch().RemoveChild(this);
        }

        internal void ResetIndents()
        {
            Int32 level = _nesting_level;
            if (!_treeViewContainer._root.IsVisible())
                level--;
            SetPadding(2 + _indent_size * level, 0, 0, 0);
            int width = GetPadding().Left + 10;
            foreach (IBaseItem item in GetItems())
            {
                width += item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right + GetSpacing().Horizontal;
            }

            int newMinWidth = width - GetSpacing().Horizontal;
            if (newMinWidth > _treeViewContainer._maxWrapperWidth)
            {
                _treeViewContainer._maxWrapperWidth = newMinWidth;
                _treeViewContainer.RefreshWrapperWidth();
            }
            else
            {
                newMinWidth = _treeViewContainer._maxWrapperWidth;
            }

            SetMinWidth(newMinWidth);
            _treeViewContainer.GetWrapper(this).SetMinWidth(newMinWidth);
        }
        public override void InitElements()
        {
            _text_object.IsFocusable = false;
            switch (_item_type)
            {
                case TreeItemType.Leaf:
                    _icon_shape.SetMargin(2, 0, 0, 0);
                    base.AddItem(_icon_shape);
                    base.AddItem(_text_object);
                    break;

                case TreeItemType.Branch:
                    _indicator.EventToggle += (sender, args) => OnToggleHide(_indicator.IsToggled());
                    _indicator.IsFocusable = false;
                    EventMouseDoubleClick += (sender, args) =>
                    {
                        if (args.Button == MouseButton.ButtonLeft)
                            _indicator.EventToggle.Invoke(sender, args);
                    };
                    base.AddItems(_indicator);
                    base.AddItem(_icon_shape);
                    base.AddItem(_text_object);
                    break;

                default:
                    base.AddItem(_text_object);
                    break;
            }
        }

        internal void OnToggleHide(bool value) // refactor
        {
            foreach (var item in _list_inners)
            {
                if (value)
                {
                    if (_indicator.IsToggled())
                    {
                        _treeViewContainer.GetWrapper(item).SetVisible(true);
                        item.SetVisible(true);
                        item.OnToggleHide(value);
                    }
                }
                else
                {
                    _treeViewContainer.GetWrapper(item).SetVisible(false);
                    item.SetVisible(false);
                    item.OnToggleHide(value);
                }
            }
            //update layout
            _treeViewContainer.UpdateElements();
        }

        private void AddTreeItem(TreeItem item)
        {
            item._parentBranch = this;
            item._treeViewContainer = _treeViewContainer;
            item._nesting_level = _nesting_level + 1;
            if (!_indicator.IsToggled())
                item.SetVisible(false);

            List<TreeItem> neighbors = GetChildren();
            int ind = -1;

            for (int i = 0; i < neighbors.Count; i++)
            {
                int compareResult = _treeViewContainer.CompareInAlphabet(neighbors[i], item);
                if (compareResult == 1)
                    break;
                ind = i;
            }

            _list_inners.Insert(ind + 1, item);
            if (ind == -1)
                _treeViewContainer.RefreshTree(this, item);
            else
                _treeViewContainer.RefreshTree(_list_inners[ind], item);

            // OnToggleHide(true);
        }

        public override void AddItem(IBaseItem item)
        {
            TreeItem tmp = item as TreeItem;
            if (tmp != null)
                AddTreeItem(tmp);
            else
                base.AddItem(item);

        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }

        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateLayout();
        }

        public virtual void UpdateLayout()
        {
            _text_object.SetWidth(_text_object.GetTextWidth());
            //update self width
            int offset = 0;
            int startX = GetX() + GetPadding().Left;

            foreach (var child in GetItems())
            {
                child.SetX(startX + offset + child.GetMargin().Left);
                if (child.GetWidthPolicy() == SizePolicy.Expand)
                {
                    child.SetWidth(GetWidth() - offset);
                }
                offset += child.GetWidth() + GetSpacing().Horizontal;
            }
        }

        //text init
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }
        public void SetFont(Font font)
        {
            _text_object.SetFont(font);
        }
        public void SetFontSize(int size)
        {
            _text_object.SetFontSize(size);
        }
        public void SetFontStyle(FontStyle style)
        {
            _text_object.SetFontStyle(style);
        }
        public void SetFontFamily(FontFamily font_family)
        {
            _text_object.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _text_object.GetFont();
        }
        public void SetText(String text)
        {
            _text_object.SetText(text);
            UpdateLayout();
        }
        public String GetText()
        {
            return _text_object.GetText();
        }
        public int GetTextWidth()
        {
            return _text_object.GetWidth();
        }
        public void SetForeground(Color color)
        {
            _text_object.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return _text_object.GetForeground();
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);

            //additional
            Style inner_style = style.GetInnerStyle("indicator");
            if (inner_style != null)
            {
                _indicator.SetStyle(inner_style);
            }
            if (_item_type == TreeItemType.Branch)
                inner_style = style.GetInnerStyle("branchicon");
            else
                inner_style = style.GetInnerStyle("leaficon");

            if (inner_style != null)
            {
                _icon_shape.SetStyle(inner_style);
            }
        }

        public void SetExpanded(bool value)
        {
            if (_item_type.Equals(TreeItemType.Branch))
            {
                _indicator.SetToggled(value);
                OnToggleHide(value);
            }
        }
    }
}