package com.spvessel.spacevil.View;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Flags.*;

public class MyDialogBox extends DialogWindow {

    public MyDialogBox() {
    }

    @Override
    public void initWindow() {
        // window appearance
        setParameters("MyDialogBox:", "MyDialogBox:", 400, 300, false);
        setBackground(55, 55, 55);
        setSize(400, 300);
        isAlwaysOnTop = true;

        // add titlebar and button to the window
        TitleBar title = new TitleBar("MyDialogBox:");
        ButtonCore okButton = new ButtonCore("Ok");
        okButton.setWidth(100);
        okButton.setAlignment(ItemAlignment.BOTTOM, ItemAlignment.HCENTER);
        okButton.setMargin(0, 0, 0, 20);
        addItems(title, okButton);

        // change actions of titlebar close button
        title.getMinimizeButton().setVisible(false); // hides button
        title.getMaximizeButton().setVisible(false); // hides button
        title.getCloseButton().eventMouseClick.clear();
        title.getCloseButton().eventMouseClick.add((sender, args) -> {
            close(); // now it closes the MyDialogBox
        });

        // Ok button will set the result
        okButton.eventMouseClick.add((sender, args) -> {
            _result = true; // set result
            close(); // close
        });
    }

    // event which will invoked when MyDialogBox is closed to perform actions.
    public EventCommonMethod onCloseDialog = new EventCommonMethod();
    CoreWindow _handler = null;

    // show the MyDialogBox.
    public void show(CoreWindow handler) {
        _handler = handler;
        _result = false;
        super.show();
    }

    // close the MyDialogBox.
    public void close() {
        // perform assigned actions
        if (onCloseDialog != null)
            onCloseDialog.execute();

        super.close();

        try {
            synchronized (_handler) {
                _handler.notify();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // getting result
    boolean _result = false;

    boolean getResult() {
        return _result;
    }

    void showAndWait(CoreWindow handler) {
        show(handler);
        try {
            synchronized (handler) {
                // handler.hold();
                handler.wait();
                // handler.proceed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}