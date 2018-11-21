package com.spvessel;

import com.spvessel.Core.KeyArgs;
import com.spvessel.Core.MouseArgs;
import com.spvessel.Core.TextInputArgs;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ActionManager {
    public ConcurrentLinkedQueue<EventTask> stackEvents = new ConcurrentLinkedQueue<EventTask>();

    public ManualResetEvent execute = new ManualResetEvent(false);
    private Lock managerLock = new ReentrantLock();
    WindowLayout _handler;
    boolean _stoped;

    public ActionManager(WindowLayout wnd) {
        _handler = wnd;
    }

    public void startManager() {
        _stoped = false;
        while (!_stoped) {
            try {
                execute.waitOne();
            } catch (InterruptedException e) {
            }
            managerLock.lock();
            try {
                executeActions();
            } finally {
                execute.reset();
                managerLock.unlock();
            }
        }
    }

    public void addTask(EventTask task) {
        managerLock.lock();
        try {
            stackEvents.add(task);
        } finally {
            managerLock.unlock();
        }
    }

    public void stopManager() {
        _stoped = true;
    }

    protected void executeActions() {
        if (stackEvents.size() == 0)
            return;

        while (stackEvents.size() > 0) {
            EventTask tmp = stackEvents.poll();
            if (tmp != null) {
                executeAction(tmp);
            }
        }
    }

    private void executeAction(EventTask task) {
        switch (task.action) {
        case MOUSE_RELEASE:
            invokeMouseClickEvent(task.item, (MouseArgs) task.args);
            break;
        case MOUSE_DOUBLE_CLICK:
            invokeMouseDoubleClickEvent(task.item, (MouseArgs) task.args);
            break;
        case MOUSE_PRESS:
            invokeMousePressedEvent(task.item, (MouseArgs) task.args);
            break;
        case MOUSE_HOVER:
            invokeMouseHoverEvent(task.item, (MouseArgs) task.args);
            break;
        case MOUSE_DRAG:
            invokeMouseDragEvent(task.item, (MouseArgs) task.args);
            break;
        case FOCUS_GET:
            invokeFocusGetEvent(task.item);
            break;
        case KEY_PRESS:
            invokeKeyPressEvent(task.item, (KeyArgs) task.args);
            break;
        case KEY_RELEASE:
            invokeKeyReleaseEvent(task.item, (KeyArgs) task.args);
            break;
        case TEXT_INPUT:
            invokeTextInputEvent(task.item, (TextInputArgs) task.args);
            break;
        default:
            break;
        }
    }

    // common events
    private void invokeFocusGetEvent(Prototype sender) {
        if (sender.eventFocusGet != null)
            sender.eventFocusGet.execute(sender);
    }

    private void invokeFocusLostEvent(Prototype sender) {
        if (sender.eventFocusLost != null)
            sender.eventFocusLost.execute(sender);
    }

    private void invokeResizedEvent(Prototype sender) {
        if (sender.eventResize != null)
            sender.eventResize.execute(sender);
    }

    private void invokeDestroyedEvent(Prototype sender) {
        if (sender.eventDestroy != null)
            sender.eventDestroy.execute(sender);
    }

    // mouse input
    private void invokeMouseClickEvent(Prototype sender, MouseArgs args) {
        if (sender.eventMouseClick != null)
            sender.eventMouseClick.execute(sender, args);
    }
    private void invokeMouseDoubleClickEvent(Prototype sender, MouseArgs args) {
        if (sender.eventMouseDoubleClick != null)
            sender.eventMouseDoubleClick.execute(sender, args);
    }

    private void invokeMouseHoverEvent(Prototype sender, MouseArgs args) {
        if (sender.eventMouseHover != null)
            sender.eventMouseHover.execute(sender, args);
    }

    private void invokeMousePressedEvent(Prototype sender, MouseArgs args) {
        if (sender.eventMousePress != null)
            sender.eventMousePress.execute(sender, args);
    }

    private void invokeMouseDragEvent(Prototype sender, MouseArgs args) {
        if (sender.eventMouseDrag != null)
            sender.eventMouseDrag.execute(sender, args);
    }

    private void invokeMouseDropEvent(Prototype sender, MouseArgs args) {
        if (sender.eventMouseDrag != null)
            sender.eventMouseDrag.execute(sender, args);
    }

    private void invokeMouseScrollUpEvent(Prototype sender, MouseArgs args) {
        if (sender.eventScrollUp != null)
            sender.eventScrollUp.execute(sender, args);
    }

    private void invokeMouseScrollDownEvent(Prototype sender, MouseArgs args) {
        if (sender.eventScrollDown != null)
            sender.eventScrollDown.execute(sender, args);
    }

    // keyboard input
    private void invokeKeyPressEvent(Prototype sender, KeyArgs args) {
        if (sender.eventKeyPress != null)
            sender.eventKeyPress.execute(sender, args);
    }

    private void invokeKeyReleaseEvent(Prototype sender, KeyArgs args) {
        if (sender.eventKeyRelease != null)
            sender.eventKeyRelease.execute(sender, args);
    }

    private void invokeTextInputEvent(Prototype sender, TextInputArgs args) {
        if (sender.eventTextInput != null)
            sender.eventTextInput.execute(sender, args);
    }
}