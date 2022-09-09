package scripts.laniax.framework.event_dispatcher;

import java.util.function.Consumer;

public class TestEventListener extends EventListener<TestEvent>
{
    public TestEventListener()
    {
    }

    public TestEventListener(Consumer<TestEvent> consumer)
    {
        super(consumer);
    }

    public TestEventListener(Consumer<TestEvent> consumer, int priority)
    {
        super(consumer, priority);
    }
}
