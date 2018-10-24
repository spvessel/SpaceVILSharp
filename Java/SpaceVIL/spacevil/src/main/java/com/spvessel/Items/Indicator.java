package com.spvessel.Items;

import com.spvessel.Common.*;
import com.spvessel.Decorations.*;

public class Indicator extends VisualItem {
    class CustomToggle extends ButtonToggle {
        @Override
        public boolean getHoverVerification(float xpos, float ypos) {
            return false;
        }
    }

    private static int count = 0;

    private CustomToggle _marker;

    public ButtonToggle getIndicatorMarker() {
        return _marker;
    }

    public Indicator() {
        setItemName("Indicator_" + count);
        count++;

        _marker = new CustomToggle();

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.Indicator"));
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.Indicator.class));
    }

    @Override
    public void initElements() {
        // marker
        _marker.setItemName(getItemName() + "_marker");
        addItem(_marker);
    }

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        Style marker_style = style.getInnerStyle("marker");
        if (marker_style != null) {
            _marker.setStyle(marker_style);
        }
    }
}