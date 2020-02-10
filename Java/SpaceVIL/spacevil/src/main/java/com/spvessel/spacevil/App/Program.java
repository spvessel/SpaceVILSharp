package com.spvessel.spacevil.App;

import com.spvessel.spacevil.View.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.*;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.RenderType;

public class Program {
    public static void main(String[] args) {

        com.spvessel.spacevil.Common.CommonService.initSpaceVILComponents();

        WindowManager.setRenderType(RenderType.ALWAYS);
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

        // MessageBox msg = new MessageBox("Another app is already running!", "MESSAGE:");
        // msg.getCancelButton().setVisible(false);
        // msg.show();
        // msg.onCloseDialog.add(() -> {
        //     WindowManager.appExit();
        // });

        DefaultsService.getDefaultTheme().replaceDefaultItemStyle(TextEdit.class, getTextEditStyle());

        MainWindow mw = new MainWindow();
        mw.setPosition(500, 500);

        //        SettingsTest st = new SettingsTest();
        ImageTest im = new ImageTest();
               FlowTest ft = new FlowTest();
        LayoutsTest lt = new LayoutsTest();
        //    ComplexTest ct = new ComplexTest();
        InputTest it = new InputTest();
        //       TextTest tt = new TextTest();
        // Containers con = new Containers();
        //        SideAreaTest sat = new SideAreaTest();
        // DPIAnalysis dpi = new DPIAnalysis();
        OpenGLTest oglt = new OpenGLTest();

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

        Calendar tmpDate = Calendar.getInstance();
        System.out.println(new SimpleDateFormat("MMMM").format(tmpDate.getTime()) + " " + tmpDate.get(Calendar.YEAR));

        WindowManager.startWith(
            mw
        //, mw2
        // st
        // im
        //                ft
        // lt
        // ct
        //  it
        // tt
        // con
        // sat
        // et
        // pt
        // oglt
        );
    }

    public static final Color Hover = new Color(255, 255, 255, 100);
    public static final Color KeyBindFocused = new Color(138, 220, 255);

    private static Style getTextEditStyle() {
        Style style = Style.getTextEditStyle();
        Style text = style.getInnerStyle("text");
        text.addItemState(ItemStateType.HOVERED, new ItemState(Hover));
        text.addItemState(ItemStateType.FOCUSED, new ItemState(KeyBindFocused));
        return style;
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
