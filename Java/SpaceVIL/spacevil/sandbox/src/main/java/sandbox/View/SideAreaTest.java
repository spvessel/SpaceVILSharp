package sandbox.View;

import java.awt.Color;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.Ellipse;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.SideArea;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.Triangle;
import com.spvessel.spacevil.Core.Size;
import com.spvessel.spacevil.Decorations.Effects;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.MSAA;
import com.spvessel.spacevil.Flags.Side;

public class SideAreaTest extends ActiveWindow {

    @Override
    public void initWindow() {
        setParameters("SideAreaTest", "SideAreaTest", 1000, 800, false);
        setAntiAliasingQuality(MSAA.MSAA8x);
        isCentered = true;

        // title
        TitleBar title = new TitleBar("SideArea");

        // layout
        Frame layout = new Frame();
        layout.setMargin(0, title.getHeight(), 0, 0);
        layout.setBackground(60, 60, 60);

        SideArea sLeft = new SideArea(this, Side.Left);
        SideArea sRight = new SideArea(this, Side.Right);
        SideArea sTop = new SideArea(this, Side.Top);
        SideArea sBottom = new SideArea(this, Side.Bottom);

        Ellipse pl = getPoint(sLeft.getAttachSide());
        Ellipse pr = getPoint(sRight.getAttachSide());
        Ellipse pt = getPoint(sTop.getAttachSide());
        Ellipse pb = getPoint(sBottom.getAttachSide());

        ButtonCore bl = getButton(sLeft, pl);
        ButtonCore br = getButton(sRight, pr);
        ButtonCore bt = getButton(sTop, pt);
        ButtonCore bb = getButton(sBottom, pb);

        Triangle tl = getTriangle(sLeft.getAttachSide());
        Triangle tr = getTriangle(sRight.getAttachSide());
        Triangle tt = getTriangle(sTop.getAttachSide());
        Triangle tb = getTriangle(sBottom.getAttachSide());

        Ellipse e = new Ellipse(4);
        e.setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
        e.setBackground(52, 190, 124);
        e.setSize(200, 200);
        Effects.addEffect(e, new Shadow(8, new Size(10, 0), Color.BLACK));

        // adding
        addItems(title, layout);
        layout.addItems(e, bl, br, bt, bb, tl, tr, tt, tb, pl, pr, pt, pb);
    }

    private ButtonCore getButton(SideArea area, Ellipse p) {
        ButtonCore btn = new ButtonCore();

        btn.setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
        Effects.addEffect(btn, new Shadow(8, new Size(4, 4), Color.BLACK));
        btn.getState(ItemStateType.Hovered).background = new Color(255, 255, 255, 100);
        int offset = 100;
        int s1 = 100;
        int s2 = 70;
        switch (area.getAttachSide()) {
            case Left:
                btn.setSize(s1, s2);
                btn.setCustomFigure(new Figure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, -90)));
                btn.setMargin(0, 0, offset, 0);
                break;

            case Right:
                btn.setSize(s1, s2);
                btn.setCustomFigure(new Figure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 90)));
                btn.setMargin(offset, 0, 0, 0);
                break;

            case Top:
                btn.setBackground(90, 210, 255);
                btn.setSize(s2, s1);
                btn.setCustomFigure(new Figure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 0)));
                btn.setMargin(0, 0, 0, offset);
                break;

            case Bottom:
                btn.setBackground(1, 110, 190);
                btn.setSize(s2, s1);
                btn.setCustomFigure(new Figure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 180)));
                btn.setMargin(0, offset, 0, 0);
                break;

            default:
                break;
        }
        btn.eventMouseClick.add((s, a) -> {
            area.show();
        });
        btn.eventMouseHover.add((s, a) -> {
            p.setVisible(true);
        });
        btn.eventMouseLeave.add((s, a) -> {
            p.setVisible(false);
        });
        return btn;
    }

    private Triangle getTriangle(Side side) {
        Triangle t = new Triangle();
        t.setBackground(1, 140, 232);
        t.setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
        Effects.addEffect(t, new Shadow(8, new Size(4, 4), Color.BLACK));
        int offset = 25;
        int s1 = 70;
        int s2 = 40;
        switch (side) {
            case Left:
                t.rotationAngle = 90;
                t.setSize(s2, s1);
                t.setMargin(0, 0, offset, 0);
                break;

            case Right:
                t.rotationAngle = -90;
                t.setSize(s2, s1);
                t.setMargin(offset, 0, 0, 0);
                break;

            case Top:
                t.setBackground(1, 95, 164);
                t.rotationAngle = 180;
                t.setSize(s1, s2);
                t.setMargin(0, 0, 0, offset);
                break;

            case Bottom:
                t.setBackground(85, 210, 255);
                t.rotationAngle = 0;
                t.setSize(s1, s2);
                t.setMargin(0, offset, 0, 0);
                break;

            default:
                break;
        }
        return t;
    }

    private Ellipse getPoint(Side side) {
        Ellipse t = new Ellipse();
        t.setVisible(false);
        t.setSize(8, 8);
        t.setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
        int offset = 160;
        switch (side) {
            case Left:
                t.setMargin(0, 0, offset, 0);
                break;

            case Right:
                t.setMargin(offset, 0, 0, 0);
                break;

            case Top:
                t.setMargin(0, 0, 0, offset);
                break;

            case Bottom:
                t.setMargin(0, offset, 0, 0);
                break;

            default:
                break;
        }
        return t;
    }
}