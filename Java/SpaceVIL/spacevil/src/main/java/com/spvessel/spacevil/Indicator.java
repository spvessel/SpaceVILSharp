package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Style;

/**
 * Indicator is the basic implementation of a user interface indicator which can
 * be in enabled state or can be disabled state. Used in
 * com.spvessel.spacevil.CheckBox and com.spvessel.spacevil.RadioButton.
 * <p>
 * Supports all events except drag and drop.
 */
public class Indicator extends Prototype {
    class CustomToggle extends ButtonToggle {
        @Override
        protected boolean getHoverVerification(float xpos, float ypos) {
            return false;
        }
    }

    private static int count = 0;

    private CustomToggle _marker;

    ButtonToggle getIndicatorMarker() {
        return _marker;
    }

    /**
     * Default Indicator constructor.
     */
    public Indicator() {
        setItemName("Indicator_" + count);
        count++;
        _marker = new CustomToggle();
        setStyle(DefaultsService.getDefaultStyle(Indicator.class));
    }

    /**
     * Initializing all elements in the Indicator.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        // marker
        _marker.setItemName(getItemName() + "_marker");
        addItem(_marker);
    }

    /**
     * Setting style of the ButtonCore.
     * <p>
     * Inner styles: "marker".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
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