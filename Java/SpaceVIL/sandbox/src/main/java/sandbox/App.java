package sandbox;

import sandbox.View.*;

import java.io.File;
import java.nio.file.Paths;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.spvessel.spacevil.internal.Wrapper.*;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Common.RenderService;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.*;
import com.spvessel.spacevil.Flags.BenchmarkIndicator;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.RenderType;

public class App {
    public static void main(String[] args) {
        // // NativeLibrary.extractEmbeddedLibrary();
        // // System.load(Paths.get("").toAbsolutePath().toString() + File.separator +
        // "wrapper.dll");
        // // CommonService.initSpaceVILComponents();

        // // NativeLibraryManager.ExtractEmbeddedLibrary(Library.Glfw);
        // // System.load("C:\\glfw\\glfw3.dll");
        // // System.load("C:\\glfw\\glfwapi.dll");

        WindowManager.setRenderType(RenderType.IfNeeded);
        // WindowManager.enableVSync(0);

        // BufferedImage cursor = null;
        // try {
        // cursor = ImageIO.read(new File("D:\\icon_small.png"));
        // } catch (IOException e) {
        // System.out.println("load icons fail");
        // }

        // DefaultsService.getDefaultTheme().replaceDefaultItemStyle(ToolTipItem.class,
        // getNewToolTipStyle());// DefaultsService.setDefaultCursor(new
        // CursorImage(cursor, 10, 10));
        // MainWindow mw2 = new MainWindow();
        // DefaultsService.setDefaultFont(new Font("Arial", Font.BOLD, 12));

        // MessageBox msg = new MessageBox("Another app is already running!",
        // "MESSAGE:");
        // msg.getCancelButton().setVisible(false);
        // msg.show();
        // msg.onCloseDialog.add(() -> {
        // WindowManager.appExit();
        // });

        // RenderService.enableMonitoring(BenchmarkIndicator.Framerate,
        // BenchmarkIndicator.Frametime);
        // DefaultsService.getDefaultTheme().replaceDefaultItemStyle(TextEdit.class,
        // getTextEditStyle());

        MainWindow mw = new MainWindow();
        mw.show();
        // // JsonTest jt = new JsonTest();
        // // // mw.show();
        // // mw.setPosition(500, 500);

        // // SettingsTest st = new SettingsTest();
        // ImageTest im = new ImageTest();
        // im.show();
        // FlowTest ft = new FlowTest();
        // ft.show();
        // LayoutsTest lt = new LayoutsTest();
        // ComplexTest ct = new ComplexTest();
        // InputTest it = new InputTest();
        // // it.show();
        // TextTest tt = new TextTest();
        // Containers con = new Containers();
        // SideAreaTest sat = new SideAreaTest();
        // DPIAnalysis dpi = new DPIAnalysis();
        // OpenGLTest oglt = new OpenGLTest();

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

        // DragWindow dw = new DragWindow();

        // Calendar tmpDate = Calendar.getInstance();
        // System.out.println(new SimpleDateFormat("MMMM").format(tmpDate.getTime()) + "
        // " + tmpDate.get(Calendar.YEAR));

        // ApiTester api = new ApiTester();
        // api.show();
        // new Simple().show();
        // WindowManager.startWith(
        // // jt
        // mw// ,
        // // dw,
        // // mw2
        // // st
        // // im
        // // ft
        // // lt
        // // ct
        // // it
        // // tt
        // // con
        // // sat
        // // et
        // // pt
        // // oglt
        // );
    }

    // public static final Color Hover = new Color(255, 255, 255, 100);
    // public static final Color KeyBindFocused = new Color(138, 220, 255);

    // private static Style getTextEditStyle() {
    // Style style = Style.getTextEditStyle();
    // Style text = style.getInnerStyle("text");
    // text.addItemState(ItemStateType.Hovered, new ItemState(Hover));
    // text.addItemState(ItemStateType.Focused, new ItemState(KeyBindFocused));
    // return style;
    // }

    // public static Style getNewToolTipStyle() {
    // Style style = Style.getToolTipStyle();
    // style.border.setColor(new Color(210, 210, 210));
    // style.border.setRadius(new CornerRadius(4, 0, 0, 4));
    // style.border.setThickness(1);
    // style.background = new Color(40, 40, 40);
    // style.foreground = new Color(210, 210, 210);
    // style.setShadow(new Shadow(10, new Position(5, 5), new Color(255, 255, 0)));
    // Style textStyle = style.getInnerStyle("text");
    // if (textStyle != null) {
    // textStyle.setMargin(30, 0, 0, 0);
    // textStyle.setTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
    // }
    // return style;
    // }
}
