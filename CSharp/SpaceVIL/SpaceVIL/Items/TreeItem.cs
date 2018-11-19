using System;
using System.Drawing;
using System.Linq;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class TreeItem : Prototype
    {
        private List<TreeItem> _list_inners;
        public List<TreeItem> GetTreeItems()
        {
            return _list_inners;
        }
        private TreeItem _branch;
        internal TreeView _parent;
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
        private void SetCustomIconShape(CustomShape icon_shape)
        {
            //set shape
        }
        private ImageItem _icon_image;
        private void SetCustomIconImage(Image icon_image)
        {
            //set image
        }
        private ContextMenu _menu;

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
            SetPassEvents(false, InputEventType.MousePress | InputEventType.MouseRelease | InputEventType.MouseDoubleClick);
            EventKeyPress += OnKeyPress;
        }

        public TreeItem(TreeItemType type, String text = "") : this(type)
        {
            SetText(text);
        }

        protected virtual void OnKeyPress(IItem sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter)
                _indicator.EventToggle.Invoke(sender, new MouseArgs());
            if (args.Key == KeyCode.Menu)
            {
                MouseArgs margs = new MouseArgs();
                margs.Button = MouseButton.ButtonRight;
                margs.Position.SetPosition(GetX() + _parent.GetWidth() / 2, GetY() + GetHeight());
                _menu?.Show(this, margs);
            }
        }

        private TreeItem GetTreeBranch()
        {
            TreeItem item = new TreeItem(TreeItemType.Branch);
            item.SetText(item.GetItemName());
            return item;
        }
        private TreeItem GetTreeLeaf()
        {
            TreeItem item = new TreeItem(TreeItemType.Leaf);
            item.SetText(item.GetItemName());
            return item;
        }

        internal void ResetIndents()
        {
            Int32 level = _nesting_level;
            if (!_parent._root.IsVisible())
                level--;
            SetPadding(2 + _indent_size * level, 0, 0, 0);
            int width = GetPadding().Left + 10;
            foreach (IBaseItem item in GetItems())
            {
                width += item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right + GetSpacing().Horizontal;
            }
            SetMinWidth(width - GetSpacing().Horizontal);
            // System.out.println(getItemName() + " " + (width - getSpacing().horizontal));
        }
        public override void InitElements()
        {

            _menu = new ContextMenu(GetHandler());
            _menu.SetBackground(40, 40, 40);
            _menu.SetPassEvents(false);
            MenuItem remove = new MenuItem("Remove");
            remove.SetForeground(Color.LightGray);
            remove.EventMouseClick += (sender, args) =>
            {
                GetParent().RemoveItem(this);
            };
            MenuItem rename = new MenuItem("Rename");
            rename.SetForeground(Color.LightGray);
            rename.EventMouseClick += (sender, args) =>
            {
                //rename
            };
            MenuItem copy = new MenuItem("Copy");
            copy.SetForeground(Color.LightGray);
            copy.EventMouseClick += (sender, args) =>
            {
                //copy
            };
            MenuItem paste = new MenuItem("Paste");
            paste.SetForeground(Color.LightGray);
            paste.EventMouseClick += (sender, args) =>
            {
                //paste
            };
            MenuItem new_leaf = new MenuItem("New Leaf");
            new_leaf.SetForeground(Color.LightGray);
            new_leaf.EventMouseClick += (sender, args) =>
            {
                this.AddItem(GetTreeLeaf());
            };
            MenuItem new_branch = new MenuItem("New Branch");
            new_branch.SetForeground(Color.LightGray);
            new_branch.EventMouseClick += (sender, args) =>
            {
                this.AddItem(GetTreeBranch());
            };
            _menu.ReturnFocus = _parent.GetArea();
            EventMouseClick += (sender, args) =>
            {
                _menu.Show(sender, args);
                _parent.GetArea().EventMouseClick?.Invoke(_parent, args);
            };

            switch (_item_type)
            {
                case TreeItemType.Leaf:
                    base.AddItem(_icon_shape);
                    base.AddItem(_text_object);
                    _menu.AddItems(rename, remove, copy);
                    break;

                case TreeItemType.Branch:
                    _indicator.EventToggle += (sender, args) => OnToggleHide(_indicator.IsToggled());
                    _indicator.SetPassEvents(false, InputEventType.MousePress | InputEventType.MouseRelease | InputEventType.MouseDoubleClick);
                    _indicator.IsFocusable = false;
                    EventMouseDoubleClick += (sender, args) =>
                    {
                        if (args.Button == MouseButton.ButtonLeft)
                            _indicator.EventToggle.Invoke(sender, args);
                    };

                    base.AddItem(_indicator);
                    base.AddItem(_icon_shape);
                    base.AddItem(_text_object);
                    _menu.AddItems(new_branch, new_leaf, rename, paste);
                    break;
                default:
                    base.AddItem(_text_object);
                    break;
            }
            _text_object.IsFocusable = false;
            ResetIndents();
        }
        int ccc = 0;
        internal void OnToggleHide(bool value) // refactor
        {
            foreach (var item in _list_inners)
            {
                if (value)
                {
                    if (_indicator.IsToggled())
                    {
                        item.SetVisible(true);
                        item.OnToggleHide(value);
                    }
                }
                else
                {
                    item.SetVisible(false);
                    item.OnToggleHide(value);
                }
            }
            //update layout
            _parent.UpdateElements();
        }
        private void AddTreeItem(TreeItem item)
        {
            _indicator.SetToggled(true);
            _list_inners.Add(item);
            item._branch = this;
            item._parent = _parent;
            item._nesting_level = _nesting_level + 1;
            _parent.RefreshTree(item);
        }
        public override void AddItem(IBaseItem item)
        {
            TreeItem tmp = item as TreeItem;
            if (tmp != null)
                AddTreeItem(tmp);
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

            //SetMinWidth(width);
        }

        //text init
        public void SetTextAlignment(ItemAlignment alignment)
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
            _text_object.SetWidth(_text_object.GetTextWidth());
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
    }
}