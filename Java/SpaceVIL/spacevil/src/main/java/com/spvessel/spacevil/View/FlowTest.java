package com.spvessel.spacevil.View;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.CustomFigure;
import com.spvessel.spacevil.Decorations.Effects;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.SubtractFigure;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.FileSystemEntryType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemRule;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.MSAA;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.Side;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.OpenEntryDialog.OpenDialogType;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceEffect;
import com.spvessel.spacevil.Core.InterfaceSubtractFigure;
import com.spvessel.spacevil.Core.MouseArgs;

import java.awt.Color;
import java.awt.Point;
import java.util.*;

public class FlowTest extends ActiveWindow {
    int count = 0;
    ContextMenu _context_menu;

    @Override
    public void initWindow() {
        isBorderHidden = true;
        setSize(870, 1000);
        setWindowName("FlowTest");
        setWindowTitle("FlowTest");

        setMinSize(500, 100);
        // setBackground(45, 45, 45);
        // setPadding(2, 2, 2, 2);

        // eventClose.clear();
        // eventClose.add(() -> {
        // WindowManager.appExit();
        // });

        TitleBar title = new TitleBar("FlowTest");
        addItem(title);
        // getWindow().eventKeyPress.add((sender, args) -> {
        // // System.out.println(getHandler().getFocusedItem());
        // });
        // getWindow().eventKeyRelease.add((sender, args) -> {
        // if (args.key == KeyCode.SPACE) {
        // btn5.eventMouseClick.execute(btn5, new MouseArgs());
        // }
        // });

        VerticalStack layout = new VerticalStack();
        layout.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        layout.setMargin(0, title.getHeight(), 0, 0);
        layout.setPadding(6, 6, 6, 6);
        layout.setSpacing(0, 10);
        layout.setBackground(255, 255, 255, 20);

        // adding toolbar
        addItem(layout);

        // Frame
        HorizontalStack toolbar = new HorizontalStack();
        toolbar.setBackground(51, 51, 51);
        toolbar.setItemName("toolbar");
        toolbar.setHeight(40);
        toolbar.setPadding(10, 0, 10, 0);
        toolbar.setSpacing(-10, 0);
        toolbar.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        toolbar.setContentAlignment(ItemAlignment.RIGHT);
        layout.addItem(toolbar);

        FreeArea flow = new FreeArea();
        flow.setPadding(6, 6, 6, 6);
        flow.setBackground(70, 70, 70);
        layout.addItem(flow);

        // btn add_at_center
        FloatItem flow_item = new FloatItem(this);
        flow_item.setPosition(200, 200);
        flow_item.setSize(300, 100);

        // btn add_at_begin
        ButtonCore btn1 = new ButtonCore();
        btn1.setBackground(13, 176, 255);
        btn1.setItemName("nameof(btn1)");
        btn1.setWidth(30);
        btn1.setHeight(30);
        btn1.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btn1.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        ItemState hovered = new ItemState(new Color(255, 255, 255, 125));
        btn1.addItemState(ItemStateType.HOVERED, hovered);
        btn1.eventMouseClick.add((sender, args) -> {
            // PopUpMessage pop = new PopUpMessage("Hello PopUpMessage!");
            // pop.show(this);
            MessageItem msg = new MessageItem("Choose one of this buttons", "Message:");
            ButtonCore btnDontSave = new ButtonCore("Do not save");
            btnDontSave.eventMouseClick.add((s, a) -> {
                System.out.println("btnDontSave is chosen");
            });
            msg.addUserButton(btnDontSave, 2); // id must be > 1
            msg.onCloseDialog.add(() -> {
                System.out.println(msg.getResult() + " " + msg.getUserButtonResult());
            });
            msg.show(this);

            // InputDialog id = new InputDialog("Input text", "Apply");
            // id.show(this);

            // LoadingScreen ls = new LoadingScreen();
            // ls.show(this);
        });
        // btn1.setCustomFigure(new CustomFigure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 180)));
        btn1.setMargin(0, 0, 20, 0);
        btn1.setHoverRule(ItemRule.STRICT);

        ButtonCore btn2 = new ButtonCore();
        btn2.setBackground(121, 223, 152);
        btn2.setItemName("nameof(btn2)");
        btn2.setWidth(30);
        btn2.setHeight(30);
        btn2.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btn2.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        btn2.addItemState(ItemStateType.HOVERED, hovered);
        btn2.eventMouseClick.add((sender, args) -> {
            InputDialog id = new InputDialog("title", "actionName", "defaultText");
            id.show(this);
        });
        btn2.setCustomFigure(new CustomFigure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 0)));
        btn2.setHoverRule(ItemRule.STRICT);

        // btn add_at_end
        ButtonCore btn3 = new ButtonCore();
        btn3.setBackground(238, 174, 128);
        btn3.setItemName("nameof(btn3)");
        btn3.setWidth(30);
        btn3.setHeight(30);
        btn3.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btn3.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        btn3.addItemState(ItemStateType.HOVERED, hovered);
        btn3.eventMouseClick.add((sender, args) -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ResizableItem frame = new ResizableItem();
                    // frame.setShadow(10, 5, 5, Color.RED);
                    // frame.setShadowExtension(20, 20);
                    frame.setBorder(new Border(Color.gray, new CornerRadius(), 2));
                    frame.setPadding(5, 5, 5, 5);
                    frame.setBackground(100, 100, 100);
                    frame.setSize(200, 200);
                    frame.setPosition(10 + i * 210, 10 + j * 210);
                    flow.addItem(frame);
                    // Graph graph = getGraph();
                    // graph.setPadding(5, 5, 5, 5);
                    // frame.addItem(graph);
                    OpenGLLayer ogl = new OpenGLLayer();
                    ogl.setMargin(0, 30, 0, 0);
                    frame.addItem(ogl);
                }
            }

            // frame.eventMouseDrag.add((s, a) -> {
            // System.out.println("clicl");
            // });

            // LoadingScreen screen = new LoadingScreen();
            // screen.show(this);

            // Thread task = new Thread(() -> {
            // for (int i = 1; i <= 100; i++) {
            // screen.setValue(i);
            // try {
            // Thread.sleep(50);
            // } catch (Exception e) {
            // }
            // }
            // screen.setToClose();
            // });
            // task.start();
        });
        btn3.setCustomFigure(new CustomFigure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 180)));
        btn3.setHoverRule(ItemRule.STRICT);

        ButtonCore btn4 = new ButtonCore();
        btn4.setBackground(187, 102, 187);
        btn4.setItemName("nameof(btn4)");
        btn4.setWidth(30);
        btn4.setHeight(30);
        btn4.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btn4.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        btn4.addItemState(ItemStateType.HOVERED, hovered);
        AlbumSideList side = new AlbumSideList(this, Side.LEFT);
        // side.setAttachSide(Side.TOP);
        Album al1 = new Album("Album1", "C:\\");
        al1._topLayout.setItemName("topLayout1");
        Album al2 = new Album("Album2", "C:\\");
        al2._topLayout.setItemName("topLayout2");
        side.addAlbum(al1);
        side.addAlbum(al2);
        // side.addAlbum(new Album("Album2", "C:\\"));
        // side.addAlbum(new Album("Album3", "C:\\"));
        // side.addAlbum(new Album("Album4", "C:\\"));

        btn4.eventMouseClick.add((sender, args) -> {
            // flow.addItem(getBlockList());
            side.show();
            // PopUpMessage popUpInfo = new PopUpMessage(
            // "\n" + "Age: " + "\n" + "Sex: " + "\n" + "Race: " + "\n" + "Class: ");
            // popUpInfo.setTimeOut(3000);
            // popUpInfo.setHeight(200);
            // popUpInfo.show(this);
        });
        btn4.setCustomFigure(new CustomFigure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 0)));
        btn4.setHoverRule(ItemRule.STRICT);

        ButtonCore btn5 = new ButtonCore();
        btn5.setBackground(238, 174, 128);
        btn5.setItemName("nameof(btn5)");
        btn5.setWidth(30);
        btn5.setHeight(30);
        btn5.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btn5.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        btn5.addItemState(ItemStateType.HOVERED, hovered);
        // btn5.setMargin(0, 0, 50, 0);
        btn5.eventMouseClick.add((sender, args) -> {
            // MessageItem msg = new MessageItem("Set TRUE?", "Message:");
            // msg.onCloseDialog.add(() -> {
            // System.out.println(msg.getResult());
            // });
            // msg.show(this);

            OpenEntryDialog opd = new OpenEntryDialog("Save File:", FileSystemEntryType.FILE, OpenDialogType.SAVE);
            opd.addFilterExtensions("Text files (*.txt);*.txt", "Images (*.png, *.bmp, *.jpg);*.png,*.bmp,*.jpg");
            opd.onCloseDialog.add(() -> {
                System.out.println(opd.getResult());
            });
            opd.setDefaultPath("D:\\");
            opd.show(this);
        });
        btn5.setCustomFigure(new CustomFigure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 180)));
        btn5.setHoverRule(ItemRule.STRICT);

        // adding buttons
        toolbar.addItems(btn1, btn2, btn3, btn4, btn5);

        // _context_menu.setWidth(110);
        MenuItem restore = new MenuItem("Build Tool");
        // ImageItem res = new ImageItem(
        // DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN,
        // EmbeddedImageSize.SIZE_32X32));
        // // res.setSize(16, 16);
        // // res.setBackground(0, 0, 0, 0);
        // // res.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        // // res.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        // // restore.addItem(res);

        restore.eventMouseClick.add((sender, args) -> {
            flow.setHScrollOffset(0);
            flow.setVScrollOffset(0);
            System.out.println(restore.getSender().getItemName());
        });
        MenuItem x_plus = new MenuItem("X += 100");
        // x_plus.eventMouseClick += (sender, args) ->
        // {
        // flow.setHScrollOffset(flow.getHScrollOffset() + 10);
        // };
        MenuItem y_plus = new MenuItem("Build Tool");
        // y_plus.eventMouseClick += (sender, args) ->
        // {
        // flow.setVScrollOffset(flow.getVScrollOffset() + 10);
        // };
        MenuItem addition = new MenuItem("ADdition");
        // context
        _context_menu = new ContextMenu(this);// new ContextMenu(this, restore, x_plus, y_plus, addition);
        _context_menu.setItemName("Base");
        _context_menu.addItems(restore, x_plus, y_plus, addition);
        // _context_menu.setWidth(200);

        ContextMenu addition_menu = new ContextMenu(this);
        addition_menu.setItemName("Addition");
        addition_menu.setSize(110, 94);
        MenuItem x_minus = new MenuItem("X -= 100");
        // x_minus.eventMouseClick += (sender, args) ->
        // {
        // flow.setHScrollOffset(flow.getHScrollOffset() - 10);
        // };
        MenuItem y_minus = new MenuItem("Y -= 100");
        y_minus.eventMouseClick.add((sender, args) -> {
            // flow.setVScrollOffset(flow.getVScrollOffset() - 10);
            System.out.println("menu width " + _context_menu.getWidth());
        });
        MenuItem ex_addition = new MenuItem("addition");
        addition_menu.addItems(x_minus, y_minus, ex_addition);

        addition.assignContextMenu(addition_menu);

        ContextMenu ex_menu = new ContextMenu(this);
        ex_menu.setSize(110, 64);
        ex_addition.assignContextMenu(ex_menu);
        flow.eventMouseClick.add((sender, args) -> _context_menu.show(sender, args));
        // addItem(new StopMenu());

        eventKeyPress.add((sender, args) -> {
            // if (args.key == KeyCode.V)
            //     CommonService.setClipboardString("SetClipBoardString");
            // if (args.key == KeyCode.C)
            //     System.out.println(CommonService.getClipboardString());
            if (args.key == KeyCode.F)
                System.out.println(WindowsBox.getCurrentFocusedWindow().getWindowName());
        });

        setAntiAliasingQuality(MSAA.MSAA_8X);
        int _diameter = 180;

        // create items
        InterfaceBaseItem cRed = getCircle(_diameter, new Color(255, 94, 94));
        cRed.setItemName("cRed");
        InterfaceBaseItem cGreen = getCircle(_diameter, new Color(16, 180, 111));
        cGreen.setItemName("cGreen");
        InterfaceBaseItem cBlue = getCircle(_diameter, new Color(10, 162, 232));
        cBlue.setItemName("cBlue");

        setCircleAlignment(cRed, ItemAlignment.TOP);
        setCircleAlignment(cGreen, ItemAlignment.LEFT, ItemAlignment.BOTTOM);
        setCircleAlignment(cBlue, ItemAlignment.RIGHT, ItemAlignment.BOTTOM);

        // add items to window
        addItems(cGreen, cRed, cBlue);

        Effects.addEffect(cRed, getCircleEffect(cRed, cBlue));
        Effects.addEffect(cRed, getCircleCenterEffect(cRed));

        Effects.addEffect(cGreen, getCircleEffect(cGreen, cRed));
        Effects.addEffect(cGreen, getCircleCenterEffect(cGreen));

        Effects.addEffect(cBlue, getCircleEffect(cBlue, cGreen));
        Effects.addEffect(cBlue, getCircleCenterEffect(cBlue));
    }

    public static InterfaceBaseItem getCircle(int diameter, Color color) {
        Ellipse circle = new Ellipse(64);
        circle.setSize(diameter, diameter);
        circle.setBackground(color);
        circle.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        circle.setShadow(10, 0, 0, Color.BLACK);
        circle.setShadowExtension(2, 2);
        return circle;
    }

    public static void setCircleAlignment(InterfaceBaseItem circle, ItemAlignment... alignment) {
        List<ItemAlignment> list = Arrays.asList(alignment);

        int offset = circle.getWidth() / 3;

        if (list.contains(ItemAlignment.TOP)) {
            circle.setMargin(circle.getMargin().left, circle.getMargin().top - offset + 10, circle.getMargin().right,
                    circle.getMargin().bottom);
        }

        if (list.contains(ItemAlignment.BOTTOM)) {
            circle.setMargin(circle.getMargin().left, circle.getMargin().top, circle.getMargin().right,
                    circle.getMargin().bottom - offset);
        }

        if (list.contains(ItemAlignment.LEFT)) {
            circle.setMargin(circle.getMargin().left - offset, circle.getMargin().top, circle.getMargin().right,
                    circle.getMargin().bottom);
        }

        if (list.contains(ItemAlignment.RIGHT)) {
            circle.setMargin(circle.getMargin().left, circle.getMargin().top, circle.getMargin().right - offset,
                    circle.getMargin().bottom);
        }
    }

    public static SubtractFigure getCircleEffect(InterfaceBaseItem circle, InterfaceBaseItem subtract) {
        int diameter = circle.getHeight();
        float scale = 1.1f;
        int diff = (int) (diameter * scale - diameter) / 2;
        int xOffset = subtract.getX() - circle.getX() - diff;
        int yOffset = subtract.getY() - circle.getY() - diff;

        SubtractFigure effect = new SubtractFigure(
                new CustomFigure(false, GraphicsMathService.getEllipse(diameter, diameter, 0, 0, 64)));
        effect.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        effect.setSizeScale(scale, scale);
        effect.setPositionOffset(xOffset, yOffset);

        return effect;
    }

    public static SubtractFigure getCircleCenterEffect(InterfaceBaseItem circle) {
        float scale = 0.4f;
        int diameter = (int) (circle.getHeight() * scale);
        SubtractFigure effect = new SubtractFigure(
                new CustomFigure(true, GraphicsMathService.getEllipse(diameter, diameter, 0, 0, 64)));
        effect.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        return effect;
    }

    // private ResizableItem getBlockList() {
    // BlockList block = new BlockList();
    // // ResizableItem block = new ResizableItem();
    // block.eventMouseClick.add((sender, args) -> {
    // // System.out.println(block.getX() + " " + block.getY());
    // });
    // block.setBackground(45, 45, 45);
    // // block.SetBackground(255, 181, 111);
    // block.setWidth(250);
    // block.setHeight(200);
    // block.setX(100);
    // block.setY(100);
    // return block;
    // }
    List<float[]> koh = new LinkedList<>();

    private Graph getGraph() {
        Graph graph_points = new Graph();
        // graph_points.setMargin(20, 20, 20, 20);
        graph_points.setBackground(32, 32, 32);
        // graph_points.setLineColor(new Color(218, 82, 160, 255));
        graph_points.setLineColor(new Color(100, 100, 100, 255));
        graph_points.setPointColor(new Color(10, 162, 232));
        graph_points.setPointThickness(20.0f);
        graph_points.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        graph_points.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        // List<float[]> crd = new List<float[]>();
        // crd.add(new float[3] { 100.0f, 0.0f, 0.0f });
        // crd.add(new float[3] { 50.0f, 100.0f, 0.0f });
        // crd.add(new float[3] { 150.0f, 100.0f, 0.0f });
        // graph_points.setPointsCoord(crd);
        // graph_points.setPointsCoord(GraphicsMathService.getRoundSquare(300, 300, 50, 0, 0));

        if (koh.isEmpty()) {
            Point point1 = new Point(200, 200);
            Point point2 = new Point(500, 200);
            Point point3 = new Point(350, 400);
            fractalKoh(koh, point1, point2, point3, 4);
            fractalKoh(koh, point2, point3, point1, 4);
            fractalKoh(koh, point3, point1, point2, 4);

            point1 = new Point(100, 100);
            point2 = new Point(250, 100);
            point3 = new Point(175, 200);
            List<float[]> koh2 = new LinkedList<>();
            fractalKoh(koh2, point1, point2, point3, 4);
            fractalKoh(koh2, point2, point3, point1, 4);
            fractalKoh(koh2, point3, point1, point2, 4);
            koh2 = GraphicsMathService.moveShape(koh2, 175, 135);

            point1 = new Point(50, 50);
            point2 = new Point(125, 50);
            point3 = new Point(87, 100);
            List<float[]> koh3 = new LinkedList<>();
            fractalKoh(koh3, point1, point2, point3, 3);
            fractalKoh(koh3, point2, point3, point1, 3);
            fractalKoh(koh3, point3, point1, point2, 3);
            koh3 = GraphicsMathService.moveShape(koh3, 262, 200);

            koh.addAll(koh2);
            koh.addAll(koh3);
            System.out.println(koh.size());
        }

        graph_points.setPointsCoord(koh);

        // Point a = new Point(0, 10);
        // Point b = new Point(this.getWidth(), 10);
        // drawKochLine(koh, a, b, 0, 1000);

        // graph_points.setPointsCoord(getTest());
        // graph_points.setPointsCoord(GraphicsMathService.getTriangle(100, 100, 0,
        // 0,
        // 0));
        // graph_points.setWidth(300);
        // graph_points.setHeight(300);
        // graph_points.setX(200);
        // graph_points.setY(200);
        //
        // graph_points.setShapePointer(GraphicsMathService.getTriangle(graph_points.getPointThickness(),
        // graph_points.getPointThickness()));
        //
        graph_points.setShapePointer(GraphicsMathService.getCross(graph_points.getPointThickness(),
                graph_points.getPointThickness(), 1, 45));

        // graph_points.setShapePointer(GraphicsMathService.getStar(graph_points.getPointThickness(),
        // graph_points.getPointThickness() / 2.0f, 8));
        return graph_points;
    }

    public ButtonCore getButton(String name) {
        ButtonCore btn = new ButtonCore(name);
        btn.setMaxHeight(30);
        btn.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        btn.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        return btn;
    }

    public void fractalKoh(List<float[]> pointsList, Point p1, Point p2, Point p3, int iterations) {

        if (iterations > 0) {
            Point p4 = new Point((p2.x + 2 * p1.x) / 3, (p2.y + 2 * p1.y) / 3);
            Point p5 = new Point((2 * p2.x + p1.x) / 3, (p1.y + 2 * p2.y) / 3);
            Point ps = new Point((p2.x + p1.x) / 2, (p2.y + p1.y) / 2);
            Point pn = new Point((4 * ps.x - p3.x) / 3, (4 * ps.y - p3.y) / 3);

            pointsList.add(new float[] { p4.x, p4.y });
            pointsList.add(new float[] { pn.x, pn.y });
            pointsList.add(new float[] { p5.x, p5.y });
            pointsList.add(new float[] { pn.x, pn.y });
            pointsList.add(new float[] { p4.x, p4.y });
            pointsList.add(new float[] { p5.x, p5.y });

            //рекурсивно вызываем функцию нужное число раз
            fractalKoh(pointsList, p4, pn, p5, iterations - 1);
            fractalKoh(pointsList, pn, p5, p4, iterations - 1);
            fractalKoh(pointsList, p1, p4, new Point((2 * p1.x + p3.x) / 3, (2 * p1.y + p3.y) / 3), iterations - 1);
            fractalKoh(pointsList, p5, p2, new Point((2 * p2.x + p3.x) / 3, (2 * p2.y + p3.y) / 3), iterations - 1);
        }
    }

    private List<float[]> getTest() {
        List<float[]> stars = new LinkedList<>();
        int skew = 10;
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                stars.add(new float[] { i * skew + skew, j * skew + skew });
            }
        }
        return stars;
    }
}