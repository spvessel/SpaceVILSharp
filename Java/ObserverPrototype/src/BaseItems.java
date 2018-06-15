import java.util.ArrayList;
import java.util.List;

public abstract class BaseItems implements EventListener {
    public EventManager eventManager = null;

    public BaseItems(String name) {
        this.name = name;
        eventManager = new EventManager();
    }

    public enum SizePolicy {
        Fixed,
        Strech,
        Expand,
        Ignored
    }

    String name;

    private List<BaseItems> children = new ArrayList<>();
    private BaseItems parent = null;

    public List<BaseItems> getChildren() {
        return children;
    }

    public void addChildren(BaseItems ch) {
        children.add(ch);
        if (ch.getParent() != null) ch.getParent().removeChildren(ch);
        ch.setParent(this);

        if (ch.getWidthPolicy() != SizePolicy.Fixed)
            addEventListener(EventManager.RESIZE_WIDTH, ch);

        if (ch.getHeightPolicy() != SizePolicy.Fixed)
            addEventListener(EventManager.RESIZE_HEIGHT, ch);

        //TODO возможно, нужны дополнительные условие с alignments
        addEventListener(EventManager.MOVED_X, ch);
        addEventListener(EventManager.MOVED_Y, ch);
    }

    public void removeChildren(BaseItems ch) {
        children.remove(ch);
        ch.removeItemFromListeners();
    }

    public BaseItems getParent() {
        return parent;
    }

    private void setParent(BaseItems pr) {
        parent = pr;
    }

    private SizePolicy widthPolicy;
    public SizePolicy getWidthPolicy() { return widthPolicy; }
    public void setWidthPolicy(SizePolicy widthPolicy) {
        if (this.widthPolicy != widthPolicy) {
            this.widthPolicy = widthPolicy;
            if (getParent() != null) { //if it's have not parent, then nobody cares
                if (widthPolicy == SizePolicy.Fixed)
                    getParent().removeEventListener(EventManager.RESIZE_WIDTH, this);
                else
                    getParent().addEventListener(EventManager.RESIZE_WIDTH, this); //Это не очень хорошо, но в EventManager есть проверка
            }
        }
    }

    private SizePolicy heightPolicy;
    public SizePolicy getHeightPolicy() { return heightPolicy; }
    public void setHeightPolicy(SizePolicy heightPolicy) {
        if (this.heightPolicy != heightPolicy) {
            this.heightPolicy = heightPolicy;
            if (getParent() != null) { //if it's have not parent, then nobody cares
                if (heightPolicy == SizePolicy.Fixed)
                    getParent().removeEventListener(EventManager.RESIZE_HEIGHT, this);
                else
                    getParent().addEventListener(EventManager.RESIZE_HEIGHT, this); //Это не очень хорошо, но в EventManager есть проверка
            }
        }
    }

    private int width;
    public int getWidth() { return width; }
    public void setWidth(int width) {
        int value = width - this.width;
        if (value != 0) {
            this.width = width;
            eventManager.notifyListeners(EventManager.RESIZE_WIDTH, value);
        }
    }

    private int height;
    public int getHeight() { return height; }
    public void setHeight(int height) {
        int value = height - this.height;
        if (value != 0) {
            this.height = height;
            eventManager.notifyListeners(EventManager.RESIZE_HEIGHT, value);
        }
    }

    private int xPosition;
    public int getXPosition() { return xPosition; }
    public void setXPosition(int xPosition) {
        int value = xPosition - this.xPosition;
        if (value != 0) {
            this.xPosition = xPosition;
            eventManager.notifyListeners(EventManager.MOVED_X, value);
        }
    }

    private int yPosition;
    public int getYPosition() { return yPosition; }
    public void setYPosition(int yPosition) {
        int value = yPosition - this.yPosition; //TODO возможно должно быть наоборот
        if (value != 0) {
            this.yPosition = yPosition;
            eventManager.notifyListeners(EventManager.MOVED_Y, value);
        }
    }

    public void addEventListener(int eventType, BaseItems listener) {
        eventManager.subscribe(eventType, listener);
    }

    public void removeEventListener(int eventType, BaseItems listener) {
        eventManager.unsubscribe(eventType, listener);
    }

    //Remove item from the lists of its parent's listeners
    public void removeItemFromListeners() {
        getParent().removeEventListener(EventManager.RESIZE_WIDTH, this);
        getParent().removeEventListener(EventManager.RESIZE_HEIGHT, this);
        getParent().removeEventListener(EventManager.MOVED_X, this);
        getParent().removeEventListener(EventManager.MOVED_Y, this);
    }

    @Override
    public void update(int eventType, int value) {
        System.out.print("Element " + name + " was ");
        switch (eventType) {
            case EventManager.MOVED_X :
                System.out.println("moved along X on " + value);
                setXPosition(getXPosition() + value);
                break;

            case EventManager.MOVED_Y :
                System.out.println("moved along Y on " + value);
                setYPosition(getYPosition() + value);
                break;

            case EventManager.RESIZE_WIDTH :
                System.out.println("change its width on " + value);
                setWidth(getWidth() + value);
                break;

            case EventManager.RESIZE_HEIGHT:
                System.out.println("change its height on " + value);
                setHeight(getHeight() + value);
                break;

            default :
                System.out.println("I don't know this event");
                break;
        }
    }
}
