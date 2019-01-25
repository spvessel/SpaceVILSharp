package com.spvessel.spacevil.App;

import com.spvessel.spacevil.View.FlowTest;
import com.spvessel.spacevil.WindowLayoutBox;
import com.spvessel.spacevil.View.*;

public class Program {
    public static void main(String[] args) {

        com.spvessel.spacevil.Common.CommonService.initSpaceVILComponents();

        MainWindow mw = new MainWindow();
        // mw.show();

        SettingsTest st = new SettingsTest();
        // st.show();
        
        ImageTest im = new ImageTest();
        // im.show();

        FlowTest ft = new FlowTest();
        // ft.show();

        LayoutsTest lt = new LayoutsTest();
        // lt.show();
        
        ComplexTest ct = new ComplexTest();
        // ct.show();
        
        InputTest it = new InputTest();
        // it.show();

        TextTest tt = new TextTest();
        // tt.show();

//        WindowLayoutBox.getWindowInstance("MainWindow").show();
        WindowLayoutBox.tryShow("InputTest");
    }
}
