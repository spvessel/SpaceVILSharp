using System;
using System.Drawing;
using System.Linq;
using System.Collections.Generic;

namespace SpaceVIL
{
    public enum TreeItemType
    {
        Leaf,
        Branch
    }

    public class TreeItem : VisualItem
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
        private TextLine _text_object;
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
            _text_object = new TextLine();

            //SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TreeBranch)));
            IsPassEvents = false;
            SetBackground(Color.Transparent);
            SetForeground(Color.LightGray);
            SetFont(DefaultsService.GetDefaultFont());
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            SetHeight(25);
            SetAlignment(ItemAlignment.Top | ItemAlignment.Left);
            SetTextAlignment(ItemAlignment.VCenter | ItemAlignment.Left);
            SetSpacing(5);
            SetPadding(5);
            AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });
        }
        public TreeItem(TreeItemType type, String text = "") : this(type)
        {
            SetText(text);
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
            if (!_parent._root.IsVisible)
                level--;
            SetPadding(2 + _indent_size * level, 0, 0, 0);
        }
        public override void InitElements()
        {
            ResetIndents();

            _menu = new ContextMenu(GetHandler());
            _menu.SetBackground(40, 40, 40);
            _menu.IsPassEvents = false;
            MenuItem remove = new MenuItem("Remove");
            remove.SetForeground(Color.LightGray);
            remove.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            remove.EventMouseClick += (sender, args) =>
            {
                GetParent().RemoveItem(this);
            };
            MenuItem rename = new MenuItem("Rename");
            rename.SetForeground(Color.LightGray);
            rename.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            rename.EventMouseClick += (sender, args) =>
            {
                //rename
            };
            MenuItem copy = new MenuItem("Copy");
            copy.SetForeground(Color.LightGray);
            copy.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            copy.EventMouseClick += (sender, args) =>
            {
                //copy
            };
            MenuItem paste = new MenuItem("Paste");
            paste.SetForeground(Color.LightGray);
            paste.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            paste.EventMouseClick += (sender, args) =>
            {
                //paste
            };
            MenuItem new_leaf = new MenuItem("New Leaf");
            new_leaf.SetForeground(Color.LightGray);
            new_leaf.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            new_leaf.EventMouseClick += (sender, args) =>
            {
                this.AddItem(GetTreeLeaf());
            };
            MenuItem new_branch = new MenuItem("New Branch");
            new_branch.SetForeground(Color.LightGray);
            new_branch.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            new_branch.EventMouseClick += (sender, args) =>
            {
                this.AddItem(GetTreeBranch());
            };

            EventMouseClick += (_, x) => _menu.Show(_, x);

            switch (_item_type)
            {
                case TreeItemType.Leaf:
                    _icon_shape = new CustomShape();
                    _icon_shape.SetBackground(129, 187, 133);
                    _icon_shape.SetSize(6, 6);
                    _icon_shape.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
                    _icon_shape.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
                    _icon_shape.SetTriangles(GraphicsMathService.GetEllipse(3, 8));
                    _icon_shape.SetMargin(2, 0, 0, 0);
                    base.AddItem(_icon_shape);
                    base.AddItem(_text_object);

                    _menu.SetSize(100, 4 + 30 * 2 - 5);
                    _menu.AddItems(rename, remove, copy);
                    break;

                case TreeItemType.Branch:
                    _icon_shape = new CustomShape();
                    _icon_shape.SetBackground(106, 185, 255);
                    _icon_shape.SetSize(14, 9);
                    _icon_shape.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
                    _icon_shape.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
                    _icon_shape.SetTriangles(GraphicsMathService.GetFolderIconShape());

                    _indicator.SetSize(15, 15);
                    _indicator.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
                    _indicator.IsCustom = new CustomFigure(true, GraphicsMathService.GetTriangle(10, 8, 0, 3, 90));
                    _indicator.SetBackground(32, 32, 32);
                    _indicator.AddItemState(ItemStateType.Toggled, new ItemState()
                    {
                        Background = _indicator.GetBackground(),
                        Shape = new CustomFigure(true, GraphicsMathService.GetTriangle(10, 8, 0, 3, 180))
                    });
                    _indicator.Border.Radius = 0;
                    _indicator.EventToggle += (sender, args) => OnToggleHide(_indicator.IsToggled);

                    base.AddItem(_indicator);
                    base.AddItem(_icon_shape);
                    base.AddItem(_text_object);

                    _menu.SetSize(100, 4 + 30 * 3 - 5);
                    _menu.AddItems(new_branch, new_leaf, rename, paste);
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
                    if (_indicator.IsToggled)
                    {
                        item.IsVisible = true;
                        item.OnToggleHide(value);
                    }
                }
                else
                {
                    item.IsVisible = false;
                    item.OnToggleHide(value);
                }
            }
            //update layout
            _parent.UpdateElements();
        }
        private void AddTreeItem(TreeItem item)
        {
            _indicator.IsToggled = true;
            _list_inners.Add(item);
            item._branch = this;
            item._parent = _parent;
            item._nesting_level = _nesting_level + 1;
            _parent.RefreshTree(item);
        }
        public override void AddItem(BaseItem item)
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
            _text_object.SetItemText(text);
            UpdateLayout();
        }
        public String GetText()
        {
            return _text_object.GetItemText();
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
            //additional

        }
    }
}