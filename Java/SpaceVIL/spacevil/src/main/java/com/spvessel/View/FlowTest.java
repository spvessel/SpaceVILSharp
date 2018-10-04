package com.spvessel.View;

import com.spvessel.Cores.*;
import com.spvessel.Decorations.*;
import com.spvessel.Engine.GraphicsMathService;
import com.spvessel.Flags.*;
import com.spvessel.Items.*;
import com.spvessel.Windows.*;

import java.awt.*;

public class FlowTest extends ActiveWindow {
    int count = 0;
    ContextMenu _context_menu;

    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "FlowTest", "FlowTest", 1200, 800, true);
        setHandler(Handler);

        Handler.setMinSize(500, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        TitleBar title = new TitleBar("FlowTest");
        Handler.addItem(title);

        FreeArea layout = new FreeArea();
        layout.setMargin(0, 30, 0, 0);
        layout.setPadding(6, 6, 6, 6);
        layout.setBackground(70, 70, 70);
        Handler.addItem(layout);

        // context
        _context_menu = new ContextMenu(Handler);
        _context_menu.setWidth(110);
        com.spvessel.Items.MenuItem restore = new com.spvessel.Items.MenuItem("Restore");
        // restore.EventMouseClick += (sender, args) =>
        // {
        // flow.setHScrollOffset(0);
        // flow.setVScrollOffset(0);
        // };
        com.spvessel.Items.MenuItem x_plus = new com.spvessel.Items.MenuItem("X += 100");
        // x_plus.EventMouseClick += (sender, args) =>
        // {
        // flow.setHScrollOffset(flow.getHScrollOffset() + 10);
        // };
        com.spvessel.Items.MenuItem y_plus = new com.spvessel.Items.MenuItem("Y += 100");
        // y_plus.EventMouseClick += (sender, args) =>
        // {
        // flow.setVScrollOffset(flow.getVScrollOffset() + 10);
        // };
        com.spvessel.Items.MenuItem addition = new com.spvessel.Items.MenuItem("Addition");

        _context_menu.addItems(restore, x_plus, y_plus, addition);

        ContextMenu addition_menu = new ContextMenu(Handler);
        addition_menu.setSize(110, 94);
        com.spvessel.Items.MenuItem x_minus = new com.spvessel.Items.MenuItem("X -= 100");
        // x_minus.EventMouseClick += (sender, args) =>
        // {
        // flow.setHScrollOffset(flow.getHScrollOffset() - 10);
        // };
        com.spvessel.Items.MenuItem y_minus = new com.spvessel.Items.MenuItem("Y -= 100");
        // y_minus.EventMouseClick += (sender, args) =>
        // {
        // Console.WriteLine("y minus");
        // flow.setVScrollOffset(flow.getVScrollOffset() - 10);
        // };
        com.spvessel.Items.MenuItem ex_addition = new com.spvessel.Items.MenuItem("Addition");
        addition_menu.addItems(x_minus, y_minus, ex_addition);

        addition.assignContexMenu(addition_menu);

        ContextMenu ex_menu = new ContextMenu(Handler);
        ex_menu.setSize(110, 64);
        ex_addition.assignContexMenu(ex_menu);
        layout.addContextMenu(_context_menu);

        VerticalScrollBar v_slider = new VerticalScrollBar();
        v_slider.setHeightPolicy(SizePolicy.FIXED);
        v_slider.setHeight(500);
        v_slider.setPosition(0, 0);
        layout.addItem(v_slider);

        FloatItem flow_item = new FloatItem(Handler);
        flow_item.setBackground(100, 100, 250);
        flow_item.setPosition(200, 200);
        flow_item.setSize(300, 100);
        flow_item.setPassEvents(false);
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        flow_item.addItemState(ItemStateType.HOVERED, hovered);

        ButtonCore btn_action = new ButtonCore();
        btn_action.setBackground(100, 255, 150);
        btn_action.setText("Columnar");
        btn_action.setForeground(0, 0, 0);
        btn_action.setItemName("Action");
        btn_action.setWidth(256);
        btn_action.setHeight(128);
        btn_action.setWidthPolicy(SizePolicy.FIXED);
        btn_action.setHeightPolicy(SizePolicy.FIXED);
        btn_action.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        btn_action.border.setRadius(10);
        InterfaceMouseMethodState flow_click = (sender, args) -> {
            // flow_item.show(sender, args);
            // PopUpMessage pop = new PopUpMessage("Hello PopUpMessage!", Handler);
            // pop.show();
        };
        btn_action.eventMouseClick.add(flow_click);

        ResizableItem frame = new ResizableItem();
        frame.setPadding(10, 10, 10, 10);
        frame.setBackground(100, 100, 100);
        frame.setSize(300, 300);
        frame.setPosition(200, 200);
        layout.addItem(frame);
        PointsContainer graph = getPointsContainer();
        frame.addItem(graph);

        // ResizableItem res = new ResizableItem();
        // res.setSize(300, 300);
        // res.setBackground(55, 55, 55);
        // res.setPassEvents(false);
        // layout.addItem(res);
        // res.addItem(btn_action);
    }

    private PointsContainer getPointsContainer() {
        PointsContainer graph_points = new PointsContainer();
        graph_points.setPointColor(new Color(10, 255, 10));
        graph_points.setPointThickness(10.0f);
        graph_points.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        graph_points.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        // List<float[]> crd = new List<float[]>();
        // crd.Add(new float[3] { 100.0f, 0.0f, 0.0f });
        // crd.Add(new float[3] { 50.0f, 100.0f, 0.0f });
        // crd.Add(new float[3] { 150.0f, 100.0f, 0.0f });
        // graph_points.setPointsCoord(crd);
        graph_points.setPointsCoord(GraphicsMathService.getRoundSquare(300, 300, 50, 0, 0));
        // graph_points.setPointsCoord(GraphicsMathService.getTriangle(100, 100, 0, 0,
        // 0));
        // graph_points.setWidth(300);
        // graph_points.setHeight(300);
        // graph_points.setX(200);
        // graph_points.setY(200);
        // graph_points.setShapePointer(GraphicsMathService.getTriangle(graph_points.getPointThickness(),
        // graph_points.getPointThickness()));
        // graph_points.setShapePointer(GraphicsMathService.getCross(graph_points.getPointThickness(),
        // graph_points.getPointThickness(), 2, 45));
        // graph_points.setShapePointer(GraphicsMathService.getStar(graph_points.getPointThickness(),
        // graph_points.getPointThickness() / 2.0f));
        return graph_points;
    }
}