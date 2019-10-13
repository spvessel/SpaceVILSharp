package OwnLibs.Owls.Views.Items;

import java.awt.Color;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

public class FileEntryTab extends Tab {

    private Ellipse _unsavedMarker;
    private Rectangle _toggleMarker;
    private boolean _isUnsaved = false;

    public boolean isUnsaved() {
        return _isUnsaved;
    }

    public void setUnsaved(boolean value) {
        if (value == _isUnsaved)
            return;
        _isUnsaved = value;
        _unsavedMarker.setVisible(_isUnsaved);
        if (_isUnsaved) {
            setTextMargin(new Indents(18, 0, 0, 0));
        } else {
            setTextMargin(new Indents(7, 0, 0, 0));
        }
    }

    public FileEntryTab(String text) {
        super(text);
        _unsavedMarker = new Ellipse(12);
        _unsavedMarker.setVisible(_isUnsaved);
        _toggleMarker = new Rectangle();
        _toggleMarker.setVisible(false);
    }

    @Override
    public void initElements() {
        super.initElements();
        //
        setBackground(55, 55, 55);
        setPadding(0, 0, 5, 0);
        setTextMargin(new Indents(7, 0, 0, 0));
        addItemState(ItemStateType.TOGGLED, new ItemState(new Color(65, 65, 65)));
        addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 30)));
        //
        _unsavedMarker.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        _unsavedMarker.setSize(6, 6);
        _unsavedMarker.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        _unsavedMarker.setMargin(new Indents(7, 0, 0, 0));
        // _unsavedMarker.setBackground(252, 165, 67);
        // _unsavedMarker.setBackground(0, 162, 232);
        // _unsavedMarker.setBackground(200, 200, 200);
        _toggleMarker.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        _toggleMarker.setHeight(2);
        _toggleMarker.setAlignment(ItemAlignment.HCENTER, ItemAlignment.BOTTOM);
        _toggleMarker.setBackground(24, 148, 188);
        _toggleMarker.setMargin(0, 0, -5, 0);

        addItems(_unsavedMarker, _toggleMarker);
    }

    @Override
    public void setToggled(boolean value) {
        super.setToggled(value);
        if (isToggled()) {
            _toggleMarker.setVisible(true);
        } else {
            _toggleMarker.setVisible(false);
        }
    }
}