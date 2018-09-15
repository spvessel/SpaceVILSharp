using System;
using System.Drawing;

namespace SpaceVIL
{
    public class HorizontalStack : VisualItem, IHLayout
    {
        static int count = 0;
        public HorizontalStack()
        {
            SetItemName("HorizontalStack_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.HorizontalStack)));
        }

        //overrides
        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }
        public override void AddItem(BaseItem item)
        {
            base.AddItem(item);
            UpdateLayout();
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

        public void UpdateLayout()
        {
            int total_space = GetWidth() - GetPadding().Left - GetPadding().Right;
            int free_space = total_space;
            int fixed_count = 0;
            int expanded_count = 0;

            foreach (var child in GetItems())
            {
                if (child.IsVisible)
                {
                    if (child.GetWidthPolicy() == SizePolicy.Fixed)
                    {
                        fixed_count++;
                        free_space -= (child.GetWidth() + child.GetMargin().Left + child.GetMargin().Right);//
                    }
                    else
                    {
                        expanded_count++;
                    }
                }
            }
            free_space -= GetSpacing().Horizontal * ((expanded_count + fixed_count) - 1);

            int width_for_expanded = 1;
            if (expanded_count > 0 && free_space > expanded_count)
                width_for_expanded = free_space / expanded_count;

            int offset = 0;
            int startX = GetX() + GetPadding().Left;
            foreach (var child in GetItems())
            {
                if (child.IsVisible)
                {
                    child.SetX(startX + offset + child.GetMargin().Left);//
                    if (child.GetWidthPolicy() == SizePolicy.Expand)
                    {
                        if (width_for_expanded - child.GetMargin().Left - child.GetMargin().Right < child.GetMaxWidth())
                            child.SetWidth(width_for_expanded - child.GetMargin().Left - child.GetMargin().Right);
                        else
                        {
                            expanded_count--;
                            free_space -= (child.GetWidth() + child.GetMargin().Left + child.GetMargin().Right);//
                            width_for_expanded = 1;
                            if (expanded_count > 0 && free_space > expanded_count)
                                width_for_expanded = free_space / expanded_count;
                        }
                    }

                    // if (child.GetX() + child.GetWidth() + child.GetMargin().Left + child.GetMargin().Right >= startX //
                    //     && child.GetX() <= GetX() + GetWidth() - GetPadding().Right)
                    //     child.IsVisible = true;
                    // else
                    //     child.IsVisible = false;

                    offset += child.GetWidth() + GetSpacing().Horizontal + child.GetMargin().Left + child.GetMargin().Right;//
                }
                //refactor
                child.SetConfines();
            }
        }
    }
}
