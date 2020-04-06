package OwnLibs.Owls.Views.Items;

import java.awt.Color;

import com.spvessel.spacevil.ButtonToggle;

public class ToggledItem extends ButtonToggle {

    @Override
    public void setToggled(boolean value) {
        super.setToggled(value);
        if (value) {
            setShadowDrop(true);
        } else {
            setShadowDrop(false);
        }
    }
}
