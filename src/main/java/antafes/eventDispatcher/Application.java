package antafes.eventDispatcher;

import scripts.laniax.framework.event_dispatcher.Dispatcher;

/**
 * @author Marian Pollzien
 */
abstract public class Application
{
    private static volatile Dispatcher dispatcher;

    public static Dispatcher getDispatcher()
    {
        if (dispatcher == null) {
            synchronized (Application.class) {
                dispatcher = Dispatcher.getInstance();
            }
        }

        return dispatcher;
    }
}
