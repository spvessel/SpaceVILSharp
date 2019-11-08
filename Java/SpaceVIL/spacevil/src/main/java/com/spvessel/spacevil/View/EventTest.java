package com.spvessel.spacevil.View;

import java.awt.Color;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.BlankItem;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.ItemsRefreshManager;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.MSAA;

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

        isBorderHidden = true;
        setSize(400, 400);
        setWindowName("EventTest");
        setWindowTitle("EventTest");

        setMinSize(400, 400);
        isCentered = true;

        setPadding(10, 10, 10, 10);
        // setBackground(new Color(0, 0, 0, 0));
        setBorderRadius(10);
        setAntiAliasingQuality(MSAA.MSAA_8X);
        // isTransparent = true;

        // TitleBar title = new TitleBar("EventTest");
        // title.setShadow(5, 0, 3, new Color(0, 0, 0, 150));
        // addItem(title);

        // Frame cc = new Frame();
        // cc.setMargin(0, title.getHeight() + 10, 0, 0);
        // cc.setBackground(50, 50, 50);
        // addItem(cc);

        BlankItem b1 = getBlankItem("Blank1");
        addItem(b1);
        // BlankItem b2 = getBlankItem("Blank2");
        // BlankItem b3 = getBlankItem("Blank3");

        // cc.addItem(b1);
        // b1.addItem(b2);
        // b2.addItem(b3);

        // b3.setFocus();

        // b3.setPassEvents(false);
        // b2.setPassEvents(false);
        // b2.setPassEvents(true, InputEventType.KEY_PRESS, InputEventType.KEY_RELEASE);

        eventMouseClick.add((sender, args) -> {
            System.out.println(getWindowName() + " EventMouseClick");
        });
        eventMouseDoubleClick.add((sender, args) -> {
            System.out.println(getWindowName() + " EventMouseDoubleClick");
        });
        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.R) {
                // ItemsRefreshManager.printSizeOfShapes();
                // System.out.println(getWindowName() + " EventKeyPress");
            }
        });
        eventKeyRelease.add((sender, args) -> {
            System.out.println(getWindowName() + " EventKeyRelease");
        });
        // getWindow().eventMouseHover.add((sender, args) -> {
        //     System.out.println(getWindow().getItemName() + " EventMouseHover");
        // });
    }
}