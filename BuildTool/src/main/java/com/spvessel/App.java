package com.spvessel;

import com.spvessel.spacevil.Common.*;
import com.spvessel.buildtool.*;

public class App {
    public static void main(String[] args) {
        Configs.readConfigs(Configs.ConfPath);
        System.out.println(CommonService.getSpaceVILInfo());
        if (CommonService.initSpaceVILComponents()) {
            MainWindow mw = new MainWindow();
            mw.show();
        }
        else
            System.out.println("Init SpaceVIL components fail.");
    }
}
