import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    public final static int FOCUSED = 0;
    public final static int MOVED_X = 1;
    public final static int MOVED_Y = 2;
    public final static int RESIZE_WIDTH = 3;
    public final static int RESIZE_HEIGHT = 4;

    Map<Integer, List<EventListener>> listeners = new HashMap<>(); //String may be replaced to Enum

    public void setListeners(int... events) { //not necessary
        for (int s : events)
            listeners.put(s, new ArrayList<>());
    }

    public void subscribe(int eventType, EventListener listener) {
        if (!listeners.containsKey(eventType)) listeners.put(eventType, new ArrayList<>());
        if (!listeners.get(eventType).contains(listener))
            listeners.get(eventType).add(listener);
    }

    public void unsubscribe(int eventType, EventListener listener) {
        if (listeners.get(eventType).contains(listener)) //На всякий случай
            listeners.get(eventType).remove(listener);
    }

    public void notifyListeners(int eventType, int value) {
        //listeners.get(eventType).forEach(s -> s.update(eventType));
        if (listeners.containsKey(eventType)) {
            for (EventListener el : listeners.get(eventType))
                el.update(eventType, value);
        }
    }
}
