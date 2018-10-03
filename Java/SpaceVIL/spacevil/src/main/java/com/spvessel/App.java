package com.spvessel;

import com.spvessel.View.*;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World! My dear friend! I'm here!");
        MainWindow mw = new MainWindow();
        FlowTest ft = new FlowTest();
        mw.show();
//        WindowLayoutBox.getWindowInstance("MainWindow").show();
    }
}
