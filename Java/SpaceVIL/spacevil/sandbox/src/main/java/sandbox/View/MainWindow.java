package sandbox.View;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.ContextMenu;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.Grid;
import com.spvessel.spacevil.ImageItem;
import com.spvessel.spacevil.ItemsRefreshManager;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.MessageBox;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.WindowManager;
import com.spvessel.spacevil.WindowsBox;
import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Effects;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IMouseMethodState;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Core.Size;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.HorizontalDirection;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.MSAA;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.RenderType;
import com.spvessel.spacevil.Flags.SizePolicy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

public class MainWindow extends ActiveWindow {

    @Override
    public void initWindow() {
        isBorderHidden = true;
        setSize(820, 220);
        setWindowName("MainWindow");
        setWindowTitle("MainWindow");
        setMinSize(500, 100);

        // setAspectRatio(4, 1);

        setPadding(10, 10, 10, 10);
        setBackground(new Color(0, 0, 0, 0));
        setBorderRadius(10);
        setAntiAliasingQuality(MSAA.MSAA8x);
        isTransparent = true;
        // setShadow(10, 0, 0, Color.black);

        // Handler.setAntiAliasingQuality(MSAA.MSAA_8X);
        // Handler.eventClose.clear();
        // Handler.eventClose.add(() -> {
        // MessageItem msg = new MessageItem("Close?", "Are You sure?");
        // msg.onCloseDialog.add(() -> {
        // if (msg.getResult())
        // Handler.close();
        // });
        // msg.show(Handler);
        // });
        // Handler.setBackground(45, 45, 45);
        // Handler.setPadding(2, 2, 2, 2);

        BufferedImage iBig = null;
        BufferedImage iSmall = null;
        BufferedImage iS = null;
        try {
            iBig = ImageIO.read(new File("D:\\icon.png"));
            iSmall = ImageIO.read(new File("D:\\icon.png"));
            iS = ImageIO.read(new File("D:\\spimages.png"));
        } catch (IOException e) {
            System.out.println("load icons fail");
        }
        if (iBig != null && iSmall != null)
            setIcon(iBig, iSmall);

        Frame layout = new Frame();
        layout.setBackground(70, 70, 70);
        layout.setBorderRadius(10);
        layout.setPadding(0, 5, 0, 0);
        Effects.addEffect(layout, new Shadow(5, new Size(10, 10), new Color(255, 80, 80, 255)));
        addItem(layout);

        // TitleBar title = new TitleBar("Main King Window - JAVA");
        TitleBar title = new TitleBar("Owls. Your own libs");
        title.setAlignment(ItemAlignment.Bottom, ItemAlignment.Left);
        title.setBorderRadius(new CornerRadius(0, 0, 7, 7));
        title.direction = HorizontalDirection.FromRightToLeft;
        title.setIcon(iBig, 20, 20);
        layout.addItem(title);

        // Ellipse ellipse = new Ellipse();
        // ellipse.setBackground(Color.WHITE);
        // ellipse.setSize(30, 30);
        // // ellipse.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        // ellipse.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);

        // addItem(ellipse);

        Grid grid = new Grid(1, 7);
        grid.setItemName("layout_grid");
        grid.setBorderRadius(new CornerRadius(7, 7, 0, 0));
        grid.setMargin(0, 0, 0, 30);
        grid.setPadding(6, 6, 6, 6);
        grid.setSpacing(6, 6);
        layout.addItem(grid);

        Font font = DefaultsService.getDefaultFont(Font.PLAIN, 18);
        // Font font = new Font("Arial", Font.BOLD, 16);
        // Font font = DefaultsService.getDefaultFont();

        ButtonCore btn_layout = new ButtonCore("Layout");
        // btn_layout.setTextAlignment(ItemAlignment.BOTTOM, ItemAlignment.HCENTER);
        btn_layout.setItemName("LayoutButton");
        Effects.addEffect(btn_layout, new Shadow(5, new Size(4, 4), new Color(255, 0, 255, 255)));
        btn_layout.setFont(font);
        btn_layout.setToolTip("Show Layout window.\nIn there you can test ListBox.");
        btn_layout.setBackground(211, 120, 134);
        btn_layout.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        btn_layout.setBorderRadius(new CornerRadius(6));
        ItemState state = new ItemState(new Color(255, 255, 255, 80));
        state.border.setThickness(2);
        state.border.setColor(new Color(255, 255, 255, 150));
        state.border.setRadius(new CornerRadius(30, 12, 6, 6));
        btn_layout.addItemState(ItemStateType.Hovered, state);
        IMouseMethodState layout_click = (sender, args) -> WindowsBox.tryShow("LayoutsTest");
        btn_layout.eventMouseClick.add(layout_click);

        ButtonCore btn_settings = new ButtonCore("Settings");
        btn_settings.setFont(font);
        btn_settings.setToolTip("Show Settings window.");
        btn_settings.setBackground(255, 181, 111);
        Effects.addEffect(btn_settings, new Shadow(5, new Size(4, 4), new Color(255, 111, 111, 180)));
        btn_settings.setBorderRadius(6);
        btn_settings.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        IMouseMethodState settings_click = (sender, args) -> WindowsBox.tryShow("SettingsTest");
        btn_settings.eventMouseClick.add(settings_click);

        ButtonCore btn_label = new ButtonCore("Label");
        btn_label.setFont(font);
        btn_label.setItemName("Show Label window.");
        btn_label.setToolTip("Show Label window.");
        btn_label.setBackground(111, 181, 255);
        btn_label.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        Effects.addEffect(btn_label, new Shadow(5, new Size(4, 4), new Color(0, 181, 255, 180)));
        btn_label.setBorderRadius(6);
        // IMouseMethodState btn_action_click = (sender, args) -> {
        // setShadeColor(new Color(100, 100, 100, 125));
        // MessageBox msg = new MessageBox("Send result?", "Message:");
        // ButtonCore btnDontSave = new ButtonCore("Do not save");
        // btnDontSave.eventMouseClick.add((s, a) -> {
        // System.out.println("btnDontSave is chosen");
        // });
        // msg.addUserButton(btnDontSave, 2); // id must be > 1
        // msg.onCloseDialog.add(() -> {
        // System.out.println(msg.getResult() + " " + msg.getUserButtonResult());
        // });
        // msg.show();

        // // MessageItem ms = new MessageItem("Send result?", "Message:");
        // // ms.show(this);
        // System.out.println(btn_label.isFocusable + " " +
        // getFocusedItem().getItemName());
        // };
        // btn_label.eventMouseClick.add(btn_action_click);
        btn_label.isFocusable = false;

        ButtonCore btn_flow = new ButtonCore("Flow");
        btn_flow.setItemName("Flow");
        btn_flow.setFont(font);
        btn_flow.setToolTip("Show Flow window.");
        btn_flow.setBackground(193, 142, 221);
        btn_flow.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        Effects.addEffect(btn_flow, new Shadow(5, new Size(4, 4), new Color(193, 142, 221, 180)));
        btn_flow.setBorderRadius(6);
        // btn_flow.setCursor(EmbeddedCursor.HAND);
        btn_flow.setCursor(iS, 64, 64);
        IMouseMethodState flow_click = (sender, args) -> WindowsBox.tryShow("FlowTest");
        btn_flow.eventMouseClick.add(flow_click);

        ButtonCore btn_complex = new ButtonCore("Complex");
        btn_complex.setFont(font);
        btn_complex.setToolTip("Show Complex window.");
        btn_complex.setBackground(114, 153, 211);
        btn_complex.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        Effects.addEffect(btn_complex, new Shadow(5, new Size(4, 4), new Color(114, 153, 211, 180)));
        btn_complex.setBorderRadius(6);
        IMouseMethodState complex_click = (sender, args) -> WindowsBox.tryShow("ComplexTest");
        btn_complex.eventMouseClick.add(complex_click);

        ButtonCore btn_image = new ButtonCore("Image");
        btn_image.setFont(font);
        btn_image.setToolTip("Show Image window.");
        btn_image.setBackground(238, 174, 128);
        btn_image.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        Effects.addEffect(btn_image, new Shadow(5, new Size(4, 4), new Color(238, 174, 128, 180)));
        btn_image.setBorderRadius(6);
        IMouseMethodState img_click = (sender, args) -> WindowsBox.tryShow("ImageTest");
        btn_image.eventMouseClick.add(img_click);

        ButtonCore btn_input = new ButtonCore("Input");
        btn_input.setFont(font);
        btn_input.setToolTip("Show Input window.");
        btn_input.setBackground(121, 223, 152);
        btn_input.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        Effects.addEffect(btn_input, new Shadow(5, new Size(4, 4), new Color(121, 223, 152, 180)));
        btn_input.setBorderRadius(6);
        btn_input.eventMouseClick.add((sender, args) -> {
            // System.out.println(WindowLayoutBox.getListOfWindows().length);
            WindowsBox.tryShow("InputTest");
        });

        grid.addItems(btn_layout, btn_settings, btn_label, btn_flow, btn_input, btn_image, btn_complex);

        MenuItem mi1 = new MenuItem("MenuItem 1");
        mi1.eventMouseClick.add((sender, args) -> {
            System.out.println("mi1 click");
        });
        MenuItem mi2 = new MenuItem("MenuItem 2");
        mi2.eventMouseClick.add((sender, args) -> {
            System.out.println("mi2 click");
        });
        MenuItem mi3 = new MenuItem("MenuItem 3");
        mi3.eventMouseClick.add((sender, args) -> {
            System.out.println("mi3 click");
        });
        MenuItem mi4 = new MenuItem("MenuItem 4");
        ContextMenu menu = new ContextMenu(this, mi1, mi2, mi3, mi4);
        mi4.eventMouseClick.add((sender, args) -> {
            System.out.println("mi4 click");
            menu.addItem(new MenuItem("New menuItem"));
        });
        // menu.returnFocus = btn_flow;
        // menu.setBorderRadius(15);

        // eventMouseClick.add((sender, args) -> menu.show(sender, args));
        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.Menu) {
                // MouseArgs margs = new MouseArgs();
                // margs.button = MouseButton.BUTTON_RIGHT;
                // menu.show(sender, margs);

                // System.out.println(isFocused());
                // focus();

                // setHidden(true);
            }

