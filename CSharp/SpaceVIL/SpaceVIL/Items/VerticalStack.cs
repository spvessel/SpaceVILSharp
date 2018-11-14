using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public class VerticalStack : Prototype, IVLayout
    {
        static int count = 0;
        public VerticalStack()
        {
            SetItemName("VerticalStack_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.VerticalStack)));
        }

        //overrides
        internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }
        
        public override void AddItem(IBaseItem item)
        {
            base.AddItem(item);
            UpdateLayout();
        }
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateLayout();
        }
        public override void SetY(int _y)
        {
            base.SetY(_y);
            UpdateLayout();
        }

        public void UpdateLayout()
        {
            int total_space = GetHeight() - GetPadding().Top - GetPadding().Bottom;
            int free_space = total_space;
            int fixed_count = 0;
            int expanded_count = 0;

            foreach (var child in GetItems())
            {
                if (child.IsVisible())
                {
                    if (child.GetHeightPolicy() == SizePolicy.Fixed)
                    {
                        fixed_count++;
                        free_space -= (child.GetHeight() + child.GetMargin().Top + child.GetMargin().Bottom);//
                    }
                    else
                    {
                        expanded_count++;
                    }
                }
            }
            free_space -= GetSpacing().Vertical * ((expanded_count + fixed_count) - 1);

            int height_for_expanded = 1;
            if (expanded_count > 0 && free_space > expanded_count)
                height_for_expanded = free_space / expanded_count;

            int offset = 0;
            int startY = GetY() + GetPadding().Top;
            foreach (var child in GetItems())
            {
                if (child.IsVisible())
                {
                    child.SetY(startY + offset + child.GetMargin().Top);//
                    if (child.GetHeightPolicy() == SizePolicy.Expand)
                    {
                        if (height_for_expanded - child.GetMargin().Top - child.GetMargin().Bottom < child.GetMaxHeight())//
                            child.SetHeight(height_for_expanded - child.GetMargin().Top - child.GetMargin().Bottom);//
                        else
                        {
                            expanded_count--;
                            free_space -= (child.GetHeight() + child.GetMargin().Top + child.GetMargin().Bottom);
                            height_for_expanded = 1;
                            if (expanded_count > 0 && free_space > expanded_count)
                                height_for_expanded = free_space / expanded_count;
                        }
                    }

                    // if (child.GetY() + child.GetHeight() + child.GetMargin().Top + child.GetMargin().Bottom >= startY //
                    //     && child.GetY() <= GetY() + GetHeight() - GetPadding().Bottom)
                    //     child.IsVisible = true;
                    // else
                    //     child.IsVisible = false;

                    offset += child.GetHeight() + GetSpacing().Vertical + child.GetMargin().Top + child.GetMargin().Bottom;//
                }

                //refactor
                child.SetConfines();
            }
        }
    }
}
