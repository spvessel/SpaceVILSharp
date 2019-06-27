package OwnLibs.Owls.Views.Items;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

public class FileEntryTab extends Tab {

    private Ellipse _unsavedMarker;
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
    }

    @Override
    public void initElements() {
        super.initElements();
        //
        setPadding(0, 0, 5, 0);
        setTextMargin(new Indents(7, 0, 0, 0));
        //
        _unsavedMarker.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        _unsavedMarker.setSize(6, 6);
        _unsavedMarker.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        _unsavedMarker.setMargin(new Indents(7, 0, 0, 0));
        // _unsavedMarker.setBackground(252, 165, 67);
        // _unsavedMarker.setBackground(0, 162, 232);
        // _unsavedMarker.setBackground(200, 200, 200);

        addItem(_unsavedMarker);
    }
}