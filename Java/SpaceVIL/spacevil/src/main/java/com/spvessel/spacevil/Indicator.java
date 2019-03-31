package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Style;

public class Indicator extends Prototype {
    class CustomToggle extends ButtonToggle {
        @Override
        protected boolean getHoverVerification(float xpos, float ypos) {
            return false;
        }
    }

    private static int count = 0;

    private CustomToggle _marker;

    /**
     * @return IndicationMarker ButtonToggle type for styling
     */
    public ButtonToggle getIndicatorMarker() {
        return _marker;
    }

    /**
     * Constructs an Indicator
     */
    public Indicator() {
        setItemName("Indicator_" + count);
        count++;

        _marker = new CustomToggle();

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.Indicator"));
        setStyle(DefaultsService.getDefaultStyle(Indicator.class));
    }

    /**
     * Initialization and adding of all elements in the Indicator
     */
    @Override
    public void initElements() {
        // marker
        _marker.setItemName(getItemName() + "_marker");
        addItem(_marker);
    }

    /**
     * Set style of the Indicator
     */
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