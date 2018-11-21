package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.InterfaceBaseItem;
import com.spvessel.Core.InterfaceHLayout;
import com.spvessel.Flags.SizePolicy;

public class HorizontalStack extends Prototype implements InterfaceHLayout {
    static int count = 0;

    public HorizontalStack() {
        setItemName("HorizontalStack_" + count);
        count++;
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.HorizontalStack"));
        setStyle(DefaultsService.getDefaultStyle(HorizontalStack.class));
        isFocusable = false;
    }

    // overrides
    @Override
    public boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    @Override
    public void addItem(InterfaceBaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    @Override
    public void setX(int _x) {
        super.setX(_x);
        updateLayout();
    }

    public void updateLayout() {
        int total_space = getWidth() - getPadding().left - getPadding().right;
        int free_space = total_space;
        int fixed_count = 0;
        int expanded_count = 0;

        for (InterfaceBaseItem child : getItems()) {
            if (child.isVisible()) {
                if (child.getWidthPolicy() == SizePolicy.FIXED) {
                    fixed_count++;
                    free_space -= (child.getWidth() + child.getMargin().left + child.getMargin().right);//
                } else {
                    expanded_count++;
                }
            }
        }
        free_space -= getSpacing().horizontal * ((expanded_count + fixed_count) - 1);
        
        int width_for_expanded = 1;
        if (expanded_count > 0 && free_space > expanded_count)
        width_for_expanded = free_space / expanded_count;
        
        int offset = 0;
        int startX = getX() + getPadding().left;
        
        for (InterfaceBaseItem child : getItems()) {
            if (child.isVisible()) {
                child.setX(startX + offset + child.getMargin().left);//
                if (child.getWidthPolicy() == SizePolicy.EXPAND) {
                    if (width_for_expanded - child.getMargin().left - child.getMargin().right < child.getMaxWidth())
                    child.setWidth(width_for_expanded - child.getMargin().left - child.getMargin().right);
                    else {
                        expanded_count--;
                        free_space -= (child.getWidth() + child.getMargin().left + child.getMargin().right);//
                        width_for_expanded = 1;
                        if (expanded_count > 0 && free_space > expanded_count)
                        width_for_expanded = free_space / expanded_count;
                    }
                }
                offset += child.getWidth() + getSpacing().horizontal + child.getMargin().left + child.getMargin().right;//
            }
            // refactor
            child.setConfines();
        }
        
        // for (InterfaceBaseItem child : getItems()) {
        //     System.out.println(child.getItemName() + " " + child.getWidth() + " " + child.getHeight() + " "  + child.getVisible());
        // }
    }
}