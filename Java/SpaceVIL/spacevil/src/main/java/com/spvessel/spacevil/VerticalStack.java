package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceVLayout;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.SizePolicy;

public class VerticalStack extends Prototype implements InterfaceVLayout {
    private static int count = 0;

    /**
     * Constructs a VerticalStack
     */
    public VerticalStack() {
        setItemName("verticalStack_" + count);
        count++;
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.VerticalStack"));
        setStyle(DefaultsService.getDefaultStyle(VerticalStack.class));
        isFocusable = false;
    }

    // overrides
    @Override
    public boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    /**
     * Add item to the VerticalStack
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    /**
     * Set height of the VerticalStack
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateLayout();
    }

    /**
     * Set Y position of the VerticalStack
     */
    @Override
    public void setY(int _y) {
        super.setY(_y);
        updateLayout();
    }

    /**
     * Update all children and VerticalStack sizes and positions
     * according to confines
     */
    public void updateLayout() {
        int total_space = getHeight() - getPadding().top - getPadding().bottom;
        int free_space = total_space;
        int fixed_count = 0;
        int expanded_count = 0;

        for (InterfaceBaseItem child : getItems()) {
            if (child.isVisible()) {
                if (child.getHeightPolicy() == SizePolicy.FIXED) {
                    fixed_count++;
                    free_space -= (child.getHeight() + child.getMargin().top + child.getMargin().bottom);//
                } else {
                    expanded_count++;
                }
            }
        }
        free_space -= getSpacing().vertical * ((expanded_count + fixed_count) - 1);

        int height_for_expanded = 1;
        if (expanded_count > 0 && free_space > expanded_count)
            height_for_expanded = free_space / expanded_count;

        int offset = 0;
        int startY = getY() + getPadding().top;

        for (InterfaceBaseItem child : getItems()) {
            if (child.isVisible()) {
                child.setY(startY + offset + child.getMargin().top);//
                if (child.getHeightPolicy() == SizePolicy.EXPAND) {
                    if (height_for_expanded - child.getMargin().top - child.getMargin().bottom < child.getMaxHeight())//
                        child.setHeight(height_for_expanded - child.getMargin().top - child.getMargin().bottom);//
                    else {
                        expanded_count--;
                        free_space -= (child.getHeight() + child.getMargin().top + child.getMargin().bottom);
                        height_for_expanded = 1;
                        if (expanded_count > 0 && free_space > expanded_count)
                            height_for_expanded = free_space / expanded_count;
                    }
                }

                // if (child.getY() + child.getHeight() + child.getMargin().top +
                // child.getMargin().bottom >= startY //
                // && child.getY() <= getY() + getHeight() - getPadding().bottom)
                // child.IsVisible = true;
                // else
                // child.IsVisible = false;

                offset += child.getHeight() + getSpacing().vertical + child.getMargin().top + child.getMargin().bottom;//
            }

            // refactor
            child.setConfines();
        }
    }
}