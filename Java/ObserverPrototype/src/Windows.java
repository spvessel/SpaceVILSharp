import java.util.ArrayList;
import java.util.List;

public class Windows extends BaseItems {
    public static List<Windows> allWindows = new ArrayList<>();

    private boolean isFocused = false;

    public Windows(String name) {
        super(name);

        allWindows.forEach(w -> addEventListener(EventManager.FOCUSED, w));
        allWindows.forEach(w -> w.addEventListener(EventManager.FOCUSED, this));
        allWindows.add(this);
    }

    @Override
    public void update(int eventType, int value) {
        if (eventType == EventManager.FOCUSED)  {
            System.out.println("Some window was focused. Window " + name + " will be unfocused");
            setFocused(false);
        } else {
            super.update(eventType, value);//eventManager.notifyListeners(eventType);
        }
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
        if (isFocused) eventManager.notifyListeners(EventManager.FOCUSED, 0);
    }

/* Это может вызваться при сменеродительского элемента, а так быть не должно
    @Override
    public void removeItem() {
        super.removeItem();
        allWindows.remove(this);
        allWindows.forEach(w -> w.removeEventListener("Someone focused", this));
    }
*/
}
