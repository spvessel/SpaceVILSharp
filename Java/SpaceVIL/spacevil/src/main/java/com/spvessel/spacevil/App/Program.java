package com.spvessel.spacevil.App;

import com.spvessel.spacevil.View.*;
import com.spvessel.spacevil.*;

public class Program {
    public static void main(String[] args) {

        com.spvessel.spacevil.Common.CommonService.initSpaceVILComponents();
        System.out.println(com.spvessel.spacevil.Common.CommonService.getSpaceVILInfo());
//        MainWindow mw = new MainWindow();
//        SettingsTest st = new SettingsTest();
//        ImageTest im = new ImageTest();
//        FlowTest ft = new FlowTest();
//        LayoutsTest lt = new LayoutsTest();
//        ComplexTest ct = new ComplexTest();
        InputTest it = new InputTest();
//        TextTest tt = new TextTest();

        WindowLayoutBox.tryShow("InputTest");
    }
}
