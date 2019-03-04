package com.spvessel.spacevil.View;

import java.awt.Color;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.BlankItem;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.WindowLayout;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.InputEventType;

public class EventTest extends ActiveWindow {

    private BlankItem getBlankItem(String name) {
        BlankItem blank = new BlankItem();
        blank.setItemName(name);
        blank.setStyle(Style.getFrameStyle());
        blank.setMargin(50, 50, 50, 50);
        blank.setBorder(new Border(new Color(0, 162, 232), new CornerRadius(10), 3));
        blank.setBackground(255, 255, 255, 50);
        blank.eventMouseClick.add((sender, args) -> {
            System.out.println(blank.getItemName() + " EventMouseClick");
        });
        blank.eventMouseDoubleClick.add((sender, args) -> {
            System.out.println(blank.getItemName() + " EventMouseDoubleClick");
        });
        blank.eventKeyPress.add((sender, args) -> {
            System.out.println(blank.getItemName() + " EventKeyPress");
        });
        blank.eventKeyRelease.add((sender, args) -> {
            System.out.println(blank.getItemName() + " EventKeyRelease");
        });
        // blank.eventMouseHover.add((sender, args) -> {
        //     System.out.println(blank.getItemName() + " EventMouseHover");
        // });
        return blank;
    }

    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout("EventTest", "EventTest", 400, 400, true);
        setHandler(Handler);
        Handler.setMinSize(400, 400);
        Handler.isCentered = true;

        TitleBar title = new TitleBar("EventTest");
        title.setShadow(5, 0, 3, new Color(0, 0, 0, 150));
        Handler.addItem(title);

        Frame cc = new Frame();
        cc.setMargin(0, title.getHeight() + 10, 0, 0);
        cc.setBackground(50, 50, 50);
        Handler.addItem(cc);

        BlankItem b1 = getBlankItem("Blank1");
        BlankItem b2 = getBlankItem("Blank2");
        BlankItem b3 = getBlankItem("Blank3");

        cc.addItem(b1);
        b1.addItem(b2);
        b2.addItem(b3);

        b3.setFocus();

        b3.setPassEvents(false);
        b2.setPassEvents(false);
        // b2.setPassEvents(true, InputEventType.KEY_PRESS, InputEventType.KEY_RELEASE);

        Handler.getWindow().eventMouseClick.add((sender, args) -> {
            System.out.println(Handler.getWindow().getItemName() + " EventMouseClick");
        });
        Handler.getWindow().eventMouseDoubleClick.add((sender, args) -> {
            System.out.println(Handler.getWindow().getItemName() + " EventMouseDoubleClick");
        });
        Handler.getWindow().eventKeyPress.add((sender, args) -> {
            System.out.println(Handler.getWindow().getItemName() + " EventKeyPress");
        });
        Handler.getWindow().eventKeyRelease.add((sender, args) -> {
            System.out.println(Handler.getWindow().getItemName() + " EventKeyRelease");
        });
        // Handler.getWindow().eventMouseHover.add((sender, args) -> {
        //     System.out.println(Handler.getWindow().getItemName() + " EventMouseHover");
        // });
    }
}