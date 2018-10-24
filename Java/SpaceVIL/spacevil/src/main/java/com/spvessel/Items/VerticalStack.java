package com.spvessel.Items;

import com.spvessel.Common.*;
import com.spvessel.Cores.*;
import com.spvessel.Flags.SizePolicy;

public class VerticalStack extends VisualItem implements InterfaceVLayout {
    static int count = 0;

    public VerticalStack() {
        setItemName("verticalStack_" + count);
        count++;
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.VerticalStack"));
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.VerticalStack.class));
    }

    // overrides
    @Override
    public boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    @Override
    public void addItem(BaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateLayout();
    }

    @Override
    public void setY(int _y) {
        super.setY(_y);
        updateLayout();
    }

    public void updateLayout() {
        int total_space = getHeight() - getPadding().top - getPadding().bottom;
        int free_space = total_space;
        int fixed_count = 0;
        int expanded_count = 0;

        for (BaseItem child : getItems()) {
            if (child.getVisible()) {
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

        for (BaseItem child : getItems()) {
            if (child.getVisible()) {
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