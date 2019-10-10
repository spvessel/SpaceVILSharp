package OwnLibs.Owls.Views.Items;

import com.spvessel.spacevil.CheckBox;
import com.spvessel.spacevil.VerticalStack;

public class SettingsCommon extends VerticalStack {
    CheckBox _maximizeOnStart;

    public SettingsCommon() {
        setBackground(64, 64, 64);
        setMargin(0, 32, 0, 0);
        setPadding(20, 20, 20, 20);
        setVisible(false);
    }

    @Override
    public void initElements() {
        _maximizeOnStart = new CheckBox("Maximize app on start");
        addItem(_maximizeOnStart);
    }
}