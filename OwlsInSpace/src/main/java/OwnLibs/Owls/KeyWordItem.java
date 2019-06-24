package OwnLibs.Owls;

import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Label;

import java.awt.*;

public class KeyWordItem extends Label {
    public KeyWordItem() {
        super();
        setWidthPolicy(SizePolicy.FIXED);
        setBackground(0, 0, 0, 0);
        setForeground(new Color(0x168DC1));
        setFontSize(15);
        setFontStyle(Font.BOLD);
        addItemState(ItemStateType.HOVERED, new ItemState(new Color(0x595959)));
        addItemState(ItemStateType.PRESSED, new ItemState(new Color(0x3F3F3F)));
        setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        eventMousePress.add(this::linkIsVisited);
    }

    public KeyWordItem(String text) {
        this();
        setText(text);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        setWidth(getTextWidth() + 10);
    }

    private void linkIsVisited(Object sender, MouseArgs args) {
        setBackground(0, 0, 0, 40);
    }
}
