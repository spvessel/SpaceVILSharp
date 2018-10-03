package com.spvessel.View;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.InterfaceMouseMethodState;
import com.spvessel.Decorations.ItemState;
import com.spvessel.Engine.GraphicsMathService;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.ItemStateType;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Items.ButtonCore;
import com.spvessel.Items.FloatItem;
import com.spvessel.Items.Frame;
import com.spvessel.Items.FreeArea;
import com.spvessel.Items.HorizontalScrollBar;
import com.spvessel.Items.PointsContainer;
import com.spvessel.Items.PopUpMessage;
import com.spvessel.Items.ResizableItem;
import com.spvessel.Items.TextLine;
import com.spvessel.Items.TitleBar;
import com.spvessel.Items.Triangle;
import com.spvessel.Items.VerticalScrollBar;
import com.spvessel.Items.VerticalSlider;
import com.spvessel.Windows.ActiveWindow;
import com.spvessel.Windows.WindowLayout;

import java.awt.*;

public class FlowTest extends ActiveWindow {
    int count = 0;

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
        // graph_points.SetPointsCoord(crd);
        graph_points.setPointsCoord(GraphicsMathService.getRoundSquare(300, 300, 50, 0, 0));
        // graph_points.setPointsCoord(GraphicsMathService.getTriangle(100, 100, 0, 0,
        // 0));
        // graph_points.setWidth(300);
        // graph_points.setHeight(300);
        // graph_points.setX(200);
        // graph_points.setY(200);
        // graph_points.SetShapePointer(GraphicsMathService.GetTriangle(graph_points.GetPointThickness(),
        // graph_points.GetPointThickness()));
        graph_points.setShapePointer(GraphicsMathService.getCross(graph_points.getPointThickness(),
                graph_points.getPointThickness(), 2, 45));
        // graph_points.SetShapePointer(GraphicsMathService.GetStar(graph_points.GetPointThickness(),
        // graph_points.GetPointThickness() / 2.0f));
        return graph_points;
    }
}