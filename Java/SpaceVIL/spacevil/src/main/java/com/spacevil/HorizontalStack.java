package com.spacevil;

import com.spacevil.Core.InterfaceBaseItem;
import com.spacevil.Core.InterfaceHLayout;

public class HorizontalStack extends Prototype implements InterfaceHLayout {
    private static int count = 0;

    /**
     * Constructs a HorizontalStack
     */
    public HorizontalStack() {
        setItemName("HorizontalStack_" + count);
        count++;
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.HorizontalStack"));
        setStyle(com.spacevil.Common.DefaultsService.getDefaultStyle(HorizontalStack.class));
        isFocusable = false;
    }

    // overrides
    @Override
    public boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    /**
     * Add item to the HorizontalStack
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    /**
     * Set width of the HorizontalStack
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    /**
     * Set X position of the HorizontalStack
     */
    @Override
    public void setX(int _x) {
        super.setX(_x);
        updateLayout();
    }

    /**
     * Update all children and HorizontalStack sizes and positions
     * according to confines
     */
    public void updateLayout() {
        int total_space = getWidth() - getPadding().left - getPadding().right;
        int free_space = total_space;
        int fixed_count = 0;
        int expanded_count = 0;

        for (InterfaceBaseItem child : getItems()) {
            if (child.isVisible()) {
                if (child.getWidthPolicy() == com.spacevil.Flags.SizePolicy.FIXED) {
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
                if (child.getWidthPolicy() == com.spacevil.Flags.SizePolicy.EXPAND) {
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