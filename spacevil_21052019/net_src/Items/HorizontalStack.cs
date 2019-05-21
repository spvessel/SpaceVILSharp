using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public class HorizontalStack : Prototype, IHLayout
    {
        static int count = 0;

        private ItemAlignment _contentAlignment = ItemAlignment.Left;

        public void SetContentAlignment(ItemAlignment alignment)
        {
            if (alignment == ItemAlignment.Top || alignment == ItemAlignment.HCenter || alignment == ItemAlignment.Right)
                _contentAlignment = alignment;
        }

        public ItemAlignment GetContentAlignment()
        {
            return _contentAlignment;
        }

        /// <summary>
        /// Constructs a HorizontalStack
        /// </summary>
        public HorizontalStack()
        {
            SetItemName("HorizontalStack_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.HorizontalStack)));
            IsFocusable = false;
        }

        //overrides
        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }

        /// <summary>
        /// Add item to the HorizontalStack
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            base.AddItem(item);
            UpdateLayout();
        }

        public override void InsertItem(IBaseItem item, int index)
        {
            base.InsertItem(item, index);
            UpdateLayout();
        }

        public override bool RemoveItem(IBaseItem item)
        {
            bool result = base.RemoveItem(item);
            if (result)
                UpdateLayout();
            return result;
        }

        /// <summary>
        /// Set width of the HorizontalStack
        /// </summary>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }

        /// <summary>
        /// Set X position of the HorizontalStack
        /// </summary>
        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateLayout();
        }

        /// <summary>
        /// Update all children and HorizontalStack sizes and positions
        /// according to confines
        /// </summary>
        public void UpdateLayout()
        {
            int total_space = GetWidth() - GetPadding().Left - GetPadding().Right;
            int free_space = total_space;
            int fixed_count = 0;
            int expanded_count = 0;

            List<int> maxWidthExpands = new List<int>();

            foreach (var child in GetItems())
            {
                if (child.IsVisible())
                {
                    if (child.GetWidthPolicy() == SizePolicy.Fixed)
                    {
                        fixed_count++;
                        free_space -= (child.GetWidth() + child.GetMargin().Left + child.GetMargin().Right);//
                    }
                    else
                    {
                        if (child.GetMaxWidth() < total_space) {
                            maxWidthExpands.Add(child.GetMaxWidth() + child.GetMargin().Right + child.GetMargin().Left);
                        }
                        expanded_count++;
                    }
                }
            }
            free_space -= GetSpacing().Horizontal * ((expanded_count + fixed_count) - 1);

            int width_for_expanded = 1;
            // if (expanded_count > 0 && free_space > expanded_count)
            //     width_for_expanded = free_space / expanded_count;
            maxWidthExpands.Sort();

            while (true) {
                if (expanded_count == 0)
                    break;
    
                if (free_space > expanded_count)
                    width_for_expanded = free_space / expanded_count;
    
                if (width_for_expanded <= 1 || maxWidthExpands.Count == 0) {
//                    width_for_expanded = 1;
                    break;
                }
    
                if (width_for_expanded > maxWidthExpands[0]) {
                    while (maxWidthExpands.Count > 0 && width_for_expanded > maxWidthExpands[0]) {
                        free_space -= maxWidthExpands[0];
                        maxWidthExpands.RemoveAt(0);
                        expanded_count--;
                    }
                } else {
                    break;
                }
//                width_for_expanded = widthForExpand(free_space, expanded_count);
            }

            int offset = 0;
            int startX = GetX() + GetPadding().Left;
            bool isFirstExpand = false;
            int diff = (free_space - width_for_expanded * expanded_count);
            if (expanded_count != 0 && diff > 0) {
                isFirstExpand = true;
            }

            if (expanded_count > 0 || _contentAlignment.Equals(ItemAlignment.Left))
            {
                foreach (var child in GetItems())
                {
                    if (child.IsVisible())
                    {
                        child.SetX(startX + offset + child.GetMargin().Left);//

                        if (child.GetWidthPolicy() == SizePolicy.Expand)
                        {
                            if (width_for_expanded - child.GetMargin().Left - child.GetMargin().Right < child.GetMaxWidth())
                            {
                                child.SetWidth(width_for_expanded - child.GetMargin().Left - child.GetMargin().Right);
                            }
                            else
                            {
                                // expanded_count--;
                                // free_space -= (child.GetWidth() + child.GetMargin().Left + child.GetMargin().Right);//
                                // width_for_expanded = 1;
                                // if (expanded_count > 0 && free_space > expanded_count)
                                //     width_for_expanded = free_space / expanded_count;
                                child.SetWidth(child.GetMaxWidth());
                            }

                            if (isFirstExpand) {
                            if (child.GetWidth() + diff < child.GetMaxWidth()) {
                                child.SetWidth(child.GetWidth() + diff);
                                isFirstExpand = false;
                            }
                        }
                        }
                        offset += child.GetWidth() + GetSpacing().Horizontal + child.GetMargin().Left + child.GetMargin().Right;//
                    }
                    //refactor
                    child.SetConfines();
                }
            }
            else
            {
                //for content alignment right
                if (_contentAlignment.Equals(ItemAlignment.Right))
                {
                    foreach (var child in GetItems())
                    {
                        if (child.IsVisible())
                        {
                            child.SetX(startX + offset + child.GetMargin().Left + free_space);
                            offset += child.GetWidth() + GetSpacing().Horizontal + child.GetMargin().Left + child.GetMargin().Right;//

                            if (child.GetWidthPolicy() == SizePolicy.Expand)
                                child.SetWidth(child.GetMaxWidth());
                        }
                        child.SetConfines();
                    }
                }
                //for content alignment hcenter
                else if (_contentAlignment.Equals(ItemAlignment.HCenter))
                {
                    foreach (var child in GetItems())
                    {
                        if (child.IsVisible())
                        {
                            child.SetX(startX + offset + child.GetMargin().Left + free_space / 2);
                            offset += child.GetWidth() + GetSpacing().Horizontal + child.GetMargin().Left + child.GetMargin().Right;//

                            if (child.GetWidthPolicy() == SizePolicy.Expand)
                                child.SetWidth(child.GetMaxWidth());
                        }
                        child.SetConfines();
                    }
                }
            }
        }
    }
}
