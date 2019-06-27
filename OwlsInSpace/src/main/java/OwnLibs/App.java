package OwnLibs;

import com.spvessel.spacevil.Common.CommonService;

import OwnLibs.Owls.Controller;
import OwnLibs.Owls.Views.Windows.*;

public class App {

    public static void main(String[] args) {
        if (CommonService.initSpaceVILComponents()) {
            // System.out.println(CommonService.getSpaceVILInfo());
            MainWindow ow = new MainWindow();
            Controller controller = new Controller(ow);
            controller.start();
        } else
            System.out.println("Init SpaceVIL components fail.");
    }
}

// 1. исправления:
// - исправить проблему кодировок (сделать единообразно UTF-8)

// 2. реализовать систему вкладок (возможно переписать стиль, добавить в
// контекстное меню пункт открыть)

// 3. окно настроек (пути к папкам, ftp, настройки с редактированием по
// умолчанию, настройки шрифта и что-нибудь еще)

// 4. шифрование выбранной папки, окно с вводом имени и пароля

// 5. добавить комбинации клавиш
