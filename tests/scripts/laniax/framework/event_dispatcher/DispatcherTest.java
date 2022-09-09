package scripts.laniax.framework.event_dispatcher;

import antafes.eventDispatcher.Application;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Laniax
 */
@Test
public class DispatcherTest
{
    private Dispatcher dispatcher;

    @BeforeMethod
    public void setUp()
    {
        dispatcher = Application.getDispatcher();
    }

    @AfterMethod
    public void tearDown()
    {
        dispatcher.destroy();
        dispatcher = null;
    }

    public void dispatch()
    {
        Assert.assertEquals(0, dispatcher.getListeners().size());

        List<String> invoked = new ArrayList<>();

        TestEventListener listener1 = new TestEventListener(event -> invoked.add("first"));
        TestEventListener listener2 = new TestEventListener(event -> invoked.add("second"));
        TestEventListener listener3 = new TestEventListener(event -> invoked.add("third"));

        dispatcher.addListener(Event.class, listener1, -10);
        dispatcher.addListener(Event.class, listener2);
        dispatcher.addListener(Event.class, listener3, 10);

        TestEvent event = new TestEvent();

        Assert.assertEquals(event, dispatcher.dispatch(event));

        Assert.assertEquals("third", invoked.get(0));
        Assert.assertEquals("second", invoked.get(1));
        Assert.assertEquals("first", invoked.get(2));
    }

    public void getAllListeners()
    {
        Assert.assertEquals(0, dispatcher.getListeners().size());

        // Check priority
        TestEventListener listener1 = new TestEventListener(event -> System.out.println("Hello, first event"), 2);
        TestEventListener listener2 = new TestEventListener(event -> System.out.println("Hello, second event"));
        TestEventListener listener3 = new TestEventListener(event -> System.out.println("Hello, third event"));

        dispatcher.addListener(Event.class, listener1);
        dispatcher.addListener(Event.class, listener2, 3);
        dispatcher.addListener(Event.class, listener3, 1);

        List<EventListener<? extends Event>> listeners = dispatcher.getListeners(Event.class);

        Assert.assertEquals(listener2, listeners.get(0));
        Assert.assertEquals(listener1, listeners.get(1));
        Assert.assertEquals(listener3, listeners.get(2));
    }

    public void getSpecificListeners()
    {
        Assert.assertEquals(0, dispatcher.getListeners().size());

        // Check priority
        TestEventListener listener1 = new TestEventListener(event -> System.out.println("Hello, first event"), 2);
        TestEventListener listener2 = new TestEventListener(event -> System.out.println("Hello, second event"));
        TestEventListener listener3 = new TestEventListener(event -> System.out.println("Hello, third event"));
        TestEventListener listener4 = new TestEventListener(event -> System.out.println("Hello, fourth event"));

        class OtherEvent extends Event {}

        dispatcher.addListener(Event.class, listener1);
        dispatcher.addListener(Event.class, listener2, 3);
        dispatcher.addListener(Event.class, listener3, 1);
        dispatcher.addListener(Event.class, listener4, 4);

        dispatcher.addListener(OtherEvent.class, listener1, 3);
        dispatcher.addListener(OtherEvent.class, listener2, 1);
        dispatcher.addListener(OtherEvent.class, listener3, 2);

        List<EventListener<? extends Event>> listeners = dispatcher.getListeners(OtherEvent.class);

        Assert.assertEquals(3, listeners.size());

        Assert.assertEquals(listener1, listeners.get(0));
        Assert.assertEquals(listener3, listeners.get(1));
        Assert.assertEquals(listener2, listeners.get(2));
    }

    public void addListener()
    {
        Assert.assertEquals(0, dispatcher.getListeners().size());

        TestEventListener listener = new TestEventListener(event -> System.out.println("Hello, first event"));

        dispatcher.addListener(Event.class, listener);

        Assert.assertEquals(1, dispatcher.getListeners().size());
        Assert.assertTrue(dispatcher.getListeners().contains(listener));

        dispatcher.addListener(Event.class, listener); // adding the same listener for the same event type should do nothing

        Assert.assertEquals(1, dispatcher.getListeners().size());
        Assert.assertTrue(dispatcher.getListeners().contains(listener));

        TestEventListener listener2 = new TestEventListener(event -> System.out.println("Hello, second event"));

        Assert.assertNotEquals(listener, listener2);

        dispatcher.addListener(Event.class, listener2);

        Assert.assertEquals(2, dispatcher.getListeners().size());
    }

    public void removeListener()
    {
        Assert.assertEquals(0, dispatcher.getListeners().size());

        TestEventListener listener = new TestEventListener(event -> System.out.println("Hello, first event"));
        TestEventListener listener2 = new TestEventListener(event -> System.out.println("Hello, second event"));
        dispatcher.addListener(Event.class, listener);
        dispatcher.addListener(Event.class, listener2);

        Assert.assertEquals(2, dispatcher.getListeners().size());
        Assert.assertTrue(dispatcher.getListeners().contains(listener));

        dispatcher.removeListener(Event.class, listener);

        Assert.assertEquals(1, dispatcher.getListeners().size());
        Assert.assertFalse(dispatcher.getListeners().contains(listener));

        dispatcher.removeListener(Event.class, listener2);

        Assert.assertEquals(0, dispatcher.getListeners().size());
        Assert.assertFalse(dispatcher.getListeners().contains(listener2));
    }
}
