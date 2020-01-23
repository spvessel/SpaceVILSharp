package com.spvessel.spacevil.App;

import com.spvessel.spacevil.View.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Decorations.*;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.RenderType;

public class Program {
    public static void main(String[] args) {

        com.spvessel.spacevil.Common.CommonService.initSpaceVILComponents();

        // WindowManager.setRenderType(RenderType.ALWAYS);
        // WindowManager.enableVSync(0);

        // BufferedImage cursor = null;
        // try {
        // cursor = ImageIO.read(new File("D:\\icon_small.png"));
        // } catch (IOException e) {
        // System.out.println("load icons fail");
        // }

        // DefaultsService.getDefaultTheme().replaceDefaultItemStyle(ToolTipItem.class, getNewToolTipStyle());// DefaultsService.setDefaultCursor(new CursorImage(cursor, 10, 10));
        // MainWindow mw2 = new MainWindow();
        // DefaultsService.setDefaultFont(new Font("Arial", Font.BOLD, 12));

//        MainWindow mw = new MainWindow();
//        mw.setPosition(500, 500);

//        SettingsTest st = new SettingsTest();
//        ImageTest im = new ImageTest();
//        FlowTest ft = new FlowTest();
//        LayoutsTest lt = new LayoutsTest();
//        ComplexTest ct = new ComplexTest();
        InputTest it = new InputTest();
//       TextTest tt = new TextTest();
        // Containers con = new Containers();
//        SideAreaTest sat = new SideAreaTest();
        // DPIAnalysis dpi = new DPIAnalysis();

        // PerformanceTest pt = new PerformanceTest();

        // EventTest et = new EventTest();
        // et.show();

        // ScissorsTest sc = new ScissorsTest();
        // sc.show();

        // mw.show();

        // lt.show();
        // con.show();

        // WindowManager.enableVSync(0);
        // WindowManager.setRenderType(RenderType.ALWAYS);

        WindowManager.startWith(
//                mw,
                //, mw2
                // st
                // im
//                ft
                // lt
                // ct
                 it
                // tt
                // con
                // sat
                // et
                // pt
                // oglt
                );
    }

    public static Style getNewToolTipStyle() {
        Style style = Style.getToolTipStyle();
        style.borderFill = new Color(210, 210, 210);
        style.borderRadius = new CornerRadius(4, 0, 0, 4);
        style.borderThickness = 1;
        style.background = new Color(40, 40, 40);
        style.foreground = new Color(210, 210, 210);
        style.setShadow(new Shadow(10, 5, 5, new Color(255, 255, 0)));
        style.isShadowDrop = true;
        Style textStyle = style.getInnerStyle("text");
        if (textStyle != null) {
            textStyle.setMargin(30, 0, 0, 0);
            textStyle.setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        }
        return style;
    }
}
