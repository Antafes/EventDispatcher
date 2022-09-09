package scripts.laniax.framework.event_dispatcher;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is the dispatcher that dispatches various events to any subscribed listener.
 *
 * @author Laniax
 */
public class Dispatcher
{
    private HashMap<Class<? extends Event>, List<EventListener<? extends Event>>> listeners;

    private Dispatcher()
    {
        listeners = new HashMap<>();
    }

    public static Dispatcher getInstance()
    {
        return new Dispatcher();
    }

    public void destroy()
    {
        listeners = null;
    }

    public <T extends Event> T dispatch(T event)
    {
        List<EventListener<? extends Event>> listeners = getListeners(event.getClass());

        if (listeners.size() > 0) {

            for (EventListener listener : listeners) {
                if (event.isPropagationStopped())
                    break;

                listener.invoke(event);
            }
        }

        return event;
    }

    public List<EventListener<? extends Event>> getListeners()
    {
        return getListeners(null);
    }

    public List<EventListener<? extends Event>> getListeners(Class<? extends Event> event)
    {
        if (event != null) {

            List<EventListener<? extends Event>> currentListenersSet = listeners.get(event);

            if (currentListenersSet == null) {
                return new ArrayList<>();
            }

            currentListenersSet.sort(Comparator.<EventListener<? extends Event>>comparingInt(EventListener::getPriority).reversed());
            return currentListenersSet;
        }

        List<EventListener<? extends Event>> result = new CopyOnWriteArrayList<>();
        listeners.values().forEach(result::addAll);

        result.sort(Comparator.<EventListener<? extends Event>>comparingInt(EventListener::getPriority).reversed());

        return result;
    }

    public Dispatcher addListener(Class<? extends Event> event, EventListener<? extends Event> listener)
    {
        return addListener(event, listener, null);
    }

    public Dispatcher addListener(Class<? extends Event> event, EventListener<? extends Event> listener, Integer priority)
    {
        if (priority != null) {
            listener.setPriority(priority);
        }

        List<EventListener<? extends Event>> currentListenersSet = listeners.get(event);

        if (currentListenersSet == null) {
            currentListenersSet = new CopyOnWriteArrayList<>();
        } else if (currentListenersSet.contains(listener))
            return this;

        currentListenersSet.add(listener);

        listeners.put(event, currentListenersSet);

        return this;
    }

    public Dispatcher removeListener(Class<? extends Event> event, EventListener<? extends Event> listener)
    {
        List<EventListener<? extends Event>> currentListenersSet = listeners.get(event);

        if (currentListenersSet == null)
            return this;

        currentListenersSet.remove(listener);

        if (currentListenersSet.size() == 0) {
            listeners.remove(event);
        } else {
            listeners.put(event, currentListenersSet);
        }

        return this;
    }
}
