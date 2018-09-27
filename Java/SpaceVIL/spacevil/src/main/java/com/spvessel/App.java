package app;

import com.spvessel.Engine.*;
import com.spvessel.Items.*;
import com.spvessel.View.MainWindow;
import com.spvessel.Windows.WindowLayoutBox;

import java.util.*;
import java.io.*;
import java.nio.*;
import java.net.URL;

public class App {
    static List<VisualItem> list = new LinkedList<VisualItem>();

    public static void main(String[] args) {
        System.out.println("Hello World! My dear friend!");
        MainWindow mw = new MainWindow();
        mw.show();
//        WindowLayoutBox.getWindowInstance("MainWindow").show();
    }
}