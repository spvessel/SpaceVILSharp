package com.spvessel.Engine;

import com.spvessel.Cores.*;
import com.spvessel.Items.*;
import com.spvessel.Windows.WindowLayout;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ActionManager {
    public ConcurrentLinkedQueue<EventTask> stackEvents = new ConcurrentLinkedQueue<EventTask>();

    public ManualResetEvent execute = new ManualResetEvent(false);
    // public final Semaphore execute = new Semaphore(1);
    WindowLayout _handler;
    boolean _stoped;

    public ActionManager(WindowLayout wnd) {
        _handler = wnd;
    }

    private Lock managerLock = new ReentrantLock();

    public void startManager() {
        _stoped = false;
        while (!_stoped) {
            try {
                execute.waitOne();
            } catch (InterruptedException e) {
            }
            managerLock.lock();
            executeActions();
            execute.reset();
            managerLock.unlock();
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
    private void invokeFocusGetEvent(VisualItem sender) {
        if (sender.eventFocusGet != null)
            sender.eventFocusGet.execute(sender);
    }

    private void invokeFocusLostEvent(VisualItem sender) {
        if (sender.eventFocusLost != null)
            sender.eventFocusLost.execute(sender);
    }

    private void invokeResizedEvent(VisualItem sender) {
        if (sender.eventResized != null)
            sender.eventResized.execute(sender);
    }

    private void invokeDestroyedEvent(VisualItem sender) {
        if (sender.eventDestroyed != null)
            sender.eventDestroyed.execute(sender);
    }

    // mouse input
    private void invokeMouseClickEvent(VisualItem sender, MouseArgs args) {
        if (sender.eventMouseClick != null)
            sender.eventMouseClick.execute(sender, args);
    }

    private void invokeMouseHoverEvent(VisualItem sender, MouseArgs args) {
        if (sender.eventMouseHover != null)
            sender.eventMouseHover.execute(sender, args);
    }

    private void invokeMousePressedEvent(VisualItem sender, MouseArgs args) {
        if (sender.eventMousePressed != null)
            sender.eventMousePressed.execute(sender, args);
    }

    private void invokeMouseReleaseEvent(VisualItem sender, MouseArgs args) {
        if (sender.eventMouseRelease != null)
            sender.eventMouseRelease.execute(sender, args);
    }

    private void invokeMouseDragEvent(VisualItem sender, MouseArgs args) {
        if (sender.eventMouseDrag != null)
            sender.eventMouseDrag.execute(sender, args);
    }

    private void invokeMouseDropEvent(VisualItem sender, MouseArgs args) {
        if (sender.eventMouseDrag != null)
            sender.eventMouseDrag.execute(sender, args);
    }

    private void invokeMouseScrollUpEvent(VisualItem sender, MouseArgs args) {
        if (sender.eventScrollUp != null)
            sender.eventScrollUp.execute(sender, args);
    }

    private void invokeMouseScrollDownEvent(VisualItem sender, MouseArgs args) {
        if (sender.eventScrollDown != null)
            sender.eventScrollDown.execute(sender, args);
    }

    // keyboard input
    private void invokeKeyPressEvent(VisualItem sender, KeyArgs args) {
        if (sender.eventKeyPress != null)
            sender.eventKeyPress.execute(sender, args);
    }

    private void invokeKeyReleaseEvent(VisualItem sender, KeyArgs args) {
        if (sender.eventKeyRelease != null)
            sender.eventKeyRelease.execute(sender, args);
    }

    private void invokeTextInputEvent(VisualItem sender, TextInputArgs args) {
        if (sender.eventTextInput != null)
            sender.eventTextInput.execute(sender, args);
    }
}