            // // if (args.key == KeyCode.V)
            // // CommonService.setClipboardString("SetClipBoardString");
            // if (args.key == KeyCode.C)
            // System.out.println(CommonService.getClipboardString());
            // if (args.key == KeyCode.F)
            // System.out.println(WindowsBox.getCurrentFocusedWindow().getWindowName());
            // // if (args.key == KeyCode.R)
            // // ItemsRefreshManager.printSizeOfShapes();

        });

        MessageBox msg = new MessageBox("Choose one of this:", "Message:");
        eventKeyRelease.add((sender, args) -> {
            // System.out.println("root is focused");
            // if (args.key == KeyCode.ALPHA1) {
            // WindowManager.setRenderType(RenderType.IF_NEEDED);
            // }
            // if (args.key == KeyCode.ALPHA2) {
            // WindowManager.setRenderType(RenderType.PERIODIC);
            // }
            // if (args.key == KeyCode.ALPHA3) {
            // WindowManager.setRenderType(RenderType.ALWAYS);
            // }
            // if (args.key == KeyCode.P)
            // System.out.println(getWorkArea().toString());
            if (args.key == KeyCode.Menu) {
                ButtonCore btnDontSave = new ButtonCore("Do not save");
                msg.addUserButton(btnDontSave, 2); // id must be > 1
                // ButtonCore btnDontSave2 = new ButtonCore("Do not save");
                // msg.addUserButton(btnDontSave2, 3); // id must be > 1
                msg.show();
            }
        });
        // WindowManager.enableVSync(0);
        // WindowManager.setRenderType(RenderType.ALWAYS);
        btn_flow.setFocus();

        // eventOnStart.add(() -> {
        // System.out.println(getWorkArea().toString());
        // });
        //
        //
        // List<List<Side>> testListSide = new LinkedList<>();
        // LinkedList test1 = new LinkedList();
        // test1.add(Side.TOP);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.BOTTOM);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.RIGHT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.BOTTOM);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.RIGHT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.BOTTOM);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.BOTTOM);
        // test1.add(Side.RIGHT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.RIGHT);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.BOTTOM);
        // test1.add(Side.RIGHT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.BOTTOM);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.LEFT);
        // test1.add(Side.RIGHT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.BOTTOM);
        // test1.add(Side.RIGHT);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.BOTTOM);
        // test1.add(Side.RIGHT);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // boolean oldExp = false, newExp = false;
        //
        // for (List<Side> currentTest : testListSide) {
        // oldExp = ((currentTest.contains(Side.RIGHT) &&
        // currentTest.contains(Side.TOP))
        // || (currentTest.contains(Side.RIGHT) && currentTest.contains(Side.BOTTOM))
        // || (currentTest.contains(Side.LEFT) && currentTest.contains(Side.TOP))
        // || (currentTest.contains(Side.LEFT) && currentTest.contains(Side.BOTTOM))
        // || currentTest.contains(Side.LEFT)
        // || currentTest.contains(Side.RIGHT));
        //
        // newExp = (currentTest.contains(Side.LEFT) ||
        // currentTest.contains(Side.RIGHT));
        //
        // System.out.print("test: " + currentTest);
        // if (oldExp == newExp)
        // System.out.println(" OK");
        // else
        // System.out.println(" FAIL");
        // }
        //
        //

        // eventClose.clear();
        // eventClose.add(() -> {
        // MessageBox msg = new MessageBox("Are you sure?", "Message:");
        // msg.onCloseDialog.add(() -> {
        // if (msg.getResult())
        // // close();
        // WindowManager.appExit();
        // });
        // msg.show();
        // });

        // ToolTip.setStyle(this, Program.getNewToolTipStyle());
        // ToolTip.addItems(this, getDecor());
    }

    private IBaseItem getDecor() {
        // Ellipse ellipse = new Ellipse(12);
        // ellipse.setSize(8, 8);
        // ellipse.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        // ellipse.setMargin(10, 0, 0, 0);
        // return ellipse;

        ImageItem image = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size32x32), false);
        image.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        image.setSize(20, 20);
        image.setAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        image.setMargin(2, 0, 0, 0);
        image.keepAspectRatio(true);

        return image;
    }
}
