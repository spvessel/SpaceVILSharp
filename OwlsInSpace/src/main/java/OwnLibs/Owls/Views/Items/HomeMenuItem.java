package OwnLibs.Owls.Views.Items;

import java.awt.Color;

import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

import OwnLibs.Owls.ElementsFactory;

public class HomeMenuItem extends Prototype {
    private Label _description;

    public void setDescription(String description) {
        if (description != null) {
            _description.setText(description);
            setToolTip(description);
        }
    }

    public HomeMenuItem(String name) {
        setItemName(name);
        _description = new Label("", false);
        setBackground(57, 57, 57);
        addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 10)));
        setCursor(EmbeddedCursor.HAND);
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        setHeight(60);
        setPadding(10, 5, 5, 5);
        setShadow(6, 0, 0, new Color(0, 0, 0, 255));
    }

    @Override
    public void initElements() {
        VerticalStack layout = new VerticalStack();
        Label name = new Label(getItemName(), false);
        ElementsFactory.getCasualLabelStyle().setStyle(name, _description);
        name.setFontSize(14);
        name.setForeground(Color.WHITE);
        _description.setForeground(180, 180, 180);

        addItem(layout);
        layout.addItems(name, _description);
    }
}