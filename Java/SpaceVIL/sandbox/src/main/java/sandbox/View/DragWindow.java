package sandbox.View;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Flags.ItemAlignment;

public class DragWindow extends ActiveWindow {

    @Override
    public void initWindow() {
        setSize(820, 220);

        MyWidget widget = new MyWidget();
        widget.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        addItem(widget);
    }
}