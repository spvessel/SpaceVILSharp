package com.spvessel.spacevil.App;

import com.spvessel.spacevil.View.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.*;
import com.spvessel.spacevil.Flags.ItemStateType;

public class Program {
    public static void main(String[] args) {

        com.spvessel.spacevil.Common.CommonService.initSpaceVILComponents();

        BufferedImage cursor = null;
        try {
            cursor = ImageIO.read(new File("D:\\icon_small.png"));
        } catch (IOException e) {
            System.out.println("load icons fail");
        }

        DefaultsService.setDefaultCursor(new CursorImage(cursor, 10, 10));

        MainWindow mw = new MainWindow();
        SettingsTest st = new SettingsTest();
        ImageTest im = new ImageTest();
        FlowTest ft = new FlowTest();
        LayoutsTest lt = new LayoutsTest();
        ComplexTest ct = new ComplexTest();
        InputTest it = new InputTest();
        TextTest tt = new TextTest();

        // EventTest et = new EventTest();
        // et.show();

        // ScissorsTest sc = new ScissorsTest();
        // sc.show();

        mw.show();
    }
}
