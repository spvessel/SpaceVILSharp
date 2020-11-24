package sandbox.View;

import java.awt.Color;
import java.awt.Font;

import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Effects;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

class StopMenu extends VerticalStack {
    Color selected = new Color(254, 64, 28);
    Font font = DefaultsService.getDefaultFont(22);

    public StopMenu() {
        setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        setHeight(200);
        setBackground(0, 0, 0, 220);
        setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
        setPadding(0, 50, 0, 0);
        setSpacing(0, 50);
        Effects.addEffect(this, new Shadow(10, Color.black));
    }

    @Override
    public void initElements() {

        Label title = new Label("WELCOME TO ENIGMAGAME!");
        title.setHeight(30);
        title.setHeightPolicy(SizePolicy.Fixed);
        title.setTextAlignment(ItemAlignment.Top, ItemAlignment.HCenter);
        title.setFont(font);
        title.setForeground(selected);

        Label manual = new Label("PRESS (SPACE) TO PLAY OR (ESCAPE) TO EXIT");
        manual.setStyle(title.getCoreStyle());
        manual.setFont(font);
        manual.setTextAlignment(ItemAlignment.Top, ItemAlignment.HCenter);
        manual.setForeground(selected);

        addItems(title, manual);
    }
}