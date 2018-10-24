package com.spvessel;

import com.spvessel.View.*;
import com.spvessel.Windows.*;
import static org.lwjgl.glfw.GLFW.*;

public class App {
    public static void main(String[] args) {
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

        WindowLayoutBox.getWindowInstance("MainWindow").show();
    }
}
