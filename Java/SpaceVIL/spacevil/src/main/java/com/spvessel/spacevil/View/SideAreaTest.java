package com.spvessel.spacevil.View;

import java.awt.Color;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.Ellipse;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.SideArea;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.Triangle;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.MSAA;
import com.spvessel.spacevil.Flags.Side;

public class SideAreaTest extends ActiveWindow {

    @Override
    public void initWindow() {
        setParameters("SideAreaTest", "SideAreaTest", 1000, 800, false);
        setAntiAliasingQuality(MSAA.MSAA_8X);
        isCentered = true;

        // title
        TitleBar title = new TitleBar("SideArea");

        // layout
        Frame layout = new Frame();
        layout.setMargin(0, title.getHeight(), 0, 0);
        layout.setBackground(60, 60, 60);

        SideArea sLeft = new SideArea(this, Side.LEFT);
        SideArea sRight = new SideArea(this, Side.RIGHT);
        SideArea sTop = new SideArea(this, Side.TOP);
        SideArea sBottom = new SideArea(this, Side.BOTTOM);

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
        e.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        e.setBackground(52, 190, 124);
        e.setSize(200, 200);
        e.setShadow(8, 0, 0, Color.BLACK);
        e.setShadowExtension(10, 0);

        // adding
        addItems(title, layout);
        layout.addItems(e, bl, br, bt, bb, tl, tr, tt, tb, pl, pr, pt, pb);
    }

    private ButtonCore getButton(SideArea area, Ellipse p) {
        ButtonCore btn = new ButtonCore();

        btn.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        btn.setShadow(8, 0, 0, Color.BLACK);
        btn.setShadowExtension(4, 4);
        btn.getState(ItemStateType.HOVERED).background = new Color(255, 255, 255, 100);
        int offset = 100;
        int s1 = 100;
        int s2 = 70;
        switch (area.getAttachSide()) {
            case LEFT:
                btn.setSize(s1, s2);
                btn.setCustomFigure(new Figure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, -90)));
                btn.setMargin(0, 0, offset, 0);
                break;

            case RIGHT:
                btn.setSize(s1, s2);
                btn.setCustomFigure(new Figure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 90)));
                btn.setMargin(offset, 0, 0, 0);
                break;

            case TOP:
                btn.setBackground(90, 210, 255);
                btn.setSize(s2, s1);
                btn.setCustomFigure(new Figure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 0)));
                btn.setMargin(0, 0, 0, offset);
                break;

            case BOTTOM:
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
        t.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        t.setShadow(8, 0, 0, Color.BLACK);
        t.setShadowExtension(4, 4);
        int offset = 25;
        int s1 = 70;
        int s2 = 40;
        switch (side) {
            case LEFT:
                t.rotationAngle = 90;
                t.setSize(s2, s1);
                t.setMargin(0, 0, offset, 0);
                break;

            case RIGHT:
                t.rotationAngle = -90;
                t.setSize(s2, s1);
                t.setMargin(offset, 0, 0, 0);
                break;

            case TOP:
                t.setBackground(1, 95, 164);
                t.rotationAngle = 180;
                t.setSize(s1, s2);
                t.setMargin(0, 0, 0, offset);
                break;

            case BOTTOM:
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
        t.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        int offset = 160;
        switch (side) {
            case LEFT:
                t.setMargin(0, 0, offset, 0);
                break;

            case RIGHT:
                t.setMargin(offset, 0, 0, 0);
                break;

            case TOP:
                t.setMargin(0, 0, 0, offset);
                break;

            case BOTTOM:
                t.setMargin(0, offset, 0, 0);
                break;

            default:
                break;
        }
        return t;
    }
}