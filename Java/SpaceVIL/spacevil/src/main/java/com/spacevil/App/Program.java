package com.spacevil.App;

import com.spacevil.View.*;
import com.spacevil.View.FlowTest;
import com.spacevil.WindowLayoutBox;

public class Program {
    public static void main(String[] args) {
        com.spacevil.View.MainWindow mw = new com.spacevil.View.MainWindow();
        // mw.show();

        SettingsTest st = new SettingsTest();
        // st.show();
        
        ImageTest im = new ImageTest();
        // im.show();

        com.spacevil.View.FlowTest ft = new FlowTest();
        // ft.show();

        LayoutsTest lt = new LayoutsTest();
        // lt.show();
        
        ComplexTest ct = new ComplexTest();
        // ct.show();
        
        InputTest it = new InputTest();
        // it.show();

        TextTest tt = new TextTest();
        // tt.show();

        WindowLayoutBox.getWindowInstance("MainWindow").show();
    }
}
