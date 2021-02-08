package sandbox.View;

import java.awt.Color;
import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.BlankItem;
import com.spvessel.spacevil.Ellipse;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.Size;
import com.spvessel.spacevil.Flags.MSAA;
import com.spvessel.spacevil.Flags.RenderType;
import com.spvessel.spacevil.WindowManager;

public class Simple extends ActiveWindow {
    @Override
    public void initWindow() {
        WindowManager.setRenderType(RenderType.IfNeeded);
        setSize(400, 400);
        setAntiAliasingQuality(MSAA.MSAA8x);

        Ellipse ellipse = new Ellipse(64);
        ellipse.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        ellipse.setBackground(255, 155, 155);
        ellipse.setMargin(100, 100, 100, 100);
        ellipse.effects().add(new Shadow(10, new Size(140, 140), new Color(255, 0, 255, 255)));
        ellipse.effects().add(new Shadow(10, new Size(120, 120), new Color(0, 0, 255, 255)));
        ellipse.effects().add(new Shadow(10, new Size(100, 100), new Color(128, 255, 255, 255)));
        ellipse.effects().add(new Shadow(10, new Size(80, 80), new Color(0, 255, 0, 255)));
        ellipse.effects().add(new Shadow(10, new Size(60, 60), new Color(255, 255, 128, 255)));
        ellipse.effects().add(new Shadow(10, new Size(40, 40), new Color(255, 128, 0, 255)));
        ellipse.effects().add(new Shadow(10, new Size(20, 20), new Color(255, 0, 0, 255)));

        addItem(ellipse);
    }
}
